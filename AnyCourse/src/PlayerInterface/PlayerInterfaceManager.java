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
	private String selectVideoUrlSQL = "select * from unit,courselist,customlistVideo where unit.unitID = ? "
			+ "and unit.unitID = customlistVideo.unitID and "
			+ "customlistVideo.courselistID = courselist.courselistID";
	private String selectCourseListSQL = "select * from unit natural join customlistVideo where courselistID = ?";
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
	
	public String getVideoUrl(int unitID) {
		Unit unit = null;
		try {
			pst = con.prepareStatement(selectVideoUrlSQL);
			pst.setInt(1, unitID);
			result = pst.executeQuery();
			
			while(result.next()) 
			{ 	
				unit = new Unit();
				unit.setUnitID(result.getInt("unit.unitID"));
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
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		Gson gson = new Gson();
		return gson.toJson(unit);
	}
	//取得完整清單中的單元影片
	public String getList(int courselistID) {
		ArrayList<Unit> units = new ArrayList<>(); 
		try {
			pst = con.prepareStatement(selectCourseListSQL);
			pst.setInt(1, courselistID);
			result = pst.executeQuery();
			while(result.next()) 
			{ 	
				Unit unit = new Unit();
				unit.setUnitID(result.getInt("unitID"));
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
	public void setVideoEndTime(int currentTime,int unitID,String userID,int duration) {
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from watchRecord where userID = '"+userID
					+"' and unitID = "+unitID);
			boolean check = false;//檢查watchRecord裡面有沒有這筆單元影片的資料，false為沒有
			System.out.println("currentTime: " + currentTime);
			System.out.println("duration: " + duration);
			while(result.next()) {check = true;}
			//如果沒有，就塞資料
			if(check == false) {
				pst = con.prepareStatement("insert ignore into watchRecord (userID,unitID,watchTime) value(?,?,null)");
				pst.setString(1,userID);
				pst.setInt(2,unitID);
			}
			//有就更新資料
			else {
				pst = con.prepareStatement("update watchRecord set watchTime = null where userID = ? and unitID = ? ");
				pst.setString(1,userID);
				pst.setInt(2,unitID);
			}
			pst.executeUpdate();
			
			//檢查有沒有存在在使用者的課程計畫中
			stat = con.createStatement();
			result = stat.executeQuery("select unitID from personalPlan where userID = '"+userID
					+"' and unitID = "+unitID);
			check = false;//檢查有沒有在課程計畫的table中找到這個unitID
			while(result.next()) {check = true;}
			//如果有，就更新影片結束時間，true是有，沒有就不做事
			if(check == true) { 
				
				//判斷結束了沒，currentTime+5秒代表5秒內會結束的都改變status
				if(currentTime+5 > duration) {
					pst = con.prepareStatement("update personalPlan set lastTime = ?,status = ? where userID = ? and unitID = ? ");
					pst.setInt(1,currentTime);
					pst.setInt(2,3);
					pst.setString(3,userID);
					System.out.println("coursePlan");
					pst.setInt(4, unitID);
				}
				else {
					pst = con.prepareStatement("update personalPlan set lastTime = ?,status = ? where userID = ? and unitID = ? ");
					pst.setInt(1,currentTime);
					pst.setInt(2,2);
					pst.setString(3,userID);
					pst.setInt(4, unitID);
					
				}
				pst.executeUpdate();
			}
			
			pst.executeUpdate();
			
		}
		catch(SQLException x){
			System.out.println("end");
		System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	//按讚時，有兩個table需要更改
	//1.shareLikes：用於推薦系統
	//2.unit：該影片的總按讚數
	public void addLike(String userID) {
		
	}
	
	
	//進入播放介面時，先設定isBrowse，以此來判斷有沒有看過讚
	public Unit setIsBrowse(String userID,int unitID) {
		int like = 0;
		Unit unit = null;
		try {
			//更新shareLikes的isBrowse
			stat = con.createStatement();
			result = stat.executeQuery("select * from shareLikes where userID = '"+userID
					+"' and unitID = "+unitID);
			boolean check = false;//檢查watchRecord裡面有沒有這筆單元影片的資料，false為沒有
			while(result.next()) {
				check = true;
				}
			//如果沒有，就塞資料
			if(check == false) {
				pst = con.prepareStatement("insert into shareLikes (userID,unitID,isShare,isLike"
						+ ",isAddToCourseList,isAddToCoursePlan,isBrowse) value(?,?,0,0,0,0,1)");
				pst.setString(1,userID);
				pst.setInt(2,unitID);
			}
			//有就更新資料
			else {
				pst = con.prepareStatement("update shareLikes set isBrowse = isBrowse + 1 where userID = ? and unitID = ? and isBrowse < 6");
				pst.setString(1,userID);
				pst.setInt(2,unitID);
			}
			pst.executeUpdate();
			
			result = stat.executeQuery("select * from shareLikes where userID = '"+userID
					+"' and unitID = "+unitID);
			while(result.next()) {
				unit = new Unit();
				unit.setPersonalLike(result.getInt("isLike"));
				
			}
		}
		catch(SQLException x) {
			System.out.println("setIsBrowse");
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
	public Unit setLike(String userID,int unitID,int like) {
		Unit unit = new Unit();
		try {
			//按讚
			if(like == 1) {
				pst = con.prepareStatement("update unit set likes = likes + 1 where unitID = ?");
				pst.setInt(1, unitID);
				pst.executeUpdate();
				
				pst = con.prepareStatement("update shareLikes set isLike = isLike + 1 where userID = ? and unitID = ? and isLike < 1");
				pst.setString(1,userID);
				pst.setInt(2, unitID);
				pst.executeUpdate();
				
				stat = con.createStatement();
				result = stat.executeQuery("select * from unit where unitID = "+unitID);
				while(result.next()) {
					unit.setLikes(result.getInt("likes"));
				}
			}
			//收回讚
			else if(like == 0) {
				pst = con.prepareStatement("update unit set likes = likes - 1 where unitID = ? and likes > 0");
				pst.setInt(1, unitID);
				pst.executeUpdate();
				pst = con.prepareStatement("update shareLikes set isLike = isLike - 1 where userID = ? and unitID = ? and isLike > 0");
				pst.setString(1,userID);
				pst.setInt(2, unitID);
				pst.executeUpdate();
				
				stat = con.createStatement();
				result = stat.executeQuery("select * from unit where unitID = "+unitID);
				while(result.next()) {
					unit.setLikes(result.getInt("likes"));
				}
			}
			
		}
		catch(SQLException x) {
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return unit;
	}
	//拿accountID
	public int getAccountID(String userID) {
		int accountID = 0;
		try {
			pst = con.prepareStatement("select account.accountID from rating,account where "
					+ "account.accountID = rating.accountID and account.userID = ?");
			pst.setString(1,userID);
			result = pst.executeQuery();
			while(result.next()) {
				accountID = result.getInt("account.accountID");
			}
		}
		catch(SQLException x) {
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return accountID;
	}
	
	//拿推薦給該使用者的影片
	public ArrayList<Unit> getRecommendList(int accountID, long unitID) throws IOException,TasteException{
		ArrayList<Unit> units = new ArrayList<Unit>();
		Unit unit = null;
		try {
			try {
				List<RecommendedItem> test = RecommendationResult.designatedItemBooleanRecommendedResult(
						(long)accountID,unitID,10);
				pst = con.prepareStatement("select * from unit,courselist,customlistVideo where "
						+ "unit.unitID = customlistVideo.unitID and customlistVideo.courselistID"
						+ " = courselist.courselistID and unit.unitID = ?");
				for(RecommendedItem r: test) {
					
					pst.setInt(1,(int)r.getItemID());
					result = pst.executeQuery();
					while(result.next()) {
						unit = new Unit();
						unit.setUnitName(result.getString("unit.unitName"));
						unit.setUnitID(result.getInt("unit.unitID"));
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
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return units;
		
	}
	
	//進入播放介面時，先設定isBrowse，以此來判斷有沒有看過讚
	public void setBrowse(int accountID,int unitID) {
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from rating where accountID = '"+accountID
					+"' and unitID = "+unitID);
			boolean check = false;//檢查watchRecord裡面有沒有這筆單元影片的資料，false為沒有
			while(result.next()) {
				check = true;
				}
			//如果沒有，就塞資料
			if(check == false) {
				pst = con.prepareStatement("insert into rating (accountID,unitID,score) value(?,?,4)");
				pst.setInt(1,accountID);
				pst.setInt(2,unitID);
				System.out.println("insert");
			}
			pst.executeUpdate();
				
		}
		catch(SQLException x) {
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
			System.out.println("Close Exception :" + e.toString()); 
		}
	}
}
