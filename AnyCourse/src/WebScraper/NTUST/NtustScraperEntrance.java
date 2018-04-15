package WebScraper.NTUST;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import WebScraper.Output.OutputFormat;

public class NtustScraperEntrance {
	public static void main(String[] args) throws Exception{
		NtustScraper scraper = new NtustScraper();
		ArrayList<OutputFormat> outputs = scraper.getItems();
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
        System.out.println(gson.toJson(outputs));
        
//		for (OutputFormat output: outputs) {
//			System.out.println(output);
//		}
		
	}
}
