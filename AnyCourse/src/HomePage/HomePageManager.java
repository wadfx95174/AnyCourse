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
	private String selectRecommend ="select * from customListVideo,unit,recommendedResult,courselist where "
			+"recommendedResult.unitId = unit.unitId and "
			+"customListVideo.unitId = unit.unitId and "
			+"customListVideo.courselistId = courselist.courselistId and "
			+"recommendedResult.accountId = '";
	private String selectPlan="select * from customListVideo,unit,personalPlan,courselist "
			+"where personalPlan.unitId = unit.unitId and "
			+"customListVideo.unitId = unit.unitId and "
			+"customListVideo.courselistId = courselist.courselistId and "
			+"personalPlan.status = ";
	private String selectPlanMax="select MAX(oorder) from personalPlan where status =";
	private String selectRand="select * from customListVideo,unit,courselist "
			+ "where customListVideo.unitId = unit.unitId and " 
			+ "customListVideo.courselistId = courselist.courselistId and "
			+ "courselist.share = 1 order by rand() limit 20";
	
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
	public ArrayList<Map<Integer, HomePage>> getAllVideo(String userId) {
		ArrayList<Map<Integer, HomePage>> homePages = new ArrayList<Map<Integer, HomePage>>();
		Map<Integer, HomePage> recommendVideo = new HashMap<Integer, HomePage>();
		Map<Integer, HomePage> recommendlist = new HashMap<Integer, HomePage>();
		Map<Integer, HomePage> videoList = new HashMap<Integer, HomePage>();
		Map<Integer, HomePage> Want = new HashMap<Integer, HomePage>();
		Map<Integer, HomePage> ING = new HashMap<Integer, HomePage>();
		recommendVideo = selectRecommendVideo(userId);
		recommendlist = selectRecommendList(userId);
		videoList = selectVideoList(userId);
		Want = selectCoursePlanWant(userId);
		ING = selectCoursePlanING(userId);
		//判斷該使用者在recommendedResult中有沒有資料，沒有的話就要random給影片
		if(recommendVideo == null || recommendVideo.size() < 1)homePages.add(selectRandVideo());
		else homePages.add(recommendVideo);
		if(recommendlist == null || recommendlist.size() < 1)homePages.add(selectRandList());
		else homePages.add(recommendlist);
		if(videoList == null || videoList.size() < 1){}
		else homePages.add(videoList);
		if(Want == null || Want.size() < 1){}
		else homePages.add(Want);
		if(ING == null || ING.size() < 1){}
		else homePages.add(ING);
		
		return homePages;
	}
	//訪客模式及新的用戶隨機抓影片
	public ArrayList<Map<Integer, HomePage>> getRandVideo(){
		ArrayList<Map<Integer, HomePage>> homePages = new ArrayList<Map<Integer, HomePage>>();
		homePages.add(selectRandVideo());
		homePages.add(selectRandList());
		
		return homePages;
	}
	//檢查該使用者在recommendedResult有沒有資料以及課程清單、想要觀看、正在觀看有無資料，有的話回傳true
	public boolean checkUser(String userId) {
		boolean check = false;
		try {
			pst = con.prepareStatement("select recommendedResult.unitId from recommendedResult,account "
					+ "where account.accountId = recommendedResult.accountId and account.userId = ?");
			pst.setString(1,userId);
			result = pst.executeQuery();
			if(result.next()) {
				check = true;
			}
			pst = con.prepareStatement("select courselistId from list where userId = ?");
			pst.setString(1, userId);
			result = pst.executeQuery();
			if(result.next()) {
				check = true;
			}
			pst = con.prepareStatement("select unitId from personalPlan where userId = ?");
			pst.setString(1, userId);
			result = pst.executeQuery();
			if(result.next()) {
				check = true;
			}
		}
		catch(SQLException x){
			System.out.println("HomePage-checkUser");
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
				homePage.setListName(result.getString("courselist.listName"));
				homePage.setUnitName(result.getString("unit.unitName"));
				homePage.setSchoolName(result.getString("unit.schoolName"));
				homePage.setTeacher(result.getString("unit.teacher"));
				homePage.setCourselistId(result.getInt("courselist.courselistId"));
				homePage.setUnitLikes(result.getInt("unit.likes"));
				homePage.setUnitId(result.getInt("unit.unitId"));
				homePage.setCreator(result.getString("courselist.creator"));
				//沒有圖片就塞預設的
				if(result.getString("unit.videoImgSrc") == "") {
					homePage.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					homePage.setVideoImgSrc(result.getString("unit.videoImgSrc"));
				}
				
				homePage.setType(1);//代表推薦影片
				if(result.getString("unit.videoUrl").split("/")[2].equals("www.youtube.com")) {
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
			System.out.println("HomePage-selectRandVideo");
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
				homePage.setListName(result.getString("courselist.listName"));
				homePage.setUnitName(result.getString("unit.unitName"));
				homePage.setSchoolName(result.getString("unit.schoolName"));
				homePage.setTeacher(result.getString("unit.teacher"));
				homePage.setCourselistId(result.getInt("courselist.courselistId"));
				homePage.setListLikes(result.getInt("courselist.likes"));
				homePage.setUnitId(result.getInt("unit.unitId"));
				homePage.setCreator(result.getString("courselist.creator"));
				if(result.getString("unit.videoImgSrc") == "") {
					homePage.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					homePage.setVideoImgSrc(result.getString("unit.videoImgSrc"));
				}
				
				homePage.setType(2);//代表推薦影片
				if(result.getString("unit.videoUrl").split("/")[2].equals("www.youtube.com")) {
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
			System.out.println("HomePage-selectRandList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return map;
	}
	
	//推薦影片
	public Map<Integer, HomePage> selectRecommendVideo(String userId){
		map = new HashMap<Integer, HomePage>();
		int count = 0;//map的key
		int accountId = 0;
		try {
			pst = con.prepareStatement("select * from recommendedResult,account where "
					+ "account.accountId = recommendedResult.accountId and account.userId = ?");
			pst.setString(1,userId);
			result = pst.executeQuery();
			if(result.next()) {
				accountId = result.getInt("account.accountId");
			}
			stat = con.createStatement();
			result = stat.executeQuery(selectRecommend + accountId + "'");
			while(result.next()) {
				homePage = new HomePage();
				homePage.setAccountId(result.getInt("recommendedResult.accountId"));
				homePage.setListName(result.getString("courselist.listName"));
				homePage.setUnitName(result.getString("unit.unitName"));
				homePage.setSchoolName(result.getString("unit.schoolName"));
				homePage.setTeacher(result.getString("unit.teacher"));
				homePage.setCourselistId(result.getInt("courselist.courselistId"));
				homePage.setUnitLikes(result.getInt("unit.likes"));
				homePage.setUnitId(result.getInt("unit.unitId"));
				homePage.setCreator(result.getString("courselist.creator"));
				if(result.getString("unit.videoImgSrc") == "") {
					homePage.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					homePage.setVideoImgSrc(result.getString("unit.videoImgSrc"));
				}
				
				homePage.setType(1);//代表推薦影片
				if(result.getString("unit.videoUrl").split("/")[2].equals("www.youtube.com")) {
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
			System.out.println("HomePage-selectRecommendVideo");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		
		return map;
	}
	
	//推薦清單
	public Map<Integer, HomePage> selectRecommendList(String userId){
		map = new HashMap<Integer, HomePage>();
		int count = 0;//map的key
		int check = 0;//0代表是第一次跑這個清單，不是0代表這個清單已經跑過
		int accountId = 0;
		try {
			pst = con.prepareStatement("select * from recommendedResult,account where "
					+ "account.accountId = recommendedResult.accountId and account.userId = ?");
			pst.setString(1,userId);
			result = pst.executeQuery();
			if(result.next()) {
				accountId = result.getInt("account.accountId");
			}
			else return null;
			stat = con.createStatement();
			result = stat.executeQuery(selectRecommend + accountId +"' order by customListVideo.oorder ASC");
			while(result.next()) {
				if(check == 0 || check!=result.getInt("customListVideo.courselistId")) {
					homePage = new HomePage();
					homePage.setAccountId(result.getInt("recommendedResult.accountId"));
					homePage.setListName(result.getString("courselist.listName"));
					homePage.setSchoolName(result.getString("unit.schoolName"));
					homePage.setTeacher(result.getString("unit.teacher"));
					homePage.setCourselistId(result.getInt("courselist.courselistId"));
					homePage.setListLikes(result.getInt("courselist.likes"));
					homePage.setUnitId(result.getInt("unit.unitId"));
					homePage.setCreator(result.getString("courselist.creator"));
					if(result.getString("unit.videoImgSrc") == "") {
						homePage.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
					}
					else {
						homePage.setVideoImgSrc(result.getString("unit.videoImgSrc"));
					}
					
					homePage.setType(2);//代表推薦清單
					if(result.getString("unit.videoUrl").split("/")[2].equals("www.youtube.com")) {
						homePage.setVideoType(1);//youtube
					}
					else {
						homePage.setVideoType(2);//jwplayer
					}
					map.put(count, homePage);
					
					count++;
				}
				check = result.getInt("customListVideo.courselistId");
			} 
		}
		catch(SQLException x){
			System.out.println("HomePage-selectRecommendList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return map;
	}
	
	//找使用者的課程清單
	public Map<Integer, HomePage> selectVideoList(String userId){
		map = new HashMap<Integer, HomePage>();
		int count = 0;//map的key
		int max = 0;
		int check = 0;//0代表是第一次跑這個清單，不是0代表這個清單已經跑過，就不需要再存了
		try {
			//找oorder來當最大值
			stat = con.createStatement();
			//檢查清單列表有沒有清單
			result = stat.executeQuery("select MAX(oorder) from list where userId = '" + userId+"'");
			while(result.next()) {
				max = result.getInt("MAX(oorder)");
				//檢查有沒有資料
				if(max == 0)return null;
			}
			max = 0;
			//看有幾個清單是有內容的，有才可以顯示在首頁
			result = stat.executeQuery("select * from customListVideo,unit,list,courselist where "
					+"customListVideo.courselistId = list.courselistId and "
					+"customListVideo.unitId = unit.unitId and "
					+"list.courselistId = courselist.courselistId and "
					+"list.userId ='" + userId+"'");
			while(result.next()) {
				if(check == 0 || check!=result.getInt("customListVideo.courselistId")) {
					max++;
					check = result.getInt("customListVideo.courselistId");
				}
					
			}
			//將清單有影片的找出來
			result = stat.executeQuery("select * from customListVideo,unit,list,courselist where "
					+"customListVideo.courselistId = list.courselistId and "
					+"customListVideo.unitId = unit.unitId and "
					+"list.courselistId = courselist.courselistId and "
					+"list.userId ='" + userId+"'");
			count = 0;
			while(result.next()) {
				if(check == 0 || check!=result.getInt("customListVideo.courselistId")) {
					homePage = new HomePage();
					homePage.setUserId(result.getString("list.userId"));
					homePage.setListName(result.getString("courselist.listName"));
					homePage.setSchoolName(result.getString("unit.schoolName"));
					homePage.setTeacher(result.getString("unit.teacher"));
					homePage.setCourselistId(result.getInt("list.courselistId"));
					homePage.setListLikes(result.getInt("courselist.likes"));
					homePage.setUnitId(result.getInt("customListVideo.unitId"));
					homePage.setCreator(result.getString("courselist.creator"));
					homePage.setNum(max);
					homePage.setType(3);//代表課程清單
					if(result.getString("unit.videoImgSrc") == "") {
						homePage.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
					}
					else {
						homePage.setVideoImgSrc(result.getString("unit.videoImgSrc"));
					}
					if(result.getString("unit.videoUrl").split("/")[2].equals("www.youtube.com")) {
						homePage.setVideoType(1);//youtube
					}
					else {
						homePage.setVideoType(2);//jwplayer
					}
					map.put(count, homePage);
					count++;
				}
				check = result.getInt("customListVideo.courselistId");
			} 
		}
		catch(SQLException x){
			System.out.println("HomePage-selectVideoList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return map;
	}
	
	//找使用者的課程計畫中的"想要觀看"
	public Map<Integer, HomePage> selectCoursePlanWant(String userId){
		map = new HashMap<Integer, HomePage>();
		int count = 0;//map的key
		int max = 0;
		try {
			//找oorder來當最大值
			stat = con.createStatement();
			result = stat.executeQuery(selectPlanMax+" 1 and userId = '" + userId+"'");
			while(result.next()) {
				max = result.getInt("MAX(oorder)");
				//檢查有沒有資料
				if(max == 0)return null;
			}
			
			result = stat.executeQuery(selectPlan+ "1 and personalPlan.userId = '" + userId+"'");
			while(result.next()) {
				homePage = new HomePage();
				homePage.setUserId(result.getString("personalPlan.userId"));
				homePage.setUnitName(result.getString("unit.unitName"));
				homePage.setListName(result.getString("courselist.listName"));
				homePage.setSchoolName(result.getString("unit.schoolName"));
				homePage.setTeacher(result.getString("unit.teacher"));
				homePage.setCourselistId(result.getInt("courselist.courselistId"));
				homePage.setUnitLikes(result.getInt("unit.likes"));
				homePage.setUnitId(result.getInt("unit.unitId"));
				homePage.setCreator(result.getString("courselist.creator"));
				homePage.setNum(max);
				homePage.setType(4);//代表想要觀看
				if(result.getString("unit.videoImgSrc") == "") {
					homePage.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					homePage.setVideoImgSrc(result.getString("unit.videoImgSrc"));
				}
				if(result.getString("unit.videoUrl").split("/")[2].equals("www.youtube.com")) {
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
			System.out.println("HomePage-selectCoursePlanWant");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return map;
	}
	
	//找使用者的課程計畫中的"正在觀看"
	public Map<Integer, HomePage> selectCoursePlanING(String userId){
		map = new HashMap<Integer, HomePage>();
		int count = 0;//map的key
		int max = 0;
		try {
			//找oorder來當最大值
			stat = con.createStatement();
			result = stat.executeQuery(selectPlanMax+" 2 and userId = '" + userId+"'");
			while(result.next()) {
				max = result.getInt("MAX(oorder)");
				//檢查有沒有資料
				if(max == 0)return null;
			}
			
			result = stat.executeQuery(selectPlan+ "2 and personalPlan.userId = '" + userId+"'");
			while(result.next()) {
				homePage = new HomePage();
				homePage.setUserId(result.getString("personalPlan.userId"));
				homePage.setUnitName(result.getString("unit.unitName"));
				homePage.setListName(result.getString("courselist.listName"));
				homePage.setSchoolName(result.getString("unit.schoolName"));
				homePage.setTeacher(result.getString("unit.teacher"));
				homePage.setCourselistId(result.getInt("courselist.courselistId"));
				homePage.setUnitLikes(result.getInt("unit.likes"));
				homePage.setUnitId(result.getInt("unit.unitId"));
				homePage.setCreator(result.getString("courselist.creator"));
				homePage.setNum(max);
				homePage.setType(5);//代表想要觀看
				if(result.getString("unit.videoImgSrc") == "") {
					homePage.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					homePage.setVideoImgSrc(result.getString("unit.videoImgSrc"));
				}
				if(result.getString("unit.videoUrl").split("/")[2].equals("www.youtube.com")) {
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
			System.out.println("HomePage-selectCoursePlanING");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return map;
	}
	//將首頁的單元影片加入課程計畫中
	public void addToCoursePlan(String userId,int unitId,String creator){
		int maxOrder = 0;
		try {
			stat = con.createStatement();
			result = stat.executeQuery(selectPlanMax+" 1 and userId = '" + userId+"'");
			if(result.next())maxOrder = result.getInt("MAX(oorder)");
			pst = con.prepareStatement("insert ignore into personalPlan (userId,unitId,lastTime,status,oorder,creator) value(?,?,0,1,?,?)");
			pst.setString(1, userId);
			pst.setInt(2, unitId);
			pst.setInt(3, ++maxOrder);
			pst.setString(4, creator);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("HomePage-addToCoursePlan");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	//將首頁的清單中的所有單元影片加入課程計畫中
	public void addToCoursePlanList(String userId,int courselistId,String creator){
		homePages = new ArrayList<HomePage>();
		int maxOrder = 0;
		try {
			stat = con.createStatement();
			result = stat.executeQuery(selectPlanMax+" 1 and userId = '" + userId+"'");
			while(result.next()) {
				maxOrder = result.getInt("MAX(oorder)");
			}
			result = stat.executeQuery("select * from unit,customListVideo,courselist where unit.unitId"
					+ " = customListVideo.unitId and customListVideo.courselistId ="
					+ " courselist.courselistId and courselist.courselistId = "+ courselistId);
			while(result.next()) {
				homePage = new HomePage();
				homePage.setUnitId(result.getInt("unit.unitId"));
				homePages.add(homePage);
			}
			pst = con.prepareStatement("insert ignore into personalPlan (userId,unitId,lastTime,status,oorder,creator) value(?,?,0,1,?,?)");
			for(int i = 0;i < homePages.size();i++) {
				
				pst.setString(1, userId);
				pst.setInt(2, homePages.get(i).getUnitId());
				pst.setInt(3, ++maxOrder);
				pst.setString(4, creator);
				//先放到batch，等迴圈跑完再一次新增
				pst.addBatch();
			}
			pst.executeBatch();
		}
		catch(SQLException x){
			System.out.println("HomePage-addToCoursePlanList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	//取得該使用者所有清單
	public ArrayList<HomePage> getVideoListName(String userId){
		homePages = new ArrayList<HomePage>();
		try {
			pst = con.prepareStatement("select list.courselistId,list.oorder,courselist.listName from list,"
					+ "courselist where list.courselistId = courselist.courselistId and list.userId = ? "
					+ "and courselist.creator = ? order by list.oorder ASC");
			pst.setString(1,userId);
			pst.setString(2,userId);
			result = pst.executeQuery();
			while(result.next()) {
				homePage = new HomePage();
				homePage.setListName(result.getString("courselist.listName"));
				homePage.setCourselistId(result.getInt("list.courselistId"));
				homePages.add(homePage);
			}
		}
		catch(SQLException x){
			System.out.println("HomePage-getVideoListName");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return homePages;
	}
	
	//將單一影片加入個人課程清單中
	public void addToVideoList(int unitId,int courselistId){
		int maxOrder = 0;
		try {
			pst = con.prepareStatement("select MAX(oorder) from customListVideo where courselistId = ?");
			pst.setInt(1, courselistId);
			result = pst.executeQuery();
			if(result.next())maxOrder = result.getInt("MAX(oorder)");
			
			pst = con.prepareStatement("insert ignore into customListVideo (courselistId,unitId,oorder) value(?,?,?)");
			pst.setInt(1, courselistId);
			pst.setInt(2, unitId);
			pst.setInt(3, ++maxOrder);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("HomePage-addToVideoList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	//將首頁的清單整個加入課程清單中
	public void addToVideoListList(String userId,int courselistId){
		int maxOrder = 0;
		try {
			pst = con.prepareStatement("select MAX(oorder) from list where userId = ?");
			pst.setString(1, userId);
			result = pst.executeQuery();
			if(result.next())maxOrder = result.getInt("MAX(oorder)");
			pst = con.prepareStatement("insert into list (userId,courselistId,oorder) value(?,?,?)");
			pst.setString(1, userId);
			pst.setInt(2, courselistId);
			pst.setInt(3, ++maxOrder);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("HomePage-addToVideoList");
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
			System.out.println("HomePage Connection Close Error");
			System.out.println("Close Exception :" + e.toString()); 
		}
	}
}
