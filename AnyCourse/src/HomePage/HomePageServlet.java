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

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import RecommenderSystem.RecommendationResult;


public class HomePageServlet extends HttpServlet {
    public HomePageServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		
		
		ArrayList<Map<Integer, HomePage>> homePages = null;
		HttpSession session = request.getSession();
		HomePageManager homePageDatabaseManager = new HomePageManager();
		boolean check;//檢查該使用者是訪客、新使用者(沒有觀看紀錄)還是舊使用者(已經有推薦結果)
		//檢查該使用者有沒有推薦資料、課程清單、想要觀看、正在觀看
		check = homePageDatabaseManager.checkUser((String)session.getAttribute("userId"));
		//訪客||新使用者
		if((String)session.getAttribute("userId") == null || check == false ) {
			homePages = homePageDatabaseManager.getRandVideo();
		}
		//舊使用者
		else {
			homePages = homePageDatabaseManager.getAllVideo((String)session.getAttribute("userId"));
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(homePages));
//		System.out.println(gson.toJson(homePages));
		//關閉資料庫連線
		homePageDatabaseManager.conClose();
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		
		HttpSession session = request.getSession();
		HomePageManager homePageDatabaseManager = new HomePageManager();
		//加單一個影片到課程計畫
		if(request.getParameter("action").equals("addToCoursePlan")&&(String)session.getAttribute("userId")!=null) {
			homePageDatabaseManager.addToCoursePlan((String)session.getAttribute("userId")
					,Integer.parseInt(request.getParameter("unitID")));
		}
		//加整個清單到課程計畫
		if(request.getParameter("action").equals("addToCoursePlan_List")&&(String)session.getAttribute("userId")!=null) {
			homePageDatabaseManager.addToCoursePlan_List((String)session.getAttribute("userId")
					,Integer.parseInt(request.getParameter("courselistID")));
		}
		//關閉資料庫連線
		homePageDatabaseManager.conClose();
	}

}
