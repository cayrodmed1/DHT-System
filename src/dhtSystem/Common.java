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
	public final static int PUT_DATA = 4;
	public final static int GET_DATA = 5;
	public final static int DATA = 6;
	public final static int REMOVE_DATA = 7;
	public final static int BYE = 8;
}
