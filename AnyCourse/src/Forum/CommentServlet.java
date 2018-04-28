package Forum;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Forum.ForumManager;
import Forum.Comment;
import Forum.Reply;

/**
 * Servlet implementation class commentServlet
 */
@WebServlet("/commentServlet")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		ForumManager dbcomment = new ForumManager();
		 
		ArrayList<Comment> comments = new ArrayList<Comment>();

		dbcomment.selectCommentTable(comments);
		String comment_json = new Gson().toJson(comments);
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(comment_json);
		System.out.println(comment_json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		String state = request.getParameter("state");
				 
		if(state.equals("insert"))
		{
			String user_id = request.getParameter("user_id");
			String comment_time = request.getParameter("comment_time");
			String comment_content = request.getParameter("comment_content");
			
			ForumManager dbcomment = new ForumManager();
			
			Comment comment = new Comment();
			
			comment.setUser_id(user_id);
			comment.setComment_time(comment_time);
			comment.setComment_content(comment_content);
			
//			dbcomment.insertCommentTable(comment);
//			response.setContentType("application/json");
//			PrintWriter out = response.getWriter();		
//			out.print("success");
			
			int id =dbcomment.insertCommentTable(comment);
			comment.setComment_id(id);
			String comment_json = new Gson().toJson(comment);
			response.setContentType("application/json");
			response.getWriter().write(comment_json);
			
		}
		if(state.equals("update"))
		{
			int comment_id = Integer.parseInt(request.getParameter("comment_id"));
			String user_id = request.getParameter("user_id");
			String comment_time = request.getParameter("comment_time");
			String comment_content = request.getParameter("comment_content");
			
			ForumManager dbcomment = new ForumManager();
			
			Comment comment = new Comment();
			comment.setComment_id(comment_id);
			comment.setUser_id(user_id);
			comment.setComment_time(comment_time);
			comment.setComment_content(comment_content);

			System.out.println(comment);
			dbcomment.updateCommentTable(comment);
			PrintWriter out = response.getWriter();		
			out.print("success");
		}	
		if(state.equals("delete"))
		{
			ForumManager dbcomment = new ForumManager();
			int comment_id = Integer.parseInt(request.getParameter("comment_id"));
			
			Comment comment = new Comment();
			comment.setComment_id(comment_id);
						
			dbcomment.deleteCommentTable(comment_id);
			PrintWriter out = response.getWriter();		
			out.print("success");
		}
	}
}
