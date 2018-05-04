package WebScraper.NTHU;

import WebScraper.Output.OutputFormat;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NthuScraper {
	private ArrayList<OutputFormat> outputs = new ArrayList<OutputFormat>();
	// 課程後面的URL
	private ArrayList<String> courseURLLists;
	// 單元影片名稱，因為還要接後面的小東東比如L2R，所以先存起來
	private ArrayList<String> unitNameLists;
	// 清華大學開放式課程官網的全部課程的頁面
	private static final String TARGET_URL = "http://ocw.nthu.edu.tw/ocw/index.php?page=courseList&classid=0&order=time&p=";
	// 清華大學開放式課程影片的前半段網址
	private static final String COURSE_URL = "http://ocw.nthu.edu.tw/ocw/";
	//清華大學單元影片圖片
	private static final String VIDEO_IMG_URL = "http://ocw.nthu.edu.tw/videosite/assert/";

	public NthuScraper() throws IOException {
		outputs = new ArrayList<OutputFormat>();
		courseURLLists = new ArrayList<String>();
		unitNameLists = new ArrayList<String>();
		Connection.Response res;
		Connection.Response res2;
		Connection.Response res3;
		
		Document document = null;
		Document document2 = null;
		Document document3 = null;
		
		Elements courseURLElement = null;// 課程網址後面片段Element
		Elements unitList = null;// 單元影片的table
		Elements tableListTr = null;//每個單元影片裏面都還有不止一個影片，這個就是所有影片的table的tr
		Elements tableListTd = null;//上面那個的內層td

		String courseURLToken;// 課程網址後面片段
		String videoURLToken;// 影片URL字串後半段
		String unitURLToken;// 影片名稱字串
		String courseName = null;// 課程名稱字串
		String unitNameFirstHalf = null;//unitName前半段
		String unitNameSecondHalf = null;//unitName後半段
//		String videoImgSrc;//把老師的圖片拿來當作影片的預覽圖片
		String videoIframe = null;//先取iframe的src，再用這個網址拆出正確的影片網址，因為他是js寫的
		String videoURL;
		
		int x;
		// 總共有14個頁面，並把每個課程的後段URL都存進courseURLLists，以便後續利用
		for (int i = 1; i <= 14; ++i) {
			// 連官網的14個頁面
			res = Jsoup.connect(TARGET_URL + i)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
					.method(Method.GET).execute();
			document = res.parse();
			try {
				courseURLElement = document.select("#content > div > div > div > div.courseListBlock > div");
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			x = 0;
			for (Element URL : courseURLElement) {// 因為不知道有幾個Tag，每一頁不一樣
				courseURLToken = courseURLElement.get(x).attr("onclick");
//				System.out.println(courseURLToken.split("\\'")[1]);
				courseURLLists.add(courseURLToken.split("\\'")[1]);
				x++;
			}
		}
//		for(String s:courseURLLists) {
//			System.out.println(s);
//		}
		
		/*每個課程的標籤都不同，皆在<div class="entry">內，共有以下幾種
		 * 1.div，[5,11,47,50,58,]
		 * 2.第二個p，[26,]
		 * 3.p > span > span > span，[3,] 
		 * 4.div > span，[0,2,45,49,59,84]
		 * 5.p > span > span，[6,9,80,81,]
		 * 6.p，[1,4,7,12,13,14,15,16,17,18,19,20,21,22,23,24,25,27,28,30,31,32,33,34,35,36,38,39,40,41
		 * ,42,43,44,46,53,55,61,62,63,64,65,66,]
		 * 7.div > div > span > span，[8,]
		 * 8.div > span > span，[10,86,]
		 * 9.沒有[29,]
		 * 10.莫名其妙(課程簡介還分很多個標籤)[37,56,67,68,70,72,74,75,76,79,82,83,84,85]
		 * 11.p > span，[48,51,52,54,57,69,77,]
		 * 12.div > div > span[60,71,73]
		 * 13.第二個div > span[78]
		 * */
		//連每個課程
		for (int i = 0; i < courseURLLists.size(); ++i) {
			OutputFormat output = new OutputFormat();
			videoURLToken = null;
			unitURLToken = null;
			System.out.println(i);
			try {
				res = Jsoup.connect(COURSE_URL + courseURLLists.get(i))
						.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
						.method(Method.GET).execute();
				document = res.parse();
			
			//課程名稱
			
				courseName = document.select("#content > div > h2").get(0).text();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			//老師圖片(用來當作影片的圖片)
//			videoImgSrc = document.select("#content > div.content-left > div:nth-child(1) > div > div > div:nth-child(7) > img").get(0).attr("src");
			output.setUniversity("國立清華大學");
			output.setCourseName(courseName);
//			output.setUnitImgSrc(videoImgSrc);
			//網頁右邊的影片li
			try {
				unitList = document.select("#search2 > ul > li");
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			for(Element count : unitList) {
				try {
					videoURLToken = count.select("div > a").get(0).attr("href");
					unitNameFirstHalf = count.select("div > a").get(0).text();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
//				System.out.println(videoURLToken);
				
//				System.out.println(unitNameFirstHalf);
				unitNameLists.add(unitNameFirstHalf);
			
				try {
					res2 = Jsoup.connect(COURSE_URL + videoURLToken)
							.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
							.method(Method.GET).execute();
					document2 = res2.parse();
					tableListTr = document2.select("#page > div.content-left2 > div:nth-child(1) > div > div > div:nth-child(5) > table > tbody > tr");
				}
				catch(Exception e) {
					e.printStackTrace();
				}
//				System.out.println(tableListTr);
				//有的單元影片不只一個tr
				for(Element tr : tableListTr) {
					try {
						tableListTd = tableListTr.select("td");
					}
					catch(Exception e) {
						e.printStackTrace();
					}
//					System.out.println(tableListTd);
					//不只一個td
					for(Element td : tableListTd) {
						try {
							unitNameSecondHalf = td.select("label").get(0).text();
							output.setUnitName(unitNameLists.get(i)+"_"+unitNameSecondHalf);
							unitURLToken = td.select("a:nth-child(2)").get(0).attr("href");
							res3 = Jsoup.connect(COURSE_URL + unitURLToken)
									.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
									.method(Method.GET).execute();
							document3 = res3.parse();
							videoIframe = document3.select("#videoFrame").get(0).attr("src");
						}
						catch(Exception e) {
							e.printStackTrace();
						}
//						System.out.println(unitNameLists.get(i)+"_"+unitNameSecondHalf);
						
//						System.out.println(videoIframe);
						try {
							String temp1 = videoIframe.split("=")[2].split("&")[0];
							String temp2 = videoIframe.split("=")[3].split("&")[0];
//							System.out.println(VIDEO_IMG_URL+temp1+"/"+temp2);
//							System.out.println(VIDEO_IMG_URL+temp1+"/cover.png");
							output.setUnitURL(VIDEO_IMG_URL+temp1+"/"+temp2);
							output.setUnitImgSrc(VIDEO_IMG_URL+temp1+"/cover.png");
						}
						catch(Exception e) {
							output.setUnitURL(null);
							output.setUnitImgSrc(null);
						}
					}
				}
			}
			outputs.add(output);
		}
		
	}
	// for(Element test : list) {//因為不知道有幾個Tag，每個課程不一樣
				// unit = list.get(x).text();
				// videoURLToken = list.get(x).attr("video_name");
				// output.setUnitName(unit);
				// output.setUnitURL(VIDEO_URL + videoURLToken);
				// x++;
				// }
				// output.setLecture("null");
				// outputs.add(output);
	public ArrayList<OutputFormat> getItems() {
		return outputs;
	}
}
