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
		response.setHeader("Cache-Control","max-age=0");
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
		response.setHeader("Cache-Control","max-age=0");
		String method = request.getParameter("method");
		if (method.equals("insert"))
		{
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			event.setTitle(request.getParameter("title"));
			event.setUrl(request.getParameter("url"));
			event.setStart(request.getParameter("start"));
			event.setEnd(request.getParameter("end"));
			event.setAllDay(request.getParameter("allDay").equals("true"));
			event.setBackgroundColor(request.getParameter("backgroundColor"));
			event.setBorderColor(request.getParameter("borderColor"));
			HttpSession session = request.getSession();
			int newId = calendarManager.insertEvent(event, (String)session.getAttribute("userId"));
			String json = new Gson().toJson(newId);
			response.getWriter().write(json);
		}
		else if (method.equals("delete"))
		{
			calendarManager.deleteEvent(Integer.parseInt(request.getParameter("eventId")));
		}
		else
		{
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			event.setTitle(request.getParameter("title"));
			event.setUrl(request.getParameter("url"));
			event.setStart(request.getParameter("start"));
			event.setEnd(request.getParameter("end"));
			event.setAllDay(request.getParameter("allDay").equals("true"));
			event.setBackgroundColor(request.getParameter("backgroundColor"));
			event.setBorderColor(request.getParameter("borderColor"));
			event.setId(Integer.parseInt(request.getParameter("id")));
			System.out.println(event);
			calendarManager.updateEvent(event);
		}
	}

}
