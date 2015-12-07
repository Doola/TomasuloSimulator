package memoryData;

public interface CacheData {

	public String Read(int wordAddress) throws IndexOutOfMemoryBoundsExceptionData;
	public boolean Write(int wordAddress, String data) throws IndexOutOfMemoryBoundsExceptionData;
	
}
