package PlayerInterface;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.mahout.cf.taste.common.TasteException;

import com.google.gson.Gson;


public class PlayerInterfaceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		PlayerInterfaceManager manager = new PlayerInterfaceManager();
		HttpSession session = request.getSession();
		
		
		response.getWriter().write(manager.getVideoUrl(Integer.parseInt(request.getParameter("unitID"))));
		manager.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PlayerInterfaceManager manager = new PlayerInterfaceManager();
		HttpSession session = request.getSession();
		response.setHeader("Cache-Control","max-age=0");
		if(request.getParameter("action").equals("getVideoList")) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(manager.getList(Integer.parseInt(request.getParameter("courselistID"))));
		}
		else if(request.getParameter("action").equals("setVideoCloseTime")
				&&(String)session.getAttribute("userID") != null) {
			PlayerInterfaceManager playerInterfaceManager = new PlayerInterfaceManager();
			System.out.println(Integer.parseInt(request.getParameter("unitID")));
			System.out.println(Integer.parseInt(request.getParameter("currentTime")));
			System.out.println(Integer.parseInt(request.getParameter("duration")));
			
			playerInterfaceManager.setVideoEndTime(Integer.parseInt(request.getParameter("currentTime"))
					, Integer.parseInt(request.getParameter("unitID")), (String)session.getAttribute("userID")
					,Integer.parseInt(request.getParameter("duration")));
			
			
		}
		else if(request.getParameter("action").equals("like")&&(String)session.getAttribute("userID") != null) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Unit unit = new Unit();
			System.out.println(Integer.parseInt(request.getParameter("unitID")));
			System.out.println(Integer.parseInt(request.getParameter("like")));
			unit = manager.setLike((String)session.getAttribute("userID")
					, Integer.parseInt(request.getParameter("unitID"))
					,Integer.parseInt(request.getParameter("like")));
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(unit));
		}
		else if(request.getParameter("action").equals("setIsBrowse")) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Unit unit = new Unit();
			
			if((String)session.getAttribute("userID") == null) {
				unit = new Unit();
				unit.setPersonalLike(0);
				Gson gson = new Gson();
				response.getWriter().write(gson.toJson(unit));
			}
			else {
				unit = new Unit();
				unit = manager.setIsBrowse((String)session.getAttribute("userID")
						, Integer.parseInt(request.getParameter("unitID")));
				Gson gson = new Gson();
				response.getWriter().write(gson.toJson(unit));
			}
		}
		else if(request.getParameter("action").equals("getRecommendation")) {
			int accountID = 0;
			if((String)session.getAttribute("userID") == null) {
				accountID = manager.getAccountID("1");
			}
			else {
				accountID = manager.getAccountID((String)session.getAttribute("userID"));
			}
			System.out.println("accountID:"+accountID);
			manager.setBrowse(accountID,Integer.parseInt(request.getParameter("unitID")));
			ArrayList<Unit> units = new ArrayList<Unit>();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			try {
				units = manager.getRecommendList(accountID, Long.parseLong(request.getParameter("unitID")));
			} catch (NumberFormatException | TasteException e) {
				e.printStackTrace();
			}
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(units));
		}
		manager.conClose();
	}
}
