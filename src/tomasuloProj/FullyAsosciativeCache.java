package tomasuloProj;

import java.util.LinkedList;

public class FullyAsosciativeCache extends TheBigCache implements Cache{
	
	
	LinkedList<CacheLine> lines;
	boolean[] DirtyBit;
	int maxNumberLines;
	
	public FullyAsosciativeCache(int S, int L, int m) {
		super(S, L, m);
		this.lengthIndex = 0;
		// this assumes that we are always word addressable
		maxNumberLines = S/this.BlockSize;
		this.lengthOffset = (int) (Math.log(L)/Math.log(2));
		this.lengthTag = 16 - lengthIndex - lengthOffset;
		lines = new LinkedList<CacheLine>();
		this.DirtyBit = new boolean[S/L];
		hier.add(this);
	}
	
	public String Read(int wordAddress)
	{
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		// length of tag and offset should be equal to 16
		String offsetBinary = word.substring(lengthTag,lengthTag + lengthOffset);
		
		// no need to check for valid bits because list is empty when no data is inserted
		for(int i = 0; i < lines.size(); i++)
		{
			if(lines.get(i).Tag == tagBinary)
			{
				return lines.get(i).Data[Integer.parseInt(offsetBinary,2)];
			}
		}
		return null;
	}
	
	public boolean Write(int wordAddress, String data) 
	{
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		String offsetBinary = word.substring(lengthTag,lengthTag + lengthOffset);
		
		for(int i = 0; i < lines.size(); i++)
		{
			if(lines.get(i).Tag == tagBinary)
			{
				if(this.WriteBack)
				{
					this.DirtyBit[i] = true;
				}
				int offset = Integer.parseInt(offsetBinary,2);
				String[] tempData = lines.get(i).Data;
				tempData[Integer.parseInt(offsetBinary,2)] = data;
				lines.get(i).Data = tempData;
				return true;
			}
		}
		return false;
	}
	
	// adding a word from memory and writing to that data
	 void addToCache(int wordAddress, String data)
	 {
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		String offsetBinary = word.substring(lengthTag,lengthTag + lengthOffset);
		if(lines.size() == maxNumberLines)
		{	
			// we need to remove a cacheLine
			// Assumption we will always remove the first line
			if(this.WriteBack && DirtyBit[0])
			{
				// copy data to memory
				String memAddress = lines.getFirst().Tag;
				for(int i=0; i<this.lengthOffset; i++)
					memAddress+="0";
				MainMemory.Insert(memAddress, lines.getFirst().Data, this.BlockSize);
			}
			lines.removeFirst();
		}
		
		int blockOffset = Integer.parseInt(offsetBinary,2);
		String[] newData = MainMemory.Read(wordAddress - blockOffset, this.BlockSize);
		CacheLine temp = new CacheLine(newData, tagBinary);
		newData[Integer.parseInt(offsetBinary,2)] = data;
		lines.addLast(temp);
	 }
	 
	 // adding a word Address from Memory
	 void addToCache(int wordAddress)
		{
			String word = Integer.toBinaryString(wordAddress);
			String tagBinary = word.substring(lengthTag);
			String offsetBinary = word.substring(lengthTag,lengthTag + lengthOffset);
			if(lines.size() == maxNumberLines)
			{
				// we need to remove a cacheLine
				// Assumption we will always remove the first line
				if(this.WriteBack && DirtyBit[0])
				{
					// copy data to memory
					String memAddress = lines.getFirst().Tag;
					for(int i=0; i<this.lengthOffset; i++)
						memAddress+="0";
					MainMemory.Insert(memAddress, lines.getFirst().Data, this.BlockSize);
				}
				lines.removeFirst();
			}
			int blockOffset = Integer.parseInt(offsetBinary,2);
			String[] data = MainMemory.Read(wordAddress - blockOffset, this.BlockSize);
			CacheLine temp = new CacheLine(data, tagBinary);
			lines.addLast(temp);
			 
		}
	

}
