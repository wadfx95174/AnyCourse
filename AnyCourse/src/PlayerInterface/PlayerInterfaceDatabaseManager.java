package PlayerInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;

public class PlayerInterfaceDatabaseManager
{
	private String selectVideoUrlSQL = "select * from unit where unit_id = ?";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public PlayerInterfaceDatabaseManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");//註冊Driver
			con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?useUnicode=true&characterEncoding=Big5"
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
