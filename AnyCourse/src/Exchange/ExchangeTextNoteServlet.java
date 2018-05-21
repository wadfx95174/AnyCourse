package Exchange;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Note.TextNote;

public class ExchangeTextNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ExchangeManager dbnote1 = new ExchangeManager();
		response.setHeader("Cache-Control","max-age=0");		 
		ArrayList<TextNote> textNotes = new ArrayList<TextNote>();
			
		
		String textNoteJson = new Gson().toJson(textNotes);
		textNoteJson = dbnote1.selectTextNoteTable(Integer.parseInt(request.getParameter("unitId")));
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(textNoteJson);
		dbnote1.conClose();		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
