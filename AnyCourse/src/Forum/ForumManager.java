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
	public String insertCommentSQL = "insert into comment (commentId,unitId,userId,nickName,commentTime,commentContent) value(null,?,?,?,null,?)";
	public String selectCommentSQL = "select * from comment where unitId= ? ";
	public String deleteCommentSQL = "delete from comment where commentId = ?";
	public String updateCommentSQL = "update comment set unitId = ?,userId = ?,nickName = ?,commentTime = ?,commentContent = ? where commentId = ?";
	
	public String insertReplySQL = "insert into reply (replyId,commentId,userId,nickName,replyTime,replyContent) value(null,?,?,?,null,?)";
	public String selectReplySQL = "select * from reply,comment where unitId = ? and reply.commentId = comment.commentId";
	public String deleteReplySQL = "delete from reply where replyId = ?";
	public String deleteReplySQL2 = "delete from reply where commentId = ?";
	public String updateReplySQL = "update reply set commentId = ?,userId = ?,nickName = ?,replyTime = ?,replyContent = ? where replyId = ?";
	
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
			pst.setInt(1,comment.getUnitId());
			pst.setString(2,comment.getUserId());
			pst.setString(3,comment.getNickName());
			pst.setString(4,comment.getCommentContent());
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if (generatedKeys.next())
			{
				int id =generatedKeys.getInt(1);
				pst = con.prepareStatement("SELECT * FROM comment WHERE commentId = ?");
				pst.setInt(1,id);
				result = pst.executeQuery();
				 while(result.next()) 
			     { 
				 comment = new Comment();
				 comment.setCommentId(result.getInt("commentId"));
				 comment.setUnitId(result.getInt("unitId"));
				 comment.setUserId(result.getString("userId"));
				 comment.setNickName(result.getString("nickName"));
				 comment.setCommentTime(result.getString("commentTime"));
				 comment.setCommentContent(result.getString("commentContent"));
			     }
				 return comment;
			}
		}
		catch(SQLException x){
			System.out.println("ForumManager-insertCommentTable");
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
		}
		return comment;
	}	
	public String selectCommentTable(int unitId) {
		ArrayList<Comment> comments = new ArrayList<>();
		try {
			pst = con.prepareStatement(selectCommentSQL);
			pst.setInt(1, unitId);
			result = pst.executeQuery();
			 while(result.next()) 
		     { 	
				 comment = new Comment();
				 comment.setCommentId(result.getInt("commentId"));
				 comment.setUnitId(result.getInt("unitId"));
				 comment.setUserId(result.getString("userId"));
				 comment.setNickName(result.getString("nickName"));
				 comment.setCommentTime(result.getString("commentTime"));
				 comment.setCommentContent(result.getString("commentContent"));
				 comments.add(comment);
		     }
		}
		catch(SQLException x){
			System.out.println("ForumManager-selectCommentTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		String json = new Gson().toJson(comments);
		return json;
	}
	public void deleteCommentTable(int commentId) {
		try {
			pst = con.prepareStatement(deleteCommentSQL);
			pst.setInt(1,commentId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("ForumManager-deleteCommentTable");
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void updateCommentTable(Comment comment){
		try {
			pst = con.prepareStatement(updateCommentSQL);	
			pst.setInt(6,comment.getCommentId());
			pst.setInt(1,comment.getUnitId());
			pst.setString(2,comment.getUserId());
			pst.setString(3,comment.getNickName());
			pst.setString(4,comment.getCommentTime());
			pst.setString(5,comment.getCommentContent());
			
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("ForumManager-updateCommentTable");
			System.out.println("Exception update"+x.toString());
		}
		finally {
			Close();
		}
	}
	
	 
	public Reply insertReplyTable(Reply reply){
		try {			
			pst = con.prepareStatement(insertReplySQL,Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1,reply.getCommentId());
			pst.setString(2,reply.getUserId());
			pst.setString(3,reply.getNickName());
			pst.setString(4,reply.getReplyContent());
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if (generatedKeys.next())
			{
				int id = generatedKeys.getInt(1);
				pst = con.prepareStatement("select * from reply,comment where replyId = ? and reply.commentId = comment.commentId");
				pst.setInt(1,id);
				result = pst.executeQuery();
				 while(result.next()) 
			     { 
					 reply = new Reply();
					 reply.setReplyId(result.getInt("replyId"));
					 reply.setCommentId(result.getInt("reply.commentId"));
					 reply.setUserId(result.getString("reply.userId"));
					 reply.setNickName(result.getString("nickName"));
					 reply.setReplyTime(result.getString("replyTime"));
					 reply.setReplyContent(result.getString("replyContent"));
					 reply.setCommentUserId(result.getString("comment.userId"));
			     }
				 return reply;
			}
		}
		catch(SQLException x){
			System.out.println("ForumManager-insertReplyTable");
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
			
		}
		return reply;
	}	 
	public void selectReplyTable(ArrayList<Reply> replys,int unitId) {
		try {
			pst = con.prepareStatement(selectReplySQL);
			pst.setInt(1,unitId);
			result = pst.executeQuery();
			 while(result.next())
		     { 
				 reply = new Reply();
				 reply.setReplyId(result.getInt("replyId"));
				 reply.setCommentId(result.getInt("commentId"));
				 reply.setUserId(result.getString("userId"));
				 reply.setNickName(result.getString("nickName"));
				 reply.setReplyTime(result.getString("replyTime"));
				 reply.setReplyContent(result.getString("replyContent"));
				 reply.setCommentUserId(result.getString("comment.userId"));
				 replys.add(reply);
		     }
		}
		catch(SQLException x){
			System.out.println("ForumManager-selectReplyTable");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void deleteReplyTable(int replyId) {
		try {
			pst = con.prepareStatement(deleteReplySQL);
			pst.setInt(1,replyId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("ForumManager-deleteReplyTable");
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}  
	public void deleteReplyTable2(int commentId) {
		try {
			pst = con.prepareStatement(deleteReplySQL2);
			pst.setInt(1,commentId);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("ForumManager-deleteReplyTable2");
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void updateReplyTable(Reply reply){
		try {
			pst = con.prepareStatement(updateReplySQL);	
			pst.setInt(6,reply.getReplyId());
			pst.setInt(1,reply.getCommentId());
			pst.setString(2,reply.getUserId());
			pst.setString(3,reply.getNickName());
			pst.setString(4,reply.getReplyTime());
			pst.setString(5,reply.getReplyContent());
			
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("ForumManager-updateReplyTable");
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
			System.out.println("ForumManager Close Exception :" + e.toString()); 
		}		
	} 
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("ForumManager Close Exception :" + e.toString()); 
		}
	}
}
