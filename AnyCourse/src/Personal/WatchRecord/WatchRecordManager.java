package Personal.WatchRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;


public class WatchRecordManager {
	public String selectWatchRecordSQL = "select * from watchRecord natural join unit where watchRecord.userId = ? and watchRecord.unitId = unit.unitId ";
	public String deleteWatchRecordSQL = "delete from watchRecord where userId = ? and unitId = ?";
	public WatchRecord watchRecord;
	
	public Connection con = null;
	public Statement stat = null;
	public ResultSet result = null;
	public PreparedStatement pst = null;
	
	public WatchRecordManager() {
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
	public String selectWatchRecordTable(String userId) {
		ArrayList<WatchRecord> watchRecords = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectWatchRecordSQL);
			pst.setString(1, userId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 watchRecord = new WatchRecord();
				 watchRecord.setUserId(result.getString("userId"));
				 watchRecord.setUnitId(result.getInt("unitId"));
				 watchRecord.setWatchTime(result.getString("watchTime"));
				 watchRecord.setSchoolName(result.getString("schoolName"));
				 watchRecord.setUnitName(result.getString("unitName"));
				 watchRecord.setVideoUrl(result.getString("videoUrl"));
				 watchRecord.setLikes(result.getInt("likes"));
				 watchRecord.setVideoImgSrc(result.getString("videoImgSrc"));
				 watchRecords.add(watchRecord);
		     }
		}
		catch(SQLException x){
			System.out.println("WatchRecordManager-selectWatchRecordTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(watchRecords);
		return json;
	}
	public void deleteWatchRecordTable(String userId,int unitId) {
		try {
			pst = con.prepareStatement(deleteWatchRecordSQL);
			pst.setString(1,userId);
			pst.setInt(2,unitId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("WatchRecordManager-deleteWatchRecordTable");
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
			System.out.println("WatchRecordManager Close Exception :" + e.toString()); 
		}		
	} 
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("WatchRecordManager Close Exception :" + e.toString()); 
		}
	}
}
