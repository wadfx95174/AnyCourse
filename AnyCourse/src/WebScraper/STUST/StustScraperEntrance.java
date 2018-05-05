package WebScraper.STUST;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import WebScraper.Output.OutputFormat;

public class StustScraperEntrance {
	public static void main(String[] args) throws Exception{
		StustScraper scraper = new StustScraper();
		ArrayList<OutputFormat> outputs = scraper.getCourseList();
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
		System.out.println(gson.toJson(outputs));
		
	}
}
