package tomasoluAlgorithim;

public class Instruction 
{
	InstructionName Name;
	RegisterName Rs,Rd,Rt;
	Stage Stage;
	int immediateValue;
	int executionCycles;
	String instructionString;
	// Instructions manual
	// *****************************************************************
	// JMP regA, imm ============> regA = Rd
	// LW regA, regB, imm =======> regA = Rd; regB = Rs
	// SW regA, regB, imm =======> regA = Rd; regB = Rs
	// BEQ regA, regB, imm ======> regA = Rd; regB = Rs
	// JALR regA, regB ==========> regA = Rd; regB = Rs
	// RET regA =================> regA = Rd
	// ADD regA, regB, regC =====> regaA = Rd; regB = Rs; regC = Rt
	// SUB regA, regB, regC =====> regaA = Rd; regB = Rs; regC = Rt
	// NAND regA, regB, regC ====> regaA = Rd; regB = Rs; regC = Rt
	// MUL regA, regB, regC =====> regaA = Rd; regB = Rs; regC = Rt
	// ADDI regA, regB, imm =====> regaA = Rd; regB = Rs; 
	// ******************************************************************
	
	
	
	// for instructions with three registers.
	public Instruction(InstructionName Name, RegisterName s, RegisterName t, RegisterName d)
	{
		this.Name = Name;
		this.Rs = t;
		this.Rt = d;
		this. Rd = s;
		this.Stage = Stage.NEW;
		this.instructionString = Name.toString() + " " + s.name().toString() + ", " + t.name().toString() + ", " + d.name().toString() ;
	}
	
	// for instructions with two registers and immediate value.
	public Instruction(InstructionName Name, RegisterName s, RegisterName t, int immediateValue)
	{
		this.Name = Name;
		this.Rd = s;
		this.Rs = t;
		this.immediateValue = immediateValue;
		this.Stage = Stage.NEW;
		this.instructionString = Name.toString() + " " + s.name().toString() + ", " + t.name().toString() + immediateValue;
	}
	
	// for instructions with two registers.
	public Instruction(InstructionName Name, RegisterName s, RegisterName t)
	{
		this.Name = Name;
		this.Rd = s;
		this.Rs = t;
		this.Stage = Stage.NEW;
		this.instructionString = Name.toString() + " " + s.name().toString() + ", " + t.name().toString();
	}
	
	// for instructions with one register.
	public Instruction(InstructionName Name, RegisterName s)
	{
		this.Name = Name;
		this.Rd = s;
		this.Stage = Stage.NEW;
		this.instructionString = Name.toString() + " " + s.name().toString();
	}
	
	// for instructions with one register and immediate value.
	public Instruction(InstructionName Name, RegisterName s, int imm)
	{
		this.Name = Name;
		this.Rd = s;
		this.immediateValue = imm;
		this.instructionString = Name.toString() + " " + s.name().toString() + ", " + imm;
	}
	
	public String getInstructionString()
	{
		return this.instructionString;
	}

}
