package Group.Management;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

public class ManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ManagementManager manager = new ManagementManager();
//		HttpSession session = request.getSession();
//		String userId = (String)session.getAttribute("userId");
//		response.getWriter().write(new Gson().toJson(manager.getGroups(userId)));
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
        // 檢查是否為該群組
        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        Map<String, Integer> groups = (Map<String, Integer>)session.getAttribute("groups");

        // 檢查 session 裡面有沒有傳進來的 groupId
        if (groups.containsValue(Integer.parseInt(request.getParameter("groupId"))))  
        {
        	int groupId = Integer.parseInt(request.getParameter("groupId"));
    		response.getWriter().write(new Gson().toJson(manager.getGroupInfo(groupId)));
        }
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		ManagementManager manager = new ManagementManager();
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		String groupName = request.getParameter("group-name");
		response.setContentType("text/html");
		
		int groupId = manager.insertGroup(userId, groupName);
		@SuppressWarnings("unchecked")
		Map<String, Integer> groups = (Map<String, Integer>)session.getAttribute("groups");
		groups.put(groupName, groupId);
		session.setAttribute("groups", groups);
		
		response.sendRedirect("http://localhost:8080/AnyCourse/AnyCourse/pages/Group/Management.html?group_id=" + groupId);
	}
}
