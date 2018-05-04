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

/**
 * Servlet implementation class ReplyServlet
 */
@WebServlet("/ReplyServlet")
public class ReplyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReplyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		HttpSession session = request.getSession();
		ForumManager dbreply = new ForumManager();
		response.setHeader("Cache-Control","max-age=0");
		ArrayList<Reply> replys = new ArrayList<Reply>();

		dbreply.selectReplyTable(replys);
		String reply_json = new Gson().toJson(replys);
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(reply_json);
//		System.out.println(reply_json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		HttpSession session = request.getSession();
		String state = request.getParameter("state");
		response.setHeader("Cache-Control","max-age=0");
		if(state.equals("insert"))
		{
			int comment_id = Integer.parseInt(request.getParameter("comment_id"));
			String user_id = (String)session.getAttribute("userId");
			String nick_name = (String)session.getAttribute("nickName");
//			String reply_time = request.getParameter("reply_time");
			String reply_content = request.getParameter("reply_content");
			
			ForumManager dbreply = new ForumManager();
			
			Reply reply = new Reply();
			
			reply.setComment_id(comment_id);
			reply.setUser_id(user_id);
			reply.setNick_name(nick_name);
//			reply.setReply_time(reply_time);
			reply.setReply_content(reply_content);
			
//			dbreply.insertReplyTable(reply);
//			response.setContentType("application/json");
//			PrintWriter out = response.getWriter();		
//			out.print("success");
			
//			int id =dbreply.insertReplyTable(reply);
//			reply.setReply_id(id);
			reply = dbreply.insertReplyTable(reply);
			String reply_json = new Gson().toJson(reply);
			response.setContentType("application/json;charset = utf-8;");
			response.getWriter().write(reply_json);
		}
		if(state.equals("update"))
		{
			int reply_id = Integer.parseInt(request.getParameter("reply_id"));
			int comment_id = Integer.parseInt(request.getParameter("comment_id"));
			String user_id = (String)session.getAttribute("userId");
			String nick_name = (String)session.getAttribute("nickName");
			String reply_time = request.getParameter("reply_time");
			String reply_content = request.getParameter("reply_content");
			
			ForumManager dbreply = new ForumManager();
			
			Reply reply = new Reply();
			
			reply.setReply_id(reply_id);
			reply.setComment_id(comment_id);
			reply.setUser_id(user_id);
			reply.setNick_name(nick_name);
			reply.setReply_time(reply_time);
			reply.setReply_content(reply_content);

			System.out.println(reply);
			dbreply.updateReplyTable(reply);
			PrintWriter out = response.getWriter();		
			out.print("success");
		}	
		if(state.equals("delete"))
		{
			ForumManager dbreply = new ForumManager();
			int reply_id = Integer.parseInt(request.getParameter("reply_id"));
			
			Reply reply = new Reply();
			reply.setReply_id(reply_id);
						
			dbreply.deleteReplyTable(reply_id);
			PrintWriter out = response.getWriter();		
//			out.print("success");
		}	
		if(state.equals("delete2"))
		{
			ForumManager dbreply = new ForumManager();
			int comment_id = Integer.parseInt(request.getParameter("comment_id"));
			
			Reply reply = new Reply();
			reply.setComment_id(comment_id);
						
			dbreply.deleteReplyTable2(comment_id);
			PrintWriter out = response.getWriter();		
//			out.print("success");
		}	
	}

}
