package Search;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SearchManager manager = new SearchManager();
		String searchQuery = request.getParameter("searchQuery");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		Gson gson = new Gson();
		response.getWriter().print(gson.toJson(manager.getCourseListByKeyword(searchQuery)));
		System.out.println();
		manager.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		
		HttpSession session = request.getSession();
		SearchManager manager = new SearchManager();
		
		if(request.getParameter("action").equals("addToCoursePlan")&&(String)session.getAttribute("userID")!=null) {
			manager.addToCoursePlan((String)session.getAttribute("userID")
					,Integer.parseInt(request.getParameter("unitID")));
		}
		if(request.getParameter("action").equals("addToCoursePlanList")&&(String)session.getAttribute("userID")!=null) {
			manager.addToCoursePlanList((String)session.getAttribute("userID")
					,Integer.parseInt(request.getParameter("courselistID")));
		}
		manager.conClose();
	}

}
