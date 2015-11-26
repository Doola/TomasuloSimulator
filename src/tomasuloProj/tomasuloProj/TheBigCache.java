package tomasuloProj;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class TheBigCache implements Cache{
	int Size, BlockSize, assosciativity, lengthIndex, lengthOffset, lengthTag;
	boolean WriteBack, WriteThrough;
	static LinkedList<TheBigCache> hier;
	static int currentCachePosition = 0;
	
	
	public TheBigCache(int S, int L, int m)
	{
		this.Size = S;
		this.BlockSize = BlockSize;
		this.assosciativity = m;
	}

	public String Read(int wordAddress){
		for (int i = 0; i < hier.size(); i++) 
		{
			if(hier.get(i).Read(wordAddress)!= null)
				return hier.get(i).Read(wordAddress);
		}
		
		addToCache(wordAddress);
		
		return MainMemory.ReadTemp(Integer.toBinaryString(wordAddress));
	}

	@Override
	public void Write(int wordAddress, String data) {
		// TODO Auto-generated method stub
		
	}
	public void WriteThrough(int wordAddress, String data){
		
	}
	
	void addToCache(int wordAddress)
	{
		for(int i=0; i<hier.size(); i++)
			hier.get(i).addToCache(wordAddress);
	}
	
}