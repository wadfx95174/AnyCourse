package PlayerInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import com.google.gson.Gson;

public class PlayerInterfaceManager
{
	private String selectVideoUrlSQL = "select * from unit natural join courselist where unit_id = ?";
	private String selectCourseListSQL = "select * from unit natural join customlist_video where courselist_id = ?";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public PlayerInterfaceManager() {
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
	public String getVideoUrl(int unitId) {
		Unit unit = new Unit();
		try {
			pst = con.prepareStatement(selectVideoUrlSQL);
			pst.setInt(1, unitId);
			result = pst.executeQuery();
			while(result.next()) 
			{ 	
				unit.setUnitId(result.getInt("unit_id"));
				unit.setUnitName(result.getString("unit_name"));
				unit.setListName(result.getString("list_name"));
				unit.setSchoolName(result.getString("school_name"));
				unit.setLikes(result.getInt("likes"));
				unit.setVideoImgSrc(result.getString("video_img_src"));
				unit.setVideoUrl(result.getString("video_url"));
				unit.setCourseInfo(result.getString("course_info"));
			}
		}
			catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		Gson gson = new Gson();
		return gson.toJson(unit);
	}
	//取得完整清單中的單元影片
	public String getList(int courselistId) {
		ArrayList<Unit> units = new ArrayList<>(); 
		try {
			pst = con.prepareStatement(selectCourseListSQL);
			pst.setInt(1, courselistId);
			result = pst.executeQuery();
			while(result.next()) 
			{ 	
				Unit unit = new Unit();
				unit.setUnitId(result.getInt("unit_id"));
				unit.setUnitName(result.getString("unit_name"));
				unit.setListName(result.getString("list_name"));
				unit.setSchoolName(result.getString("school_name"));
				unit.setLikes(result.getInt("likes"));
				unit.setVideoImgSrc(result.getString("video_img_src"));
				unit.setVideoUrl(result.getString("video_url"));
				units.add(unit);
			}
		}
			catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		Gson gson = new Gson();
		return gson.toJson(units);
	}
	//設定影片的結束時間
	//watch_record的watch_time是存入資料時資料庫自動抓現在的時間
	//如果該影片有存在在使用者的課程計畫中，則personal_plan的last_time也要update
	public void setVideoEndTime(int currentTime,int unit_id,String user_id) {
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from watch_record where user_id = "+user_id
					+" and unit_id = "+unit_id);
			boolean check = false;//檢查watch_record裡面有沒有這筆單元影片的資料，false為沒有
//			System.out.println("currentTime: " + currentTime);
//			System.out.println("unit_id: " + unit_id);
//			System.out.println("user_id: " + user_id);
			while(result.next()) {check = true;}
			//如果沒有，就塞資料
			if(check == false) {
				pst = con.prepareStatement("insert ignore into watch_record (user_id,unit_id,watch_time) value(?,?,null)");
				pst.setString(1,user_id);
				pst.setInt(2,unit_id);
			}
			//沒有
			else {
				pst = con.prepareStatement("update watch_record set watch_time = null where user_id = ? and unit_id = ? ");
				pst.setString(1,user_id);
				pst.setInt(2,unit_id);
			}
			pst.executeUpdate();
			
			//檢查有沒有存在在使用者的課程計畫中
			stat = con.createStatement();
			result = stat.executeQuery("select unit_id from personal_plan where user_id = "+user_id
					+" and unit_id = "+unit_id);
			check = false;//檢查有沒有在課程計畫的table中找到這個unit_id
			while(result.next()) {check = true;}
			//如果有，就更新影片結束時間
			if(check == true) {
				pst = con.prepareStatement("update personal_plan set last_time = ? where user_id = ? and unit_id = ? ");
				pst.setInt(1,currentTime);
				pst.setString(2,user_id);
				pst.setInt(3, unit_id);
				pst.executeUpdate();
			}
			
		}
		catch(SQLException x){
		System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void Close() {
		try {
			if(result!=null) {
				result.close();
				result = null;
			}
			if(stat!=null) {
				stat.close();
				stat = null;
			}
			if(pst!=null) {
				pst.close();
				pst = null;
			}
		}
		catch(SQLException e) {
			System.out.println("Close Exception :" + e.toString()); 
		}		
	} 
}
