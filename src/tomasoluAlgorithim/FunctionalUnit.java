package tomasoluAlgorithim;

public class FunctionalUnit {
	public FunctionalUnitName name;
	public InstructionName operation;
	public boolean busy;
	public Register Vi, Vj;
	int imm;
	public int Qi, Qj, ROBDST, Address;
	int cycles;
	public int destination;
	int cyclesRemaining;
	public String value;
	int instructionPosition;

	public FunctionalUnit(FunctionalUnitName name, InstructionName operation,
			boolean busy, Register Vi, Register Vj, int Qi, int Qj, int ROBDST,
			int Address, int destination, int imm) {
		this.name = name;
		this.Address = Address;
		this.Vi = Vi;
		this.Vj = Vj;
		this.operation = operation;
		this.ROBDST = ROBDST;
		this.Qi = Qi;
		this.Qj = Qj;
		this.busy = busy;
		this.destination = destination;
		this.imm = imm;
	}
	
	public FunctionalUnit(FunctionalUnitName name)
	{
		this.name = name;
	}
	
	
	
	public String toString() {
		return "--------------------------------------------\n" + "name ===> "
				+ this.name + "\nAddress ===> " + this.Address + "\nVi ===> "
				+ this.Vi + "\nVj ===> " + this.Vj + "\nOperation ===> "
				+ this.operation + "\nROB Destination ===> " + this.ROBDST
				+ "\nQi ===> " + this.Qi + "\nQj ===> " + this.Qj + "\nBusy ===>"
				+ this.busy + "\nDestination ===>" + this.destination
				+ "\nImmediate ===> " + this.imm
				+ "\n--------------------------------------------";
	}
	
}