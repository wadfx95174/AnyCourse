package Group.VideoList;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Group.CoursePlan.GroupCoursePlanManager;

public class GroupVideoListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		
		@SuppressWarnings("unchecked")
		Map<String, Integer> groups = (Map<String, Integer>)session.getAttribute("groups");
		
		GroupVideoListManager manager = new GroupVideoListManager();
		
		int groupId = Integer.parseInt(request.getParameter("groupId"));
	    String action = request.getParameter("action");
	    String userId = (String)session.getAttribute("userId");
	    
	    if (groups.containsValue(groupId) && action.equals("getList")) {
	    	
	    	response.getWriter().write(manager.getList(groupId, userId));
	    	
	    }
	    if(groups.containsValue(groupId) && action.equals("getUnit")) {
	    	
	    	response.getWriter().write(manager.getUnit(groupId,userId,
	    			Integer.parseInt(request.getParameter("courselistId"))));
	    	
	    }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		GroupVideoListManager manager = new GroupVideoListManager();
		
		String action = request.getParameter("action");
		
		if(action.equals("addToCoursePlanList")) {
			manager.addToCoursePlanList(userId
					,Integer.parseInt(request.getParameter("courselistId"))
					,(String)request.getParameter("creator"));
		}
		else if(action.equals("addToVideoList")) {
			manager.addToVideoList(userId, Integer.parseInt(request.getParameter("courselistId")));
		}
	}

}
