package Personal.CoursePlan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import HomePage.HomePage;
import Personal.VideoList.VideoList;

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
	
	//取得該使用者所有的課程清單
	public ArrayList<CoursePlan> getVideoList(String userId){
		coursePlans = new ArrayList<CoursePlan>();
		try {
			pst = con.prepareStatement("select distinct courselist.courselistId,courselist.listName "
					+ "from personalPlan,customListVideo,courselist where "
					+ "personalPlan.unitId = customListVideo.unitId and "
					+ "customListVideo.courselistId = courselist.courselistId and "
					+ "personalPlan.creator = courselist.creator and "
					+ "personalPlan.userId = ?");
			pst.setString(1, userId);
			result = pst.executeQuery();
			while(result.next()) {
				coursePlan = new CoursePlan();
				coursePlan.setUserId(userId);
				coursePlan.setCourselistId(result.getInt("courselist.courselistId"));
				coursePlan.setListName(result.getString("courselist.listName"));
				coursePlans.add(coursePlan);
			}
		}
		catch(SQLException x){
			System.out.println("CoursePlan-getVideoList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return coursePlans;
	}
	
	//取得該使用者課程計畫對應的影片
	public ArrayList<CoursePlan> getCoursePlanUnit(String userId, int courselistId){
		coursePlans = new ArrayList<CoursePlan>();
		try {
			pst = con.prepareStatement("select * from personalPlan,unit,customListVideo,courselist where "
					+ "personalPlan.unitId = unit.unitId and "
					+ "unit.unitId = customListVideo.unitId and "
					+ "customListVideo.courselistId = courselist.courselistId and "
					+ "personalPlan.userId = ? and courselist.courselistId = ? "
					+ "order by personalPlan.oorder ASC");
			pst.setString(1, userId);
			pst.setInt(2,courselistId);
			result = pst.executeQuery();
			while(result.next()) {
				coursePlan = new CoursePlan();
				coursePlan.setCourselistId(result.getInt("courselist.courselistId"));
				coursePlan.setListName(result.getString("courselist.listName"));
				coursePlan.setUnitName(result.getString("unit.unitName"));
				coursePlan.setSchoolName(result.getString("unit.schoolName"));
				coursePlan.setTeacher(result.getString("unit.teacher"));
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
				//youtube
				if(result.getString("unit.videoUrl").split("/")[2].equals("www.youtube.com")) {
					coursePlan.setVideoType(1);
				}
				//jwplayer
				else {
					coursePlan.setVideoType(2);
				}
				coursePlans.add(coursePlan);
			} 
		}
		catch(SQLException x){
			System.out.println("CoursePlan-getCoursePlanUnit");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return coursePlans;
	}
	
	//取得該使用者課程計畫所有影片
	public ArrayList<CoursePlan> getAllUnit(String userId){
		coursePlans = new ArrayList<CoursePlan>();
		try {
			pst = con.prepareStatement("select * from personalPlan,unit,customListVideo,courselist where "
					+ "personalPlan.unitId = unit.unitId and "
					+ "unit.unitId = customListVideo.unitId and "
					+ "customListVideo.courselistId = courselist.courselistId and "
					+ "personalPlan.creator = courselist.creator and "
					+ "personalPlan.userId = ? order by personalPlan.oorder ASC");
			pst.setString(1, userId);
			result = pst.executeQuery();
			while(result.next()) {
				coursePlan = new CoursePlan();
				coursePlan.setCourselistId(result.getInt("courselist.courselistId"));
				coursePlan.setListName(result.getString("courselist.listName"));
				coursePlan.setUnitName(result.getString("unit.unitName"));
				coursePlan.setSchoolName(result.getString("unit.schoolName"));
				coursePlan.setTeacher(result.getString("unit.teacher"));
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
			System.out.println("CoursePlan-getAllUnit");
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
				coursePlan.setLastTime(result.getInt("lastTime"));
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
			pst = con.prepareStatement("update personalPlan set status = ?,oorder = ?,lastTime = ? "
					+ "where userId = ? and unitId = ? ");
			if(coursePlan.getStatus() == 1)pst.setInt(3,0);
			else pst.setInt(3, coursePlan.getLastTime());
			pst.setInt(1,coursePlan.getStatus());
			pst.setInt(2,coursePlan.getOorder());
			pst.setString(4,coursePlan.getUserId());
			pst.setInt(5,coursePlan.getUnitId());
			
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
	
	//刪除課程計畫中的影片
	public void deleteVideo(String userId,int unitId) {
		try {
			//1:想要觀看。2:正在觀看。3:已觀看完
			pst = con.prepareStatement("delete from personalPlan where userId = ? and unitId = ?");
			pst.setString(1,userId);
			pst.setInt(2,unitId);
			
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
	//分享個人課程計畫至指定群組
	public void shareCoursePlanToGroup(ArrayList<String> toUserIdList,String userId,int unitId,int groupId){
		int[] maxOrder = new int[toUserIdList.size()+1];
		int index = 0;
		
		try {
			for(int i = 0; i < toUserIdList.size();i++) {
				pst = con.prepareStatement("select MAX(oorder) from groupPlan where groupId = ? and userId = ? "
						+ "and status = 1 ");
				pst.setInt(1, groupId);
				pst.setString(2, toUserIdList.get(i));
				result = pst.executeQuery();
				if(result.next())maxOrder[i] = result.getInt("MAX(oorder)");
				index++;
			}
			pst = con.prepareStatement("select MAX(oorder) from groupPlan where groupId = ? and userId = ? "
					+ "and status = 1 ");
			pst.setInt(1, groupId);
			pst.setString(2, userId);
			result = pst.executeQuery();
			if(result.next())maxOrder[index] = result.getInt("MAX(oorder)");
			
			java.util.Date now = new java.util.Date();//取得現在時間
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		    sf.setTimeZone(TimeZone.getTimeZone("GMT"));  // 設定時區為格林威治 GMT 時區
		    String sGMT = sf.format(now);
		    java.sql.Date createDate = java.sql.Date.valueOf(sGMT); // 要轉換成 java.sql.Date 的物件才可以寫入資料庫

		    
			pst = con.prepareStatement("insert ignore into groupPlan (groupId,deadlineTime"
					+ ",userId,unitId,lastTime,status,oorder) value(?,?,?,?,0,1,?)");
			for(int i = 0;i < toUserIdList.size();i++) {
				
				pst.setInt(1, groupId);
				pst.setDate(2, createDate);
				pst.setString(3, toUserIdList.get(i));
				pst.setInt(4, unitId);
				pst.setInt(5, ++maxOrder[i]);
				//先放到batch，等迴圈跑完再一次新增
				pst.addBatch();
			}
			pst.setInt(1, groupId);
			pst.setDate(2, createDate);
			pst.setString(3, userId);
			pst.setInt(4, unitId);
			pst.setInt(5, ++maxOrder[index]);
			pst.addBatch();
			pst.executeBatch();
			
			
		}
		catch(SQLException x){
			System.out.println("CoursePlan-shareCoursePlanToGroup");
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
