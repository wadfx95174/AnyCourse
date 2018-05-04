package Exchange;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;

import Note.PictureNote;
import Note.TextNote;
import KeyLabel.KeyLabel;

public class ExchangeManager {
	public String selectTextNoteSQL = "select * from account natural join text_note where unit_id = ? and share=1 ";
	public String selectPictureNoteSQL = "select * from picture_note where unit_id = ? and share=1";
	public String selectKeylabelSQL = "select * from account natural join keylabel where unit_id = ? and share=1";
	
	public TextNote textNote;
	public PictureNote pictureNote;
	public KeyLabel keyLabel;
	public Connection con = null;
	public Statement stat = null;
	public ResultSet result = null;
	public PreparedStatement pst = null; 
	
	public Random random = new Random();
	
	
	public ExchangeManager(){
		try {
			Class.forName("com.mysql.jdbc.Driver");//?��餃�??�river
			con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=Big5", "root", "peter");//���onnection			
		}
		catch(ClassNotFoundException e){
			System.out.println("DriverClassNotFound"+e.toString());
		}
		catch(SQLException x){
			System.out.println("Exception"+x.toString());
		}
	}
	
	public String selectTextNoteTable(int unit_id) {
		ArrayList<TextNote> textNotes = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectTextNoteSQL);
			pst.setInt(1, unit_id);
			result = pst.executeQuery();
//			stat = con.createStatement();
//			result = stat.executeQuery(selectTextNoteSQL);
			 while(result.next()) 
		     { 	
				 textNote = new TextNote();
				 textNote.setText_note_id(result.getInt("text_note_id"));
				 textNote.setUnit_id(result.getInt("unit_id"));
				 textNote.setUser_id(result.getString("user_id"));	
				 textNote.setNick_name(result.getString("nick_name"));
				 textNote.setText_note(result.getString("text_note"));
			//	 textNote.setPicture_note_url(result.getString("picture_note_url"));
				 textNote.setShare(result.getInt("share"));
				 textNote.setShare_time(result.getString("share_time"));
				 textNote.setLikes(result.getInt("likes"));	
				 textNotes.add(textNote);
		     }
//			 System.out.println(textNotes);
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
	 
	public String selectPictureNoteTable(int unit_id) {
		ArrayList<PictureNote> pictureNotes = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectPictureNoteSQL);
			pst.setInt(1, unit_id);
			result = pst.executeQuery();
//			stat = con.createStatement();
//			result = stat.executeQuery(selectPictureNoteSQL);
			 while(result.next()) 
		     { 
				 pictureNote = new PictureNote();
				 pictureNote.setPicture_note_id(result.getInt("picture_note_id"));
				 pictureNote.setUnit_id(result.getInt("unit_id"));
				 pictureNote.setUser_id(result.getString("user_id"));
				 pictureNote.setPicture_note_url(result.getString("picture_note_url"));
				 pictureNote.setShare(result.getInt("share"));
				 pictureNote.setShare_time(result.getString("share_time"));
				 pictureNote.setLikes(result.getInt("likes"));
				 pictureNotes.add(pictureNote);
		     }
//			 System.out.println(pictureNotes);
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
	
	public String selectKeyLabelTable(int unit_id) {
		ArrayList<KeyLabel> keyLabels = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectKeylabelSQL);
			pst.setInt(1, unit_id);
			result = pst.executeQuery();
//			stat = con.createStatement();
//			result = stat.executeQuery(selectKeylabelSQL);
			 while(result.next()) 
		     { 
				 keyLabel = new KeyLabel();
				 keyLabel.setKeyLabelId(result.getInt("keylabel_id"));
				 keyLabel.setUnitId(result.getInt("unit_id"));
				 keyLabel.setUserId(result.getString("user_id"));
				 keyLabel.setNick_name(result.getString("nick_name"));
				 keyLabel.setKeyLabelName(result.getString("keylabel_name"));
				 keyLabel.setBeginTime(result.getInt("begin_time"));
				 keyLabel.setEndTime(result.getInt("end_time"));
				 keyLabel.setShare(result.getInt("share"));
				 keyLabel.setShareTime(result.getString("share_time"));
				 keyLabel.setLikes(result.getInt("likes"));
				 keyLabels.add(keyLabel);
		     }
//			 System.out.println(pictureNotes);
		}
			 catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(keyLabels);
		return json;
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
