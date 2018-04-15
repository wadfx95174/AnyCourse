package WebScraper.NCTU;

import java.io.PrintWriter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import WebScraper.Output.OutputFormat;

public class NctuScraperEntrance {
	public static void main(String[] args) throws Exception{
		NctuScraper scraper = new NctuScraper();
		ArrayList<OutputFormat> outputs = scraper.getItems();
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
		System.out.println(gson.toJson(outputs));
		
	}
}


