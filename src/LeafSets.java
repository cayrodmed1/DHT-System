import org.jgroups.Address;

/**
 * 
 */

/**
 * @author Cayetano
 *
 */
public interface LeafSets {

	public void addLeaf (Leaf leaf);
	public void removeLeaf (int key);
	public Leaf closestLeaf (int key);
	public Address getAddressByKey (int key);
	
}
