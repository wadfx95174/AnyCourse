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

import com.google.gson.Gson;

/**
 * Servlet implementation class NoteUpLoad
 */
@WebServlet("/TextNoteServlet")
public class TextNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TextNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
  
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());		
		NoteManager dbnote1 = new NoteManager();
//		Note_database dbnote2 = new Note_database();
		 
		ArrayList<TextNote> textNotes = new ArrayList<TextNote>();
//		PictureNote pictureNote = new PictureNote();
		
		dbnote1.selectTextNoteTable(textNotes);
		String textNote_json = new Gson().toJson(textNotes);
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(textNote_json);
		System.out.println(textNote_json);
				
		
//		dbnote2.selectPictureNoteTable(pictureNote);
//		String pictureNote_json = new Gson().toJson(pictureNote);
//		response.setContentType("application/json");
//		response.getWriter().write(pictureNote_json);	
		
//		System.out.println(pictureNote_json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		String state = request.getParameter("state");
		
		int unit_id = Integer.parseInt(request.getParameter("unit_id"));
		String user_id = request.getParameter("user_id");
		String text_note = request.getParameter("text_note");
		int share = Integer.parseInt(request.getParameter("share"));
		String share_time = request.getParameter("share_time");
		int likes = Integer.parseInt(request.getParameter("likes")); 
		 
		if(state.equals("insert"))
		{
			NoteManager dbnote = new NoteManager();
			
			TextNote textNote = new TextNote();
			
			textNote.setUnit_id(unit_id);
			textNote.setUser_id(user_id);
			textNote.setText_note(text_note);
			textNote.setShare(share);
			textNote.setShare_time(share_time);
			textNote.setLikes(likes);
			//textNote.setText_note_id(text_note_id);
			
			dbnote.insertTextNoteTable(textNote);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();		
			out.print("success");
		}
		if(state.equals("update"))
		{
			NoteManager dbnote = new NoteManager();
			int text_note_id = Integer.parseInt(request.getParameter("text_note_id"));
			
			TextNote textNote = new TextNote();
			textNote.setText_note_id(text_note_id);
			textNote.setUnit_id(unit_id);
			textNote.setUser_id(user_id);
			textNote.setText_note(text_note);
			textNote.setShare(share);
			textNote.setShare_time(share_time);
			textNote.setLikes(likes);

			System.out.println(textNote);
			dbnote.updateTextNoteTable(textNote);
			PrintWriter out = response.getWriter();		
			out.print("success");
		}
		
	}

}
