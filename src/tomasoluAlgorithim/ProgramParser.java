package tomasoluAlgorithim;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
public class ProgramParser {
	
	

	public  ArrayList<Instruction> ReadProgram(String filePath)
			throws IOException {

		return processStream(getProgram(filePath));
	}
	

	public ArrayList<Instruction> processStream(ArrayList<String> lines) {
		ArrayList<Instruction> out = new ArrayList<Instruction>();
		Instruction tempInstruction = null;
		InstructionName name;
		String[] temp;
		for (int i = 0; i < lines.size(); i++) {
			temp = lines.get(i).replaceAll("[,;]", "").split(" ");

			switch (temp[0]) {
			case "LW":
				name = InstructionName.LW;
				tempInstruction = new Instruction(name,
						RegisterName.valueOf(temp[1]),
						RegisterName.valueOf(temp[2]),
						Integer.parseInt(temp[3]));
						break;
			case "SW":
				name = InstructionName.SW;
				tempInstruction = new Instruction(name,
						RegisterName.valueOf(temp[1]),
						RegisterName.valueOf(temp[2]),
						Integer.parseInt(temp[3]));
						break;
			case "ADDI":
				name = InstructionName.ADDI;
				tempInstruction = new Instruction(name,
						RegisterName.valueOf(temp[1]),
						RegisterName.valueOf(temp[2]),
						Integer.parseInt(temp[3]));
						break;
			case "BREQ":
				name = InstructionName.BEQ;
				tempInstruction = new Instruction(name,
						RegisterName.valueOf(temp[1]),
						RegisterName.valueOf(temp[2]),
						Integer.parseInt(temp[3]));
						break;
			case "ADD":
				name = InstructionName.ADD;
				tempInstruction = new Instruction(name,
						RegisterName.valueOf(temp[1]),
						RegisterName.valueOf(temp[2]),
						RegisterName.valueOf(temp[3]));
						break;
			case "SUB":
				name = InstructionName.SUB;
				tempInstruction = new Instruction(name,
						RegisterName.valueOf(temp[1]),
						RegisterName.valueOf(temp[2]),
						RegisterName.valueOf(temp[3]));
						break;
			case "NAND":
				name = InstructionName.NAND;
				tempInstruction = new Instruction(name,
						RegisterName.valueOf(temp[1]),
						RegisterName.valueOf(temp[2]),
						RegisterName.valueOf(temp[3]));
						break;
			case "MUL":
				name = InstructionName.MUL;
				tempInstruction = new Instruction(name,
						RegisterName.valueOf(temp[1]),
						RegisterName.valueOf(temp[2]),
						RegisterName.valueOf(temp[3]));
						break;
			case "JMP":
				name = InstructionName.JMP;
				tempInstruction = new Instruction(name,
						RegisterName.valueOf(temp[1]),
						Integer.parseInt(temp[2]));
						break;
			case "JALR":
				name = InstructionName.JALR;
				tempInstruction = new Instruction(name,
						RegisterName.valueOf(temp[1]),
						RegisterName.valueOf(temp[2]));
						break;
			case "RET":
				name = InstructionName.RET;
				tempInstruction = new Instruction(name,
						RegisterName.valueOf(temp[1]));
						break;

			}

			out.add(tempInstruction);
		}
		return out;

	}

	public ArrayList<String> getProgram(String filePath) throws IOException {
		ArrayList<String> out = new ArrayList<String>();
		if (filePath.length() < 2) {
			System.out.println("Please type the program to simulate followed by an empty line to indicate program termination: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String line = br.readLine();
			String lineWithoutCommas;
			while (line != null) {
				lineWithoutCommas = line.replaceAll("[,;]", "");
				out.add(lineWithoutCommas);
				line = br.readLine();
			}
		} else {
			FileReader fileReader = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fileReader);
			String line, lineWithoutCommas;
			while ((line = br.readLine()) != null) {
				lineWithoutCommas = line.replaceAll("[,;]", "");
				out.add(lineWithoutCommas);
			}
		}
		return out;
	}

	/* public static void main(String[] args) {
		String s = "ADD regA, regB, regC";
		String lineWithoutCommas = s.replaceAll("[,;]", "");
		String[] temp = lineWithoutCommas.split(" ");
		System.out.println(lineWithoutCommas);
		
		for (int i = 0; i < temp.length; i++) {
			System.out.println(temp[i]);
		}
	}*/

}
