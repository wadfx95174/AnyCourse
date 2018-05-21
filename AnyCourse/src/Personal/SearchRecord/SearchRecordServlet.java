package Personal.SearchRecord;

import java.io.*;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SearchRecordServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		HttpSession session = request.getSession();
		ArrayList<SearchRecord> searchRecords = new ArrayList<SearchRecord>(); 
		SearchRecordManager searchRecordDatebaseManager = new SearchRecordManager();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
		searchRecords = searchRecordDatebaseManager.selectSearchRecordTable((String)session.getAttribute("userID"));
		
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(searchRecords));
		//關閉connection
		searchRecordDatebaseManager.conClose();
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		HttpSession session = request.getSession();
		SearchRecordManager searchRecordDatebaseManager = new SearchRecordManager();
		
		searchRecordDatebaseManager.deleteSearchRecordTable((String)session.getAttribute("userID"),
				request.getParameter("searchWord"),request.getParameter("searchTime"));
		//關閉connection
		searchRecordDatebaseManager.conClose();
	}
}
