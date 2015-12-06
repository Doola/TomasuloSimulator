package tomasoluAlgorithim;

public class ROBEntry
{
	
	Register Destination; 
	String Value;
	Boolean Ready;
	Enum<ROBType> type;
	int index ;
	boolean WrongPrediction;
	boolean taken;
	int PCBeforeBranch;
	
	public ROBEntry(Register Destination, String Value, Boolean Ready, Enum<ROBType> type)
	{
		this.Destination = Destination;
		this.Value = Value;
		this.Ready = Ready;
		this.type = type;
	}
}
