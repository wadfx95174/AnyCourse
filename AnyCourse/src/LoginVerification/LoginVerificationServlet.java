package LoginVerification;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

public class LoginVerificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private LoginVerificationDatabaseManager manager= new LoginVerificationDatabaseManager();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null)
		{
			UserProfile userProfile = new UserProfile();
			userProfile.setUserId(null);
			String json = new Gson().toJson(userProfile);
			response.getWriter().write(json);
		}
		else
		{
			UserProfile userProfile = new UserProfile();
			userProfile.setUserId((String)session.getAttribute("userId"));
			String json = new Gson().toJson(userProfile);
			response.getWriter().write(json);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
//		UserProfile userProfile = new UserProfile();
		HttpSession session = request.getSession();
		String method = request.getParameter("method");
		if (method.equals("login"))
		{
			String param = request.getParameter("AccountEmail");
			String pw = manager.getUserPassword(param);
			if (pw == null || !pw.equals(request.getParameter("Password")))
			{
				response.getWriter().println("登入失敗");
			}
			else
			{
				response.getWriter().println("登入成功");
				session.setAttribute("userId", manager.getUserId(param));
				System.out.println(session.getAttribute("userId"));
			}
		}
		else if (method.equals("register"))
		{
	
		}
	}

}
