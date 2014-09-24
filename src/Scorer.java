import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class Scorer {
	
	static double[][] matrix = new double[3][3];
	
	static Map<String, String> resultMap = new HashMap<String, String>();
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(args[1]));
		for (String key : properties.stringPropertyNames()) {
			   resultMap.put(key, properties.get(key).toString());
		}
		File file = new File(args[0]);
		Scanner scanner = new Scanner(file);
		int skipcount = 0;
		int scorecount = 0;
		while (scanner.hasNextLine()) {
			String[] line = scanner.nextLine().split("\t");
			if (line.length == 4){
				if (resultMap.containsKey(line[1])){
					matrix[getSentiValue(line[2])][getSentiValue(resultMap.get(line[1]))]++;
//					if(getSentiValue(resultMap.get(line[1])) == 0 && getSentiValue(line[2]) == 1){
//						System.out.println(line[2]);
//						System.out.println(resultMap.get(line[1]));
//					}
					scorecount++;
				}
				else{
					skipcount++;
				}
			}
		}
		score();
		System.out.println();
		System.out.println("Skips: " + skipcount);
		System.out.println("Scores: " + scorecount);
		System.out.println();
		System.out.println(matrix[0][0] +  " | " + matrix[0][1] + " | " + matrix[0][2]);
		System.out.println(matrix[1][0] +  " | " + matrix[1][1] + " | " + matrix[1][2]);
		System.out.println(matrix[2][0] +  " | " + matrix[2][1] + " | " + matrix[2][2]);
		System.out.println();
		System.out.println("TestNeg: " + (int) matrix[0][0] + " / " +  (int) (matrix[0][0] +  matrix[0][1] +  matrix[0][2]));
		System.out.println("TestNeu: " + (int) matrix[1][1] + " / " +  (int) (matrix[1][0] +  matrix[1][1] +  matrix[1][2]));
		System.out.println("TestPos: " + (int) matrix[2][2] + " / " +  (int) (matrix[2][0] +  matrix[2][1] +  matrix[2][2]));
	}
	
	private static int getSentiValue(String value) {
		if (value.equals("positive")) {
			return 2;
		}
		else{
			if (value.equals("negative")) {
				return 0;
			}
			else {
				return 1;
			}
		}
	}
	
	private static void score(){
		double precisionA = (matrix[0][0] == 0.0) ? 0.0 : matrix[0][0] / (matrix[0][0] + matrix[1][0] + matrix[2][0]);
		double precisionB = (matrix[1][1] == 0.0) ? 0.0 : matrix[1][1] / (matrix[1][1] + matrix[2][1] + matrix[0][1]);
		double precisionC = (matrix[2][2] == 0.0) ? 0.0 : matrix[2][2] / (matrix[2][2] + matrix[0][2] + matrix[1][2]);
//		int classes = bi ? 2 : 3;
		int classes = false ? 2 : 3;
		double precision = (precisionA + precisionB + precisionC) / classes;
		
		double recallA = (matrix[0][0] == 0.0) ? 0.0 : matrix[0][0] / (matrix[0][0] + matrix[0][1] + matrix[0][2]);
		double recallB = (matrix[1][1] == 0.0) ? 0.0 : matrix[1][1] / (matrix[1][1] + matrix[1][2] + matrix[1][0]);
		double recallC = (matrix[2][2] == 0.0) ? 0.0 : matrix[2][2] / (matrix[2][2] + matrix[2][0] + matrix[2][1]);
		double recall = (recallA + recallB + recallC) / classes;
		
		double f1 = 2 * ((precision * recall) / (precision + recall));
		
		System.out.println("precision: " + precision);
		System.out.println("recall: " + recall);
		System.out.println("f1: " + f1);
	}
}
