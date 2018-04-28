package DataHandle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

	private String insertCustomListVideoSQL = "insert into customlist_video (courselist_id,unit_id,oorder) value(?,?,?)";
	private String selectUnit_CourseListSQL = "select * from courselist,unit where courselist.list_name = unit.list_name and courselist.school_name = unit.school_name";
	
	
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
	//運用courselist及unit兩個table生成customlist_video
	public void insertCustomlist_videoTable() {
		try {
			int order = 1;
			String temp = null;
			pst = con.prepareStatement(insertCustomListVideoSQL);
			stat = con.createStatement();
			result = stat.executeQuery(selectUnit_CourseListSQL);
			while(result.next()) {
				pst.setInt(1,Integer.parseInt(result.getString("courselist_id")));
				pst.setInt(2,Integer.parseInt(result.getString("unit_id")));
				
				//檢查這一次迴圈是否已經執行到下一個課程了，等於temp的話，代表還在同一個課程
//				System.out.println(temp);
//				System.out.println(result.getString("courselist.list_name"));
				if(result.getString("courselist.list_name").equals(temp)) {
					
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
				
				temp = result.getString("courselist.list_name");
//				System.out.println(temp);
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
	public static void main(String[] args) {
		DatabaseManager d = new DatabaseManager();
		d.insertCustomlist_videoTable();
	}

}
