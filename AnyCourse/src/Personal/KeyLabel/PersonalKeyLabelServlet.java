package Personal.KeyLabel;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PersonalKeyLabelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PersonalKeyLabelManager manager = new PersonalKeyLabelManager();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		HttpSession seesion = request.getSession();
		response.getWriter().print(manager.getUnitPersonalKeyLabel((String)seesion.getAttribute("userId")));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
