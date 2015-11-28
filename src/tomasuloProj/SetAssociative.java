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
		noOfLines = s / l;
		DirtyBits = new boolean[noOfLines];
		setSize = m;
		cache = new SetOfLines[(s / l) / m];
		
		for (int i = 0; i < cache.length; i++) {
			cache[i] = new SetOfLines(m);
		}
	}

	 void addToCache(int wordAddress)
		{
			// get index and place cache line at given index
			// must check for write back and dirty bit
			String word = Integer.toBinaryString(wordAddress);
			String tagBinary = word.substring(lengthTag);
			String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
			String offsetBinary = word.substring(lengthTag + lengthIndex,16);
			int index = Integer.parseInt(indexBinary,2);
			
			// check if cache line is dirty and writeback
			if(this.WriteBack)
			{
				if(DirtyBits[index])
				{
					String memAddress = cache[index/setSize].Lines[index%setSize].Tag + Integer.toBinaryString(index);
					
					// adding zeroes to adjust for missing offset bits in extracted address
					for(int i=0; i<this.lengthOffset; i++)
						memAddress+="0";
					MainMemory.Insert(memAddress, cache[index/setSize].Lines[index%setSize].Data, this.BlockSize);
				}
			}
			// the word address passed should be shifted to be the address
			// of the start of the block.
			int blockOffset = Integer.parseInt(offsetBinary,2);
			String[] data = MainMemory.Read(wordAddress - blockOffset, this.BlockSize);
			CacheLine temp = new CacheLine(data, tagBinary);
			this.cache[index/setSize].Lines[index%setSize] = temp;
			 
		}

		@Override
		public String Read(int wordAddress) 
		{
			
			
			// converting wordaddress to binary
			// extract index, tag,offset
			String word = Integer.toBinaryString(wordAddress);
			String tagBinary = word.substring(lengthTag);
			String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
			String offsetBinary = word.substring(lengthTag + lengthIndex,16);
			int index = Integer.parseInt(indexBinary,2);
			
			// get cache line
			// if not valid then its an empty cacheLine
			// if valid but tags are not equal then must fetch from memory and replace cache line
			// before replacing in memory must check if write back then 
			
			if(cache[index/setSize].Lines[index%setSize].ValidBit)
			{
				if(cache[index/setSize].Lines[index%setSize].Tag.equals(tagBinary))
					return cache[index/setSize].Lines[index%setSize].Data[Integer.parseInt(offsetBinary,2)];
				else
				{
					// check in lower level
					
					//this.hier.get(currentCachePosition).;
					
					// check if the data in this location was modified
					// if modified then must write to memory in case of writeBack
					// if modified but writeThrough then i only need to replace cache line
					if(this.WriteBack)
					{
						
						// check levels el ta7t fi el caches awy and remove the instruction from the lower levels
						
						// copy data to memory location if dirty bit set
						// fetch new data from memory and put in cache
						if(DirtyBits[index])
						{
							// address in memory is extracted by concatenating tag..index..offset
							// 
							String memAddress = cache[index/setSize].Lines[index%setSize].Tag + Integer.toBinaryString(index);
				
							// adding zeroes to adjust for missing offset bits in extracted address
							// this creates the address of the start of the block
							// *********************************************************
							// *********************************************************
							// *********************************************************
							// I need to write the changed data in the lower levels of the cache
							for(int i=0; i<this.lengthOffset; i++)
								memAddress+="0";
							MainMemory.Insert(memAddress, cache[index/setSize].Lines[index%setSize].Data, this.BlockSize);
							
							
							CacheLine temp = new CacheLine(MainMemory.Read(wordAddress,this.BlockSize), tagBinary);
							cache[index/setSize].Lines[index%setSize] = temp;
							
						}
						
						else
						{
							// no need to copy data to memory since not modified
							String[] data = MainMemory.Read(wordAddress,this.BlockSize);
							CacheLine temp = new CacheLine(data, tagBinary);
							cache[index/setSize].Lines[index%setSize] = temp;
							
						}
						
					}
					
					else
					{
						// then its write through so cache and main memory are consistant
						String[] data = MainMemory.Read(wordAddress,this.BlockSize);
						CacheLine temp = new CacheLine(data, tagBinary);
						cache[index/setSize].Lines[index%setSize] = temp;
					}
					
				}
			}
			
			// fetching data from main memory and inserting into cache
			
			else
			{
				String[] data = MainMemory.Read(wordAddress,this.BlockSize);
				CacheLine temp = new CacheLine(data, tagBinary);
				cache[index/setSize].Lines[index%setSize] = temp;
			}
			
			return null;
		}

		
		public void Write(int wordAddress, String[] data) 
		{
			if(this.WriteBack)
				WriteBack(wordAddress,data);
			else
				WriteThrough(wordAddress,data);
			
		}
		
		public void WriteBack(int wordAddress, String[] data)
		{
			
			String word = Integer.toBinaryString(wordAddress);
			String tagBinary = word.substring(lengthTag);
			String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
			String offsetBinary = word.substring(lengthTag + lengthIndex,16);
			int index = Integer.parseInt(indexBinary,2);

			CacheLine temp =cache[index/setSize].Lines[index%setSize];
			
			// Write hit
			if(temp.Tag.equals(tagBinary) && temp.ValidBit)
			{
				temp.Data = data;
				DirtyBits[index] = true;
				temp.ValidBit = true;
				cache[index/setSize].Lines[index%setSize] = temp;
			}
			
			// Write miss
			else
			{
				// place old data in memory
				if(DirtyBits[index])
				{
					String[] DataToMemory = cache[index/setSize].Lines[index%setSize].Data;
					
					// starting block address in memory without offset
					
					String address = cache[index/setSize].Lines[index%setSize].Tag + Integer.toBinaryString(index);
					
					// adding zeroes to adjust for missing offset bits in extracted address
					for(int i=0; i<this.lengthOffset; i++)
						address+="0";
					MainMemory.Insert(address, DataToMemory, this.BlockSize);
					
				}
				// placing new data in cache
				temp.Data = data;
				temp.Tag = tagBinary;
				cache[index/setSize].Lines[index%setSize] = temp;
			}

		}
		
		public void WriteThrough(int wordAddress, String[] data)
		{
			String word = Integer.toBinaryString(wordAddress);
			String tagBinary = word.substring(lengthTag);
			String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
			String offsetBinary = word.substring(lengthTag + lengthIndex,16);
			int index = Integer.parseInt(indexBinary,2);
			
			// inserting new data into cache
			CacheLine temp = new CacheLine(data, tagBinary);
			cache[index/setSize].Lines[index%setSize] = temp;
			
			// inserting into main memory
			MainMemory.Insert(word, data, this.BlockSize);
		}
	public int BinToDec(String bin) {
		int i = 0;
		int result = 0;
		while (bin != "") {
			result += ((int) (bin.charAt(bin.length() - 1))) * Math.pow(2, i++);
			bin = bin.substring(0, bin.length());
		}
		return result;
	}
}


