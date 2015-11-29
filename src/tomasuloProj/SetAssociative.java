package tomasuloProj;

import java.lang.Math;
import java.util.Set;

public class SetAssociative extends TheBigCache implements Cache {
	// final int MemSize = 65536;
	boolean[] DirtyBits;
	// cache is 2D array of Lines where the first Dimension is the number of
	// sets and the second dimension is the number of lines per set.
	public SetOfLines[] cache;
	public int noOfLines;
	int setSize;

	// lineSize is the size of line in bits
	public SetAssociative(int s, int l, int m) {
		super(s, l, m);
		l*=16;
		noOfLines = s / l;
		DirtyBits = new boolean[noOfLines];
		setSize = m;
		cache = new SetOfLines[(s / l) / m];
		
		for (int i = 0; i < cache.length; i++) {
			cache[i] = new SetOfLines(m);
		}
	}

	// called only when we know that certain line needs to be pushed to cache
		 void addToCache(int wordAddress)
		{
			// get index and place cache line at given index
			// must check for write back and dirty bit
			// if the line's valid bit is false then skip all steps and just add it
			String word = Integer.toBinaryString(wordAddress);
			String tagBinary = word.substring(lengthTag);
			String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
			String offsetBinary = word.substring(lengthTag + lengthIndex,16);
			int index = Integer.parseInt(indexBinary,2);
			
			// If the index is invalid then we can fetch data from memory and replace it right away
			// optimisation
			
			if(cache[index/setSize].Lines[index%setSize].ValidBit)
			{
				// if write back and dirty bit must copy data to memory first
				// then replace with our data
				
				if(this.WriteBack && DirtyBits[index])
				{
					String memAddress = cache[index/setSize].Lines[index%setSize].Tag + Integer.toBinaryString(index);
					
					// adding zeroes to adjust for missing offset bits in extracted address
					// this gives us the address of the start of block for this specific cache
					for(int i=0; i<this.lengthOffset; i++)
						memAddress+="0";
					MainMemory.Insert(memAddress, cache[index/setSize].Lines[index%setSize].Data, this.BlockSize);
				}
			}
			// cycle Calc $$$$$$$$$$$$$$$
			// the word address passed should be shifted to be the address
			// of the start of the block.
			// ### cycle calculation note:
			// we access the memory but this is technically not memory access or is it counted 
			
			int blockOffset = Integer.parseInt(offsetBinary,2);
			String[] data = MainMemory.Read(wordAddress - blockOffset, this.BlockSize);
			CacheLine temp = new CacheLine(data, tagBinary);
			this.cache[index/setSize].Lines[index%setSize] = temp;		
			 
		}
		 
		 
		 void addToCache(int wordAddress, String data)
		 {
				String word = Integer.toBinaryString(wordAddress);
				String tagBinary = word.substring(lengthTag);
				String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
				String offsetBinary = word.substring(lengthTag + lengthIndex,16);
				int index = Integer.parseInt(indexBinary,2);
				if(cache[index/setSize].Lines[index%setSize].ValidBit)
				{
					if(this.WriteBack && DirtyBits[index])
					{
						String memAddress = cache[index/setSize].Lines[index%setSize].Tag + Integer.toBinaryString(index);
						for(int i=0; i<this.lengthOffset; i++)
							memAddress+="0";
						MainMemory.Insert(memAddress, cache[index/setSize].Lines[index%setSize].Data, this.BlockSize);
					}
				}
				int blockOffset = Integer.parseInt(offsetBinary,2);
				String[] newData = MainMemory.Read(wordAddress - blockOffset, this.BlockSize);
				newData[Integer.parseInt(offsetBinary,2)] = data;
				CacheLine temp = new CacheLine(newData, tagBinary);
				this.cache[index/setSize].Lines[index%setSize] = temp;		
		 }
		 

		@Override
		public String Read(int wordAddress) 
		{
			// Convert word address to binary and extract index, tag and offset
			// check if index is valid and tag is equal
			// case yes: return data
			// case no: return null 
			
			String word = Integer.toBinaryString(wordAddress);
			String tagBinary = word.substring(lengthTag);
			String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
			String offsetBinary = word.substring(lengthTag + lengthIndex,16);
			int index = Integer.parseInt(indexBinary,2);
			
			if(cache[index/setSize].Lines[index%setSize].ValidBit)
			{
				if(cache[index/setSize].Lines[index%setSize].Tag.equals(tagBinary))
				{
					return cache[index/setSize].Lines[index%setSize].Data[Integer.parseInt(offsetBinary,2)];
				}
				// The block in the index is not the one we are looking for so it must be replaced
				// We can return null too since the read already adds the correct blocks from memory 
				// when we reach the ram without finding the required data.
			}
			return null;
		}
		
		
		// The user changes the data at one address only
		// Find the address in cache update the data.
		// If write back set dirty bit to 1
		// if write through then we must copy to all other levels.
		public boolean Write(int wordAddress, String data) 
		{
			String word = Integer.toBinaryString(wordAddress);
			String tagBinary = word.substring(lengthTag);
			String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
			String offsetBinary = word.substring(lengthTag + lengthIndex,16);
			int index = Integer.parseInt(indexBinary,2);
			if(cache[index/setSize].Lines[index%setSize].ValidBit)
			{
				if(cache[index/setSize].Lines[index%setSize].Tag.equals(tagBinary))
				{
					if(this.WriteBack)
					{
						this.DirtyBits[index] = true;
					}
				
					int offset = Integer.parseInt(offsetBinary,2);
					cache[index/setSize].Lines[index%setSize].Data[offset] = data;
					return true;
				}
			}
			return false;
		}
}


