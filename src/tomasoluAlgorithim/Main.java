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
	static boolean CanWrite =true;
	static Queue WriteQueu = new Queue();
	

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
		
		int freeReservationStation = findFreeReservationStation(ProgramCode[CurrentInstruction].Name);
		if ((!ReservationStations[freeReservationStation].busy && head != tail)
				|| first) 
		{
			first = false;
			ReservationStations[freeReservationStation].busy = true;
			ReservationStations[freeReservationStation].operation = ProgramCode[CurrentInstruction].Name;
			ReservationStations[freeReservationStation].destination = tail;
			// add conditions for load and store
			// *********************************
			// *********************************
			// *********************************
			// *********************************
			// *********************************
			// *********************************
			
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
			
			if(ProgramCode[CurrentInstruction].Name.toString().equals("SW"))
			{
				ReservationStations[freeReservationStation].Address=ProgramCode[CurrentInstruction].immediateValue;
			}
			
			if(ProgramCode[CurrentInstruction].Name.toString().equals("SW"))
				ROB[tail].type = ROBType.SD;
			else if (ProgramCode[CurrentInstruction].Name.toString().equals("LW"))
				ROB[tail].type = ROBType.LD;
			else
				ROB[tail].type = ROBType.INT;
			
			ROB[tail].Destination.Name = ProgramCode[CurrentInstruction].Rd;
			tail++;
			
			// check if tail reached the end of table
			if (tail > ROBSize)
				tail = 1;
		} 
	}

	public static void Execute() 
	{
		// loop over all functions in RS and execute them if operands are ready
		// execute: calculate the number of cycles need to finish execution/
		// add to write array
		// actaully execute
		for (int i = 0; i < ReservationStations.length; i++)
		{
			if(ReservationStations[i].Qi ==0 && ReservationStations[i].Qj==0 && ReservationStations[i].cyclesRemaining >= 0)
				
			{
				//Execute the instruction then
				
				if(ReservationStations[i].cyclesRemaining == 0)
				{
				   ReservationStations[i].value="store the value bitch"; //call func calculate value (check load store in lec 14)
				   // put the whole reservation station in a queue
				   WriteQueu.enqueue(ReservationStations[i]);
				 
				}
				
				ReservationStations[i].cyclesRemaining --;
			}
		}
	}

	public static void WriteBack() throws InterruptedException {
		// the conditions i need are can write and loop over fu that should write bs
		
		if(!WriteQueu.isEmpty()){
			 FunctionalUnit temp = (FunctionalUnit)WriteQueu.dequeue(); // check if errors ,yasser:D
			if(temp.cyclesRemaining == -1 && CanWrite)
			{
				CanWrite = !CanWrite;
				ROB[temp.destination].Value = temp.value;
				//if store
				if(temp.name.equals("STORE") && temp.Qj == 0)
					ROB[temp.destination].Value = temp.Vi.Name.toString();
				ROB[temp.destination].Ready =true;
			
				String s = ROB[temp.destination].Destination.Name.toString().substring(1);
				int registerIndex = Integer.parseInt(s);
				for (int k = 0; k < ReservationStations.length; k++)
				{
					if(ReservationStations[k].Qi == RegisterStatus[registerIndex])
						ReservationStations[k].Qi =0;
					
					if(ReservationStations[k].Qj == RegisterStatus[registerIndex])
						ReservationStations[k].Qj =0;
				}
				
				temp.busy=false;
				RegisterStatus[registerIndex]=0;
				
				
			}
		}
		
	}

	public static void Commit() 
	{
		//wrong prediction 
		for (int i = 0; i < ROB.length; i++) 
		{
			if(ROB[i].Ready && head==i)
			{
				// save value in Memory/REG then (cahce)
				// if store , des= address, in memory ,Mem[rob.Dest]=value
				head ++;
				if(head>ROB.length)
					head=1;
			}
			
		}
		
	}

	public static void main(String[] args) throws InterruptedException {
		
		while(true
				)
		{
			Fetch();
			Issue();
			Execute();
			WriteBack();
//			cycles++;
		}
	}
}