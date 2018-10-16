package Group.Calendar;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import Group.Management.GroupInfo;
import Group.Management.ManagementManager;
import Group.Management.Member;
import Personal.CoursePlan.CoursePlanManager;


public class CalendarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CalendarManager calendarManager = new CalendarManager();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		HttpSession session = request.getSession();// 檢查是否為該群組
		String userId = (String)session.getAttribute("userId");
	    Map<String, Integer> groups = (Map<String, Integer>)session.getAttribute("groups");

	    
		if (request.getParameter("method").equals("getEvent"))
		{
			// 檢查 session 裡面有沒有傳進來的 groupId
		    if (groups.containsValue(Integer.parseInt(request.getParameter("groupId"))))
		    {
		    	int groupId = Integer.parseInt(request.getParameter("groupId"));
				response.getWriter().write(new Gson().toJson(calendarManager.getEventsWithCheckingUser(groupId, userId)));	
		    }
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
			int groupId = Integer.parseInt(request.getParameter("groupId"));
			String gcId = calendarManager.getGoogleCalendarId(groupId);
			response.getWriter().write(gcId != null ? gcId : "");
		}
		else if (request.getParameter("method").equals("getManager"))
		{
			ManagementManager managementManager = new ManagementManager();
	    	int groupId = Integer.parseInt(request.getParameter("groupId"));
			GroupInfo info = managementManager.getGroupInfo(groupId);
			for (int i = 0; i < info.getManagers().size(); i++)
			{
				if (userId.equals(info.getManagers().get(i).getUserId()))
				{
					response.getWriter().write(new Gson().toJson(info.getManagers().get(i)));	
				}
			}
		}
		else if (request.getParameter("method").equals("getCreator"))
		{
			ManagementManager managementManager = new ManagementManager();
	    	int groupId = Integer.parseInt(request.getParameter("groupId"));
	    	Member member = new Member();
	    	member.setUserId(managementManager.getGroupCreator(groupId));
	    	member.setIdentity(member.getUserId().equals(userId));
			response.getWriter().print(new Gson().toJson(member));
		}
		else if (request.getParameter("method").equals("getGroupInfo"))
		{
			ManagementManager managementManager = new ManagementManager();
	    	int groupId = Integer.parseInt(request.getParameter("groupId"));
			GroupInfo info = managementManager.getGroupInfo(groupId);
			response.getWriter().print(new Gson().toJson(info));
		}
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
			int groupId = Integer.parseInt(request.getParameter("groupId"));
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			event.setUserId(userId);
			event.setTitle(request.getParameter("title"));
			event.setUrl(request.getParameter("url"));
			event.setStart(request.getParameter("start"));
			event.setEnd(request.getParameter("end"));
			event.setAllDay(request.getParameter("allDay").equals("true"));
			event.setBackgroundColor(request.getParameter("backgroundColor"));
			event.setBorderColor(request.getParameter("borderColor"));
			int newId = calendarManager.insertEvent(event, groupId);
			String json = new Gson().toJson(newId);
			response.getWriter().write(json);
		}
		else if (method.equals("delete"))
		{
			calendarManager.deleteEvent(Integer.parseInt(request.getParameter("eventId")));
		}
		else if (method.equals("setGCId"))
		{
			int groupId = Integer.parseInt(request.getParameter("groupId"));
			calendarManager.setGoogleCalendarId(groupId, request.getParameter("gcId"));
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
	}
}
