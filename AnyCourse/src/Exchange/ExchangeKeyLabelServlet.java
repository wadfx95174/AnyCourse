package Exchange;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import KeyLabel.KeyLabel;;

public class ExchangeKeyLabelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ExchangeManager dbkeylabel = new ExchangeManager();
		 
		ArrayList<KeyLabel> keyLabels = new ArrayList<KeyLabel>();
			
		
		String keyLabelJson = new Gson().toJson(keyLabels);
		keyLabelJson = dbkeylabel.selectKeyLabelTable(Integer.parseInt(request.getParameter("unitId")));
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(keyLabelJson);
		dbkeylabel.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
