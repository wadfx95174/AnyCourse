<<<<<<< HEAD
package Personal.CoursePlan;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class CoursePlanServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CoursePlanManager coursePlanManager = new CoursePlanManager();
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CoursePlanManager coursePlanManager = new CoursePlanManager();
		
	}

}
=======
package Personal.CoursePlan;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class CoursePlanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CoursePlanManager coursePlanManager = new CoursePlanManager();
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CoursePlanManager coursePlanManager = new CoursePlanManager();
		
	}

}
>>>>>>> branch 'master' of https://github.com/wadfx95174/AnyCourse.git
