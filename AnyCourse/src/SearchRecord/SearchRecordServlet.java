package SearchRecord;

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
		
		ArrayList<SearchRecord> searchRecords = new ArrayList<SearchRecord>(); 
		SearchRecordDatabaseManager searchRecordDatebaseManager = new SearchRecordDatabaseManager();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		
		searchRecordDatebaseManager.selectSearchRecordTable(searchRecords);
		
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(searchRecords));
//		System.out.println(gson.toJson(searchRecords));
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		
		SearchRecordDatabaseManager searchRecordDatebaseManager = new SearchRecordDatabaseManager();
		SearchRecord searchRecord = new SearchRecord();
		
		searchRecord.setUserID(request.getParameter("user_id"));
		searchRecord.setSearchWord(request.getParameter("search_word"));
		searchRecord.setSearchTime(request.getParameter("search_time"));
		
		searchRecordDatebaseManager.deleteSearchRecordTable(searchRecord);
	}
}
