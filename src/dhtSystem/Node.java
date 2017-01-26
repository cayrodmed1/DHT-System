package dhtSystem;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.conf.ClassConfigurator;

import dhtSystem.messages.ByeMessage;
import dhtSystem.messages.DataMessage;
import dhtSystem.messages.GetDataMessage;
import dhtSystem.messages.JoinMessage;
import dhtSystem.messages.TypeOfMessageHeader;
import dhtSystem.messages.LeafSetMessage;
import dhtSystem.messages.NewNodeMessage;
import dhtSystem.messages.PutDataMessage;
import dhtSystem.messages.RemoveDataMessage;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * @author Alejandro Rodriguez Calzado
 *
 */
public class Node extends ReceiverAdapter{
	private Leaf ownLeaf;
	private LeafSet leafSet;
	
	private DataSet dataSet;
	
	private JChannel channel;
	private View view;
	
	private static final Logger log = Common.log;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
	
		Node node = new Node();
		
		ShutdownHelper shutdownHelper = new ShutdownHelper(node);
        Runtime.getRuntime().addShutdownHook(shutdownHelper);
	}

	/**
	 * 
	 */
	public Node() {
		// For logging with log4j
		BasicConfigurator.configure();
		
		/*
		 * tcp.xml lists only localhost[7800] and localhost[7801] 
		 * for initial discovery in TCPPING. If you have to use TCP, then you must 
		 * list *all* members of your cluster in TCPPING ! 
		 * Use MPING for dynamic discovery if you can't know all of your members 
		 * beforehand.
		 * 
		 * MPING: TCP-based stack with IP multicast-based discovery
		 */ 
        String props="mping.xml"; 
        String name=null;
        
        try {
        	// Connect to the group
        	this.start(props, name);
        } catch (Exception e) {
        	log.error("Error during connection.");
        }
        
        // Create my own leaf
        ownLeaf = new Leaf(channel.getAddress());
        
        // Create an empty leafSet
        leafSet = new LeafSetImpl(ownLeaf);
        
        // Create an empty dataSet
        dataSet = new DataSetImpl ();
        
        ClassConfigurator.add(Common.TYPE_HEADER_MAGIC_ID, TypeOfMessageHeader.class);
        
        if (getView().size() > 1)
        	join(getView().getMembers().get(0), ownLeaf);
	}

	/**
	 * @return the ownLeaf
	 */
	public Leaf getOwnLeaf() {
		return ownLeaf;
	}

	/**
	 * @param ownLeaf the leaf to set
	 */
	public void setOwnLeaf(Leaf leaf) {
		this.ownLeaf = leaf;
	}

	/**
	 * @return the leafSet
	 */
	public LeafSet getLeafSet() {
		return leafSet;
	}

	/**
	 * @param leafSet the leafSet to set
	 */
	public void setLeafSet(LeafSet leafSet) {
		this.leafSet = leafSet;
	}
	
	/**
	 * @return the dataSet
	 */
	public DataSet getDataSet() {
		return dataSet;
	}

	/**
	 * @param dataSet the dataSet to set
	 */
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * @return the channel
	 */
	public JChannel getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(JChannel channel) {
		this.channel = channel;
	}

	/**
	 * @return the view
	 */
	public View getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(View view) {
		this.view = view;
	}

	public void viewAccepted (View new_view) {
		view = new_view;
		log.info("** view: " + new_view);
	}
	
	private void start(String props, String name) throws Exception {
		view = null;
		channel=new JChannel(props);
		if(name != null)
			channel.name(name);
		channel.setReceiver(this);
		channel.connect("DHTCluster");
	}

	public void receive(Message msg) {
		// To testing
		//String line= msg.getHeader(Common.TYPE_HEADER_MAGIC_ID) + "[" + msg.getSrc() + "]: " + msg.getObject();
		//log.info(line);
		int type = ((TypeOfMessageHeader) msg.getHeader(Common.TYPE_HEADER_MAGIC_ID)).getType();
		switch (type){
		case Common.JOIN:
			log.info("JOIN RECEIVED");
			joinReceived((JoinMessage) msg.getObject());
			break;
		case Common.NEW_NODE:
			log.info("NEW NODE RECEIVED");
			newNodeReceived((NewNodeMessage) msg.getObject());
			log.info(leafSet);
			log.info(dataSet);
			break;
		case Common.LEAF_SET:
			log.info("LEAF SET RECEIVED");
			leafSetReceived((LeafSetMessage) msg.getObject());
			log.info(leafSet);
			break;
		case Common.PUT_DATA:
			log.info("PUT DATA RECEIVED");
			putDataReceived((PutDataMessage) msg.getObject());
			break;
		case Common.GET_DATA:
			log.info("GET DATA RECEIVED");
			getDataReceived((GetDataMessage) msg.getObject());
			break;
		case Common.DATA:
			log.info("DATA RECEIVED");
			Data dataReceived = dataReceived((DataMessage) msg.getObject());
			log.info(dataReceived);
			break;
		case Common.REMOVE_DATA:
			log.info("REMOVE DATA RECEIVED");
			removeDataReceived((RemoveDataMessage) msg.getObject());
			log.info(dataSet);
			break;
		case Common.BYE:
			log.info("BYE RECEIVED");
			byeReceived((ByeMessage) msg.getObject());
			log.info(leafSet);
			log.info(dataSet);
		}
		
		
	}
	
	private void join(Address address, Leaf leaf){
		Message msg=new Message(address, new JoinMessage(leaf));
		msg.putHeader(Common.TYPE_HEADER_MAGIC_ID, new TypeOfMessageHeader(Common.JOIN));
		try{
			channel.send(msg);
		} catch (Exception e) {
			log.error("Error during join.");
		}
	}
	
	private void joinReceived(JoinMessage join){
		Leaf newLeaf = join.getLeaf();
		Leaf closest = leafSet.closestLeaf(newLeaf.getKey());
		if (leafSet.isInRange(newLeaf.getKey()) && closest.getKey() == ownLeaf.getKey()) {
			// Send leafSet to the new node
			sendLeafSet(newLeaf.getAddress(),this.leafSet);
			// Send my own leaf to the new node
			newNode(newLeaf.getAddress(), ownLeaf);
			
			// Send new node message to my leafSet
			for (Leaf l: leafSet.getLeafSet()){
				if (l != null)
					newNode(l.getAddress(), newLeaf);
			}
			
			// Add the new node to my own leafSet
			leafSet.addLeaf(newLeaf);
			
			log.info(leafSet);
			
			//TODO update data
			redistributeData(newLeaf);
			
			log.info(dataSet);
		} else {
			join(closest.getAddress(), newLeaf);
		}
	}
	
	private void newNode(Address address, Leaf leaf){
		Message msg=new Message(address, new NewNodeMessage(leaf));
		msg.putHeader(Common.TYPE_HEADER_MAGIC_ID, new TypeOfMessageHeader(Common.NEW_NODE));
		try{
			channel.send(msg);
		} catch (Exception e) {
			log.error("Error sending NewNodeMessage.");
		}
	}
	
	private void newNodeReceived (NewNodeMessage newNodeMsg){
		Leaf newNode = newNodeMsg.getLeaf();
		if (leafSet.isInRange(newNode.getKey())) {
			leafSet.addLeaf(newNode);
			redistributeData(newNode);
		}
	}
	
	private void sendLeafSet (Address address, LeafSet leafSet){
		Message msg=new Message(address, new LeafSetMessage(leafSet));
		msg.putHeader(Common.TYPE_HEADER_MAGIC_ID, new TypeOfMessageHeader(Common.LEAF_SET));
		try{
			channel.send(msg);
		} catch (Exception e) {
			log.error("Error sending LeafSetMessage.");
		}
	}
	
	private void leafSetReceived (LeafSetMessage leafSetMsg){
		leafSet = leafSetMsg.getLeafSet();
		leafSet.setNodeLeaf(ownLeaf);
	}
	
	private void putData (Address address, Data data){
		Message msg=new Message(address, new PutDataMessage(data));
		msg.putHeader(Common.TYPE_HEADER_MAGIC_ID, new TypeOfMessageHeader(Common.PUT_DATA));
		try{
			channel.send(msg);
		} catch (Exception e) {
			log.error("Error sending PutDataMessage.");
		}
	}
	
	private void putDataReceived (PutDataMessage putDataMsg){
		Data data = putDataMsg.getData();
		Leaf closest = leafSet.closestLeaf(data.getKey());
		if (closest.getKey() == ownLeaf.getKey()) {
			// Add data to dataSet
			dataSet.addData(data);
			
			log.info(dataSet);
			
		} else {
			// forward the message to the closest in your leaf set
			putData(closest.getAddress(), data);
		}
	}
	
	private void getData (Address srcAddress, Address destAddress, int key){
		Message msg=new Message(destAddress, new GetDataMessage(key, srcAddress));
		msg.putHeader(Common.TYPE_HEADER_MAGIC_ID, new TypeOfMessageHeader(Common.GET_DATA));
		try{
			channel.send(msg);
		} catch (Exception e) {
			log.error("Error sending GetDataMessage.");
		}
	}
	
	private void getDataReceived (GetDataMessage getDataMsg){
		int key = getDataMsg.getKey();
		Address srcAddress = getDataMsg.getAddress();
		Leaf closest = leafSet.closestLeaf(key);
		
		if (closest.getKey() == ownLeaf.getKey()) {
			// Look for the data in the data set
			Data data = dataSet.getData(key);
			
			sendData(srcAddress, data);
			
		} else {
			// forward the message to the closest in your leaf set
			getData(srcAddress, closest.getAddress(), key);
		}
	}
	
	private void sendData (Address address, Data data){
		Message msg=new Message(address, new DataMessage(data));
		msg.putHeader(Common.TYPE_HEADER_MAGIC_ID, new TypeOfMessageHeader(Common.DATA));
		try{
			channel.send(msg);
		} catch (Exception e) {
			log.error("Error sending DataMessage.");
		}
	}
	
	private Data dataReceived (DataMessage dataMsg){
		return dataMsg.getData();
	}
	
	private void removeData (Address address, int key){
		Message msg=new Message(address, new RemoveDataMessage(key));
		msg.putHeader(Common.TYPE_HEADER_MAGIC_ID, new TypeOfMessageHeader(Common.REMOVE_DATA));
		try{
			channel.send(msg);
		} catch (Exception e) {
			log.error("Error sending RemoveDataMessage.");
		}
	}
	
	private void removeDataReceived (RemoveDataMessage rmDataMsg){
		int key = rmDataMsg.getKey();
		Leaf closest = leafSet.closestLeaf(key);
		
		if (closest.getKey() == ownLeaf.getKey()) {
			// Look for the data in the data set
			if (dataSet.getData(key) != null) {
				log.info("Removing data: " + dataSet.getData(key));

				// Remove the data
				dataSet.removeData(key);
			} else {
				log.info("Data not found.");
			}
			
		} else {
			// forward the message to the closest in your leaf set
			removeData(closest.getAddress(), key);
		}
	}
	
	private void bye (Address address, LeafSet leafSet, DataSet dataSet){
		Message msg=new Message(address, new ByeMessage(leafSet, dataSet));
		msg.putHeader(Common.TYPE_HEADER_MAGIC_ID, new TypeOfMessageHeader(Common.BYE));
		try{
			channel.send(msg);
		} catch (Exception e) {
			log.error("Error sending Bye Message.");
		}
	}
	
	private void byeReceived (ByeMessage bye){
		LeafSet leafSet = bye.getLeafSet();
		DataSet dataSet = bye.getDataSet();
		
		// Remove leaf that sends BYE from my leafSet
		this.leafSet.removeLeaf(leafSet.getNodeLeaf().getKey());
		
		// Check the leaving node's leaf set and add the closest node to my leaf set
		for (int i = 0; i < leafSet.getLeafSet().length; i++){
			
			if (leafSet.getLeafSet()[i] != null && this.leafSet.isInRange(leafSet.getLeafSet()[i].getKey()))
				this.leafSet.addLeaf(leafSet.getLeafSet()[i]);
		}
		
		// Iterate across the leaving node dataSet 
		Iterator it = dataSet.getKeySet().iterator();
		while (it.hasNext()){
			Integer dataKey = (Integer) it.next();
			Leaf closest = this.leafSet.closestLeaf(dataKey);
			
			// If I'm the closest node to the data, save it in my dataSet
			if (closest.getKey() == ownLeaf.getKey()) {
				// Add data to dataSet
				this.dataSet.addData(dataSet.getData(dataKey));
			}
		}
		log.info(this.dataSet);
	}
	
	
	
	private void redistributeData (Leaf leaf){
		int newKey = leaf.getKey();
		Address address = leaf.getAddress();

		// Calculate the data that is stored in my data set but are closest to the new node
		for (Entry<Integer, Data> entry : dataSet.getClosestDataTo(newKey, leafSet).entrySet())
		{			
			// Send this data to the new node
			putData(address, entry.getValue());
			// Remove data from my data set
			dataSet.removeData(entry.getKey());
		}
		
	}
	
	public void shutdown (){
		log.info("The node is being disconnected from the DHT cluster...");
		
		for (int i = 0; i <leafSet.getLeafSet().length; i++)
			if (leafSet.getLeafSet()[i] != null)
				bye(leafSet.getLeafSet()[i].getAddress(), leafSet, dataSet);
		channel.close();
	}
	
}
