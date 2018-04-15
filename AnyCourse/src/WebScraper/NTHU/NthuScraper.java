package WebScraper.NTHU;

import WebScraper.Output.OutputFormat;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NthuScraper {
	private ArrayList<OutputFormat> outputs = new ArrayList<OutputFormat>();
	//課程後面的URL
	private ArrayList<String> courseURLList;
	
	//清華大學開放式課程官網的全部課程的頁面
	private static final String TARGET_URL = "http://ocw.nthu.edu.tw/ocw/index.php?page=courseList&classid=0&order=time&p=";
	//清華大學開放式課程影片的前半段網址
	private static final String COURSE_URL = "http://ocw.nthu.edu.tw/ocw/";
	
	
	public NthuScraper() throws IOException{
		courseURLList = new ArrayList<String>();
		Connection.Response res;
		Document document = null;
		Elements courseURL;//課程網址後面片段Element
		Elements courseElement;//課程名稱Tag
		Elements list;//影片URL跟影片名稱的上層Tag
		
		String courseURLToken;//課程網址後面片段
		String videoURLToken;//影片URL字串後半段
		String unit;//影片名稱字串
		String courseName = null;//課程名稱字串
		
		int x;
		//總共有14個頁面
		for(int i = 1;i <= 1 ; ++i) {
			//連官網的14個頁面
			res = Jsoup.connect(TARGET_URL+i)
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
					.method(Method.GET).execute();
			document = res.parse();
			courseURL = document.select("#content > div > div > div > div.courseListBlock > div");
			x = 0;
			for(Element URL : courseURL) {//因為不知道有幾個Tag，每一頁不一樣
				courseURLToken = courseURL.get(0).attr("onclick");
				System.out.println(courseURLToken);
				
				courseURLList.add(courseURLToken);
				x++;
			}
		}
		for(int i = 1;i <= 14 ; ++i) {
			OutputFormat output = new OutputFormat();
			videoURLToken = null;
			unit = null;
			
			
			
			
			
			
//			output.setUniversity("國立清華大學");
//			output.setCourseName(courseName);
//			for(Element test : list) {//因為不知道有幾個Tag，每個課程不一樣
//				unit = list.get(x).text();
//				videoURLToken = list.get(x).attr("video_name");
//				output.setUnitName(unit);
//				output.setUnitURL(VIDEO_URL + videoURLToken);
//				x++;
//			}
//			output.setLecture("null");
//			outputs.add(output);
		}
	}
	public ArrayList<OutputFormat> getItems() {
		return outputs;
	}
}


