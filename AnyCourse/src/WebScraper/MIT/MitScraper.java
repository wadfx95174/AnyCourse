package WebScraper.MIT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import WebScraper.Output.CourseList;
import WebScraper.Output.OutputFormat;

public class MitScraper implements CourseList{
	//	固定格式url
	private static final String SCHOOL_URL = "https://ocw.mit.edu";
	private static final String HOMEPAGE_URL = "/courses/audio-video-courses/";
	private static final String YOUTUBE_URL = "http://www.youtube.com/embed/";
	private static final String IMAGE_URL = "https://i.ytimg.com/vi/";
	private static final String IMAGE_PARAM = "/hqdefault.jpg";
	//	課號
	private static final String[] COURSE_ID_LIST = {"16.660J","16.853","16.885J","Supplemental","CMS.611J","CMS.930","EC.711","ESD.35J","ESD.62J","ESD.63J","ES.S41","HST.506J","STS.050","1.053J","2.003SC","2.830J","2.710","2.71","2.627","2.626","2.570","2.57","3.60","3.36","3.320","3.091SC","3.054","4.215J","4.241J","5.07SC","5.111SC","5.111","5.112","5.60","5.80","6.858","6.868J","6.874J","6.890","6.912","6.0002","6.00SC","6.00","6.0001","6.01SC","6.001","6.002","6.003","6.006","6.02","6.033","6.034","6.041","6.041SC","6.042J","6.042J","6.046J","6.046J","6.073","6.172","6.189","6.262","6.370","6.431","6.450","6.451","6.780J","6.802J","6.832","6.849","6.851","7.32","7.36J","7.01SC","7.012","7.81J","7.91J","7.014","8.05","8.422","8.04","8.421","8.334","8.333","8.286","8.01SC","8.03SC","8.04","8.591J","8.821","8.851","8.871","9.00SC","9.04","11.969","11.161J","11.309J","11.330J","11.965","14.01SC","14.43J","14.73","14.772","15.031J","15.401","15.S50","15.S50","15.S21","18.03","18.06SC","18.06","18.062J","18.062J","18.085","18.410J","18.410J","18.650","18.6501","18.S096","18.S997","18.086","18.01","18.01SC","18.02SC","18.02","18.03SC","20.219","20.490J","20.390J","21L.011","21G.034","21W.739J","21L.448J","21A.341J"};
	private static final HashSet<String> ID_SET = new HashSet<String>(Arrays.asList(COURSE_ID_LIST));
	//	初始化
	private Connection.Response res;
	private ArrayList<OutputFormat> outputs = new ArrayList<OutputFormat>();
	@Override
	public ArrayList<OutputFormat> getCourseList() {
		Document documentAll = null;
		Document documentCourse = null;
		Document documentInnerCourse = null;
		Document documentUnit = null;
		
		//	課程名稱的URL後段
		Elements courseToken; 
		//	確保抓的課程不重複
		HashSet<String> courseListUrls = new HashSet<String>(); 
		
		try
		{
			// 抓首頁
			documentAll = jsoupProcess(SCHOOL_URL + HOMEPAGE_URL);
		} catch (IOException e)
		{
			return outputs;
		}
		
		courseToken = documentAll.select(".courseRow .courseNumCol a");
		//	抓所有課程去掉多的(重複的)
		for (Element token:courseToken)
		{
			if (ID_SET.contains(token.text()))
			{
				courseListUrls.add(token.attr("href"));
			}
		}
			
			
			for (String courseListUrl: courseListUrls)
			{
				OutputFormat output = new OutputFormat();
				
				//	抓取課程
				try
				{
					documentCourse = jsoupProcess(SCHOOL_URL + courseListUrl);
				} catch (IOException e)
				{
					continue;
				}
				
				//	學校名稱
				output.setUniversity("MIT");
				//	課程名稱
				output.setCourseName(documentCourse.select("#course_title").text());
				//	教師名稱  (可能有多個)
				output.setTeacher(documentCourse.select("#course_info .ins").eachText().toString());
				//	課程簡介  (可能有多個)
				output.setCourseInfo(documentCourse.select("#description div p").eachText().toString());

//				//	筆記    **格式不同，暫時不抓 **
//				innerCourseToken = documentCourse.select(".specialfeatures li a");
//				int index = 0;
//				for (String innerToken: innerCourseToken.eachText())
//				{
//					if (innerToken.contains("note"))
//					{
//						System.out.println(urlProcess(innerCourseToken.get(index).attr("href")));
//						break;
//					}
//					index++;
//				}
					
				//	內部urls
				String innerUrl = urlProcess(documentCourse.select(".specialfeatures li a").get(0).attr("href"));
				
				//	可能會有無效網址	  (捨棄進入不同格式的網頁)
				if (innerUrl != null)
				{
					//	抓單元項目
					try
					{
						documentInnerCourse = jsoupProcess(SCHOOL_URL + innerUrl);
					} catch (IOException e)
					{
						continue;
					}
					
					//	進入單元網頁
					for (String videoUrl: documentInnerCourse.select(".medialisting > a").eachAttr("href"))
					{
						//	抓取單元頁面
						try
						{
							documentUnit = jsoupProcess(SCHOOL_URL + videoUrl);
						} catch (IOException e)
						{
							continue;
						}
						//	單元名稱
						output.setUnitName(documentUnit.select("#parent-fieldname-title").text());
						//  切 youtube uid
						String url = documentUnit.select("#vid_index .related-media-thumbnail-nolink img").attr("src");
						
						if (url != null)
						{
							String vid = "";
							if (!url.startsWith("https://img"))
							{
								url = documentUnit.select("#vid_related>ul>li>a").attr("href");
								if (url != null)
								{
									String[]tokens = url.split("/");
									vid = tokens[tokens.length - 1];
									if (vid.endsWith(".jpg"))
										vid = vid.split(".")[0];
								}
							}
							else
								vid = url.split("/")[4];
							//  圖片ID
							output.setUnitImgSrc(IMAGE_URL + vid + IMAGE_PARAM);
							//  影片ID
							output.setUnitURL(YOUTUBE_URL + vid);
						}
					}
				}
				outputs.add(output);
//				System.out.println(output);
			} // end for
		return outputs;
	}

	// 判斷開頭是否為 http://
	private String urlProcess(String url)
	{
		if (url!= null && !url.substring(0,1).equals("/"))
		{
			String subUrl = url.split("/")[2];
			// 去掉無法爬的網址
			if (subUrl.equals("www.ll.mit.edu"))
				return null;
			// 去掉開頭以符合url規格
			else if (subUrl.equals("ocw.mit.edu"))
				url = url.substring(18);
		}
		return url;
	}
	
	private Document jsoupProcess(String url) throws IOException
	{
		res = Jsoup.connect(url)
				.userAgent(
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0")
				.method(Method.GET).execute();
		return res.parse();
	}
}
