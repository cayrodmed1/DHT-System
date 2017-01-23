/**
 * 
 */
package dhtSystem.messages;

import java.io.Serializable;

import dhtSystem.Leaf;

/**
 * @author Alejandro Rodriguez Calzado
 *
 */
public class NewNodeMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private Leaf leaf;
	
	/**
	 * @param leaf
	 */
	public NewNodeMessage(Leaf leaf) {
		this.leaf = leaf;
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


	@Override
	public String toString() {
		return String.format("NewNodeMessage [leaf=%s]", leaf);
	}

}
