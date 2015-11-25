package tomasuloProj;

import java.util.ArrayList;


public class FullyAssociativeCache implements Cache {
	ArrayList<CacheLine> lines = new ArrayList<CacheLine>();
	int maxNoLines;
	Write write;
	int LineSize;
	boolean [] dirtyBits;
	
	public FullyAssociativeCache(int lineSize, int cacheSize, Write write) {
		maxNoLines = cacheSize / lineSize;
		this.write=write;
		this.LineSize=LineSize;
		dirtyBits = new boolean[cacheSize];
	}

	@Override
	public void Read() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Write(int index, String data,String tag) {
		if (lines.size() < maxNoLines) {
			lines.add(new CacheLine(data,tag));
			if(write== Write.WriteBack)
				dirtyBits[maxNoLines-1]=true;
			else if(write == Write.WriteThrough){
				MainMemory.Insert(Integer.parseInt(tag, 2),Integer.parseInt(data, 2));
			}
		}else{
			lines.remove(0);
			lines.add(new CacheLine(data,tag));
		}
	}
	
	@Override
	public void Write() {
		// TODO Auto-generated method stub
		
	}

}
