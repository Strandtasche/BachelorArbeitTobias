import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

//zuerst manuell umcodieren: 1=0,2=1,3=2,4=3,5=4
//Dieses Programm führt die Rekodierung der verneinten Fragen durch im Bereich Extraversion
public class Auswertung {

	public static void main(String[] args) {
		
		ArrayList<String> paths = new ArrayList<String>();
		
		try {
			Files.walk(Paths.get("/Users/saibot1207/Documents/BA/Auswertung/")).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) {
			    	paths.add((filePath.toString()));
			    }
			});

//			File folder = new File("/Users/saibot1207/Documents/BA/Auswertung/");
//			File[] listOfFiles = folder.listFiles();
//
//			for (File file : listOfFiles) {
//				if (file.isFile()) {
//						(file.getName())
//				}
//			}

		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		String[] array = null;

		System.out.println("Files: " + paths.size());

		for (String p : paths) {
			String s = readStringFromFile(p);

			//System.out.println(s);

			array = s.split("(\\r?\\n)|,");

			//System.out.println(array.length);

//			String[] userids = new String[array.length];
//
//			for (int i = 0; i < array.length; i++) {
//				userids[i] = array[i][0];
//				array
//			}

//			String[] lines = array.split(',');
//
//			for (String s1: lines) {
//				System.out.println(s1);
//			}

			//System.out.println(array[0]);

			for (int i = 0; i < array.length; i++) {
				array[i] = Integer.toString((Integer.parseInt(array[i]) - 1));
			}

			ArrayList<String[]> lines = new ArrayList<String[]>();

			for (int i = 0; i < (array.length / 241); i++) {
				lines.add(Arrays.copyOfRange(array, i * 241, (i+1) * 241));
				//System.out.println(lines.get(i)[0]);
			}

			//int[] numbers = {60,0,120,180,35,95,155,10,70,46,105,165
				,20,80,230,140,55,115,175,205,235,31,91,6,
				66,126,186,41,101,161,221,16,76,136,51,111,
				26,86,146,206,32,92,152,182,212,7,67,127,
				42,102,162,17,77,137,197,227,52,112,172,27,
				87,147,207,237,3,63,123,38,98,158,188,218,
				13,73,133,48,108,168,198,228,23,83,143,233,
				58,118,34,94,154,9,69,129,189,219,44,104,
				19,79,139,54,114,174,204,29,89,149};

			//Arrays.sort(numbers);
			for (int s1 : numbers){
				System.out.println(s1);
			}

			int dist = 0;
			for (String[] str : lines) {

				String[] returning = recode(str);

				String saveToFile = "";
				for (int i = 0; i < returning.length; i++) {
					if (i != 0){
						saveToFile += "\n" + returning[i];
					} else {
						saveToFile += returning[i];
					}

				}

				try {
					PrintWriter out = new PrintWriter("/Users/saibot1207/Documents/BA/AusgabeAuswertung/Line" + dist + ".txt" );
					out.println(saveToFile);
					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				dist++;
			}
			//System.out.println(saveToFile);

			array = null;
		}
		
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
	
	
	private static String[] recode(String[] array) {
		int[] numbers = {60,0,120,180,35,95,155,10,70,46,105,165
						,20,80,230,140,55,115,175,205,235,31,91,6,
						66,126,186,41,101,161,221,16,76,136,51,111,
						26,86,146,206,32,92,152,182,212,7,67,127,
						42,102,162,17,77,137,197,227,52,112,172,27,
						87,147,207,237,3,63,123,38,98,158,188,218,
						13,73,133,48,108,168,198,228,23,83,143,233,
						58,118,34,94,154,9,69,129,189,219,44,104,
						19,79,139,54,114,174,204,29,89,149};
		
		for (int i = 0; i <array.length; i++) {
			for (int j = 1; j < numbers.length; j++) {
				if (numbers[j] == i) {
			        switch (Integer.parseInt(array[i])) {
			            case 4:  array[i] = "0";
			                     break;
			            case 3:  array[i] = "1";
			                     break;
			            case 1:  array[i] = "3";
			                     break;
			            case 0:  array[i] = "4";
			                     break;
			            case 2:  break;
			            default: System.out.println("ERROR");
			                     break;
			        }
				}
			}
		}
		return array;
	}

	private static String[] fixNumbers() {
		return null;
	}
}
