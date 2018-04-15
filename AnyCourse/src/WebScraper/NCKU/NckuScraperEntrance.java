package WebScraper.NCKU;

import java.io.PrintWriter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import WebScraper.Output.OutputFormat;

public class NckuScraperEntrance {
	public static void main(String[] args) throws Exception{
		NckuScraper scraper = new NckuScraper();
		ArrayList<OutputFormat> outputs = scraper.getItems();
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
		System.out.println(gson.toJson(outputs));

		
	}
}

