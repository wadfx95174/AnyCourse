package LoginVerification;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import Group.Management.ManagementManager;

public class LoginVerificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LoginVerificationManager manager= new LoginVerificationManager();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		String method = request.getParameter("method");
		response.setHeader("Cache-Control","max-age=0");
		if (method.equals("checkLogin"))
		{
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
		else if (method.equals("checkExist"))
		{
			String userId = request.getParameter("userId");
			UserProfile userProfile = new UserProfile();
			userProfile.setUserId(manager.getUserId(userId));
			String json = new Gson().toJson(userProfile);
			response.getWriter().write(json);
		}
		manager.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LoginVerificationManager manager= new LoginVerificationManager(); 
		ManagementManager managementManager = new ManagementManager();
		response.setContentType("text/html");
		response.setHeader("Cache-Control","max-age=0");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
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
				UserProfile user = manager.getUserProfile(param);
				session.setAttribute("userId", user.getUserId());
				session.setAttribute("nickName", user.getNickName());
				session.setAttribute("pictureUrl", user.getPictureUrl());
				session.setAttribute("groups", managementManager.getPersonalGroups(user.getUserId()));
				response.sendRedirect("AnyCourse/HomePage.html");
			}
		}
		else if (method.equals("googleLogin"))
		{
			UserProfile userProfile = new UserProfile();
			userProfile.setUserId(request.getParameter("Account"));
			userProfile.setNickName(request.getParameter("Nickname"));
			userProfile.setEmail(request.getParameter("Email"));
			userProfile.setPictureUrl(request.getParameter("PictureUrl"));
			if (manager.getUserProfile(userProfile.getUserId()) == null)
			{
				manager.insertGoogleAccount(userProfile);
			}
			session.setAttribute("userId", userProfile.getUserId());
			session.setAttribute("nickName", userProfile.getNickName());
			session.setAttribute("pictureUrl", userProfile.getPictureUrl());
		}
		else if (method.equals("register"))
		{
			UserProfile userProfile = new UserProfile();
			userProfile.setUserId(request.getParameter("Account"));
			userProfile.setPassword(request.getParameter("Password"));
			userProfile.setEmail(request.getParameter("Email"));
			userProfile.setNickName(request.getParameter("Nickname"));
			userProfile.addFavoriteCourse(request.getParameter("Preference"));
			manager.createAccount(userProfile);
			manager.insertFavoriteCourse(userProfile);
			session.setAttribute("userId", request.getParameter("Account"));
			session.setAttribute("nickName", request.getParameter("Nickname"));
			session.setAttribute("pictureUrl", "");
			response.sendRedirect("AnyCourse/HomePage.html");
		}
		manager.conClose();
	}
}
