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
	private String selectVideoUrlSQL = "select * from unit,courselist,customlist_video where unit.unit_id = ? "
			+ "and unit.unit_id = customlist_video.unit_id and "
			+ "customlist_video.courselist_id = courselist.courselist_id";
	private String selectCourseListSQL = "select * from unit natural join customlist_video where courselist_id = ?";
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
	
	public String getVideoUrl(int unitId) {
		Unit unit = null;
		try {
			pst = con.prepareStatement(selectVideoUrlSQL);
			pst.setInt(1, unitId);
			result = pst.executeQuery();
			
			while(result.next()) 
			{ 	
				unit = new Unit();
//				System.out.println(result.getInt("unit.unit_id"));
				unit.setUnitId(result.getInt("unit.unit_id"));
				unit.setUnitName(result.getString("unit.unit_name"));
				unit.setListName(result.getString("courselist.list_name"));
				unit.setSchoolName(result.getString("courselist.school_name"));
				unit.setLikes(result.getInt("unit.likes"));
				unit.setVideoImgSrc(result.getString("unit.video_img_src"));
				unit.setVideoUrl(result.getString("unit.video_url"));
				unit.setCourseInfo(result.getString("courselist.course_info"));
			}
		}
			catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		Gson gson = new Gson();
//		System.out.println(gson.toJson(unit));
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
				unit.setUnitId(result.getInt("unit_id"));
				unit.setUnitName(result.getString("unit_name"));
				unit.setListName(result.getString("list_name"));
				unit.setSchoolName(result.getString("school_name"));
				unit.setLikes(result.getInt("likes"));
				unit.setVideoImgSrc(result.getString("video_img_src"));
				unit.setVideoUrl(result.getString("video_url"));
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
	//watch_record的watch_time是存入資料時資料庫自動抓現在的時間
	//如果該影片有存在在使用者的課程計畫中，則personal_plan的last_time也要update，並且判斷要不要改status
	//設定share_likes	的觀看紀錄，將isBrowse+1
	public void setVideoEndTime(int currentTime,int unit_id,String user_id,int duration) {
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from watch_record where user_id = '"+user_id
					+"' and unit_id = "+unit_id);
			boolean check = false;//檢查watch_record裡面有沒有這筆單元影片的資料，false為沒有
//			System.out.println("currentTime: " + currentTime);
//			System.out.println("unit_id: " + unit_id);
//			System.out.println("user_id: " + user_id);
			while(result.next()) {check = true;}
			//如果沒有，就塞資料
			if(check == false) {
				pst = con.prepareStatement("insert ignore into watch_record (user_id,unit_id,watch_time) value(?,?,null)");
				pst.setString(1,user_id);
				pst.setInt(2,unit_id);
			}
			//有就更新資料
			else {
				pst = con.prepareStatement("update watch_record set watch_time = null where user_id = ? and unit_id = ? ");
				pst.setString(1,user_id);
				pst.setInt(2,unit_id);
			}
			pst.executeUpdate();
			
			//檢查有沒有存在在使用者的課程計畫中
			stat = con.createStatement();
			result = stat.executeQuery("select unit_id from personal_plan where user_id = '"+user_id
					+"' and unit_id = "+unit_id);
			check = false;//檢查有沒有在課程計畫的table中找到這個unit_id
			while(result.next()) {check = true;}
			//如果有，就更新影片結束時間，true是有，沒有就不做事
			if(check == true) { 
				
				//判斷結束了沒，currentTime+5秒代表5秒內會結束的都改變status
//				if(currentTime+5 > duration) {
//					
//				}
				pst = con.prepareStatement("update personal_plan set last_time = ? where user_id = ? and unit_id = ? ");
				pst.setInt(1,currentTime);
				pst.setString(2,user_id);
				pst.setInt(3, unit_id);
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
	//1.share_likes：用於推薦系統
	//2.unit：該影片的總按讚數
	public void addLike(String user_id) {
		
	}
	
	
	//進入播放介面時，先設定isBrowse，以此來判斷有沒有看過讚
	public Unit setIsBrowse(String user_id,int unit_id) {
		int like = 0;
		Unit unit = null;
		try {
			//更新share_likes的isBrowse
			stat = con.createStatement();
			result = stat.executeQuery("select * from share_likes where user_id = '"+user_id
					+"' and unit_id = "+unit_id);
			boolean check = false;//檢查watch_record裡面有沒有這筆單元影片的資料，false為沒有
			while(result.next()) {
				check = true;
				}
//			System.out.println(check);
			//如果沒有，就塞資料
			if(check == false) {
				pst = con.prepareStatement("insert into share_likes (user_id,unit_id,isShare,isLike"
						+ ",isAddToCourseList,isAddToCoursePlan,isBrowse) value(?,?,0,0,0,0,1)");
				pst.setString(1,user_id);
				pst.setInt(2,unit_id);
			}
			//有就更新資料
			else {
//				System.out.println("");
				pst = con.prepareStatement("update share_likes set isBrowse = isBrowse + 1 where user_id = ? and unit_id = ? and isBrowse < 6");
				pst.setString(1,user_id);
				pst.setInt(2,unit_id);
			}
			pst.executeUpdate();
			
			result = stat.executeQuery("select * from share_likes where user_id = '"+user_id
					+"' and unit_id = "+unit_id);
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
	//更新2:share_likes的isLike
	public Unit setLike(String user_id,int unit_id,int like) {
		Unit unit = new Unit();
		try {
			//按讚
			if(like == 1) {
				pst = con.prepareStatement("update unit set likes = likes + 1 where unit_id = ?");
				pst.setInt(1, unit_id);
				pst.executeUpdate();
				
				pst = con.prepareStatement("update share_likes set isLike = isLike + 1 where user_id = ? and unit_id = ? and isLike < 1");
				pst.setString(1,user_id);
				pst.setInt(2, unit_id);
				pst.executeUpdate();
				
				stat = con.createStatement();
				result = stat.executeQuery("select * from unit where unit_id = "+unit_id);
				while(result.next()) {
					unit.setLikes(result.getInt("likes"));
				}
			}
			//收回讚
			else if(like == 0) {
				pst = con.prepareStatement("update unit set likes = likes - 1 where unit_id = ? and likes > 0");
				pst.setInt(1, unit_id);
				pst.executeUpdate();
//				System.out.println("111");
				pst = con.prepareStatement("update share_likes set isLike = isLike - 1 where user_id = ? and unit_id = ? and isLike > 0");
				pst.setString(1,user_id);
				pst.setInt(2, unit_id);
				pst.executeUpdate();
				
				stat = con.createStatement();
				result = stat.executeQuery("select * from unit where unit_id = "+unit_id);
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
	//拿account_id
	public int getAccountID(String user_id) {
		int account_id = 0;
		try {
//			System.out.println("1");
			pst = con.prepareStatement("select account.account_id from rating,account where "
					+ "account.account_id = rating.account_id and account.user_id = ?");
			pst.setString(1,user_id);
			result = pst.executeQuery();
			while(result.next()) {
				account_id = result.getInt("account.account_id");
//				System.out.println(result.getInt("account.account_id"));
			}
		}
		catch(SQLException x) {
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return account_id;
	}
	
	//拿推薦給該使用者的影片
	public ArrayList<Unit> getRecommendList(int account_id, long unit_id) throws IOException,TasteException{
		ArrayList<Unit> units = new ArrayList<Unit>();
		Unit unit = null;
//		ArrayList<Integer> list = new ArrayList<Integer>();
		try {
			try {
				List<RecommendedItem> test = RecommendationResult.designatedItemBooleanRecommendedResult(
						(long)account_id,unit_id,10);
				pst = con.prepareStatement("select * from unit,courselist,customlist_video where "
						+ "unit.unit_id = customlist_video.unit_id and customlist_video.courselist_id"
						+ " = courselist.courselist_id and unit.unit_id = ?");
				for(RecommendedItem r: test) {
		    		System.out.println(r);
					
					pst.setInt(1,(int)r.getItemID());
					result = pst.executeQuery();
					while(result.next()) {
						unit = new Unit();
//						System.out.println(result.getString("unit.unit_name"));
						unit.setUnitName(result.getString("unit.unit_name"));
						unit.setUnitId(result.getInt("unit.unit_id"));
						unit.setVideoImgSrc(result.getString("unit.video_img_src"));
						unit.setTeacher(result.getString("courselist.teacher"));
						unit.setSchoolName(result.getString("courselist.school_name"));
						unit.setLikes(result.getInt("unit.likes"));
						if(result.getString("unit.video_url").split("/")[2].equals("www.youtube.com")) {
							unit.setType(1);//youtube
						}
						else {
							unit.setType(2);//jwplayer
						}
						units.add(unit);
					}
//		    		list.add((int)r.getItemID());
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
	public void setBrowse(int account_id,int unit_id) {
		try {
			stat = con.createStatement();
			result = stat.executeQuery("select * from rating where account_id = '"+account_id
					+"' and unit_id = "+unit_id);
			boolean check = false;//檢查watch_record裡面有沒有這筆單元影片的資料，false為沒有
			while(result.next()) {
				check = true;
				}
			//如果沒有，就塞資料
			if(check == false) {
				pst = con.prepareStatement("insert into rating (account_id,unit_id,score) value(?,?,4)");
				pst.setInt(1,account_id);
				pst.setInt(2,unit_id);
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
