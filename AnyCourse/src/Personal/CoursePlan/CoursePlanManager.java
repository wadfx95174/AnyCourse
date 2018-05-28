package Personal.CoursePlan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import HomePage.HomePage;

public class CoursePlanManager {
	
	private ArrayList<CoursePlan> coursePlans;
	private CoursePlan coursePlan;

	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;

	public CoursePlanManager(){
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
	//拿該使用者課程計畫所有影片
	public ArrayList<CoursePlan> getCoursePlanAllList(String userId){
		coursePlans = new ArrayList<CoursePlan>();
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from personalPlan,unit,customListVideo,courselist where "
					+ "personalPlan.unitId = unit.unitId and "
					+ "unit.unitId = customListVideo.unitId and "
					+ "customListVideo.courselistId = courselist.courselistId and "
					+ "personalPlan.userId = '"+userId +"' order by personalPlan.oorder ASC");
			while(result.next()) {
				coursePlan = new CoursePlan();
				coursePlan.setCourselistId(result.getInt("courselist.courselistId"));
				coursePlan.setListName(result.getString("courselist.listName"));
				coursePlan.setUnitName(result.getString("unit.unitName"));
				if(result.getString("courselist.schoolName") == null) {
					coursePlan.setSchoolName("未知學校");
				}
				else {
					coursePlan.setSchoolName(result.getString("courselist.schoolName"));
				}
				if(result.getString("courselist.teacher") == null) {
					coursePlan.setTeacher("未知");
				}
				else {
					coursePlan.setTeacher(result.getString("courselist.teacher"));
				}
				coursePlan.setLastTime(result.getInt("personalPlan.lastTime"));
				coursePlan.setLikes(result.getInt("unit.likes"));
				coursePlan.setUnitId(result.getInt("unit.unitId"));
				coursePlan.setStatus(result.getInt("personalPlan.status"));//狀態
				coursePlan.setOorder(result.getInt("oorder"));
				if(result.getString("unit.videoImgSrc") == "") {
					coursePlan.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					coursePlan.setVideoImgSrc(result.getString("unit.videoImgSrc"));
				}
				if(result.getString("unit.videoUrl").split("/")[2].equals("www.youtube.com")) {
					coursePlan.setVideoType(1);//youtube
				}
				else {
					coursePlan.setVideoType(2);//jwplayer
				}
				coursePlans.add(coursePlan);
			} 
		}
		catch(SQLException x){
			System.out.println("CoursePlan-getCoursePlanAllList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return coursePlans;
	}
	//獲取原本該狀態列表的資料
	public ArrayList<CoursePlan> getCoursePlanOrder(String userId,String received){
		coursePlans = new ArrayList<CoursePlan>();
		int status = 0;
		if(received.equals("wantList"))status = 1;
		else if(received.equals("ingList"))status = 2;
		else if(received.equals("doneList"))status = 3;
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from personalPlan where userId = '" + userId + "' and status = " + status);
			while(result.next()) {
				coursePlan = new CoursePlan();
				coursePlan.setUserId(result.getString("userId"));
				coursePlan.setUnitId(result.getInt("unitId"));
				coursePlan.setStatus(result.getInt("status"));
				coursePlan.setOorder(result.getInt("oorder"));
				coursePlans.add(coursePlan);
			}
		}
		catch(SQLException x){
			System.out.println("CoursePlan-getCoursePlanOrder");
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
		return coursePlans;
	}
	//更新移動後的List的排序
	public void updateCoursePlanList(CoursePlan coursePlan) {
		try {
			//1:想要觀看。2:正在觀看。3:已觀看完
			pst = con.prepareStatement("update personalPlan set status = ? , oorder = ? where userId = ? and unitId = ? ");
			
			pst.setString(3,coursePlan.getUserId());
			pst.setInt(4,coursePlan.getUnitId());
			pst.setInt(1,coursePlan.getStatus());
			pst.setInt(2,coursePlan.getOorder());
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("CoursePlan-updateCoursePlanList");
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
	}
	//獲取移動前的清單的資料
	public ArrayList<CoursePlan> getOldCoursePlanOrder(String userId,String sender){
		coursePlans = new ArrayList<CoursePlan>();
		int status = 0;
		if(sender.equals("wantList"))status = 1;
		else if(sender.equals("ingList"))status = 2;
		else if(sender.equals("doneList"))status = 3;
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from personalPlan where userId = '" + userId + "' and status = " + status);
			while(result.next()) {
				coursePlan = new CoursePlan();
				coursePlan.setUserId(result.getString("userId"));
				coursePlan.setUnitId(result.getInt("unitId"));
				coursePlan.setStatus(result.getInt("status"));
				coursePlan.setOorder(result.getInt("oorder"));
				coursePlans.add(coursePlan);
			}
		}
		catch(SQLException x){
			System.out.println("CoursePlan-getOldCoursePlanOrder");
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
		return coursePlans;
	}
	//更新移動前的清單的排序
	public void updateOldStatusList(CoursePlan oldCoursePlan) {
		try {
			//1:想要觀看。2:正在觀看。3:已觀看完
			pst = con.prepareStatement("update personalPlan set status = ? , oorder = ? where userId = ? and unitId = ? ");
			
			pst.setString(3,oldCoursePlan.getUserId());
			pst.setInt(4,oldCoursePlan.getUnitId());
			pst.setInt(1,oldCoursePlan.getStatus());
			pst.setInt(2,oldCoursePlan.getOorder());
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("CoursePlan-updateOldStatusList");
			System.out.println("Exception update"+x.toString());
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
			System.out.println("CoursePlanManager Close Error");
			System.out.println("Close Exception :" + e.toString()); 
		}		
	} 
	//關閉connection
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("CoursePlanManager Connection Close Error");
			System.out.println("Close Exception :" + e.toString()); 
		}
	}
}
