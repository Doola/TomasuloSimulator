package tomasuloProj;

public class TheBigCache {
	int Size, BlockSize, assosciativity, lengthIndex, lengthOffset, lengthTag;
	
	boolean WriteBack, WriteThrough;
	
	public TheBigCache(int S, int L, int m)
	{
		this.Size = S;
		this.BlockSize = BlockSize;
		this.assosciativity = m;
	}

}
