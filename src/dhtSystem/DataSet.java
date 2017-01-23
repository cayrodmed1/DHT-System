package dhtSystem;

import java.util.HashMap;

public interface DataSet {
	public Data getData (int key);
	public void addData (Data newData);
	public void removeData (int key);
	public Boolean isInDataSet (int key);
	public HashMap<Integer, Data> getClosestDataTo (int key, LeafSet leafSet);
}
