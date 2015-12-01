package tomasoluAlgorithim;

public class FunctionalUnit {
	FunctionalUnitName name;
	InstructionName operation;
	boolean busy;
	Register Vi, Vj;
	int Qi, Qj, ROBDST, Address;
	int cycles;
	int destination;

	public FunctionalUnit(FunctionalUnitName name, InstructionName operation,
			boolean busy, Register Vi, Register Vj, int Qi, int Qj, int ROBDST,
			int Address, int destination) {
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
	}
}