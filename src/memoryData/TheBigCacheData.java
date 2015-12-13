package memoryData;

import java.util.ArrayList;
import java.util.LinkedList;
import memory.*;

public class TheBigCacheData implements CacheData {
	int Size, BlockSize, assosciativity, lengthIndex, lengthOffset, lengthTag;
	double accessTime, numberOfMisses, numberOfAccesses;
	boolean WriteBack, WriteThrough;

	static LinkedList<TheBigCacheData> hier = new LinkedList<TheBigCacheData>();
	static int currentCachePosition = 0;

	public TheBigCacheData(int S, int L, int m) {
		this.Size = S;
		// assuming we are given number of words not bits
		this.BlockSize = L;
		this.assosciativity = m;
	}

	public void writeBlock(int wordAddress, String[] data) {

	}

	public String Read(int wordAddress) throws IndexOutOfMemoryBoundsException {

		// Loop through all levels of cache till we find the word is found
		for (int i = 0; i < hier.size(); i++) {
			// When the word is found in a cache level then must copy to
			// levels above.
			String word = hier.get(i).Read(wordAddress);
			if (word != null) {
				// adding required word address to levels of cache that did not
				// contain this word.
				// when I add the required address to the cache I need to check
				// before removing blocks if they are write back, then I need to
				// copy the data contained in them to the lower levels till I
				// hit
				// a write back cache

				for (int k = 0; k < i; k++) {
					hier.get(k).addToCache(wordAddress);
				}
				return word;
			}
		}

		// The address was not found in any cache level
		// this address is in main memory so we need to fetch it from there
		// then copy the block to all levels of cache.
		addToCache(wordAddress);
		String address = Integer.toBinaryString(wordAddress);
		while (address.length() < 16)
			address = "0" + address;
		return MainMemory.ReadTemp(address);
	}

	@Override
	// this should take only the byte of data that needs to be changed
	// because the size of blocks is different from level to level.
	public boolean Write(int wordAddress, String data)
			throws IndexOutOfMemoryBoundsException {

		for (int i = 0; i < hier.size(); i++) {
			if (hier.get(i).Write(wordAddress, data)) {
				// add data to lower levels to ensure consistency
				// add updated blocks to upper levels that did not contain the
				// block
				// if this level is writeBack stop
				// if write through then copy to lower levels till we reach
				// a write back level.

				boolean stop = false;
				if (hier.get(i).WriteBack)
					stop = true;
				for (int k = i + 1; k < hier.size() && !stop; k++) {
					if (hier.get(k).WriteBack)
						stop = true;
					// should always be a hit due to consistency.
					hier.get(k).Write(wordAddress, data);
				}
				// copy data to upper levels that did not contain the data.
				// The misses for these levels we are already counted
				// as we were exploring the hierarchy from top to bottom.
				// The next access will be counted as hits.
				for (int j = i - 1; j >= 0; j--)
					hier.get(j).addToCache(wordAddress, data);

				// if(!hier.get(i).WriteBack)
				// {
				// MainMemory.RAM.put(Integer.toBinaryString(wordAddress),
				// data);
				// }
				// need to loop over all levels copying the new data

				return true;
			}
		}

		// #jolly
		// must add to cache and then write
		// do i add it to cache then write or write mn el awel

		// add to all levels altering the data i want
		// I need use add to cache but i have to make sure the data i add is the
		// new
		addToCache(wordAddress, data);
		return false;

	}

	void addToCache(int wordAddress, String data)
			throws IndexOutOfMemoryBoundsException {
		for (int i = 0; i < hier.size(); i++)
			hier.get(i).addToCache(wordAddress, data);
	}

	void addToCache(int wordAddress) throws IndexOutOfMemoryBoundsException {
		for (int i = 0; i < hier.size(); i++)
			hier.get(i).addToCache(wordAddress);
	}

	public double getAverageMemoryAccessTime() {
		// loop over caches applying the formula
		// AMAT = hitTime + (missRate * AverageMissPenalty[n])
		// AverageMissPenalty[n] = hitTime[n] + (missRate[n] *
		// AverageMissPenalty[n+1])

		double amat = 0;
		double penalty = hier.get(hier.size() - 1).accessTime
				+ ((hier.get(hier.size() - 1).numberOfMisses / hier.get(hier
						.size() - 1).numberOfAccesses) * MainMemory.accessTime);
		for (int i = hier.size() - 2; i > 0; i--) {

			penalty = hier.get(i).accessTime
					+ ((hier.get(i).numberOfMisses / hier.get(i).numberOfAccesses) * penalty);
		}
		amat = hier.get(0).accessTime
				+ ((hier.get(0).numberOfMisses / hier.get(0).numberOfAccesses) * penalty);

		return amat;
	}

	double getHitRatio() {
		return (numberOfAccesses - numberOfMisses) / numberOfAccesses;
	}

	public void WriteData(int wordAddress, String[] data) {

	}

}
