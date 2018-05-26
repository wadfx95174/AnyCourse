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
	public String selectTextNoteSQL = "select * from account natural join textNote where unitId = ? and share=1 ";
	public String selectPictureNoteSQL = "select * from pictureNote where unitId = ? and share=1";
	public String selectKeylabelSQL = "select * from account natural join keylabel where unitId = ? and share=1";
	
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
	
	public String selectTextNoteTable(int unitId) {
		ArrayList<TextNote> textNotes = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectTextNoteSQL);
			pst.setInt(1, unitId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 textNote = new TextNote();
				 textNote.setTextNoteId(result.getInt("textNoteId"));
				 textNote.setUnitId(result.getInt("unitId"));
				 textNote.setUserId(result.getString("userId"));	
				 textNote.setNickName(result.getString("nickName"));
				 textNote.setTextNote(result.getString("textNote"));
				 textNote.setShare(result.getInt("share"));
				 textNote.setShareTime(result.getString("shareTime"));
				 textNote.setLikes(result.getInt("likes"));	
				 textNotes.add(textNote);
		     }
		}
		catch(SQLException x){
			System.out.println("ExchangeManager-selectTextNoteTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(textNotes);
		return json;
	}
	 
	public String selectPictureNoteTable(int unitId) {
		ArrayList<PictureNote> pictureNotes = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectPictureNoteSQL);
			pst.setInt(1, unitId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 
				 pictureNote = new PictureNote();
				 pictureNote.setPictureNoteId(result.getInt("pictureNoteId"));
				 pictureNote.setUnitId(result.getInt("unitId"));
				 pictureNote.setUserId(result.getString("userId"));
				 pictureNote.setPictureNoteUrl(result.getString("pictureNoteUrl"));
				 pictureNote.setShare(result.getInt("share"));
				 pictureNote.setShareTime(result.getString("shareTime"));
				 pictureNote.setLikes(result.getInt("likes"));
				 pictureNotes.add(pictureNote);
		     }
		}
		catch(SQLException x){
			System.out.println("ExchangeManager-selectPictureNoteTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(pictureNotes);
		return json;
	}
	
	public String selectKeyLabelTable(int unitId) {
		ArrayList<KeyLabel> keyLabels = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectKeylabelSQL);
			pst.setInt(1, unitId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 
				 keyLabel = new KeyLabel();
				 keyLabel.setKeyLabelId(result.getInt("keylabelId"));
				 keyLabel.setUnitId(result.getInt("unitId"));
				 keyLabel.setUserId(result.getString("userId"));
				 keyLabel.setNickName(result.getString("nickName"));
				 keyLabel.setKeyLabelName(result.getString("keylabelName"));
				 keyLabel.setBeginTime(result.getInt("beginTime"));
				 keyLabel.setEndTime(result.getInt("endTime"));
				 keyLabel.setShare(result.getInt("share"));
				 keyLabel.setShareTime(result.getString("shareTime"));
				 keyLabel.setLikes(result.getInt("likes"));
				 keyLabels.add(keyLabel);
		     }
		}
		catch(SQLException x){
			System.out.println("ExchangeManager-selectKeyLabelTable");
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
			System.out.println("ExchangeManager Close Exception :" + e.toString()); 
		}		
	} 
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("ExchangeManager Close Exception :" + e.toString()); 
		}
	}
}
