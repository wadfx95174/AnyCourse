package PlayerInterface;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PlayerInterfaceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PlayerInterfaceManager manager = new PlayerInterfaceManager();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
//		System.out.println(request.getParameter("unitId"));
		response.getWriter().write(manager.getVideoUrl(Integer.parseInt(request.getParameter("unitId"))));		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(manager.getList(Integer.parseInt(request.getParameter("courselistId"))));
	}

}
