package Personal.VideoList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class VideoListManager {

	private String deleteCourseListSQL = "delete from courselist where courselist_id = ? and list_name = ? and creator = ? ";
	private String deleteListSQL = "delete from list where user_id = ? and courselist_id = ? and oorder = ? ";

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
	public ArrayList<VideoList> selectVideoListTable(String userID) {
		videoLists = new ArrayList<VideoList>();
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from courselist,list where courselist.courselist_id "
					+ "= list.courselist_id and list.user_id = '"+userID+"' order by list.oorder ASC");
			while(result.next()) {
				//選取該使用者的課程清單
				videoList = new VideoList();
				videoList.setUserID(result.getString("list.user_id"));
				videoList.setCourselistID(result.getInt("list.courselist_id"));
				videoList.setOorder(result.getInt("list.oorder"));
				videoList.setListName(result.getString("courselist.list_name")); 
				videoList.setCreator(result.getString("courselist.creator"));
				videoList.setSchoolName(result.getString("courselist.school_name"));
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
	public ArrayList<UnitVideo> selectUnitTable(String userID,String schoolName
			,String listName) {
		unitVideos = new ArrayList<UnitVideo>();
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from customlist_video,unit,list,courselist where "
					+"customlist_video.courselist_id = list.courselist_id and "
					+"customlist_video.unit_id = unit.unit_id and "
					+"list.courselist_id = courselist.courselist_id and "
					+"list.user_id = '"+userID+"' and "
					+"courselist.school_name = '"+schoolName+"' and "
					+"courselist.list_name = '"+listName+"' order by customlist_video.oorder ASC");
			while(result.next()) {
				unitVideo = new UnitVideo();
				unitVideo.setUserID(result.getString("list.user_id"));
				unitVideo.setUnitName(result.getString("unit.unit_name"));
				unitVideo.setCourseInfo(result.getString("courselist.course_info"));
				unitVideo.setSchoolName(result.getString("courselist.school_name"));
				unitVideo.setTeacher(result.getString("courselist.teacher"));
				unitVideo.setVideoImgSrc(result.getString("unit.video_img_src"));
				unitVideo.setCourselistID(result.getInt("list.courselist_id"));
				unitVideo.setOorder(result.getInt("customlist_video.oorder"));
				unitVideo.setLikes(result.getInt("unit.likes"));
				unitVideo.setUnitID(result.getInt("customlist_video.unit_id"));
				if(result.getString("unit.video_url").split("/")[2].equals("www.youtube.com")) {
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
	public void insertCourseListTable(String userID,String listName) {
		int courselistID = 0;
		int listOrder = 0;
		try {
			//先存入courselist這個table
			pst = con.prepareStatement("insert into courselist (courselist_id,list_name"
					+ ",creator,share,likes) value(null,?,?,?,?)");
			pst.setString(1,listName);
			pst.setString(2,userID);
			pst.setInt(3,0);
			pst.setInt(4,0);
			pst.executeUpdate();
			
			//從courselist中抓自動生成的courselist_id
			stat = con.createStatement();
			result = stat.executeQuery("select courselist_id from courselist where creator = '"+userID+"' and "
					+ "list_name = '"+listName+"'");
			while(result.next()) {
				courselistID = result.getInt("courselist_id");
			}
			//去list中抓oorder欄位的最大值
			stat = con.createStatement();
			result = stat.executeQuery("select MAX(oorder) from list where user_id = '"+userID+"'");
			while(result.next()) {
				listOrder = result.getInt("MAX(oorder)");
			}
			
			pst2 = con.prepareStatement("insert into list (user_id,courselist_id,oorder) value(?,?,?)");
			pst2.setString(1,userID);
			pst2.setInt(2,courselistID);
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
			result = stat.executeQuery("select * from courselist where courselist_id =" + videoList.getCourselistID());
			while(result.next()) {
				if(result.getString("creator").equals(videoList.getUserID())) {
					check = 1;
				}
			}
			//不是本人創建的，因此只刪除list的資料
			if(check == 0) {
				pst = con.prepareStatement(deleteListSQL);
				pst.setString(1,videoList.getUserID());
				pst.setInt(2,videoList.getCourselistID());
				pst.setInt(3,videoList.getOorder());
				pst.executeUpdate();
			}
			//本人創建的，因此要同時刪除courselist及list的資料
			else if (check == 1) {
				pst = con.prepareStatement(deleteCourseListSQL);
				pst.setInt(1,videoList.getCourselistID());
				pst.setString(2,videoList.getListName());
				pst.setString(3,videoList.getCreator());
				pst.executeUpdate();
				
				pst2 = con.prepareStatement(deleteListSQL);
				pst2.setString(1,videoList.getUserID());
				pst2.setInt(2,videoList.getCourselistID());
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
			//1:list_name。2:courselist_id。3:creator
			pst = con.prepareStatement("update courselist set list_name = ? where courselist_id = ? and creator = ? ");
			pst.setInt(2,videoList.getCourselistID());
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
