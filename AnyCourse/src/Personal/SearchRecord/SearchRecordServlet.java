package Personal.SearchRecord;

import java.io.*;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SearchRecordServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		ArrayList<SearchRecord> searchRecords = new ArrayList<SearchRecord>(); 
		SearchRecordManager searchRecordDatebaseManager = new SearchRecordManager();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
		searchRecords = searchRecordDatebaseManager.selectSearchRecordTable();
		
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(searchRecords));
		//關閉connection
		searchRecordDatebaseManager.conClose();
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		SearchRecordManager searchRecordDatebaseManager = new SearchRecordManager();
		
		searchRecordDatebaseManager.deleteSearchRecordTable(request.getParameter("userID"),
				request.getParameter("searchWord"),request.getParameter("searchTime"));
		//關閉connection
		searchRecordDatebaseManager.conClose();
	}
}
