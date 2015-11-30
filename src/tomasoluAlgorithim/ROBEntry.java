package tomasoluAlgorithim;

public class ROBEntry
{
	
	Register Destination; 
	String Value;
	Boolean Ready;
	Enum<ROBType> type;
	
	public ROBEntry(Register Destination, String Value, Boolean Ready, Enum<ROBType> type)
	{
		this.Destination = Destination;
		this.Value = Value;
		this.Ready = Ready;
		this.type = type;
	}
}
