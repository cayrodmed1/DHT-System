import java.util.ArrayList;

import org.jgroups.Address;

/**
 * 
 */

/**
 * @author alejandro
 *
 */
public class LeafSetImpl implements LeafSet {
	
	private Leaf nodeLeaf;
	private Leaf [] leafSet = new Leaf[2*Common.L];
	private int right;
	private int left;

	/**
	 * 
	 */
	public LeafSetImpl() {
		nodeLeaf = null;
		for (int i = 0; i < this.leafSet.length; i++ ) {
			this.leafSet[i] = null;
		}
		right = 0;
		left = 0;
	}

	/**
	 * @param nodeLeaf
	 */
	public LeafSetImpl(Leaf nodeLeaf) {
		this.nodeLeaf = nodeLeaf;
		for (int i = 0; i < this.leafSet.length; i++ ) {
			this.leafSet[i] = null;
		}
		right = 0;
		left = 0;
	}

	/**
	 * @param nodeLeaf
	 * @param leafSet
	 */
	public LeafSetImpl(Leaf nodeLeaf, Leaf [] leafSet, int right, int left) {
		this.nodeLeaf = nodeLeaf;
		for (int i = 0; i < leafSet.length; i++ ) {
			this.leafSet[i] = leafSet[i];
		}
		this.right = right;
		this.left = left;
	}




	/**
	 * @return the nodeLeaf
	 */
	public Leaf getNodeLeaf() {
		return nodeLeaf;
	}

	/**
	 * @param nodeLeaf the nodeLeaf to set
	 */
	public void setNodeLeaf(Leaf nodeLeaf) {
		this.nodeLeaf = nodeLeaf;
	}

	/**
	 * @return the leafSet
	 */
	public Leaf[] getLeafSet() {
		return leafSet;
	}

	/**
	 * @param leafSet the leafSet to set
	 */
	public void setLeafSet(Leaf[] leafSet) {
		this.leafSet = leafSet;
	}

	/**
	 * @return the right
	 */
	public int getRight() {
		return right;
	}

	/**
	 * @param right the right to set
	 */
	public void setRight(int right) {
		this.right = right;
	}

	/**
	 * @return the left
	 */
	public int getLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(int left) {
		this.left = left;
	}

	@Override
	public void addLeaf(Leaf leaf) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeLeaf(int key) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean isInRange(int key) {
		Boolean result = false;
		
		if (right < Common.L || left < Common.L)
			//If there is free space, the node is in the range
			result = true;
		else {
			// Normalize LeafSet to the central node
			int [] normKeys = this.normalize();
			// Normalize new node key to the central node
			int normNewNode = key - nodeLeaf.getKey();
			// if < 0 -> add MAX_ADDR
			if (normNewNode < 0)
				normNewNode += Common.MAX_ADDR;
			
			// Calculate distances to the new node
			int rightDist = normNewNode;
			int leftDist = Common.MAX_ADDR - normNewNode;
						
			// Calculate max distances of our LeafSet
			int maxRight = normKeys[2*Common.L - 1]; 
			int maxLeft = Common.MAX_ADDR -normKeys[0];
			
			// If it fits, return true
			if (rightDist < maxRight || leftDist < maxLeft)
				result = true;
		}
		
		return result;
	}

	@Override
	public Address getAddressByKey(int key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Normalize LeafSet to the central node
	private int [] normalize () {
		int[] normKeys = new int[2*Common.L];
		
		for (int i=0; i<leafSet.length; i++)
			if (leafSet[i] != null) {
				normKeys[i] = leafSet[i].getKey() - nodeLeaf.getKey();
				if (normKeys[i] < 0) 
					normKeys[i] += Common.MAX_ADDR;
			}
			else
				normKeys[i] = 0;
		
		return normKeys;
	}

}
