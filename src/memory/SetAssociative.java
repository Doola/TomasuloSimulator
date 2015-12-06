package memory;

import memory.Cache;
import memory.CacheLine;
import memory.IndexOutOfMemoryBoundsException;
import memory.MainMemory;
import memory.TheBigCache;

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

	// called only when we know that certain line needs to be pushed to cache
		void addToCache(int wordAddress) throws IndexOutOfMemoryBoundsException {
			// get index and place cache line at given index
			// must check for write back and dirty bit
			// if the line's valid bit is false then skip all steps and just add it
			String word = Integer.toBinaryString(wordAddress);
			for (int i = word.length(); i <= 16; i++) {
				word = "0" + word;
			}
			String tagBinary = word.substring(0, lengthTag+1);
			String indexBinary = word.substring(lengthTag+1, lengthTag + lengthIndex +1);
			String offsetBinary = word.substring(lengthTag + lengthIndex + 1);
			int index = Integer.parseInt(indexBinary, 2);

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
			this.cache[index/setSize].Lines[index%setSize] = temp;
		}

		void addToCache(int wordAddress, String data) throws IndexOutOfMemoryBoundsException {
			String word = Integer.toBinaryString(wordAddress);
			for (int i = word.length(); i <= 16; i++) {
				word = "0" + word;
			}
			String tagBinary = word.substring(0, lengthTag+1);
			String indexBinary = word.substring(lengthTag+1, lengthTag + lengthIndex +1);
			String offsetBinary = word.substring(lengthTag + lengthIndex + 1);
			int index = Integer.parseInt(indexBinary, 2);
			int blockOffset = Integer.parseInt(offsetBinary, 2);
			String[] newData = MainMemory.Read(wordAddress - blockOffset,
					this.BlockSize);
			newData[Integer.parseInt(offsetBinary, 2)] = data;
			CacheLine temp = new CacheLine(newData, tagBinary);
			this.cache[index/setSize].Lines[index%setSize] = temp;
		}

		@Override
		public String Read(int wordAddress) {
			// Converting wordAddress to binary and extracting
			// tag, index and offset.
			String word = Integer.toBinaryString(wordAddress);
			for (int i = word.length(); i <= 16; i++) {
				word = "0" + word;
			}
			String tagBinary = word.substring(0, lengthTag+1);
			String indexBinary = word.substring(lengthTag+1, lengthTag + lengthIndex +1);
			String offsetBinary = word.substring(lengthTag + lengthIndex + 1);
			int index = Integer.parseInt(indexBinary, 2);

			// If the line is not valid then nothing was ever written in it.
			// Compare the tag of word address with tag in line, If equal then
			// the address is present thus we return it, else we return null
			// indicating that the address is not present in the cache.
			for (int i = 0; i < setSize; i++) {
			
			if (cache[index/setSize].Lines[i].ValidBit) {
				if (cache[index/setSize].Lines[i].Tag.equals(tagBinary)) {

					return cache[index/setSize].Lines[i].Data[Integer.parseInt(offsetBinary, 2)];
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
			String tagBinary = word.substring(0, lengthTag+1);
			String indexBinary = word.substring(lengthTag+1, lengthTag + lengthIndex +1);
			String offsetBinary = word.substring(lengthTag + lengthIndex + 1);
			int index = Integer.parseInt(indexBinary, 2);

			// If the line is not valid then nothing was ever written in it.
			// Compare the tag of word address with tag in line, If equal then
			// the address is present thus we write it. If this level is writeBack
			// we set the dirty bit and return true.
			for (int i = 0; i < setSize; i++) {
			
			if (cache[index/setSize].Lines[i].ValidBit) {
				if (cache[index/setSize].Lines[i].Tag.equals(tagBinary)) {
					int offset = Integer.parseInt(offsetBinary, 2);
					cache[index/setSize].Lines[index].Data[offset] = data;
					return true;
				}
			}
			}
			return false;
		}
		
		public void WriteData(int wordAddress, String[] data){
			// Converting wordAddress to binary and extracting
			// tag, index and offset.
			String word = Integer.toBinaryString(wordAddress);
			for (int i = word.length(); i <= 16; i++) {
				word = "0" + word;
			}
			String tagBinary = word.substring(0, lengthTag+1);
			String indexBinary = word.substring(lengthTag+1, lengthTag + lengthIndex +1);
			String offsetBinary = word.substring(lengthTag + lengthIndex + 1);
			int index = Integer.parseInt(indexBinary, 2);
			// replace the data with edited data
			cache[index/setSize].Lines[(int)Math.random()*setSize+1] = new CacheLine(data, tagBinary);
		}

}

