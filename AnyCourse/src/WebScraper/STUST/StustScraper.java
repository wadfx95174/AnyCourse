package WebScraper.STUST;

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

public class StustScraper implements CourseList{
	private ArrayList<OutputFormat> outputs = new ArrayList<OutputFormat>();
	//南台科技大學開放式課程官網
	private static final String TARGET_URL = "http://ocw.stust.edu.tw/tc/node/";
	//
	private static final String LECTURE_URL = "http://ocw.stust.edu.tw/Sysid";
	
	//Course的網址Token
	private String[] academic_Token = {"web_09801","statistics2_10002","statistics1_10001"
			,"economics2_09902","economics1_09901","calculus0_09802","calculus1_09802","elementary0_09801"
			,"elementary1_09801","database_01","Object-Oriented_Programming_10202","electronics_10002"
			,"electronics_10001","electronics_09902","physics2_09802","calculus1_09801","math_09801"
			,"calculus_09702","mechanics","math_09701","10502_spanish","Spanish_10001","Spanish_10002"
			,"Spanish","spanish10601","10501_spanish","Basic_Spanish2","Advanced_Spanish","spanish10602"
			,"Russian_09901","Russian","Russian_10002","Russian_10102","Modeling"
			,"Intorduction_to_Video_Production","international_09702","life_09902","cgand_09901"
			,"business_09801"};
	
	//URL後面的數字
	private String[] video_URL = {"web_09801_01","web_09801_02","web_09801_03","web_09801_04"
			,"web_09801_05","web_09801_06","statistics2_10002_01","statistics2_10002_02"
			,"statistics2_10002_03","statistics2_10002_04","statistics2_10002_05","statistics1_10001_01"
			,"statistics1_10001_02","statistics1_10001_03","statistics1_10001_04","statistics1_10001_05"
			,"statistics1_10001_06","statistics1_10001_07","economics2_09902_01","economics2_09902_02"
			,"economics2_09902_03","economics1_09901_01","economics1_09901_02","economics1_09901_03"
			,"economics1_09901_04","economics1_09901_05","economics1_09901_06","economics1_09901_07"
			,"calculus0_09802_lin01","calculus0_09802_lin02","calculus0_09802_lin03"
			,"calculus0_09802_lin04","calculus0_09802_lin05","calculus0_09802_lin06"
			,"calculus0_09802_hsiao01","calculus0_09802_hsiao02","calculus0_09802_hsiao03"
			,"calculus0_09801_lin01","calculus0_09801_lin02","calculus0_09801_lin03"
			,"calculus0_09801_lin04","calculus0_09801_hsiao01","calculus0_09801_hsiao02"
			,"calculus0_09801_hsiao3","calculus0_09801_hsiao04","calculus0_09801_hsiao05"
			,"database_011","database_012","database_013","database_014","database_015"
			,"Object-Oriented_Programming_10202_01","Object-Oriented_Programming_10202_02"
			,"Object-Oriented_Programming_10202_03","Object-Oriented_Programming_10202_04"
			,"Object-Oriented_Programming_10202_05","Object-Oriented_Programming_10202_06"
			,"electronics_10002_01","electronics_10002_02","electronics_10002_03","electronics_10002_04"
			,"electronics_10002_05","electronics_10002_06","electronics_10002_07","electronics_10002_08"
			,"electronics_10002_09","electronics_10001_01","electronics_10001_02","electronics_10001_03"
			,"electronics_10001_04","electronics_09902_01","electronics_09902_02","electronics_09902_03"
			,"electronics_09902_04","physics2_09802_01","physics2_09802_02","physics2_09802_03"
			,"physics2_09802_04","physics2_09802_05","physics2_09802_06","physics2_09802_07"
			,"physics2_09802_08","calculus1_09801_01","calculus1_09801_02","calculus1_09801_03"
			,"calculus1_09801_04","math_09801_01","math_09801_02","math_09801_03","math_09801_04"
			,"math_09801_05","math_09801_06","calculus_09702_01","calculus_09702_02","calculus_09702_03"
			,"calculus_09702_04","calculus_09702_05","calculus_09702_06","calculus_09702_07"
			,"mechanics_09702","mechanics_09702_02","mechanics_09702_03","mechanics_09702_06"
			,"mechanics_09702_07","mechanics_09702_08","mechanics_09702_09","mechanics_09702_10"
			,"mechanics_09702_11","mechanics_09702_12","math_09701_01","math_09701_02","math_09701_03"
			,"math_09701_04","math_09701_05","math_09701_06","10502_spanish2","10502_spanish3"
			,"spanish03161","spanish03092","10502_spanish0427","10502_spanish0406","10502_spanish0323"
			,"10502_spanish033001","10502_spanish041301","0502_spanish0504","spanish0309"
			,"spanish10001_01","spanish10001_02","spanish10001_03","spanish10001_05"
			,"spanish10002_01","spanish10002_02","spanish10002_03","Spanish_2","Spanish_3","Spanish_4"
			,"Spanish_5","spanish10601_video","10502_spanish0302","10502_spanish03095","spanish03163"
			,"10502_spanish03022","spanish_Indefinido","_spanish_intermediate","10502_spanish03231"
			,"10502_spanish03093","_spanish_Futuro","Presente","spanish03162","10502_spanish033002"
			,"10501_spanish041302","10501_spanish040602","spanish10202_01","spanish10202_02"
			,"spanish10202_04","spanish10202_05","Advanced_Spanish_01","Advanced_Spanish_02"
			,"Advanced_Spanish_03","Advanced_Spanish_04","Advanced_Spanish_06","Advanced_Spanish_09"
			,"Advanced_Spanish_10","Advanced_Spanish_07","Advanced_Spanish_05","spanish10602_video1"
			,"spanish10602_video2","Russin_09901_01","Russin_09901_02","Russin_09901_03","Russin_09901_04"
			,"Russin_09901_05","Russine_01","Russine_02","Russine_04","Russine_05","Russine_06"
			,"Russine_07","bussian10002_01","bussian10002_02","bussian10002_03","Russian_01","Russian_02"
			,"modeling_01","modeling_02","modeling_03","modeling_04","modeling_05","modeling_06"
			,"modeling_07","video_01","video_02","video_03","video_04","video_05","video_06","video_07"
			,"international_09702_01","international_09702_02","international_09702_03"
			,"international_09702_04","international_09702_05","international_09702_06"
			,"international_09702_07","international_09702_08","international_09702_09"
			,"international_09702_10","international_09702_011","life_09902_01","life_09902_02"
			,"life_09902_03","life_09902_04","life_09902_05","life_09902_06","life_09902_07"
			,"life_09902_08","cgand_09901_01","cgand_09901_02","cgand_09901_03","cgand_09901_04"
			,"cgand_09901_05","cgand_09901_06","cgand_09901_07","cgand_09901_08","business_09801_01"
			,"business_09801_02","business_09801_03","business_09801_04"};
	
	public StustScraper() throws IOException{
		Connection.Response res;
		Connection.Response res2;
		Document document = null;
		Document documentTarget = null;
		Elements courseElement;//課程名稱Tag
		Elements lectureElement;
		Elements list;
		
		
		String videoURLToken;//影片URL字串後半段
		String unit;//影片名稱字串
		String courseName = null;//課程名稱字串
		String lectureName;
		String unitURL;
		
		
		int courseNumberFirst = 0;//判斷每個課程影片要從第幾個index開始(因為放在陣列)
		int courseNumberLast = 0;//判斷每個課程要跑到第幾個index
		int x;
		for(int i = 0;i <= 38 ; ++i) {//總共有39個課程
			OutputFormat output = new OutputFormat();
			videoURLToken = null;
			unit = null;
			
			res = Jsoup.connect(TARGET_URL + academic_Token[i])
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
					.method(Method.GET).execute();
			documentTarget = res.parse();
			courseElement = documentTarget.select("#ContentPlaceHolder1_contentinner > h1 > div");
			
			if( i == 24 || i == 28) {
				//基礎西班牙語文法速成練習題解說
				if( i == 24)courseNumberLast += 1;
				else if( i == 24)courseNumberLast += 2;
			}
			else {
				//以下去判斷每個課程有哪些目錄，用來跑video_URL這個陣列
				if(i == 0 || i == 5 || i == 10 || i == 16 || i == 19 || i == 30)courseNumberLast += 6;
				else if(i == 2 || i == 4 || i == 17 || i == 33 || i == 34)courseNumberLast += 7;
				else if(i == 3 || i == 6 || i == 22 || i == 31)courseNumberLast += 3;
				else if(i == 7 || i == 12 || i == 13 || i == 15 || i == 38 || i == 6 
						|| i == 21 || i == 23)courseNumberLast += 4;
				else if(i == 1 || i == 8 || i == 9 || i == 29)courseNumberLast += 5;
				else if(i == 11 || i == 27)courseNumberLast += 9;
				else if(i == 14 || i == 36 || i == 37)courseNumberLast += 8;
				else if(i == 18)courseNumberLast += 10;
				else if(i == 20 || i == 35)courseNumberLast += 11;
				else if(i == 25)courseNumberLast += 14;
				else if(i == 32)courseNumberLast += 2;
				for(int j = courseNumberFirst ; j < courseNumberLast ; j++) {
					res2 = Jsoup.connect(TARGET_URL + video_URL[j])
							.userAgent(
									"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
							.method(Method.GET).execute();
					document = res2.parse();
					list = document.select("#ContentPlaceHolder1_contentinner > div.noncount > table > tbody > tr:nth-child(2) > td:nth-child(2) > b > a");
					lectureElement = document.select("#ContentPlaceHolder1_contentinner > div.noncount > table > tbody > tr:nth-child(2) > td:nth-child(1)");
					x = 0;
					for(Element test : list) {//因為不知道有幾個Tag，每個課程不一樣
						unit = list.get(x).text();
						unitURL = list.get(x).attr("href");
						output.setUnitName(unit);
						output.setUnitURL(unitURL);
//						System.out.println(unit);
//						System.out.println(unitURL);
						x++;
					}
					for(Element test : lectureElement) {
						try {
							lectureName = lectureElement.get(0).select("div > b > a").get(x).text();
							String lectureToken[] = lectureElement.get(0).select("div > b > a").get(x).attr("href").split("Sysid");
							output.setLectureName(lectureName);
							output.setLecture(LECTURE_URL + lectureToken[1]);
//							System.out.println(lectureName);
//							System.out.println(LECTURE_URL + lectureToken[1]);
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
				courseNumberFirst = courseNumberLast;
			}
			
			courseName = courseElement.get(0).text();

			output.setUniversity("南台科技大學");
			output.setCourseName(courseName);

			outputs.add(output);
		}
	}
	@Override
	public ArrayList<OutputFormat> getCourseList() {
		return outputs;
	}
}

