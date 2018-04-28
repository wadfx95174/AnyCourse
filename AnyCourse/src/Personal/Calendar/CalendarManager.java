package Personal.Calendar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;


public class CalendarManager
{
	private final String selectEventsSQL = "select * from event ";
	private final String insertEventSQL = "insert into event value (null,1,?,?,?,?,?,?)";
	private final String updateEventSQL = "update event set title = ?,url = ?,start = ?,end = ? where event_id = ?";
	
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
	
	public String getEvents()
	{
		List<CalendarDTO> list = new ArrayList<CalendarDTO>();
		try
		{
			stat = con.createStatement();
			result = stat.executeQuery(selectEventsSQL);
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
				list.add(dto);
			}
		} catch (final SQLException x)
		{
			System.out.println("Exception select" + x.toString());
		} finally
		{
			Close();
		}

		String json = new Gson().toJson(list);
		return json;
	}
	
	public int insertEvent(CalendarDTO dto)
	{
		try
		{
			pst = con.prepareStatement(insertEventSQL, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, dto.getTitle());
			pst.setString(2,dto.getUrl());
			pst.setString(3, dto.getStart());
			pst.setString(4, dto.getEnd());
			pst.setString(5, dto.getBackgroundColor());
			pst.setString(6, dto.getBorderColor());
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if (generatedKeys.next())
				return generatedKeys.getInt(1);
			
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
			pst.setInt(5, dto.getId());
			pst.executeUpdate();
		} catch (final SQLException x)
		{
			System.out.println("Exception update" + x.toString());
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
		System.out.println(manager.getEvents());
	}
}
