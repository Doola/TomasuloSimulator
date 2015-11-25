package tomasuloProj;

public interface Cache {

	public void Read();
	public void Write();
	void Write(int index, String data, String tag);
	
}
