package Group.Note;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Forum.Comment;
import Forum.ForumManager;
import Note.NoteCategory;
import Note.NoteManager;


public class GroupNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		GroupNoteManager dbgroupName = new GroupNoteManager();	
		HttpSession session = request.getSession();
		
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(dbgroupName.selectGroupName((String)session.getAttribute("userId")));	
		dbgroupName.conClose();
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String state = request.getParameter("state");
		response.setHeader("Cache-Control","max-age=0");		
		String userId = (String)session.getAttribute("userId");
		int groupId = Integer.parseInt(request.getParameter("groupId"));
		int unitId = Integer.parseInt(request.getParameter("unitId"));
		
		if(state.equals("insert"))
		{
			GroupNoteManager dbgroupNote = new GroupNoteManager();
			GroupNote groupNote = new GroupNote();
			
			groupNote.setGroupId(groupId);
			groupNote.setUserId(userId);
			groupNote.setUnitId(unitId);
			
			dbgroupNote.insertGroupNoteMatchTable(groupNote);
//			response.setContentType("application/json");
			dbgroupNote.conClose();
		}
		if(state.equals("delete"))
		{
			GroupNoteManager dbgroupNote = new GroupNoteManager();			
			GroupNote groupNote = new GroupNote();
			
			groupNote.setGroupId(groupId);
			groupNote.setUserId(userId);
			groupNote.setUnitId(unitId);
						
			dbgroupNote.deleteGroupNoteMatchTable(groupNote);
			dbgroupNote.conClose();
		}
	}

}
