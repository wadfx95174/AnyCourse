package Note;
  
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;;

public class NoteManager {
	public String insertTextNoteSQL = "insert into textNote (textNoteID,unitID,userID,textNote,share,shareTime,likes) value(null,?,?,?,?,?,?)";
	public String selectTextNoteSQL = "select * from textNote where unitID = ? and userID= ? ";
	public String deleteTextNoteSQL = "delete from textNote where textNoteID = ?";
	public String updateTextNoteSQL = "update textNote set unitID = ?,userID = ?,textNote = ?,share = ?,shareTime = ?,likes = ? where textNoteID = ?";
	public String insertPictureNoteSQL = "insert into pictureNote (pictureNoteID,unitID,userID,pictureNoteUrl,share,shareTime,likes) value(null,?,?,?,?,?,?)";
	public String selectPictureNoteSQL = "select * from pictureNote where unitID = ? and userID= ?";
	public String deletePictureNoteSQL = "delete from pictureNote where pictureNoteID = ?";
	public String shareTextNoteSQL = "UPDATE textNote SET share=1  WHERE unitID = ? and userID = ? and share=0 ";
	public String notShareTextNoteSQL = "UPDATE textNote SET share=0  WHERE unitID = ? and userID = ? and share=1";
	public String sharePictureNoteSQL = "UPDATE pictureNote SET share=1  WHERE unitID = ? and userID = ? and share=0 ";
	public String notSharePictureNoteSQL = "UPDATE pictureNote SET share=0  WHERE unitID = ? and userID = ? and share=1 ";
	
	public TextNote textNote;
	public PictureNote pictureNote;
	public Connection con = null;
	public Statement stat = null;
	public ResultSet result = null;
	public PreparedStatement pst = null; 
	
	public Random random = new Random();
	
	
	public NoteManager(){
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
	
	public void insertTextNoteTable(TextNote textNote){
		try {
			pst = con.prepareStatement(insertTextNoteSQL);
			pst.setInt(1,textNote.getUnitID());
			pst.setString(2,textNote.getUserID());
			pst.setString(3,textNote.getTextNote());
			pst.setInt(4,textNote.getShare());
			pst.setString(5,textNote.getShareTime());
			pst.setInt(6,textNote.getLikes());
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
		}
	}	
	public String selectTextNoteTable(int unitID,String userID) {
		ArrayList<TextNote> textNotes = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectTextNoteSQL);
			pst.setInt(1, unitID);
			pst.setString(2, userID);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 textNote = new TextNote();
				 textNote.setTextNoteID(result.getInt("textNoteID"));
				 textNote.setUnitID(result.getInt("unitID"));
				 textNote.setUserID(result.getString("userID"));
				 textNote.setTextNote(result.getString("textNote"));
				 textNote.setShare(result.getInt("share"));
				 textNote.setShareTime(result.getString("shareTime"));
				 textNote.setLikes(result.getInt("likes"));	
				 textNotes.add(textNote);
		     }
		}
			 catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(textNotes);
		return json;
	}
	public void deleteTextNoteTable(int textNoteID) {
		try {
			pst = con.prepareStatement(deleteTextNoteSQL);
			pst.setInt(1,textNoteID);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void updateTextNoteTable(TextNote textNote){
		try {
			pst = con.prepareStatement(updateTextNoteSQL);	
			pst.setInt(7,textNote.getTextNoteID());
			pst.setInt(1,textNote.getUnitID());
			pst.setString(2,textNote.getUserID());
			pst.setString(3,textNote.getTextNote());
			pst.setInt(4,textNote.getShare());
			pst.setString(5,textNote.getShareTime());
			pst.setInt(6,textNote.getLikes());
			
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	 
	public int insertPictureNoteTable(PictureNote pictureNote){
		try {
			pst = con.prepareStatement(insertPictureNoteSQL,Statement.RETURN_GENERATED_KEYS);
			//pst.setInt(1,);
			pst.setInt(1,pictureNote.getUnitID());
			pst.setString(2,pictureNote.getUserID());
			pst.setString(3,pictureNote.getPictureNoteUrl());
			pst.setInt(4,pictureNote.getShare());
			pst.setString(5,pictureNote.getShareTime());
			pst.setInt(6,pictureNote.getLikes());
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if (generatedKeys.next())
				return generatedKeys.getInt(1);		   			    
		}
		catch(SQLException x){
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
		}
		return -1;
		
	}	 
	public String selectPictureNoteTable(int unitID,String userID) {
		ArrayList<PictureNote> pictureNotes = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectPictureNoteSQL);
			System.out.println();
			pst.setInt(1,unitID);
			pst.setString(2,userID);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 
				 pictureNote = new PictureNote();
				 pictureNote.setPictureNoteID(result.getInt("pictureNoteID"));
				 pictureNote.setUnitID(result.getInt("unitID"));
				 pictureNote.setUserID(result.getString("userID"));
				 pictureNote.setPictureNoteUrl(result.getString("pictureNoteUrl"));
				 pictureNote.setShare(result.getInt("share"));
				 pictureNote.setShareTime(result.getString("shareTime"));
				 pictureNote.setLikes(result.getInt("likes"));
				 pictureNotes.add(pictureNote);
		     }
		}
			 catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(pictureNotes);
		return json;
	}
	public void deletePictureNoteTable(int pictureNoteID) {
		try {
			pst = con.prepareStatement(deletePictureNoteSQL);
			pst.setInt(1,pictureNoteID);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void shareTextNote(int unitID,String userID) {
		try {
			pst = con.prepareStatement(shareTextNoteSQL);
			pst.setInt(1,unitID);
			pst.setString(2,userID);			
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void notShareTextNote(int unitID,String userID) {
		try {
			pst = con.prepareStatement(notShareTextNoteSQL);
			pst.setInt(1,unitID);
			pst.setString(2,userID);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void sharePictureNote(int unitID,String userID) {
		try {
//			System.out.println("OKOKOKOK");
			pst = con.prepareStatement(sharePictureNoteSQL);
			pst.setInt(1,unitID);
			pst.setString(2,userID);			
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void notSharePictureNote(int unitID,String userID) {
		try {
			pst = con.prepareStatement(notSharePictureNoteSQL);
			pst.setInt(1,unitID);
			pst.setString(2,userID);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("Exception update"+x.toString());
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
