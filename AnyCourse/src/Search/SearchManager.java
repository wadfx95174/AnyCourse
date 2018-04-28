package Search;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SearchManager
{
	private String selectCourseListSQL = "select * from courselist where list_name like ? ";
	private String selectUnitKeywordSQL = "select * from unit natural join unit_keyword where unit_keyword like ? ";
	private String selectCourseKeywordSQL = "select * from courselist natural join course_keyword where course_keyword like ?";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public SearchManager() {
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
	public ArrayList<String> selectCourseListTable(String name) {

		ArrayList<String> outputList = new ArrayList<>(); 
		try {

			pst = con.prepareStatement(selectCourseListSQL);
			pst.setString(1,  "%" + name + "%" );
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 String output = result.getString("list_name");
				 outputList.add(output);
		     }
		}
			 catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		 return outputList;
	}
	
	public ArrayList<String> selectUnitKeywordTable(String name) {

		ArrayList<String> outputList = new ArrayList<>(); 
		try {

			pst = con.prepareStatement(selectUnitKeywordSQL);
			pst.setString(1,  "%" + name + "%" );
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 String output = result.getString(3);
				 outputList.add(output);
		     }
		}
			 catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		 return outputList;
	}
	
	public ArrayList<String> selectCourseKeywordTable(String name) {

		ArrayList<String> outputList = new ArrayList<>(); 
		try {

			pst = con.prepareStatement(selectCourseKeywordSQL);
			pst.setString(1,  "%" + name + "%" );
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 String output = result.getString(3);
				 outputList.add(output);
		     }
		}
			 catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		 return outputList;
	}
	
	public ArrayList<String> getAllKeyword(String name) {

		ArrayList<String> outputList = selectCourseKeywordTable(name);
		outputList.addAll(selectUnitKeywordTable(name));
		 return outputList;
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
	
	public static void main(String []args)
	{
		SearchManager kldm = new SearchManager();
		
//		System.out.println(kldm.selectCourseListTable("化學"));
//		System.out.println(kldm.selectUnitKeywordTable("微積分"));
		System.out.println(kldm.getAllKeyword("微積分"));
	}
}
