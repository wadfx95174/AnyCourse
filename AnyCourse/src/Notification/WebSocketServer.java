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
