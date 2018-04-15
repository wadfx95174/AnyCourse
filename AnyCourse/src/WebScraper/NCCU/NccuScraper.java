package WebScraper.NCCU;

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

public class NccuScraper {
	private ArrayList<OutputFormat> outputs = new ArrayList<OutputFormat>();
	//政治大學開放式課程官網
	private static final String TARGET_URL = "http://ctld.video.nccu.edu.tw";
	//MP4類型的前段固定網址
	private static final String FIXURLMP4 = "http://ctld.video.nccu.edu.tw/streaming/doc/";
	//MP4類型的後段固定網址第1種
	private static final String FIXURLMP4LAST1 = "/video/video.mp4";
	//MP4類型的後段固定網址第2種
	private static final String FIXURLMP4LAST2 = "/video/video_src.mp4";
	//
	private static final String FIXURLMP4LAST3 = "/video/video_hd.mp4";
	//URL後面的數字
	private String[] course_URL = {"/km/1072","/km/1074","/km/1111","/km/1112","/km/1097"/*src*/
			,"/km/1088"/*src*/,"/km/1089"/*src*/,"/km/1110"/*src*/,"/km/1092"/*src*/,"/km/1098"/*src*/
			,"/km/1132"/*src*/,"/km/1133"/*src*/,"/km/1100"/*hd*/,"/km/1108"/*hd*/,"/km/1126"/*hd*/
			,"/km/1129"/*hd*/};//16個課程
	
	
	//video後段網址
	private String[] video_URL = {"/media/69","/media/75","/media/76","/media/77","/media/364","/media/363"
			,"/media/362","/media/361","/media/360","/media/521","/media/379","/media/377","/media/378"
			,"/media/376","/media/375","/media/374","/media/373","/media/372","/media/371","/media/370"
			,"/media/369","/media/368","/media/367","/media/366","/media/365","/media/406","/media/405"
			,"/media/404","/media/403","/media/402","/media/401","/media/400","/media/399","/media/398"
			,"/media/397","/media/398","/media/395","/media/394","/media/393","/media/392","/media/326"
			,"/media/325","/media/324","/media/323","/media/322","/media/321","/media/320","/media/319"
			,"/media/318","/media/317","/media/316","/media/315","/media/450","/media/449","/media/448"
			,"/media/447","/media/446","/media/445","/media/444","/media/443","/media/442","/media/441"
			,"/media/440","/media/439","/media/438","/media/437","/media/436","/media/435","/media/434"
			,"/media/433","/media/432","/media/431","/media/430","/media/429","/media/428","/media/427"
			,"/media/426","/media/425","/media/424","/media/423","/media/422","/media/421","/media/420"
			,"/media/419","/media/418","/media/417","/media/416","/media/415","/media/414","/media/413"
			,"/media/412","/media/411","/media/410","/media/409","/media/408","/media/407","/media/486"
			,"/media/485","/media/484","/media/483","/media/482","/media/481","/media/480","/media/479"
			,"/media/478","/media/477","/media/476","/media/475","/media/474","/media/473","/media/472"
			,"/media/471","/media/470","/media/469","/media/468","/media/467","/media/466","/media/465"
			,"/media/464","/media/462","/media/461","/media/460","/media/459","/media/458","/media/457"
			,"/media/456","/media/455","/media/454","/media/453","/media/452","/media/451","/media/391"
			,"/media/390","/media/389","/media/388","/media/387","/media/386","/media/384","/media/383"
			,"/media/382","/media/381","/media/380","/media/344","/media/343","/media/342","/media/341"
			,"/media/340","/media/339","/media/338","/media/337","/media/336","/media/358","/media/357"
			,"/media/356","/media/355","/media/354","/media/353","/media/352","/media/351","/media/350"
			,"/media/349","/media/348","/media/347","/media/346","/media/345","/media/689","/media/688"
			,"/media/687"
			,"/media/686","/media/685","/media/684","/media/683","/media/682","/media/681","/media/680"
			,"/media/679","/media/678","/media/677","/media/700","/media/699","/media/698","/media/697"
			,"/media/696","/media/695","/media/694","/media/693","/media/692","/media/691","/media/690"
			,"/media/519","/media/518"
			,"/media/517","/media/516","/media/515","/media/514","/media/513","/media/512","/media/511"
			,"/media/510","/media/509","/media/508","/media/507","/media/506","/media/505","/media/504"
			,"/media/503","/media/615","/media/614","/media/613","/media/612","/media/611","/media/610"
			,"/media/609","/media/608","/media/607","/media/606","/media/605","/media/604","/media/603"
			,"/media/602","/media/601","/media/600","/media/599","/media/598","/media/597","/media/596"
			,"/media/595","/media/594","/media/593","/media/592","/media/591","/media/590","/media/589"
			,"/media/588","/media/495","/media/494","/media/493","/media/492","/media/491","/media/489"
			,"/media/488","/media/487","/media/587","/media/586","/media/585","/media/584","/media/583"
			,"/media/582","/media/581","/media/580","/media/579","/media/578","/media/577","/media/576"
			,"/media/575","/media/574","/media/573","/media/572","/media/571","/media/570","/media/569"
			,"/media/568","/media/567","/media/566","/media/565","/media/564","/media/563","/media/562"
			,"/media/561","/media/560","/media/559","/media/558","/media/557","/media/556","/media/555"
			,"/media/554","/media/553","/media/552","/media/551","/media/550","/media/549","/media/548"
			,"/media/547","/media/546","/media/545","/media/544","/media/675","/media/674","/media/673"
			,"/media/672","/media/671","/media/670","/media/669","/media/668","/media/667","/media/666"
			,"/media/665","/media/664","/media/663","/media/662","/media/661","/media/660","/media/659"
			,"/media/658","/media/657","/media/656","/media/655","/media/654","/media/653","/media/652"
			,"/media/651","/media/650","/media/649","/media/648","/media/647","/media/646","/media/645"
			,"/media/644","/media/643","/media/642","/media/641","/media/640","/media/639","/media/638"
			,"/media/637","/media/636","/media/635","/media/634","/media/633","/media/632","/media/631"
			,"/media/630","/media/629","/media/628","/media/627","/media/626","/media/625","/media/624"
			,"/media/623","/media/622","/media/621","/media/620"};
	
	public NccuScraper() throws IOException{
		Connection.Response res;
		Connection.Response res2;
		Document document = null;
		Document documentVideo = null;
		
		Elements courseElement;//課程名稱Tag
		Elements courseInfoTag;//課程簡介Tag
		Elements list;//影片URL的上層Tag
		Elements unitTag;//影片名稱的上層Tag
		Elements imgTag;//用來獲得影片的中間Token
		
		String videoURL;//影片URL字串
		String unit;//影片名稱字串
		String courseName = null;//課程名稱字串
		String courseInfo = null;//課程簡介字串
		
		
		
		int courseNumberFirst = 0;//判斷每個課程影片要從第幾個index開始(因為放在陣列)
		int courseNumberLast = 0;//判斷每個課程要跑到第幾個index
		for(int i = 0;i <= 15 ; ++i) {//總共16個課程
			OutputFormat output = new OutputFormat();
			videoURL = null;
			unit = null;
			//連官網
			res = Jsoup.connect(TARGET_URL + course_URL[i])
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
					.method(Method.GET).execute();
			document = res.parse();
			courseElement = document.select("#xbox-inline > div.module.app-km.app-km-show > div > div.header > div > div.title");

			if(i == 0) {//YouTube Player
				String fixURLYouTube = "http://www.youtube.com/embed/";
				courseNumberLast += 4;
				courseInfoTag = document.select("#xbox-inline > div.module.app-km.app-km-show > div > div.body > div.clearfix");
				courseInfo = courseInfoTag.get(0).select("div>div").get(0).text();
				
				for(int j = courseNumberFirst ; j < courseNumberLast ; j++) {
					res2 = Jsoup.connect(TARGET_URL + video_URL[j])
							.userAgent(
									"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
							.method(Method.GET).execute();
					documentVideo = res2.parse();
					unitTag = documentVideo.select("#mediaHeader > div > div.title > div");
					list = documentVideo.select("#mediaBox > div.extraBox.clearfix > div.left.clearfix > ul > li > div > a");
					String videoURLToken[] = list.get(0).attr("href").split("=");
					unit = unitTag.get(0).text();
					
					output.setUnitName(unit);
					//因為是js生成網址，所以切他網頁中有提供的youtube網址並接上"fixURL"
					output.setUnitURL(fixURLYouTube + videoURLToken[1]);
//					System.out.println(unit);
//					System.out.println(fixURLYouTube + videoURLToken[1]);
				}
				output.setCourseInfo(courseInfo);
				courseNumberFirst = courseNumberLast;
			}
			else if(i >= 1 && i <= 3) {
				if(i == 1)courseNumberLast += 5; 
				else if (i == 2)courseNumberLast += 16;
				else if (i == 3)courseNumberLast += 15;
				for(int j = courseNumberFirst ; j < courseNumberLast ; j++) {
					res2 = Jsoup.connect(TARGET_URL + video_URL[j])
							.userAgent(
									"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
							.method(Method.GET).execute();
					documentVideo = res2.parse();
					unitTag = documentVideo.select("#mediaHeader > div > div.title > div");
					imgTag = documentVideo.select("head > meta:nth-child(8)");
					list = documentVideo.select("#fsPlayer > div.fs-videoWrap > video");
					
					unit = unitTag.get(0).text();
					String imgToken[] = imgTag.get(0).attr("content").split("/");
					
					output.setUnitName(unit);
					output.setUnitURL(FIXURLMP4 + imgToken[5] + "/" + imgToken[6] + FIXURLMP4LAST1);
//					System.out.println(unit);
//					System.out.println(FIXURLMP4 + imgToken[5] + "/" + imgToken[6] + FIXURLMP4LAST1);
					
				}
				courseNumberFirst = courseNumberLast;
			}
			else if(i >= 4 && i <= 11){
				if(i == 4)courseNumberLast += 12;
				else if( i == 5)courseNumberLast += 44;
				else if( i == 6)courseNumberLast += 35;
				else if( i == 7)courseNumberLast += 11;
				else if( i == 8)courseNumberLast += 9;
				else if( i == 9)courseNumberLast += 14;
				else if( i == 10)courseNumberLast += 13;
				else if( i == 11)courseNumberLast += 11;
				for(int j = courseNumberFirst ; j < courseNumberLast ; j++) {
					res2 = Jsoup.connect(TARGET_URL + video_URL[j])
							.userAgent(
									"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
							.method(Method.GET).execute();
					documentVideo = res2.parse();
					unitTag = documentVideo.select("#mediaHeader > div > div.title > div");
					imgTag = documentVideo.select("head > meta:nth-child(8)");
					list = documentVideo.select("#fsPlayer > div.fs-videoWrap > video");
					
					unit = unitTag.get(0).text();
					String imgToken[] = imgTag.get(0).attr("content").split("/");
					output.setUnitName(unit);
					output.setUnitURL(FIXURLMP4 + imgToken[5] + "/" + imgToken[6] + FIXURLMP4LAST2);
//					System.out.println(unit);
//					System.out.println(FIXURLMP4 + imgToken[5] + "/" + imgToken[6] + FIXURLMP4LAST2);
				}
				courseNumberFirst = courseNumberLast;
			}
			
			else if(i >= 12 && i <= 15){
				if( i == 12)courseNumberLast += 17;
				else if ( i == 13)courseNumberLast += 36;
				else if ( i == 14)courseNumberLast += 44;
				else if ( i == 15)courseNumberLast += 56;
				for(int j = courseNumberFirst ; j < courseNumberLast ; j++) {
					res2 = Jsoup.connect(TARGET_URL + video_URL[j])
							.userAgent(
									"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
							.method(Method.GET).execute();
					documentVideo = res2.parse();
					unitTag = documentVideo.select("#mediaHeader > div > div.title > div");
					imgTag = documentVideo.select("head > meta:nth-child(8)");
					list = documentVideo.select("#fsPlayer > div.fs-videoWrap > video");
					
					unit = unitTag.get(0).text();
					String imgToken[] = imgTag.get(0).attr("content").split("/");
					
					output.setUnitName(unit);
					output.setUnitURL(FIXURLMP4 + imgToken[5] + "/" + imgToken[6] + FIXURLMP4LAST3);
//					System.out.println(unit);
//					System.out.println(FIXURLMP4 + imgToken[5] + "/" + imgToken[6] + FIXURLMP4LAST3);
				}
				courseNumberFirst = courseNumberLast;
			}

			courseName = courseElement.get(0).text();
//			System.out.println(courseName);
//			System.out.println("/////////////////////////////////////////////////////////////////////");
			output.setUniversity("政治大學");
			output.setCourseName(courseName);
			output.setLectureName("null");
			output.setLecture("null");
			outputs.add(output);
		}
	}
	public ArrayList<OutputFormat> getItems() {
		return outputs;
	}
}

