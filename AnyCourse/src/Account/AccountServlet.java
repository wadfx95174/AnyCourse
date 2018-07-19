package Account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control","max-age=0");
		HttpSession session = request.getSession();
		session.invalidate();
		response.sendRedirect("AnyCourse/HomePage.html");
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control","max-age=0");
		HttpSession session = request.getSession();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		Account account = new Account();
		if (session.getAttribute("userId")!=null)
		{
			account.setUserId((String)session.getAttribute("userId"));
			account.setNickName((String)session.getAttribute("nickName"));
			account.setPictureUrl((String)session.getAttribute("pictureUrl"));
			account.setGroups((Map<String, Integer>)session.getAttribute("groups"));
		}
		Gson gson = new Gson();
		response.getWriter().print(gson.toJson(account));
	}

}