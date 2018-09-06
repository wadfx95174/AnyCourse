package Notification;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import Forum.Comment;
import Forum.ForumManager;
import Note.NoteManager;
import Note.TextNote;


public class NotificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public NotificationServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		NotificationManager manager = new NotificationManager();
		HttpSession session = request.getSession();
		
		String action = request.getParameter("action");
		
		if(action.equals("setNotificationIsBrowse")) {
			
			System.out.println(Integer.parseInt(request.getParameter("notificationId")));
			manager.setNotificationIsBrowse(Integer.parseInt(request.getParameter("notificationId")));
			
		}
		else if(action.equals("getNotification")) {
			
			String notificationJson = manager.getNotification((String)session.getAttribute("userId"));
			response.setHeader("Cache-Control","max-age=0");
			response.setContentType("application/json;charset = utf-8;");
			response.getWriter().write(notificationJson);
			
		}
		
		manager.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		Notification notification;
		NotificationManager manager = new NotificationManager();
		String userId = (String)session.getAttribute("userId");
		String action = request.getParameter("action");
		String nickName = (String)session.getAttribute("nickName");
		
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		response.setContentType("application/json");
		
		if(action.equals("insertNotification")){
			
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			String toUserId = manager.findCommentUser(commentId);
			String url = request.getParameter("url");
			
			response.getWriter().write(manager.insertNotification(
					toUserId,"playerInterfaceReply",nickName,url));
		}
		
		if (action.equals("sendGroupInviteNotification")) {
			
			String toUserId = (String)request.getParameter("toUserId");
			int groupId = Integer.parseInt(request.getParameter("groupId"));
			String groupName = manager.getGroupName(groupId);
			
	    	response.getWriter().write(manager.insertNotification(
					toUserId,"groupInvitation",nickName,groupId,groupName));
	    	
	    }
		
		manager.conClose();
	}

}
