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
	public String selectWatchRecordSQL = "select * from watch_record natural join unit where watch_record.user_id = ? and watch_record.unit_id = unit.unit_id ";
	public String deleteWatchRecordSQL = "delete from watch_record where user_id = ? and unit_id = ?";
	public WatchRecord watchRecord;
	
	public Connection con = null;
	public Statement stat = null;
	public ResultSet result = null;
	public PreparedStatement pst = null;
	
	public WatchRecordManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");//閮餃�river

			con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=Big5"
					, "root", "peter");//���onnection
			
		}
		catch(ClassNotFoundException e){
			System.out.println("DriverClassNotFound"+e.toString());
		}
		catch(SQLException x){
			System.out.println("Exception" + x.toString());
		}
	}
	//�������������
	public String selectWatchRecordTable(String user_id) {
		ArrayList<WatchRecord> watchRecords = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectWatchRecordSQL);
			pst.setString(1, user_id);
			result = pst.executeQuery();
//			stat = con.createStatement();
//			result = stat.executeQuery(selectWatchRecordSQL);
			 while(result.next()) 
		     { 	
				 watchRecord = new WatchRecord();
				 watchRecord.setUser_id(result.getString("user_id"));
				 watchRecord.setUnit_id(result.getInt("unit_id"));
				 watchRecord.setWatch_time(result.getString("watch_time"));
				 watchRecord.setSchool_name(result.getString("school_name"));
				 watchRecord.setUnit_name(result.getString("unit_name"));
				 watchRecord.setVideo_url(result.getString("video_url"));
				 watchRecord.setLikes(result.getInt("likes"));
				 watchRecord.setVideo_img_src(result.getString("video_img_src"));
				 watchRecords.add(watchRecord);
		     }
		}
			 catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(watchRecords);
		return json;
	}
	//����������
	public void deleteWatchRecordTable(String user_id,int unit_id) {
		try {
			pst = con.prepareStatement(deleteWatchRecordSQL);
			pst.setString(1,user_id);
			pst.setInt(2,unit_id);
			pst.executeUpdate();
		}
		catch(SQLException x){
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
			System.out.println("Close Exception :" + e.toString()); 
		}		
	} 
}
