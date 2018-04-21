package LoginVerification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

public class LoginVerificationDatabaseManager
{
	private String selectPasswordSQL = "select password from account where user_id = ? or email = ?";
	private String selectUserIdSQL = "select user_id from account where user_id = ? or email = ?";
	//private String selectPersonalKeyLabelSQL = "select * from keylabel where unit_id = ? and user_id = ? ";
//	private String insertKeyLabelSQL = "insert into keylabel value (null,?,?,?,?,?,?,?,?)";
//	private String deleteKeyLabelSQL = "delete from keylabel where keylabel_id = ?";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public LoginVerificationDatabaseManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");//註冊Driver
			con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?useUnicode=true&characterEncoding=Big5"
					, "root", "peter");//取得connection
			
		}
		catch(ClassNotFoundException e){
			System.out.println("DriverClassNotFound"+e.toString());
		}
		catch(SQLException x){
			System.out.println("Exception" + x.toString());
		}
	}
	public String getUserPassword(String param) {
		try {
			pst = con.prepareStatement(selectPasswordSQL);
			pst.setString(1, param);
			pst.setString(2, param);
			result = pst.executeQuery();
			if(result.next()) 
			{ 	
				return result.getString("password");
			}
		}
			catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return null;
	}
	public String getUserId(String param) {
		try {
			pst = con.prepareStatement(selectUserIdSQL);
			pst.setString(1, param);
			pst.setString(2, param);
			result = pst.executeQuery();
			if(result.next()) 
			{ 	
				return result.getString("user_id");
			}
		}
			catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return null;
	}
	
//	public String getUnitPersonalKeyLabel(int unit, int user) {
//
//		ArrayList<KeyLabel> outputList = new ArrayList<>(); 
//		try {
//			pst = con.prepareStatement(selectPersonalKeyLabelSQL);
//			pst.setInt(1, unit);
//			pst.setInt(2, user);
//			result = pst.executeQuery();
//			while(result.next()) 
//			{ 	
//				KeyLabel keyLabel = new KeyLabel();
//				keyLabel.setKeyLabelId(result.getInt("keylabel_id"));
//				keyLabel.setUnitId(result.getInt("unit_id"));
//				keyLabel.setUserId(result.getString("user_id"));
//				keyLabel.setKeyLabelName(result.getString("keylabel_name"));
//				keyLabel.setBeginTime(result.getInt("begin_time"));
//				keyLabel.setEndTime(result.getInt("end_time"));
//				keyLabel.setShare(result.getInt("share"));
//				keyLabel.setShareTime(result.getString("share_time"));
//				keyLabel.setLikes(result.getInt("likes"));
//				outputList.add(keyLabel);
//			}
//		}
//			catch(SQLException x){
//			System.out.println("Exception select"+x.toString());
//		}
//		finally {
//			Close();
//		}
//		String json = new Gson().toJson(outputList);
//		return json;
//	}
	
//	public void insertKeyLabel(KeyLabel keyLabel)
//	{
//		try
//		{
//			pst = con.prepareStatement(insertKeyLabelSQL, Statement.RETURN_GENERATED_KEYS);
//			pst.setInt(1, keyLabel.getUnitId());
//			pst.setString(2, keyLabel.getUserId());
//			pst.setString(3, keyLabel.getKeyLabelName());
//			pst.setInt(4, keyLabel.getBeginTime());
//			pst.setInt(5, keyLabel.getEndTime());
//			pst.setInt(6, 0);
//			pst.setString(7, null);
//			pst.setInt(8, 0);
//			pst.executeUpdate();
////			ResultSet generatedKeys = pst.getGeneratedKeys();
////			if (generatedKeys.next())
////				return generatedKeys.getInt(1);
//			
//		} catch (final SQLException x)
//		{
//			System.out.println("Exception insert" + x.toString());
//		} finally
//		{
//			Close();
//		}
//	}
	
//	public void deleteKeyLabel(int keyLabelId)
//	{
//		try {
//			pst = con.prepareStatement(deleteKeyLabelSQL);
//			pst.setInt(1,keyLabelId);
//			pst.executeUpdate();
//		}
//		catch(SQLException x){
//			System.out.println("Exception delete"+x.toString());
//		}
//		finally {
//			Close();
//		}
//	}
	
	public void Close() {
		try {
			if(result!=null) {
				result.close();
				result = null;
			}
			if(stat!=null) {
				stat.close();
				stat = null;
			}
			if(pst!=null) {
				pst.close();
				pst = null;
			}
		}
		catch(SQLException e) {
			System.out.println("Close Exception :" + e.toString()); 
		}		
	} 
	
	public static void main(String []args)
	{
		LoginVerificationDatabaseManager lvdm = new LoginVerificationDatabaseManager();
		
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
	}
}
