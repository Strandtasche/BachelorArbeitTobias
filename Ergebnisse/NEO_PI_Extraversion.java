import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

//import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;


//Dieses Programm nimmt umkodierten Ergebnisse im entsprechenden Ordner entgegen und berechnet die Extraversion
//Zahlwert fr Frage 1 in Zeile 1 usw...
public class NEO_PI_Extraversion {

	
	static double avgE1 = 0;
	static double avgE2 = 0;
	static double avgE3 = 0;
	static double avgE4 = 0;
	static double avgE5 = 0;
	static double avgE6 = 0;
	static double avgNEOE = 0;

	public static void main(String[] args) {
		
		int[] numbersE1 = {1,31,61,91,121,151,181,211};
		int[] numbersE2 = {6,36,66,96,156,126,216,186};
		int[] numbersE3 = {11,41,101,71,161,131,191,221};
		int[] numbersE4 = {46,16,106,76,166,196,226,136};
		int[] numbersE5 = {21,81,141,171,51,201,111,231};
		int[] numbersE6 = {26,56,86,116,146,176,206,236};
		int[] numbersNEOE = {1,31,61,91,121,151,181,21,
							6,36,66,96,156,126,216,186,
							11,41,101,71,161,131,191,221,
							46,16,106,76,166,196,226,136,
							21,81,141,171,51,201,111,231,
							26,56,86,116,146,176,206,236};
		
		
		ArrayList<String> paths = new ArrayList<String>();
		
		try {
			Files.walk(Paths.get("/Users/saibot1207/Documents/BA/AusgabeAuswertung")).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) {
			    	paths.add((filePath.toString()));
			    }
			});
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		String[] array = null;
		
		for (String p : paths) {
			String s = readStringFromFile(p);
			array = s.split("\\r?\\n");
			
			double[] doubleArray = new double[array.length];
			for (int i = 0; i < array.length; i++) {
				doubleArray[i] = Double.parseDouble(array[i]);
			}
			
			double meanE1 = calculateMean(numbersE1, doubleArray);
			double meanE2 = calculateMean(numbersE2, doubleArray);
			double meanE3 = calculateMean(numbersE3, doubleArray);
			double meanE4 = calculateMean(numbersE4, doubleArray);
			double meanE5 = calculateMean(numbersE5, doubleArray);
			double meanE6 = calculateMean(numbersE6, doubleArray);
			double meanNEOE = calculateMean(numbersNEOE, doubleArray);
			
			avgE1 += meanE1;
			avgE2 += meanE2;
			avgE3 += meanE3;
			avgE4 += meanE4;
			avgE5 += meanE5;
			avgE6 += meanE6;
			avgNEOE += meanNEOE;
			
			System.out.println("Statistik fr " + p);
			System.out.println("Mean Herzlichkeit: " + meanE1);
			System.out.println("Mean Geselligkeit: " + meanE2);
			System.out.println("Mean Durchsetzungsvermgen: " + meanE3);
			System.out.println("Mean Aktivitt: " + meanE4);
			System.out.println("Mean Exaltiertheit/Erlebnishunger: " + meanE5);
			System.out.println("Mean positive Gefhle: " + meanE6);
			System.out.println("Mean gesamt: " + meanNEOE);
			
			array = null;
		}
		
		avgE1 /= paths.size();
		avgE2 /= paths.size();
		avgE3 /= paths.size();
		avgE4 /= paths.size();
		avgE5 /= paths.size();
		avgE6 /= paths.size();
		avgNEOE /= paths.size();
		
		System.out.println("\n" + "Gesamt - Durschnitt:");
		System.out.println("avgE1 - Herzlichkeit: " + avgE1);
		System.out.println("avgE2 - Geselligkeit: " + avgE2);
		System.out.println("avgE3 - Durchsetzungsvermgen: " + avgE3);
		System.out.println("avgE4 - Aktivitt: " + avgE4);
		System.out.println("avgE5 - Exaltiertheit/Erlebnishunger suchend: " + avgE5);
		System.out.println("avgE6 - positive Gefhle: " + avgE6);
		System.out.println("avgNEOE - gesamt: " + avgNEOE);
		
	}

	//Calculate mean with DescriptiveStatistics - apache commons
	private static double calculateMean(int[] numbers, double[] values) {	
		double sum = 0;
		for (int i = 0; i < numbers.length; i++){
			sum += values[numbers[i]];
		}

		return sum/(numbers.length);
	}

	public static String readStringFromFile(String path) {
		  byte[] encoded = null;
			try {
				encoded = Files.readAllBytes(Paths.get(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		  return new String(encoded);
	}
}
