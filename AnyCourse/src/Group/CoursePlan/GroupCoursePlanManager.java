package Group.CoursePlan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Personal.CoursePlan.CoursePlan;

public class GroupCoursePlanManager {
	
	private ArrayList<GroupCoursePlan> groupCoursePlans;
	private GroupCoursePlan groupCoursePlan;
	
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;

	public GroupCoursePlanManager() {
		try
		{
			Class.forName("com.mysql.jdbc.Driver");// 註冊Driver
			con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=Big5"
					, "root", "peter");// 取得connection

		} catch (final ClassNotFoundException e)
		{
			System.out.println("DriverClassNotFound" + e.toString());
		} catch (final SQLException x)
		{
			System.out.println("Exception" + x.toString());
		}
	}
	
	//取得該使用者所有的課程清單
	public String getVideoList(int groupId,String userId){
		groupCoursePlans = new ArrayList<GroupCoursePlan>();
		try {
			pst = con.prepareStatement("select courselist.courselistId,courselist.listName "
					+ "from groupPlan,customListVideo,courselist where "
					+ "groupPlan.unitId = customListVideo.unitId and "
					+ "customListVideo.courselistId = courselist.courselistId and "
					+ "groupPlan.groupId = ? and groupPlan.userId = ?");
			pst.setInt(1, groupId);
			pst.setString(2,userId);
			result = pst.executeQuery();
			
			while(result.next()) {
				groupCoursePlan = new GroupCoursePlan();
				groupCoursePlan.setCourselistId(result.getInt("courselist.courselistId"));
				groupCoursePlan.setListName(result.getString("courselist.listName"));
				groupCoursePlans.add(groupCoursePlan);
			}
		}
		catch(SQLException x){
			System.out.println("GroupCoursePlanManager-getVideoList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return new Gson().toJson(groupCoursePlans);
	}
	
	//取得該使用者課程計畫對應的影片
	public String getCoursePlanUnit(String userId, int courselistId, int groupId){
		
		groupCoursePlans = new ArrayList<GroupCoursePlan>();
		try {
			pst = con.prepareStatement("select * from groupPlan,unit,customListVideo,courselist where "
					+ "groupPlan.unitId = unit.unitId and "
					+ "unit.unitId = customListVideo.unitId and "
					+ "customListVideo.courselistId = courselist.courselistId and "
					+ "groupPlan.userId = ? and courselist.courselistId = ? and "
					+ "groupPlan.groupId = ? order by groupPlan.oorder ASC");
			pst.setString(1, userId);
			pst.setInt(2,courselistId);
			pst.setInt(3, groupId);
			result = pst.executeQuery();
			while(result.next()) {
				groupCoursePlan = new GroupCoursePlan();
				groupCoursePlan.setGroupId(groupId);
				groupCoursePlan.setCourselistId(result.getInt("courselist.courselistId"));
				groupCoursePlan.setListName(result.getString("courselist.listName"));
				groupCoursePlan.setUnitName(result.getString("unit.unitName"));
				groupCoursePlan.setSchoolName(result.getString("unit.schoolName"));
				groupCoursePlan.setTeacher(result.getString("unit.teacher"));
				groupCoursePlan.setLastTime(result.getInt("groupPlan.lastTime"));
				groupCoursePlan.setUnitLikes(result.getInt("unit.likes"));
				groupCoursePlan.setUnitId(result.getInt("unit.unitId"));
				groupCoursePlan.setStatus(result.getInt("groupPlan.status"));//狀態
				groupCoursePlan.setOorder(result.getInt("oorder"));
				groupCoursePlan.setCreator(result.getString("creator"));
				if(result.getString("unit.videoImgSrc") == "") {
					groupCoursePlan.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					groupCoursePlan.setVideoImgSrc(result.getString("unit.videoImgSrc"));
				}
				
				//youtube
				if(result.getString("unit.videoUrl").split("/")[2].equals("www.youtube.com")) {
					groupCoursePlan.setVideoType(1);
				}
				//jwplayer
				else {
					groupCoursePlan.setVideoType(2);
				}
				groupCoursePlans.add(groupCoursePlan);
			} 
		}
		catch(SQLException x){
			System.out.println("GroupCoursePlanManager-getCoursePlanUnit");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return new Gson().toJson(groupCoursePlans);
	}
	
	//取得該使用者課程計畫所有影片
	public String getCoursePlanAllList(String userId , int groupId){
		groupCoursePlans = new ArrayList<GroupCoursePlan>();
		try {
			pst = con.prepareStatement("select * from groupPlan,unit,customListVideo,courselist where "
					+ "groupPlan.unitId = unit.unitId and "
					+ "unit.unitId = customListVideo.unitId and "
					+ "customListVideo.courselistId = courselist.courselistId and "
					+ "groupPlan.groupId = ? and groupPlan.userId = ? order by groupPlan.oorder ASC");
			pst.setInt(1, groupId);
			pst.setString(2,userId);
			result = pst.executeQuery();
			while(result.next()) {
				groupCoursePlan = new GroupCoursePlan();
				groupCoursePlan.setGroupId(groupId);
				groupCoursePlan.setCourselistId(result.getInt("courselist.courselistId"));
				groupCoursePlan.setListName(result.getString("courselist.listName"));
				groupCoursePlan.setUnitName(result.getString("unit.unitName"));
				groupCoursePlan.setSchoolName(result.getString("unit.schoolName"));
				groupCoursePlan.setTeacher(result.getString("unit.teacher"));
				groupCoursePlan.setLastTime(result.getInt("groupPlan.lastTime"));
				groupCoursePlan.setUnitLikes(result.getInt("unit.likes"));
				groupCoursePlan.setUnitId(result.getInt("unit.unitId"));
				groupCoursePlan.setStatus(result.getInt("groupPlan.status"));//狀態
				groupCoursePlan.setOorder(result.getInt("oorder"));
				
				if(result.getString("unit.videoImgSrc") == "") {
					groupCoursePlan.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					groupCoursePlan.setVideoImgSrc(result.getString("unit.videoImgSrc"));
				}
				
				//youtube
				if(result.getString("unit.videoUrl").split("/")[2].equals("www.youtube.com")) {
					groupCoursePlan.setVideoType(1);
				}
				//jwplayer
				else {
					groupCoursePlan.setVideoType(2);
				}
				groupCoursePlans.add(groupCoursePlan);
			} 
		}
		catch(SQLException x){
			System.out.println("GroupCoursePlanManager-getCoursePlanAllList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return new Gson().toJson(groupCoursePlans);
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
			System.out.println("CalendarManager Close Exception :" + e.toString()); 
		}		
	} 
	
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("CalendarManager Close Exception :" + e.toString()); 
		}
	}

}
