package Keyword;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import anycourse.Course;

public class KeywordManager
{
	private String selectCourseListSQL = "select listName , courseInfo from courselist";
	private String selectUnitListSQL = "select unitName from unit";
	private String insertUnitKeywordSQL = "insert into unitKeyword value (?,?,?)";
	private String insertCourseKeywordSQL = "insert into courseKeyword value (?,?,?)";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public KeywordManager() {
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
	
	public ArrayList<Course> getCourse()
	{
		ArrayList<Course> courses = new ArrayList<>();
		
		try {
			pst = con.prepareStatement(selectCourseListSQL);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 Course course = new Course();
				 course.setCourseName(result.getString("listName"));
				 String info = result.getString("courseInfo");
				 course.setCourseInfo(info != null ? info.replaceAll("\r|\n|\\*|。|\\(|「|、|\\+|\\?|\\)|,|《|〕|", "") : null);
				 courses.add(course);
		     }
		}
			 catch(SQLException x){
			System.out.println("Exception getCourse "+x.toString());
		}
		finally {
			Close();
		}
		
		return courses;
	}
	
	public ArrayList<String> getUnit()
	{
		ArrayList<String> units = new ArrayList<>();
		
		try {

			pst = con.prepareStatement(selectUnitListSQL);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 units.add(result.getString("unitName"));
		     }
		}
			 catch(SQLException x){
			System.out.println("Exception getUnit "+x.toString());
		}
		finally {
			Close();
		}
		
		return units;
	}
	
	public void insertUnitKeyword(List<Entry<String, Double>> list, int unitId)
	{
		for (Entry<String, Double> keyword: list)
		{
			try {
				pst = con.prepareStatement(insertUnitKeywordSQL);
				pst.setInt(1, unitId);
				pst.setString(2, keyword.getKey());
				pst.setDouble(3, keyword.getValue());
				pst.executeUpdate();
			}catch(SQLException x){
				 System.out.println("Exception insertUnitKeyword "+x.toString());
			}finally {
				Close();
			}
		}
	}
	
	public void insertCourseKeyword(List<Entry<String, Double>> list, int courseId)
	{
//		System.out.println("insert");
//		System.out.println(list.toString());
		for (Entry<String, Double> keyword: list)
		{
			try {
				pst = con.prepareStatement(insertCourseKeywordSQL);
				pst.setInt(1, courseId);
				pst.setString(2, keyword.getKey());
				pst.setDouble(3, keyword.getValue());
				pst.executeUpdate();
			}catch(SQLException x){
				 System.out.println("Exception insertCourseKeyword "+x.toString());
			}finally {
				Close();
			}
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
