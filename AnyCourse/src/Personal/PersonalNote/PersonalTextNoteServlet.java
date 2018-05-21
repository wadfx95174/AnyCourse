package Personal.PersonalNote;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import Personal.WatchRecord.WatchRecord;
import Personal.WatchRecord.WatchRecordManager;

public class PersonalTextNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PersonalNoteManager dbpersonalNote = new PersonalNoteManager();	
		HttpSession session = request.getSession();
		ArrayList<PersonalTextNote> personalTextNotes = new ArrayList<>();
		
		String personalTextNoteJson = new Gson().toJson(personalTextNotes);
		personalTextNoteJson = dbpersonalNote.selectPersonalTextNoteTable((String)session.getAttribute("userId"));
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(personalTextNoteJson);	
		dbpersonalNote.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
