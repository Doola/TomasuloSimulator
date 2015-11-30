package tomasuloProj;

public class FU {
	FU_Name name;
	InstructionName operation;
	boolean busy;
	Register Vi, Vj;
	int Qi, Qj, ROBDST, Address;

	public FU(FU_Name name, InstructionName operation, boolean busy,
			Register Vi, Register Vj, int Qi, int Qj, int ROBDST, int Address) {
		this.name = name;
		this.Address = Address;
		this.Vi = Vi;
		this.Vj = Vj;
		this.operation = operation;
		this.ROBDST = ROBDST;
		this.Qi = Qi;
		this.Qj = Qj;
		this.busy = busy;
	}
}
