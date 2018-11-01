package DataHandle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateLectureTable {
	
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public UpdateLectureTable() {
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
	//更新unitLecture的unitId
	public void updateLectureUnitId() {
		try {
			pst = con.prepareStatement("update unitLecture set unitId = ? where listName = ? and unitName = ? ");
			stat = con.createStatement();
			result = stat.executeQuery("select unit.unitId,unit.listName,unit.unitName "
					+ "from unit,unitLecture where unit.listName = unitLecture.listName "
					+ "and unit.unitName = unitLecture.unitName");
			while(result.next()) {
				pst.setInt(1,Integer.parseInt(result.getString("unitId")));
				pst.setString(2, result.getString("unit.listName"));
				pst.setString(3, result.getString("unit.unitName"));
				pst.executeUpdate();
			}
		}
		catch(SQLException x){
			System.out.println("Exception insert"+x.toString());
		}
		finally {
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
			System.out.println("Close Exception :" + e.toString()); 
		}		
	}
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("Close Exception :" + e.toString()); 
		}
	}
	public static void main(String[] args) {
		UpdateLectureTable d = new UpdateLectureTable();
		d.updateLectureUnitId();
	}
}
