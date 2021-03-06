package Group.Note;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;




public class GroupTextNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupTextNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		GroupNoteManager dbgroupNote = new GroupNoteManager();	
		HttpSession session = request.getSession();
		Map<String, Integer> groups = (Map<String, Integer>)session.getAttribute("groups");
		
		if (groups.containsValue(Integer.parseInt(request.getParameter("groupId"))))
		{
			System.out.print("AA");
			response.setContentType("application/json;charset = utf-8;");
			response.getWriter().write(dbgroupNote.selectGroupTextNoteTable(Integer.parseInt(request.getParameter("groupId"))));	
			dbgroupNote.conClose();
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
