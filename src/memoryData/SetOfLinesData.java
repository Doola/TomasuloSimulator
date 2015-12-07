package memoryData;


public class SetOfLinesData{
	public CacheLineData [] Lines;
	
	public SetOfLinesData(int size){
		Lines = new CacheLineData [size];
		for (int i = 0; i < Lines.length; i++) {
			Lines[i] = new CacheLineData(null,null);
		}
	}
	
}
	
