package Note;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;


public class NoteCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoteCategoryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		NoteManager dbnote = new NoteManager();
		HttpSession session = request.getSession();
		response.setHeader("Cache-Control","max-age=0");
		ArrayList<NoteCategory> noteCategorys = new ArrayList<NoteCategory>();
		
		String noteCategoryJson = new Gson().toJson(noteCategorys);
		noteCategoryJson = dbnote.selectNoteCategoryTable((String)session.getAttribute("userId"));
		response.setContentType("application/json;charset = utf-8;");	
		response.getWriter().write(noteCategoryJson);
		dbnote.conClose();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String state = request.getParameter("state");
		response.setHeader("Cache-Control","max-age=0");		
		String userId = (String)session.getAttribute("userId");
		
		if(state.equals("insert"))
		{
			NoteManager dbnote = new NoteManager();
			String categoryName = request.getParameter("categoryName");
			
			NoteCategory noteCategory = new NoteCategory();
						
			noteCategory.setUserId(userId);
//			noteCategory.setCategoryId(categoryId);
			noteCategory.setCategoryName(categoryName);			
			
			dbnote.insertNoteCategoryTable(noteCategory);
			response.setContentType("application/json");
			dbnote.conClose();
		}
//		if(state.equals("update"))
//		{
//			NoteManager dbnote = new NoteManager();
//			
//			
//			NoteCategory noteCategory = new NoteCategory();
//			
//			noteCategory.setUserId(userId);
//			noteCategory.setCategoryId(categoryId);
//			noteCategory.setCategoryName(categoryName);	
//
//			dbnote.updateNoteCategoryTable(noteCategory);
//			dbnote.conClose();
//		}
	}

}
