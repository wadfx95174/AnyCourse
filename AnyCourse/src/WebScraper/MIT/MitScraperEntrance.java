package WebScraper.MIT;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import WebScraper.Output.OutputFormat;

public class MitScraperEntrance 
{
	public static void main(String[] args) 
	{
		MitScraper scraper = new MitScraper();
		ArrayList<OutputFormat> outputs = scraper.getCourseList();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
		System.out.println(gson.toJson(outputs));
	}
}
