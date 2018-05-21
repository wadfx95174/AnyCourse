package Personal.SearchRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SearchRecordManager {
	private String deleteSearchRecordSQL = "delete from searchRecord where userId = ? and searchWord = ? and searchTime = ?";
	private SearchRecord searchRecord;
	private ArrayList<SearchRecord> searchRecords;
	
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public SearchRecordManager() {
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
	//選取要呈現的搜尋紀錄
	public ArrayList<SearchRecord> selectSearchRecordTable(String userId) {
		searchRecords = new ArrayList<SearchRecord>();
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from searchRecord where userId = '" + userId + "'");
			 while(result.next()) 
		     { 	
				 searchRecord = new SearchRecord();
				 searchRecord.setSearchWord(result.getString("searchWord"));
				 searchRecord.setSearchTime(result.getString("searchTime"));
				 searchRecords.add(searchRecord);
		     }
		}catch(SQLException x){
			System.out.println("SearchRecord-selectSearchRecordTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return searchRecords;
	}
	//刪除指定搜尋資料
	public void deleteSearchRecordTable(String userId,String searchWord,String searchTime) {
		try {
			pst = con.prepareStatement(deleteSearchRecordSQL);
			pst.setString(1,userId);
			pst.setString(2,searchWord);
			pst.setString(3,searchTime);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("SearchRecord-deleteSearchRecordTable");
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
			}
			if(stat!=null) {
				stat.close();
			}
			if(pst!=null) {
				pst.close();
			}
		}
		catch(SQLException e) {
			System.out.println("SearchRecord Close Error");
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
			System.out.println("SearchRecord Connection Close Error");
			System.out.println("Close Exception :" + e.toString()); 
		}
	}
}
