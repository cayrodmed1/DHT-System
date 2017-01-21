import org.jgroups.Address;

/**
 * 
 */

/**
 * @author Cayetano
 *
 */
public interface LeafSet {

	public void addLeaf (Leaf leaf);
	public void removeLeaf (int key);
	public Boolean isInRange (int key);
	public Address getAddressByKey (int key);
	
}
