package Group.Announcement;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AnnouncementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AnnouncementManager manager = new AnnouncementManager();
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		response.setContentType("application/json");
		// 檢查是否為該群組
		HttpSession session = request.getSession();
		Map<String, Integer> groups = (Map<String, Integer>)session.getAttribute("groups");
		if (groups.containsValue(Integer.parseInt(request.getParameter("groupId"))))
			response.getWriter().print(manager.getAllAnnouncement(Integer.parseInt(request.getParameter("groupId")), (String)session.getAttribute("userId")));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AnnouncementManager manager = new AnnouncementManager();
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		String method = request.getParameter("method");
		int groupId = Integer.parseInt(request.getParameter("groupId"));
		String userId = (String)session.getAttribute("userId");
		String title;
		String content;
		switch (method)
		{
		case "insert":
			title = request.getParameter("title");
			content = request.getParameter("content");
			manager.insertAnnouncement(groupId, userId, title, content);
			break;
		case "update":
			title = request.getParameter("title");
			content = request.getParameter("content");
			manager.updateAnnouncement(groupId, userId, title, content);
			break;
		case "delete":
			manager.deleteAnnouncement(groupId, userId);
			break;
		}
	}
}
