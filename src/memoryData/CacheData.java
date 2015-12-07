package memoryData;
import memory.*;

public interface CacheData {

	public String Read(int wordAddress) throws IndexOutOfMemoryBoundsException;
	public boolean Write(int wordAddress, String data) throws IndexOutOfMemoryBoundsException;
	
}
