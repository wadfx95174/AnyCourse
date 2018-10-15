package Group.Member;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GroupMemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
	    @SuppressWarnings("unchecked")
		Map<String, Integer> groups = (Map<String, Integer>)session.getAttribute("groups");
	    
	    GroupMemberManager manager = new GroupMemberManager();
	    
	    int groupId = Integer.parseInt(request.getParameter("groupId"));
	    String action = request.getParameter("action");
	    String userId = (String)session.getAttribute("userId");
	    
	    if (groups.containsValue(groupId) && action.equals("checkUserExist")) {
	    	
	    	response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control","max-age=0");
			response.setContentType("application/json");
	    	response.getWriter().write(manager.checkUserExist((String)request.getParameter("user"), groupId));
	    	
	    }
	    if(action.equals("checkJoinGroup")) {
	    	response.getWriter().write(manager.checkJoinGroup(userId, groupId));
	    }
	    else if(action.equals("joinGroup")) {
	    	
	    }
	}

}
