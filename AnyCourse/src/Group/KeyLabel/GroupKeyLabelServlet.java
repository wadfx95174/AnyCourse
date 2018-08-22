package Group.KeyLabel;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GroupKeyLabelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		GroupKeyLabelManager manager = new GroupKeyLabelManager();
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		response.setContentType("application/json");
		// 檢查是否為該群組
	    HttpSession session = request.getSession();
	    Map<String, Integer> groups = (Map<String, Integer>)session.getAttribute("groups");

	    // 檢查 session 裡面有沒有傳進來的 groupId
	    if (groups.containsValue(Integer.parseInt(request.getParameter("groupId"))))  
	    	response.getWriter().print(manager.getAllGroupKeyLabel(Integer.parseInt(request.getParameter("groupId"))));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		GroupKeyLabelManager manager = new GroupKeyLabelManager();
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		response.setContentType("application/json");
		switch(request.getParameter("method"))
		{
		case "insert":
			int groupId = Integer.parseInt(request.getParameter("groupId"));
			int insertKeyLabelId = Integer.parseInt(request.getParameter("keyLabelId"));
			manager.insertKeyLabel(groupId, insertKeyLabelId);
			break;
			
		case "delete":
			int deleteKeyLabelId = Integer.parseInt(request.getParameter("keyLabelId"));
			manager.deleteKeyLabel(deleteKeyLabelId);
			break;
		}
	}
}
