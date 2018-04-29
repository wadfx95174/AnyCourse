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
import java.util.Random;;

public class NoteManager {
	public String insertTextNoteSQL = "insert into text_note (text_note_id,unit_id,user_id,text_note,share,share_time,likes) value(null,?,?,?,?,?,?)";
	public String selectTextNoteSQL = "select * from text_note ";
	public String deleteTextNoteSQL = "delete from text_note where text_note_id = ?";
	public String updateTextNoteSQL = "update text_note set unit_id = ?,user_id = ?,text_note = ?,share = ?,share_time = ?,likes = ? where text_note_id = ?";
	public String insertPictureNoteSQL = "insert into picture_note (picture_note_id,unit_id,user_id,picture_note_url,share,share_time,likes) value(null,?,?,?,?,?,?)";
	public String selectPictureNoteSQL = "select * from picture_note ";
	public String deletePictureNoteSQL = "delete from picture_note where picture_note_id = ?";
	
	public TextNote textNote;
	public PictureNote pictureNote;
	public Connection con = null;
	public Statement stat = null;
	public ResultSet result = null;
	public PreparedStatement pst = null; 
	
	public Random random = new Random();
	
	
	public NoteManager(){
		try {
			Class.forName("com.mysql.jdbc.Driver");//?��餃�??�river
			con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?useUnicode=true&characterEncoding=Big5", "root", "peter");//���onnection
			
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
			//pst.setInt(1,);
			pst.setInt(1,textNote.getUnit_id());
			pst.setString(2,textNote.getUser_id());
			pst.setString(3,textNote.getText_note());
		//	pst.setString(4,textNote.getPicture_note_url());
			pst.setInt(4,textNote.getShare());
			pst.setString(5,textNote.getShare_time());
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
	public void deleteTextNoteTable(int text_note_id) {
		try {
			pst = con.prepareStatement(deleteTextNoteSQL);
			pst.setInt(1,text_note_id);
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
			pst.setInt(7,textNote.getText_note_id());
			pst.setInt(1,textNote.getUnit_id());
			pst.setString(2,textNote.getUser_id());
			pst.setString(3,textNote.getText_note());
			pst.setInt(4,textNote.getShare());
			pst.setString(5,textNote.getShare_time());
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
			pst.setInt(1,pictureNote.getUnit_id());
			pst.setString(2,pictureNote.getUser_id());
			pst.setString(3,pictureNote.getPicture_note_url());
			pst.setInt(4,pictureNote.getShare());
			pst.setString(5,pictureNote.getShare_time());
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
	public void deletePictureNoteTable(int picture_note_id) {
		try {
			pst = con.prepareStatement(deletePictureNoteSQL);
			pst.setInt(1,picture_note_id);
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