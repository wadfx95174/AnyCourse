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

/**
 * Servlet implementation class WatchRecordServlet
 */
@WebServlet("/WatchRecordServlet")
public class WatchRecordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WatchRecordServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		WatchRecordManager dbwatchRecord = new WatchRecordManager();	
		HttpSession session = request.getSession();
		ArrayList<WatchRecord> watchRecords = new ArrayList<>();
		
		String watchRecord_json = new Gson().toJson(watchRecords);
		watchRecord_json = dbwatchRecord.selectWatchRecordTable((String)session.getAttribute("userId"));
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(watchRecord_json);	
		dbwatchRecord.conClose();
//		System.out.println(watchRecord_json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		HttpSession session = request.getSession();
		String user_id = (String)session.getAttribute("userId");
		int unit_id = Integer.parseInt(request.getParameter("unit_id"));
		
		
		WatchRecordManager dbwatchRecord = new WatchRecordManager();
		WatchRecord watchRecord = new WatchRecord();
				
		watchRecord.setUser_id(user_id);
		watchRecord.setUnit_id(unit_id);	
		
		dbwatchRecord.deleteWatchRecordTable(user_id,unit_id);
		dbwatchRecord.conClose();
	}

}
