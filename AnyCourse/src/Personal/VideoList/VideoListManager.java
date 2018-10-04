package Personal.VideoList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

import HomePage.HomePage;


public class VideoListManager {

	private String deleteCourseListSQL = "delete from courselist where courselistId = ? and listName = ? and creator = ? ";
	private String deleteListSQL = "delete from list where userId = ? and courselistId = ? and oorder = ? ";

	private VideoList videoList;
	private UnitVideo unitVideo;
	private ArrayList<VideoList> videoLists;
	private ArrayList<UnitVideo> unitVideos;
	
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	private PreparedStatement pst2 = null;
	
	public VideoListManager() {
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
	//找該user的List
	public ArrayList<VideoList> selectVideoListTable(String userId) {
		videoLists = new ArrayList<VideoList>();
		try {
			pst = con.prepareStatement("select * from courselist,list where list.courselistId = "
					+ "courselist.courselistId and list.userId = ? order by list.oorder ASC");
			pst.setString(1, userId);
			result = pst.executeQuery();
			while(result.next()) {
				//選取該使用者的課程清單
				videoList = new VideoList();
				videoList.setUserId(userId);
				videoList.setCourselistId(result.getInt("list.courselistId"));
				videoList.setOorder(result.getInt("list.oorder"));
				videoList.setListName(result.getString("courselist.listName")); 
				videoList.setCreator(result.getString("courselist.creator"));
				videoList.setSchoolName(result.getString("courselist.schoolName"));
				videoLists.add(videoList);
			}
		}
		catch(SQLException x){
			System.out.println("VideoList-selectVideoListTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return videoLists;
	}
	
	//尋找對應的單元影片
	public ArrayList<UnitVideo> selectUnitTable(String userId,int courselistId) {
		unitVideos = new ArrayList<UnitVideo>();
		try {
			pst = con.prepareStatement("select * from customListVideo,unit,list,courselist where "
					+"customListVideo.courselistId = list.courselistId and "
					+"customListVideo.unitId = unit.unitId and "
					+"list.courselistId = courselist.courselistId and "
					+"list.userId = ? and "
					+"courselist.courselistId = ? order by customListVideo.oorder ASC");
			pst.setString(1, userId);
			pst.setInt(2, courselistId);
			result = pst.executeQuery();
			while(result.next()) {
				unitVideo = new UnitVideo();
				unitVideo.setUserId(result.getString("list.userId"));
				unitVideo.setUnitName(result.getString("unit.unitName"));
				unitVideo.setCourseInfo(result.getString("courselist.courseInfo"));
				unitVideo.setSchoolName(result.getString("unit.schoolName"));
				unitVideo.setCreator(result.getString("courselist.creator"));
				unitVideo.setTeacher(result.getString("unit.teacher"));
				if(result.getString("unit.videoImgSrc") == "") {
					unitVideo.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					unitVideo.setVideoImgSrc(result.getString("unit.videoImgSrc"));
				}
				unitVideo.setCourselistId(result.getInt("list.courselistId"));
				unitVideo.setOorder(result.getInt("customListVideo.oorder"));
				unitVideo.setLikes(result.getInt("unit.likes"));
				unitVideo.setUnitId(result.getInt("customListVideo.unitId"));
				if(result.getString("unit.videoUrl").split("/")[2].equals("www.youtube.com")) {
					unitVideo.setVideoType(1);//youtube
				}
				else {
					unitVideo.setVideoType(2);//jwplayer
				}
				unitVideos.add(unitVideo);
			} 
		     
		}
		catch(SQLException x){
			System.out.println("VideoList-selectUnitTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return unitVideos;
	}
	
	//新增清單
	public void insertCourseListTable(String userId,String listName) {
		int courselistId = 0;
		int listOrder = 0;
		try {
			//先存入courselist這個table
			pst = con.prepareStatement("insert into courselist (courselistId,listName"
					+ ",creator,share,likes) value(null,?,?,0,0)");
			pst.setString(1,listName);
			pst.setString(2,userId);
			pst.executeUpdate();
			
			//從courselist中抓自動生成的courselistId
			stat = con.createStatement();
			result = stat.executeQuery("select courselistId from courselist where creator = '"+userId+"' and "
					+ "listName = '"+listName+"'");
			while(result.next()) {
				courselistId = result.getInt("courselistId");
			}
			//去list中抓oorder欄位的最大值
			stat = con.createStatement();
			result = stat.executeQuery("select MAX(oorder) from list where userId = '"+userId+"'");
			while(result.next()) {
				listOrder = result.getInt("MAX(oorder)");
			}
			
			pst2 = con.prepareStatement("insert into list (userId,courselistId,oorder) value(?,?,?)");
			pst2.setString(1,userId);
			pst2.setInt(2,courselistId);
			//把抓到的oorder最大值+1
			pst2.setInt(3,listOrder+1);
			pst2.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("VideoList-insertCourseListTable");
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	//刪除指定搜尋資料
	public void deleteCourseListTable(VideoList videoList) {
		int check = 0;//0 = 要刪除的list不是他本人創建的。1 = 要刪除的lost是本人創建的
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from courselist where courselistId =" + videoList.getCourselistId());
			while(result.next()) {
				if(result.getString("creator").equals(videoList.getUserId())) {
					check = 1;
				}
			}
			//不是本人創建的，因此只刪除list的資料
			if(check == 0) {
				pst = con.prepareStatement(deleteListSQL);
				pst.setString(1,videoList.getUserId());
				pst.setInt(2,videoList.getCourselistId());
				pst.setInt(3,videoList.getOorder());
				pst.executeUpdate();
			}
			//本人創建的，因此要同時刪除courselist及list的資料
			else if (check == 1) {
				pst = con.prepareStatement(deleteCourseListSQL);
				pst.setInt(1,videoList.getCourselistId());
				pst.setString(2,videoList.getListName());
				pst.setString(3,videoList.getCreator());
				pst.executeUpdate();
				
				pst2 = con.prepareStatement(deleteListSQL);
				pst2.setString(1,videoList.getUserId());
				pst2.setInt(2,videoList.getCourselistId());
				pst2.setInt(3,videoList.getOorder());
				pst2.executeUpdate();
			}
		}
		catch(SQLException x){
			System.out.println("VideoList-deleteCourseListTable");
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}
	//編輯清單
	public void updateCourseListTable(VideoList videoList){
		try {
			//1:listName。2:courselistId。3:creator
			pst = con.prepareStatement("update courselist set listName = ? where courselistId = ? and creator = ? ");
			pst.setInt(2,videoList.getCourselistId());
			pst.setString(3,videoList.getCreator());
			pst.setString(1,videoList.getListName());
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("VideoList-updateCourseListTable");
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
	}
	//刪除使用者點擊的清單中的影片
	public void deleteUnitVideo(int courselistId, int unitId) {
		try {
			//1:listName。2:courselistId。3:creator
			pst = con.prepareStatement("delete from customListVideo where courselistId = ? and unitId = ?");
			pst.setInt(1,courselistId);
			pst.setInt(2,unitId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("VideoList-updateCourseListTable");
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	//將整個清單的影片加入至課程計畫
	public void addToCoursePlanList(String userId,int courselistId,String creator) {
		unitVideos = new ArrayList<UnitVideo>();
		int maxOrder = 0;
		try {
			pst = con.prepareStatement("select Max(oorder) from personalPlan where status = 1 and "
					+ "userId = ?");
			pst.setString(1, userId);
			result = pst.executeQuery();
			if(result.next())maxOrder = result.getInt("MAX(oorder)");
			
			pst = con.prepareStatement("select unitId from customListVideo where courselistId = ?");
			pst.setInt(1, courselistId);
			result = pst.executeQuery();
			
			while(result.next()) {
				unitVideo = new UnitVideo();
				unitVideo.setUnitId(result.getInt("unitId"));
				unitVideos.add(unitVideo);
			}
			pst = con.prepareStatement("insert ignore into personalPlan (userId,unitId,lastTime,status,oorder,creator) value(?,?,0,1,?,?)");
			for(int i = 0;i < unitVideos.size();i++) {
				
				pst.setString(1, userId);
				pst.setInt(2, unitVideos.get(i).getUnitId());
				pst.setInt(3, ++maxOrder);
				pst.setString(4, creator);
				//先放到batch，等迴圈跑完再一次新增
				pst.addBatch();
			}
			pst.executeBatch();
		}
		catch(SQLException x){
			System.out.println("VideoList-addToCoursePlanList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	//分享完整清單的影片給所有人
	public void shareVideoList(String userId,int courselistId) {
		try {
			pst = con.prepareStatement("update courselist set share = ? where courselistId = ? and userId = ? ");
			pst.setInt(1, 1);
			pst.setInt(2, courselistId);
			pst.setString(3, userId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("VideoList-shareVideoList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	//分享完整清單的影片給所有人
	public String getAllGroup(String userId) {
		videoLists = new ArrayList<VideoList>();
		try {
			pst = con.prepareStatement("select ggroup.groupId,ggroup.groupName from groupMember,ggroup where "
					+ "groupMember.groupId = ggroup.groupId and groupMember.userId = ?");
			pst.setString(1, userId);
			result = pst.executeQuery();
			while(result.next()) {
				videoList = new VideoList();
				videoList.setGroupId(result.getInt("ggroup.groupId"));
				videoList.setGroupName(result.getString("ggroup.groupName"));
				videoLists.add(videoList);
			}
			
		}
		catch(SQLException x){
			System.out.println("VideoList-getAllGroup");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return new Gson().toJson(videoLists);
	}
	
	//分享完整清單的影片給所有人
	public void shareVideoToGroup(String userId,int courselistId,int groupId) {
		videoLists = new ArrayList<VideoList>();
		int maxOrder = 0;
		try {
			pst = con.prepareStatement("select Max(oorder) from groupList where groupId = ?");
			pst.setInt(1, groupId);
			result = pst.executeQuery();
			if(result.next())maxOrder = result.getInt("MAX(oorder)");
			
			pst = con.prepareStatement("insert ignore into groupList (groupId,courselistId,oorder) value(?,?,?)");
			pst.setInt(1, groupId);
			pst.setInt(2, courselistId);
			pst.setInt(3, ++maxOrder);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("VideoList-shareVideoToGroup");
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
			System.out.println("VideoList Close Error");
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
			System.out.println("VideoList Connection Close Error");
			System.out.println("Close Exception :" + e.toString()); 
		}
	}
}
