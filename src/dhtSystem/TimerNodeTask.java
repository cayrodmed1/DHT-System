/**
 * 
 */
package dhtSystem;

import java.util.TimerTask;

/**
 * @author Alejandro Rodriguez Calzado
 *
 */
public class TimerNodeTask extends TimerTask {

	Node node;
	
	public TimerNodeTask(Node node) {
		this.node = node;
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

	@Override
	public void run() {
		node.keepAlive();
	}
}
