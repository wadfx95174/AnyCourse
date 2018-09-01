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
        System.out.println("Received : "+ message);
        System.out.println(session.getId());
        
		Gson gson = new Gson();
		//從前端傳送來的JSON，將他轉成Notification物件
		Notification notification = gson.fromJson(message, Notification.class);
		
		
		//檢查是否用來開啟連接，存userId進session
        if(notification.getType().equals("connection")) {
        	session.getUserProperties().put("userId", notification.getUserId());
        	System.out.println((String)session.getUserProperties().get("userId"));
        }
        //播放介面-討論區"回覆"通知
        else if(notification.getType().equals("playerInterfaceReply")) {
        	String sessionsUserId = null;
        	for (Session s : sessions) {
        		sessionsUserId = (String)s.getUserProperties().get("userId");
//        		System.out.println("sessionsUserId" + sessionsUserId);
//        		System.out.println("toUserId" + notification.getToUserId());
                if (s.isOpen() && sessionsUserId.equals(notification.getToUserId())) {
//                	System.out.println(notification.getType());
//            		System.out.println(notification.getNickname());
//                	System.out.println(notification.getUrl());
//                	System.out.println(notification.getToUserId());
                    s.getBasicRemote().sendText(gson.toJson(notification));
                }
            }
        }
        //群組討論區(有人"提問"或"回覆"都會通知)
        else if(notification.getType().equals("groupForum")) {
        	
        }
        //群組邀請
        else if(notification.getType().equals("groupInvitation")) {
        	
        }
        //群組公告
        else if(notification.getType().equals("groupAnnouncement")) {
        	
        }
        //個人行事曆到期提醒
        else if(notification.getType().equals("personalCalanderNotify")) {
        	
        }
        //群組行事曆到期提醒
        else if(notification.getType().equals("groupCalanderNotify")) {
        	
        }
        //群組共同清單
        else if(notification.getType().equals("groupVideoList")) {
        	
        }
        //群組共同計畫
        else if(notification.getType().equals("groupCoursePlan")) {
        	
        }
        //群組筆記
        else if(notification.getType().equals("groupNote")) {
        	
        }
        //群組資源庫
        else if(notification.getType().equals("groupResourceLibrary")) {
        	
        }
        //群組有其他成員加入
        else if(notification.getType().equals("groupMember")) {
        	
        }
//        session.getBasicRemote().sendText("From Server :"+message);
//        for (Session s : sessions) {    //對每個連接的Client傳送訊息
//            if (s.isOpen() && s.getId() != session.getId()) {
//            	tttt.setNickname((String)session.getUserProperties().get("userName"));
//            	System.out.println("b:"+(String)session.getUserProperties().get("userName"));
//            	tttt.setMessage(test2.getMessage());
//                s.getBasicRemote().sendText(gson.toJson(tttt));
//            }
//        }
    }
 
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("one or more client connection");
        if (sessions == null) {
            sessions = new ArrayList<Session>();
        }
        sessions.add(session);
    }
 
    @OnClose
    public void onClose(Session session) {
        System.out.println("lost a client");
        if (sessions == null) {
            sessions = new ArrayList<Session>();
        }
        sessions.remove(session);
    }

}
