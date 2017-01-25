/**
 * 
 */
package dhtSystem;

/**
 * @author Alejandro Rodriguez Calzado
 *
 */
public class ShutdownHelper extends Thread {
	Node node;

	public ShutdownHelper(Node node) {
		super();
		this.node = node;
	}


	public void run() {
		try {
			node.shutdown();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
