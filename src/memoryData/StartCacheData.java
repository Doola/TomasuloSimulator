package memoryData;
import sun.print.resources.serviceui;
import memoryData.TheBigCacheData;
import memory.*;

public class StartCacheData {
	
	
	public static String covertToBinary(int n)
	{
		String res = Integer.toBinaryString(n);
		while(res.length() < 16)
			res = "0" + res;
		return res;
	}
	
	

	public static void main(String[]args) throws NumberFormatException, IndexOutOfMemoryBoundsException{
		int s = 16*1024;
		int l = 16;
		
		String add111 = "0000000000000001";		// 	1
		String add211 = "0000000000000011";		// 	3
		String add311 = "0000000000000111";		//	7
		String add411 = "0000000000001111";		//	15
		String add511 = "0000000000011111";		//	31
		String add611 = "0010101011100100";		//	10980
		String add711 = "0001010101100111";		//	5479
		String add811 = "0000101011100011";		//	2787
		String add911 = "0011101000100101";		//	14885
		String add101 = "0010110111101101";		//	11757
		String add122 = "0010111111100101";		//	12261
		String add121 = "0001010101111111";		//	5503
		String add131 = "0000011011100101";		//	1765
		String add141 = "0000101011100101";		//	2789
		String add151 = "0010101011101101";		//	10989
		String add161 = "0010100001100101";		//	10341
		String add171 = "0001010101100000";		//	5472
		String add181 = "0000101010100100";		//	2724
		String add191 = "0011100001100101";		//	14437
		String add201 = "0000001001101101";		//	621
		String doola1 = "0000001000000001";		//	513
		
		// index 6 bits
		// offset 4 bits
		// tag 6 bits
			
		
		MainMemory memory = new MainMemory();
		
		memory.RAM.put(add111, "adeeeeeeeeeek");
		memory.RAM.put(add211, "yad");
		memory.RAM.put(add311, "ya Doola");
		memory.RAM.put(add411, "el");
		memory.RAM.put(add511, "tary");
		memory.RAM.put(add611, "adeeeeeeeeeek1");
		memory.RAM.put(add711, "yad1");
		memory.RAM.put(add811, "ya Doola1");
		memory.RAM.put(add911, "el1");
		memory.RAM.put(add101, "tary1");
		memory.RAM.put(add122, "adeeeeeeeeeek2");
		memory.RAM.put(add121, "yad2");
		memory.RAM.put(add131, "ya Doola2");
		memory.RAM.put(add151, "el2");
		memory.RAM.put(add161, "tary2");
		memory.RAM.put(add171, "adeeeeeeeeeek3");
		memory.RAM.put(add181, "yad3");
		memory.RAM.put(add191, "ya Doola3");
		memory.RAM.put(add201, "el3");
		memory.RAM.put(doola1, "tary3");

		
		
		TheBigCacheData cache = new TheBigCacheData(s,l,1);
		DirectMappedCacheData a = new DirectMappedCacheData(s,l,1);
		FullyAsosciativeCacheData b = new FullyAsosciativeCacheData(s,l,16);
		SetAssociativeData c = new SetAssociativeData(s,l,3);
		//cache.hier.add(a);
		//cache.hier.add(b);
		//cache.hier.add(c);

	
		cache.Write(1, "el3b yasta");
		System.out.println(cache.Read(1));		//	cache miss
		cache.Read(3);		//	cache hit
		cache.Read(1);		//	cache hit
		cache.Read(1);		//	cache hit
		cache.Read(1);		//	cache hit
		cache.Read(1);		//	cache hit
		cache.Read(1);		//	cache hit
		cache.Read(1);		//	cache hit
		cache.Read(1);		//	cache hit
		cache.Read(1);		//	cache hit
		cache.Read(1);		//	cache hit
		cache.Read(1);		//	cache hit
		cache.Read(1);		//	cache hit
		cache.Read(1);		//	cache hit
		cache.Read(1);		//	cache hit
		cache.Read(1);		//	cache hit
		cache.Read(10980);
		System.out.println(cache.Read(1));
		System.out.println(a.hitRatio());
		System.out.println(a.getStatistics());
		System.out.println(b.getStatistics());
		System.out.println(c.getStatistics());
		//System.out.println();
		}
}