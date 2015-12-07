package memoryInstructions;

import memoryInstructions.Cache;
import memoryInstructions.CacheLine;
import memoryInstructions.TheBigCache;
import memory.*;

public class SetAssociative extends TheBigCache implements Cache {
	// final int MemSize = 65536;
	boolean[][] DirtyBits;
	// cache is 2D array of Lines where the first Dimension is the number of
	// sets and the second dimension is the number of lines per set.
	public SetOfLines[] cache;
	public int noOfLines;
	int setSize;

	// lineSize is the size of line in bits
	public SetAssociative(int s, int l, int m) {
		super(s, l, m);
		l*=16;
		this.lengthIndex = (int) (Math.log(s / l) / Math.log(2));
		// this assumes that we are always word addressable
		this.lengthOffset = (int) (Math.log(l/16) / Math.log(2));
		this.lengthTag = 16 - lengthIndex - lengthOffset;
		noOfLines = s / (l * m);
		setSize = m;
		DirtyBits = new boolean[noOfLines][setSize];
		cache = new SetOfLines[(s / l) / m];

		for (int i = 0; i < cache.length; i++) {
			cache[i] = new SetOfLines(m);
			for(int j=0; j<m;j++)
				cache[i].Lines[j] = new CacheLine(null, "");
		}
		hier.add(this);
		this.numberOfAccesses = 0.0;
		this.numberOfMisses = 0.0;
	}

	// called only when we know that certain line needs to be pushed to cache
	void addToCache(int wordAddress) throws IndexOutOfMemoryBoundsException {
		// get index and place cache line at given index
		// must check for write back and dirty bit
		// if the line's valid bit is false then skip all steps and just add it
		String word = Integer.toBinaryString(wordAddress);
		for (int i = word.length(); i <= 16; i++) {
			word = "0" + word;
		}
		String tagBinary = word.substring(0, lengthTag);
		String indexBinary = word.substring(lengthTag + 1, lengthTag
				+ lengthIndex + 1);
		String offsetBinary = word.substring(lengthTag + lengthIndex + 1);
		int index = Integer.parseInt(indexBinary, 2);
		
		
		// check if i have an empty slot
		// if empty slot then just put it there
		// lw mfeesh remove el fi index zero
		int emptySlot = -1;
		for(int i=0; i<setSize; i++){
			if(this.cache[index/setSize].Lines[i].Tag == ""){
				emptySlot = i;
			}	
		}
		// just place line in empty lot
		// else replace line at index zero
		if(emptySlot != -1){
			int blockOffset = Integer.parseInt(offsetBinary, 2);
			String[] data = MainMemory.Read(wordAddress - blockOffset,
					this.BlockSize);
			CacheLine temp = new CacheLine(data, tagBinary);
			this.cache[index/setSize].Lines[emptySlot] = temp;
		}
		else
		{
			if(this.WriteBack && DirtyBits[index/setSize][0]){
				if (this.equals(hier.getLast())) {
					String memAddress = cache[index].Lines[0].Tag + indexBinary;

					// adding zeroes to adjust for missing offset bits
					// in
					// extracted address
					// this gives us the address of the start of block
					// for
					// this specific cache
					for (int i = 0; i < this.lengthOffset; i++)
						memAddress = memAddress + "0";
					MainMemory.Insert(memAddress, cache[index].Lines[0].Data,
							this.BlockSize);
				}
				else
				{
					// copy data to lower levels until we reach a
					// writeBack level
					boolean stop = false;
					String AddressToReplace = "";
					String binaryIndex = "";

					AddressToReplace = cache[index/setSize].Lines[0].Tag + indexBinary;
					while (AddressToReplace.length() < 16) {
						AddressToReplace = AddressToReplace + "0";
					}
					String[] block = cache[index/setSize].Lines[0].Data;

					SetAssociative temp;

					for (int i = hier.indexOf(this) + 1; i < hier
							.size() && !stop; i++) {

						// fetch the address from the block and write on
						// this address at lowers levels
						if (hier.get(i).WriteBack) {
							stop = true;
						}

						hier.get(i).writeBlock(
								Integer.parseInt(AddressToReplace, 2),
								block);
					}
				}
			}
		}
			

		// cycle Calc $$$$$$$$$$$$$$$
		// the word address passed should be shifted to be the address
		// of the start of the block.
		// ### cycle calculation note:
		// we access the memory but this is technically not memory access or is
		// it counted

		int blockOffset = Integer.parseInt(offsetBinary, 2);
		String[] data = MainMemory.Read(wordAddress - blockOffset,
				this.BlockSize);
		CacheLine temp = new CacheLine(data, tagBinary);
		this.cache[index / setSize].Lines[index % setSize] = temp;
		
		
	}

	
	void addToCache(int wordAddress, String data)
			throws IndexOutOfMemoryBoundsException {
		
		
		// get index and place cache line at given index
				// must check for write back and dirty bit
				// if the line's valid bit is false then skip all steps and just add it
				String word = Integer.toBinaryString(wordAddress);
				for (int i = word.length(); i <= 16; i++) {
					word = "0" + word;
				}
				String tagBinary = word.substring(0, lengthTag);
				String indexBinary = word.substring(lengthTag + 1, lengthTag
						+ lengthIndex + 1);
				String offsetBinary = word.substring(lengthTag + lengthIndex + 1);
				int index = Integer.parseInt(indexBinary, 2);
				
				
				// check if i have an empty slot
				// if empty slot then just put it there
				// lw mfeesh remove el fi index zero
				int emptySlot = -1;
				for(int i=0; i<setSize; i++){
					if(this.cache[index/setSize].Lines[i].Tag == ""){
						emptySlot = i;
					}	
				}
				// just place line in empty lot
				// else replace line at index zero
				if(emptySlot != -1){
					int blockOffset = Integer.parseInt(offsetBinary, 2);
					String[] tempData = MainMemory.Read(wordAddress - blockOffset,
							this.BlockSize);
					CacheLine temp = new CacheLine(tempData, tagBinary);
					this.cache[index/setSize].Lines[emptySlot] = temp;
				}
				else
				{
					if(this.WriteBack && DirtyBits[index/setSize][0]){
						if (this.equals(hier.getLast())) {
							String memAddress = cache[index].Lines[0].Tag + indexBinary;

							// adding zeroes to adjust for missing offset bits
							// in
							// extracted address
							// this gives us the address of the start of block
							// for
							// this specific cache
							for (int i = 0; i < this.lengthOffset; i++)
								memAddress = memAddress + "0";
							MainMemory.Insert(memAddress, cache[index].Lines[0].Data,
									this.BlockSize);
						}
						else
						{
							// copy data to lower levels until we reach a
							// writeBack level
							boolean stop = false;
							String AddressToReplace = "";
							String binaryIndex = "";

							AddressToReplace = cache[index/setSize].Lines[0].Tag + indexBinary;
							while (AddressToReplace.length() < 16) {
								AddressToReplace = AddressToReplace + "0";
							}
							String[] block = cache[index/setSize].Lines[0].Data;

							SetAssociative temp;

							for (int i = hier.indexOf(this) + 1; i < hier
									.size() && !stop; i++) {

								// fetch the address from the block and write on
								// this address at lowers levels
								if (hier.get(i).WriteBack) {
									stop = true;
								}

								hier.get(i).writeBlock(
										Integer.parseInt(AddressToReplace, 2),
										block);
							}
						}
					}
				}
					

				// cycle Calc $$$$$$$$$$$$$$$
				// the word address passed should be shifted to be the address
				// of the start of the block.
				// ### cycle calculation note:
				// we access the memory but this is technically not memory access or is
				// it counted

				int blockOffset = Integer.parseInt(offsetBinary, 2);
				String[] newData = MainMemory.Read(wordAddress - blockOffset,
						this.BlockSize);
				newData[Integer.parseInt(offsetBinary, 2)] = data;
				CacheLine temp = new CacheLine(newData, tagBinary);
				this.cache[index / setSize].Lines[index % setSize] = temp;
	}

	@Override
	public String Read(int wordAddress) {
		// Converting wordAddress to binary and extracting
		// tag, index and offset.
		String word = Integer.toBinaryString(wordAddress);
		for (int i = word.length(); i <= 16; i++) {
			word = "0" + word;
		}
		String tagBinary = word.substring(0, lengthTag);
		String indexBinary = word.substring(lengthTag + 1, lengthTag
				+ lengthIndex + 1);
		String offsetBinary = word.substring(lengthTag + lengthIndex + 1);
		int index = Integer.parseInt(indexBinary, 2);

		// If the line is not valid then nothing was ever written in it.
		// Compare the tag of word address with tag in line, If equal then
		// the address is present thus we return it, else we return null
		// indicating that the address is not present in the cache.
		for (int i = 0; i < setSize; i++) {
			if (cache[index / setSize].Lines[i].ValidBit) {
				if (cache[index / setSize].Lines[i].Tag.equals(tagBinary)) {

					return cache[index / setSize].Lines[i].Data[Integer
							.parseInt(offsetBinary, 2)];
				}
			}
		}
		this.numberOfMisses++;
		return null;
	}

	// The user changes the data at one address only
	// Find the address in cache update the data.
	// If write back set dirty bit to 1
	// if write through then we must copy to all other levels.
	public boolean Write(int wordAddress, String data) {
		// Converting wordAddress to binary and extracting
		// tag, index and offset.
		String word = Integer.toBinaryString(wordAddress);
		for (int i = word.length(); i <= 16; i++) {
			word = "0" + word;
		}
		String tagBinary = word.substring(0, lengthTag);
		String indexBinary = word.substring(lengthTag + 1, lengthTag
				+ lengthIndex + 1);
		String offsetBinary = word.substring(lengthTag + lengthIndex + 1);
		int index = Integer.parseInt(indexBinary, 2);

		// If the line is not valid then nothing was ever written in it.
		// Compare the tag of word address with tag in line, If equal then
		// the address is present thus we write it. If this level is writeBack
		// we set the dirty bit and return true.
		for (int i = 0; i < setSize; i++) {

			if (cache[index / setSize].Lines[i].ValidBit) {
				if (cache[index / setSize].Lines[i].Tag.equals(tagBinary)) {
					int offset = Integer.parseInt(offsetBinary, 2);
					cache[index / setSize].Lines[i].Data[offset] = data;
					DirtyBits[index / setSize][i] = true;
					return true;
				}
			}
		}
		return false;
	}

	
	public void writeBlock(int wordAddress, String[] data)
	{
		String word = Integer.toBinaryString(wordAddress);
		for (int i = word.length(); i <= 16; i++) {
			word = "0" + word;
		}
		String tagBinary = word.substring(0, lengthTag);
		String indexBinary = word.substring(lengthTag+1, lengthTag + lengthIndex +1);
		String offsetBinary = word.substring(lengthTag + lengthIndex + 1);
		int index = Integer.parseInt(indexBinary, 2);
		
		cache[index].Lines[0].Data = data;
		cache[index].Lines[0].Tag = tagBinary;
		if(this.WriteBack)
			this.DirtyBits[index][0] = true;
	}

}
