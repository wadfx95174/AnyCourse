package WebScraper.MIT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MitScraperEntrance {
	public static void main(String[] args) {
		Map<String, List<String>> columns = new HashMap<String, List<String>>();
		List<String> rows = new ArrayList<String>();
		 
		// 第一筆資料
		rows.add("a");
		rows.add("b");
		columns.put("1", rows);
		
		rows=new ArrayList<String>();
		rows.add("x");
		rows.add("y");
		columns.put("2", rows);
		
		
		for(String row : columns.keySet())
		{
//			for(String s:columns.get(row)) {
//				System.out.println(s);
//			}
			System.out.println(columns.get(row).get(0));
		}
	}
}
