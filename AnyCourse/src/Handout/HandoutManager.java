package Handout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;
import Handout.Handout;

public class HandoutManager {	
	public String selectHandoutSQL = "select * from handout where unitId= ? ";
	
	public Handout handout;
	public Connection con = null;
	public Statement stat = null;
	public ResultSet result = null;
	public PreparedStatement pst = null; 
	
	public Random random = new Random();
	
	
	public HandoutManager(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=Big5", "root", "peter");//���onnection			   
		}
		catch(ClassNotFoundException e){
			System.out.println("DriverClassNotFound"+e.toString());
		}
		catch(SQLException x){
			System.out.println("Exception"+x.toString());
		}
	}
		
	public String selectHandoutTable(int unitId) {
		ArrayList<Handout> handouts = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectHandoutSQL);
			pst.setInt(1, unitId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 handout = new Handout();
				 handout.setUnitId(result.getInt("unitId"));
				 handout.setHandoutName(result.getString("handoutName"));
				 handout.setHandoutUrl(result.getString("handoutUrl"));
				 handouts.add(handout);
		     }
		}
		catch(SQLException x){
			System.out.println("ForumManager-selectCommentTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(handouts);
		return json;
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
			System.out.println("ForumManager Close Exception :" + e.toString()); 
		}		
	} 
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("ForumManager Close Exception :" + e.toString()); 
		}
	}
}
