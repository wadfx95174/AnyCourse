package Group.Member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;

public class GroupMemberManager {
	
	private GroupMember groupMember;
	
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;

	public GroupMemberManager() {
		try
		{
			Class.forName("com.mysql.jdbc.Driver");// 註冊Driver
			con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=Big5"
					, "root", "peter");// 取得connection

		} catch (final ClassNotFoundException e)
		{
			System.out.println("DriverClassNotFound" + e.toString());
		} catch (final SQLException x)
		{
			System.out.println("Exception" + x.toString());
		}
	}
	
	//檢查要邀請的使用者是否存在、是否已經加入該群組
	//invalidUser:不存在資料庫
	//legalUser:存在資料庫，且未加入該群組
	//hasJoinedTheUser:存在資料庫，且已經加入該群組
	public String checkUserExist(String user,int groupId) {
		try
		{
			groupMember = new GroupMember();
			
			//有該使用者，輸入的字串可能為userId或nickName
			pst = con.prepareStatement("select userId from account where userId = ?");
			pst.setString(1,user);
			result = pst.executeQuery();
			if(result.next()) {
				groupMember.setToUserId(result.getString("userId"));
			}
			
			pst = con.prepareStatement("select userId from account where nickName = ?");
			pst.setString(1,user);
			result = pst.executeQuery();
			if(result.next()) {
				groupMember.setToUserId(result.getString("userId"));
			}
			
			//該使用者存在
			if(groupMember.getToUserId() != null) {
				pst = con.prepareStatement("select userId from groupMember where userId = ? and groupId = ?");
				pst.setString(1, groupMember.getToUserId());
				pst.setInt(2, groupId);
				result = pst.executeQuery();
				if(result.next()) {
					groupMember.setSituation("hasJoinedTheUser");
				}
				else {
					groupMember.setSituation("legalUser");
				}
			}
			//該使用者不存在
			else {
				groupMember = new GroupMember();
				groupMember.setSituation("invalidUser");
			}
		} 
		catch (final SQLException x)
		{
			System.out.println("GroupMemberManager-checkUserExist");
			System.out.println("Exception insert" + x.toString());
		} 
		finally
		{
			Close();
		}
		return new Gson().toJson(groupMember);
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
			System.out.println("CalendarManager Close Exception :" + e.toString()); 
		}		
	} 
	
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("CalendarManager Close Exception :" + e.toString()); 
		}
	}
}
