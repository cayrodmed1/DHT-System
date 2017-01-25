package dhtSystem.messages;

import java.io.Serializable;

/**
 * @author Alejandro Rodriguez Calzado
 *
 */
public class RemoveDataMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	private int key;
	
	/**
	 * @param key
	 */
	public RemoveDataMessage(int key) {
		this.key = key;
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

	@Override
	public String toString() {
		return String.format("RemoveDataMessage [key=%s]", key);
	}
}
