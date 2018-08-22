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
		NotificationManager notificationManager = new NotificationManager();
		String userId = (String)session.getAttribute("userId");
		String state = request.getParameter("state");
		response.setHeader("Cache-Control","max-age=0");
		String action = request.getParameter("action");
		if(action.equals("setNotificationIsBrowse")) {
			System.out.println(Integer.parseInt(request.getParameter("notificationId")));
			notificationManager.setNotificationIsBrowse(userId,Integer.parseInt(request.getParameter("notificationId")));
		}
		
		if(action.equals("insertNotification"))
		{
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			String toUserId = notificationManager.findCommentUser(commentId);
			String nickName = (String)session.getAttribute("nickName");
			String url = request.getParameter("url");
			
			Notification notification = new Notification();
			
			notification.setToUserId(toUserId);
			notification.setNotificationId(notificationManager.insertNotification(
					toUserId,"playerInterfaceReply",nickName,url));
			notification.setNickname(nickName);
			
			response.setContentType("application/json");
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
		notificationManager.conClose();
	}

}
