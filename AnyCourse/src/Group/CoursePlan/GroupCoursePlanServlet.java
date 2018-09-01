package Group.CoursePlan;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GroupCoursePlanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
	    @SuppressWarnings("unchecked")
		Map<String, Integer> groups = (Map<String, Integer>)session.getAttribute("groups");
	    
	    GroupCoursePlanManager manager = new GroupCoursePlanManager();
	    
	    int groupId = Integer.parseInt(request.getParameter("groupId"));
	    String action = request.getParameter("action");
	    String userId = (String)session.getAttribute("userId");
	    
	    // 檢查 session 裡面有沒有傳進來的 groupId
	    if (groups.containsValue(groupId) && action.equals("getVideoList")) {
	    	response.getWriter().write(manager.getVideoList(groupId,userId));
	    }
	    if(groups.containsValue(groupId) && action.equals("getUnit")) {
	    	response.getWriter().write(manager.getCoursePlanUnit(userId, 
	    			Integer.parseInt(request.getParameter("courselistId")),groupId));
	    }
	    if(groups.containsValue(groupId) && action.equals("getAllUnit")) {
	    	response.getWriter().write(manager.getCoursePlanAllList(userId,groupId));
	    }
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
