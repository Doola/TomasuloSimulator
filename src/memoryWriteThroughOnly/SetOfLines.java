package memoryWriteThroughOnly;


public class SetOfLines{
	public CacheLine [] Lines;
	
	public SetOfLines(int size){
		Lines = new CacheLine [size];
		for (int i = 0; i < Lines.length; i++) {
			Lines[i] = new CacheLine(null,null);
		}
	}
	
}
	
