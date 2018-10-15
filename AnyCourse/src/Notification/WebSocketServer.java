package Notification;


import java.io.IOException;
import java.util.ArrayList;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

@ServerEndpoint("/WebSocket")
public class WebSocketServer {

	private static ArrayList<Session> sessions;
    
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
//        System.out.println("Received : "+ message);
//        System.out.println(session.getId());
        
		Gson gson = new Gson();
		//從前端傳送來的JSON，將他轉成Notification物件
		Notification notification = gson.fromJson(message, Notification.class);
		
		String type = notification.getType();
		
		//檢查是否用來開啟連接，存userId進session
        if(type.equals("connection")) {
        	session.getUserProperties().put("userId", notification.getUserId());
//        	System.out.println((String)session.getUserProperties().get("userId"));
        }
        //其餘所有類型的通知
        else {
        	String sessionsUserId = null;
        	for (Session s : sessions) {
        		sessionsUserId = (String)s.getUserProperties().get("userId");
//        		System.out.println("sessionsUserId:" + sessionsUserId);
//        		System.out.println("toUserId:" + notification.getToUserId());
                if (s.isOpen() && sessionsUserId.equals(notification.getToUserId())) {
//                	System.out.println("type: "+notification.getType());
//            		System.out.println("nickname: "+notification.getNickname());
//                	System.out.println("groupId: "+notification.getGroupId());
//                	System.out.println("groupName: "+notification.getGroupName());
//                	System.out.println("toUserId: "+notification.getToUserId());
//                	System.out.println("notification: "+notification.getNotificationId());
//                	System.out.println("url: "+notification.getUrl());
                    s.getBasicRemote().sendText(gson.toJson(notification));
                }
            }
        }
        //播放介面-討論區"回覆"通知
//        else if(type.equals("playerInterfaceReply")) {}
//        //群組邀請
//        else if(type.equals("groupInvitation")) {}
//        //群組討論區(有人"提問"或"回覆"都會通知)
//        else if(type.equals("groupForum")) {}
//        //群組公告
//        else if(type.equals("groupAnnouncement")) {}
//        //個人行事曆到期提醒
//        else if(type.equals("personalCalanderNotify")) {}
//        //群組行事曆到期提醒
//        else if(type.equals("groupCalanderNotify")) {}
//        //群組共同清單
//        else if(type.equals("groupVideoList")) {}
//        //群組共同計畫
//        else if(type.equals("groupCoursePlan")) {}
//        //群組筆記
//        else if(type.equals("groupNote")) {}
//        //群組資源庫
//        else if(type.equals("groupResourceLibrary")) {}
//        //群組有成員加入
//        else if(type.equals("groupMemberJoin")) {}
    }
 
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Session " + session.getId() + " connect success");
        if (sessions == null) {
            sessions = new ArrayList<Session>();
        }
        sessions.add(session);
    }
 
    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " + session.getId() + " disconnection");
        if (sessions == null) {
            sessions = new ArrayList<Session>();
        }
        sessions.remove(session);
    }

}
