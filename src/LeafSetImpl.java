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
	
	private static final int RIGHT = 1;
	private static final int LEFT = 0;
	private Leaf nodeLeaf;
	private Leaf [] leafSet = new Leaf[2*Common.L];
	private int right;
	private int left;
	
	// To test the class
	public static void main (String[] args){
		
		Leaf node = new Leaf(56);
		Leaf [] leafSet = {new Leaf(900), new Leaf(32), null, null};
		LeafSetImpl leafSetImpl = new LeafSetImpl(node, leafSet, 0 , 2);
		System.out.println("isInRange: " + leafSetImpl.isInRange(100));
		System.out.println("Side: " + leafSetImpl.calculateSide(100));
		System.out.println("Position: " + leafSetImpl.calculatePosition(100, leafSetImpl.calculateSide(100)));
	}

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

	// Add a new leaf to the leafSet. It supposes
	// that the node is in range.
	@Override
	public void addLeaf(Leaf leaf) {
		int side = this.calculateSide(leaf.getKey());
		Leaf leaveNode = null;  // Variable to save the node that will leave 
		
		switch (side){
			case RIGHT:
				if (right >= Common.L)
					leaveNode = leafSet[2*Common.L -1];
				break;
			case LEFT:
				if (left >= Common.L)
					leaveNode = leafSet[0];
				break;
		}
		
		int position = this.calculatePosition(leaf.getKey(), side);
		
		// If new node should be placed in one side (min distance) but
		// it is out of range in this side (distance > Max distance (side))
		// and there is free space in the other side, place it there
		if (position == -1 && right < Common.L)
			position = Common.L + right;
		else if (position == 2*Common.L && left < Common.L)
			position = Common.L - 1 - left;
		//TODO
				

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
			
			// To testing
			System.out.println("[" + normKeys[0] +", "+ normKeys[1] +", "+ normKeys[2] +", "+ normKeys[3] + "]");
			
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
		Address result = null;
		
		for (int i=0; i<leafSet.length && result == null; i++)
			if (leafSet[i].getKey() == key)
				result = leafSet[i].getAddress();
		return result;
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
	
	// Function to calculate side where the new node should be placed
	// Return RIGHT or LEFT 
	private int calculateSide (int key){
		int result = -1;
		
		// Normalize LeafSet to the central node
		int [] normKeys = this.normalize();
					
		// To testing
		System.out.println("[" + normKeys[0] +", "+ normKeys[1] +", "+ normKeys[2] +", "+ normKeys[3] + "]");
					
		// Normalize new node key to the central node
		int normNewNode = key - nodeLeaf.getKey();
		// if < 0 -> add MAX_ADDR
		if (normNewNode < 0)
			normNewNode += Common.MAX_ADDR;

		// Calculate distances to the new node
		int rightDist = normNewNode;
		int leftDist = Common.MAX_ADDR - normNewNode;

		// Return the side
		if (rightDist < leftDist)
			result = RIGHT;
		else 
			result = LEFT;
			
		return result;
	}
	
	private int calculatePosition (int key, int side){
		int result = 0;
		Boolean stop = false;
		
		// Normalize new node key to the central node
		int normNewNode = key - nodeLeaf.getKey();
		
		// Normalize LeafSet to the central node
		int [] normKeys = this.normalize();
		// if < 0 -> add MAX_ADDR
		if (normNewNode < 0)
			normNewNode += Common.MAX_ADDR;

		switch (side){
			case RIGHT:
				int rightDist = normNewNode;
				
				int i = Common.L + right;
				
				while (i > Common.L  && !stop) {
					if (rightDist <  normKeys[i-1])
						i--;
					else 
						stop = true;
				}
				result = i;
				break;
				
			case LEFT:
				int leftDist = Common.MAX_ADDR - normNewNode;
			
				int j = Common.L - left - 1;
				while (j < Common.L - 1 && !stop) {
					if (leftDist < (Common.MAX_ADDR - normKeys[j+1]))
						j++;
					else 
						stop = true;
				}
				result = j;
				break;
		}

		return result;
	}

}
