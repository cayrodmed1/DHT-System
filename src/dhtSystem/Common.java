package dhtSystem;

import org.apache.log4j.Logger;

/**
 * @author Alejandro Rodríguez Calzado
 *
 */
public class Common {
	public static final Logger log = Logger.getLogger(Node.class.getName());
	
	public final static int MAX_ADDR = 1024;
	public final static int L = 2;
	public final static short TYPE_HEADER_MAGIC_ID = 1994;
	public final static int JOIN = 1;
	public final static int NEW_NODE = 2;
	public final static int LEAF_SET = 3;
}
