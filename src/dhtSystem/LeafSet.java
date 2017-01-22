package dhtSystem;
import org.jgroups.Address;

/**
 * 
 */

/**
 * @author Cayetano Rodriguez Medina
 *
 */
public interface LeafSet {

	public void addLeaf (Leaf leaf);
	public void removeLeaf (int key);
	public Boolean isInRange (int key);
	public Address getAddressByKey (int key);
	public Leaf closestLeaf (int key);
}
