package HomePage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HomePageManager {
	private String selectRecommend ="select * from customlist_video,unit,recommended_result,courselist where "
			+"recommended_result.unit_id = unit.unit_id and "
			+"customlist_video.unit_id = unit.unit_id and "
			+"customlist_video.courselist_id = courselist.courselist_id and "
			+"recommended_result.account_id = '";
	private String selectPlan="select * from customlist_video,unit,personal_plan,courselist "
			+"where personal_plan.unit_id = unit.unit_id and "
			+"customlist_video.unit_id = unit.unit_id and "
			+"customlist_video.courselist_id = courselist.courselist_id and "
			+"personal_plan.status = ";
	private String selectPlanMax="select MAX(oorder) from personal_plan where status =";
	private String selectRand="select * from customlist_video,unit,courselist "
			+ "where customlist_video.unit_id = unit.unit_id and " 
			+ "customlist_video.courselist_id = courselist.courselist_id order by rand() limit 20";
	
	private Map<Integer, HomePage> map;
	private HomePage homePage;
	private ArrayList<HomePage> homePages;
	
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;

	public HomePageManager() {
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
	//登入之後取得所有推薦影片及課程清單、想要觀看等等的影片或清單
	public ArrayList<Map<Integer, HomePage>> getAllVideo(String userID) {
		ArrayList<Map<Integer, HomePage>> homePages = new ArrayList<Map<Integer, HomePage>>();
		 
		homePages.add(selectRecommendVideo(userID));
		homePages.add(selectRecommendList(userID));
		homePages.add(selectVideoList(userID));
		homePages.add(selectCoursePlanWant(userID));
		homePages.add(selectCoursePlanING(userID));
		
		return homePages;
	}
	//訪客模式及新的用戶隨機抓影片
	public ArrayList<Map<Integer, HomePage>> getRandVideo(){
		ArrayList<Map<Integer, HomePage>> homePages = new ArrayList<Map<Integer, HomePage>>();
		homePages.add(selectRandVideo());
		homePages.add(selectRandList());
		
		return homePages;
	}
	//檢查該使用者在recommended_result有沒有資料以及課程清單、想要觀看、正在觀看有無資料，有的話回傳true
	public boolean checkUser(String userID) {
		boolean check = false;
		try {
			pst = con.prepareStatement("select recommended_result.unit_id from recommended_result,account "
					+ "where account.account_id = recommended_result.account_id and account.user_id = ?");
			pst.setString(1,userID);
			result = pst.executeQuery();
			if(result.next()) {
				check = true;
			}
			pst = con.prepareStatement("select courselist_id from list where user_id = ?");
			pst.setString(1, userID);
			result = pst.executeQuery();
			if(result.next()) {
				check = true;
			}
			pst = con.prepareStatement("select unit_id from personal_plan where user_id = ?");
			pst.setString(1, userID);
			result = pst.executeQuery();
			if(result.next()) {
				check = true;
			}
		}
		catch(SQLException x){
			System.out.println("HomePage Recommend");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return check;
	}
	//隨機抓20個影片
	public Map<Integer, HomePage> selectRandVideo(){
		map = new HashMap<Integer, HomePage>();
		int count = 0;//map的key
		try {
			stat = con.createStatement();
			result = stat.executeQuery(selectRand);
			while(result.next()) {
				homePage = new HomePage();
				homePage.setListName(result.getString("courselist.list_name"));
				homePage.setUnitName(result.getString("unit.unit_name"));
				homePage.setSchoolName(result.getString("courselist.school_name"));
				homePage.setTeacher(result.getString("courselist.teacher"));
				homePage.setCourselistID(result.getInt("courselist.courselist_id"));
				homePage.setUnitLikes(result.getInt("unit.likes"));
				homePage.setUnitID(result.getInt("unit.unit_id"));
				//沒有圖片就塞預設的
				if(result.getString("unit.video_img_src") == "") {
					homePage.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					homePage.setVideoImgSrc(result.getString("unit.video_img_src"));
				}
				
				homePage.setType(1);//代表推薦影片
				if(result.getString("unit.video_url").split("/")[2].equals("www.youtube.com")) {
					homePage.setVideoType(1);//youtube
				}
				else {
					homePage.setVideoType(2);//jwplayer
				}
				map.put(count, homePage);
				count++;
			} 
		}
		catch(SQLException x){
			System.out.println("selectRandVideo");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return map;
	}
	
	//隨機抓20個清單
	public Map<Integer, HomePage> selectRandList(){
		map = new HashMap<Integer, HomePage>();
		int count = 0;//map的key
		try {
			stat = con.createStatement();
			result = stat.executeQuery(selectRand);
			while(result.next()) {
				homePage = new HomePage();
				homePage.setListName(result.getString("courselist.list_name"));
				homePage.setUnitName(result.getString("unit.unit_name"));
				homePage.setSchoolName(result.getString("courselist.school_name"));
				homePage.setTeacher(result.getString("courselist.teacher"));
				homePage.setCourselistID(result.getInt("courselist.courselist_id"));
				homePage.setListLikes(result.getInt("courselist.likes"));
				homePage.setUnitID(result.getInt("unit.unit_id"));
				if(result.getString("unit.video_img_src") == "") {
					homePage.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					homePage.setVideoImgSrc(result.getString("unit.video_img_src"));
				}
				
				homePage.setType(2);//代表推薦影片
				if(result.getString("unit.video_url").split("/")[2].equals("www.youtube.com")) {
					homePage.setVideoType(1);//youtube
				}
				else {
					homePage.setVideoType(2);//jwplayer
				}
				map.put(count, homePage);
				count++;
			} 
		}
		catch(SQLException x){
			System.out.println("selectRandList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return map;
	}
	
	//推薦影片
	public Map<Integer, HomePage> selectRecommendVideo(String userID){
		map = new HashMap<Integer, HomePage>();
		int count = 0;//map的key
		int accountID = 0;
		try {
			pst = con.prepareStatement("select * from recommended_result,account where "
					+ "account.account_id = recommended_result.account_id and account.user_id = ?");
			pst.setString(1,userID);
			result = pst.executeQuery();
			if(result.next()) {
				accountID = result.getInt("account.account_id");
			}
			
			stat = con.createStatement();
			result = stat.executeQuery(selectRecommend + accountID + "'");
			while(result.next()) {
				homePage = new HomePage();
				homePage.setAccountID(result.getInt("recommended_result.account_id"));
				homePage.setListName(result.getString("courselist.list_name"));
				homePage.setUnitName(result.getString("unit.unit_name"));
				homePage.setSchoolName(result.getString("courselist.school_name"));
				homePage.setTeacher(result.getString("courselist.teacher"));
				homePage.setCourselistID(result.getInt("courselist.courselist_id"));
				homePage.setUnitLikes(result.getInt("unit.likes"));
				homePage.setUnitID(result.getInt("unit.unit_id"));
				if(result.getString("unit.video_img_src") == "") {
					homePage.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					homePage.setVideoImgSrc(result.getString("unit.video_img_src"));
				}
				
				homePage.setType(1);//代表推薦影片
				if(result.getString("unit.video_url").split("/")[2].equals("www.youtube.com")) {
					homePage.setVideoType(1);//youtube
				}
				else {
					homePage.setVideoType(2);//jwplayer
				}
				map.put(count, homePage);
				count++;
			} 
		}
		catch(SQLException x){
			System.out.println("selectRecommendVideo");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return map;
	}
	
	//推薦清單
	public Map<Integer, HomePage> selectRecommendList(String userID){
		map = new HashMap<Integer, HomePage>();
		int count = 0;//map的key
		int check = 0;//0代表是第一次跑這個清單，不是0代表這個清單已經跑過
		int accountID = 0;
		try {
			pst = con.prepareStatement("select * from recommended_result,account where "
					+ "account.account_id = recommended_result.account_id and account.user_id = ?");
			pst.setString(1,userID);
			result = pst.executeQuery();
			if(result.next()) {
				accountID = result.getInt("account.account_id");
			}
			else return null;
			
			result = stat.executeQuery(selectRecommend + accountID +"' order by customlist_video.oorder ASC");
			while(result.next()) {
				if(check == 0 || check!=result.getInt("customlist_video.courselist_id")) {
					homePage = new HomePage();
					homePage.setAccountID(result.getInt("recommended_result.account_id"));
					homePage.setListName(result.getString("courselist.list_name"));
					homePage.setSchoolName(result.getString("courselist.school_name"));
					homePage.setTeacher(result.getString("courselist.teacher"));
					homePage.setCourselistID(result.getInt("courselist.courselist_id"));
					homePage.setListLikes(result.getInt("courselist.likes"));
					homePage.setUnitID(result.getInt("unit.unit_id"));
					if(result.getString("unit.video_img_src") == "") {
						homePage.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
					}
					else {
						homePage.setVideoImgSrc(result.getString("unit.video_img_src"));
					}
					
					homePage.setType(2);//代表推薦清單
					if(result.getString("unit.video_url").split("/")[2].equals("www.youtube.com")) {
						homePage.setVideoType(1);//youtube
					}
					else {
						homePage.setVideoType(2);//jwplayer
					}
					map.put(count, homePage);
					
					count++;
				}
				check = result.getInt("customlist_video.courselist_id");
			} 
		}
		catch(SQLException x){
			System.out.println("selectRecommendList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return map;
	}
	
	//找使用者的課程清單
	public Map<Integer, HomePage> selectVideoList(String userID){
		map = new HashMap<Integer, HomePage>();
		int count = 0;//map的key
		int max = 0;
		int check = 0;//0代表是第一次跑這個清單，不是0代表這個清單已經跑過，就不需要再存了
		try {
			//找oorder來當最大值
			stat = con.createStatement();
			//檢查清單列表有沒有清單
			result = stat.executeQuery("select MAX(oorder) from list where user_id = '" + userID+"'");
			while(result.next()) {
				max = result.getInt("MAX(oorder)");
				//檢查有沒有資料
				if(max == 0)return null;
			}
			max = 0;
			//看有幾個清單是有內容的，有才可以顯示在首頁
			result = stat.executeQuery("select * from customlist_video,unit,list,courselist where "
					+"customlist_video.courselist_id = list.courselist_id and "
					+"customlist_video.unit_id = unit.unit_id and "
					+"list.courselist_id = courselist.courselist_id and "
					+"list.user_id ='" + userID+"'");
			while(result.next()) {
				if(check == 0 || check!=result.getInt("customlist_video.courselist_id")) {
					max++;
					check = result.getInt("customlist_video.courselist_id");
				}
					
			}
			//將清單有影片的找出來
			result = stat.executeQuery("select * from customlist_video,unit,list,courselist where "
					+"customlist_video.courselist_id = list.courselist_id and "
					+"customlist_video.unit_id = unit.unit_id and "
					+"list.courselist_id = courselist.courselist_id and "
					+"list.user_id ='" + userID+"'");
			count = 0;
			while(result.next()) {
				if(check == 0 || check!=result.getInt("customlist_video.courselist_id")) {
					homePage = new HomePage();
					homePage.setUserID(result.getString("list.user_id"));
					homePage.setListName(result.getString("courselist.list_name"));
					homePage.setSchoolName(result.getString("courselist.school_name"));
					homePage.setTeacher(result.getString("courselist.teacher"));
					homePage.setCourselistID(result.getInt("list.courselist_id"));
					homePage.setListLikes(result.getInt("courselist.likes"));
					homePage.setUnitID(result.getInt("customlist_video.unit_id"));
					homePage.setNum(max);
					homePage.setType(3);//代表課程清單
					if(result.getString("unit.video_img_src") == "") {
						homePage.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
					}
					else {
						homePage.setVideoImgSrc(result.getString("unit.video_img_src"));
					}
					if(result.getString("unit.video_url").split("/")[2].equals("www.youtube.com")) {
						homePage.setVideoType(1);//youtube
					}
					else {
						homePage.setVideoType(2);//jwplayer
					}
					map.put(count, homePage);
					count++;
				}
				check = result.getInt("customlist_video.courselist_id");
			} 
		}
		catch(SQLException x){
			System.out.println("selectVideoList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return map;
	}
	
	//找使用者的課程計畫中的"想要觀看"
	public Map<Integer, HomePage> selectCoursePlanWant(String userID){
		map = new HashMap<Integer, HomePage>();
		int count = 0;//map的key
		int max = 0;
		try {
			//找oorder來當最大值
			stat = con.createStatement();
			result = stat.executeQuery(selectPlanMax+" 1 and user_id = '" + userID+"'");
			while(result.next()) {
				max = result.getInt("MAX(oorder)");
				//檢查有沒有資料
				if(max == 0)return null;
			}
			
			result = stat.executeQuery(selectPlan+ "1 and personal_plan.user_id = '" + userID+"'");
			while(result.next()) {
				homePage = new HomePage();
				homePage.setUserID(result.getString("personal_plan.user_id"));
				homePage.setUnitName(result.getString("unit.unit_name"));
				homePage.setListName(result.getString("courselist.list_name"));
				homePage.setSchoolName(result.getString("courselist.school_name"));
				homePage.setTeacher(result.getString("courselist.teacher"));
				homePage.setCourselistID(result.getInt("courselist.courselist_id"));
				homePage.setUnitLikes(result.getInt("unit.likes"));
				homePage.setUnitID(result.getInt("unit.unit_id"));
				homePage.setNum(max);
				homePage.setType(4);//代表想要觀看
				if(result.getString("unit.video_img_src") == "") {
					homePage.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					homePage.setVideoImgSrc(result.getString("unit.video_img_src"));
				}
				if(result.getString("unit.video_url").split("/")[2].equals("www.youtube.com")) {
					homePage.setVideoType(1);//youtube
				}
				else {
					homePage.setVideoType(2);//jwplayer
				}
				map.put(count, homePage);
				count++;
			} 
		}
		catch(SQLException x){
			System.out.println("selectCoursePlanWant");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return map;
	}
	
	//找使用者的課程計畫中的"正在觀看"
	public Map<Integer, HomePage> selectCoursePlanING(String userID){
		map = new HashMap<Integer, HomePage>();
		int count = 0;//map的key
		int max = 0;
		try {
			//找oorder來當最大值
			stat = con.createStatement();
			result = stat.executeQuery(selectPlanMax+" 2 and user_id = '" + userID+"'");
			while(result.next()) {
				max = result.getInt("MAX(oorder)");
				//檢查有沒有資料
				if(max == 0)return null;
			}
			
			result = stat.executeQuery(selectPlan+ "2 and personal_plan.user_id = '" + userID+"'");
			while(result.next()) {
				homePage = new HomePage();
				homePage.setUserID(result.getString("personal_plan.user_id"));
				homePage.setUnitName(result.getString("unit.unit_name"));
				homePage.setListName(result.getString("courselist.list_name"));
				homePage.setSchoolName(result.getString("courselist.school_name"));
				homePage.setTeacher(result.getString("courselist.teacher"));
				homePage.setCourselistID(result.getInt("courselist.courselist_id"));
				homePage.setUnitLikes(result.getInt("unit.likes"));
				homePage.setUnitID(result.getInt("unit.unit_id"));
				homePage.setNum(max);
				homePage.setType(5);//代表想要觀看
				if(result.getString("unit.video_img_src") == "") {
					homePage.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					homePage.setVideoImgSrc(result.getString("unit.video_img_src"));
				}
				if(result.getString("unit.video_url").split("/")[2].equals("www.youtube.com")) {
					homePage.setVideoType(1);//youtube
				}
				else {
					homePage.setVideoType(2);//jwplayer
				}
				map.put(count, homePage);
				count++;
			} 
		}
		catch(SQLException x){
			System.out.println("selectCoursePlanING");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return map;
	}
	//將首頁的單元影片加入課程計畫中
	public void addToCoursePlan(String userID,int unitID){
		int maxOrder = 0;
		try {
			stat = con.createStatement();
			result = stat.executeQuery(selectPlanMax+" 1 and user_id = '" + userID+"'");
			while(result.next()) {
				maxOrder = result.getInt("MAX(oorder)");
			}
			pst = con.prepareStatement("insert into personal_plan (user_id,unit_id,last_time,status,oorder) value(?,?,0,1,?)");
			pst.setString(1, userID);
			pst.setInt(2, unitID);
			pst.setInt(3, ++maxOrder);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("addToCoursePlan");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	//將首頁的清單中的所有單元影片加入課程計畫中
	public void addToCoursePlan_List(String userID,int courselistID){
		homePages = new ArrayList<HomePage>();
		homePage = new HomePage();
		int maxOrder = 0;
		try {
			stat = con.createStatement();
			result = stat.executeQuery(selectPlanMax+" 1 and user_id = '" + userID+"'");
			while(result.next()) {
				maxOrder = result.getInt("MAX(oorder)");
			}
			result = stat.executeQuery("select * from unit,customlist_video,courselist where unit.unit_id"
					+ " = customlist_video.unit_id and customlist_video.courselist_id ="
					+ " courselist.courselist_id and courselist.courselist_id = "+ courselistID);
			while(result.next()) {
				homePage = new HomePage();
				homePage.setUnitID(result.getInt("unit.unit_id"));
				homePages.add(homePage);
			}
			pst = con.prepareStatement("insert ignore into personal_plan (user_id,unit_id,last_time,status,oorder) value(?,?,0,1,?)");
			for(int i = 0;i < homePages.size();i++) {
				
				pst.setString(1, userID);
				pst.setInt(2, homePages.get(i).getUnitID());
				pst.setInt(3, ++maxOrder);
				//先放到batch，等迴圈跑完再一次新增
				pst.addBatch();
			}
			pst.executeBatch();
		}
		catch(SQLException x){
			System.out.println("addToCoursePlan_List");
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
			System.out.println("HomePage Close Error");
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
			System.out.println("Connection Error");
			System.out.println("Close Exception :" + e.toString()); 
		}
	}
}
