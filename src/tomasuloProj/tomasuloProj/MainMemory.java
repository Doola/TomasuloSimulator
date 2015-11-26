package tomasuloProj;

import java.util.HashMap;


public class MainMemory {
	
	static HashMap<String, String> RAM;
	
	public MainMemory()
	{
		RAM = new HashMap<String, String>();
	}
	
	// Must check that address of data is not > 64KB
	public static void Insert(String address , String data, int blockSize)
	{
		// extracting 16 bit words from given block
		String[] dataCopied = new String[blockSize];
		for(int i=0; i<blockSize; i++)
		{
			dataCopied[i] = data.substring(i*16, i*16+16);
		}
		
		// placing extracted words in corresponding addresses
		int add = Integer.parseInt(address,2);
		for(int i=0; i<blockSize; i++)
		{
			RAM.put(Integer.toBinaryString(add),dataCopied[i]);
			add++;
		}
	}
	
	public static String Read(int wordAddress, int blockSize)
	{
		// must return block equivilant to block size of cache
		// fetch addresses in order till block is full
		
		String block = "";
		for(int i=0; i<blockSize; i++)
		{
			String address = Integer.toBinaryString(wordAddress);
			block += RAM.get(address);
			wordAddress++;
		}
		
		return block;
	
	}
	
	
	public static String ReadTemp(String wordAddress)
	{
		return RAM.get(wordAddress);
	}
	
	

}
