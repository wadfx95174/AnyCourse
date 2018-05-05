package WebScraper.NTUST;
import WebScraper.Output.CourseList;
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

public class NtustScraper implements CourseList{
	private ArrayList<OutputFormat> outputs = new ArrayList<OutputFormat>();
	//台灣科技大學開放式課程官網
	private static final String TARGET_URL = "http://ocw.ntust.edu.tw/index.php/frontend/show/info_page1/";
	//台灣科技大學開放式課程影片網址
	private static final String VIDEO_URL = "http://ocw.ntust.edu.tw/public/";
	//URL後面的數字
	private String[] course_URL = {"194","193","191","192","189","185","171","164","117","115","114","102"
			,"109","110","111","112","113"};
	
	public NtustScraper() throws IOException{
		Connection.Response res;
		Document document = null;
		Elements courseElement;//課程名稱Tag
		Elements list;//影片URL跟影片名稱的上層Tag
		String videoURLToken;//影片URL字串後半段
		String unit;//影片名稱字串
		String courseName = null;//課程名稱字串
		
		
		int x;//跑迴圈時決定跑第幾個tag
		for(int i = 0;i <= 16 ; ++i) {//總共有17個課程
			OutputFormat output = new OutputFormat();
			videoURLToken = null;
			unit = null;
			//連官網
			res = Jsoup.connect(TARGET_URL+course_URL[i])
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
					.method(Method.GET).execute();
			document = res.parse();
			courseElement = document.select("body > div.container > div.main_cont > div.teacher_right > div.lessons_name > span");
			
			list = document.select("ul>li>a");
			courseName = courseElement.get(0).text();

			output.setUniversity("台灣科技大學");
			output.setCourseName(courseName);
			x = 0;
			for(Element test : list) {//因為不知道有幾個Tag，每個課程不一樣
				unit = list.get(x).text();
				videoURLToken = list.get(x).attr("video_name");
				output.setUnitName(unit);
				output.setUnitURL(VIDEO_URL + videoURLToken);
				x++;
			}
			output.setLecture("null");
			outputs.add(output);
		}
	}
	@Override
	public ArrayList<OutputFormat> getCourseList() {
		return outputs;
	}
}

