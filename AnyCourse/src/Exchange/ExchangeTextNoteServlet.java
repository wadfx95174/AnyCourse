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

/**
 * Servlet implementation class ExchangeNoteServlet
 */
//@WebServlet("/ExchangeNoteServlet")
public class ExchangeTextNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExchangeTextNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		ExchangeManager dbnote1 = new ExchangeManager();
				 
		ArrayList<TextNote> textNotes = new ArrayList<TextNote>();
			
		dbnote1.selectTextNoteTable(textNotes);
		String textNote_json = new Gson().toJson(textNotes);
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(textNote_json);
//		System.out.println(textNote_json);
						
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
