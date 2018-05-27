package Personal.WatchRecord;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

public class WatchRecordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WatchRecordManager dbwatchRecord = new WatchRecordManager();	
		HttpSession session = request.getSession();
		ArrayList<WatchRecord> watchRecords = new ArrayList<>();
		
		String watchRecordJson = new Gson().toJson(watchRecords);
		watchRecordJson = dbwatchRecord.selectWatchRecordTable((String)session.getAttribute("userId"));
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(watchRecordJson);	
		dbwatchRecord.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		int unitId = Integer.parseInt(request.getParameter("unitId"));
		
		
		WatchRecordManager dbwatchRecord = new WatchRecordManager();
		WatchRecord watchRecord = new WatchRecord();
				
		watchRecord.setUserId(userId);
		watchRecord.setUnitId(unitId);	
		
		dbwatchRecord.deleteWatchRecordTable(userId,unitId);
		dbwatchRecord.conClose();
	}

}
