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
		boolean check;
		//檢查該使用者有沒有推薦資料、課程清單、想要觀看、正在觀看
		check = homePageDatabaseManager.checkUser((String)session.getAttribute("userId"));
		if((String)session.getAttribute("userId") == null || check == false ) {
			homePages = homePageDatabaseManager.getRandVideo();
		}
		else {
			homePages = homePageDatabaseManager.getAllVideo((String)session.getAttribute("userId"));
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(homePages));
		System.out.println(gson.toJson(homePages));
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		
		HttpSession session = request.getSession();
		HomePageManager homePageDatabaseManager = new HomePageManager();
		
		if(request.getParameter("action").equals("addToCoursePlan")) {
			homePageDatabaseManager.addToCoursePlan((String)session.getAttribute("userId")
					,Integer.parseInt(request.getParameter("unit_id")));
		}
		if(request.getParameter("action").equals("addToCoursePlan_List")) {
			homePageDatabaseManager.addToCoursePlan_List((String)session.getAttribute("userId")
					,Integer.parseInt(request.getParameter("unit_id")),
					Integer.parseInt(request.getParameter("courselist_id")));
		}
	}

}
