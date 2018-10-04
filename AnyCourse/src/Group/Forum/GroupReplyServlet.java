package Group.Forum;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import Group.Forum.GroupForumManager;
import Group.Forum.GroupReply;

/**
 * Servlet implementation class GroupReplyServlet
 */
public class GroupReplyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupReplyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		GroupForumManager dbreply = new GroupForumManager();
		response.setHeader("Cache-Control","max-age=0");
		ArrayList<GroupReply> replys = new ArrayList<GroupReply>();
		HttpSession session = request.getSession();
		Map<String, Integer> groups = (Map<String, Integer>)session.getAttribute("groups");
		
		if (groups.containsValue(Integer.parseInt(request.getParameter("groupId"))))
		{
			System.out.print("BB");
			dbreply.selectReplyTable(replys);
			String replyJson = new Gson().toJson(replys);
			response.setContentType("application/json;charset = utf-8;");
			response.getWriter().write(replyJson);
			dbreply.conClose();
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
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			String userId = (String)session.getAttribute("userId");
			String nickName = (String)session.getAttribute("nickName");
			String replyContent = request.getParameter("replyContent");
			
			GroupForumManager dbreply = new GroupForumManager();
			
			GroupReply reply = new GroupReply();
			
			reply.setCommentId(commentId);
			reply.setUserId(userId);
			reply.setNickName(nickName);
			reply.setReplyContent(replyContent);

			reply = dbreply.insertReplyTable(reply);
			String replyJson = new Gson().toJson(reply);
			response.setContentType("application/json;charset = utf-8;");
			response.getWriter().write(replyJson);
			dbreply.conClose();
		}
		if(state.equals("update"))
		{
			int replyId = Integer.parseInt(request.getParameter("replyId"));
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			String userId = (String)session.getAttribute("userId");
			String nickName = (String)session.getAttribute("nickName");
			String replyTime = request.getParameter("replyTime");
			String replyContent = request.getParameter("replyContent");
			
			GroupForumManager dbreply = new GroupForumManager();
			
			GroupReply reply = new GroupReply();
			
			reply.setReplyId(replyId);
			reply.setCommentId(commentId);
			reply.setUserId(userId);
			reply.setNickName(nickName);
			reply.setReplyTime(replyTime);
			reply.setReplyContent(replyContent);

			dbreply.updateReplyTable(reply);
			PrintWriter out = response.getWriter();		
			out.print("success");
			dbreply.conClose();
		}	
		if(state.equals("delete"))
		{
			GroupForumManager dbreply = new GroupForumManager();
			int replyId = Integer.parseInt(request.getParameter("replyId"));
			
			GroupReply reply = new GroupReply();
			reply.setReplyId(replyId);
						
			dbreply.deleteReplyTable(replyId);
			PrintWriter out = response.getWriter();
			dbreply.conClose();
		}	
		if(state.equals("delete2"))
		{
			GroupForumManager dbreply = new GroupForumManager();
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			
			GroupReply reply = new GroupReply();
			reply.setCommentId(commentId);
						
			dbreply.deleteReplyTable2(commentId);
			dbreply.conClose();
		}	
	}

}
