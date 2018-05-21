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
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		if(request.getParameter("action").equals("selectList")) {
			videoLists = videoListDatebaseManager.selectVideoListTable((String)session.getAttribute("userID"));
			response.setContentType("application/json");
			response.getWriter().write(gson.toJson(videoLists));
		}
		else if(request.getParameter("action").equals("selectUnit")) {
			unitVideos = videoListDatebaseManager.selectUnitTable((String)session.getAttribute("userID")
					,request.getParameter("schoolName"),request.getParameter("lisNname"));
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
		VideoListManager videoListDatebaseManager = new VideoListManager();
		VideoList videoList = new VideoList();
		
		//insert
		if(request.getParameter("action").equals("insert")) {
			videoListDatebaseManager.insertCourseListTable((String)session.getAttribute("userID")
					,request.getParameter("listName"));
		}
		//update
		else if(request.getParameter("action").equals("update")) {
			videoList.setCreator(request.getParameter("creator"));
			videoList.setCourselistID(Integer.parseInt(request.getParameter("courselistID")));
			videoList.setListName(request.getParameter("listName"));
			videoListDatebaseManager.updateCourseListTable(videoList);
		}
		//delete
		else if(request.getParameter("action").equals("remove")) {
			videoList.setCourselistID(Integer.parseInt(request.getParameter("courselistID")));
			videoList.setListName(request.getParameter("listName"));
			videoList.setCreator(request.getParameter("creator"));
			videoList.setUserID(request.getParameter("userID"));
			videoList.setOorder(Integer.parseInt(request.getParameter("oorder")));
			videoListDatebaseManager.deleteCourseListTable(videoList);
		}
		//關閉connection
		videoListDatebaseManager.conClose();
	}
}