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

/**
 * Servlet implementation class ExchangePictureNoteServlet
 */
//@WebServlet("/ExchangePictureNoteServlet")
public class ExchangePictureNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExchangePictureNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		ExchangeManager dbnote2 = new ExchangeManager();
		response.setHeader("Cache-Control","max-age=0");
		ArrayList<PictureNote> pictureNotes = new ArrayList<PictureNote>();
		
		
		String pictureNote_json = new Gson().toJson(pictureNotes);
		pictureNote_json = dbnote2.selectPictureNoteTable(Integer.parseInt(request.getParameter("unit_id")));
		response.setContentType("application/json");
		response.getWriter().write(pictureNote_json);			
//		System.out.println(pictureNote_json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
