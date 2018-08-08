package Handout;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Handout.HandoutManager;
import Handout.Handout;


public class Handoutservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Handoutservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HandoutManager dbhandout = new HandoutManager();
		response.setHeader("Cache-Control","max-age=0");		 
		ArrayList<Handout> handouts = new ArrayList<Handout>();
			
		
		String handoutJson = new Gson().toJson(handouts);
		handoutJson = dbhandout.selectHandoutTable(Integer.parseInt(request.getParameter("unitId")));
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(handoutJson);
		dbhandout.conClose();	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
