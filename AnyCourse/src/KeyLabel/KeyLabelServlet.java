package KeyLabel;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/KeyLabelServlet")
public class KeyLabelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private KeyLabelDatabaseManager keyLabelDatebaseManager = new KeyLabelDatabaseManager();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(keyLabelDatebaseManager.getUnitKeyLabel(Integer.parseInt(request.getParameter("unit_id"))));		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		KeyLabel keyLabel = new KeyLabel();
		String method = request.getParameter("method");
		if (method.equals("insert"))
		{
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			keyLabel.setUserId(request.getParameter("userId"));
			keyLabel.setUnitId(Integer.parseInt(request.getParameter("unitId")));
			keyLabel.setKeyLabelName(request.getParameter("keyLabelName"));
			keyLabel.setBeginTime(Integer.parseInt(request.getParameter("beginTime")));
			keyLabel.setEndTime(Integer.parseInt(request.getParameter("endTime")));
			keyLabel.setKeyLabelId(keyLabelDatebaseManager.insertKeyLabel(keyLabel));
			Gson gson = new Gson();
			String json = gson.toJson(keyLabel);
			response.getWriter().write(json);
		}
		else if (method.equals("delete"))
		{
			keyLabelDatebaseManager.deleteKeyLabel(Integer.parseInt(request.getParameter("keyLabelId")));
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
	}

}
