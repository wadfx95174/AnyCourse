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

/**
 * Servlet implementation class PersonalPictureNoteServlet
 */
@WebServlet("/PersonalPictureNoteServlet")
public class PersonalPictureNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PersonalPictureNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		PersonalNoteManager dbpersonalNote = new PersonalNoteManager();	
		HttpSession session = request.getSession();
		ArrayList<PersonalPictureNote> personalPictureNotes = new ArrayList<>();
		
		String personalPictureNote_json = new Gson().toJson(personalPictureNotes);
		personalPictureNote_json = dbpersonalNote.selectPersonalPictureNoteTable((String)session.getAttribute("userId"));
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(personalPictureNote_json);	
		
//		System.out.println(personalTextNote_json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
	}

}
