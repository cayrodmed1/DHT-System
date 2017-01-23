/**
 * 
 */
package dhtSystem;

import java.io.Serializable;

/**
 * @author Alejandro Rodriguez Calzado
 *
 */
public class Data implements Serializable {

	private String id;
	private int key;
	private String content;
	
	/**
	 * @param id
	 */
	public Data(String id) {
		this.id = id;
		key = Math.abs(id.hashCode() % Common.MAX_ADDR);
	}
	
	/**
	 * @param id
	 * @param content
	 */
	public Data(String id, String content) {
		this.id = id;
		key = Math.abs(id.hashCode() % Common.MAX_ADDR);
		this.content = content;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return String.format("Data [id=%s, key=%s, content=%s]", id, key, content);
	}
	


}
