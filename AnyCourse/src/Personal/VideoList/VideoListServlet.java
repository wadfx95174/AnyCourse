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
		ArrayList<UnitVideo> unitVideos = new ArrayList<UnitVideo>();
		VideoListManager videoListDatebaseManager = new VideoListManager();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		if(request.getParameter("action").equals("0")) {
			videoListDatebaseManager.selectCourseListTable(videoLists);
			response.setContentType("application/json");
			response.getWriter().write(gson.toJson(videoLists));
//			System.out.println(gson.toJson(videoLists));
		}
		else if(request.getParameter("action").equals("1")) {
			videoListDatebaseManager.selectCustomListVideoTable(unitVideos
					,request.getParameter("school_name"),request.getParameter("list_name"));
			response.setContentType("application/json");
			response.getWriter().write(gson.toJson(unitVideos));
		}
		
		
		
//		System.out.println(gson.toJson(videoLists));
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		
		VideoListManager videoListDatebaseManager = new VideoListManager();
		VideoList videoList = new VideoList();
		
		//0代表insert
		if(request.getParameter("action").equals("0")) {
			videoList.setCreator("1");
			videoList.setListName(request.getParameter("list_name"));
			videoList.setShare(0);
			videoList.setLikes(0);
			videoListDatebaseManager.insertCourseListTable(videoList);
			
		}
		//1代表delete
		else if(request.getParameter("action").equals("1")) {
			videoList.setCourselistID(Integer.parseInt(request.getParameter("courselist_id")));
			videoList.setListName(request.getParameter("list_name"));
			videoList.setCreator(request.getParameter("creator"));
			videoList.setUserID(request.getParameter("user_id"));
			videoList.setOorder(Integer.parseInt(request.getParameter("oorder")));
			videoListDatebaseManager.deleteCourseListTable(videoList);
		}
		//2代表update
		else if(request.getParameter("action").equals("2")) {
			videoList.setCreator(request.getParameter("creator"));
			videoList.setCourselistID(Integer.parseInt(request.getParameter("courselist_id")));
			videoList.setListName(request.getParameter("list_name"));
			videoListDatebaseManager.updateCourseListTable(videoList);
		}
	}
	


}
