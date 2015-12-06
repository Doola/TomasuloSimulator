package memoryWriteThroughOnly;

import memoryWriteThroughOnly.Cache;
import memoryWriteThroughOnly.CacheLine;
import memoryWriteThroughOnly.IndexOutOfMemoryBoundsException;
import memoryWriteThroughOnly.MainMemory;
import memoryWriteThroughOnly.TheBigCache;

public class DirectMappedCache extends TheBigCache implements Cache {

	// array of data type cache lines, representing individual lines in the cache
	CacheLine[] lines;
	// to be used for write back
	boolean[] DirtyBit;

	public DirectMappedCache(int s, int l, int m) {
		// since direct mapped needs associativity to be equal to 1
		super(s, l, 1);
		l *= 16;
		this.lengthIndex = (int) (Math.log(s / l) / Math.log(2));
		// this assumes that we are always word addressable
		this.lengthOffset = (int) (Math.log(l/16) / Math.log(2));
		this.lengthTag = 16 - lengthIndex - lengthOffset;
		lines = new CacheLine[s / l];
		for (int i = 0; i < lines.length; i++) {
			lines[i] = new CacheLine(null, "");
		}
		this.DirtyBit = new boolean[s / l];
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
		this.lines[index] = temp;
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
		this.lines[index] = temp;
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
		if (lines[index].ValidBit) {
			if (lines[index].Tag.equals(tagBinary)) {

				return lines[index].Data[Integer.parseInt(offsetBinary, 2)];
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
		if (lines[index].ValidBit) {
			if (lines[index].Tag.equals(tagBinary)) {
				int offset = Integer.parseInt(offsetBinary, 2);
				lines[index].Data[offset] = data;
				return true;
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
		lines[index] = new CacheLine(data, tagBinary);
	}
}
