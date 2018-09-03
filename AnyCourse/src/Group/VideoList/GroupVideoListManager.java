package Group.VideoList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

import Personal.VideoList.UnitVideo;
import Personal.VideoList.VideoList;

public class GroupVideoListManager {
	
	private GroupVideoList groupVideoList;
	private ArrayList<GroupVideoList> groupVideoLists;

	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	
	public GroupVideoListManager() {
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

	//獲取該groupId的List
	public String getList(int groupId, String userId) {
		groupVideoLists = new ArrayList<GroupVideoList>();
		try {
			pst = con.prepareStatement("select * from courselist,groupList where "
					+ "courselist.courselistId = groupList.courselistId and groupList.groupId = ? "
					+ "order by groupList.oorder ASC");//ASC小到大
			pst.setInt(1, groupId);
			result = pst.executeQuery();
			while(result.next()) {
				//選取該使用者的課程清單
				groupVideoList = new GroupVideoList();
				groupVideoList.setUserId(userId);
				groupVideoList.setCourselistId(result.getInt("groupList.courselistId"));
				groupVideoList.setOorder(result.getInt("groupList.oorder"));
				groupVideoList.setListName(result.getString("courselist.listName")); 
				groupVideoList.setCreator(result.getString("courselist.creator"));
				groupVideoList.setSchoolName(result.getString("courselist.schoolName"));
				groupVideoLists.add(groupVideoList);
			}
		}
		catch(SQLException x){
			System.out.println("GroupVideoListManager-getList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return new Gson().toJson(groupVideoLists);
	}
	
	//尋找對應的單元影片
	public String getUnit(int groupId, String userId, int courselistId) {
		groupVideoLists = new ArrayList<GroupVideoList>();
		try {
			pst = con.prepareStatement("select * from customListVideo,unit,groupList,courselist where "
					+"customListVideo.courselistId = groupList.courselistId and "
					+"customListVideo.unitId = unit.unitId and "
					+"groupList.courselistId = courselist.courselistId and groupList.groupId = ? and "
					+"courselist.courselistId = ? order by customListVideo.oorder ASC");
			pst.setInt(1, groupId);
			pst.setInt(2, courselistId);
			result = pst.executeQuery();
			while(result.next()) {
				groupVideoList = new GroupVideoList();
				groupVideoList.setUserId(userId);
				groupVideoList.setUnitName(result.getString("unit.unitName"));
				groupVideoList.setCourseInfo(result.getString("courselist.courseInfo"));
				groupVideoList.setSchoolName(result.getString("unit.schoolName"));
				groupVideoList.setCreator(result.getString("courselist.creator"));
				groupVideoList.setTeacher(result.getString("unit.teacher"));
				if(result.getString("unit.videoImgSrc") == "") {
					groupVideoList.setVideoImgSrc("https://i.imgur.com/eKSYvRv.png");
				}
				else {
					groupVideoList.setVideoImgSrc(result.getString("unit.videoImgSrc"));
				}
				groupVideoList.setCourselistId(result.getInt("groupList.courselistId"));
				groupVideoList.setOorder(result.getInt("customListVideo.oorder"));
				groupVideoList.setUnitLikes(result.getInt("unit.likes"));
				groupVideoList.setUnitId(result.getInt("customListVideo.unitId"));
				if(result.getString("unit.videoUrl").split("/")[2].equals("www.youtube.com")) {
					groupVideoList.setVideoType(1);//youtube
				}
				else {
					groupVideoList.setVideoType(2);//jwplayer
				}
				groupVideoLists.add(groupVideoList);
			} 
		     
		}
		catch(SQLException x){
			System.out.println("GroupVideoListManager-getUnit");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return new Gson().toJson(groupVideoLists);
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
