package Exchange;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import Note.PictureNote;
import Note.TextNote;
import KeyLabel.KeyLabel;

public class ExchangeManager {
	public String selectTextNoteSQL = "select * from text_note where share=1";
	public String selectPictureNoteSQL = "select * from picture_note where share=1";
	public String selectKeylabelSQL = "select * from keylabel where share=1";
	
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
	
	public void selectTextNoteTable(ArrayList<TextNote> textNotes) {
		try {
			stat = con.createStatement();
			result = stat.executeQuery(selectTextNoteSQL);
			 while(result.next()) 
		     { 	
				 textNote = new TextNote();
				 textNote.setText_note_id(result.getInt("text_note_id"));
				 textNote.setUnit_id(result.getInt("unit_id"));
				 textNote.setUser_id(result.getString("user_id"));
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
	}
	 
	public void selectPictureNoteTable(ArrayList<PictureNote> pictureNotes) {
		try {
			stat = con.createStatement();
			result = stat.executeQuery(selectPictureNoteSQL);
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
}
