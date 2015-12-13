package memoryData;

import java.util.LinkedList;

import memory.*;

public class FullyAsosciativeCacheData extends TheBigCacheData implements CacheData {

	public LinkedList<CacheLineData> lines;
	boolean[] DirtyBit;
	int maxNumberLines;

	public FullyAsosciativeCacheData(int S, int L, int m) {
		super(S, L, m);
		L *= 16;
		this.lengthIndex = 0;
		// this assumes that we are always word addressable
		maxNumberLines = S / L;
		this.lengthOffset = (int) (Math.log(L / 16) / Math.log(2));
		this.lengthTag = 16 - lengthIndex - lengthOffset;
		lines = new LinkedList<CacheLineData>();
		this.DirtyBit = new boolean[S / L];
		hier.add(this);
		this.numberOfAccesses = 0.0;
		this.numberOfMisses = 0.0;
	}

	public String Read(int wordAddress) {
		String word = Integer.toBinaryString(wordAddress);
		for (int i = word.length(); i <= 16; i++) {
			word = "0" + word;
		}
		String tagBinary = word.substring(0,lengthTag);
		// length of tag and offset should be equal to 16
		String offsetBinary = word.substring(lengthTag+1, lengthTag
				+ lengthOffset + 1);

		// no need to check for valid bits because list is empty when no data is
		// inserted
		this.numberOfAccesses++;
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).Tag.equals(tagBinary)) {
				return lines.get(i).Data[Integer.parseInt(offsetBinary, 2)];
			}
		}
		this.numberOfMisses++;
		return null;
	}

	public boolean Write(int wordAddress, String data) {
		String word = Integer.toBinaryString(wordAddress);
		for (int i = word.length(); i <= 16; i++) {
			word = "0" + word;
		}
		String tagBinary = word.substring(0, lengthTag);
		String offsetBinary = word.substring(lengthTag + lengthIndex + 1);

		this.numberOfAccesses++;
		
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).Tag.equals(tagBinary)) {
				if (this.WriteBack) {
					this.DirtyBit[i] = true;
				}
				int offset = Integer.parseInt(offsetBinary, 2);
				String[] tempData = lines.get(i).Data;
				tempData[offset] = data;
				lines.get(i).Data = tempData;
				return true;
			}
		}
		this.numberOfMisses++;
		return false;
	}

	void addToCache(int wordAddress, String data)
			throws IndexOutOfMemoryBoundsException {
		String word = Integer.toBinaryString(wordAddress);
		for (int i = word.length(); i <= 16; i++) {
			word = "0" + word;
		}
		String tagBinary = word.substring(0, lengthTag);
		String offsetBinary = word.substring(lengthTag + lengthIndex + 1);
		// If i am removing a line from write back and it has a dirty bit
		// then I need to move to lower levels writing till I reach an other
		// write Back
		// or the memory

		// loop till i find tag
		// if found el3b
		// if not then check si`e lw fi makan zwedo
		// lw mfeesh kick awel element
		// so smart
		for (int i = 0; i < this.lines.size(); i++) {
			if (lines.get(i).Tag.equals(tagBinary)) {
				lines.get(i).Data[Integer.parseInt(offsetBinary, 2)] = data;
				return;
			}
		}

		if (lines.size() < maxNumberLines) {
			CacheLineData temp = new CacheLineData(MainMemory.Read(wordAddress,
					this.BlockSize), tagBinary);
			lines.addLast(temp);
		} else {
			// remove the line
			if (this.WriteBack && DirtyBit[0]) {
				if (this.equals(hier.getLast())) {
					String memAddress = lines.getFirst().Tag;

					// adding zeroes to adjust for missing offset bits in
					// extracted address
					// this gives us the address of the start of block for
					// this specific cache
					for (int i = 0; i < this.lengthOffset; i++)
						memAddress = memAddress + "0";
					MainMemory.Insert(memAddress, lines.getFirst().Data,
							this.BlockSize);
				} else {
					boolean stop = false;
					String AddressToReplace = "";
					String binaryIndex = "";

					AddressToReplace = lines.getFirst().Tag;
					while (AddressToReplace.length() < 16) {
						AddressToReplace = AddressToReplace + "0";
					}
					String[] block = lines.getFirst().Data;

					DirectMappedCacheData temp;

					for (int i = hier.indexOf(this) + 1; i < hier.size()
							&& !stop; i++) {

						// fetch the address from the block and write on this
						// address at lowers levels
						if (hier.get(i).WriteBack) {
							stop = true;
						}
						// write in the addresses considering the fact that they
						// might be in different
						// indexes due to difference in block size
						// new write method which writes the whole data array
						// since same block size.
						// add address to index

						// pass index to start and da

						// get the address that i will replace

						hier.get(i).writeBlock(
								Integer.parseInt(AddressToReplace, 2), block);
					}
				}

			}
		}
		this.numberOfAccesses++;
	}
	
	
	
	void addToCache(int wordAddress)
			throws IndexOutOfMemoryBoundsException {
		String word = Integer.toBinaryString(wordAddress);
		for (int i = word.length(); i <= 16; i++) {
			word = "0" + word;
		}
		String tagBinary = word.substring(0, lengthTag);
		String offsetBinary = word.substring(lengthTag + lengthIndex + 1);
		// If i am removing a line from write back and it has a dirty bit
		// then I need to move to lower levels writing till I reach an other
		// write Back
		// or the memory

		// loop till i find tag
		// if found el3b
		// if not then check si`e lw fi makan zwedo
		// lw mfeesh kick awel element
		// so smart
		

		if (lines.size() < maxNumberLines) {
			CacheLineData temp = new CacheLineData(MainMemory.Read(wordAddress,
					this.BlockSize), tagBinary);
			lines.addLast(temp);
		} else {
			// remove the line
			if (this.WriteBack && DirtyBit[0]) {
				if (this.equals(hier.getLast())) {
					String memAddress = lines.getFirst().Tag;

					// adding zeroes to adjust for missing offset bits in
					// extracted address
					// this gives us the address of the start of block for
					// this specific cache
					for (int i = 0; i < this.lengthOffset; i++)
						memAddress = memAddress + "0";
					MainMemory.Insert(memAddress, lines.getFirst().Data,
							this.BlockSize);
				} else {
					boolean stop = false;
					String AddressToReplace = "";
					String binaryIndex = "";

					AddressToReplace = lines.getFirst().Tag;
					while (AddressToReplace.length() < 16) {
						AddressToReplace = AddressToReplace + "0";
					}
					String[] block = lines.getFirst().Data;

					DirectMappedCacheData temp;

					for (int i = hier.indexOf(this) + 1; i < hier.size()
							&& !stop; i++) {

						// fetch the address from the block and write on this
						// address at lowers levels
						if (hier.get(i).WriteBack) {
							stop = true;
						}
						// write in the addresses considering the fact that they
						// might be in different
						// indexes due to difference in block size
						// new write method which writes the whole data array
						// since same block size.
						// add address to index

						// pass index to start and da

						// get the address that i will replace

						hier.get(i).writeBlock(
								Integer.parseInt(AddressToReplace, 2), block);
					}
				}

			}
			else
			{
				CacheLineData temp = new CacheLineData(MainMemory.Read(wordAddress,
						this.BlockSize), tagBinary);
				lines.removeFirst();
				lines.addFirst(temp);
			}
		}
		this.numberOfAccesses++;
	}

	

	// adding a word Address from Memory
	
	public void writeBlock(int wordAddress, String[] data) {
		this.numberOfAccesses++;
		String word = Integer.toBinaryString(wordAddress);
		for (int i = word.length(); i <= 16; i++) {
			word = "0" + word;
		}
		String tagBinary = word.substring(0, lengthTag);
		String offsetBinary = word.substring(lengthTag + lengthIndex + 1);

		int c = -1;

		for (int i = 0; i < this.lines.size(); i++) {
			if (this.lines.get(i).Tag.equals(tagBinary))
				c = i;
		}
		if (c > -1) {
			lines.get(c).Data = data;
			if (this.WriteBack)
				this.DirtyBit[c] = true;
		} else {
			if (this.lines.size() < maxNumberLines)
				this.lines.addFirst(new CacheLineData(data, tagBinary));
			else {
				this.lines.removeFirst();
				this.lines.addFirst(new CacheLineData(data, tagBinary));
			}
		}
	}
	
	public double hitRatio() {
		return (this.numberOfAccesses - this.numberOfMisses)
				/ this.numberOfAccesses;
	}
	
	public double getMissRatio(){
		return this.numberOfMisses / this.numberOfAccesses;
	}

	public int getNumberOfAccess() {
		return (int) this.numberOfAccesses;
	}

	public int getNumberOfMisses() {
		return (int) this.numberOfMisses;
	}

	public String getStatistics() {
		return "--------------------------------------------------------\n"
				+ "The number of Accesses is: " + this.getNumberOfAccess()
				+ "\n" + "The number of misses is: " + this.getNumberOfMisses()
				+ "\n" + "The hit ratio is: " + this.getHitRatio()
				+ "\n" + "The miss ratio is: " + this.getMissRatio()
				+ "\nThe access time is: " + this.accessTime
				+ "\n--------------------------------------------------------";
	}
}
