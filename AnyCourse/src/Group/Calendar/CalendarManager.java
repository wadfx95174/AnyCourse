package Group.Calendar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class CalendarManager
{
	private final String selectEventsSQL = "select * from event natural join groupCalendar natural join account where groupId = ?";
	private final String insertEventSQL = "insert into event value (null,1,?,?,?,?,?,?,?,?)";
	private final String updateEventSQL = "update event set title = ?,url = ?,start = ?,end = ?,allDay = ? where eventId = ?";
	private final String deleteEventSQL = "delete from event where eventId = ?";
	private final String deleteCalendarSQL = "delete from groupCalendar where eventId = ?";
	private final String insertCalendarSQL = "insert into groupCalendar value (?,?,?)";
	private final String selectGoogleCalendarIdSQL = "select * from groupGoogleCalendar where groupId = ?";
	private final String insertGoogleCalendarIdSQL = "insert into groupGoogleCalendar value (?,?)";
	private final String insertGoogleEventIdSQL = "update event set googleEventId = ? where eventId = ?";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;

	public CalendarManager()
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
	
	// 回傳群組 Events 陣列 (含該事件是否為已登入之使用者)
	public ArrayList<CalendarDTO> getEventsWithCheckingUser(int groupId, String userId)
	{
		ArrayList<CalendarDTO> result = getEvents(groupId);
		CalendarDTO eachEvent;
		for (int i = 0; i < result.size(); i++)
		{
			eachEvent = result.get(i);
			// 如果為同個使用者 -> sameUser=true
			eachEvent.setSameUser(eachEvent.getUserId().equals(userId));
		}
		return result;
	}
	
	// 回傳群組 Events 陣列
	public ArrayList<CalendarDTO> getEvents(int groupId)
	{
		ArrayList<CalendarDTO> list = new ArrayList<CalendarDTO>();
		try
		{
			pst = con.prepareStatement(selectEventsSQL);
			pst.setInt(1, groupId);
			result = pst.executeQuery();
			while (result.next())
			{
				CalendarDTO dto = new CalendarDTO();
				dto.setId(result.getInt("eventId"));
				dto.setUserId(result.getString("userId"));
				dto.setNickName(result.getString("nickName"));
				dto.setTitle(result.getString("title"));
				dto.setUrl(result.getString("url"));
				dto.setStart(result.getString("start"));
				dto.setEnd(result.getString("end"));
				dto.setBackgroundColor(result.getString("backgroundColor"));
				dto.setBorderColor(result.getString("borderColor"));
				dto.setAllDay(result.getBoolean("allDay"));
				dto.setGoogleEventId(result.getString("googleEventId"));
				list.add(dto);
			}
		} catch (final SQLException x)
		{
			System.out.println("CalendarManager-getEvents");
			System.out.println("Exception select" + x.toString());
		} finally
		{
			Close();
		}

		return list;
	}
	
	public int insertEvent(CalendarDTO dto, int groupId)
	{
		try
		{
			pst = con.prepareStatement(insertEventSQL, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, dto.getTitle());
			pst.setString(2, dto.getUrl());
			pst.setString(3, dto.getStart());
			pst.setString(4, dto.getEnd());
			pst.setString(5, dto.getBackgroundColor());
			pst.setString(6, dto.getBorderColor());
			pst.setBoolean(7, dto.isAllDay());
			pst.setString(8, null);
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if (generatedKeys.next())
			{
				int newEventId = generatedKeys.getInt(1);
				pst = con.prepareStatement(insertCalendarSQL);
				pst.setInt(1, groupId);
				pst.setInt(2, newEventId);
				pst.setString(3, dto.getUserId());
				pst.executeUpdate();
				return newEventId;
			}
		} catch (final SQLException x)
		{
			System.out.println("CalendarManager-insertEvent");
			System.out.println("Exception insert" + x.toString());
		} finally
		{
			Close();
		}
		return 0;
	}
	
	public void updateEvent(CalendarDTO dto)
	{
		try
		{
			pst = con.prepareStatement(updateEventSQL);
			pst.setString(1, dto.getTitle());
			pst.setString(2,dto.getUrl());
			pst.setString(3, dto.getStart());
			pst.setString(4, dto.getEnd());
			pst.setBoolean(5, dto.isAllDay());
			pst.setInt(6, dto.getId());
			pst.executeUpdate();
		} catch (final SQLException x)
		{
			System.out.println("CalendarManager-updateEvent");
			System.out.println("Exception update" + x.toString());
		} finally
		{
			Close();
		}
	}
	
	public void deleteEvent(int eventId)
	{
		try
		{
			pst = con.prepareStatement(deleteEventSQL);
			pst.setInt(1,eventId);
			pst.executeUpdate();
			pst = con.prepareStatement(deleteCalendarSQL);
			pst.setInt(1, eventId);
			pst.executeUpdate();
		} catch (final SQLException x)
		{
			System.out.println("CalendarManager-deleteEvent");
			System.out.println("Exception delete" + x.toString());
		} finally
		{
			Close();
		}
	}
	
	public void setGoogleEventId(String eventId, String googleEventId)
	{
		try
		{
			pst = con.prepareStatement(insertGoogleEventIdSQL);
			pst.setString(1, googleEventId);
			pst.setString(2, eventId);
			pst.executeUpdate();
		} catch (final SQLException x)
		{
			System.out.println("CalendarManager-setGoogleEventId");
			System.out.println("Exception insert" + x.toString());
		} finally
		{
			Close();
		}
	}
	
	public String getGoogleCalendarId(int groupId)
	{
		String gcId = null;
		try
		{
			pst = con.prepareStatement(selectGoogleCalendarIdSQL);
			pst.setInt(1, groupId);
			result = pst.executeQuery();
			while(result.next())
			{
				gcId = result.getString("googleCalendarId");
			}
		} catch (final SQLException x)
		{
			System.out.println("CalendarManager-getGoogleCalendarId");
			System.out.println("Exception select" + x.toString());
		} finally
		{
			Close();
		}
		return gcId;
	}
	
	public void setGoogleCalendarId(int groupId, String gcId)
	{
		try
		{
			pst = con.prepareStatement(insertGoogleCalendarIdSQL);
			pst.setInt(1, groupId);
			pst.setString(2, gcId);
			pst.executeUpdate();
		} catch (final SQLException x)
		{
			System.out.println("CalendarManager-setGoogleCalendarId");
			System.out.println("Exception insert" + x.toString());
		} finally
		{
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
	
	public static void main(String []args)
	{
		CalendarManager manager = new CalendarManager();
		System.out.println(manager.getEvents(1));
	}
}

