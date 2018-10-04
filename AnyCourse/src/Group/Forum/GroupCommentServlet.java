package Group.Forum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import Group.Forum.GroupComment;
import Group.Forum.GroupForumManager;

/**
 * Servlet implementation class GroupCommentServlet
 */

public class GroupCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupCommentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		GroupForumManager dbcomment = new GroupForumManager();
		response.setHeader("Cache-Control","max-age=0"); 
		ArrayList<GroupComment> comments = new ArrayList<GroupComment>();
		HttpSession session = request.getSession();
		Map<String, Integer> groups = (Map<String, Integer>)session.getAttribute("groups");
		
		if (groups.containsValue(Integer.parseInt(request.getParameter("groupId"))))
		{
			System.out.print("AA");
			String commentJson = new Gson().toJson(comments);
			commentJson = dbcomment.selectCommentTable(Integer.parseInt(request.getParameter("groupId")));
			response.setContentType("application/json;charset = utf-8;");
			response.getWriter().write(commentJson);
			dbcomment.conClose();
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String state = request.getParameter("state");
		response.setHeader("Cache-Control","max-age=0");		 
		if(state.equals("insert"))
		{
			int groupId = Integer.parseInt(request.getParameter("groupId"));
			String userId = (String)session.getAttribute("userId");
			String nickName = (String)session.getAttribute("nickName");
			String commentContent = request.getParameter("commentContent");
			
			GroupForumManager dbcomment = new GroupForumManager();
			
			GroupComment comment = new GroupComment();
			
			comment.setGroupId(groupId);
			comment.setUserId(userId);
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
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			int groupId = Integer.parseInt(request.getParameter("groupId"));
			String userId = (String)session.getAttribute("userId");
			String nickName = (String)session.getAttribute("nickName");
			String commentTime = request.getParameter("commentTime");
			String commentContent = request.getParameter("commentContent");
			
			GroupForumManager dbcomment = new GroupForumManager();
			
			GroupComment comment = new GroupComment();
			comment.setCommentId(commentId);
			comment.setGroupId(groupId);
			comment.setUserId(userId);
			comment.setNickName(nickName);
			comment.setCommentTime(commentTime);
			comment.setCommentContent(commentContent);

			dbcomment.updateCommentTable(comment);
			dbcomment.conClose();
		}	
		if(state.equals("delete"))
		{
			GroupForumManager dbcomment = new GroupForumManager();
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			
			GroupComment comment = new GroupComment();
			comment.setCommentId(commentId);
						
			dbcomment.deleteCommentTable(commentId);
			dbcomment.conClose();
		}
	}

}
