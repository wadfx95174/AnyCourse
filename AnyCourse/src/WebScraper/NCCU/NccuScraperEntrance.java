package WebScraper.NCCU;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import WebScraper.Output.OutputFormat;

public class NccuScraperEntrance {
	public static void main(String[] args) throws Exception{
		NccuScraper scraper = new NccuScraper();
		ArrayList<OutputFormat> outputs = scraper.getCourseList();
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
		System.out.println(gson.toJson(outputs));
		
	}
}
