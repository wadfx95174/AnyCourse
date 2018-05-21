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

public class ReplyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ForumManager dbreply = new ForumManager();
		response.setHeader("Cache-Control","max-age=0");
		ArrayList<Reply> replys = new ArrayList<Reply>();

		dbreply.selectReplyTable(replys);
		String replyJson = new Gson().toJson(replys);
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(replyJson);
		dbreply.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String state = request.getParameter("state");
		response.setHeader("Cache-Control","max-age=0");
		if(state.equals("insert"))
		{
			int commentID = Integer.parseInt(request.getParameter("commentID"));
			String userID = (String)session.getAttribute("userID");
			String nickName = (String)session.getAttribute("nickName");
			String replyContent = request.getParameter("replyContent");
			
			ForumManager dbreply = new ForumManager();
			
			Reply reply = new Reply();
			
			reply.setCommentID(commentID);
			reply.setUserID(userID);
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
			int replyID = Integer.parseInt(request.getParameter("replyID"));
			int commentID = Integer.parseInt(request.getParameter("commentID"));
			String userID = (String)session.getAttribute("userID");
			String nickName = (String)session.getAttribute("nickName");
			String replyTime = request.getParameter("replyTime");
			String replyContent = request.getParameter("replyContent");
			
			ForumManager dbreply = new ForumManager();
			
			Reply reply = new Reply();
			
			reply.setReplyID(replyID);
			reply.setCommentID(commentID);
			reply.setUserID(userID);
			reply.setNickName(nickName);
			reply.setReplyTime(replyTime);
			reply.setReplyContent(replyContent);

			System.out.println(reply);
			dbreply.updateReplyTable(reply);
			PrintWriter out = response.getWriter();		
			out.print("success");
			dbreply.conClose();
		}	
		if(state.equals("delete"))
		{
			ForumManager dbreply = new ForumManager();
			int replyID = Integer.parseInt(request.getParameter("replyID"));
			
			Reply reply = new Reply();
			reply.setReplyID(replyID);
						
			dbreply.deleteReplyTable(replyID);
			PrintWriter out = response.getWriter();
			dbreply.conClose();
		}	
		if(state.equals("delete2"))
		{
			ForumManager dbreply = new ForumManager();
			int commentID = Integer.parseInt(request.getParameter("commentID"));
			
			Reply reply = new Reply();
			reply.setCommentID(commentID);
						
			dbreply.deleteReplyTable2(commentID);
			PrintWriter out = response.getWriter();
			dbreply.conClose();
		}	
	}

}
