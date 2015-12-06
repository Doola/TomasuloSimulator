package memoryWriteThroughOnly;

import java.util.ArrayList;
import java.util.LinkedList;

import sun.applet.Main;

public class TheBigCache implements Cache{
	int Size, BlockSize, assosciativity, lengthIndex,
	lengthOffset, lengthTag; 
	double accessTime, numberOfMisses, numberOfAccesses;
	boolean WriteBack, WriteThrough;
	
	static LinkedList<TheBigCache> hier = new LinkedList<TheBigCache>();
	static int currentCachePosition = 0;

	
	
	public TheBigCache(int S, int L, int m)
	{
		this.Size = S;
		// assuming we are given number of words not bits
		this.BlockSize = L;
		this.assosciativity = m;
	}

	public String Read(int wordAddress) throws IndexOutOfMemoryBoundsException{
		// Loop through all levels of cache till we find the word is found
		for (int i = 0; i < hier.size(); i++) 
		{			 
			if(hier.get(i).Read(wordAddress)!= null)
			{
				// must place the data at the top levels
				for(int k=0; k<i; k++){
					hier.get(k).addToCache(wordAddress);
				}
				return hier.get(i).Read(wordAddress);
			}
				
		}		
		// this address is in main memory so we need to fetch it from there
		// the block size is different for each cache so we need to pass block size
		// and call this recursively till we are at the top level.
		addToCache(wordAddress);
		return MainMemory.ReadTemp(Integer.toBinaryString(wordAddress));
	}

	@Override
	// this should take only the byte of data that needs to be changed
	// because the size of blocks is different from level to level.
	
	
	public boolean Write(int wordAddress, String data) throws IndexOutOfMemoryBoundsException {
		
		for (int i = 0; i < hier.size(); i++) {
			if(hier.get(i).Write(wordAddress, data))
			{
				// will loop 3ala levels below writing
				// write 3ala levels above
				// also need to write fi el memory
				
				for(int k =i+1; k < hier.size(); k++)
					hier.get(i).Write(wordAddress, data);
				String address = Integer.toBinaryString(wordAddress);
				while(address.length()<16)
					address = "0" + address;
				MainMemory.RAM.put(address, data);
				for(int j = i-1; j >= 0; j--)
					hier.get(j).addToCache(wordAddress, data);
				return true;
			}
		}
		addToCache(wordAddress,data);
		return false;
		}
	

	void addToCache(int wordAddress,String data) throws IndexOutOfMemoryBoundsException
	{
		for(int i=0; i<hier.size(); i++)
			hier.get(i).addToCache(wordAddress,data);
	}
	
	void addToCache(int wordAddress) throws IndexOutOfMemoryBoundsException
	{
		// I need to check when I remove data if this is a write back then
		// 
		for(int i=0; i<hier.size(); i++)
			hier.get(i).addToCache(wordAddress);
	}
	
	double getHitRatio()
	{
		return (numberOfAccesses - numberOfMisses)/numberOfAccesses;
	}
	
	public void WriteData(int wordAddress, String[] data){
		
	}
	
}
