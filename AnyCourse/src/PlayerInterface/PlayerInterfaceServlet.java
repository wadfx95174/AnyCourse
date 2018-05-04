package PlayerInterface;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

public class PlayerInterfaceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		PlayerInterfaceManager manager = new PlayerInterfaceManager();
		HttpSession session = request.getSession();
		
//		System.out.println(request.getParameter("unitId"));
		
		response.getWriter().write(manager.getVideoUrl(Integer.parseInt(request.getParameter("unitId"))));
				/*,(String)session.getAttribute("userId"))*/
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PlayerInterfaceManager manager = new PlayerInterfaceManager();
		HttpSession session = request.getSession();
		response.setHeader("Cache-Control","max-age=0");
		if(request.getParameter("action").equals("getVideoList")) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(manager.getList(Integer.parseInt(request.getParameter("courselistId"))));
		}
		else if(request.getParameter("action").equals("setVideoCloseTime")
				&&(String)session.getAttribute("userId") != null) {
//			System.out.println(Integer.parseInt(request.getParameter("unitId")));
//			System.out.println(Integer.parseInt(request.getParameter("currentTime")));
//			System.out.println(Integer.parseInt(request.getParameter("duration")));
			
			manager.setVideoEndTime(Integer.parseInt(request.getParameter("currentTime"))
					, Integer.parseInt(request.getParameter("unitId")), (String)session.getAttribute("userId")
					,Integer.parseInt(request.getParameter("duration")));
			
			
		}
		else if(request.getParameter("action").equals("like")&&(String)session.getAttribute("userId") != null) {
//			System.out.println(Integer.parseInt(request.getParameter("like")));
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Unit unit = new Unit();
//			System.out.println(Integer.parseInt(request.getParameter("unit_id")));
			unit = manager.setLike((String)session.getAttribute("userId")
					, Integer.parseInt(request.getParameter("unit_id"))
					,Integer.parseInt(request.getParameter("like")));
//			System.out.println(Integer.parseInt(request.getParameter("like")));
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(unit));
//			System.out.println(gson.toJson(unit));
		}
//		else if(request.getParameter("action").equals("unlike")) {
//			response.setContentType("application/json");
//			response.setCharacterEncoding("UTF-8");
//			Unit unit = new Unit();
//			unit = manager.setUnLike((String)session.getAttribute("userId"),Integer.parseInt(request.getParameter("unit_id")));
//		}
		else if(request.getParameter("action").equals("setIsBrowse")) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
//			System.out.println((String)session.getAttribute("userId"));
			if((String)session.getAttribute("userId") == null) {
				Unit unit = new Unit();
				unit.setPersonalLike(0);
				Gson gson = new Gson();
				response.getWriter().write(gson.toJson(unit));
//				System.out.println(gson.toJson(unit));
			}
			else {
				Unit unit = new Unit();
				unit = manager.setIsBrowse((String)session.getAttribute("userId")
						, Integer.parseInt(request.getParameter("unitId")));
				Gson gson = new Gson();
				response.getWriter().write(gson.toJson(unit));
//				System.out.println(gson.toJson(unit));
			}
			
		}
	}
}
