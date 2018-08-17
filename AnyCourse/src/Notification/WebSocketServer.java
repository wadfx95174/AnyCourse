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
        
        Notification tttt = new Notification();
		Gson gson = new Gson();
		
		Notification notification = gson.fromJson(message, Notification.class);
		
		//檢查是否用來開啟連接時，存userId進session
        if(notification.getType().equals("connection")) {
        	session.getUserProperties().put("userId", notification.getUserId());
        	System.out.println((String)session.getUserProperties().get("userId"));
        	return;
        }
//        session.getBasicRemote().sendText("From Server :"+message);
        Notification test2 = gson.fromJson(message,Notification.class);
        for (Session s : sessions) {    //對每個連接的Client傳送訊息
            if (s.isOpen() && s.getId() != session.getId()) {
            	tttt.setNickname((String)session.getUserProperties().get("userName"));
            	System.out.println("b:"+(String)session.getUserProperties().get("userName"));
            	tttt.setMessage(test2.getMessage());
                s.getBasicRemote().sendText(gson.toJson(tttt));
            }
        }
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
