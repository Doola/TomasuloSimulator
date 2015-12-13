package tomasoluAlgorithim;

public class ROBEntry {

	public Register Destination;
	public String Value;
	public Boolean Ready;
	public Enum<ROBType> type;
	public int index;
	public boolean WrongPrediction;
	boolean taken;
	int PCBeforeBranch;

	public ROBEntry(Register Destination, String Value, Boolean Ready,
			Enum<ROBType> type) {
		this.Destination = Destination;
		this.Value = Value;
		this.Ready = Ready;
		this.type = type;
	}

	public String toString() {
		return "--------------------------------------------\n"
				+ " Destination ===> " + this.Destination.Name + " \nValue ===> "
				+ this.Value + " \nReady ===> " + this.Ready + " \nType ===> "
				+ this.type.toString() + " \nIndex ===> " + this.index
				+ " \nWrong prediction ==>" + this.WrongPrediction + "\nTaken ==> "
				+ this.taken + "\nPC before branch ==>" + this.PCBeforeBranch;

	}
}
