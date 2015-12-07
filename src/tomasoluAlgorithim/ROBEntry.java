package tomasoluAlgorithim;

public class ROBEntry
{
	
	public Register Destination; 
	public String Value;
	public Boolean Ready;
	public Enum<ROBType> type;
	public int index ;
	public boolean WrongPrediction;
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
