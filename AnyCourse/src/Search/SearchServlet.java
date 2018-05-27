package Search;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchQuery = request.getParameter("searchQuery");
		if (searchQuery != "")
		{
			SearchManager manager = new SearchManager();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control","max-age=0");
			Gson gson = new Gson();
			response.getWriter().print(gson.toJson(manager.getCourseListByKeyword(searchQuery)));
			manager.conClose();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		
		HttpSession session = request.getSession();
		SearchManager manager = new SearchManager();
		
		if(request.getParameter("action").equals("addToCoursePlan")&&(String)session.getAttribute("userId")!=null) {
			manager.addToCoursePlan((String)session.getAttribute("userId")
					,Integer.parseInt(request.getParameter("unitId")));
		}
		if(request.getParameter("action").equals("addToCoursePlanList")&&(String)session.getAttribute("userId")!=null) {
			manager.addToCoursePlanList((String)session.getAttribute("userId")
					,Integer.parseInt(request.getParameter("courselistId")));
		}
		manager.conClose();
	}

}
