/**
 * 
 */
package dhtSystem.messages;

import java.io.Serializable;

import org.jgroups.Address;

/**
 * @author Alejandro Rodriguez Calzado
 *
 */
public class GetStateMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	private int key;
	private Address address;
	
	/**
	 * @param key
	 * @param address
	 */
	public GetStateMessage(int key, Address address) {
		this.key = key;
		this.address = address;
	}

	/**
	 * @return the key
	 */
	public int getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(int key) {
		this.key = key;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return String.format("GetStateMessage [key=%s, address=%s]", key, address);
	}
	
	
}
