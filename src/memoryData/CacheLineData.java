package memoryData;

public class CacheLineData {

	boolean ValidBit;
	String Tag;
	String[] Data;
	
	public CacheLineData(String[] Data, String Tag)
	{
		this.Tag = Tag;
		this.Data = Data;
		this.ValidBit = true;
	}
}
