package KeyLabel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

public class KeyLabelManager
{
	private String selectUnitKeyLabelSQL = "select * from keylabel where unitId = ?";
	private String selectExangeKeyLabelSQL = "select * from keylabel where unitId = ? and share = 1";
	private String selectPersonalKeyLabelSQL = "select * from keylabel where unitId = ? and userId = ?";
	private String updateKeyLabelSQL = "update keylabel set keylabelName = ?, beginTime = ?, endTime = ? where keylabelId = ?";
	private String insertKeyLabelSQL = "insert into keylabel value (null,?,?,?,?,?,?,?,?)";
	private String deleteKeyLabelSQL = "delete from keylabel where keylabelId = ?";
	private String shareKeyLabelSQL = "update keylabel set share = ? where keylabelId = ?";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public KeyLabelManager() {
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
	// 取單元全部的重點標籤
	public String getUnitKeyLabel(int unit) {

		ArrayList<KeyLabel> outputList = new ArrayList<>(); 
		try {
			pst = con.prepareStatement(selectUnitKeyLabelSQL);
			pst.setInt(1, unit);
			result = pst.executeQuery();
			while(result.next()) 
			{ 	
				KeyLabel keyLabel = new KeyLabel();
				keyLabel.setKeyLabelId(result.getInt("keylabelId"));
				keyLabel.setUnitId(result.getInt("unitId"));
				keyLabel.setUserId(result.getString("userId"));
				keyLabel.setKeyLabelName(result.getString("keylabelName"));
				keyLabel.setBeginTime(result.getInt("beginTime"));
				keyLabel.setEndTime(result.getInt("endTime"));
				keyLabel.setShare(result.getInt("share"));
				keyLabel.setShareTime(result.getString("shareTime"));
				keyLabel.setLikes(result.getInt("likes"));
				outputList.add(keyLabel);
			}
		}
		catch(SQLException x){
			System.out.println("KeyLabelManager-getUnitKeyLabel");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(outputList);
		return json;
	}
	// 取單元交流重點標籤
	public String getExangeKeyLabel(int unit) {

		ArrayList<KeyLabel> outputList = new ArrayList<>(); 
		try {
			pst = con.prepareStatement(selectExangeKeyLabelSQL);
			pst.setInt(1, unit);
			result = pst.executeQuery();
			while(result.next()) 
			{ 	
				KeyLabel keyLabel = new KeyLabel();
				keyLabel.setKeyLabelId(result.getInt("keylabelId"));
				keyLabel.setUnitId(result.getInt("unitId"));
				keyLabel.setUserId(result.getString("userId"));
				keyLabel.setKeyLabelName(result.getString("keylabelName"));
				keyLabel.setBeginTime(result.getInt("beginTime"));
				keyLabel.setEndTime(result.getInt("endTime"));
				keyLabel.setShare(result.getInt("share"));
				keyLabel.setShareTime(result.getString("shareTime"));
				keyLabel.setLikes(result.getInt("likes"));
				outputList.add(keyLabel);
			}
		}
		catch(SQLException x){
			System.out.println("KeyLabelManager-getExangeKeyLabel");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(outputList);
		return json;
	}
	// 取單元個人重點標籤
	public String getPersonalKeyLabel(int unit, String user) {

		ArrayList<KeyLabel> outputList = new ArrayList<>(); 
		try {
			pst = con.prepareStatement(selectPersonalKeyLabelSQL);
			pst.setInt(1, unit);
			pst.setString(2, user);
			result = pst.executeQuery();
			while(result.next()) 
			{ 	
				KeyLabel keyLabel = new KeyLabel();
				keyLabel.setKeyLabelId(result.getInt("keylabelId"));
				keyLabel.setUnitId(result.getInt("unitId"));
				keyLabel.setUserId(result.getString("userId"));
				keyLabel.setKeyLabelName(result.getString("keylabelName"));
				keyLabel.setBeginTime(result.getInt("beginTime"));
				keyLabel.setEndTime(result.getInt("endTime"));
				keyLabel.setShare(result.getInt("share"));
				keyLabel.setShareTime(result.getString("shareTime"));
				keyLabel.setLikes(result.getInt("likes"));
				outputList.add(keyLabel);
			}
		}
		catch(SQLException x){
			System.out.println("KeyLabelManager-getPersonalKeyLabel");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(outputList);
		return json;
	}
	
	public int insertKeyLabel(KeyLabel keyLabel)
	{
		try
		{
			pst = con.prepareStatement(insertKeyLabelSQL, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, keyLabel.getUnitId());
			pst.setString(2, keyLabel.getUserId());
			pst.setString(3, keyLabel.getKeyLabelName());
			pst.setInt(4, keyLabel.getBeginTime());
			pst.setInt(5, keyLabel.getEndTime());
			pst.setInt(6, 0);
			pst.setString(7, null);
			pst.setInt(8, 0);
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if (generatedKeys.next())
				return generatedKeys.getInt(1);
			
		} catch (final SQLException x){
			System.out.println("KeyLabelManager-insertKeyLabel");
			System.out.println("Exception insert" + x.toString());
		} finally
		{
			Close();
		}
		return 0;
	}
	
	public void updateKeyLabel(KeyLabel keyLabel)
	{
		try {
			pst = con.prepareStatement(updateKeyLabelSQL);
			pst.setString(1,keyLabel.getKeyLabelName());
			pst.setInt(2,keyLabel.getBeginTime());
			pst.setInt(3,keyLabel.getEndTime());
			pst.setInt(4, keyLabel.getKeyLabelId());
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("KeyLabelManager-updateKeyLabel");
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	public void deleteKeyLabel(int keyLabelId)
	{
		try {
			pst = con.prepareStatement(deleteKeyLabelSQL);
			pst.setInt(1,keyLabelId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("KeyLabelManager-deleteKeyLabel");
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	public void shareKeyLabel(int keyLabelId, int share)
	{
		try {
			pst = con.prepareStatement(shareKeyLabelSQL);
			pst.setInt(1, share);
			pst.setInt(2,keyLabelId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("KeyLabelManager-shareKeyLabel");
			System.out.println("Exception delete"+x.toString());
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
			System.out.println("KeyLabelManager Close Exception :" + e.toString()); 
		}		
	} 
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("KeyLabelManager Close Exception :" + e.toString()); 
		}
	}
//	public static void main(String []args)
//	{
//		KeyLabelDatabaseManager kldm = new KeyLabelDatabaseManager();
//		
//		ArrayList<KeyLabel> unitKeyLabel = kldm.getUnitKeyLabel(1);
//		for (KeyLabel i:unitKeyLabel)
//		{
//			System.out.println(i);
//		}
//		System.out.println("unitPersonal:");
//		ArrayList<KeyLabel> unitPersonalKeyLabel = kldm.getUnitPersonalKeyLabel(1, 1);
//		for (KeyLabel i:unitPersonalKeyLabel)
//		{
//			System.out.println(i);
//		}
//	}
}
