import java.lang.Math;

public class GoldMine{

	public int maxGoldDp(int[] caps) {
		int[] maxSoFar = new int[caps.length];
		int[] source = new int[caps.length];
		int max = 0;
		boolean first = false;
		if (caps.length == 1)
			return caps[0];
		if (caps.length == 2)
			return Math.max(caps[0], caps[1]);
		if (caps.length == 3)
			return Math.max(caps[2], maxSoFar[1]);

		source[0] = -1;
		source[1] = -1;
		if(caps[0]> caps[caps.length -1])
		{
		source[2] = 0;
		maxSoFar[2] = caps[2]+ caps[0];
		}
		else{
			source[2]=-1;
			maxSoFar[2] = caps[2];

		}
		maxSoFar[0] = caps[0];
		maxSoFar[1] = caps[1];

		for (int i = 3; i < caps.length; i++) {
			maxSoFar[i] = Math.max(maxSoFar[i - 2], maxSoFar[i - 3]) + caps[i];

			if (maxSoFar[i - 2] > maxSoFar[i - 3])
				source[i] = i - 2;
			else
				source[i] = i - 3;

			if (i == caps.length - 1) {
				int j = i;
				while(source[j]>-1){
						if(source[j] == 0){
						first = true;
						break;
				}
						System.out.print(caps[j]+",");
					j = source[j];
				}
				System.out.print(caps[j]+",");

			}
			if (maxSoFar[i] > max && !first)
				max = maxSoFar[i];
			first = false;
		}

		return max;
	}

	public static void main(String[] args) {
		int[] caps = { 1,2,3,4,5,1,2,3,4,5 };
		System.out.println(new GoldMine().maxGoldDp(caps));
	}
}
