/**
 * 
 */
package dhtSystem.messages;

import java.io.Serializable;

import dhtSystem.Data;


/**
 * @author Alejandro Rodriguez Calzado
 *
 */
public class PutDataMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	private Data data;
	
	/**
	 * @param data
	 */
	public PutDataMessage(Data data) {
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public Data getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Data data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return String.format("PutDataMessage [data=%s]", data);
	}
	
	
}
