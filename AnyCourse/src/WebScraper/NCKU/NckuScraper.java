package WebScraper.NCKU;

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

public class NckuScraper {
	private ArrayList<OutputFormat> outputs = new ArrayList<OutputFormat>();
	//成功大學開放式課程官網
	private static final String TARGET_URL = "https://i-ocw.yct.ncku.edu.tw/site/course_content/";
	//URL後面的數字
	private String[] course_URL = {"b_q1r1prTcA","bq0SqR2T2je","F_TrpRq0hh8","bT1_rSprq9c","F2rRSr1rhwe"
			,"b2S2r12_Sih","b__12r0__iw","a_r00R1pr0h","F_q01S_Shhi","bS1qRq1RTji","Fr1Trq__hwi"
			,"FqqR_2S0hh9","F1Sq2qTqhic","bp22rT__2cw","b1p_pT20_8e","FR_Tp1rRhhw","FS0S2_0_hhA"
			,"bRRqTRrS08A","FTppS_q1hwj","Fr2rr1_rhi7","brRq_RSRRc7","Frp0T2p2hih","a_pST1S22TA"
			,"b02T0r02Rcc","F0_2_Srqhij","Fq0_qqqThhj","bp2pT_0pr7e","bSSRTSS20jc","b2prqr1129j"
			,"bTT_2qRr1jA","FSr10012hii","b_p01TrpSc8","aR1qpqqRqqe","b2020pqR0j7","b_pq0rR_ri9"
			,"bS_S_00219w","b0pSqr011ee","b0RSRrSTRh8","bq00p1_qR99","bq02Sp1Rq7A","arS_S_00219"
			,"bS10TS_r07h","br_1p220rcj","a0qrRp1S1r8","Fqp12Tp_hh7","FTpRSpqRhi8","bSSp22rp29i"
			,"bqr1_rR0S9e","bR_Rq011Tc9","bpTrT_rSrci","bpST1S22TAw","b_rS0q12ric","bTSSrSr2q79"
			,"b1qSpTTT_7i","b1q21_SR19h","aRr1pqSrq17","b1qpqqRqqew","FpRTqT2_hhe","FRR1R_SThhc"
			,"br00R1pr0hw","br1pqSrq17w","aq__12r0__i","b21TT2_p1h9","bp1r0SR00jj","F12pRrR1hiw"
			,"bT2p__1p_j8","F0r0TTS1hwc","a0p22rT__2c"};
	
	public NckuScraper() throws IOException{
		Connection.Response res;
		Document document = null;
		Elements courseElement;//課程名稱Tag
		Elements list;//影片URL的上層Tag
		Elements unitTag;//影片名稱的上層Tag
		String videoURL;//影片URL字串
		String unit;//影片名稱字串
		String courseName = null;//課程名稱字串
		
		
		int x;//跑迴圈時決定跑第幾個tag
		for(int i = 0;i <= 35 ; ++i) {//總共68個課程
			OutputFormat output = new OutputFormat();
			videoURL = null;
			unit = null;
			//連官網
			res = Jsoup.connect(TARGET_URL + course_URL[i])
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
					.method(Method.GET).execute();
			document = res.parse();
			courseElement = document.select("#logo > div > div > div.col-xs-12.col-sm-8.col-md-8.col-lg-8.header-title2 > h2 > b");
			
			list = document.select("#video_list_ul>li>a");
			unitTag = document.select("#video_list_ul>li>a>div>span");
			courseName = courseElement.get(0).text();
			
			output.setUniversity("成功大學-雲嘉南區域教學資源中心");
			output.setCourseName(courseName);
			x = 0;
			for(Element test : list) {//因為不知道有幾個Tag，每個課程不一樣
				unit = unitTag.get(x).text();
				videoURL = list.get(x).attr("data-video-link");
				output.setUnitName(unit);
				output.setUnitURL(videoURL);
				x++;
			}
			output.setLectureName("null");
			output.setLecture("null");
			outputs.add(output);
		}
	}
	public ArrayList<OutputFormat> getItems() {
		return outputs;
	}
}


