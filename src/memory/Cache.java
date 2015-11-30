package memory;

public interface Cache {

	public String Read(int wordAddress);
	public boolean Write(int wordAddress, String data);
	
}
