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
	private final String selectCourseListSQL = "select * from courselist where list_name like ?";
	private final String selectUnitKeywordSQL = "select * from unit natural join unit_keyword where unit_keyword like ? ";
//	private final String selectCourseKeywordSQL = "select * from courselist natural join course_keyword where course_keyword like ? or list_name like ? group by courselist_id";
	private final String selectCourseKeywordSQL = "select max(courselist_id)courselist_id, max(school_name)school_name, max(list_name)list_name, max(teacher)teacher, max(department_name)department_name, max(course_info)course_info, max(creator)creator, max(share)share, max(likes)likes from courselist natural join course_keyword where course_keyword like ? or list_name like ? group by list_name ORDER BY `courselist_id` ASC";
	private final String selectUnitByCourseIdSQL = "select * from unit natural join customlist_video where courselist_id = ?";
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
	
	public ArrayList<String> getUnitByKeyword(String keyword) {

		ArrayList<String> outputList = new ArrayList<>(); 
		try {

			pst = con.prepareStatement(selectUnitKeywordSQL);
			pst.setString(1,  "%" + keyword + "%" );
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
	
	public ArrayList<Search> getCourseListByKeyword(String keyword) {

		ArrayList<Search> outputList = new ArrayList<Search>(); 
		try {

			pst = con.prepareStatement(selectCourseKeywordSQL);
			pst.setString(1,  "%" + keyword + "%" );
			pst.setString(2,  "%" + keyword + "%" );
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 Search output = new Search();
				 output.setCourselistId(result.getInt("courselist_id"));
				 output.setSchoolName(result.getString("school_name"));
				 output.setListName(result.getString("list_name"));
				 output.setTeacher(result.getString("teacher"));
				 output.setDepartmentName(result.getString("department_name"));
				 output.setCourseInfo(result.getString("course_info"));
				 output.setCreator(result.getString("creator"));
				 output.setShare(result.getInt("share"));
				 output.setLikes(result.getInt("likes"));
				 outputList.add(output);
		     }
			pst = con.prepareStatement(selectUnitByCourseIdSQL);
			for (int i = 0; i < outputList.size(); i++)
			{
				pst.setInt(1,outputList.get(i).getCourselistId());
				result = pst.executeQuery();
				while (result.next())
				{
					Unit unit = new Unit();
					unit.setUnitId(result.getInt("unit_id"));
					unit.setUnitName(result.getString("unit_name"));
					unit.setVideoUrl(result.getString("video_url"));
					unit.setLikes(result.getInt("likes"));
					unit.setVideoImgSrc(result.getString("video_img_src"));
					outputList.get(i).addUnit(unit);
				}
			}
			pst = con.prepareStatement(selectUnitKeywordSQL);
			pst.setString(1, "%" + keyword + "%" );
			result = pst.executeQuery();
			while (result.next())
			{
				Search search = new Search();
				Unit unit = new Unit();
				unit.setUnitId(result.getInt("unit_id"));
				unit.setUnitName(result.getString("unit_name"));
				unit.setVideoUrl(result.getString("video_url"));
				unit.setLikes(result.getInt("likes"));
				unit.setSchoolName(result.getString("school_name"));
				unit.setVideoImgSrc(result.getString("video_img_src"));
				search.addUnit(unit);
				outputList.add(search);;
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
	
//	public ArrayList<String> getAllKeyword(String name) {

//		ArrayList<String> outputList = selectCourseKeywordTable(name);
//		outputList.addAll(selectUnitKeywordTable(name));
//		 return outputList;
//	}
	
	
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
		System.out.println(kldm.getCourseListByKeyword("微積分"));
//		System.out.println(kldm.getAllKeyword("微積分"));
	}
}
