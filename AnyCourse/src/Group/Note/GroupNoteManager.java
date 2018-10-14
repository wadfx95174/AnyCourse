package Group.Note;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

import Group.Note.*;

public class GroupNoteManager {
	public String selectGroupNameSQL = "select * from ggroup, groupMember where groupMember.userId = ? and ggroup.groupId = groupMember.groupId ";
	public String insertGroupNoteMatchSQL = "insert ignore into groupNoteMatch (groupId,userId,unitId) value(?,?,?)";
	public String deleteGroupNoteMatchSQL = "delete from groupNoteMatch where groupId = ? AND userId = ? AND unitId = ?";
	public String selectGroupTextNoteSQL = "select * from groupNoteMatch,textNote , unit where groupNoteMatch.groupId = ? and groupNoteMatch.userId = textNote.userId and groupNoteMatch.unitId = textNote.unitId and textNote.unitId = unit.unitId";
	public String selectGroupPictureNoteSQL = "select * from groupNoteMatch,pictureNote , unit where groupNoteMatch.groupId = ? and groupNoteMatch.userId = pictureNote.userId and groupNoteMatch.unitId = pictureNote.unitId and pictureNote.unitId = unit.unitId";
	
	public GroupName groupName;
	public GroupTextNote groupTextNote;
	public GroupPictureNote groupPictureNote;
	
	public Connection con = null;
	public Statement stat = null;
	public ResultSet result = null;
	public PreparedStatement pst = null;
	
	public GroupNoteManager() {
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
	
	public String selectGroupName(String userId) {
		ArrayList<GroupName> groupNames = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectGroupNameSQL);
			pst.setString(1, userId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 				 
				 groupName = new GroupName();
				 groupName.setGroupId(result.getInt("groupId"));
				 groupName.setGroupName(result.getString("groupName"));				 
				 groupNames.add(groupName);
		     }
		}
		catch(SQLException x){
			System.out.println("GroupNoteManager-selectGroupName");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(groupNames);
		return json;
	}
	
	public void insertGroupNoteMatchTable(GroupNote groupNote){
		try {
			pst = con.prepareStatement(insertGroupNoteMatchSQL);
			pst.setInt(1,groupNote.getGroupId());
			pst.setString(2,groupNote.getUserId());
			pst.setInt(3,groupNote.getUnitId());
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("GroupNoteManager-insertGroupNoteMatchTable");
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	public void deleteGroupNoteMatchTable(GroupNote groupNote) {
		try {
			pst = con.prepareStatement(deleteGroupNoteMatchSQL);
			pst.setInt(1,groupNote.getGroupId());
			pst.setString(2,groupNote.getUserId());
			pst.setInt(3,groupNote.getUnitId());
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("GroupNoteManager-deleteGroupNoteMatchTable");
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	public String selectGroupTextNoteTable(int groupId) {
		ArrayList<GroupTextNote> groupTextNotes = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectGroupTextNoteSQL);
			pst.setInt(1, groupId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 				 
				 groupTextNote = new GroupTextNote();
				 groupTextNote.setGroupId(result.getInt("groupId"));
				 groupTextNote.setTextNoteId(result.getInt("textNoteId"));
				 groupTextNote.setUserId(result.getString("userId"));
				 groupTextNote.setUnitId(result.getInt("unitId"));
				 groupTextNote.setTextNote(result.getString("textNote"));
				 groupTextNote.setShare(result.getInt("share"));
				 groupTextNote.setShareTime(result.getString("shareTime"));
				 groupTextNote.setUnitName(result.getString("unitName"));
				 groupTextNote.setSchoolName(result.getString("schoolName"));				 
				 groupTextNote.setVideoUrl(result.getString("videoUrl"));
				 groupTextNote.setLikes(result.getInt("likes"));				 
				 groupTextNotes.add(groupTextNote);
		     }
		}
		catch(SQLException x){
			System.out.println("GroupNoteManager-selectGroupTextNoteTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(groupTextNotes);
		return json;
	}
	
	public String selectGroupPictureNoteTable(int groupId) {
		ArrayList<GroupPictureNote> groupPictureNotes = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectGroupPictureNoteSQL);
			pst.setInt(1, groupId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 				 
				 groupPictureNote = new GroupPictureNote();
				 groupPictureNote.setGroupId(result.getInt("groupId"));
				 groupPictureNote.setPictureNoteId(result.getInt("pictureNoteId"));
				 groupPictureNote.setUserId(result.getString("userId"));
				 groupPictureNote.setUnitId(result.getInt("unitId"));
				 groupPictureNote.setPictureNoteUrl(result.getString("pictureNoteUrl"));
				 groupPictureNote.setShare(result.getInt("share"));
				 groupPictureNote.setShareTime(result.getString("shareTime"));
				 groupPictureNote.setUnitName(result.getString("unitName"));
				 groupPictureNote.setSchoolName(result.getString("schoolName"));				 
				 groupPictureNote.setVideoUrl(result.getString("videoUrl"));
				 groupPictureNote.setLikes(result.getInt("likes"));				 
				 groupPictureNotes.add(groupPictureNote);
		     }
		}
		catch(SQLException x){
			System.out.println("GroupNoteManager-selectGroupPictureNoteTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(groupPictureNotes);
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
