package Search;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SearchManager
{
	private final String selectCourseListSQL = "select * from courselist where listName like ?";
	private final String selectUnitKeywordSQL = "select * from unit natural join unitKeyword where unitKeyword like ? ";
	private final String selectCourseKeywordSQLStart = "select max(courselistId)courselistId, max(schoolName)schoolName, max(listName)listName, max(teacher)teacher, max(departmentName)departmentName, max(courseInfo)courseInfo, max(creator)creator, max(share)share, max(likes)likes ,max(tfidf)tfidf from courselist natural join courseKeyword where ";
	private final String selectCourseKeywordSQLMiddle = "(courseKeyword like ? or listName like ? or teacher like ? or schoolName like ? or departmentName like ?) ";
	private final String selectCourseKeywordSQLEnd = "group by listName ORDER BY `tfidf` DESC";
	private final String selectUnitByCourseIdSQL = "select * from unit natural join customListVideo where courselistId = ?";
	private final String selectTeacherSQL = "select distinct teacher from courselist where teacher is not null";
	private final String selectDepartmentSQL = "select distinct departmentName from courselist where departmentName is not null";
	private final String selectPlanMaxSQL = "select MAX(oorder) from personalPlan where status = 1 and userId = ?";
	private final String insertSearchRecordSQL = "insert into searchRecord (userId, searchWord, searchTime) value(?,?,null)";
	public enum SearchMethod {PRECISE_COURSE, FUZZY_COURSE, UNIT, DEFAULT, ALL}; // 搜尋方法
	private enum QueryType {PRECISE, FUZZY}; // 精準/模糊 搜尋
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null; 
	
	public SearchManager() {
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
	public ArrayList<String> selectCourseListTable(String name) {

		ArrayList<String> outputList = new ArrayList<>(); 
		try {

			pst = con.prepareStatement(selectCourseListSQL);
			pst.setString(1,  "%" + name + "%" );
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 String output = result.getString("listName");
				 outputList.add(output);
		     }
		}
		catch(SQLException x){
			System.out.println("SearchManager-selectCourseListTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		 return outputList;
	}

	public ArrayList<String> getUnitByKeyword(String keyword) {

		ArrayList<String> outputList = new ArrayList<>(); 
		try {

			pst = con.prepareStatement(selectUnitKeywordSQL);
			pst.setString(1,  "%" + keyword + "%" );
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 String output = result.getString(3);
				 outputList.add(output);
		     }
		}
		catch(SQLException x){
			System.out.println("SearchManager-getUnitByKeyword");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		 return outputList;
	}
	
	/** 回傳關鍵字查詢結果 (推薦用)
	 * 
	 * @param keyword : (String) 關鍵字
	 * @return 型態為 (Search) 的ArrayList
	 */
	public ArrayList<Search> keywordSearch(String keyword)
	{
		return keywordSearch(keyword, null, SearchMethod.ALL);
	}

	/** 回傳關鍵字查詢結果 (ex. SearchManager.SearchMethod.ALL -> 回傳全部的結果)
	 * 
	 * @param keyword : (String) 關鍵字
	 * @param userId : (String) 帳號
	 * @param method : {PRECISE_COURSE, FUZZY_COURSE, UNIT, DEFAULT, ALL} 搜尋方法
	 * @return 型態為 (Search) 的ArrayList
	 */
	public ArrayList<Search> keywordSearch(String keyword, String userId, SearchMethod method)
	{
		ArrayList<Search> rusultList = new ArrayList<Search>();

		switch(method)
		{
		// 回傳精準查詢後的 Courselist
		case PRECISE_COURSE:
			rusultList.addAll(getCourseListByKeyword(keyword, QueryType.PRECISE));
			break;
		// 回傳模糊查詢後的 Courselist
		case FUZZY_COURSE:
			rusultList.addAll(getCourseListByKeyword(keyword, QueryType.FUZZY));
			break;
		// 回傳查詢後的 Unit
		case UNIT:
			rusultList.addAll(getUnitListByKeyword(keyword));
			break;
		// 回傳精準查詢後的Courselist & Unit (為預設的搜尋方法)
		case DEFAULT:
			rusultList.addAll(getCourseListByKeyword(keyword, QueryType.PRECISE));
			rusultList.addAll(getUnitListByKeyword(keyword));
			break;
		// 回傳精準、模糊查詢的Courselist & Unit查詢結果
		case ALL:
			rusultList.addAll(getCourseListByKeyword(keyword, QueryType.PRECISE));
			rusultList.addAll(getCourseListByKeyword(keyword, QueryType.FUZZY));
			rusultList.addAll(getUnitListByKeyword(keyword));
			break;
		}
		if (userId != null)
			insertSearchRecord(keyword, userId);
		
		conClose();
		return rusultList;
	}
	
	/** 輸入字串回傳含有%的字串
	 * 
	 * @param keyword : (String) 關鍵字
	 * @param type : {PRECISE, FUZZY} 精準/模糊搜尋
	 * @return (String) e.g.微積分 -> PRECISE: %微積分% , FUZZY: %微%積%分%
	 */
	private String getQuery(String keyword, QueryType type)
	{
		String query = "%";
		switch (type)
		{
		case PRECISE:
			// (ex. 微積分 -> %微積分%)
			query += (keyword + "%");
			break;
		case FUZZY:
			// (ex. 微積分 -> %微%積%分%)
			if (keyword.length() > 1)
			{
				for (String word:keyword.split(""))
				{
					query += (word + "%");
				}
			}
			else
			{
				query += (keyword + "%");
			}
			break;
		}
		return query;
	}
	
	// 輸出 SelectCourseKeyword SQL 語法
	private String getSelectCourseKeywordSQL(String keyword, QueryType type)
	{
		String selectCourseKeywordSQL = selectCourseKeywordSQLStart + selectCourseKeywordSQLMiddle;
		// 如果有多個query (用+分隔)，則搜尋資料庫用and連接
		if (keyword.contains("+"))
		{
			String []wordList = keyword.split("[+]");	//RE的+有特別意思，所以要用括號括起來
			selectCourseKeywordSQL = selectCourseKeywordSQL.replace("?", "'" + getQuery(wordList[0], type) + "'");
			for (int i = 1; i < wordList.length; i++)
			{
				selectCourseKeywordSQL += " and " + selectCourseKeywordSQLMiddle;
				selectCourseKeywordSQL = selectCourseKeywordSQL.replace("?", "'" + getQuery(wordList[i], type) + "'");
			}
		}
		// 單個query
		else
		{
			selectCourseKeywordSQL = selectCourseKeywordSQL.replace("?", "'" + getQuery(keyword, type) + "'");
		}
		selectCourseKeywordSQL += selectCourseKeywordSQLEnd;
		return selectCourseKeywordSQL;
	}
	
	// 進行課程的搜尋
	private ArrayList<Search> getCourseListByKeyword(String keyword, QueryType type) {
		ArrayList<Search> courseList = new ArrayList<Search>();
		try {
			pst = con.prepareStatement(getSelectCourseKeywordSQL(keyword, type));
			result = pst.executeQuery();
			while(result.next()) 
		    { 	
				Search output = new Search();
				output.setCourselistId(result.getInt("courselistId"));
				output.setSchoolName(result.getString("schoolName"));
				output.setListName(result.getString("listName"));
				output.setTeacher(result.getString("teacher"));
				output.setDepartmentName(result.getString("departmentName"));
				output.setCourseInfo(result.getString("courseInfo"));
				output.setCreator(result.getString("creator"));
				output.setShare(result.getInt("share"));
				output.setLikes(result.getInt("likes"));
				courseList.add(output);
		    }
		} catch(SQLException x)
		{
			System.out.println("SearchManager-getCourseListByKeyword");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		getFullCourseInfo(courseList);
		return courseList;
	}

	// 將  Unit 放到  Course 裡面
	private ArrayList<Search> getFullCourseInfo(ArrayList<Search> prevList)
	{
		try
		{
			pst = con.prepareStatement(selectUnitByCourseIdSQL);
			for (int i = 0; i < prevList.size(); i++)
			{
				pst.setInt(1,prevList.get(i).getCourselistId());
				result = pst.executeQuery();
				while (result.next())
				{
					Unit unit = new Unit();
					unit.setUnitId(result.getInt("unitId"));
					unit.setUnitName(result.getString("unitName"));
					unit.setVideoUrl(result.getString("videoUrl"));
					unit.setLikes(result.getInt("likes"));
					unit.setVideoImgSrc(result.getString("videoImgSrc"));
					prevList.get(i).addUnit(unit);
				}
			}
		} catch (SQLException x)
		{
			System.out.println("SearchManager-getFullCourseInfo");
			System.out.println("Exception select"+x.toString());
		} finally {
			Close();
		}
		return prevList;
	}

	// 進行單元的搜尋
	private ArrayList<Search> getUnitListByKeyword(String keyword)
	{
		ArrayList<Search> unitList = new ArrayList<Search>();
		try
		{
			pst = con.prepareStatement(selectUnitKeywordSQL);
			pst.setString(1, "%" + keyword + "%" );
			result = pst.executeQuery();
			while (result.next())
			{
				Search search = new Search();
				Unit unit = new Unit();
				unit.setUnitId(result.getInt("unitId"));
				unit.setUnitName(result.getString("unitName"));
				unit.setVideoUrl(result.getString("videoUrl"));
				unit.setLikes(result.getInt("likes"));
				unit.setSchoolName(result.getString("schoolName"));
				unit.setVideoImgSrc(result.getString("videoImgSrc"));
				search.addUnit(unit);
				unitList.add(search);;
			}
		} catch (SQLException x)
		{
			System.out.println("SearchManager-getUnitListByKeyword");
			System.out.println("Exception select"+x.toString());
		} finally {
			Close();
		}
		return unitList;
	}
	
	// 將搜尋紀錄加進資料庫
	private void insertSearchRecord(String keyword, String userId)
	{
		try
		{
			pst = con.prepareStatement(insertSearchRecordSQL);
			pst.setString(1, userId);
			pst.setString(2, keyword);
			pst.executeUpdate();
		} catch(SQLException x){
			System.out.println("SearchManager-insertSearchRecord");
			System.out.println("Exception insert"+x.toString());
		} finally {
			Close();
		}
	}
	
	public ArrayList<String> getTeacher()
	{
		ArrayList<String> outputList = new ArrayList<>(); 
		try {

			pst = con.prepareStatement(selectTeacherSQL);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 String output = result.getString(1);
				 outputList.add(output);
		     }
		}
		catch(SQLException x){
			System.out.println("SearchManager-getTeacher");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		conClose();
		return outputList;
	}

	public ArrayList<String> getDepartment()
	{
		ArrayList<String> outputList = new ArrayList<>(); 
		try {

			pst = con.prepareStatement(selectDepartmentSQL);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 String output = result.getString(1);
				 outputList.add(output);
		     }
		}
		catch(SQLException x){
			System.out.println("SearchManager-getDepartment");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		conClose();
		return outputList;
	}
	//將搜尋的單元影片加入課程計畫中
	public void addToCoursePlan(String userId,int unitId){
		int maxOrder = 0;
		try {
			pst = con.prepareStatement(selectPlanMaxSQL);
			pst.setString(1, userId);
			result = pst.executeQuery();
			while(result.next()) {
				maxOrder = result.getInt("MAX(oorder)");
			}
			pst = con.prepareStatement("insert into personalPlan (userId,unitId,lastTime,status,oorder) value(?,?,0,1,?)");
			pst.setString(1, userId);
			pst.setInt(2, unitId);
			pst.setInt(3, ++maxOrder);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("SearchManager-addToCoursePlan");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	//將搜尋的清單中的所有單元影片加入課程計畫中
	public void addToCoursePlanList(String userId,int courselistId){
		ArrayList<Integer> plans = new ArrayList<Integer>();
		int maxOrder = 0;
		try {
			pst = con.prepareStatement(selectPlanMaxSQL);
			pst.setString(1, userId);
			result = pst.executeQuery();
			while(result.next()) {
				maxOrder = result.getInt("MAX(oorder)");
			}
			result = stat.executeQuery("select * from unit,customListVideo,courselist where unit.unitId"
					+ " = customListVideo.unitId and customListVideo.courselistId ="
					+ " courselist.courselistId and courselist.courselistId = "+ courselistId);
			while(result.next()) {
				plans.add(result.getInt("unit.unitId"));
			}
			
			pst = con.prepareStatement("insert ignore into personalPlan (userId,unitId,lastTime,status,oorder) value(?,?,0,1,?)");
			for(int i = 0;i < plans.size();i++) {
				
				pst.setString(1, userId);
				pst.setInt(2, plans.get(i));
				pst.setInt(3, ++maxOrder);
				pst.addBatch();
			}
			pst.executeBatch();
		}
		catch(SQLException x){
			System.out.println("SearchManager-addToCoursePlanList");
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
			System.out.println("SearchManager Close Exception :" + e.toString()); 
		}		
	} 
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("SearchManager Close Exception :" + e.toString()); 
		}
	}
	
	public static void main(String []args)
	{
//		SearchManager kldm = new SearchManager();
		
//		System.out.println(kldm.selectCourseListTable("化學"));
//		System.out.println(kldm.selectUnitKeywordTable("微積分"));
//		System.out.println(kldm.getCourseListByKeyword("微積分"));
//		System.out.println(kldm.getAllKeyword("微積分"));
//		System.out.println(kldm.getTeacher());
//		System.out.println(kldm.getDepartment());
	}
}
