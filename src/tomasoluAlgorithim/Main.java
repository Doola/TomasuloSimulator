package tomasoluAlgorithim;

import java.security.KeyRep.Type;
import java.util.ArrayList;

import sun.misc.Queue;
import memory.*;

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
	static boolean CanWrite = true;
	static Queue WriteQueu = new Queue();
	static Queue LoadStore = new Queue();
	static int NrOfBranches = 0;
	static int NrOfBranchesMispredicted = 0;
	static int cycle;
	static TheBigCacheData dataCache;
	static TheBigCache instructionCache;
	static MainMemory memory = new MainMemory();

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
		if (a.toString().equals("ADDI") && b.toString().equals("ADD"))
			return true;
		if (a.toString().equals("SUB") && b.toString().equals("ADD"))
			return true;
		if (a.toString().equals("MUL") && b.toString().equals("MULT"))
			return true;
		if (a.toString().equals("JMP") && b.toString().equals("ADD"))
			return true;
		if (a.toString().equals("BEQ") && b.toString().equals("ADD"))
			return true;
		if (a.toString().equals("JALR") && b.toString().equals("ADD"))
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
		// CurrentInstruction++;
	}

	public static void Issue() {

		// if beq and bne then instruction name is ADD
		if (ProgramCode[CurrentInstruction].Name.equals("BEQ")
				|| ProgramCode[CurrentInstruction].Name.equals("JMP")
				|| ProgramCode[CurrentInstruction].Name.equals("LW")
				|| ProgramCode[CurrentInstruction].Name.equals("SW")
				|| ProgramCode[CurrentInstruction].Name.equals("ADDI")) {

			if (ProgramCode[CurrentInstruction].Name.equals("BEQ")) // Awel ma
																	// ygeeli
																	// branch
																	// increment
																	// to get
																	// the total
																	// nr of
																	// branches
																	// in code.
				NrOfBranches++;
			int freeReservationStation = findFreeReservationStation(ProgramCode[CurrentInstruction].Name);

			ReservationStations[freeReservationStation].imm = ProgramCode[CurrentInstruction].immediateValue;
		}

		int freeReservationStation = findFreeReservationStation(ProgramCode[CurrentInstruction].Name);

		// If this reservation station is not busy insert instruction in it's
		// correct FU.
		if ((!ReservationStations[freeReservationStation].busy && head != tail)
				|| first) {
			first = false;
			ReservationStations[freeReservationStation].busy = true;

			ReservationStations[freeReservationStation].operation = ProgramCode[CurrentInstruction].Name;

			ReservationStations[freeReservationStation].destination = tail;

			if (!ProgramCode[CurrentInstruction].Name.toString().equals("RET")
					&& !(ProgramCode[CurrentInstruction].Name.toString()
							.equals("JMP"))) {
				if (FreeRegisterStatus(ProgramCode[CurrentInstruction].Rs)) {
					ReservationStations[freeReservationStation].Vi.Name = ProgramCode[CurrentInstruction].Rs;
				} else {
					String s = ProgramCode[CurrentInstruction].Rs.toString()
							.substring(1);
					int registerIndex = Integer.parseInt(s);
					ReservationStations[freeReservationStation].Qi = RegisterStatus[registerIndex];
				}
			}
			// //
			if (ProgramCode[CurrentInstruction].Name.toString().equals("ADD")
					|| ProgramCode[CurrentInstruction].Name.toString().equals(
							"MUL")
					|| ProgramCode[CurrentInstruction].Name.toString().equals(
							"SUB")
					|| ProgramCode[CurrentInstruction].Name.toString().equals(
							"NAND")) {

				if (FreeRegisterStatus(ProgramCode[CurrentInstruction].Rt)) {
					ReservationStations[freeReservationStation].Vj.Name = ProgramCode[CurrentInstruction].Rt;
				} else {
					String s = ProgramCode[CurrentInstruction].Rt.toString()
							.substring(1);
					int registerIndex = Integer.parseInt(s);
					ReservationStations[freeReservationStation].Qj = RegisterStatus[registerIndex];
				}

			}
			if (!ProgramCode[CurrentInstruction].Name.toString().equals("SW"))
				ROB[tail].Destination.Name = ProgramCode[CurrentInstruction].Rd;
			else
				ReservationStations[freeReservationStation].Vj.Name = ProgramCode[CurrentInstruction].Rd;

			if (ProgramCode[CurrentInstruction].Name.toString().equals("LW"))
				ROB[tail].type = ROBType.LD;
			else if (ProgramCode[CurrentInstruction].Name.toString().equals(
					"SW"))
				ROB[tail].type = ROBType.SD;
			else
				ROB[tail].type = ROBType.INT;
			// //////////

			if (ProgramCode[CurrentInstruction].Name.toString().equals("LW")
					|| ProgramCode[CurrentInstruction].Name.toString().equals(
							"SW")) {

				ReservationStations[freeReservationStation].Address = ProgramCode[CurrentInstruction].immediateValue;
			}

			CurrentInstruction++;

			// BRAAAAANCH!!!!!!!!!!!!!!!!!!!
			// / if its a beq,bne then if imm>0 pc+1 else if imm<0 pc +imm
			if ((ProgramCode[CurrentInstruction].Name.equals("BEQ") || ProgramCode[CurrentInstruction].Name
					.equals("BNE"))) {
				if (ProgramCode[CurrentInstruction].immediateValue < 0) {
					// currentInsr. was incremented before the if condition
					ROB[tail].PCBeforeBranch = CurrentInstruction - 1;
					ROB[tail].taken = true;
					CurrentInstruction += ProgramCode[CurrentInstruction].immediateValue - 1;
				} else
					ROB[tail].taken = false;
			}

			// BRAAAAANCH!!!!!!!!!!!!!!!!!!!

			// check if tail reached the end of table
			tail++;
			if (tail > ROBSize)
				tail = 1;
		}
	}

	// Called in execute to do the actual operation
	public static String CalculateValue(FunctionalUnit fu) {
		switch (fu.operation) {

		case JMP:
			return Integer.parseInt(ROB[fu.destination].Destination.Value)
					+ fu.imm + 1 + CurrentInstruction + "";
		case JALR:
			fu.Vj.Value = CurrentInstruction + 1 + "";
			return Integer.parseInt(fu.Vi.Value) + "";

		case RET:
			return Integer.parseInt(ROB[fu.destination].Destination.Value) + "";

		case BEQ:
			if (Integer.parseInt(fu.Vi.Value)
					- Integer.parseInt(ROB[fu.destination].Destination.Value) == 0) {
				if (!ROB[fu.destination].taken) {
					ROB[fu.destination].WrongPrediction = true;
					// wrong prediction , set rob flag false
				} else
					ROB[fu.destination].WrongPrediction = false;

			}
			break;
		case ADD:
			return Integer.parseInt(fu.Vi.Value)
					+ Integer.parseInt(fu.Vj.Value) + "";
		case SUB:
			return Integer.parseInt(fu.Vi.Value)
					- Integer.parseInt(fu.Vj.Value) + "";
		case ADDI:
			return Integer.parseInt(fu.Vi.Value) + fu.imm + "";
		case NAND:
			return ~(Integer.parseInt(fu.Vi.Value) & Integer
					.parseInt(fu.Vj.Value)) + "";
		case MUL:
			return Integer.parseInt(fu.Vi.Value)
					* Integer.parseInt(fu.Vj.Value) + "";
		}
		return null;
	}

	public static void Execute() {
		// loop over all functions in RS and execute them if operands are ready
		// execute: calculate the number of cycles need to finish execution/
		// add to write array
		// Actually execute
		for (int i = 0; i < ReservationStations.length; i++) {
			if (ReservationStations[i].Qi == 0
					&& ReservationStations[i].Qj == 0
					&& ReservationStations[i].cyclesRemaining >= 0) {
				// Execute the instruction then
				if (ReservationStations[i].cyclesRemaining == 0) {
					if (ReservationStations[i].operation.equals("SW")) {
						if (ReservationStations[i].Qi == 0) {
							// ////////
							ROB[ReservationStations[i].destination].Destination.Value = ReservationStations[i].Address
									+ ReservationStations[i].Vi.Value;

						}
					} else if (ReservationStations[i].operation.equals("LW")) {
						if (ReservationStations[i].Qi == 0 /* && no stores */) {
							// calculate value

							ReservationStations[i].Address = ReservationStations[i].Address
									+ Integer
											.parseInt(ReservationStations[i].Vi.Value);
							// check all stores have ROB have diff address
							// read from Memory
							// MEM[ReservationStations[i].Address] DOOLA and
							// store in rd
						}
					} else if (ReservationStations[i].operation.equals("JMP")
							|| ReservationStations[i].operation.equals("JALR")
							|| ReservationStations[i].operation.equals("RET")) {
						CurrentInstruction = Integer
								.parseInt(CalculateValue(ReservationStations[i]));
					}

					else
						ReservationStations[i].value = CalculateValue(ReservationStations[i]);

					// put the whole reservation station in a queue
					WriteQueu.enqueue(ReservationStations[i]);

				}

				ReservationStations[i].cyclesRemaining--;
			}
		}
	}

	public static void WriteBack() throws InterruptedException {
		// the conditions i need are can write and loop over fu that should
		// write bs

		if (!WriteQueu.isEmpty()) {
			FunctionalUnit temp = (FunctionalUnit) WriteQueu.dequeue(); // check
																		// if
																		// errors
																		// ,yasser:D

			if (temp.cyclesRemaining == -1 && CanWrite) {
				CanWrite = !CanWrite;
				ROB[temp.destination].Value = temp.value;
				// if store
				if (temp.name.equals("STORE") && temp.Qj == 0)
					ROB[temp.destination].Value = temp.Vj.Name.toString();

				ROB[temp.destination].Ready = true;

				String s = ROB[temp.destination].Destination.Name.toString()
						.substring(1);
				int registerIndex = Integer.parseInt(s);
				for (int k = 0; k < ReservationStations.length; k++) {
					if (ReservationStations[k].Qi == RegisterStatus[registerIndex])
						ReservationStations[k].Qi = 0;

					if (ReservationStations[k].Qj == RegisterStatus[registerIndex])
						ReservationStations[k].Qj = 0;
				}

				temp.busy = false;

			}
		}

	}

	public static void Commit() throws NumberFormatException,
			IndexOutOfMemoryBoundsException {
		// wrong prediction
		for (int i = 0; i < ROB.length; i++) {
			if (ROB[i].Ready && head == i) {
				if (!ROB[i].WrongPrediction) {
					// save value in Memory/REG then (cahce)
					// if store , Des= address, in memory ,Mem[rob.Dest]=value
					// (DONE)
					// all operations save rob[i].value in dest register/memory
					// in caches
					if (ROB[i].type.equals(ROBType.valueOf("SD"))) {
						dataCache.Write(
								Integer.parseInt(ROB[i].Destination.Value),
								ROB[i].Value);
					}
					ROB[i].Ready = false;
					String s = ROB[i].Destination.toString().substring(1);
					int registerIndex = Integer.parseInt(s);
					RegisterStatus[registerIndex] = 0;
					head++;
					if (head > ROB.length)
						head = 1;
				} else {
					if (ROB[i].taken)
						// cI=pcbeforebranch++
						CurrentInstruction = ROB[i].PCBeforeBranch++;
					else
						CurrentInstruction = ROB[i].PCBeforeBranch
								- ProgramCode[ROB[i].PCBeforeBranch].immediateValue
								+ 1;
					// cI= pcbefroe branch - imm +1

					ROB = new ROBEntry[ROBSize]; // flush rob
					RegisterStatus = new int[NumberOfRegisters]; // clear
																	// reg.status
					NrOfBranchesMispredicted++; // The number of times we flush
												// the ROB indicates the number
												// of times it was a
												// mispredicton
				}
			}

		}

	}

	public static void main(String[] args) throws InterruptedException {

		// intialise memory we hakaza


		int s = 16 * 1024;
		int l = 16;

		// Data Cache
		dataCache = new TheBigCacheData(s, l, 1);
		FullyAsosciativeCacheData a = new FullyAsosciativeCacheData(s, l, 3);
		DirectMappedCacheData b = new DirectMappedCacheData(s, l, 1);
		SetAssociativeData c = new SetAssociativeData(s, l, 3);
		CacheLineData fully = new CacheLineData(new String[] { "yasser",
				"read", "works" }, "000000000000");
		CacheLineData temp2 = new CacheLineData(new String[] { "yaaay",
				"read2", "works" }, "000000");
		CacheLineData temp = new CacheLineData(new String[] { "yasta",
				"el so7ab", "fi", "agaza" }, "000001");
		// b.lines[0] = temp;
		c.cache[0].Lines[2] = temp;
		a.lines.add(fully);
		b.lines[0] = temp2;

		// Instruction Cache

		instructionCache = new TheBigCache(s, l, 1);
		FullyAsosciativeCache a2 = new FullyAsosciativeCache(s, l, 3);
		DirectMappedCache b2 = new DirectMappedCache(s, l, 1);
		SetAssociative c2 = new SetAssociative(s, l, 3);
		CacheLine fully2 = new CacheLine(new String[] { "yasser", "read",
				"works" }, "000000000000");
		CacheLine temp22 = new CacheLine(new String[] { "yaaay", "read2",
				"works" }, "000000");
		CacheLine temp1 = new CacheLine(new String[] { "yasta", "el so7ab",
				"fi", "agaza" }, "000001");
		// b.lines[0] = temp;
		// c.cache[0].Lines[2] = temp;
		// a.lines.add(fully);
		b2.lines[0] = temp22;

		try {
			dataCache.Write(1, "yad");
			System.out.println(instructionCache.Read(1));
		} catch (IndexOutOfMemoryBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// intialise memory we hakaza

		dataCache = new TheBigCacheData(16 * 1024, 16, 1);

		for (int i = 1; i <= cycle; i++) {
			Fetch();
			Issue();
			Execute();
			WriteBack();
			// cycles++;
		}

		if (NrOfBranches != 0) {
			int TotatlMisprediction = NrOfBranchesMispredicted / NrOfBranches;
		}
	}
}
