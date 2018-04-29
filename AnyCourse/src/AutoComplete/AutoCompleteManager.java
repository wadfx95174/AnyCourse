package AutoComplete;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;


public class AutoCompleteManager
{

	private String selectUnitKeywordSQL = "select max(unit_keyword)unit_keyword from unit_keyword group by unit_keyword";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public AutoCompleteManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");//註冊Driver
			con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?useUnicode=true&characterEncoding=Big5"
					, "root", "peter");//取得connection
			
		}
		catch(ClassNotFoundException e){
			System.out.println("DriverClassNotFound"+e.toString());
		}
		catch(SQLException x){
			System.out.println("Exception" + x.toString());
		}
	}
	
	public ArrayList<String> getUnitKeyword() {
		ArrayList<String> outputList = new ArrayList<>(); 
		int num = 0;
		try {
			pst = con.prepareStatement(selectUnitKeywordSQL);
			result = pst.executeQuery();
			while(result.next()) 
			{ 	
				String str = result.getString("unit_keyword");
				outputList.add(str);
				if (num / 20 == 1)
				{
					System.out.println();
					num = 0;
				}
				System.out.print("\"" + str + "\",");
				num++;
			}
		}
			catch(SQLException x){
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return outputList;
	}
	
	
	public void Close() {
		try {
			if(result!=null) {
				result.close();
				result = null;
			}
			if(stat!=null) {
				stat.close();
				stat = null;
			}
			if(pst!=null) {
				pst.close();
				pst = null;
			}
		}
		catch(SQLException e) {
			System.out.println("Close Exception :" + e.toString()); 
		}		
	} 
	public static void main(String args[])
	{
		AutoCompleteManager manager = new AutoCompleteManager();
		manager.getUnitKeyword();
	}
}
