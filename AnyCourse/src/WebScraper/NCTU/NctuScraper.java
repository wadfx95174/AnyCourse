package WebScraper.NCTU;

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

public class NctuScraper implements CourseList{
	private ArrayList<OutputFormat> outputs = new ArrayList<OutputFormat>();
	//交通大學開放式課程官網
	private static final String COURSE_URL = "http://ocw.nctu.edu.tw/";
	//交通大學開放式課程影片網址前半段
	private static final String VIDEO_URL = "http://ocw.nctu.edu.tw/course_detail-v.php";
	//交通大學開放式課程講義網址前半段
	private static final String LECTURE_URL = "http://ocw.nctu.edu.tw/course_detail-s.php";
	//URL中間的一段固定字串
	private static final String MIDSTRING = "?bgid=";
	//URL後面的數字
	private String[] video_URL = {"1&gid=1&nid=563","1&gid=1&nid=561","1&gid=1&nid=503","1&gid=1&nid=500"
			,"1&gid=1&nid=490","1&gid=1&nid=501","1&gid=1&nid=9","1&gid=1&nid=16","1&gid=1&nid=449"
			,"1&gid=1&nid=270","1&gid=1&nid=362","1&gid=1&nid=14","1&gid=1&nid=13","1&gid=1&nid=1"
			,"1&gid=1&nid=12","1&gid=1&nid=171","1&gid=1&nid=263","1&gid=1&nid=188","1&gid=1&nid=233"
			,"1&gid=1&nid=15","1&gid=1&nid=187","1&gid=1&nid=234","1&gid=1&nid=271","1&gid=1&nid=361"
			,"1&gid=1&nid=17","1&gid=1&nid=18","1&gid=1&nid=239","1&gid=1&nid=240","1&gid=1&nid=408"
			,"1&gid=1&nid=175","1&gid=1&nid=205","1&gid=1&nid=259","1&gid=1&nid=45","1&gid=1&nid=46"
			,"1&gid=2&nid=556","1&gid=2&nid=545","1&gid=2&nid=538","1&gid=2&nid=529","1&gid=2&nid=499"
			,"1&gid=2&nid=510","1&gid=2&nid=435","1&gid=2&nid=411","1&gid=2&nid=409","1&gid=2&nid=371"
			,"1&gid=2&nid=372","1&gid=2&nid=373","1&gid=2&nid=374","1&gid=2&nid=375","1&gid=2&nid=530"
			,"1&gid=2&nid=381","1&gid=3&nid=533","1&gid=3&nid=492","1&gid=3&nid=531","1&gid=3&nid=440"
			,"1&gid=3&nid=382","1&gid=3&nid=383","1&gid=3&nid=384","1&gid=3&nid=385","1&gid=3&nid=386"
			,"1&gid=3&nid=387","1&gid=3&nid=452","1&gid=4&nid=543","1&gid=4&nid=528","1&gid=4&nid=537"
			,"1&gid=4&nid=536","2&gid=0&nid=571","2&gid=0&nid=560","2&gid=0&nid=551","2&gid=0&nid=516"
			,"2&gid=0&nid=479","2&gid=0&nid=474","2&gid=0&nid=475","2&gid=0&nid=460","2&gid=0&nid=477"
			,"2&gid=0&nid=405","2&gid=0&nid=439","2&gid=0&nid=410","2&gid=0&nid=52","2&gid=0&nid=53"
			,"2&gid=0&nid=54","2&gid=0&nid=55","2&gid=0&nid=191","2&gid=0&nid=190","2&gid=0&nid=189"
			,"2&gid=0&nid=351","8&gid=0&nid=566","8&gid=0&nid=564","8&gid=0&nid=558","8&gid=0&nid=557"
			,"8&gid=0&nid=554","8&gid=0&nid=550","8&gid=0&nid=517","8&gid=0&nid=514","8&gid=0&nid=491"
			,"8&gid=0&nid=512","8&gid=0&nid=509","8&gid=0&nid=498","8&gid=0&nid=493","8&gid=0&nid=453"
			,"8&gid=0&nid=416","8&gid=0&nid=343","8&gid=0&nid=248","8&gid=0&nid=269","8&gid=0&nid=192"
			,"8&gid=0&nid=106","8&gid=0&nid=236","8&gid=0&nid=174","8&gid=0&nid=346","8&gid=0&nid=181"
			,"3&gid=0&nid=521","3&gid=0&nid=513","3&gid=0&nid=511","3&gid=0&nid=454","3&gid=0&nid=502"
			,"3&gid=0&nid=496","3&gid=0&nid=487","3&gid=0&nid=455","3&gid=0&nid=426","3&gid=0&nid=505"
			,"3&gid=0&nid=469","3&gid=0&nid=403","3&gid=0&nid=402","3&gid=0&nid=368","3&gid=0&nid=367"
			,"3&gid=0&nid=48","3&gid=0&nid=49","3&gid=0&nid=51","3&gid=0&nid=179","3&gid=0&nid=107"
			,"3&gid=0&nid=277","3&gid=0&nid=50","3&gid=0&nid=245","3&gid=0&nid=358","9&gid=0&nid=565"
			,"9&gid=0&nid=546","9&gid=0&nid=542","9&gid=0&nid=441","9&gid=0&nid=419","9&gid=0&nid=413"
			,"9&gid=0&nid=412","9&gid=0&nid=170","9&gid=0&nid=238","9&gid=0&nid=345","9&gid=0&nid=275"
			,"9&gid=0&nid=274","9&gid=0&nid=246","9&gid=0&nid=235","9&gid=0&nid=250","9&gid=0&nid=252"
			,"9&gid=0&nid=364","4&gid=0&nid=552","4&gid=0&nid=544","4&gid=0&nid=539","4&gid=0&nid=523"
			,"4&gid=0&nid=540","4&gid=0&nid=56","4&gid=0&nid=312","5&gid=0&nid=555","5&gid=0&nid=553"
			,"5&gid=0&nid=527","5&gid=0&nid=522","5&gid=0&nid=506","5&gid=0&nid=504","5&gid=0&nid=494"
			,"5&gid=0&nid=470","5&gid=0&nid=461","5&gid=0&nid=451","5&gid=0&nid=438","5&gid=0&nid=437"
			,"5&gid=0&nid=417","5&gid=0&nid=415","5&gid=0&nid=104","5&gid=0&nid=177","5&gid=0&nid=276"
			,"5&gid=0&nid=342","5&gid=0&nid=242","5&gid=0&nid=58","5&gid=0&nid=59","5&gid=0&nid=57"
			,"5&gid=0&nid=193","5&gid=0&nid=135","5&gid=0&nid=61","10&gid=0&nid=508","10&gid=0&nid=473"
			,"10&gid=0&nid=418","10&gid=0&nid=186","10&gid=0&nid=105","6&gid=0&nid=570","6&gid=0&nid=559"
			,"6&gid=0&nid=549","6&gid=0&nid=548","6&gid=0&nid=541","6&gid=0&nid=532","6&gid=0&nid=524"
			,"6&gid=0&nid=526","6&gid=0&nid=525","6&gid=0&nid=519","6&gid=0&nid=518","6&gid=0&nid=495"
			,"6&gid=0&nid=507","6&gid=0&nid=488","6&gid=0&nid=489","6&gid=0&nid=468","6&gid=0&nid=467"
			,"6&gid=0&nid=433","6&gid=0&nid=434","6&gid=0&nid=427","6&gid=0&nid=370","6&gid=0&nid=182"
			,"6&gid=0&nid=344","6&gid=0&nid=194","7&gid=0&nid=102","7&gid=0&nid=244"};//總共213個(0~212)
	//該課程有無講義，1為有、0為無
	private String[] lectureCheck = {"0","1","0","0","1","1","1","1","1","1","1","1","1","0","0","0"
			,"1","1","1","1","1","1","1","1","0","0","1","1","1","0","0","1","1","1","1","0","1","1"
			,"1","1","1","1","1","1","1","1","1","1","0","0","1","0","1","1","1","1","0","1","0","1"
			,"0","1","1","1","1","1","0","1","1","0","1","1","1","1","0","0","1","1","1","1","1","1"
			,"1","1","0","0","1","1","1","1","0","0","1","1","1","1","0","1","1","0","1","1","0","1"
			,"1","1","1","0","1","0","1","1","1","1","1","1","1","1","0","0","1","1","1","0","1","1"
			,"1","1","0","1","0","1","1","1","1","1","1","1","0","1","0","0","0","0","0","1","1","1"
			,"1","1","1","1","1","1","1","1","1","1","1","1","0","1","0","1","1","1","0","1","0","0"
			,"1","1","1","0","1","1","1","0","0","0","0","0","1","1","0","1","1","1","0","1","1","1"
			,"1","1","1","0","0","1","0","1","1","1","1","1","1","1","0","1","0","1","0","1","0"};//213
	
	public NctuScraper() throws IOException{
		Connection.Response res;
		Connection.Response res2;
		Document documentAll = null;
		Document documentVideo = null;
		Document documentCourse = null;
		Document documentLecture = null;
		
		Elements courseToken;//課程名稱的URL後段
		Elements courseElement;//課程名稱Tag
		Elements list;//影片名稱及影片URL的上層Tag
		Elements lecture;//講義URL
		
		String courseURL = null;
		String courseName = null;//課程名稱字串
		String unit = null;//影片名稱字串
		String videoToken = null;//影片URL字串
		String lectureURL;//講義URL字串
		String lectureName;//講義名稱
		
		Elements video;
		String videoURL = null;
		

		int x;//跑迴圈時決定跑第幾個tag
		for(int i = 0;i <= 50; ++i) {
			System.out.println(i);
			OutputFormat output = new OutputFormat();

			res = Jsoup.connect(VIDEO_URL + MIDSTRING + video_URL[i])
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
					.method(Method.GET).execute();
			
			documentAll = res.parse();
			
			courseToken = documentAll.select("#about-us > div > div.col-sm-9 > span > div > div.blog > div > div > div:nth-child(1) > a:nth-child(1)");
			list = documentAll.select("#about-us > div > div.col-sm-9 > span > div > div.blog > div > div > div.table-responsive.hidden-sm.hidden-xs > table > tbody > tr");
			courseURL = courseToken.get(0).attr("href");
			
			res = Jsoup.connect(COURSE_URL + courseURL)
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
					.method(Method.GET).execute();
			documentCourse = res.parse();
			
			courseElement = documentCourse.select("#about-us > div > div.col-sm-9 > h3 > div > span");
			courseName = courseElement.get(0).text();

			output.setUniversity("交通大學");
			output.setCourseName(courseName);
			
			res2 = Jsoup.connect(LECTURE_URL + MIDSTRING + video_URL[i])
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
					.method(Method.GET).execute();
			documentLecture = res2.parse();
			lecture = documentLecture.select("#about-us > div > div.col-sm-9 > span > div > div.blog > div > div > div.table-responsive.hidden-xs.hidden-sm > table > tbody > tr");
			
			x = -1;
			for(Element test : list) {
				x++;
				if(x == 0)continue;//因為第一個是周次/課程內容/課程影音等<th>
				
				unit = list.get(x).select("td").get(1).text();
				videoToken = list.get(x).select("td > a").get(0).attr("href");
				
				res = Jsoup.connect(VIDEO_URL + videoToken)
						.userAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
						.method(Method.GET).execute();
				documentVideo = res.parse();
				video = documentVideo.select("#about-us > div > div.col-sm-9 > span > div > div.blog > div > div > div.video-container > iframe");
				try {
					videoURL = video.get(0).attr("src");
				}catch(IndexOutOfBoundsException e) {
					e.printStackTrace();
				}
				
				
//				System.out.println(unit);
//				System.out.println(videoURL);
				output.setUnitName(unit);
				output.setUnitURL(videoURL);
				
			}
			
			if(lectureCheck[i] == "1") {
				x = -1;
				for(Element test : lecture) {
					x++;
					if(x == 0)continue;//因為第一個是課程內容/講義下載等<th>
					
					lectureURL = lecture.get(x).select("td > a").get(0).attr("href");
					lectureName = lecture.get(x).select("td").get(0).text();
//					System.out.println(lectureName);
//					System.out.println(lectureURL);
					output.setLectureName(lectureName);
					output.setLecture(lectureURL);
				}
			}
			else if(lectureCheck[i] == "0"){
				output.setLectureName(null);
				output.setLecture(null);
			}
			outputs.add(output);
		}
	}
	@Override
	public ArrayList<OutputFormat> getCourseList() {
		return outputs;
	}
}



