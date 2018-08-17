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

/**
 * Servlet implementation class NotificationServlet
 */

public class NotificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NotificationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		NotificationManager dbnotification = new NotificationManager();
		HttpSession session = request.getSession();
		response.setHeader("Cache-Control","max-age=0");
		ArrayList<Notification> notifications = new ArrayList<Notification>();
		
		String notificationJson = new Gson().toJson(notifications);
		notificationJson = dbnotification.selectNotificationTable((String)session.getAttribute("userId"));
		response.setContentType("application/json;charset = utf-8;");	
		response.getWriter().write(notificationJson);
//		System.out.print(notificationJson);
		dbnotification.conClose();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String state = request.getParameter("state");
		response.setHeader("Cache-Control","max-age=0");
		int commentId = Integer.parseInt(request.getParameter("commentId"));
		
		
		if(state.equals("insert"))
		{
//			System.out.print("AAA");
			NotificationManager dbnotification = new NotificationManager();
			String userId = (String)dbnotification.findCommentUser(commentId);
			String type = "Forum";
			String nickName = (String)session.getAttribute("nickName");			
			String url = request.getParameter("url");
			int isBrowse = 0;
			
			Notification notification = new Notification();
			
			notification.setUserId(userId);
			notification.setType(type);
			notification.setNickname(nickName);
			notification.setUrl(url);
			notification.setIsBrowse(isBrowse);
			
			
			dbnotification.insertNotificationTable(notification);
			response.setContentType("application/json");
			dbnotification.conClose();
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
		
	}

}
