package HomePage;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Personal.VideoList.UnitVideo;

public class HomePageDatabaseManager {
	
	private String selectRecommendUnitSQL = "select * from recommended_result,unit"
			+"where recommended_result.user_id = 1 and recommended_result.unit_id = unit.unit_id";
	

	private HomePage homePage;
	
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	private PreparedStatement pst2 = null;

	public HomePageDatabaseManager() {
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
	public ArrayList<Map<Integer, HomePage>> getAllVideo(String user_id) {
		Map<Integer, String> columns;
		ArrayList<Map<Integer, HomePage>> rows = new ArrayList<Map<Integer, HomePage>>();
		 
		
		rows.add( selectVideoList(user_id) );
		 
		// 第二筆資料
//		columns = new HashMap<Integer, String>();
//		columns.put("id", "2");
//		columns.put("name", "Lily");
//		columns.put("sex", "女生");
//		rows.add( columns );
		 
		// 第三筆資料
//		columns = new HashMap<Integer, String>();
//		columns.put("id", "3");
//		columns.put("name", "Joan");
//		columns.put("sex", "女生");
//		rows.add( columns );
		 
//		for( Map row : rows )
//		{
//		    System.out.println( "id:" + row.get("id") + "\t"
//		                      + "name:" + row.get("name") + "\t"
//		                      + "sex:" + row.get("sex") + "\t"
//		                      );
//		}
		return rows;
	}
	/*public Map<String, String> selectRecommendedResult(ArrayList<HomePage> homePages,HomePage homePage) {
		Map<String, String> map = new HashMap<String, String>();
		
		
		int check = 0;//選到了
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from recommended_result where user_id =" + homePage.getUser_id());
			while(result.next()) {
				if(result.getString("creator").equals(homePage.getUser_id())) {
					check = 1;
				}
			}
			
			
			stat = con.createStatement();
			result = stat.executeQuery(selectCourseListSQL);
			while(result.next()) {
				//選取該使用者的課程清單
				if(result.getString("user_id").equals("1")) {
					homePage = new HomePage();
					homePage.setUserID(result.getString("list.user_id"));
					homePage.setCourselistID(Integer.parseInt(result.getString("list.courselist_id")));
					homePage.setOorder(result.getInt("list.oorder"));
					homePage.setListName(result.getString("courselist.list_name")); 
					homePage.setCreator(result.getString("courselist.creator"));
					homePage.setSchoolName(result.getString("courselist.school_name"));
					homePages.add(homePage);
				}
			}
		     
		}
		catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}*/
	public Map<Integer, HomePage> selectVideoList(String user_id){
		Map<Integer, HomePage> map = new HashMap<Integer, HomePage>();
		List<Integer> courseListID = new ArrayList<Integer>();
		HomePage homePage;
		int count = 0;//map的key
		boolean check = false;//檢查該使用者的課程清單有無資料
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select courselist_id from list where user_id = " + user_id);
			if(!result.next()) {return null;}
			
			result = stat.executeQuery("select * from customlist_video,unit,list,courselist where "
					+"customlist_video.courselist_id = list.courselist_id and "
					+"customlist_video.unit_id = unit.unit_id and "
					+"list.courselist_id = courselist.courselist_id and "
					+"list.user_id =" + user_id);
			while(result.next()) {
				homePage = new HomePage();
				homePage.setUser_id(result.getString("list.user_id"));
				homePage.setList_name(result.getString("courselist.list_name"));
				homePage.setCourse_info(result.getString("courselist.course_info"));
				homePage.setSchool_name(result.getString("courselist.school_name"));
				homePage.setTeacher(result.getString("courselist.teacher"));
				homePage.setCourselist_id(result.getInt("list.courselist_id"));
				homePage.setListLikes(result.getInt("courselist.likes"));
				homePage.setUnit_id(result.getInt("customlist_video.unit_id"));
				homePage.setType(3);//代表課程清單
				if(result.getString("unit.video_url").split("/")[2].equals("www.youtube.com")) {
					homePage.setVideo_type(1);//youtube
				}
				else {
					homePage.setVideo_type(2);//jwplayer
				}
				map.put(count, homePage);
				count++;
			} 
			
		}
		catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		
		return map;
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
			if(pst2!=null) {
				pst2.close();
				pst2 = null;
			}
		}
		catch(SQLException e) {
			System.out.println("Close Exception :" + e.toString()); 
		}		
	}
}
