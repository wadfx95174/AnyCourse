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
		
		response.getWriter().write(manager.getVideoUrl(Integer.parseInt(request.getParameter("unitId"))));
		manager.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PlayerInterfaceManager manager = new PlayerInterfaceManager();
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		int unitId = Integer.parseInt(request.getParameter("unitId"));
		response.setHeader("Cache-Control","max-age=0");
		if(request.getParameter("action").equals("getVideoList")) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(manager.getList(Integer.parseInt(request.getParameter("courselistId"))));
		}
		else if(request.getParameter("action").equals("setVideoCloseTime")
				&& userId != null) {
			PlayerInterfaceManager playerInterfaceManager = new PlayerInterfaceManager();
			
			playerInterfaceManager.setVideoEndTime(Integer.parseInt(request.getParameter("currentTime"))
					,unitId,userId,Integer.parseInt(request.getParameter("duration")));
			
			playerInterfaceManager.conClose();
		}
		else if(request.getParameter("action").equals("like")&& userId != null) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Unit unit = new Unit();
			unit = manager.setLike(userId,unitId,Integer.parseInt(request.getParameter("like")));
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(unit));
		}
		else if(request.getParameter("action").equals("setIsBrowse")) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Unit unit = new Unit();
			
			if(userId == null) {
				unit = new Unit();
				unit.setPersonalLike(0);
				Gson gson = new Gson();
				response.getWriter().write(gson.toJson(unit));
			}
			else {
				unit = new Unit();
				unit = manager.setIsBrowse(userId,unitId);
				Gson gson = new Gson();
				response.getWriter().write(gson.toJson(unit));
			}
		}
		else if(request.getParameter("action").equals("getRecommendation")) {
			int accountId = 0;
			if(userId == null) {
				accountId = manager.getAccountId("1");
			}
			else {
				accountId = manager.getAccountId(userId);
			}
			manager.setBrowse(accountId,unitId);
			manager.setRecommendedResult(accountId,unitId);
			ArrayList<Unit> units = new ArrayList<Unit>();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			try {
				units = manager.getRecommendList(accountId,unitId);
			} catch (NumberFormatException | TasteException e) {
				e.printStackTrace();
			}
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(units));
		}
		manager.conClose();
	}
}