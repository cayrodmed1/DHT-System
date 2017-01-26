package dhtSystem.messages;

import java.io.Serializable;

import dhtSystem.DataSet;
import dhtSystem.LeafSet;

/**
 * @author Alejandro Rodriguez Calzado
 *
 */
public class StateMessage implements Serializable {

	private int nodeKey;
	private LeafSet leafSet;
	private DataSet dataSet;
	
	/**
	 * @param nodeKey
	 */
	public StateMessage(int nodeKey) {
		this.nodeKey = nodeKey;
		this.leafSet = null;
		this.dataSet = null;
	}
	
	/**
	 * @param nodeKey
	 * @param leafSet
	 * @param dataSet
	 */
	public StateMessage(int nodeKey, LeafSet leafSet, DataSet dataSet) {
		this.nodeKey = nodeKey;
		this.leafSet = leafSet;
		this.dataSet = dataSet;
	}

	/**
	 * @return the nodeKey
	 */
	public int getNodeKey() {
		return nodeKey;
	}

	/**
	 * @param nodeKey the nodeKey to set
	 */
	public void setNodeKey(int nodeKey) {
		this.nodeKey = nodeKey;
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
		return String.format("StateMessage [nodeKey=%s, leafSet=%s, dataSet=%s]", nodeKey, leafSet, dataSet);
	}
}
