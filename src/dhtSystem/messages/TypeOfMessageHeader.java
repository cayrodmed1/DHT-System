package dhtSystem.messages;
import java.io.DataInput;
import java.io.DataOutput;
import java.util.function.Supplier;

import org.jgroups.Global;
import org.jgroups.Header;

import dhtSystem.Common;

/**
 * 
 */

/**
 * @author alejandro
 *
 */
public class TypeOfMessageHeader extends Header {
	int type = 0;

	/**
	 * 
	 */
	public TypeOfMessageHeader() {
	}

	/**
	 * @param type
	 */
	public TypeOfMessageHeader(int type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	@Override
	public void readFrom(DataInput in) throws Exception {
		type = in.readInt();
	}

	@Override
	public void writeTo(DataOutput out) throws Exception {
		out.writeInt(type);
	}

	@Override
	public int size() {
		return Global.INT_SIZE;
	}
	
	@Override
	public String toString() {
		return String.format("TypeOfMessageHeader [type=%s]", type);
	}

	@Override
	public Supplier<? extends Header> create() {
		return TypeOfMessageHeader::new;
	}

	@Override
	public short getMagicId() {
		return Common.TYPE_HEADER_MAGIC_ID;
	}

}
