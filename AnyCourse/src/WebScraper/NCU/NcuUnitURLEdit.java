package WebScraper.NCU;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NcuUnitURLEdit {
	public static void main(String[] args) throws IOException {
		try {
			FileReader fr = new FileReader("C:/Users/Chen/Desktop/Java/AnyCourse/src/WebScraper/NCU/ncu.csv");
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			ArrayList<String> unitList = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
//				System.out.println(line);
				String tempArray[] = line.split(",");
				unitList.add("https://www.youtube.com/embed/" + tempArray[2].split("=")[1].split("&")[0]);
			}
			
			for (String s : unitList) {
				System.out.println(s);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}
