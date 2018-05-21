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

public class PersonalPictureNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PersonalNoteManager dbpersonalNote = new PersonalNoteManager();	
		HttpSession session = request.getSession();
		ArrayList<PersonalPictureNote> personalPictureNotes = new ArrayList<>();
		
		String personalPictureNoteJson = new Gson().toJson(personalPictureNotes);
		personalPictureNoteJson = dbpersonalNote.selectPersonalPictureNoteTable((String)session.getAttribute("userId"));
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(personalPictureNoteJson);	
		dbpersonalNote.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
