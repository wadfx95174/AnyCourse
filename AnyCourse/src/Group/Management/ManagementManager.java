package Group.Management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ManagementManager
{
	private final String selectGroupsSQL = "select * from ggroup natural join groupMember where userId = ?";
	private final String insertGroupSQL = "insert into ggroup value (null,?,?)";
	private final String insertMemberSQL = "insert into groupMember value (?,?,?)";
	private final String selectGroupMemberSQL = "select userId, groupId, groupName, identity, nickName from ggroup natural join groupMember natural join account where groupId = ?";
	private final String selectGroupCreatorSQL = "select * from ggroup where groupId = ?";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;

	public ManagementManager()
	{
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
	
	public Map<String, Integer> getPersonalGroups(String userId)
	{
		Map<String, Integer> groups = new HashMap<String, Integer>();
		ArrayList<GroupInfo> list = getGroups(userId);
		for(GroupInfo info: list)
		{
			groups.put(info.getGroupName(), info.getGroupId());
		}
		
		return groups;
	}
	
	public ArrayList<GroupInfo> getGroups(String userId)
	{
		ArrayList<GroupInfo> list = new ArrayList<GroupInfo>();
		try
		{
			pst = con.prepareStatement(selectGroupsSQL);
			pst.setString(1, userId);
			result = pst.executeQuery();
			while (result.next())
			{
				GroupInfo info = new GroupInfo();
				info.setGroupId(result.getInt("groupId"));
				info.setGroupName(result.getString("groupName"));
				list.add(info);
			}
		} catch (final SQLException x)
		{
			System.out.println("ManagementManager-getGroups");
			System.out.println("Exception select" + x.toString());
		} finally
		{
			Close();
		}

		return list;
	}
	
	public GroupInfo getGroupInfo(int groupId)
	{
		GroupInfo info = new GroupInfo();
		try
		{
			pst = con.prepareStatement(selectGroupMemberSQL);
			pst.setInt(1, groupId);
			result = pst.executeQuery();
			while (result.next())
			{
				info.setGroupId(result.getInt("groupId"));
				info.setGroupName(result.getString("groupName"));
				Member member = new Member();
				member.setUserId(result.getString("userId"));
				member.setIdentity(result.getBoolean("identity"));
				member.setUserName(result.getString("nickName"));
				if (member.isIdentity())
					info.addManager(member);
				else
					info.addMember(member);
			}
		} catch (final SQLException x)
		{
			System.out.println("ManagementManager-getGroupInfo");
			System.out.println("Exception select" + x.toString());
		} finally
		{
			Close();
		}
		return info;
	}
	
	public int insertGroup(String userId, String groupName)
	{
		try
		{
			pst = con.prepareStatement(insertGroupSQL, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, groupName);
			pst.setString(2, userId);
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if (generatedKeys.next())
			{
				int newGroupId = generatedKeys.getInt(1);
				pst = con.prepareStatement(insertMemberSQL);
				pst.setInt(1, newGroupId);
				pst.setString(2, userId);
				pst.setInt(3, 1);
				pst.executeUpdate();
				return newGroupId;
			}
		} catch (final SQLException x)
		{
			System.out.println("ManagementManager-insertGroup");
			System.out.println("Exception insert" + x.toString());
		} finally
		{
			Close();
		}
		return 0;
	}
	
	public String getGroupCreator(int groupId)
	{
		String creator = null;
		try
		{
			pst = con.prepareStatement(selectGroupCreatorSQL);
			pst.setInt(1, groupId);
			result = pst.executeQuery();
			while (result.next())
			{
				creator = result.getString("creator");
			}
		} catch (final SQLException x)
		{
			System.out.println("ManagementManager-getGroupCreator");
			System.out.println("Exception select" + x.toString());
		} finally
		{
			Close();
		}
		return creator;
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
