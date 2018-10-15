package Notification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Forum.Comment;
import KeyLabel.KeyLabel;
import Note.TextNote;
import Notification.Notification;

public class NotificationManager {
	//notification這個table的欄位順序
	//1             ;2     ;3   ;4       ;5      ;6        ;7          ;8  ;9
	//notificationId;userId;type;nickName;groupId;groupName;releaseTime;url;isBrowse
	public String selectNotificationSQL = 
			"select * from notification where userId = ? order by releaseTime desc";	
	public String findCommentUserSQL = "select * from comment where commentId = ?";
	
	public ArrayList<Notification> notifications;
	public Notification notification;
	public Comment comment;
	public Connection con = null;
	public Statement stat = null;
	public ResultSet result = null;
	public PreparedStatement pst = null;
	
	public NotificationManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=Big5"
					, "root", "peter");
			
		}
		catch(ClassNotFoundException e){
			System.out.println("DriverClassNotFound"+e.toString());
		}
		catch(SQLException x){
			System.out.println("Exception" + x.toString());
		}
	}
	//儲存通知(個人討論區回覆，通知提問者)，並且取得剛新增的通知的notificationId
	//(方法為用userId、nickname取，接著因為可能會有不只一個，所以用releaseTime排序，遠到近，所以抓最後一筆資料)
	public String playerInterfaceComment(String toUserId,String type,String nickname,String url){
		int notificationId = 0;
		try {
			pst = con.prepareStatement("insert into notification value (null,?,?,?,null,null,null,?,?,null)");
			pst.setString(1,toUserId);
			pst.setString(2,type);
			pst.setString(3,nickname);
			pst.setString(4,url);
			pst.setInt(5,0);
			pst.executeUpdate();
			
			pst = con.prepareStatement("select notificationId from notification where userId = ? and nickName = ? order by releaseTime ASC");
			pst.setString(1, toUserId);
			pst.setString(2, nickname);
			result = pst.executeQuery();
			while(result.next()) {
				notificationId = result.getInt("notificationId");
			}
			notification = new Notification();
			notification.setToUserId(toUserId);
			notification.setNotificationId(notificationId);
			notification.setNickname(nickname);
			notification.setType(type);
			notification.setUrl(url);
		}
		catch(SQLException x){
			System.out.println("NotificationManager-playerInterfaceComment");
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
		}
//		GsonBuilder builder = new GsonBuilder();
//		Gson gson = builder.setPrettyPrinting().create();
//		System.out.println(gson.toJson(notification));
		return new Gson().toJson(notification);
	}
	
	
	//儲存通知(個人討論區回覆，通知其他回覆者)，並且取得剛新增的通知的notificationId
	public String playerInterfaceReply(ArrayList<String> toUserIdList,String type,String nickname,String url,String commentNickname){
		notifications = new ArrayList<Notification>();
		try {
			//通知該群組所有人
			pst = con.prepareStatement("insert into notification value(null,?,?,?,null,null,null,?,?,?)");
			for(int i = 0;i < toUserIdList.size();i++) {
				pst.setString(1, toUserIdList.get(i));
				pst.setString(2, type);
				pst.setString(3, nickname);
				pst.setString(4, url);
				pst.setInt(5, 0);
				pst.setString(6, commentNickname);
				//先放到batch，等迴圈跑完再一次新增
				pst.addBatch();
			}
			pst.executeBatch();
			
			int userNumber = 0;//用來避免重複抓同一個使用者
			
			pst = con.prepareStatement("select notificationId from notification where nickName = ? and type = ? order by notificationId DESC");
			pst.setString(1, nickname);
			pst.setString(2, type);
			result = pst.executeQuery();
			while(result.next()) {
				//因為可能會抓到已經顯示的通知
				if(userNumber == toUserIdList.size()) break;
				
				notification = new Notification();
				notification.setNotificationId(result.getInt("notificationId"));
				notification.setToUserId(toUserIdList.get(userNumber));
				notification.setType(type);
				notification.setNickname(nickname);
				notification.setUrl(url);
				notification.setCommentNickname(commentNickname);
				notifications.add(notification);
				
				userNumber++;
			}
			
		}
		catch(SQLException x){
			System.out.println("NotificationManager-playerInterfaceReply");
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
		}
//		GsonBuilder builder = new GsonBuilder();
//		Gson gson = builder.setPrettyPrinting().create();
//		System.out.println(gson.toJson(notifications));
		return new Gson().toJson(notifications);
	}
	
	//儲存通知(群組邀請)，並且取得剛新增的通知的notificationId
	//(方法為用userId、nickname取，接著因為可能會有不只一個，所以用releaseTime排序，遠到近，所以抓最後一筆資料)
	public String groupInvitation(String toUserId,String type,String nickname,int groupId,String groupName){
		int notificationId = 0;
		try {
			pst = con.prepareStatement("insert into notification value (null,?,?,?,?,?,null,null,?,null)");
			pst.setString(1, toUserId);
			pst.setString(2, type);
			pst.setString(3, nickname);
			pst.setInt(4, groupId);
			pst.setString(5, groupName);
			pst.setInt(6, 0);
			pst.executeUpdate();
			
			pst = con.prepareStatement("select notificationId from notification where userId = ? and nickName = ? order by releaseTime ASC");
			pst.setString(1, toUserId);
			pst.setString(2, nickname);
			result = pst.executeQuery();
			while(result.next()) {
				notificationId = result.getInt("notificationId");
			}
			
			notification = new Notification();
			notification.setToUserId(toUserId);
			notification.setNotificationId(notificationId);
			notification.setGroupId(groupId);
			notification.setGroupName(groupName);
			notification.setNickname(nickname);
		}
		catch(SQLException x){
			System.out.println("NotificationManager-groupInvitation");
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
		}
		return new Gson().toJson(notification);
	}
	
	//取得通知
	public String getNotification(String userId) {
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
				 notification.setNickname(result.getString("nickName"));
				 notification.setGroupId(result.getInt("groupId"));
				 notification.setGroupName(result.getString("groupName"));
				 notification.setUrl(result.getString("url"));
				 notification.setIsBrowse(result.getInt("isBrowse"));
				 notification.setCommentNickname(result.getString("commentNickname"));
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
		return new Gson().toJson(notifications);
	}
	
	//討論區通知，取得要被通知的人的ID，存在ArrayList
	//如A提問，B回答，C回答，A回答，D回答(通知B、C，因為A已經由另一個情況通知(通知提問者)，所以這裡不把A加入ArrayList中)
	public ArrayList<String> getForumToUser(String userId, int commentId) {
		ArrayList<String> toUserIdList = new ArrayList<String>();
		String toUserId = null;
		try {
			pst = con.prepareStatement("select userId from comment where commentId = ?");
			pst.setInt(1, commentId);
			result = pst.executeQuery();
			if(result.next()) {
				toUserId = result.getString("userId");
//				if(!userId.equals(result.getString("userId")))
//					toUserIdList.add(result.getString("userId"));
			}
			
			pst = con.prepareStatement("select userId from reply where commentId = ?");
			pst.setInt(1, commentId);
			result = pst.executeQuery();
			while(result.next()) {
				String tempUserId = result.getString("userId");
				//判斷toUserIdList裡面是否已經有該userId，有的話就不加入，避免重複通知
				if((!toUserId.equals(tempUserId)) && (!toUserIdList.contains(tempUserId)) && (!userId.equals(tempUserId))) {
//					System.out.println(result.getString("userId"));
					toUserIdList.add(tempUserId);
				}
			}
		}
		catch(SQLException x){
			System.out.println("NotificationManager-getForumToUser");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return toUserIdList;
	}
	
	//找提問者的ID
	public String findCommentUser(int commentId) {
		String toUserId = null;
		try {
			pst = con.prepareStatement(findCommentUserSQL);
			pst.setInt(1, commentId);
			result = pst.executeQuery();
			if(result.next()) 
		    { 	
				toUserId = result.getString("userId");
		    }
		}
		catch(SQLException x){
			System.out.println("NotificationManager-findCommentUser");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return toUserId;
	}
	
	//找提問者的nickname
	public String findCommentUserNickname(int commentId) {
		String toUserId = null;
		try {
			pst = con.prepareStatement(findCommentUserSQL);
			pst.setInt(1, commentId);
			result = pst.executeQuery();
			while(result.next()) 
		    { 	
				toUserId = result.getString("nickName");
		    }
		}
		catch(SQLException x){
			System.out.println("NotificationManager-findCommentUserNickname");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return toUserId;
	}
	
	//找提問者的ID
	public String findGroupCommentUser(int commentId) {
		String toUserId = null;
		try {
			pst = con.prepareStatement("select userId from groupComment where commentId = ?");
			pst.setInt(1, commentId);
			result = pst.executeQuery();
			if(result.next()) 
		    { 	
				toUserId = result.getString("userId");
		    }
		}
		catch(SQLException x){
			System.out.println("NotificationManager-findGroupCommentUser");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return toUserId;
	}
	
	//找提問者的nickname
	public String findGroupCommentUserNickname(int commentId) {
		String toUserId = null;
		try {
			pst = con.prepareStatement("select nickName from groupComment where commentId = ?");
			pst.setInt(1, commentId);
			result = pst.executeQuery();
			while(result.next()) 
		    { 	
				toUserId = result.getString("nickName");
		    }
		}
		catch(SQLException x){
			System.out.println("NotificationManager-findCommentUserNickname");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return toUserId;
	}
	
	//取得groupId對應的群組名稱
	public String getGroupName(int groupId) {
		String groupName = null;
		try {
			pst = con.prepareStatement("select groupName from ggroup where groupId = ?");
			pst.setInt(1, groupId);
			result = pst.executeQuery();
			 if(result.next()) 
		     { 	
				 groupName = result.getString("groupName");
		     }
		}
		catch(SQLException x){
			System.out.println("NotificationManager-getGroupName");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return groupName;
	}
	
	//更新該使用者的通知，將isBrowse改為1(代表已經看過)
	public void setNotificationIsBrowse(int notificationId){
		try {
			pst = con.prepareStatement("update notification set isBrowse = ? where notificationId = ?");
			pst.setInt(1,1);
			pst.setInt(2, notificationId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("NotificationManager-setNotificationIsBrowse");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	//取得該群組的成員
	public ArrayList<String> getGroupUsers(int groupId, String userId) {
		ArrayList<String> toUserIdList = new ArrayList<String>();
		try {
			pst = con.prepareStatement("select userId from groupMember where groupId = ? and userId != ?");
			pst.setInt(1, groupId);
			pst.setString(2, userId);
			result = pst.executeQuery();
			while(result.next()) {
				toUserIdList.add(result.getString("userId"));
			}
		}
		catch (final SQLException x)
		{
			System.out.println("NotificationManager-getGroupUsers");
			System.out.println("Exception insert" + x.toString());
		} 
		finally {
			Close();
		}
		return toUserIdList;
	}
	
	
	//insert通知回該群組的所有人，並且將被邀請人加入該群組
	public String agreeGroupInvitation(ArrayList<String> toUserIdList,String type,String nickname,int groupId,String groupName,String url,String userId) {
		notifications = new ArrayList<Notification>();
		try
		{
			//通知該群組所有人
			pst = con.prepareStatement("insert into notification value(null,?,?,?,?,?,null,?,?,null)");
			for(int i = 0;i < toUserIdList.size();i++) {
				
				pst.setString(1, toUserIdList.get(i));
				pst.setString(2, type);
				pst.setString(3, nickname);
				pst.setInt(4, groupId);
				pst.setString(5, groupName);
				pst.setString(6, url);
				pst.setInt(7, 0);
				//先放到batch，等迴圈跑完再一次新增
				pst.addBatch();
			}
			pst.executeBatch();
			
			int index = 0;
			
//			for(int i = 0 ; i < toUserIdList.size();i++) {
				pst = con.prepareStatement("select notificationId from notification where nickName = ? and type = ? order by releaseTime ASC");
				pst.setString(1, nickname);
				pst.setString(2, type);
				result = pst.executeQuery();
				while(result.next()) {
					notification = new Notification();
					notification.setNotificationId(result.getInt("notificationId"));
					notification.setToUserId(toUserIdList.get(index));
					notification.setType(type);
					notification.setNickname(nickname);
					notification.setGroupId(groupId);
					notification.setGroupName(groupName);
					notification.setUrl(url);
					notifications.add(notification);
					index++;
				}
//			}
			
			//加入群組
			pst = con.prepareStatement("insert into groupMember value (?,?,?)");
			pst.setInt(1,groupId);
			pst.setString(2,userId);
			pst.setInt(3, 0);
			pst.executeUpdate();
			
			
		} 
		catch (final SQLException x)
		{
			System.out.println("NotificationManager-agreeGroupInvitation");
			System.out.println("Exception insert" + x.toString());
		} 
		finally
		{
			Close();
		}
//		GsonBuilder builder = new GsonBuilder();
//		Gson gson = builder.setPrettyPrinting().create();
//		System.out.println(gson.toJson(notifications));
		return new Gson().toJson(notifications);
	}
	
	
	//當群組各個頁面有通知時呼叫此method
	public String groupNotification(ArrayList<String> toUserIdList,String type,String nickname,int groupId,String groupName,String url,String userId) {
		notifications = new ArrayList<Notification>();
		try
		{
			//通知該群組所有人
			pst = con.prepareStatement("insert into notification value(null,?,?,?,?,?,null,?,?,null)");
			for(int i = 0;i < toUserIdList.size();i++) {
				pst.setString(1, toUserIdList.get(i));
				pst.setString(2, type);
				pst.setString(3, nickname);
				pst.setInt(4, groupId);
				pst.setString(5, groupName);
				pst.setString(6, url);
				pst.setInt(7, 0);
				//先放到batch，等迴圈跑完再一次新增
				pst.addBatch();
			}
			pst.executeBatch();
			
			int userNumber = 0;//用來避免重複抓同一個使用者
			
			pst = con.prepareStatement("select notificationId from notification where nickName = ? and type = ? order by notificationId DESC");
			pst.setString(1, nickname);
			pst.setString(2, type);
			result = pst.executeQuery();
			while(result.next()) {
				//因為可能會抓到已經顯示的通知
				if(userNumber == toUserIdList.size()) break;
				
				notification = new Notification();
				notification.setNotificationId(result.getInt("notificationId"));
				notification.setToUserId(toUserIdList.get(userNumber));
				notification.setType(type);
				notification.setNickname(nickname);
				notification.setGroupId(groupId);
				notification.setGroupName(groupName);
				notification.setUrl(url);
				notifications.add(notification);
				
				userNumber++;
			}
		} 
		catch (final SQLException x)
		{
			System.out.println("NotificationManager-groupNotification");
			System.out.println("Exception insert" + x.toString());
		} 
		finally
		{
			Close();
		}
//		GsonBuilder builder = new GsonBuilder();
//		Gson gson = builder.setPrettyPrinting().create();
//		System.out.println(gson.toJson(notifications));
		return new Gson().toJson(notifications);
	}
	
	//當群組各個頁面有通知時呼叫此method
	public String getToUser(String toUserId) {
		String toUser = null;
		try
		{
			pst = con.prepareStatement("select nickName from account where userId = ?");
			pst.setString(1, toUserId);
			result = pst.executeQuery();
			if(result.next()) toUser = result.getString("nickName");
		} 
		catch (final SQLException x)
		{
			System.out.println("NotificationManager-getToUser");
			System.out.println("Exception insert" + x.toString());
		} 
		finally
		{
			Close();
		}
		return toUser;
	}
	
	//新增通知(群組討論區回覆，通知提問者)，並且取得剛新增的通知的notificationId
	//(方法為用userId、nickname取，接著因為可能會有不只一個，所以用releaseTime排序，遠到近，所以抓最後一筆資料)
	public String groupComment(String toUserId,String type,String nickname,String url,int groupId,String groupName){
		int notificationId = 0;
		try {
			pst = con.prepareStatement("insert into notification value (null,?,?,?,?,?,null,?,0,null)");
			pst.setString(1,toUserId);
			pst.setString(2,type);
			pst.setString(3,nickname);
			pst.setInt(4,groupId);
			pst.setString(5,groupName);
			pst.setString(6,url);
			
			pst.executeUpdate();
			
			pst = con.prepareStatement("select notificationId from notification where userId = ? and nickName = ? order by releaseTime ASC");
			pst.setString(1, toUserId);
			pst.setString(2, nickname);
			result = pst.executeQuery();
			while(result.next()) {
				notificationId = result.getInt("notificationId");
			}
			notification = new Notification();
			notification.setToUserId(toUserId);
			notification.setNotificationId(notificationId);
			notification.setNickname(nickname);
			notification.setType(type);
			notification.setUrl(url);
			notification.setGroupId(groupId);
			notification.setGroupName(groupName);
		}
		catch(SQLException x){
			System.out.println("NotificationManager-groupComment");
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
		}
//		GsonBuilder builder = new GsonBuilder();
//		Gson gson = builder.setPrettyPrinting().create();
//		System.out.println(gson.toJson(notification));
		return new Gson().toJson(notification);
	}
	
	//新增通知(群組討論區回覆，通知其他提問者)，並且取得剛新增的通知的notificationId
	//(方法為用userId、nickname取，接著因為可能會有不只一個，所以用releaseTime排序，遠到近，所以抓最後一筆資料)
	public String groupReply(ArrayList<String> toUserIdList,String type,String nickname,String url,int groupId,String groupName,String commentNickname){
		notifications = new ArrayList<Notification>();
		try {
			pst = con.prepareStatement("insert into notification value (null,?,?,?,?,?,null,?,0,?)");
			for(int i = 0;i < toUserIdList.size();i++) {
				pst.setString(1, toUserIdList.get(i));
				pst.setString(2, type);
				pst.setString(3, nickname);
				pst.setInt(4, groupId);
				pst.setString(5, groupName);
				pst.setString(6, url);
				pst.setString(7, commentNickname);
				//先放到batch，等迴圈跑完再一次新增
				pst.addBatch();
			}
			pst.executeBatch();
			
			int userNumber = 0;//用來避免重複抓同一個使用者
			
			pst = con.prepareStatement("select notificationId from notification where nickName = ? and type = ? order by notificationId DESC");
			pst.setString(1, nickname);
			pst.setString(2, type);
			result = pst.executeQuery();
			while(result.next()) {
				//因為可能會抓到已經顯示的通知
				if(userNumber == toUserIdList.size()) break;
				
				notification = new Notification();
				notification.setNotificationId(result.getInt("notificationId"));
				notification.setToUserId(toUserIdList.get(userNumber));
				notification.setType(type);
				notification.setNickname(nickname);
				notification.setGroupId(groupId);
				notification.setGroupName(groupName);
				notification.setUrl(url);
				notification.setCommentNickname(commentNickname);
				notifications.add(notification);
				
				userNumber++;
			}
		}
		catch(SQLException x){
			System.out.println("NotificationManager-groupReply");
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
		}
//		GsonBuilder builder = new GsonBuilder();
//		Gson gson = builder.setPrettyPrinting().create();
//		System.out.println(gson.toJson(notification));
		return new Gson().toJson(notification);
	}
	
	//群組討論區通知，取得要被通知的人的ID，存在ArrayList
	//如A提問，B回答，C回答，A回答，D回答(通知B、C，因為A已經由另一個情況通知(通知提問者)，所以這裡不把A加入ArrayList中)
	public ArrayList<String> getGroupForumToUser(String userId, int commentId) {
		ArrayList<String> toUserIdList = new ArrayList<String>();
		String toUserId = null;
		try {
			pst = con.prepareStatement("select userId from groupComment where commentId = ?");
			pst.setInt(1, commentId);
			result = pst.executeQuery();
			if(result.next()) {
				toUserId = result.getString("userId");
			}
			pst = con.prepareStatement("select userId from groupReply where commentId = ?");
			pst.setInt(1, commentId);
			result = pst.executeQuery();
			while(result.next()) {
				String tempUserId = result.getString("userId");
				//判斷toUserIdList裡面是否已經有該userId，有的話就不加入，避免重複通知
				if((!toUserId.equals(tempUserId)) && (!toUserIdList.contains(tempUserId)) && (!userId.equals(tempUserId))) {
					toUserIdList.add(tempUserId);
				}
			}
		}
		catch(SQLException x){
			System.out.println("NotificationManager-getGroupForumToUser");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return toUserIdList;
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
