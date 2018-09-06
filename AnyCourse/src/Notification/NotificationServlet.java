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
		NotificationManager dbnotification = new NotificationManager();
		HttpSession session = request.getSession();
		response.setHeader("Cache-Control","max-age=0");
		
		String notificationJson = dbnotification.getNotification((String)session.getAttribute("userId"));
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(notificationJson);
		dbnotification.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		Notification notification;
		NotificationManager manager = new NotificationManager();
		String userId = (String)session.getAttribute("userId");
		String action = request.getParameter("action");
		String nickName = (String)session.getAttribute("nickName");
		
		
		if(action.equals("setNotificationIsBrowse")) {
			
			System.out.println(Integer.parseInt(request.getParameter("notificationId")));
			manager.setNotificationIsBrowse(Integer.parseInt(request.getParameter("notificationId")));
			
		}
		
		if(action.equals("insertNotification")){
			
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control","max-age=0");
			response.setContentType("application/json");
			
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			String toUserId = manager.findCommentUser(commentId);
			
			String url = request.getParameter("url");
			
			notification = new Notification();
			
			notification.setToUserId(toUserId);
			notification.setNotificationId(manager.insertNotification(
					toUserId,"playerInterfaceReply",nickName,url));
			notification.setNickname(nickName);
			
			response.getWriter().write(new Gson().toJson(notification));
		}
		
		if (action.equals("sendGroupInviteNotification")) {
			
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control","max-age=0");
			response.setContentType("application/json");
			
			String toUserId = (String)request.getParameter("toUserId");
			int groupId = Integer.parseInt(request.getParameter("groupId"));
			
			notification = new Notification();
			notification.setToUserId(toUserId);
			notification.setNotificationId(manager.insertNotification(
					toUserId,"groupInvitation",nickName,groupId));
			notification.setNickname(nickName);
			notification.setGroupId(groupId);
			
	    	response.getWriter().write(new Gson().toJson(notification));
	    	
	    }
		
		
//		if(state.equals("update"))
//		{
//			NoteManager dbnote = new NoteManager();
//			int textNoteId = Integer.parseInt(request.getParameter("textNoteId"));
//			String textNotestr = request.getParameter("textNote");
//			int share = Integer.parseInt(request.getParameter("share"));
//			String shareTime = request.getParameter("shareTime");
//			int likes = Integer.parseInt(request.getParameter("likes"));
//			
//			TextNote textNote = new TextNote();
//			textNote.setTextNoteId(textNoteId);
//			textNote.setUnitId(unitId);
//			textNote.setUserId(userId);
//			textNote.setTextNote(textNotestr);
//			textNote.setShare(share);
//			textNote.setShareTime(shareTime);
//			textNote.setLikes(likes);
//
//			dbnote.updateTextNoteTable(textNote);
//			dbnote.conClose();
//		}
		manager.conClose();
	}

}
