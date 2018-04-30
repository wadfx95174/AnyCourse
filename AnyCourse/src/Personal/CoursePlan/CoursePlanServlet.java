
package Personal.CoursePlan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CoursePlanServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		CoursePlanManager coursePlanManager = new CoursePlanManager();
		ArrayList<CoursePlan> coursePlans = null;
		HttpSession session = request.getSession();
//		if(request.getParameter("action").equals("select")) {
			coursePlans = coursePlanManager.getCoursePlanAllList((String)session.getAttribute("userId"));
//		}
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(coursePlans));
//		System.out.println(coursePlans);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CoursePlanManager coursePlanManager = new CoursePlanManager();
		if(request.getParameter("action").equals("sortable")) {
			
		}
	}

}
