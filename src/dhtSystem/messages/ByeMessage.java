package dhtSystem.messages;

import java.io.Serializable;

import dhtSystem.DataSet;
import dhtSystem.LeafSet;

/**
 * @author Alejandro Rodriguez Calzado
 *
 */
public class ByeMessage implements Serializable {
	
	private LeafSet leafSet;
	private DataSet dataSet;
	
	/**
	 * @param leafSet
	 * @param dataSet
	 * @param key
	 */
	public ByeMessage(LeafSet leafSet, DataSet dataSet) {
		this.leafSet = leafSet;
		this.dataSet = dataSet;
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


	@Override
	public String toString() {
		return String.format("ByeMessage [leafSet=%s, dataSet=%s]", leafSet, dataSet);
	}
	


}
