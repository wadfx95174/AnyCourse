package Personal.Calendar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class CalendarManager
{
	private final String selectEventsSQL = "select * from event natural join calendar where user_id = ?";
	private final String insertEventSQL = "insert into event value (null,1,?,?,?,?,?,?,?)";
	private final String updateEventSQL = "update event set title = ?,url = ?,start = ?,end = ?,all_day = ? where event_id = ?";
	private final String deleteEventSQL = "delete from event where event_id = ?";
	private final String deleteCalendarSQL = "delete from calendar where event_id = ?";
	private final String insertCalendarSQL = "insert into calendar value (?,?)";
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
	
	public ArrayList<CalendarDTO> getEvents(String userId)
	{
		ArrayList<CalendarDTO> list = new ArrayList<CalendarDTO>();
		try
		{
			pst = con.prepareStatement(selectEventsSQL);
			pst.setString(1, userId);
			result = pst.executeQuery();
			while (result.next())
			{
				CalendarDTO dto = new CalendarDTO();
				dto.setId(result.getInt("event_id"));
				dto.setTitle(result.getString("title"));
				dto.setUrl(result.getString("url"));
				dto.setStart(result.getString("start"));
				dto.setEnd(result.getString("end"));
				dto.setBackgroundColor(result.getString("background_color"));
				dto.setBorderColor(result.getString("border_color"));
				dto.setAllDay(result.getBoolean("all_day"));
				list.add(dto);
			}
		} catch (final SQLException x)
		{
			System.out.println("Exception select" + x.toString());
		} finally
		{
			Close();
		}

		return list;
	}
	
	public int insertEvent(CalendarDTO dto, String userId)
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
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if (generatedKeys.next())
			{
				int newEventId = generatedKeys.getInt(1);
				pst = con.prepareStatement(insertCalendarSQL);
				pst.setString(1, userId);
				pst.setInt(2, newEventId);
				pst.executeUpdate();
				return newEventId;
			}
		} catch (final SQLException x)
		{
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
			System.out.println("Exception delete" + x.toString());
		} finally
		{
			Close();
		}
	}
	
	private void Close()
	{
		try
		{
			if (result != null)
			{
				result.close();
				result = null;
			}
			if (stat != null)
			{
				stat.close();
				stat = null;
			}
			if (pst != null)
			{
				pst.close();
				pst = null;
			}
		} catch (final SQLException e)
		{
			System.out.println("Close Exception :" + e.toString());
		}

	}
	
	public static void main(String []args)
	{
		CalendarManager manager = new CalendarManager();
		System.out.println(manager.getEvents("1"));
	}
}
