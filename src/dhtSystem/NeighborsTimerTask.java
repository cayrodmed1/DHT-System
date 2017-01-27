/**
 * 
 */
package dhtSystem;

import java.util.TimerTask;

/**
 * @author Alejandro Rodriguez Calzado
 *
 */
public class NeighborsTimerTask extends TimerTask {

	Node node;
	int neighborKey;

	/**
	 * @param node
	 * @param neighborKey
	 */
	public NeighborsTimerTask(Node node, int neighborKey) {
		this.node = node;
		this.neighborKey = neighborKey;
	}

	/**
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * @param node the node to set
	 */
	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * @return the neighborKey
	 */
	public int getNeighborKey() {
		return neighborKey;
	}

	/**
	 * @param neighborKey the neighborKey to set
	 */
	public void setNeighborKey(int neighborKey) {
		this.neighborKey = neighborKey;
	}

	@Override
	public void run() {
		node.deadNode(neighborKey);
	}

}
