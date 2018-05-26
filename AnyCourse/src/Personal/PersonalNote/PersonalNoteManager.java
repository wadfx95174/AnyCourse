package Personal.PersonalNote;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

import Personal.WatchRecord.WatchRecord;

public class PersonalNoteManager {
	public String selectPersonalTextNoteSQL = "select * from textNote , unit where textNote.userId = ? and textNote.unitId = unit.unitId ";
	public String selectPersonalPictureNoteSQL = "select * from pictureNote , unit where pictureNote.userId = ? and pictureNote.unitId = unit.unitId ";
	
	public PersonalTextNote personalTextNote;
	public PersonalPictureNote personalPictureNote;
	
	public Connection con = null;
	public Statement stat = null;
	public ResultSet result = null;
	public PreparedStatement pst = null;
	
	public PersonalNoteManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=Big5"
					, "root", "peter");
		}
		catch(ClassNotFoundException e){
			System.out.println("DriverClassNotFound"+e.toString());
		}
		catch(SQLException x){
			System.out.println("Exception" + x.toString());
		}
	}
	
	public String selectPersonalTextNoteTable(String userId) {
		ArrayList<PersonalTextNote> personalTextNotes = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectPersonalTextNoteSQL);
			pst.setString(1, userId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 				 
				 personalTextNote = new PersonalTextNote();
				 personalTextNote.setTextNoteId(result.getInt("textNoteId"));
				 personalTextNote.setUserId(result.getString("userId"));
				 personalTextNote.setUnitId(result.getInt("unitId"));
				 personalTextNote.setTextNote(result.getString("textNote"));
				 personalTextNote.setShare(result.getInt("share"));
				 personalTextNote.setShareTime(result.getString("shareTime"));
				 personalTextNote.setUnitName(result.getString("unitName"));
				 personalTextNote.setSchoolName(result.getString("schoolName"));				 
				 personalTextNote.setVideoUrl(result.getString("videoUrl"));
				 personalTextNote.setLikes(result.getInt("likes"));				 
				 personalTextNotes.add(personalTextNote);
		     }
		}
		catch(SQLException x){
			System.out.println("PersonalNoteManager-selectPersonalTextNoteTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(personalTextNotes);
		return json;
	}
	
	public String selectPersonalPictureNoteTable(String userId) {
		ArrayList<PersonalPictureNote> personalPictureNotes = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectPersonalPictureNoteSQL);
			pst.setString(1, userId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 				 
				 personalPictureNote = new PersonalPictureNote();
				 personalPictureNote.setPictureNoteId(result.getInt("pictureNoteId"));
				 personalPictureNote.setUserId(result.getString("userId"));
				 personalPictureNote.setUnitId(result.getInt("unitId"));
				 personalPictureNote.setPictureNoteUrl(result.getString("pictureNoteUrl"));
				 personalPictureNote.setShare(result.getInt("share"));
				 personalPictureNote.setShareTime(result.getString("shareTime"));
				 personalPictureNote.setUnitName(result.getString("unitName"));
				 personalPictureNote.setSchoolName(result.getString("schoolName"));				 
				 personalPictureNote.setVideoUrl(result.getString("videoUrl"));
				 personalPictureNote.setLikes(result.getInt("likes"));				 
				 personalPictureNotes.add(personalPictureNote);
		     }
		}
		catch(SQLException x){
			System.out.println("PersonalNoteManager-selectPersonalPictureNoteTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(personalPictureNotes);
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
			System.out.println("PersonalNoteManager Close Exception :" + e.toString()); 
		}		
	} 
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("PersonalNoteManager Close Exception :" + e.toString()); 
		}
	}
}
