package tomasuloProj;

import java.util.LinkedList;

public class FullyAsosciativeCache extends TheBigCache implements Cache{
	
	
	LinkedList<CacheLine> lines;
	boolean[] DirtyBit;
	
	public FullyAsosciativeCache(int S, int L, int m) {
		super(S, L, m);
		
	}
	
	public String Read(int wordAddress)
	{
		return null;
	}
	public boolean Write(int wordAddress, String data)
	{
		return false;
	}

}
