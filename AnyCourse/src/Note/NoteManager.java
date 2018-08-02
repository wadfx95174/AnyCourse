package Note;

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
	public String insertTextNoteSQL = "insert into textNote (textNoteId,unitId,userId,textNote,share,shareTime,likes,categoryId) value(null,?,?,?,?,?,?,?)";
	public String selectTextNoteSQL = "select * from textNote where unitId = ? and userId= ? ";
	public String deleteTextNoteSQL = "delete from textNote where textNoteId = ?";
	public String updateTextNoteSQL = "update textNote set unitId = ?,userId = ?,textNote = ?,share = ?,shareTime = ?,likes = ?,categoryId = ? where textNoteId = ?";
	public String insertPictureNoteSQL = "insert into pictureNote (pictureNoteId,unitId,userId,pictureNoteUrl,share,shareTime,likes,categoryId) value(null,?,?,?,?,?,?,?)";
	public String selectPictureNoteSQL = "select * from pictureNote where unitId = ? and userId= ?";
	public String deletePictureNoteSQL = "delete from pictureNote where pictureNoteId = ?";
	public String shareTextNoteSQL = "UPDATE textNote SET share=1  WHERE unitId = ? and userId = ? and share=0 ";
	public String notShareTextNoteSQL = "UPDATE textNote SET share=0  WHERE unitId = ? and userId = ? and share=1";
	public String sharePictureNoteSQL = "UPDATE pictureNote SET share=1  WHERE unitId = ? and userId = ? and share=0 ";
	public String notSharePictureNoteSQL = "UPDATE pictureNote SET share=0  WHERE unitId = ? and userId = ? and share=1 ";
	
	public String insertNoteCategorySQL = "insert into noteCategory (userId,categoryId,categoryName) value(?,null,?)";
	public String selectNoteCategorySQL = "select * from noteCategory where userId= ? ";
	
	public TextNote textNote;
	public PictureNote pictureNote;
	public NoteCategory noteCategory;
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
	
	public void insertNoteCategoryTable(NoteCategory noteCategory){
		try {
			pst = con.prepareStatement(insertNoteCategorySQL);
			pst.setString(1,noteCategory.getUserId());
			pst.setString(2,noteCategory.getCategoryName());
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("NoteManager-insertNoteCategoryTable");
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
		}
	}
	public String selectNoteCategoryTable(String userId) {
		ArrayList<NoteCategory> noteCategorys = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectNoteCategorySQL);
			pst.setString(1, userId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 noteCategory = new NoteCategory();
				 noteCategory.setUserId(result.getString("userId"));
				 noteCategory.setCategoryId(result.getInt("categoryId"));
				 noteCategory.setCategoryName(result.getString("categoryName"));					
				 noteCategorys.add(noteCategory);
		     }
		}
		catch(SQLException x){
			System.out.println("NoteManager-selectNoteCategoryTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(noteCategorys);
		return json;
	}
	
	
	public void insertTextNoteTable(TextNote textNote){
		try {
			pst = con.prepareStatement(insertTextNoteSQL);
			pst.setInt(1,textNote.getUnitId());
			pst.setString(2,textNote.getUserId());
			pst.setString(3,textNote.getTextNote());
			pst.setInt(4,textNote.getShare());
			pst.setString(5,textNote.getShareTime());
			pst.setInt(6,textNote.getLikes());
			pst.setInt(7,textNote.getCategoryId());
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("NoteManager-insertTextNoteTable");
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
		}
	}	
	public String selectTextNoteTable(int unitId,String userId) {
		ArrayList<TextNote> textNotes = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectTextNoteSQL);
			pst.setInt(1, unitId);
			pst.setString(2, userId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 textNote = new TextNote();
				 textNote.setTextNoteId(result.getInt("textNoteId"));
				 textNote.setUnitId(result.getInt("unitId"));
				 textNote.setUserId(result.getString("userId"));
				 textNote.setTextNote(result.getString("textNote"));
				 textNote.setShare(result.getInt("share"));
				 textNote.setShareTime(result.getString("shareTime"));
				 textNote.setLikes(result.getInt("likes"));	
				 textNotes.add(textNote);
		     }
		}
		catch(SQLException x){
			System.out.println("NoteManager-selectTextNoteTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(textNotes);
		return json;
	}
	public void deleteTextNoteTable(int textNoteId) {
		try {
			pst = con.prepareStatement(deleteTextNoteSQL);
			pst.setInt(1,textNoteId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("NoteManager-deleteTextNoteTable");
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void updateTextNoteTable(TextNote textNote){
		try {
			pst = con.prepareStatement(updateTextNoteSQL);	
			pst.setInt(8,textNote.getTextNoteId());
			pst.setInt(1,textNote.getUnitId());
			pst.setString(2,textNote.getUserId());
			pst.setString(3,textNote.getTextNote());
			pst.setInt(4,textNote.getShare());
			pst.setString(5,textNote.getShareTime());
			pst.setInt(6,textNote.getLikes());
			pst.setInt(7,textNote.getCategoryId());
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("NoteManager-updateTextNoteTable");
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	 
	public int insertPictureNoteTable(PictureNote pictureNote){
		try {
			pst = con.prepareStatement(insertPictureNoteSQL,Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1,pictureNote.getUnitId());
			pst.setString(2,pictureNote.getUserId());
			pst.setString(3,pictureNote.getPictureNoteUrl());
			pst.setInt(4,pictureNote.getShare());
			pst.setString(5,pictureNote.getShareTime());
			pst.setInt(6,pictureNote.getLikes());
			pst.setInt(7,pictureNote.getCategoryId());
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if (generatedKeys.next())
				return generatedKeys.getInt(1);		   			    
		}
		catch(SQLException x){
			System.out.println("NoteManager-insertPictureNoteTable");
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
		}
		return -1;
		
	}	 
	public String selectPictureNoteTable(int unitId,String userId) {
		ArrayList<PictureNote> pictureNotes = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectPictureNoteSQL);
			pst.setInt(1,unitId);
			pst.setString(2,userId);
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
			System.out.println("NoteManager-selectPictureNoteTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(pictureNotes);
		return json;
	}
	public void deletePictureNoteTable(int pictureNoteId) {
		try {
			pst = con.prepareStatement(deletePictureNoteSQL);
			pst.setInt(1,pictureNoteId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("NoteManager-deletePictureNoteTable");
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void shareTextNote(int unitId,String userId) {
		try {
			pst = con.prepareStatement(shareTextNoteSQL);
			pst.setInt(1,unitId);
			pst.setString(2,userId);			
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("NoteManager-shareTextNote");
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void notShareTextNote(int unitId,String userId) {
		try {
			pst = con.prepareStatement(notShareTextNoteSQL);
			pst.setInt(1,unitId);
			pst.setString(2,userId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("NoteManager-notShareTextNote");
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void sharePictureNote(int unitId,String userId) {
		try {
			pst = con.prepareStatement(sharePictureNoteSQL);
			pst.setInt(1,unitId);
			pst.setString(2,userId);			
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("NoteManager-sharePictureNote");
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void notSharePictureNote(int unitId,String userId) {
		try {
			pst = con.prepareStatement(notSharePictureNoteSQL);
			pst.setInt(1,unitId);
			pst.setString(2,userId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("NoteManager-notSharePictureNote");
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
			System.out.println("NoteManager Close Exception :" + e.toString()); 
		}		
	} 
	
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("NoteManager Close Exception :" + e.toString()); 
		}
	}
}
