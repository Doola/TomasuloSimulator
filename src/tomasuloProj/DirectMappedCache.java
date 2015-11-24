package tomasuloProj;

import tomasuloProj.MainMemory;

public class DirectMappedCache extends TheBigCache implements Cache{
	
	CacheLine[] lines;
	boolean[] DirtyBit;
	
	
	public DirectMappedCache(int s, int l ,int m)
	{
		// since direct mapped needs assosciativity to be equal to 1
		super(s,l,1);
		this.lengthIndex = (int) (Math.log(s/l)/Math.log(2));
		this.lengthOffset = (int) (Math.log(l)/Math.log(2));
		this.lengthTag = 16 - lengthIndex - lengthOffset;
		lines = new CacheLine[s/l];
		this.DirtyBit = new boolean[s/l];
	}


	@Override
	public String Read(int wordAddress) 
	{
		// get index, tag and offset
		
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
		String offsetBinary = word.substring(lengthTag + lengthIndex,16);
		int index = Integer.parseInt(indexBinary,2);
		
		// if not valid then its an empty cacheLine
		// if valid but tags are not equal then must fetch from memory
		
		if(lines[index].ValidBit && lines[index].Tag.equals(tagBinary))
		{
			return lines[index].Data;
		}
		
		// fetching data from main memory and inserting into cache
		
		else
		{
			String data = MainMemory.Read(wordAddress);
			CacheLine temp = new CacheLine(data, tagBinary);
			lines[index] = temp;
		}
		
		return null;
	}

	
	public void Write(int wordAddress, String data) 
	{
		if(this.WriteBack)
			WriteBack(wordAddress,data);
		else
			WriteThrough(wordAddress,data);
		
	}
	
	public void WriteBack(int wordAddress, String data)
	{
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
		String offsetBinary = word.substring(lengthTag + lengthIndex,16);
		int index = Integer.parseInt(indexBinary,2);

		CacheLine temp =lines[index];
		
		// Write hit
		if(temp.Tag.equals(tagBinary) && temp.ValidBit)
		{
			temp.Data = data;
			DirtyBit[index] = true;
			temp.ValidBit = true;
			lines[index] = temp;
		}
		
		// Write miss
		else
		{
			// place old data in memory
			if(DirtyBit[index])
			{
				String DataToMemory = lines[index].Data;
				
				// starting block address in memory without offset
				
				String address = lines[index].Tag + Integer.toBinaryString(index);
				
				// adding zeroes to adjust for missing offset bits in extracted address
				for(int i=0; i<this.lengthOffset; i++)
					address+="0";
				
				// change memory write to adjust to block sizes
				// will pass starting address to memory and block size 
				// will iterate until full block is copied to memory
				MainMemory.Insert(address, DataToMemory, this.BlockSize);
				
			}
			// placing new data in cache
			temp.Data = data;
			temp.Tag = tagBinary;
			lines[index] = temp;
		}

	}
	
	public void WriteThrough(int wordAddress, String data)
	{
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		String indexBinary = word.substring(lengthTag,lengthTag + lengthIndex);
		String offsetBinary = word.substring(lengthTag + lengthIndex,16);
		int index = Integer.parseInt(indexBinary,2);
		
		// inserting new data into cache
		CacheLine temp = new CacheLine(data, tagBinary);
		lines[index] = temp;
		
		// inserting into main memory
		MainMemory.Insert(word, data, this.BlockSize);
	}

}
