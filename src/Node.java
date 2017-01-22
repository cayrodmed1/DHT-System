import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.conf.ClassConfigurator;


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
		
		// To testing 
		if (node.getView().size() > 1){
			Address first=node.getView().getMembers().get(0);
			Message msg=new Message(first, "Hello world");
			msg.putHeader(Common.TYPE_HEADER_MAGIC_ID, new TypeOfMessageHeader(5));
			node.getChannel().send(msg);
		}
	}

	/**
	 * 
	 */
	public Node() {
        String props="tcp.xml";
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
		String line= msg.getHeader(Common.TYPE_HEADER_MAGIC_ID) + "[" + msg.getSrc() + "]: " + msg.getObject();
		System.out.println(line);
	}
}
