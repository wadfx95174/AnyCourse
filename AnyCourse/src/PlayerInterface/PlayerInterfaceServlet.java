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
import com.google.gson.GsonBuilder;

import Search.SearchManager;
import Search.Search;

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
		response.setHeader("Cache-Control","max-age=0");
		PlayerInterfaceManager manager = new PlayerInterfaceManager();
		HttpSession session = request.getSession();
		String action = request.getParameter("action");
		String userId = (String)session.getAttribute("userId");
//		System.out.println(action);
		
		if(action.equals("getVideoList")) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(manager.getList(Integer.parseInt(request.getParameter("courselistId"))));
		}
		else if(action.equals("setVideoCloseTime")&& userId != null) {
			
			manager.setVideoCloseTime(Integer.parseInt(request.getParameter("currentTime"))
					,Integer.parseInt(request.getParameter("unitId")),userId
					,Integer.parseInt(request.getParameter("duration")));
		}
		else if(action.equals("setGroupVideoCloseTime") && userId != null) {
			manager.setGroupVideoCloseTime(Integer.parseInt(request.getParameter("currentTime"))
					,Integer.parseInt(request.getParameter("unitId")),userId
					,Integer.parseInt(request.getParameter("duration")),
					Integer.parseInt(request.getParameter("groupId")));
		}
		else if(action.equals("like")&& userId != null) {
			manager.setLike(userId,Integer.parseInt(request.getParameter("unitId"))
					,Integer.parseInt(request.getParameter("like")));
		}
		else if(action.equals("setIsBrowse")) {
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
				unit = manager.setIsBrowse(userId,Integer.parseInt(request.getParameter("unitId")));
				Gson gson = new Gson();
				response.getWriter().write(gson.toJson(unit));
			}
		}
		else if(action.equals("getRecommendation")) {
			int accountId = 0;
			ArrayList<Unit> units = new ArrayList<Unit>();
			Gson gson = new Gson();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			if(userId == null) {
//				accountId = manager.getAccountId("1");
				SearchManager searchManger = new SearchManager();
				PlayerInterfaceManager playerInterfaceManager = new PlayerInterfaceManager();
				ArrayList<Search> searchs = new ArrayList<Search>();
				
				String unitName = playerInterfaceManager.getUnitName(Integer.parseInt(request.getParameter("unitId")));
				searchs = searchManger.keywordSearchWithJieba(unitName);
//				GsonBuilder builder = new GsonBuilder();
//				gson = builder.setPrettyPrinting().create();
//				System.out.println(gson.toJson(searchs));
//				System.out.println();
				response.getWriter().write(gson.toJson(searchs));
			}
			else {
				accountId = manager.getAccountId(userId);
				manager.setBrowse(accountId,Integer.parseInt(request.getParameter("unitId")));
				manager.setRecommendedResult(accountId,Integer.parseInt(request.getParameter("unitId")));
				try {
					units = manager.getRecommendList(accountId,Integer.parseInt(request.getParameter("unitId")));
				} catch (NumberFormatException | TasteException e) {
					e.printStackTrace();
				}
				response.getWriter().write(gson.toJson(units));
			}
			
		}
		else if(action.equals("getLecture")) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Gson gson = new Gson();
			response.getWriter().write(gson.toJson(manager.getLecture(Integer.parseInt(request.getParameter("unitId")))));
		}
		manager.conClose();
	}
}