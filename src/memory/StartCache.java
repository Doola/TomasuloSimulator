package memory;
import sun.print.resources.serviceui;
import memory.TheBigCache;

public class StartCache {

	public static void main(String[]args) throws NumberFormatException, IndexOutOfMemoryBoundsException{
		int s = 16*1024;
		int l = 16;
		
		String add1 = "0010101011100101";
		String add2 = "0001010101100101";
		String add3 = "0000101011100101";
		String add4 = "0011101011100101";
		String add5 = "0010101011101101";
		String add6 = "0010101011100100";
		String add7 = "0001010101100111";
		String add8 = "0000101011100011";
		String add9 = "0011101000100101";
		String add10 = "0010110111101101";
		String add11 = "0010111111100101";
		String add12 = "0001010101111111";
		String add13 = "0000011011100101";
		String add14 = "0000101011100101";
		String add15 = "0010101011101101";
		String add16 = "0010100001100101";
		String add17 = "0001010101100000";
		String add18 = "0000101010100100";
		String add19 = "0011100001100101";
		String add20 = "0000001001101101";
		String doola = "0000000000000001";
		// index 6 bits
		// offset 4 bits
		// tag 6 bits
			
		
		MainMemory memory = new MainMemory();
		
		memory.RAM.put(add1, "adeeeeeeeeeek");
		memory.RAM.put(add2, "yad");
		memory.RAM.put(add3, "ya Doola");
		memory.RAM.put(add4, "el");
		memory.RAM.put(add5, "tary");
		memory.RAM.put(add6, "adeeeeeeeeeek1");
		memory.RAM.put(add7, "yad1");
		memory.RAM.put(add8, "ya Doola1");
		memory.RAM.put(add9, "el1");
		memory.RAM.put(add10, "tary1");
		memory.RAM.put(add11, "adeeeeeeeeeek2");
		memory.RAM.put(add12, "yad2");
		memory.RAM.put(add13, "ya Doola2");
		memory.RAM.put(add14, "el2");
		memory.RAM.put(add15, "tary2");
		memory.RAM.put(add16, "adeeeeeeeeeek3");
		memory.RAM.put(add17, "yad3");
		memory.RAM.put(add18, "ya Doola3");
		memory.RAM.put(add19, "el3");
		memory.RAM.put(add20, "tary3");
		memory.RAM.put("0000000000000000", "Oh yeah !!!");
		
		
		TheBigCache cache = new TheBigCache(s,l,1);
		FullyAsosciativeCache a = new FullyAsosciativeCache(s,l,3);
		FullyAsosciativeCache b = new FullyAsosciativeCache(s,l,1);
		FullyAsosciativeCache c = new FullyAsosciativeCache(s,l,3);
		//cache.hier.add(a);
		//cache.hier.add(b);
		//cache.hier.add(c);
		
		
		//CacheLine temp = new CacheLine(new String[]{"yaaay","read","works"}, "000000");
		//b.lines[0] = temp;
		//c.cache[0].Lines[2] = temp;

		//cache.hier.get(0).Write(Integer.parseInt(add1,2), "adeeeeeeeeeek");
		cache.Write(0, "yad");
		//cache.Write(Integer.parseInt(add3,2), "ya Doola");
		//cache.Write(Integer.parseInt(add4,2), "el");
		//cache.Write(Integer.parseInt(add5,2), "tary");
		//cache.Write(Integer.parseInt(add6,2), "adeeeeeeeeeek1");
//		memory.RAM.put(add7, "yad1");
//		memory..put(add8, "ya Doola1");
//		memory.RAM.put(add9, "el1");
//		memory.RAM.put(add10, "tary1");
//		memory.RAM.put(add11, "adeeeeeeeeeek2");
//		memory.RAM.put(add12, "yad2");
//		memory.RAM.put(add13, "ya Doola2");
//		memory.RAM.put(add14, "el2");
//		memory.RAM.put(add15, "tary2");
//		memory.RAM.put(add16, "adeeeeeeeeeek3");
//		memory.RAM.put(add17, "yad3");
//		memory.RAM.put(add18, "ya Doola3");
//		memory.RAM.put(add19, "el3");
//		memory.RAM.put(add20, "tary3");
//		
		System.out.println(cache.Read(0));
		}
}