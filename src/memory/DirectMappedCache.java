package memory;

public class DirectMappedCache extends TheBigCache implements Cache {

	// array of data type cache lines, representing individal lines in the cache
	CacheLine[] lines;
	// to be used for write back
	boolean[] DirtyBit;

	public DirectMappedCache(int s, int l, int m) {
		// since direct mapped needs assosciativity to be equal to 1
		super(s, l, 1);
		l *= 16;
		this.lengthIndex = (int) (Math.log(s / l) / Math.log(2));
		// this assumes that we are always word addressable
		this.lengthOffset = (int) (Math.log(l/16) / Math.log(2));
		this.lengthTag = 16 - lengthIndex - lengthOffset;
		lines = new CacheLine[s / l];
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
		String tagBinary = word.substring(lengthTag);
		String indexBinary = word.substring(lengthTag, lengthTag + lengthIndex);
		String offsetBinary = word.substring(lengthTag + lengthIndex, 16);
		int index = Integer.parseInt(indexBinary, 2);

		// If the index is invalid then we can fetch data from memory and
		// replace it right away
		if (lines[index].ValidBit) {
			// if write back and dirty bit must copy data to memory first
			// then replace with our data
			if (this.WriteBack && DirtyBit[index])
				try {
					{
						// loop over lower levels till I reach WriteBack again and stop
						// if I am the last level then insert in memory
						
						if(this.equals(hier.getLast())){
							String memAddress = lines[index].Tag
									+ Integer.toBinaryString(index);

							// adding zeroes to adjust for missing offset bits in
							// extracted address
							// this gives us the address of the start of block for
							// this specific cache
							for (int i = 0; i < this.lengthOffset; i++)
								memAddress += "0";
							MainMemory.Insert(memAddress, lines[index].Data,
									this.BlockSize);
						} else {
							// copy data to lower levels until we reach a writeBack level
							boolean stop = false;
							String AddressToReplace = "";
							String data = lines[index].Data[Integer.parseInt(offsetBinary,2)];
							DirectMappedCache temp ;
							for(int i=hier.indexOf(this); i<hier.size(); i++){
								// fetch the address from the block and write on this address at lowers levels
								if(hier.get(i).WriteBack){
									stop = true;
								}
								// write in the addresses considering the fact that they might be in different 
								// indexes due to difference in block size
								// new write method which writes the whole data array since same block size.
								// add address to index
								
								// pass index to start and da
								AddressToReplace = 
								hier.get(i).WriteData(AddressToBeReplacedInAboveLevel, data);
								// I need to type cast and call the WriteNew method
							}
						}
						
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		this.lines[index] = temp;
	}

	void addToCache(int wordAddress, String data) throws IndexOutOfMemoryBoundsException {
		String word = Integer.toBinaryString(wordAddress);
		String tagBinary = word.substring(lengthTag);
		String indexBinary = word.substring(lengthTag, lengthTag + lengthIndex);
		String offsetBinary = word.substring(lengthTag + lengthIndex, 16);
		int index = Integer.parseInt(indexBinary, 2);
		if (lines[index].ValidBit) {
			if (this.WriteBack && DirtyBit[index])
				try {
					{
						String memAddress = lines[index].Tag
								+ Integer.toBinaryString(index);
						for (int i = 0; i < this.lengthOffset; i++)
							memAddress += "0";
						MainMemory
								.Insert(memAddress, lines[index].Data, this.BlockSize);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
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
		if (word.length() < 16) {
			for (int i = word.length(); i <= 16; i++) {
				word = "0" + word;
			}

		}
		String tagBinary = word.substring(lengthTag);
		String indexBinary = word.substring(lengthTag, lengthTag + lengthIndex);
		String offsetBinary = word.substring(lengthTag + lengthIndex, 16);
		int index = Integer.parseInt(indexBinary, 2);

		// If the line is not valid then nothing was ever written in it.
		// Compare the tag of word address with tag in line, If equal then
		// the address is present thus we return it, else we return null
		// indicating
		// that the address is not present in the cache.
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
		String tagBinary = word.substring(lengthTag);
		String indexBinary = word.substring(lengthTag, lengthTag + lengthIndex);
		String offsetBinary = word.substring(lengthTag + lengthIndex, 16);
		int index = Integer.parseInt(indexBinary, 2);

		// If the line is not valid then nothing was ever written in it.
		// Compare the tag of word address with tag in line, If equal then
		// the address is present thus we write it. If this level is writeBack
		// we set the dirty bit and return true.
		if (lines[index].ValidBit) {
			if (lines[index].Tag.equals(tagBinary)) {
				if (this.WriteBack) {
					this.DirtyBit[index] = true;
				}
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
		String tagBinary = word.substring(lengthTag);
		String indexBinary = word.substring(lengthTag, lengthTag + lengthIndex);
		String offsetBinary = word.substring(lengthTag + lengthIndex, 16);
		int index = Integer.parseInt(indexBinary, 2);
		// replace the data with edited data
		lines[index].Data = data;
	}
}
