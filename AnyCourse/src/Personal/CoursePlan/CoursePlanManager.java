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

	public CoursePlanManager()
	{
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
	public ArrayList<CoursePlan> getCoursePlanAllList(String user_id){
		coursePlans = new ArrayList<CoursePlan>();
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from personal_plan,unit,customlist_video,courselist where "
					+ "personal_plan.unit_id = unit.unit_id and "
					+ "unit.unit_id = customlist_video.unit_id and "
					+ "customlist_video.courselist_id = courselist.courselist_id and "
					+ "personal_plan.user_id = "+user_id);
			while(result.next()) {
				coursePlan = new CoursePlan();
				coursePlan.setList_name(result.getString("courselist.list_name"));
				coursePlan.setUnit_name(result.getString("unit.unit_name"));
				coursePlan.setSchool_name(result.getString("courselist.school_name"));
				coursePlan.setTeacher(result.getString("courselist.teacher"));
//				coursePlan.setCourselist_id(result.getInt("courselist.courselist_id"));
				coursePlan.setLikes(result.getInt("unit.likes"));
				coursePlan.setUnit_id(result.getInt("unit.unit_id"));
				coursePlan.setStatus(result.getInt("personal_plan.status"));//狀態
				coursePlan.setOorder(result.getInt("oorder"));
				if(result.getString("unit.video_img_src") == "") {
					coursePlan.setVideo_img_src("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					coursePlan.setVideo_img_src(result.getString("unit.video_img_src"));
				}
				if(result.getString("unit.video_url").split("/")[2].equals("www.youtube.com")) {
					coursePlan.setVideo_type(1);//youtube
				}
				else {
					coursePlan.setVideo_type(2);//jwplayer
				}
				coursePlans.add(coursePlan);
			} 
		}
		catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return coursePlans;
	}
	
	//更改排序
	public void updateCoursePlanList() {
		
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
