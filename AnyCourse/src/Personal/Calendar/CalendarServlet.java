package Personal.Calendar;

import java.io.IOException;

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
			response.getWriter().write(new Gson().toJson(coursePlanManager.getAllUnit(userId)));
		}
		else if (request.getParameter("method").equals("getVideoList"))
		{
			CoursePlanManager coursePlanManager = new CoursePlanManager();
			response.getWriter().write(new Gson().toJson(coursePlanManager.getVideoList(userId)));
		}
		else if (request.getParameter("method").equals("getGCId"))
		{
			String gcId = calendarManager.getGoogleCalendarId(userId);
			response.getWriter().write(gcId != null ? gcId : "");
		}
		calendarManager.conClose();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CalendarManager calendarManager = new CalendarManager();
		CalendarDTO event = new CalendarDTO();
		response.setHeader("Cache-Control","max-age=0");
		String method = request.getParameter("method");
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
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
			int newId = calendarManager.insertEvent(event, userId);
			String json = new Gson().toJson(newId);
			response.getWriter().write(json);
		}
		else if (method.equals("delete"))
		{
			calendarManager.deleteEvent(Integer.parseInt(request.getParameter("eventId")));
		}
		else if (method.equals("setGCId"))
		{
			calendarManager.setGoogleCalendarId(userId, request.getParameter("gcId"));
		}
		else if (method.equals("insertGCEvent"))
		{
			String eventId = request.getParameter("eventId");
			String googleEventId = request.getParameter("googleEventId");
			calendarManager.setGoogleEventId(eventId, googleEventId);
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
			calendarManager.updateEvent(event);
		}
		calendarManager.conClose();
	}

}
