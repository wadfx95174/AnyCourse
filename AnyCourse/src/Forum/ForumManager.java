package Forum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;

import Forum.Comment;
import Forum.Reply;;

public class ForumManager {
	public String insertCommentSQL = "insert into comment (commentID,unitID,userID,nickName,commentTime,commentContent) value(null,?,?,?,null,?)";
	public String selectCommentSQL = "select * from comment where unitID= ? ";
	public String deleteCommentSQL = "delete from comment where commentID = ?";
	public String updateCommentSQL = "update comment set unitID = ?,userID = ?,nickName = ?,commentTime = ?,commentContent = ? where commentID = ?";
	
	public String insertReplySQL = "insert into reply (replyID,commentID,userID,nickName,replyTime,replyContent) value(null,?,?,?,null,?)";
	public String selectReplySQL = "select * from reply";
	public String deleteReplySQL = "delete from reply where replyID = ?";
	public String deleteReplySQL2 = "delete from reply where commentID = ?";
	public String updateReplySQL = "update reply set commentID = ?,userID = ?,nickName = ?,replyTime = ?,replyContent = ? where replyID = ?";
	
	public Comment comment;
	public Reply reply;
	public Connection con = null;
	public Statement stat = null;
	public ResultSet result = null;
	public PreparedStatement pst = null; 
	
	public Random random = new Random();
	
	
	public ForumManager(){
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
	
	public Comment insertCommentTable(Comment comment){
		try {
			pst = con.prepareStatement(insertCommentSQL,Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1,comment.getUnitID());
			pst.setString(2,comment.getUserID());
			pst.setString(3,comment.getNickName());
			pst.setString(4,comment.getCommentContent());
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if (generatedKeys.next())
			{
				int id =generatedKeys.getInt(1);
				pst = con.prepareStatement("SELECT * FROM comment WHERE commentID = ?");
				pst.setInt(1,id);
				result = pst.executeQuery();
				 while(result.next()) 
			     { 
				 comment = new Comment();
				 comment.setCommentID(result.getInt("commentID"));
				 comment.setUnitID(result.getInt("unitID"));
				 comment.setUserID(result.getString("userID"));
				 comment.setNickName(result.getString("nickName"));
				 comment.setCommentTime(result.getString("commentTime"));
				 comment.setCommentContent(result.getString("commentContent"));
			     }
				 return comment;
			}
		}
		catch(SQLException x){
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
		}
		return comment;
	}	
	public String selectCommentTable(int unitID) {
		ArrayList<Comment> comments = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectCommentSQL);
			pst.setInt(1, unitID);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 comment = new Comment();
				 comment.setCommentID(result.getInt("commentID"));
				 comment.setUnitID(result.getInt("unitID"));
				 comment.setUserID(result.getString("userID"));
				 comment.setNickName(result.getString("nickName"));
				 comment.setCommentTime(result.getString("commentTime"));
				 comment.setCommentContent(result.getString("commentContent"));
				 comments.add(comment);
		     }
		}
			 catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(comments);
		return json;
	}
	public void deleteCommentTable(int commentID) {
		try {
			pst = con.prepareStatement(deleteCommentSQL);
			pst.setInt(1,commentID);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void updateCommentTable(Comment comment){
		try {
			pst = con.prepareStatement(updateCommentSQL);	
			pst.setInt(6,comment.getCommentID());
			pst.setInt(1,comment.getUnitID());
			pst.setString(2,comment.getUserID());
			pst.setString(3,comment.getNickName());
			pst.setString(4,comment.getCommentTime());
			pst.setString(5,comment.getCommentContent());
			
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	 
	public Reply insertReplyTable(Reply reply){
		try {
			pst = con.prepareStatement(insertReplySQL,Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1,reply.getCommentID());
			pst.setString(2,reply.getUserID());
			pst.setString(3,reply.getNickName());
			pst.setString(4,reply.getReplyContent());
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if (generatedKeys.next())
			{
				int id =generatedKeys.getInt(1);
				pst = con.prepareStatement("SELECT * FROM reply WHERE replyID = ?");
				pst.setInt(1,id);
				result = pst.executeQuery();
				 while(result.next()) 
			     { 
				 reply = new Reply();
				 reply.setReplyID(result.getInt("replyID"));
				 reply.setCommentID(result.getInt("commentID"));
				 reply.setUserID(result.getString("userID"));
				 reply.setNickName(result.getString("nickName"));
				 reply.setReplyTime(result.getString("replyTime"));
				 reply.setReplyContent(result.getString("replyContent"));
			     }
				 return reply;
			}
		}
		catch(SQLException x){
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
			
		}
		return reply;
	}	 
	public void selectReplyTable(ArrayList<Reply> replys) {
		try {
			stat = con.createStatement();
			result = stat.executeQuery(selectReplySQL);
			 while(result.next()) 
		     { 
				 reply = new Reply();
				 reply.setReplyID(result.getInt("replyID"));
				 reply.setCommentID(result.getInt("commentID"));
				 reply.setUserID(result.getString("userID"));
				 reply.setNickName(result.getString("nickName"));
				 reply.setReplyTime(result.getString("replyTime"));
				 reply.setReplyContent(result.getString("replyContent"));
				 replys.add(reply);
		     }
		}
			 catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void deleteReplyTable(int replyID) {
		try {
			pst = con.prepareStatement(deleteReplySQL);
			pst.setInt(1,replyID);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}  
	public void deleteReplyTable2(int commentID) {
		try {
			pst = con.prepareStatement(deleteReplySQL2);
			pst.setInt(1,commentID);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void updateReplyTable(Reply reply){
		try {
			pst = con.prepareStatement(updateReplySQL);	
			pst.setInt(6,reply.getReplyID());
			pst.setInt(1,reply.getCommentID());
			pst.setString(2,reply.getUserID());
			pst.setString(3,reply.getNickName());
			pst.setString(4,reply.getReplyTime());
			pst.setString(5,reply.getReplyContent());
			
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
