package dhtSystem;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.conf.ClassConfigurator;

import dhtSystem.messages.JoinMessage;
import dhtSystem.messages.TypeOfMessageHeader;


/**
 * @author alejandro
 *
 */
public class Node extends ReceiverAdapter{
	private Leaf leaf;
	private LeafSet leafSet;
	
	private JChannel channel;
	private View view;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		Node node = new Node();
		System.out.println(node.getLeafSet());
	}

	/**
	 * 
	 */
	public Node() {
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
        	System.err.println("Error during connection.");
        }
        
        // Create my own leaf
        leaf = new Leaf(channel.getAddress());
        
        // Create an empty leafSet
        leafSet = new LeafSetImpl(leaf);
        
        ClassConfigurator.add(Common.TYPE_HEADER_MAGIC_ID, TypeOfMessageHeader.class);
        
        if (getView().size() > 1)
        	join(getView().getMembers().get(0));
	}

	/**
	 * @return the leaf
	 */
	public Leaf getLeaf() {
		return leaf;
	}

	/**
	 * @param leaf the leaf to set
	 */
	public void setLeaf(Leaf leaf) {
		this.leaf = leaf;
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
		System.out.println("** view: " + new_view);
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
		//System.out.println(line);
		int type = ((TypeOfMessageHeader) msg.getHeader(Common.TYPE_HEADER_MAGIC_ID)).getType();
		switch (type){
		case Common.JOIN:
			joinReceived((JoinMessage) msg.getObject());
			break;
		}
	}
	
	private void join(Address address){
		Message msg=new Message(address, new JoinMessage(leaf));
		msg.putHeader(Common.TYPE_HEADER_MAGIC_ID, new TypeOfMessageHeader(Common.JOIN));
		try{
			channel.send(msg);
		} catch (Exception e) {
			System.err.println("Error during join.");
		}
	}
	
	private void joinReceived(JoinMessage join){
		Leaf leaf = join.getLeaf();
		if (leafSet.isInRange(leaf.getKey())) {
			leafSet.addLeaf(leaf);
			System.out.println("LeafSet changed: \n" + leafSet);
		}
	}
}
