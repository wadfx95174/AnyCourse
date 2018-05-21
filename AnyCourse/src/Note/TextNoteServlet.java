package Note;
  
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

public class TextNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		NoteManager dbnote1 = new NoteManager();
		HttpSession session = request.getSession();
		response.setHeader("Cache-Control","max-age=0");
		ArrayList<TextNote> textNotes = new ArrayList<TextNote>();
		
		String textNoteJson = new Gson().toJson(textNotes);
		textNoteJson = dbnote1.selectTextNoteTable(Integer.parseInt(request.getParameter("unitID")), (String)session.getAttribute("userID"));
		response.setContentType("application/json;charset = utf-8;");	
		response.getWriter().write(textNoteJson);
		dbnote1.conClose();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String state = request.getParameter("state");
		response.setHeader("Cache-Control","max-age=0");
		int unitID = Integer.parseInt(request.getParameter("unitID"));
		String userID = (String)session.getAttribute("userID");
		
		if(state.equals("insert"))
		{
			NoteManager dbnote = new NoteManager();
			String textNotestr = request.getParameter("textNote");
			int share = Integer.parseInt(request.getParameter("share"));
			String shareTime = request.getParameter("shareTime");
			int likes = Integer.parseInt(request.getParameter("likes"));
			
			TextNote textNote = new TextNote();
			
			textNote.setUnitID(unitID);
			textNote.setUserID(userID);
			textNote.setTextNote(textNotestr);
			textNote.setShare(share);
			textNote.setShareTime(shareTime);
			textNote.setLikes(likes);
			
			dbnote.insertTextNoteTable(textNote);
			response.setContentType("application/json");
			dbnote.conClose();
		}
		if(state.equals("update"))
		{
			NoteManager dbnote = new NoteManager();
			int textNoteID = Integer.parseInt(request.getParameter("textNoteID"));
			String textNotestr = request.getParameter("textNote");
			int share = Integer.parseInt(request.getParameter("share"));
			String shareTime = request.getParameter("shareTime");
			int likes = Integer.parseInt(request.getParameter("likes"));
			
			TextNote textNote = new TextNote();
			textNote.setTextNoteID(textNoteID);
			textNote.setUnitID(unitID);
			textNote.setUserID(userID);
			textNote.setTextNote(textNotestr);
			textNote.setShare(share);
			textNote.setShareTime(shareTime);
			textNote.setLikes(likes);

			System.out.println(textNote);
			dbnote.updateTextNoteTable(textNote);
			dbnote.conClose();
		}
		if(state.equals("share"))
		{
			NoteManager dbnote = new NoteManager();
			
			TextNote textNote = new TextNote();			
			textNote.setUnitID(unitID);
			textNote.setUserID(userID);
			
			System.out.println(userID);
			dbnote.shareTextNote(unitID,userID);
			dbnote.sharePictureNote(unitID,userID);
			dbnote.conClose();
		}
		if(state.equals("notShare"))
		{
			NoteManager dbnote = new NoteManager();
			
			TextNote textNote = new TextNote();			
			textNote.setUnitID(unitID);
			textNote.setUserID(userID);

			System.out.println(userID);
			dbnote.notShareTextNote(unitID,userID);
			dbnote.notSharePictureNote(unitID,userID);
			dbnote.conClose();
		}
		
		
	}

}
