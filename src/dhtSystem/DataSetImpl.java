package dhtSystem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Alejandro Rodriguez Calzado
 *
 */
public class DataSetImpl implements DataSet, Serializable {

	private HashMap <Integer,Data> dataSet;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
	
		DataSet dataSet = new DataSetImpl ();
		dataSet.addData(new Data("test.txt", "Test"));
		dataSet.addData(new Data("test2.txt", "Test2"));

		System.out.println(dataSet.getData((new Data("test.txt", "Test")).getKey()));
		System.out.println(dataSet);
	}
	
	public DataSetImpl() {
		dataSet = new HashMap<Integer, Data> ();
	}

	@Override
	public Data getData(int key) {
		return dataSet.get(key);
	}

	@Override
	public void addData(Data newData) {
		dataSet.put(newData.getKey(), newData);
	}
	
	@Override
	public void removeData (int key){
		dataSet.remove(key);
	}
	
	@Override
	public int size() {
		return dataSet.size();
	}
	
	@Override
	public Set<Integer> getKeySet(){
		return dataSet.keySet();
	}
	
	@Override
	public Boolean isInDataSet (int key) {
		return dataSet.containsKey(key);
	}

	@Override
	public HashMap<Integer, Data> getClosestDataTo(int key, LeafSet leafSet) {
		HashMap<Integer, Data> result = new HashMap<Integer, Data> ();
		
		// Calculate distances between all datas and my own node and compare it with
		// the distance between all datas and the new node
		for (Entry<Integer, Data> entry : dataSet.entrySet())
		{
			if (leafSet.closestLeaf(entry.getKey()).getKey() == key){
				result.put(entry.getKey(), entry.getValue());
			}
				
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("DataSetImpl [dataSet=%s]", dataSet);
	}

}
