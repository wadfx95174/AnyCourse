package KeyLabel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

public class KeyLabelDatabaseManager
{
	private String selectUnitKeyLabelSQL = "select * from keylabel where unit_id = ? and share = 1";
	private String selectPersonalKeyLabelSQL = "select * from keylabel where unit_id = ? and user_id = ? ";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public KeyLabelDatabaseManager() {
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
	public String getUnitKeyLabel(int unit) {

		ArrayList<KeyLabel> outputList = new ArrayList<>(); 
		try {
			pst = con.prepareStatement(selectUnitKeyLabelSQL);
			pst.setInt(1, unit);
			result = pst.executeQuery();
			while(result.next()) 
			{ 	
				KeyLabel keyLabel = new KeyLabel();
				keyLabel.setKeyLabelId(result.getInt("keylabel_id"));
				keyLabel.setUnitId(result.getInt("unit_id"));
				keyLabel.setUserId(result.getString("user_id"));
				keyLabel.setKeyLabelName(result.getString("keylabel_name"));
				keyLabel.setBeginTime(result.getInt("begin_time"));
				keyLabel.setEndTime(result.getInt("end_time"));
				keyLabel.setShare(result.getInt("share"));
				keyLabel.setShareTime(result.getString("share_time"));
				keyLabel.setLikes(result.getInt("likes"));
				outputList.add(keyLabel);
			}
		}
			catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(outputList);
		return json;
	}
	
	public String getUnitPersonalKeyLabel(int unit, int user) {

		ArrayList<KeyLabel> outputList = new ArrayList<>(); 
		try {
			pst = con.prepareStatement(selectPersonalKeyLabelSQL);
			pst.setInt(1, unit);
			pst.setInt(2, user);
			result = pst.executeQuery();
			while(result.next()) 
			{ 	
				KeyLabel keyLabel = new KeyLabel();
				keyLabel.setKeyLabelId(result.getInt("keylabel_id"));
				keyLabel.setUnitId(result.getInt("unit_id"));
				keyLabel.setUserId(result.getString("user_id"));
				keyLabel.setKeyLabelName(result.getString("keylabel_name"));
				keyLabel.setBeginTime(result.getInt("begin_time"));
				keyLabel.setEndTime(result.getInt("end_time"));
				keyLabel.setShare(result.getInt("share"));
				keyLabel.setShareTime(result.getString("share_time"));
				keyLabel.setLikes(result.getInt("likes"));
				outputList.add(keyLabel);
			}
		}
			catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(outputList);
		return json;
	}
	
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
		KeyLabelDatabaseManager kldm = new KeyLabelDatabaseManager();
		
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
