package HomePage;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HomePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		
		
		ArrayList<Map<Integer, HomePage>> homePages = null;
		HttpSession session = request.getSession();
		HomePageManager homePageDatabaseManager = new HomePageManager();
		String userId = (String)session.getAttribute("userId");
		boolean check;//檢查該使用者是訪客、新使用者(沒有觀看紀錄)還是舊使用者(已經有推薦結果)
		//檢查該使用者有沒有推薦資料、課程清單、想要觀看、正在觀看
		check = homePageDatabaseManager.checkUser(userId);
		//訪客||新使用者
		if((String)session.getAttribute("userId") == null || check == false ) {
			homePages = homePageDatabaseManager.getRandVideo();
		}
		//舊使用者
		else {
			homePages = homePageDatabaseManager.getAllVideo(userId);
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(homePages));
		//關閉資料庫連線
		homePageDatabaseManager.conClose();
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		HomePageManager homePageDatabaseManager = new HomePageManager();
		ArrayList<HomePage> homePages = new ArrayList<HomePage>();
		
		//加單一個影片到課程計畫
		if(request.getParameter("action").equals("addToCoursePlan")&&userId!=null) {
			homePageDatabaseManager.addToCoursePlan(userId,Integer.parseInt(request.getParameter("unitId")));
		}
		//加整個清單到課程計畫
		else if(request.getParameter("action").equals("addToCoursePlanList")&&userId!=null) {
			homePageDatabaseManager.addToCoursePlanList(userId,
					Integer.parseInt(request.getParameter("courselistId")));
		}
		//獲取該使用者所有的清單名稱，讓使用者可以選擇將影片加入哪個清單
		else if(request.getParameter("action").equals("getVideoListName")&&userId!=null) {
			
			homePages = homePageDatabaseManager.getVideoListName(userId);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.setPrettyPrinting().create();
			response.setContentType("application/json");
			response.getWriter().write(gson.toJson(homePages));
		}
		else if(request.getParameter("action").equals("addToVideoList")) {
			System.out.println("a");
			homePageDatabaseManager.addToVideoList(Integer.parseInt(request.getParameter("unitId")), 
					Integer.parseInt(request.getParameter("courselistId")));
		}
		else if(request.getParameter("action").equals("addToVideoListList")) {
			homePageDatabaseManager.addToVideoListList(userId, 
					Integer.parseInt(request.getParameter("courselistId")));
		}
		//關閉資料庫連線
		homePageDatabaseManager.conClose();
	}

}
