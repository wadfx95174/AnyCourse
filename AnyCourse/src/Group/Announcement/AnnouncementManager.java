package Group.Announcement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

public class AnnouncementManager {
	private final String selectAnnouncementSQL = "select * from announcement natural join account where groupId = ?";
	private final String insertAnnouncementSQL = "insert into announcement value (?,?,null,?,?)";
	private final String updateAnnouncementSQL = "update announcement set title = ?, content = ? where groupId = ? and userId = ? ";
	private final String deleteAnnouncementSQL = "delete from announcement where groupId = ? and userId = ? ";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public AnnouncementManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");//註冊Driver
			con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=Big5"
					, "root", "peter");//取得connection
			
		}
		catch(ClassNotFoundException e){
			System.out.println("DriverClassNotFound"+e.toString());
		}
		catch(SQLException x){
			System.out.println("Exception" + x.toString());
		}
	}
	
	public String getAllAnnouncement(int groupId, String userId) {
		ArrayList<Announcement> outputList = new ArrayList<>(); 
		try {
			pst = con.prepareStatement(selectAnnouncementSQL);
			pst.setInt(1, groupId);
			result = pst.executeQuery();
			while(result.next()) 
			{ 	
				Announcement announcement = new Announcement();
				announcement.setUserId(result.getString("userId"));
				announcement.setNickName(result.getString("nickName"));
				announcement.setGroupId(result.getInt("groupId"));
				announcement.setTime(result.getString("time"));
				announcement.setTitle(result.getString("title"));
				announcement.setContent(result.getString("content"));
				announcement.setSameUser(announcement.getUserId().equals(userId) ? true : false);
				outputList.add(announcement);
			}
		}
		catch(SQLException x){
			System.out.println("AnnouncementManager-getAllAnnouncement");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(outputList);
		return json;
	}
	
	public void insertAnnouncement(int groupId, String userId, String title, String content)
	{
		try {
			pst = con.prepareStatement(insertAnnouncementSQL);
			pst.setInt(1, groupId);
			pst.setString(2, userId);
			pst.setString(3, title);
			pst.setString(4, content);
			pst.executeUpdate();
		} catch(SQLException x) {
			System.out.println("AnnouncementManager-insertAnnouncement");
			System.out.println("Exception insert"+x.toString());
		} finally {
			Close();
		}
	}
	
	public void updateAnnouncement(int groupId, String userId, String title, String content)
	{
		try {
			pst = con.prepareStatement(updateAnnouncementSQL);
			pst.setString(1, title);
			pst.setString(2, content);
			pst.setInt(3, groupId);
			pst.setString(4, userId);
			pst.executeUpdate();
		} catch(SQLException x) {
			System.out.println("AnnouncementManager-updateAnnouncement");
			System.out.println("Exception update"+x.toString());
		} finally {
			Close();
		}
	}
	
	public void deleteAnnouncement(int groupId, String userId)
	{
		try {
			pst = con.prepareStatement(deleteAnnouncementSQL);
			pst.setInt(1, groupId);
			pst.setString(2, userId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("AnnouncementManager-deleteAnnouncement");
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
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
			System.out.println("AnnouncementManager Close Exception :" + e.toString()); 
		}		
	} 
}
