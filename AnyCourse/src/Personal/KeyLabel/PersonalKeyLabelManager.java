package Personal.KeyLabel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

public class PersonalKeyLabelManager
{
	private String selectPersonalKeyLabelSQL = "select * from keylabel , unit where keylabel.unitId = unit.unitId and userId = ?";
	private String deleteKeyLabelSQL = "delete from keylabel where keylabelId = ?";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public PersonalKeyLabelManager() {
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
	
	public String getAllPersonalKeyLabel(String user) {
		ArrayList<PersonalKeyLabel> outputList = new ArrayList<>(); 
		try {
			pst = con.prepareStatement(selectPersonalKeyLabelSQL);
			pst.setString(1, user);
			result = pst.executeQuery();
			while(result.next()) 
			{ 	
				PersonalKeyLabel keyLabel = new PersonalKeyLabel();
				keyLabel.setKeyLabelId(result.getInt("keylabelId"));
				keyLabel.setUnitId(result.getInt("unitId"));
				keyLabel.setUnitName(result.getString("unitName"));
				keyLabel.setUserId(result.getString("userId"));
				keyLabel.setKeyLabelName(result.getString("keylabelName"));
				keyLabel.setUrl(result.getString("videoUrl"));
				keyLabel.setBeginTime(result.getInt("beginTime"));
				keyLabel.setEndTime(result.getInt("endTime"));
				keyLabel.setShare(result.getInt("share"));
				keyLabel.setShareTime(result.getString("shareTime"));
				keyLabel.setLikes(result.getInt("likes"));
				outputList.add(keyLabel);
			}
		}
		catch(SQLException x){
			System.out.println("PersonalKeyLabelManager-getUnitPersonalKeyLabel");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(outputList);
		return json;
	}
	
	public void deleteKeyLabel(int keyLabelId)
	{
		try {
			pst = con.prepareStatement(deleteKeyLabelSQL);
			pst.setInt(1,keyLabelId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("PersonalKeyLabelManager-deleteKeyLabel");
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
			System.out.println("PersonalKeyLabelManager Close Exception :" + e.toString()); 
		}		
	} 
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("PersonalKeyLabelManager Close Exception :" + e.toString()); 
		}
	}
}
