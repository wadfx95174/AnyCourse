package Note;
  
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

/**
 * Servlet implementation class PictureNoteServlet
 */  
@WebServlet("/PictureNoteServlet")
public class PictureNoteServlet extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PictureNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		NoteManager dbnote = new NoteManager();	
		HttpSession session = request.getSession();
		ArrayList<PictureNote> pictureNotes = new ArrayList<PictureNote>();
//		System.out.println(request.getParameter("unit_id"));
		
		String pictureNote_json = new Gson().toJson(pictureNotes);
		pictureNote_json = dbnote.selectPictureNoteTable(Integer.parseInt(request.getParameter("unit_id")), (String)session.getAttribute("userId"));
		response.setContentType("application/json");
		response.getWriter().write(pictureNote_json);	
		
//		System.out.println(pictureNote_json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */  
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		HttpSession session = request.getSession();
		String state = request.getParameter("state");	
		 
		if(state.equals("delete"))
		{
			System.out.println("ddd");
			NoteManager dbnote = new NoteManager();
			int picture_note_id = Integer.parseInt(request.getParameter("picture_note_id"));
			
			PictureNote pictureNote = new PictureNote();
			pictureNote.setPicture_note_id(picture_note_id);
			
			
			dbnote.deletePictureNoteTable(picture_note_id);
			PrintWriter out = response.getWriter();		
//			out.print("success");
		}
		if(state.equals("insert"))
		{
//		System.out.println("AAAAA");
			int unit_id = Integer.parseInt(request.getParameter("unit_id"));
			String user_id = (String)session.getAttribute("userId");
			String picture_note_url = request.getParameter("picture_note_url");
			int share = Integer.parseInt(request.getParameter("share"));
			String share_time = request.getParameter("share_time");
			int likes = Integer.parseInt(request.getParameter("likes"));
			NoteManager dbnote = new NoteManager();
						
			PictureNote pictureNote = new PictureNote();
//			pictureNote.setPicture_note_id(picture_note_id);
			pictureNote.setUnit_id(unit_id);
			pictureNote.setUser_id(user_id);
			pictureNote.setPicture_note_url(picture_note_url);
			pictureNote.setShare(share);
			pictureNote.setShare_time(share_time);
			pictureNote.setLikes(likes);
			
					
			int id =dbnote.insertPictureNoteTable(pictureNote);
			pictureNote.setPicture_note_id(id);
			String pictureNote_json = new Gson().toJson(pictureNote);
			response.setContentType("application/json");
			response.getWriter().write(pictureNote_json);	
//			PrintWriter out = response.getWriter();		
//			out.print("success");
		}
		
	}


}
