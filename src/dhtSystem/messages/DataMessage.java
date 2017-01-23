
package dhtSystem.messages;

import java.io.Serializable;

import dhtSystem.Data;

/**
 * @author Alejandro Rodriguez Calzado
 *
 */
public class DataMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	private Data data;
	
	/**
	 * @param data
	 */
	public DataMessage(Data data) {
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
		return String.format("DataMessage [data=%s]", data);
	}
	

	

}
