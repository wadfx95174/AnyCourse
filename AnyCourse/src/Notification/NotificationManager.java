package Notification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

import Forum.Comment;
import KeyLabel.KeyLabel;
import Note.TextNote;
import Notification.Notification;

public class NotificationManager {
	
	public String insertNotificationSQL = "insert into notification value (null,?,?,?,null,?,?)";
	public String selectNotificationSQL = "select * from notification where userId = ? and isBrowse = 0";
	public String selectNotification1SQL = "select * from notification where userId = ?";
	public String updateNotificationSQL = "update notification set isBrowse = ? where notificationId = ?";	
	public String findCommentUserSQL = "select * from comment where commentId = ?";
	
	public Notification notification;
	public Comment comment;
	public Connection con = null;
	public Statement stat = null;
	public ResultSet result = null;
	public PreparedStatement pst = null;
	
	public NotificationManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");//µù¥UDriver
			con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=Big5"
					, "root", "peter");//¨ú±oconnection
			
		}
		catch(ClassNotFoundException e){
			System.out.println("DriverClassNotFound"+e.toString());
		}
		catch(SQLException x){
			System.out.println("Exception" + x.toString());
		}
	}
	
	public void insertNotificationTable(Notification notification){
		try {
			pst = con.prepareStatement(insertNotificationSQL);
			pst.setString(1,notification.getUserId());
			pst.setString(2,notification.getType());
			pst.setString(3,notification.getNickName());
//			pst.setString(4,notification.getReleaseTime());
			pst.setString(4,notification.getUrl());
			pst.setInt(5,notification.getIsBrowse());
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("NotificationManager-insertNotificationTable");
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	public String selectNotificationTable(String userId) {
		ArrayList<Notification> notifications = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectNotificationSQL);
			pst.setString(1, userId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 notification = new Notification();
				 notification.setNotificationId(result.getInt("notificationId"));
				 notification.setUserId(result.getString("userId"));
				 notification.setType(result.getString("type"));
				 notification.setNickName(result.getString("nickName"));
				 notification.setReleaseTime(result.getString("releaseTime"));
				 notification.setUrl(result.getString("url"));
				 notification.setIsBrowse(result.getInt("isBrowse"));
				 notifications.add(notification);
		     }
		}
		catch(SQLException x){
			System.out.println("NotificationManager-selectNotificationTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(notifications);
		return json;
	}
	
	public String selectNotification1Table(int userId) {
		ArrayList<Notification> notifications = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectNotification1SQL);
			pst.setInt(1, userId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 notification = new Notification();
				 notification.setNotificationId(result.getInt("notificationId"));
				 notification.setUserId(result.getString("userId"));
				 notification.setType(result.getString("type"));
				 notification.setNickName(result.getString("nickName"));
				 notification.setReleaseTime(result.getString("releaseTime"));
				 notification.setUrl(result.getString("url"));
				 notification.setIsBrowse(result.getInt("isBrowse"));
				 notifications.add(notification);
		     }
		}
		catch(SQLException x){
			System.out.println("NotificationManager-selectNotification1Table");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(notifications);
		return json;
	}
	
	public void updateNotificationTable(Notification notification){
		try {
			pst = con.prepareStatement(updateNotificationSQL);				
			pst.setInt(1,notification.getIsBrowse());
			pst.setInt(2,notification.getNotificationId());
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("NotificationManager-updateNotificationTable");
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	public String findCommentUser(int commentId) {
		ArrayList<Comment> comments = new ArrayList<>();
		try {
			pst = con.prepareStatement(findCommentUserSQL);
			pst.setInt(1, commentId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 comment = new Comment();
				 comment.setCommentId(result.getInt("commentId"));
				 comment.setUnitId(result.getInt("unitId"));
				 comment.setUserId(result.getString("userId"));
				 comment.setNickName(result.getString("nickName"));
				 comment.setCommentTime(result.getString("commentTime"));
				 comment.setCommentContent(result.getString("commentContent"));
				 comments.add(comment);
		     }
		}
		catch(SQLException x){
			System.out.println("NotificationManager-findCommentUser");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}		
		return comment.userId;
	}
	
	public void Close() {
		try {
			if(result!=null) {
				result.close();
			}
			if(stat!=null) {
				stat.close();
			}
			if(pst!=null) {
				pst.close();
			}
		}
		catch(SQLException e) {
			System.out.println("KeyLabelManager Close Exception :" + e.toString()); 
		}		
	} 
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("KeyLabelManager Close Exception :" + e.toString()); 
		}
	}
}
