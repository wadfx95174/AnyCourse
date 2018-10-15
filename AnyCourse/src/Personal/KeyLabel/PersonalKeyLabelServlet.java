package Personal.KeyLabel;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PersonalKeyLabelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PersonalKeyLabelManager manager = new PersonalKeyLabelManager();
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		response.setContentType("application/json");
		HttpSession session = request.getSession();
		response.getWriter().print(manager.getAllPersonalKeyLabel((String)session.getAttribute("userId")));
		manager.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		String method = request.getParameter("method");
		//取得該使用者所有群組
		if(method.equals("getAllGroup")) {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control","max-age=0");
			response.setContentType("application/json");
			
			response.getWriter().print(((Map<String, Integer>)session.getAttribute("groups")));
		}
	}

}
