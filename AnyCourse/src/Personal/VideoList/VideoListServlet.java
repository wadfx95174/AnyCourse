package Personal.VideoList;

import java.io.*;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class VideoListServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		HttpSession session = request.getSession();
		ArrayList<VideoList> videoLists = new ArrayList<VideoList>();
		ArrayList<UnitVideo> unitVideos = new ArrayList<UnitVideo>();
		VideoListManager videoListDatebaseManager = new VideoListManager();
		String userId = (String)session.getAttribute("userId");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		if(request.getParameter("action").equals("selectList")) {
			videoLists = videoListDatebaseManager.selectVideoListTable(userId);
			response.setContentType("application/json");
			response.getWriter().write(gson.toJson(videoLists));
		}
		else if(request.getParameter("action").equals("selectUnit")) {
			unitVideos = videoListDatebaseManager.selectUnitTable(userId
					,Integer.parseInt(request.getParameter("courselistId")));
			response.setContentType("application/json");
			response.getWriter().write(gson.toJson(unitVideos));
		}
		//關閉connection
		videoListDatebaseManager.conClose();
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		VideoListManager videoListDatebaseManager = new VideoListManager();
		VideoList videoList = new VideoList();
		
		//insert
		if(request.getParameter("action").equals("insert")) {
			videoListDatebaseManager.insertCourseListTable(userId,request.getParameter("listName"));
		}
		//update
		else if(request.getParameter("action").equals("update")) {
			videoList.setCreator(request.getParameter("creator"));
			videoList.setCourselistId(Integer.parseInt(request.getParameter("courselistId")));
			videoList.setListName(request.getParameter("listName"));
			videoListDatebaseManager.updateCourseListTable(videoList);
		}
		//delete
		else if(request.getParameter("action").equals("remove")) {
			videoList.setCourselistId(Integer.parseInt(request.getParameter("courselistId")));
			videoList.setListName(request.getParameter("listName"));
			videoList.setCreator(request.getParameter("creator"));
			videoList.setUserId(userId);
			videoList.setOorder(Integer.parseInt(request.getParameter("oorder")));
			videoListDatebaseManager.deleteCourseListTable(videoList);
		}
		//delete unitVideo
		else if(request.getParameter("action").equals("removeUnitVideo")) {
			videoListDatebaseManager.deleteUnitVideo(Integer.parseInt(request.getParameter("courselistId"))
					,Integer.parseInt(request.getParameter("unitId")));
		}
		//將完整清單添加至課程計畫
		else if(request.getParameter("action").equals("addToCoursePlanList")) {
			videoListDatebaseManager.addToCoursePlanList(userId
					,Integer.parseInt(request.getParameter("courselistId")),
					(String)request.getParameter("creator"));
		}
		//分享完整清單內容給所有人
		
		else if(request.getParameter("action").equals("shareVideoList")) {
			videoListDatebaseManager.shareVideoList(userId
					,Integer.parseInt(request.getParameter("courselistId")));
		}
		//關閉connection
		videoListDatebaseManager.conClose();
	}
}