package Forum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import Forum.Comment;
import Forum.Reply;;

public class ForumManager {
	public String insertCommentSQL = "insert into comment (comment_id,unit_id,user_id,nick_name,comment_time,comment_content) value(null,?,?,?,null,?)";
	public String selectCommentSQL = "select * from comment ";
	public String deleteCommentSQL = "delete from comment where comment_id = ?";
	public String updateCommentSQL = "update comment set unit_id = ?,user_id = ?,nick_name = ?,comment_time = ?,comment_content = ? where comment_id = ?";
	
	public String insertReplySQL = "insert into reply (reply_id,comment_id,user_id,nick_name,reply_time,reply_content) value(null,?,?,?,null,?)";
	public String selectReplySQL = "select * from reply ";
	public String deleteReplySQL = "delete from reply where reply_id = ?";
	public String deleteReplySQL2 = "delete from reply where comment_id = ?";
	public String updateReplySQL = "update reply set comment_id = ?,user_id = ?,nick_name = ?,reply_time = ?,reply_content = ? where reply_id = ?";
	
	public Comment comment;
	public Reply reply;
	public Connection con = null;
	public Statement stat = null;
	public ResultSet result = null;
	public PreparedStatement pst = null; 
	
	public Random random = new Random();
	
	
	public ForumManager(){
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
	
	public Comment insertCommentTable(Comment comment){
		try {
			pst = con.prepareStatement(insertCommentSQL,Statement.RETURN_GENERATED_KEYS);
			//pst.setInt(1,);
			//pst.setInt(1,comment.getComment_id());
			pst.setInt(1,comment.getUnit_id());
			pst.setString(2,comment.getUser_id());
			pst.setString(3,comment.getNick_name());
			//pst.setString(3,comment.comment_time);
			pst.setString(4,comment.getComment_content());
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if (generatedKeys.next())
			{
				int id =generatedKeys.getInt(1);
				pst = con.prepareStatement("SELECT * FROM comment WHERE comment_id = ?");
				pst.setInt(1,id);
				result = pst.executeQuery();
				 while(result.next()) 
			     { 
				 comment = new Comment();
				 comment.setComment_id(result.getInt("comment_id"));
				 comment.setUnit_id(result.getInt("unit_id"));
				 comment.setUser_id(result.getString("user_id"));
				 comment.setNick_name(result.getString("nick_name"));
				 comment.setComment_time(result.getString("comment_time"));
				 comment.setComment_content(result.getString("comment_content"));
			     }
//				 System.out.println(result.getInt("comment_id"));
//				 System.out.println(result.getString("comment_time"));
				//return generatedKeys.getInt(1);
				 return comment;
			}
		}
		catch(SQLException x){
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
			return comment;
		}
	}	
	public void selectCommentTable(ArrayList<Comment> comments) {
		try {
			stat = con.createStatement();
			result = stat.executeQuery(selectCommentSQL);
			 while(result.next()) 
		     { 	
				 comment = new Comment();
				 comment.setComment_id(result.getInt("comment_id"));
				 comment.setUnit_id(result.getInt("unit_id"));
				 comment.setUser_id(result.getString("user_id"));
				 comment.setNick_name(result.getString("nick_name"));
				 comment.setComment_time(result.getString("comment_time"));
				 comment.setComment_content(result.getString("comment_content"));
				 comments.add(comment);
		     }
//			 System.out.println(comments);
		}
			 catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void deleteCommentTable(int comment_id) {
		try {
			pst = con.prepareStatement(deleteCommentSQL);
			pst.setInt(1,comment_id);
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
			pst.setInt(6,comment.getComment_id());
			pst.setInt(1,comment.getUnit_id());
			pst.setString(2,comment.getUser_id());
			pst.setString(3,comment.getNick_name());
			pst.setString(4,comment.getComment_time());
			pst.setString(5,comment.getComment_content());
			
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
			//pst.setInt(1,);
			//pst.setInt(1,reply.getReply_id());
			pst.setInt(1,reply.getComment_id());
			pst.setString(2,reply.getUser_id());
			pst.setString(3,reply.getNick_name());
			//pst.setString(4,reply.getReply_time());
			pst.setString(4,reply.getReply_content());
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if (generatedKeys.next())
			{
				int id =generatedKeys.getInt(1);
				pst = con.prepareStatement("SELECT * FROM reply WHERE reply_id = ?");
				pst.setInt(1,id);
				result = pst.executeQuery();
				 while(result.next()) 
			     { 
				 reply = new Reply();
				 reply.setReply_id(result.getInt("reply_id"));
				 reply.setComment_id(result.getInt("comment_id"));
				 reply.setUser_id(result.getString("user_id"));
				 reply.setNick_name(result.getString("nick_name"));
				 reply.setReply_time(result.getString("reply_time"));
				 reply.setReply_content(result.getString("reply_content"));
			     }
//				 System.out.println(result.getInt("comment_id"));
//				 System.out.println(result.getString("comment_time"));
				//return generatedKeys.getInt(1);
				 return reply;
			}
		}
		catch(SQLException x){
			System.out.println("Exception insert"+x.toString());
		}
		finally {
			Close();
			return reply;
		}
			
	}	 
	public void selectReplyTable(ArrayList<Reply> replys) {
		try {
			stat = con.createStatement();
			result = stat.executeQuery(selectReplySQL);
			 while(result.next()) 
		     { 
				 reply = new Reply();
				 reply.setReply_id(result.getInt("reply_id"));
				 reply.setComment_id(result.getInt("comment_id"));
				 reply.setUser_id(result.getString("user_id"));
				 reply.setNick_name(result.getString("nick_name"));
				 reply.setReply_time(result.getString("reply_time"));
				 reply.setReply_content(result.getString("reply_content"));
				 replys.add(reply);
		     }
//			 System.out.println(replys);
		}
			 catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
	}
	public void deleteReplyTable(int reply_id) {
		try {
			pst = con.prepareStatement(deleteReplySQL);
			pst.setInt(1,reply_id);
			pst.executeUpdate();
		}
		catch(SQLException x){
			System.out.println("Exception delete"+x.toString());
		}
		finally {
			Close();
		}
	}  
	public void deleteReplyTable2(int comment_id) {
		try {
			pst = con.prepareStatement(deleteReplySQL2);
			pst.setInt(1,comment_id);
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
			pst.setInt(6,reply.getReply_id());
			pst.setInt(1,reply.getComment_id());
			pst.setString(2,reply.getUser_id());
			pst.setString(3,reply.getNick_name());
			pst.setString(4,reply.getReply_time());
			pst.setString(5,reply.getReply_content());
			
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
