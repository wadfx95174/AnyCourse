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

/**
 * Servlet implementation class ExchangeKeylabelServlet
 */
@WebServlet("/ExchangeKeylabelServlet")
public class ExchangeKeylabelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExchangeKeylabelServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		ExchangeManager dbkeylabel = new ExchangeManager();
		 
		ArrayList<KeyLabel> keyLabels = new ArrayList<KeyLabel>();
			
		dbkeylabel.selectKeyLabelTable(keyLabels);
		String keyLabel_json = new Gson().toJson(keyLabels);
		response.setContentType("application/json;charset = utf-8;");
		response.getWriter().write(keyLabel_json);
//		System.out.println(keyLabel_json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
