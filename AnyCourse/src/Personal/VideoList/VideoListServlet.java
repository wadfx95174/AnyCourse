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
		response.setHeader("Cache-Control","max-age=0");
		HttpSession session = request.getSession();
		ArrayList<VideoList> videoLists = new ArrayList<VideoList>();
		ArrayList<UnitVideo> unitVideos = new ArrayList<UnitVideo>();
		VideoListManager videoListDatebaseManager = new VideoListManager();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		if(request.getParameter("action").equals("selectList")) {
//			System.out.println((String)session.getAttribute("userId"));
			videoLists = videoListDatebaseManager.selectCourseListTable((String)session.getAttribute("userId"));
//			System.out.println((String)session.getAttribute("userId"));
			response.setContentType("application/json");
			response.getWriter().write(gson.toJson(videoLists));
//			System.out.println(gson.toJson(videoLists));
		}
		else if(request.getParameter("action").equals("selectUnit")) {
			videoListDatebaseManager.selectCustomListVideoTable(unitVideos
					,request.getParameter("school_name"),request.getParameter("list_name"));
			response.setContentType("application/json");
			response.getWriter().write(gson.toJson(unitVideos));
		}
		
		videoListDatebaseManager.conClose();
		
//		System.out.println(gson.toJson(videoLists));
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		HttpSession session = request.getSession();
		VideoListManager videoListDatebaseManager = new VideoListManager();
		VideoList videoList = new VideoList();
		
		//0代表insert
		if(request.getParameter("action").equals("insert")) {
			videoList.setCreator("1");
			videoList.setListName(request.getParameter("list_name"));
			videoList.setShare(0);
			videoList.setLikes(0);
			videoListDatebaseManager.insertCourseListTable(videoList,(String)session.getAttribute("userId"));
			
		}
		//1代表delete
		else if(request.getParameter("action").equals("remove")) {
			videoList.setCourselistID(Integer.parseInt(request.getParameter("courselist_id")));
			videoList.setListName(request.getParameter("list_name"));
			videoList.setCreator(request.getParameter("creator"));
			videoList.setUserID(request.getParameter("user_id"));
			videoList.setOorder(Integer.parseInt(request.getParameter("oorder")));
			videoListDatebaseManager.deleteCourseListTable(videoList);
		}
		//2代表update
		else if(request.getParameter("action").equals("update")) {
			videoList.setCreator(request.getParameter("creator"));
			videoList.setCourselistID(Integer.parseInt(request.getParameter("courselist_id")));
			videoList.setListName(request.getParameter("list_name"));
			videoListDatebaseManager.updateCourseListTable(videoList);
		}
		videoListDatebaseManager.conClose();
	}
	


}
