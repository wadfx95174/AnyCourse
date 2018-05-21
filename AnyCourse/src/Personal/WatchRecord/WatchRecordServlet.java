package Personal.WatchRecord;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import Note.NoteManager;
import Note.PictureNote;
import Personal.SearchRecord.SearchRecord;
import Personal.SearchRecord.SearchRecordManager;

public class WatchRecordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WatchRecordManager dbwatchRecord = new WatchRecordManager();	
		HttpSession session = request.getSession();
		ArrayList<WatchRecord> watchRecords = new ArrayList<>();
		
		String watchRecordJson = new Gson().toJson(watchRecords);
		watchRecordJson = dbwatchRecord.selectWatchRecordTable((String)session.getAttribute("userID"));
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(watchRecordJson);	
		dbwatchRecord.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userID = (String)session.getAttribute("userID");
		int unitID = Integer.parseInt(request.getParameter("unitID"));
		
		
		WatchRecordManager dbwatchRecord = new WatchRecordManager();
		WatchRecord watchRecord = new WatchRecord();
				
		watchRecord.setUserID(userID);
		watchRecord.setUnitID(unitID);	
		
		dbwatchRecord.deleteWatchRecordTable(userID,unitID);
		dbwatchRecord.conClose();
	}

}
