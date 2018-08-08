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

public class PictureNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		NoteManager dbnote = new NoteManager();	
		HttpSession session = request.getSession();
		ArrayList<PictureNote> pictureNotes = new ArrayList<PictureNote>();
		response.setHeader("Cache-Control","max-age=0");
		String pictureNoteJson = new Gson().toJson(pictureNotes);
		pictureNoteJson = dbnote.selectPictureNoteTable(Integer.parseInt(request.getParameter("unitId")), (String)session.getAttribute("userId"));
		response.setContentType("application/json");
		response.getWriter().write(pictureNoteJson);	
		dbnote.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String state = request.getParameter("state");	
		response.setHeader("Cache-Control","max-age=0");
		if(state.equals("delete"))
		{
			NoteManager dbnote = new NoteManager();
			int pictureNoteId = Integer.parseInt(request.getParameter("pictureNoteId"));
			
			PictureNote pictureNote = new PictureNote();
			pictureNote.setPictureNoteId(pictureNoteId);
			
			
			dbnote.deletePictureNoteTable(pictureNoteId);
			dbnote.conClose();
		}
		if(state.equals("insert"))
		{
			int unitId = Integer.parseInt(request.getParameter("unitId"));
			String userId = (String)session.getAttribute("userId");
			String pictureNoteUrl = request.getParameter("pictureNoteUrl");
			int share = Integer.parseInt(request.getParameter("share"));
			String shareTime = request.getParameter("shareTime");
			int likes = Integer.parseInt(request.getParameter("likes"));
			int categoryId = Integer.parseInt(request.getParameter("categoryId"));
			
			NoteManager dbnote = new NoteManager();
						
			PictureNote pictureNote = new PictureNote();
			pictureNote.setUnitId(unitId);
			pictureNote.setUserId(userId);
			pictureNote.setPictureNoteUrl(pictureNoteUrl);
			pictureNote.setShare(share);
			pictureNote.setShareTime(shareTime);
			pictureNote.setLikes(likes);
			pictureNote.setCategoryId(categoryId);
			
			int id =dbnote.insertPictureNoteTable(pictureNote);
			pictureNote.setPictureNoteId(id);
			String pictureNoteJson = new Gson().toJson(pictureNote);
			response.setContentType("application/json");
			response.getWriter().write(pictureNoteJson);
			dbnote.conClose();
		}
	}
}