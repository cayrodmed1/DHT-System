import org.jgroups.Address;

/**
 * @author Alejandro Rodríguez Calzado
 *
 */
public class Leaf {
	private Address address;
	private int key;
	
	
	/**
	 * @param address
	 */
	public Leaf(Address address) {
		this.address = address;
		key = Math.abs(address.hashCode() % Common.MAX_ADDR);
	}
	
	public Leaf(int key){
		this.address = null;
		this.key = key;
	}
	
	
	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}
	
	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}
	
	/**
	 * @return the key
	 */
	public int getKey() {
		return key;
	}
	
	/**
	 * @param key the key to set
	 */
	public void setKey(int key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return String.format("Leaf [address=%s, key=%s]", address, key);
	}
	
	
}
