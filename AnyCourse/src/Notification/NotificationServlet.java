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
		else if(action.equals("getToUser")) {
			response.getWriter().write(manager.getToUser(request.getParameter("toUserId")));
		}
		
		manager.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		NotificationManager manager = new NotificationManager();
		String userId = (String)session.getAttribute("userId");
		String action = request.getParameter("action");
		String nickName = (String)session.getAttribute("nickName");
		String toUserId = null;
		ArrayList<String> toUserIdList = new ArrayList<String>();
		int groupId;
		String groupName = null;
		String url = null;
		
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		response.setContentType("application/json");
		
		//播放介面討論區回覆通知(通知提問者)
		if(action.equals("playerInterfaceComment")){
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			toUserId = manager.findCommentUser(commentId);
			url = (String)request.getParameter("url");
			
			response.getWriter().write(manager.playerInterfaceComment(
					toUserId,(String)request.getParameter("type"),nickName,url));
		}
		//播放介面討論區回覆通知(通知其他回覆者)
		else if(action.equals("playerInterfaceReply")){
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			toUserIdList = manager.getForumToUser(userId,commentId);
			String commentNickname = manager.findCommentUserNickname(commentId);
			url = (String)request.getParameter("url");
			
			response.getWriter().write(manager.playerInterfaceReply(
					toUserIdList,(String)request.getParameter("type"),nickName,url,commentNickname));
		}
		//群組邀請
		else if(action.equals("sendGroupInviteNotification")) {
			
			toUserId = (String)request.getParameter("toUserId");
			groupId = Integer.parseInt(request.getParameter("groupId"));
			groupName = manager.getGroupName(groupId);
			
	    	response.getWriter().write(manager.groupInvitation(
					toUserId,"groupInvitation",nickName,groupId,groupName));
	    	
	    }
		//答應群組邀請，通知該群組其餘成員
		else if(action.equals("agreeGroupInvitation")) {
			
			url = (String)request.getParameter("url");
			groupId = Integer.parseInt(request.getParameter("groupId"));
			groupName = (String)request.getParameter("groupName");
			toUserIdList = manager.getGroupUsers(groupId, userId);
			
			response.getWriter().write(manager.agreeGroupInvitation
					(toUserIdList, "groupMemberJoin", nickName, groupId, groupName, url, userId));
			
		}
		//群組各個頁面通知
		else if(action.equals("groupNotification")) {
			url = (String)request.getParameter("url");
			groupId = Integer.parseInt(request.getParameter("groupId"));
			groupName = manager.getGroupName(groupId);
			toUserIdList = manager.getGroupUsers(groupId, userId);
			
			response.getWriter().write(manager.groupNotification
					(toUserIdList, (String)request.getParameter("type"), nickName, groupId, groupName, url, userId));
			
		}
		//群組討論區(通知提問者)
		else if(action.equals("groupComment")) {
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			toUserId = manager.findGroupCommentUser(commentId);
			url = (String)request.getParameter("url");
			groupId = Integer.parseInt(request.getParameter("groupId"));
			groupName = manager.getGroupName(groupId);
			
			response.getWriter().write(manager.groupComment(
					toUserId,(String)request.getParameter("type"),nickName,url,groupId,groupName));
		}
		//群組討論區(通知其他回覆者)
		else if(action.equals("groupReply")) {
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			url = (String)request.getParameter("url");
			groupId = Integer.parseInt(request.getParameter("groupId"));
			groupName = manager.getGroupName(groupId);
			toUserIdList = manager.getGroupForumToUser(userId, commentId);
			String commentNickname = manager.findGroupCommentUserNickname(commentId);
			
			response.getWriter().write(manager.groupReply(
					toUserIdList,(String)request.getParameter("type"),nickName,url,groupId,groupName,commentNickname));
		}
		
		manager.conClose();
	}

}
