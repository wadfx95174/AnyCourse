package WebScraper.Output;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import WebScraper.MIT.MitScraper;
import WebScraper.NCCU.NccuScraper;
import WebScraper.NCKU.NckuScraper;
import WebScraper.NCTU.NctuScraper;
import WebScraper.NTHU.NthuScraper;
import WebScraper.NTU.NtuScraper;
import WebScraper.NTUST.NtustScraper;
import WebScraper.STUST.StustScraper;

public class WebScraperEntrance {

	public static void main(String[] args) throws IOException {
		MitScraper mitScraper = new MitScraper();
		NccuScraper nccuScraper = new NccuScraper();
		NckuScraper nckuScraper = new NckuScraper();
		NctuScraper nctuScraper = new NctuScraper();
		NthuScraper nthuScraper = new NthuScraper();
		NtuScraper ntuScraper = new NtuScraper();
		NtustScraper ntustScraper = new NtustScraper();
		StustScraper stustScraper = new StustScraper();
		
		int choice = 0;
		System.out.println("選擇學校:");
		System.out.println("1:台灣大學");
		System.out.println("2:清華大學");
		System.out.println("3:交通大學");
		System.out.println("4:成功大學");
		System.out.println("5:政治大學");
		System.out.println("6:台灣科技大學");
		System.out.println("7:麻省理工學院");
		System.out.println("8:南台科技大學");
		System.out.println("9:全部");
		System.out.println("0:結束");
		Scanner in = new Scanner(System.in);
		while(choice != 0)
		choice = in.nextInt();
		switch(choice) {
		case 1:
			output(ntuScraper);
			break;
		case 2:
			output(nthuScraper);
			break;
		case 3:
			output(nctuScraper);
			break;
		case 4:
			output(nckuScraper);
			break;
		case 5:
			output(nccuScraper);
			break;
		case 6:
			output(ntustScraper);
			break;
		case 7:
			output(mitScraper);
			break;
		case 8:
			output(stustScraper);
			break;
		case 0:
			outputAll();
			break;
		}
	}
	public static void output(CourseList courseList) {
		ArrayList<OutputFormat> outputs = courseList.getCourseList();
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
		System.out.println(gson.toJson(outputs));
	}
	public static void outputAll() throws IOException {
		
		CourseList courseList1 = new MitScraper();
		CourseList courseList2 = new NccuScraper();
		CourseList courseList3 = new NckuScraper();
		CourseList courseList4 = new NctuScraper();
		CourseList courseList5 = new NthuScraper();
		CourseList courseList6 = new NtuScraper();
		CourseList courseList7 = new NtustScraper();
		CourseList courseList8 = new StustScraper();
		
		Map <Integer,ArrayList<OutputFormat>> outputAll = new HashMap<Integer,ArrayList<OutputFormat>>();
		outputAll.put(1, courseList1.getCourseList());
		outputAll.put(2, courseList2.getCourseList());
		outputAll.put(3, courseList3.getCourseList());
		outputAll.put(4, courseList4.getCourseList());
		outputAll.put(5, courseList5.getCourseList());
		outputAll.put(6, courseList6.getCourseList());
		outputAll.put(7, courseList7.getCourseList());
		outputAll.put(8, courseList8.getCourseList());
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
		System.out.println(gson.toJson(outputAll));
	}
}
