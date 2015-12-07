package memoryData;

import java.util.HashMap;

public class MainMemory {

	static HashMap<String, String> RAM;
	static double accessTime = 0.0;

	public MainMemory() {
		RAM = new HashMap<String, String>();
	}

	public static double getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(double n) {
		accessTime = n;
	}

	public static void Insert(String address, String[] data, int blockSize)
			throws IndexOutOfMemoryBoundsExceptionData {
		// If the address given is out of memory then throw exception.
		if (Integer.parseInt(address, 2) + blockSize > 65535) {
			throw new IndexOutOfMemoryBoundsExceptionData();
		}
		// Adding data to memory in order.
		int add = Integer.parseInt(address, 2);
		for (int i = 0; i < data.length; i++) {
			RAM.put(Integer.toBinaryString(i + add), data[i]);
		}
	}
	
	public static void WriteWord(String address, String data) {
		// This will take address of a word and word itself to add
		// We need this method because each cache has a different block size
		RAM.put(address, data);
	}


	public static String[] Read(int wordAddress, int blockSize) {
		// we pass the starting address of the block we want to read
		String[] block = new String[blockSize];
		
		for (int i = 0; i < block.length; i++) {
			String address = Integer.toBinaryString(wordAddress + i);
			while(address.length() < 16)
				address = "0" + address;
			block[i] = RAM.get(address);
		}
		return block;
	}

	public static String ReadTemp(String wordAddress) {
		return RAM.get(wordAddress);
	}

}
