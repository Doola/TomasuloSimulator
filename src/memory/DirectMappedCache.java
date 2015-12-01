package memory;


public class DirectMappedCache extends TheBigCache implements Cache{
	
	// array of data type cache lines, representing individal lines in the cache
	CacheLine[] lines;
	// to be used for write back
	boolean[] DirtyBit;
	
	
	public DirectMappedCache(int s, int l ,int m)
	{
		// since direct mapped needs assosciativity to be equal to 1
		super(s,l,1);
		l*=16;
		this.lengthIndex = (int) (Math.log(s/l)/Math.log(2));
		// this assumes that we are always word addressable
		this.lengthOffset = (int) (Math.log(l)/Math.log(2));
		this.lengthTag = 16 - lengthIndex - lengthOffset;
		lines = new CacheLine[s/l];
		this.DirtyBit = new boolean[s/l];
		hier.add(this);
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
		
		if(lines[index].ValidBit)
		{
			// if write back and dirty bit must copy data to memory first
			// then replace with our data
			
			if(this.WriteBack && DirtyBit[index])
			{
				String memAddress = lines[index].Tag + Integer.toBinaryString(index);
				
				// adding zeroes to adjust for missing offset bits in extracted address
				// this gives us the address of the start of block for this specific cache
				for(int i=0; i<this.lengthOffset; i++)
					memAddress+="0";
				MainMemory.Insert(memAddress, lines[index].Data, this.BlockSize);
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
		this.lines[index] = temp;		
		 
	}
	 
	 
	 void addToCache(int wordAddress, String data)
	 {
			String word = Integer.toBinaryString(wordAddress);
			String tagBinary = word.substring(lengthTag);
			String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
			String offsetBinary = word.substring(lengthTag + lengthIndex,16);
			int index = Integer.parseInt(indexBinary,2);
			if(lines[index].ValidBit)
			{
				if(this.WriteBack && DirtyBit[index])
				{
					String memAddress = lines[index].Tag + Integer.toBinaryString(index);
					for(int i=0; i<this.lengthOffset; i++)
						memAddress+="0";
					MainMemory.Insert(memAddress, lines[index].Data, this.BlockSize);
				}
			}
			int blockOffset = Integer.parseInt(offsetBinary,2);
			String[] newData = MainMemory.Read(wordAddress - blockOffset, this.BlockSize);
			newData[Integer.parseInt(offsetBinary,2)] = data;
			CacheLine temp = new CacheLine(newData, tagBinary);
			this.lines[index] = temp;		
	 }
	 

	@Override
	public String Read(int wordAddress) 
	{
		/*
		// ***********************************************************************************
		// ***********************************************************************************
		// ***********************************************************************************
		// ***********************************************************************************
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
		
		if(lines[index].ValidBit)
		{
			if(lines[index].Tag.equals(tagBinary))
				return lines[index].Data[Integer.parseInt(offsetBinary,2)];
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
					if(DirtyBit[index])
					{
						// address in memory is extracted by concatenating tag..index..offset
						// 
						String memAddress = lines[index].Tag + Integer.toBinaryString(index);
			
						// adding zeroes to adjust for missing offset bits in extracted address
						// this creates the address of the start of the block
						// *********************************************************
						// *********************************************************
						// *********************************************************
						// I need to write the changed data in the lower levels of the cache
						for(int i=0; i<this.lengthOffset; i++)
							memAddress+="0";
						MainMemory.Insert(memAddress, lines[index].Data, this.BlockSize);
						
						
						CacheLine temp = new CacheLine(MainMemory.Read(wordAddress,this.BlockSize), tagBinary);
						lines[index] = temp;
						
					}
					
					else
					{
						// no need to copy data to memory since not modified
						String[] data = MainMemory.Read(wordAddress,this.BlockSize);
						CacheLine temp = new CacheLine(data, tagBinary);
						lines[index] = temp;
						
					}
					
				}
				
				else
				{
					// then its write through so cache and main memory are consistant
					String[] data = MainMemory.Read(wordAddress,this.BlockSize);
					CacheLine temp = new CacheLine(data, tagBinary);
					lines[index] = temp;
				}
				
			}
		}
		
		// fetching data from main memory and inserting into cache
		
		else
		{
			String[] data = MainMemory.Read(wordAddress,this.BlockSize);
			CacheLine temp = new CacheLine(data, tagBinary);
			lines[index] = temp;
		}
		
		return null;
		// ***********************************************************************************
		// ***********************************************************************************
		// ***********************************************************************************
		// ***********************************************************************************
		 */
		
		// Convert word address to binary and extract index, tag and offset
		// check if index is valid and tag is equal
		// case yes: return data
		// case no: return null 
		
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
		String offsetBinary = word.substring(lengthTag + lengthIndex,16);
		int index = Integer.parseInt(indexBinary,2);
		
		if(lines[index].ValidBit)
		{
			if(lines[index].Tag.equals(tagBinary))
			{
				return lines[index].Data[Integer.parseInt(offsetBinary,2)];
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
		if(lines[index].ValidBit)
		{
			if(lines[index].Tag.equals(tagBinary))
			{
				if(this.WriteBack)
				{
					this.DirtyBit[index] = true;
				}
			
				int offset = Integer.parseInt(offsetBinary,2);
				lines[index].Data[offset] = data;
				return true;
			}
		}
		return false;
	}
	
}
