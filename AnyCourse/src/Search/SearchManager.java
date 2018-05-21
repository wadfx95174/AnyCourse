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
	private final String selectCourseListSQL = "select * from courselist where listName like ?";
	private final String selectUnitKeywordSQL = "select * from unit natural join unitKeyword where unitKeyword like ? ";
	private final String selectCourseKeywordSQL = "select max(courselistId)courselistId, max(schoolName)schoolName, max(listName)listName, max(teacher)teacher, max(departmentName)departmentName, max(courseInfo)courseInfo, max(creator)creator, max(share)share, max(likes)likes from courselist natural join courseKeyword where courseKeyword like ? or listName like ? or teacher like ? or schoolName like ? or departmentName like ? group by listName ORDER BY `courselistId` ASC";
	private final String selectUnitByCourseIdSQL = "select * from unit natural join customlistVideo where courselistId = ?";
	private final String selectTeacherSQL = "select distinct teacher from courselist where teacher is not null";
	private final String selectDepartmentSQL = "select distinct departmentName from courselist where departmentName is not null";
	private final String selectPlanMaxSQL = "select MAX(oorder) from personalPlan where status = 1 and userId = ?";
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
				 String output = result.getString("listName");
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
			if (keyword.length() == 2)
			{
				String firstWord = keyword.substring(0,1);
				String secondWord = keyword.substring(1);
				pst.setString(1,  "%" + firstWord + "%" + secondWord + "%");
				pst.setString(2,  "%" + firstWord + "%" + secondWord + "%");
				pst.setString(3,  "%" + firstWord + "%" + secondWord + "%");
				pst.setString(4,  "%" + firstWord + "%" + secondWord + "%");
				pst.setString(5,  "%" + firstWord + "%" + secondWord + "%");
			}
			else
			{
				pst.setString(1,  "%" + keyword + "%" );
				pst.setString(2,  "%" + keyword + "%" );
				pst.setString(3,  "%" + keyword + "%" );
				pst.setString(4,  "%" + keyword + "%" );
				pst.setString(5,  "%" + keyword + "%" );
			}
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 Search output = new Search();
				 output.setCourselistId(result.getInt("courselistId"));
				 output.setSchoolName(result.getString("schoolName"));
				 output.setListName(result.getString("listName"));
				 output.setTeacher(result.getString("teacher"));
				 output.setDepartmentName(result.getString("departmentName"));
				 output.setCourseInfo(result.getString("courseInfo"));
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
					unit.setUnitId(result.getInt("unitId"));
					unit.setUnitName(result.getString("unitName"));
					unit.setVideoUrl(result.getString("videoRrl"));
					unit.setLikes(result.getInt("likes"));
					unit.setVideoImgSrc(result.getString("videoImgSrc"));
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
				unit.setUnitId(result.getInt("unitId"));
				unit.setUnitName(result.getString("unitName"));
				unit.setVideoUrl(result.getString("videoRrl"));
				unit.setLikes(result.getInt("likes"));
				unit.setSchoolName(result.getString("schoolName"));
				unit.setVideoImgSrc(result.getString("videoImgSrc"));
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
	
	public ArrayList<String> getTeacher()
	{
		ArrayList<String> outputList = new ArrayList<>(); 
		try {

			pst = con.prepareStatement(selectTeacherSQL);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 String output = result.getString(1);
				 outputList.add(output);
		     }
		}
			 catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		conClose();
		return outputList;
	}

	public ArrayList<String> getDepartment()
	{
		ArrayList<String> outputList = new ArrayList<>(); 
		try {

			pst = con.prepareStatement(selectDepartmentSQL);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 String output = result.getString(1);
				 outputList.add(output);
		     }
		}
			 catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		conClose();
		return outputList;
	}
	//將搜尋的單元影片加入課程計畫中
	public void addToCoursePlan(String userId,int unitId){
		int maxOrder = 0;
		try {
			pst = con.prepareStatement(selectPlanMaxSQL);
			pst.setString(1, userId);
			result = pst.executeQuery();
			while(result.next()) {
				maxOrder = result.getInt("MAX(oorder)");
			}
			pst = con.prepareStatement("insert into personalPlan (userId,unitId,lastTime,status,oorder) value(?,?,0,1,?)");
			pst.setString(1, userId);
			pst.setInt(2, unitId);
			pst.setInt(3, ++maxOrder);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	//將搜尋的清單中的所有單元影片加入課程計畫中
	public void addToCoursePlanList(String userId,int courselistId){
		ArrayList<Integer> plans = new ArrayList<Integer>();
		int maxOrder = 0;
		try {
			pst = con.prepareStatement(selectPlanMaxSQL);
			pst.setString(1, userId);
			result = pst.executeQuery();
			while(result.next()) {
				maxOrder = result.getInt("MAX(oorder)");
			}
			result = stat.executeQuery("select * from unit,customlistVideo,courselist where unit.unitId"
					+ " = customlistVideo.unitId and customlistVideo.courselistId ="
					+ " courselist.courselistId and courselist.courselistId = "+ courselistId);
			while(result.next()) {
				plans.add(result.getInt("unit.unitId"));
			}
			
			pst = con.prepareStatement("insert ignore into personalPlan (userId,unitId,lastTime,status,oorder) value(?,?,0,1,?)");
			for(int i = 0;i < plans.size();i++) {
				
				pst.setString(1, userId);
				pst.setInt(2, plans.get(i));
				pst.setInt(3, ++maxOrder);
				pst.addBatch();
			}
			pst.executeBatch();
		}
		catch(SQLException x){
			System.out.println("Exception select"+x.toString());
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
	
	public static void main(String []args)
	{
		SearchManager kldm = new SearchManager();
		
//		System.out.println(kldm.selectCourseListTable("化學"));
//		System.out.println(kldm.selectUnitKeywordTable("微積分"));
//		System.out.println(kldm.getCourseListByKeyword("微積分"));
//		System.out.println(kldm.getAllKeyword("微積分"));
//		System.out.println(kldm.getTeacher());
		System.out.println(kldm.getDepartment());
	}
}
