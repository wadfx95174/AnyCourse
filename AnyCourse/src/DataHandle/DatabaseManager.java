package DataHandle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

	private String insertCustomListVideoSQL = "insert into customListVideo (courselistId,unitId,oorder) value(?,?,?)";
	private String selectUnitCourseListSQL = "select * from courselist,unit where courselist.listName "
			+ "= unit.listName and courselist.schoolName = unit.schoolName and courselist.teacher = "
			+ "unit.teacher";
	
	
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public DatabaseManager() {
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
	//運用courselist及unit兩個table生成customlistVideo
	public void insertCustomlistVideoTable() {
		try {
			int order = 1;
			String temp = null;
			pst = con.prepareStatement(insertCustomListVideoSQL);
			stat = con.createStatement();
			result = stat.executeQuery(selectUnitCourseListSQL);
			while(result.next()) {
				pst.setInt(1,Integer.parseInt(result.getString("courselistId")));
				pst.setInt(2,Integer.parseInt(result.getString("unitId")));
				
				//檢查這一次迴圈是否已經執行到下一個課程了，等於temp的話，代表還在同一個課程
				if(result.getString("courselist.listName").equals(temp)) {
					
					pst.setInt(3,order);
					System.out.println(order);
					order++;
					pst.executeUpdate();
				}
				else {
					order = 1;
					pst.setInt(3,order);
					order++;
					pst.executeUpdate();
					
				}
				temp = result.getString("courselist.listName");
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
		DatabaseManager d = new DatabaseManager();
		d.insertCustomlistVideoTable();
	}
}
