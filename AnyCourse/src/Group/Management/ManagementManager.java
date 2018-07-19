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
import java.util.Set;


public class ManagementManager
{
	private final String selectGroupsSQL = "select * from ggroup natural join groupMember where userId = ?";
	private final String insertGroupSQL = "insert into ggroup value (null,?)";
	private final String updateEventSQL = "update event set title = ?,url = ?,start = ?,end = ?,allDay = ? where eventId = ?";
	private final String deleteEventSQL = "delete from event where eventId = ?";
	private final String deleteCalendarSQL = "delete from calendar where eventId = ?";
	private final String insertMemberSQL = "insert into groupMember value (?,?,?)";
	private final String selectGoogleCalendarIdSQL = "select * from googleCalendarMatch where userId = ?";
	private final String insertGoogleCalendarIdSQL = "insert into googleCalendarMatch value (?,?)";
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
	
	public int insertGroup(String userId, String groupName)
	{
		try
		{
			pst = con.prepareStatement(insertGroupSQL, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, groupName);
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
//	
//	public void updateEvent(CalendarDTO dto)
//	{
//		try
//		{
//			pst = con.prepareStatement(updateEventSQL);
//			pst.setString(1, dto.getTitle());
//			pst.setString(2,dto.getUrl());
//			pst.setString(3, dto.getStart());
//			pst.setString(4, dto.getEnd());
//			pst.setBoolean(5, dto.isAllDay());
//			pst.setInt(6, dto.getId());
//			pst.executeUpdate();
//		} catch (final SQLException x)
//		{
//			System.out.println("CalendarManager-updateEvent");
//			System.out.println("Exception update" + x.toString());
//		} finally
//		{
//			Close();
//		}
//	}
//	
//	public void deleteEvent(int eventId)
//	{
//		try
//		{
//			pst = con.prepareStatement(deleteEventSQL);
//			pst.setInt(1,eventId);
//			pst.executeUpdate();
//			pst = con.prepareStatement(deleteCalendarSQL);
//			pst.setInt(1, eventId);
//			pst.executeUpdate();
//		} catch (final SQLException x)
//		{
//			System.out.println("CalendarManager-deleteEvent");
//			System.out.println("Exception delete" + x.toString());
//		} finally
//		{
//			Close();
//		}
//	}
//	
//	public String getGoogleCalendarId(String userId)
//	{
//		String gcId = null;
//		try
//		{
//			pst = con.prepareStatement(selectGoogleCalendarIdSQL);
//			pst.setString(1, userId);
//			result = pst.executeQuery();
//			while(result.next())
//			{
//				gcId = result.getString("googleCalendarId");
//			}
//		} catch (final SQLException x)
//		{
//			System.out.println("CalendarManager-getGoogleCalendarId");
//			System.out.println("Exception select" + x.toString());
//		} finally
//		{
//			Close();
//		}
//		return gcId;
//	}
//	
//	public void setGoogleCalendarId(String userId, String gcId)
//	{
//		try
//		{
//			pst = con.prepareStatement(insertGoogleCalendarIdSQL);
//			pst.setString(1, userId);
//			pst.setString(2, gcId);
//			pst.executeUpdate();
//		} catch (final SQLException x)
//		{
//			System.out.println("CalendarManager-setGoogleCalendarId");
//			System.out.println("Exception insert" + x.toString());
//		} finally
//		{
//			Close();
//		}
//	}
	
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
