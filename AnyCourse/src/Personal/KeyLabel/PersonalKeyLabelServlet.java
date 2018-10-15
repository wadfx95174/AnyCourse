package Personal.KeyLabel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import Group.KeyLabel.GroupKeyLabelManager;
import Group.Management.GroupInfo;

public class PersonalKeyLabelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PersonalKeyLabelManager manager = new PersonalKeyLabelManager();
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control","max-age=0");
		response.setContentType("application/json");
		HttpSession session = request.getSession();
		response.getWriter().print(manager.getAllPersonalKeyLabel((String)session.getAttribute("userId")));
		manager.conClose();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		String method = request.getParameter("method");
		//取得該使用者所有群組
		if(method.equals("getAllGroup")) {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control","max-age=0");
			response.setContentType("application/json");
			ArrayList<GroupInfo> groups = new ArrayList<GroupInfo>();
			Map<String, Integer> map = ((Map<String, Integer>)session.getAttribute("groups"));
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				GroupInfo info = new GroupInfo();
				info.setGroupName(entry.getKey()); 
				info.setGroupId(entry.getValue());
				groups.add(info);
			}
			response.getWriter().print(new Gson().toJson(groups));
		}
		else if (method.equals("insertToGroup")) {
			int groupId = Integer.parseInt(request.getParameter("groupId"));
			int keyLabelId = Integer.parseInt(request.getParameter("KeyLabelId"));
			GroupKeyLabelManager groupKeyLabelmanager = new GroupKeyLabelManager();
			groupKeyLabelmanager.insertKeyLabel(groupId, keyLabelId);
		}
	}

}
