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

	public Leaf getNodeLeaf();
	public void setNodeLeaf(Leaf nodeLeaf);
	public Leaf [] getLeafSet ();
	public void setLeafSet(Leaf[] leafSet);
	public void addLeaf (Leaf leaf);
	public void removeLeaf (int key);
	public Boolean isInRange (int key);
	public Address getAddressByKey (int key);
	public Leaf closestLeaf (int key);
	public Leaf closestLeafToNodeLeaf();
}
