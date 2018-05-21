package Exchange;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Note.PictureNote;

public class ExchangePictureNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ExchangeManager dbnote2 = new ExchangeManager();
		response.setHeader("Cache-Control","max-age=0");
		ArrayList<PictureNote> pictureNotes = new ArrayList<PictureNote>();
		
		
		String pictureNoteJson = new Gson().toJson(pictureNotes);
		pictureNoteJson = dbnote2.selectPictureNoteTable(Integer.parseInt(request.getParameter("unitId")));
		response.setContentType("application/json");
		response.getWriter().write(pictureNoteJson);
		dbnote2.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
