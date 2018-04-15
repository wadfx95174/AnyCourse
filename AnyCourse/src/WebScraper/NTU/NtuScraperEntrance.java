package WebScraper.NTU;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import WebScraper.Output.OutputFormat;


public class NtuScraperEntrance {
	public static void main(String[] args) throws Exception{
		NtuScraper scraper = new NtuScraper();
		ArrayList<OutputFormat> outputs = scraper.getItems();
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
        System.out.println(gson.toJson(outputs));
        
//		for (OutputFormat output: outputs) {
//			System.out.println(output);
//		}
		
	}
}
