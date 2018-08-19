package Group.KeyLabel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

public class GroupKeyLabelManager {
	private final String selectGroupKeyLabelSQL = "select * from groupKeylabelMatch natural join keylabel , unit where keylabel.unitId = unit.unitId and groupId = ?";
	private final String insertGroupKeyLabelSQL = "insert into groupKeylabelMatch value (?,?)";
	private final String deleteGroupKeyLabelSQL = "delete from groupKeylabelMatch where keylabelId = ?";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public GroupKeyLabelManager() {
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
	
	public String getAllGroupKeyLabel(int groupId) {
		ArrayList<GroupKeyLabel> outputList = new ArrayList<>(); 
		try {
			pst = con.prepareStatement(selectGroupKeyLabelSQL);
			pst.setInt(1, groupId);
			result = pst.executeQuery();
			while(result.next()) 
			{ 	
				GroupKeyLabel keyLabel = new GroupKeyLabel();
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
			System.out.println("GroupKeyLabelManager-getAllGroupKeyLabel");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(outputList);
		return json;
	}
	
	public void insertKeyLabel(int groupId, int keyLabelId)
	{
		try {
			pst = con.prepareStatement(insertGroupKeyLabelSQL);
			pst.setInt(1, groupId);
			pst.setInt(2, keyLabelId);
			pst.executeUpdate();
		} catch(SQLException x) {
			System.out.println("GroupKeyLabelManager-deleteKeyLabel");
			System.out.println("Exception delete"+x.toString());
		} finally {
			Close();
		}
	}
	
	public void deleteKeyLabel(int keyLabelId)
	{
		try {
			pst = con.prepareStatement(deleteGroupKeyLabelSQL);
			pst.setInt(1,keyLabelId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("GroupKeyLabelManager-deleteKeyLabel");
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
			System.out.println("GroupKeyLabelManager Close Exception :" + e.toString()); 
		}		
	} 
}
