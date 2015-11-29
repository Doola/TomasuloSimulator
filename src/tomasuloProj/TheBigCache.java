package tomasuloProj;

import java.util.ArrayList;
import java.util.LinkedList;

import tomasuloProj.MainMemory;

public abstract class TheBigCache implements Cache{
	int Size, BlockSize, assosciativity, lengthIndex, lengthOffset, lengthTag;
	boolean WriteBack, WriteThrough;
	
	static LinkedList<TheBigCache> hier;
	static int currentCachePosition = 0;
	
	
	public TheBigCache(int S, int L, int m)
	{
		this.Size = S;
		// assuming we are given number of words not bits
		this.BlockSize = L/16;
		this.assosciativity = m;
	}

	public String Read(int wordAddress){
		for (int i = 0; i < hier.size(); i++) 
		{
			
			// Loop through all levels of cache till we find the word is found
			 
			if(hier.get(i).Read(wordAddress)!= null)
			{
				// adding required word address to levels of cache that didnot 
				// contain the word.
				for(int k=0; k<i; k++)
					hier.get(k).addToCache(wordAddress);
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
	
	
	public boolean Write(int wordAddress, String data) {
		
		for (int i = 0; i < hier.size(); i++) {
			if(hier.get(i).Write(wordAddress, data))
			{
				if(!hier.get(i).WriteBack)
				{
					MainMemory.RAM.put(Integer.toBinaryString(wordAddress), data);
				}
				return true;
			}
		}
		
		// #jolly
		// must add to cache and then write 
		// do i add it to cache then write or write mn el awel
		
		//add to all levels altering the data i want
		// I need use add to cache but i have to make sure the data i add is the new 
		addToCache(wordAddress,data);
		return false;
		
	}
	

	void addToCache(int wordAddress,String data)
	{
		for(int i=0; i<hier.size(); i++)
			hier.get(i).addToCache(wordAddress,data);
	}
	
	void addToCache(int wordAddress)
	{
		for(int i=0; i<hier.size(); i++)
			hier.get(i).addToCache(wordAddress);
	}
	
}
