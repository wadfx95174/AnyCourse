package WebScraper.NTU;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import WebScraper.Output.OutputFormat;

public class NtuScraper {
	private ArrayList<OutputFormat> outputs = new ArrayList<OutputFormat>();
	//台灣大學開放式課程官網
	private static final String TARGET_URL = "http://ocw.aca.ntu.edu.tw/ntu-ocw/ocw/cou/";
	//台灣大學開放式課程影片網址片段(MP4類型)
	private static final String MP4_URL = "http://140.112.161.119/vod/";
	//課程代碼
	private String[] course_number = {"100S103","101S204","103S109","100S229","101S129","100S109","100S116"
			,"102S206","101S211","104S109","100S220","099S108","103S201","100S107","105S102","104S207"
			,"101S118","104S202","102S113","100S102","100S204","099S119","100S121","103S110","100S207"
			,"101S109","105S104","103S117","101S207","104S116","099S124","100S212","103S205","100S115"
			,"105S105","100S110","104S212","101S131","103S111","100S230","099S120","104S206","101S116"
			,"101S107","099S121","101S122","104S211","102S211","104S114","105S111","099S106","103S108"
			,"099S126","102S212","101S201","103S106","102S117","101S111","105S109","102S109","100S111"
			,"101S206","101S127","102S204","103S209","104S204","100S122","101S213","099S117","102S101"
			,"103S120","102S118","105S101","105S106","103S211","100S214","104S103","099S128","101S208"
			,"101S125","099S122","102S207","099S129","103S113","101S205","099S131","099S127","101S124"
			,"102S110","101S102","100S112","102S201","103S112","101S209","101S134","101S120","100S218"
			,"104S205","104S101","100S216","104S102","099S112","102S114","101S135","100S219","105S112"
			,"099S114","099S109","100S222","103S206","104S108","101S128","104S115","100S223","100S118"
			,"099S105","100S117","101S210","103S208","099S115","104S208","104S203","100S105","100S225"
			,"100S106","100S217","102S202","104S110","101S110","101S123","100S113","104S106","101S117"
			,"105S107","103S210","102S106","099S104","104S210","103S116","099S132","100S211","101S113"
			,"102S102","103S124","103S105","101S105","099S133","101S103","101S119","101S212","100S119"
			,"099S125","099S118","101S101","100S215","104S201","103S119","105S201","102S105","102S108"
			,"100S227","099S123","100S120","104S105","101S121","101S203","102S104","101S104","102S208"
			,"103S103","100S213","104S107","100S201","104S111","104S104","101S136","101S126","099S107"
			,"103S121","103S203","100S210","100S228","100S114","100S104","102S209","103S115","105S103"
			,"100S221","103S123","101S115","099S134","101S108","102S112","099S113","099S111","102S116"
			,"101S106","103S202","101S114","103S207","100S108","101S130","103S107","099S103","101S133"
			,"104S113","100S206","100S202","099S101","102S107","101S202","100S205","100S224","104S112"
			,"099S110","101S112","103S114","100S203","102S203","103S102"};
	
	public NtuScraper() throws IOException{
		Connection.Response res;
		Document document = null;
		
		Elements unitNumberElement;//找該課程有幾個單元要跑
		int unitNumber = 0;//同上
		
		Elements courseElement;//課程名稱Tag
		Elements list;//影片URL跟影片名稱的上層Tag
		Elements unitNameElement;
		Elements unitURLElement;//影片網址Tag
		
		String unit_lectureURL;//影片和講義URL字串，還不知道是哪一種
		String video_LectureURLToken = null;//影片和講義URL部分字串，用來檢查是影片網址還是講義網址
		String unitName = null;//影片名稱
		String courseName = null;//課程名稱字串
		String unitURL;//影片網址
		
		int x;//跑迴圈時決定跑第幾個tag
		
		for(int i = 166;i <= 219; ++i) {//總共有220個課程
			OutputFormat output = new OutputFormat();
			//連官網
			res = Jsoup.connect(TARGET_URL+course_number[i])
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
					.method(Method.GET).execute();
			document = res.parse();
			unitNumberElement = document.select("#course_description > p:nth-child(6) > span:nth-child(1)");
			unitNumber = Integer.parseInt(unitNumberElement.get(0).text());
			
			courseElement = document.select("#course_description > h2");
			courseName = courseElement.get(0).text();
			for(int j = 1 ; j <= unitNumber ; j++) {
				res = Jsoup.connect(TARGET_URL+course_number[i]+"/"+j)
						.userAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
						.method(Method.GET).execute();
				document = res.parse();
				list = document.select("#top" + j + " > div.AccordionPanelContent > div.classnote > ul > li");
				
				unitNameElement = document.select("#top" + j + " > div > div.AccordionPanelTab-text");
				try {
					unitName = unitNameElement.get(0).text();
				}catch(Exception e) {
					e.printStackTrace();
					System.out.println(courseName);
				}
				
				
				x = 0;//每次跑單元影片都是從頭開始跑
				for(Element test : list) {
					unit_lectureURL = list.select("a").get(x).attr("href");
					try {
						video_LectureURLToken = unit_lectureURL.split("/")[2];
					}catch(Exception e) {
						e.printStackTrace();
						System.out.println(courseName);
					}
					
					if(video_LectureURLToken.equals("ocw")) {
						res = Jsoup.connect(TARGET_URL+course_number[i]+"/"+j+"/V"+x)
								.userAgent(
										"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
								.method(Method.GET).execute();
						document = res.parse();
						try {
							unitURLElement = document.select("#container");
							unitURL = unitURLElement.get(0).attr("src");
							output.setUnitName(unitName);
							if(unitURL.split("/")[2].equals("bitstream"))output.setUnitURL(MP4_URL+ course_number[i] + "/" + unitURL.split("=")[1]);
							else output.setUnitURL(unitURL);
						}catch(Exception e) {
							e.printStackTrace();
							System.out.println(courseName);
						}
						
					}
					else {
						output.setLectureName(unitName);//因為他沒有講義名稱，所以把講義名稱存成影片單元名稱，而且有講義才存
						output.setLecture(unit_lectureURL);
					}
					x++;
				}
			}

			output.setUniversity("台灣大學");
			output.setCourseName(courseName);
			
			
			outputs.add(output);
		}
	}
	public ArrayList<OutputFormat> getItems() {
		return outputs;
	}
}
