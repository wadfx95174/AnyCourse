package PlayerInterface;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.google.gson.Gson;

import RecommenderSystem.RecommendationResult;

public class PlayerInterfaceManager
{
	private String selectVideoUrlSQL = "select * from unit,courselist,customListVideo where unit.unitId = ? "
			+ "and unit.unitId = customListVideo.unitId and "
			+ "customListVideo.courselistId = courselist.courselistId";
	private String selectCourseListSQL = "select * from unit natural join customListVideo where courselistId = ?";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public PlayerInterfaceManager() {
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
	
	public String getUnitName(int unitId) {
		String unitName = null;
		try {
			pst = con.prepareStatement("select unitName from unit where unitId = ?");
			pst.setInt(1, unitId);
			result = pst.executeQuery();
			while(result.next()) 
			{
				unitName = result.getString("unitName");
			}
		}
		catch(SQLException x){
			System.out.println("PlayerInterfaceManager-getUnitName");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		
		return unitName;
	}
	
	public String getVideoUrl(int unitId) {
		Unit unit = null;
		try {
			pst = con.prepareStatement(selectVideoUrlSQL);
			pst.setInt(1, unitId);
			result = pst.executeQuery();
			
			while(result.next()) 
			{ 	
				unit = new Unit();
				unit.setUnitId(result.getInt("unit.unitId"));
				unit.setUnitName(result.getString("unit.unitName"));
				unit.setListName(result.getString("courselist.listName"));
				unit.setSchoolName(result.getString("courselist.schoolName"));
				unit.setLikes(result.getInt("unit.likes"));
				unit.setVideoImgSrc(result.getString("unit.videoImgSrc"));
				unit.setVideoUrl(result.getString("unit.videoUrl"));
				unit.setCourseInfo(result.getString("courselist.courseInfo"));
			}
		}
		catch(SQLException x){
			System.out.println("PlayerInterfaceManager-getVideoUrl");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		Gson gson = new Gson();
		return gson.toJson(unit);
	}
	//取得完整清單中的單元影片
	public String getList(int courselistId) {
		ArrayList<Unit> units = new ArrayList<>(); 
		try {
			pst = con.prepareStatement(selectCourseListSQL);
			pst.setInt(1, courselistId);
			result = pst.executeQuery();
			while(result.next()) 
			{ 	
				Unit unit = new Unit();
				unit.setUnitId(result.getInt("unitId"));
				unit.setUnitName(result.getString("unitName"));
				unit.setListName(result.getString("listName"));
				unit.setSchoolName(result.getString("schoolName"));
				unit.setLikes(result.getInt("likes"));
				unit.setVideoImgSrc(result.getString("videoImgSrc"));
				unit.setVideoUrl(result.getString("videoUrl"));
				units.add(unit);
			}
		}
		catch(SQLException x){
			System.out.println("PlayerInterfaceManager-getList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		Gson gson = new Gson();
		return gson.toJson(units);
	}
	//設定影片的結束時間
	//watchRecord的watchTime是存入資料時資料庫自動抓現在的時間
	//如果該影片有存在在使用者的課程計畫中，則personalPlan的lastTime也要update，並且判斷要不要改status
	//設定shareLikes	的觀看紀錄，將isBrowse+1
	public void setVideoCloseTime(int currentTime,int unitId,String userId,int duration) {
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from watchRecord where userId = '"+userId
					+"' and unitId = "+unitId);
			boolean check = false;//檢查watchRecord裡面有沒有這筆單元影片的資料，false為沒有
			if(result.next())check = true;
			//如果沒有，就塞資料
			if(check == false) {
				pst = con.prepareStatement("insert ignore into watchRecord (userId,unitId,watchTime) value(?,?,null)");
				pst.setString(1,userId);
				pst.setInt(2,unitId);
			}
			//有就更新資料
			else {
				pst = con.prepareStatement("update watchRecord set watchTime = null where userId = ? and unitId = ? ");
				pst.setString(1,userId);
				pst.setInt(2,unitId);
			}
			pst.executeUpdate();
			
			////////////////////////////////////////////////////////////////////////////////////////////
			check = false;//檢查有沒有在課程計畫的table中找到這個unitId
			int maxOrder = 0,statusInit = 1,status = 1;
			//檢查有沒有存在在使用者的課程計畫中
			pst = con.prepareStatement("select status from personalPlan where userId = ? and unitId = ?");
			pst.setString(1, userId);
			pst.setInt(2, unitId);
			result = pst.executeQuery();
			if(result.next()) {
				check = true;
				statusInit = result.getInt("status");
			}
			
			
			//如果有，就更新影片結束時間，true是有，沒有就不做事
			if(check == true) {
				if(currentTime+5 > duration)status = 3;
				//改變status為正在觀看
				else status = 2;
				//找order最大值
				pst = con.prepareStatement("select MAX(oorder) from personalPlan where status = ? "
						+ "and userId = ?");
				pst.setInt(1, status);
				pst.setString(2, userId);
				result = pst.executeQuery();
				if(result.next()) {
					//判斷狀態有無改變
					if(statusInit == status) {
						maxOrder = result.getInt("MAX(oorder)");
					}
					else
						maxOrder = result.getInt("MAX(oorder)") + 1;
				}
				
				//更新該影片所屬的狀態列表
				pst = con.prepareStatement("update personalPlan set lastTime = ?,status = ?,oorder = ? "
						+ "where userId = ? and unitId = ?");
				pst.setInt(1,currentTime);
				//判斷結束了沒，currentTime+5秒代表5秒內會結束的都改變status為已觀看完
				pst.setInt(2,status);
				pst.setInt(3,maxOrder);
				pst.setString(4,userId);
				pst.setInt(5, unitId);
				
				pst.executeUpdate();
			}
		}
		catch(SQLException x){
			System.out.println("PlayerInterfaceManager-setVideoCloseTime");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	//設定影片的結束時間，存入groupPlan
	//如果該影片有存在在使用者的課程計畫中，則personalPlan的lastTime也要update，並且判斷要不要改status
	//設定shareLikes	的觀看紀錄，將isBrowse+1
	public void setGroupVideoCloseTime(int currentTime,int unitId,String userId,int duration,int groupId) {
		try {

			boolean check = false;//檢查watchRecord裡面有沒有這筆單元影片的資料，false為沒有
			
			pst = con.prepareStatement("select * from watchRecord where userId = ? and unitId = ?");
			pst.setString(1, userId);
			pst.setInt(2, unitId);
			result = pst.executeQuery();
			if(result.next())check = true;
			
			//如果沒有，就塞資料
			if(check == false) {
				pst = con.prepareStatement("insert ignore into watchRecord (userId,unitId,watchTime) value(?,?,null)");
				pst.setString(1,userId);
				pst.setInt(2,unitId);
			}
			//有就更新資料
			else {
				pst = con.prepareStatement("update watchRecord set watchTime = null where userId = ? and unitId = ? ");
				pst.setString(1,userId);
				pst.setInt(2,unitId);
			}
			pst.executeUpdate();
			
			check = false;//檢查groupPlan有無這個unitId
			int maxOrder = 0,statusInit = 1,status = 1;
			
			//檢查此unit是否存在在groupPlan
			pst = con.prepareStatement("select status from groupPlan where userId = ? and unitId = ? "
					+ "and groupId = ?");
			pst.setString(1, userId);
			pst.setInt(2, unitId);
			pst.setInt(3, groupId);
			result = pst.executeQuery();
			if(result.next()) {
				check = true;
				statusInit = result.getInt("status");
			}
			
			//如果有，就更新影片結束時間，true是有，沒有就不做事
			if(check == true) {
				if(currentTime+5 > duration)status = 3;
				//改變status為正在觀看
				else status = 2;
				
				//找order最大值
				pst = con.prepareStatement("select MAX(oorder) from groupPlan where status = ? "
						+ "and userId = ? and groupId = ?");
				pst.setInt(1, status);
				pst.setString(2, userId);
				pst.setInt(3, groupId);
				result = pst.executeQuery();
				if(result.next()) {
					//判斷狀態有無改變
					if(statusInit == status) {
						maxOrder = result.getInt("MAX(oorder)");
					}
					else
						maxOrder = result.getInt("MAX(oorder)") + 1;
				}
				
				//更新該影片所屬的狀態列表
				pst = con.prepareStatement("update groupPlan set lastTime = ?,status = ?,oorder = ? "
						+ "where userId = ? and unitId = ? and groupId = ?");
				pst.setInt(1,currentTime);
				//判斷結束了沒，currentTime+5秒代表5秒內會結束的都改變status為已觀看完
				pst.setInt(2,status);
				pst.setInt(3,maxOrder);
				pst.setString(4,userId);
				pst.setInt(5, unitId);
				pst.setInt(6, groupId);
				
				pst.executeUpdate();
			}
		}
		catch(SQLException x){
			System.out.println("PlayerInterfaceManager-setGroupVideoCloseTime");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	//按讚時，有兩個table需要更改
	//1.shareLikes：用於推薦系統
	//2.unit：該影片的總按讚數
	public void addLike(String userId) {
		
	}
	
	
	//進入播放介面時，先設定isBrowse，以此來判斷有沒有看過讚
	public Unit setIsBrowse(String userId,int unitId) {
		Unit unit = null;
		try {
			//更新shareLikes的isBrowse
			stat = con.createStatement();
			result = stat.executeQuery("select * from shareLikes where userId = '"+userId
					+"' and unitId = "+unitId);
			boolean check = false;//檢查watchRecord裡面有沒有這筆單元影片的資料，false為沒有
			while(result.next()) {
				check = true;
				}
			//如果沒有，就塞資料
			if(check == false) {
				pst = con.prepareStatement("insert into shareLikes (userId,unitId,isShare,isLike"
						+ ",isAddToCourseList,isAddToCoursePlan,isBrowse) value(?,?,0,0,0,0,1)");
				pst.setString(1,userId);
				pst.setInt(2,unitId);
			}
			//有就更新資料
			else {
				pst = con.prepareStatement("update shareLikes set isBrowse = isBrowse + 1 where userId = ? and unitId = ? and isBrowse < 6");
				pst.setString(1,userId);
				pst.setInt(2,unitId);
			}
			pst.executeUpdate();
			
			result = stat.executeQuery("select * from shareLikes where userId = '"+userId
					+"' and unitId = "+unitId);
			while(result.next()) {
				unit = new Unit();
				unit.setPersonalLike(result.getInt("isLike"));
				
			}
		}
		catch(SQLException x) {
			System.out.println("PlayerInterfaceManager-setIsBrowse");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return unit;
	}
	
	
	//按讚，更新2個選取1個
	//更新1:unit的likes
	//更新2:shareLikes的isLike
	public void setLike(String userId,int unitId,int like) {
		try {
			//按讚
			if(like == 1) {
				pst = con.prepareStatement("update unit set likes = likes + 1 where unitId = ?");
				pst.setInt(1, unitId);
				pst.executeUpdate();
				
				pst = con.prepareStatement("update shareLikes set isLike = isLike + 1 where userId = ? and unitId = ? and isLike < 1");
				pst.setString(1,userId);
				pst.setInt(2, unitId);
				pst.executeUpdate();
			}
			//收回讚
			else if(like == 0) {
				pst = con.prepareStatement("update unit set likes = likes - 1 where unitId = ? and likes > 0");
				pst.setInt(1, unitId);
				pst.executeUpdate();
				pst = con.prepareStatement("update shareLikes set isLike = isLike - 1 where userId = ? and unitId = ? and isLike > 0");
				pst.setString(1,userId);
				pst.setInt(2, unitId);
				pst.executeUpdate();
			}
			
		}
		catch(SQLException x) {
			System.out.println("PlayerInterfaceManager-setLike");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	//拿accountId
	public int getAccountId(String userId) {
		int accountId = 0;
		try {
			pst = con.prepareStatement("select account.accountId from rating,account where "
					+ "account.accountId = rating.accountId and account.userId = ?");
			pst.setString(1,userId);
			result = pst.executeQuery();
			while(result.next()) {
				accountId = result.getInt("account.accountId");
			}
		}
		catch(SQLException x) {
			System.out.println("PlayerInterfaceManager-getAccountId");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return accountId;
	}
	
	//拿推薦給該使用者的影片
	public ArrayList<Unit> getRecommendList(int accountId, long unitId) throws IOException,TasteException{
		ArrayList<Unit> units = new ArrayList<Unit>();
		Unit unit = null;
		try {
			try {
				List<RecommendedItem> test = RecommendationResult.designatedItemBooleanRecommendedResult(
						(long)accountId,unitId,10);
				pst = con.prepareStatement("select * from unit,courselist,customListVideo where "
						+ "unit.unitId = customListVideo.unitId and customListVideo.courselistId"
						+ " = courselist.courselistId and unit.unitId = ?");
				for(RecommendedItem r: test) {
					pst.setInt(1,(int)r.getItemID());
					result = pst.executeQuery();
					while(result.next()) {
						unit = new Unit();
						unit.setUnitName(result.getString("unit.unitName"));
						unit.setUnitId(result.getInt("unit.unitId"));
						unit.setVideoImgSrc(result.getString("unit.videoImgSrc"));
						unit.setTeacher(result.getString("courselist.teacher"));
						unit.setSchoolName(result.getString("courselist.schoolName"));
						unit.setLikes(result.getInt("unit.likes"));
						if(result.getString("unit.videoUrl").split("/")[2].equals("www.youtube.com")) {
							unit.setType(1);//youtube
						}
						else {
							unit.setType(2);//jwplayer
						}
						units.add(unit);
					}
		    	}
			} catch (TasteException e) {
				e.printStackTrace();
			}	
		}
		catch(SQLException x) {
			System.out.println("PlayerInterfaceManager-getRecommendList");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return units;
		
	}
	
	public void setBrowse(int accountId,int unitId) {
		
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from rating where accountId = '"+accountId
					+"' and unitId = "+unitId);
			boolean check = false;
			
			if(result.next())check = true;
			//如果沒有，就塞資料
			if(check == false) {
				pst = con.prepareStatement("insert into rating (accountId,unitId,score) value(?,?,4)");
				pst.setInt(1,accountId);
				pst.setInt(2,unitId);
				pst.executeUpdate();
			}
		}
		catch(SQLException x) {
			System.out.println("PlayerInterfaceManager-setBrowse");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	//把該影片的其他清單加入recommendedResult，如果某個影片已經存在，則把該影片的分數提高
	public void setRecommendedResult(int accountId ,int unitId) {
		try {
			boolean check = false;
			
			pst = con.prepareStatement("select * from recommendedResult where accountId = ? and unitId = ?");
			pst.setInt(1,accountId);
			pst.setInt(2,unitId);
			result = pst.executeQuery();
			if(result.next())check = true;
			//該影片沒有出現在recommendedResult中
			if(check == false) {
				pst = con.prepareStatement("insert into recommendedResult (accountId,unitId,recommendedScore) value(?,?,4)");
				pst.setInt(1,accountId);
				pst.setInt(2,unitId);
				pst.executeUpdate();
			}
			//該影片有出現在recommendedResult中，將recommendedResult+1
			else {
				pst = con.prepareStatement("update recommendedResult set recommendedScore = recommendedScore + 1 where accountId = ? and unitId = ?");
				pst.setInt(1, accountId);
				pst.setInt(2, unitId);
				pst.executeUpdate();
			}
		}
		catch(SQLException x) {
			System.out.println("PlayerInterfaceManager-setRecommendedResult");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	//取得講義
	public Unit getLecture(int unitId) {
		Unit unit = new Unit();
		try {
			pst = con.prepareStatement("select lectureName,lectureUrl from unitLecture where unitId = ?");
			pst.setInt(1,unitId);
			result = pst.executeQuery();
			if(result.next()) {
				unit.setLectureName(result.getString("lectureName"));
				unit.setLectureUrl(result.getString("lectureUrl"));
			}
		}
		catch(SQLException x) {
			System.out.println("PlayerInterfaceManager-getLecture");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return unit;
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
			System.out.println("PlayerInterfaceManager Close Exception :" + e.toString()); 
		}		
	} 
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("PlayerInterfaceManager Connection Close Exception :" + e.toString()); 
		}
	}
}
