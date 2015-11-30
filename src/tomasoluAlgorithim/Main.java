package tomasoluAlgorithim;

import java.util.ArrayList;

import sun.misc.Queue;

public class Main {

	static int ROBSize;
	static int NumberOfReservationStations;
	static int NumberOfRegisters;
	static int NumberOfInstructions = 0;
	static ROBEntry[] ROB = new ROBEntry[ROBSize];
	static int head = 1;
	static int tail = 1;
	static FunctionalUnit[] ReservationStations = new FunctionalUnit[NumberOfReservationStations];
	static int[] RegisterStatus = new int[NumberOfRegisters];
	static Instruction[] ProgramCode = new Instruction[NumberOfInstructions];
	static String[] InputProgramCode;
	static boolean Issuing;
	static Register[] RegisterFile = new Register[32];
	static int CurrentInstruction = 0;
	static ArrayList<Stage>[] TheBigTable;
	static boolean first = true;

	// static Queue ROBTable;

	public Main() {
		for (int i = 1; i < 32; i++) {
			RegisterFile[i] = new Register(RegisterName.valueOf("R" + i), "");
		}
		RegisterFile[0] = new Register(RegisterName.valueOf("R0"), "0");
	}

	public static void Intialise() {
		// numberOfInstructions = ????

		TheBigTable = (ArrayList<Stage>[]) new ArrayList[NumberOfInstructions];
	}

	public static boolean FunctionalUnitNameToInstructionName(
			InstructionName a, FunctionalUnitName b) {
		// check the rest of the instructions
		// **************************************
		// **************************************
		// **************************************
		// **************************************
		// **************************************
		// **************************************
		if (a.toString().equals("LW") && b.toString().equals("LOAD"))
			return true;
		if (a.toString().equals("SW") && b.toString().equals("STORE"))
			return true;
		if (a.toString().equals("ADD") && b.toString().equals("ADD"))
			return true;
		if (a.toString().equals("SUB") && b.toString().equals("ADD"))
			return true;
		if (a.toString().equals("MUL") && b.toString().equals("MULT"))
			return true;
		return false;
	}

	public static int findFreeReservationStation(InstructionName a) {
		for (int i = 0; i < ReservationStations.length; i++) {
			if (FunctionalUnitNameToInstructionName(a,
					ReservationStations[i].name)) {
				return i;
			}
		}
		return -1;
	}

	public static boolean FreeRegisterStatus(RegisterName R) {
		String s = R.toString().substring(1);
		int registerIndex = Integer.parseInt(s);
		if (RegisterStatus[registerIndex] == 0) // start ROB index from 1
		{
			return true;
		}

		return false;
	}

	public static void Fetch() {
		// get instruction from cache
		CurrentInstruction++;
	}

	public static void Issue() {
		// check if there is an empty functional unit to server instruction

		int freeReservationStation = findFreeReservationStation(ProgramCode[CurrentInstruction].Name);

		if (!ReservationStations[freeReservationStation].busy && head != tail && !first) // and rob is has an empty slot
		{
			ReservationStations[freeReservationStation].busy = true;
			ReservationStations[freeReservationStation].operation = ProgramCode[CurrentInstruction].Name;
			// add conditions for load and store
			// *********************************
			// *********************************
			// *********************************
			// *********************************
			// *********************************
			// *********************************
			// Check Register status, if register is free
			if (FreeRegisterStatus(ProgramCode[CurrentInstruction].Rs)) {
				ReservationStations[freeReservationStation].Vi.Name = ProgramCode[CurrentInstruction].Rs;
			} else {
				String s = ProgramCode[CurrentInstruction].Rs.toString()
						.substring(1);
				int registerIndex = Integer.parseInt(s);
				ReservationStations[freeReservationStation].Qi = RegisterStatus[registerIndex];
			
			}
			if (FreeRegisterStatus(ProgramCode[CurrentInstruction].Rd)) {
				ReservationStations[freeReservationStation].Vj.Name = ProgramCode[CurrentInstruction].Rd;
			} else {
				String s = ProgramCode[CurrentInstruction].Rd.toString()
						.substring(1);
				int registerIndex = Integer.parseInt(s);
				ReservationStations[freeReservationStation].Qj = RegisterStatus[registerIndex];
				
			}
			// if inst. is load or store , type should be equal to ld,st
			ROB[tail].type=ROBType.INT;
			ROB[tail].Destination.Name = ProgramCode[CurrentInstruction].Rd;

		}
		else if(first) first = false;
//		if (!ReservationStations[freeReservationStation].busy && head != tail && first) 
//		{
//			first = !first;
//		}

	}

	public static void Execute() {

	}

	public static void WriteBack() {

	}

	public static void Commit() {

	}

	public static void main(String[] args) {

	}
}
