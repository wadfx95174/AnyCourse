package KeyLabel;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

public class KeyLabelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control","max-age=0");
		KeyLabelManager keyLabelDatebaseManager = new KeyLabelManager();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String method = request.getParameter("method");
		if (method.equals("getPKL"))
		{
			HttpSession session = request.getSession();
			response.getWriter().write(keyLabelDatebaseManager.getPersonalKeyLabel(Integer.parseInt(request.getParameter("unitId")), (String)session.getAttribute("userId")));
		}
		else if (method.equals("getEKL"))
			response.getWriter().write(keyLabelDatebaseManager.getExangeKeyLabel(Integer.parseInt(request.getParameter("unitId"))));
		keyLabelDatebaseManager.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control","max-age=0");
		KeyLabelManager keyLabelDatebaseManager = new KeyLabelManager();
		KeyLabel keyLabel = new KeyLabel();
		String method = request.getParameter("method");
		if (method.equals("insert"))
		{
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			HttpSession session = request.getSession();
			keyLabel.setUserId((String)session.getAttribute("userId"));
			keyLabel.setUnitId(Integer.parseInt(request.getParameter("unitId")));
			keyLabel.setKeyLabelName(request.getParameter("keyLabelName"));
			keyLabel.setBeginTime(Integer.parseInt(request.getParameter("beginTime")));
			keyLabel.setEndTime(Integer.parseInt(request.getParameter("endTime")));
			keyLabel.setKeyLabelId(keyLabelDatebaseManager.insertKeyLabel(keyLabel));
			Gson gson = new Gson();
			String json = gson.toJson(keyLabel);
			response.getWriter().write(json);
		}
		else if (method.equals("update"))
		{
			keyLabel.setKeyLabelName(request.getParameter("keyLabelName"));
			keyLabel.setBeginTime(Integer.parseInt(request.getParameter("beginTime")));
			keyLabel.setEndTime(Integer.parseInt(request.getParameter("endTime")));
			keyLabel.setKeyLabelId(Integer.parseInt(request.getParameter("keyLabelId")));
			keyLabelDatebaseManager.updateKeyLabel(keyLabel);
		}
		else if (method.equals("delete"))
		{
			keyLabelDatebaseManager.deleteKeyLabel(Integer.parseInt(request.getParameter("keyLabelId")));
		}
		else if (method.equals("share"))
		{
			keyLabelDatebaseManager.shareKeyLabel(Integer.parseInt(request.getParameter("keyLabelId")), Integer.parseInt(request.getParameter("share")));
		}
//		response.setContentType("application/json");
//		response.setCharacterEncoding("UTF-8");
//		if (request.getParameter("method").equals("insert"))
//		{
//			int newId = calendarManager.insertEvent(event);
//			System.out.println(newId);
//			String json = new Gson().toJson(newId);
//			response.getWriter().write(json);
//		}
//		else
//		{
//			event.setId(Integer.parseInt(request.getParameter("id")));
//			calendarManager.updateEvent(event);
//		}
		keyLabelDatebaseManager.conClose();
	}

}
