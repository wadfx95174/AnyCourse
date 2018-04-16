package Personal.VideoList;

import java.io.*;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class VideoListServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		
		ArrayList<VideoList> videoLists = new ArrayList<VideoList>(); 
		VideoListDatabaseManager videoListDatebaseManager = new VideoListDatabaseManager();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
		videoListDatebaseManager.selectSearchRecordTable(videoLists);
		
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(videoLists));
//			System.out.println(gson.toJson(searchRecords));
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		
		VideoListDatabaseManager videoListDatebaseManager = new VideoListDatabaseManager();
		VideoList videoList = new VideoList();
		
		videoList.setUserID(request.getParameter("user_id"));
		videoList.setSearchWord(request.getParameter("search_word"));
		videoList.setSearchTime(request.getParameter("search_time"));
		
		videoListDatebaseManager.deleteSearchRecordTable(videoList);
	}
	


}
