package WebScraper.NTHU;

import WebScraper.Output.CourseList;
import WebScraper.Output.OutputFormat;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NthuScraper implements CourseList{
	private ArrayList<OutputFormat> outputs = new ArrayList<OutputFormat>();
	// 隤脩����RL
	private ArrayList<String> courseURLLists;
	// ���蔣���迂嚗����敺����瘥�2R嚗�隞亙��絲靘�
	private ArrayList<String> unitNameLists;
	// 皜憭批飛��撘玨蝔�雯���隤脩���
	private static final String TARGET_URL = "http://ocw.nthu.edu.tw/ocw/index.php?page=courseList&classid=0&order=time&p=";
	// 皜憭批飛��撘玨蝔蔣�����挾蝬脣�
	private static final String COURSE_URL = "http://ocw.nthu.edu.tw/ocw/";
	//皜憭批飛���蔣�����
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
		
		Elements courseURLElement = null;// 隤脩�雯��敺��挾Element
		Elements unitList = null;// ���蔣���able
		Elements tableListTr = null;//瘥���蔣��������迫銝��蔣�����停�����蔣���able��r
		Elements tableListTd = null;//銝���撅川d

		String courseURLToken;// 隤脩�雯��敺��挾
		String videoURLToken;// 敶梁�RL摮葡敺�挾
		String unitURLToken;// 敶梁��迂摮葡
		String courseName = null;// 隤脩��迂摮葡
		String unitNameFirstHalf = null;//unitName���挾
		String unitNameSecondHalf = null;//unitName敺�挾
//		String videoImgSrc;//���葦����靘雿蔣����汗����
		String videoIframe = null;//���frame��rc嚗���雯����甇�蝣箇�蔣��雯��嚗�隞js撖怎��
		String videoURL;
		
		int x;

		// 蝮賢���14��嚗蒂����玨蝔��挾URL�摮�淆ourseURLLists嚗誑靘踹���
		for (int i = 0; i <= 14; ++i) {
			// ��摰雯���14��


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
			for (Element URL : courseURLElement) {// ��銝���嗾�ag嚗������璅�
				courseURLToken = courseURLElement.get(x).attr("onclick");
//				System.out.println(courseURLToken.split("\\'")[1]);
				courseURLLists.add(courseURLToken.split("\\'")[1]);
				x++;
			}
		}
//		for(String s:courseURLLists) {
//			System.out.println(s);
//		}
		
		/*瘥�玨蝔��惜�銝���<div class="entry">�嚗��誑銝嗾蝔�
		 * 1.div嚗5,11,47,50,58,]
		 * 2.蝚砌��嚗26,]
		 * 3.p > span > span > span嚗3,] 
		 * 4.div > span嚗0,2,45,49,59,84]
		 * 5.p > span > span嚗6,9,80,81,]
		 * 6.p嚗1,4,7,12,13,14,15,16,17,18,19,20,21,22,23,24,25,27,28,30,31,32,33,34,35,36,38,39,40,41
		 * ,42,43,44,46,53,55,61,62,63,64,65,66,]
		 * 7.div > div > span > span嚗8,]
		 * 8.div > span > span嚗10,86,]
		 * 9.瘝�29,]
		 * 10.���憒�(隤脩�陛隞������惜)[37,56,67,68,70,72,74,75,76,79,82,83,84,85]
		 * 11.p > span嚗48,51,52,54,57,69,77,]
		 * 12.div > div > span[60,71,73]
		 * 13.蝚砌��iv > span[78]
		 * */
		//��瘥�玨蝔�
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
			
			//隤脩��迂
			
				courseName = document.select("#content > div > h2").get(0).text();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			//�葦����(�靘雿蔣������)
//			videoImgSrc = document.select("#content > div.content-left > div:nth-child(1) > div > div > div:nth-child(7) > img").get(0).attr("src");
			output.setUniversity("����憭批飛");
			output.setCourseName(courseName);
//			output.setUnitImgSrc(videoImgSrc);
			//蝬脤����蔣��i
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
				System.out.println(videoURLToken);
				
				System.out.println(unitNameFirstHalf);
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
				//�����蔣���銝��r
				for(Element tr : tableListTr) {
					try {
						tableListTd = tableListTr.select("td");
					}
					catch(Exception e) {
						e.printStackTrace();
					}
//					System.out.println(tableListTd);
					//銝銝��d
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
							System.out.println(VIDEO_IMG_URL+temp1+"/"+temp2);
							System.out.println(VIDEO_IMG_URL+temp1+"/cover.png");
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
	// for(Element test : list) {//��銝���嗾�ag嚗��玨蝔��璅�
				// unit = list.get(x).text();
				// videoURLToken = list.get(x).attr("video_name");
				// output.setUnitName(unit);
				// output.setUnitURL(VIDEO_URL + videoURLToken);
				// x++;
				// }
				// output.setLecture("null");
				// outputs.add(output);
	@Override
	public ArrayList<OutputFormat> getCourseList() {
		return outputs;
	}
}
