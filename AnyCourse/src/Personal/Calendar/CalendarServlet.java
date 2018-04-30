package Personal.Calendar;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import Personal.CoursePlan.CoursePlanManager;

public class CalendarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CalendarManager calendarManager = new CalendarManager();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		if (request.getParameter("method").equals("getEvent"))
		{
			response.getWriter().write(new Gson().toJson(calendarManager.getEvents(userId)));
		}
		else if (request.getParameter("method").equals("getCoursePlan"))
		{
			CoursePlanManager coursePlanManager = new CoursePlanManager();
			response.getWriter().write(new Gson().toJson(coursePlanManager.getCoursePlanAllList(userId)));
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CalendarManager calendarManager = new CalendarManager();
		CalendarDTO event = new CalendarDTO();
		event.setTitle(request.getParameter("title"));
		event.setUrl(request.getParameter("url"));
		event.setStart(request.getParameter("start"));
		event.setEnd(request.getParameter("end"));
		event.setBackgroundColor(request.getParameter("backgroundColor"));
		event.setBorderColor(request.getParameter("borderColor"));

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		if (request.getParameter("method").equals("insert"))
		{
			HttpSession session = request.getSession();
			int newId = calendarManager.insertEvent(event, (String)session.getAttribute("userId"));
			String json = new Gson().toJson(newId);
			response.getWriter().write(json);
		}
		else
		{
			event.setId(Integer.parseInt(request.getParameter("id")));
			calendarManager.updateEvent(event);
		}
	}

}
