package dhtSystem;

import java.util.Scanner;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.conf.ClassConfigurator;

import dhtSystem.messages.DataMessage;
import dhtSystem.messages.GetDataMessage;
import dhtSystem.messages.TypeOfMessageHeader;


/**
 * @author Alejandro Rodriguez Calzado
 *
 */
public class DHT extends ReceiverAdapter {
	private static DHT instance = null;
	
	private static final Scanner scanner = new Scanner(System.in);
	private static int select = -1;
	
	private JChannel channel;
	private View view;
	
	//TODO Allow the user to set the requested node by command line options

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DHT dht = DHT.getInstance();
		
		synchronized (dht) {
			while(select != 0){

				try{
					System.out.println("Choose an option:\n1.- Get data" +
							"\n2.- Put data\n" +
							"0.- Exit");

					select = Integer.parseInt(scanner.nextLine()); 


					switch(select){
					case 1: 
						dht.getData();
						dht.wait();
						break;
					case 2: 
						dht.putData();
						break;
					case 0: 
						System.out.println("Closing DHT agent...");
						dht.exit();
						break;
					default:
						System.out.println("This option isn't available");
						break;
					}

					System.out.println("\n"); 

				}catch(Exception e){
					System.out.println("Error!");
				}
			}
		}
	}

	private DHT() {
		String props="mping.xml"; 
        String name=null;
        
        try {
        	// Connect to the group
        	this.start(props, name);
        } catch (Exception e) {
        	System.err.println("Error during connection.");
        }
        
        ClassConfigurator.add(Common.TYPE_HEADER_MAGIC_ID, TypeOfMessageHeader.class);
	}
	
	public static DHT getInstance(){
		if (instance == null)
			instance = new DHT();
		
		return instance;
	}
	
	public  void getData() {
		System.out.println("Id of the data you want to get: ");
		String id = scanner.nextLine();
		int key = Math.abs(id.hashCode() % Common.MAX_ADDR);
		System.out.println(key);
		sendGetDataMessage (channel.getAddress(), view.getMembers().get(0), key);
	}
	
	public  void putData(){
		
	}
	
	private void sendGetDataMessage (Address srcAddress, Address destAddress, int key){
		Message msg=new Message(destAddress, new GetDataMessage(key, srcAddress));
		msg.putHeader(Common.TYPE_HEADER_MAGIC_ID, new TypeOfMessageHeader(Common.GET_DATA));
		try{
			channel.send(msg);
		} catch (Exception e) {
			System.err.println("Error sending GetDataMessage.");
		}
	}
	
	private Data dataReceived (DataMessage dataMsg){
		return dataMsg.getData();
	}
	
	public void viewAccepted (View new_view) {
		view = new_view;
		//System.out.println(view);
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

		int type = ((TypeOfMessageHeader) msg.getHeader(Common.TYPE_HEADER_MAGIC_ID)).getType();
		DHT dht = DHT.getInstance();

		synchronized (dht){
			switch (type){
			case Common.DATA:
				Data dataReceived = dataReceived((DataMessage) msg.getObject());
				if (dataReceived != null)
					System.out.println(dataReceived);
				else
					System.out.println("The requested data doesn't exist in the DHT cluster.");
				dht.notify();
				break;
			}

		}
	}
	
	public void exit (){
		channel.close();
	}
}
