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
import com.google.gson.GsonBuilder;

public class ReplyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ForumManager dbreply = new ForumManager();
		response.setHeader("Cache-Control","max-age=0");
		ArrayList<Reply> replys = new ArrayList<Reply>();
		int unitId = Integer.parseInt(request.getParameter("unitId"));
		
		dbreply.selectReplyTable(replys,unitId);
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(new Gson().toJson(replys));
		
		dbreply.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String state = request.getParameter("state");
		response.setHeader("Cache-Control","max-age=0");
		if(state.equals("insert"))
		{
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			String userId = (String)session.getAttribute("userId");
			String nickName = (String)session.getAttribute("nickName");
			String replyContent = request.getParameter("replyContent");
			
			ForumManager dbreply = new ForumManager();
			
			Reply reply = new Reply();
			
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
			
			ForumManager dbreply = new ForumManager();
			
			Reply reply = new Reply();
			
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
			ForumManager dbreply = new ForumManager();
			int replyId = Integer.parseInt(request.getParameter("replyId"));
			
			Reply reply = new Reply();
			reply.setReplyId(replyId);
						
			dbreply.deleteReplyTable(replyId);
			PrintWriter out = response.getWriter();
			dbreply.conClose();
		}	
		if(state.equals("delete2"))
		{
			ForumManager dbreply = new ForumManager();
			int commentId = Integer.parseInt(request.getParameter("commentId"));
			
			Reply reply = new Reply();
			reply.setCommentId(commentId);
						
			dbreply.deleteReplyTable2(commentId);
			dbreply.conClose();
		}	
	}

}
