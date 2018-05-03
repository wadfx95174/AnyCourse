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
	public String selectPersonalTextNoteSQL = "select * from text_note natural join unit where text_note.user_id = ? and text_note.unit_id = unit.unit_id ";
//	public String deletePersonalTextNoteSQL = "delete from text_note where user_id = ? and unit_id = ?";
	public String selectPersonalPictureNoteSQL = "select * from picture_note natural join unit where picture_note.user_id = ? and picture_note.unit_id = unit.unit_id ";
//	public String deletePersonalPictureNoteSQL = "delete from picture_note where user_id = ? and unit_id = ?";
	
	public PersonalTextNote personalTextNote;
	public PersonalPictureNote personalPictureNote;
	
	public Connection con = null;
	public Statement stat = null;
	public ResultSet result = null;
	public PreparedStatement pst = null;
	
	public PersonalNoteManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");//閮餃�river

			con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=Big5"
					, "root", "peter");//���onnection
			
		}
		catch(ClassNotFoundException e){
			System.out.println("DriverClassNotFound"+e.toString());
		}
		catch(SQLException x){
			System.out.println("Exception" + x.toString());
		}
	}
	//�������������
	public String selectPersonalTextNoteTable(String user_id) {
		ArrayList<PersonalTextNote> personalTextNotes = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectPersonalTextNoteSQL);
			pst.setString(1, user_id);
			result = pst.executeQuery();
//			stat = con.createStatement();
//			result = stat.executeQuery(selectWatchRecordSQL);
			 while(result.next()) 
		     { 				 
				 personalTextNote = new PersonalTextNote();
				 personalTextNote.setText_note_id(result.getInt("text_note_id"));
				 personalTextNote.setUser_id(result.getString("user_id"));
				 personalTextNote.setUnit_id(result.getInt("unit_id"));
				 personalTextNote.setText_note(result.getString("text_note"));
				 personalTextNote.setShare(result.getInt("share"));
				 personalTextNote.setShare_time(result.getString("share_time"));
				 personalTextNote.setUnit_name(result.getString("unit_name"));
				 personalTextNote.setSchool_name(result.getString("school_name"));				 
				 personalTextNote.setVideo_url(result.getString("video_url"));
				 personalTextNote.setLikes(result.getInt("likes"));				 
				 personalTextNotes.add(personalTextNote);
		     }
		}
			 catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(personalTextNotes);
		return json;
	}
//	//����������
//	public void deleteWatchRecordTable(String user_id,int unit_id) {
//		try {
//			pst = con.prepareStatement(deleteWatchRecordSQL);
//			pst.setString(1,user_id);
//			pst.setInt(2,unit_id);
//			pst.executeUpdate();
//		}
//		catch(SQLException x){
//			System.out.println("Exception delete"+x.toString());
//		}
//		finally {
//			Close();
//		}
//	}
	
	public String selectPersonalPictureNoteTable(String user_id) {
		ArrayList<PersonalPictureNote> personalPictureNotes = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectPersonalPictureNoteSQL);
			pst.setString(1, user_id);
			result = pst.executeQuery();
//			stat = con.createStatement();
//			result = stat.executeQuery(selectWatchRecordSQL);
			 while(result.next()) 
		     { 				 
				 personalPictureNote = new PersonalPictureNote();
				 personalPictureNote.setPicture_note_id(result.getInt("picture_note_id"));
				 personalPictureNote.setUser_id(result.getString("user_id"));
				 personalPictureNote.setUnit_id(result.getInt("unit_id"));
				 personalPictureNote.setPicture_note_url(result.getString("picture_note_url"));
				 personalPictureNote.setShare(result.getInt("share"));
				 personalPictureNote.setShare_time(result.getString("share_time"));
				 personalPictureNote.setUnit_name(result.getString("unit_name"));
				 personalPictureNote.setSchool_name(result.getString("school_name"));				 
				 personalPictureNote.setVideo_url(result.getString("video_url"));
				 personalPictureNote.setLikes(result.getInt("likes"));				 
				 personalPictureNotes.add(personalPictureNote);
		     }
		}
			 catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(personalPictureNotes);
		return json;
	}
//	//����������
//	public void deleteWatchRecordTable(String user_id,int unit_id) {
//		try {
//			pst = con.prepareStatement(deleteWatchRecordSQL);
//			pst.setString(1,user_id);
//			pst.setInt(2,unit_id);
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
}
