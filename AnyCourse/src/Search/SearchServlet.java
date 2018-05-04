package Search;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SearchManager manager = new SearchManager();
		String searchQuery = request.getParameter("search_query");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		Gson gson = new Gson();
		response.getWriter().print(gson.toJson(manager.getCourseListByKeyword(searchQuery)));
		
//		request.setAttribute("search_query", searchQuery);
//		RequestDispatcher view = request.getRequestDispatcher("/AnyCourse/pages/SearchResult.html");
//		view.forward(request, response);
		manager.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
