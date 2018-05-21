package Forum;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import Forum.ForumManager;
import Forum.Comment;
import Forum.Reply;


public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ForumManager dbcomment = new ForumManager();
		response.setHeader("Cache-Control","max-age=0"); 
		ArrayList<Comment> comments = new ArrayList<Comment>();

		
		String commentJson = new Gson().toJson(comments);
		commentJson = dbcomment.selectCommentTable(Integer.parseInt(request.getParameter("unitID")));
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(commentJson);
		dbcomment.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String state = request.getParameter("state");
		response.setHeader("Cache-Control","max-age=0");		 
		if(state.equals("insert"))
		{
			int unitID = Integer.parseInt(request.getParameter("unitID"));
			String userID = (String)session.getAttribute("userID");
			String nickName = (String)session.getAttribute("nickName");
			String commentContent = request.getParameter("commentContent");
			
			ForumManager dbcomment = new ForumManager();
			
			Comment comment = new Comment();
			
			comment.setUnitID(unitID);
			comment.setUserID(userID);
			comment.setNickName(nickName);
			comment.setCommentContent(commentContent);
			
			comment = dbcomment.insertCommentTable(comment);
			String commentJson = new Gson().toJson(comment);
			response.setContentType("application/json;charset = utf-8;");
			response.getWriter().write(commentJson);
			dbcomment.conClose();
		}
		if(state.equals("update"))
		{
			int commentID = Integer.parseInt(request.getParameter("commentID"));
			int unitID = Integer.parseInt(request.getParameter("unitID"));
			String userID = (String)session.getAttribute("userID");
			String nickName = (String)session.getAttribute("nickName");
			String commentTime = request.getParameter("commentTime");
			String commentContent = request.getParameter("commentContent");
			
			ForumManager dbcomment = new ForumManager();
			
			Comment comment = new Comment();
			comment.setCommentID(commentID);
			comment.setUnitID(unitID);
			comment.setUserID(userID);
			comment.setNickName(nickName);
			comment.setCommentTime(commentTime);
			comment.setCommentContent(commentContent);

			System.out.println(comment);
			dbcomment.updateCommentTable(comment);
			PrintWriter out = response.getWriter();
			dbcomment.conClose();
		}	
		if(state.equals("delete"))
		{
			ForumManager dbcomment = new ForumManager();
			int commentID = Integer.parseInt(request.getParameter("commentID"));
			
			Comment comment = new Comment();
			comment.setCommentID(commentID);
						
			dbcomment.deleteCommentTable(commentID);
			PrintWriter out = response.getWriter();
			dbcomment.conClose();
		}
	}
}
