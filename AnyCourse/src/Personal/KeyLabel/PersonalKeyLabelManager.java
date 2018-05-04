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
	private String selectPersonalKeyLabelSQL = "select * from keylabel natural join unit where user_id = ?";
	private String deleteKeyLabelSQL = "delete from keylabel where keylabel_id = ?";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public PersonalKeyLabelManager() {
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
	
	public String getUnitPersonalKeyLabel(String user) {
		ArrayList<PersonalKeyLabel> outputList = new ArrayList<>(); 
		try {
			pst = con.prepareStatement(selectPersonalKeyLabelSQL);
			pst.setString(1, user);
			result = pst.executeQuery();
			while(result.next()) 
			{ 	
				PersonalKeyLabel keyLabel = new PersonalKeyLabel();
				keyLabel.setKeyLabelId(result.getInt("keylabel_id"));
				keyLabel.setUnitId(result.getInt("unit_id"));
				keyLabel.setUnitName(result.getString("unit_name"));
				keyLabel.setUserId(result.getString("user_id"));
				keyLabel.setKeyLabelName(result.getString("keylabel_name"));
				keyLabel.setUrl(result.getString("video_url"));
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
	
	public void deleteKeyLabel(int keyLabelId)
	{
		try {
			pst = con.prepareStatement(deleteKeyLabelSQL);
			pst.setInt(1,keyLabelId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	public void Close() {
		try {
			if(con!=null) {
				con.close();
			}
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
}
