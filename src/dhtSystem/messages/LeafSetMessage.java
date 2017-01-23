/**
 * 
 */
package dhtSystem.messages;

import java.io.Serializable;

import dhtSystem.LeafSet;

/**
 * @author Alejandro Rodriguez Calzado
 *
 */
public class LeafSetMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private LeafSet leafSet;
	
	/**
	 * @param leafSet
	 */
	public LeafSetMessage(LeafSet leafSet) {
		this.leafSet = leafSet;
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

	@Override
	public String toString() {
		return String.format("LeafSetMessage [leafSet=%s]", leafSet);
	}
}
