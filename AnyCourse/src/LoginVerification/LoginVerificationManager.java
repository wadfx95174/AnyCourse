package LoginVerification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginVerificationManager
{
	private final String selectPasswordSQL = "select password from account where userId = ? or email = ?";
	private final String selectUserSQL = "select * from account where userId = ? or email = ?";
	private final String selectUserIdSQL = "select userId from account where userId = ? or email = ?";
	private final String insertAccountTableSQL = "insert into account value (null,?,null,?,null,?,?,null)";
	private final String insertFavoriteCourseSQL = "insert into favoriteCourse value(?,?)";
	private final String insertGoogleAccountSQL = "insert ignore into account (userId,email,nickName,pictureUrl) values (?,?,?,?)";
	private final String updateNickNameSQL = "update account set nickName = ? where userId = ?";
	private final String updateEmailSQL = "update account set email = ? where userId = ?";
	private final String updatePasswordSQL = "update account set password = ? where userId = ?";
	private Connection con = null;
	private Statement stat = null;
	private ResultSet result = null;
	private PreparedStatement pst = null;
	
	public LoginVerificationManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");//註冊Driver
			con = DriverManager.getConnection("jdbc:mysql://140.121.197.130:45021/anycourse?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=Big5"
					, "root", "peter");//取得connection
			
		}
		catch(ClassNotFoundException e){
			System.out.println("DriverClassNotFound"+e.toString());
		}
		catch(SQLException x){
			System.out.println("Exception" + x.toString());
		}
	}
	public String getUserPassword(String param) {
		try {
			pst = con.prepareStatement(selectPasswordSQL);
			pst.setString(1, param);
			pst.setString(2, param);
			result = pst.executeQuery();
			if(result.next()) 
			{ 	
				return result.getString("password");
			}
		}
		catch(SQLException x){
			System.out.println("LoginVerificationManager-getUserPassword");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return null;
	}
	public String getUserId(String param) {
		try {
			pst = con.prepareStatement(selectUserIdSQL);
			pst.setString(1, param);
			pst.setString(2, param);
			result = pst.executeQuery();
			if(result.next()) 
			{ 	
				return result.getString("userId");
			}
		}
		catch(SQLException x){
			System.out.println("LoginVerificationManager-getUserId");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return null;
	}
	
	public UserProfile getUserProfile(String param) {
		UserProfile user = new UserProfile();
		try {
			pst = con.prepareStatement(selectUserSQL);
			pst.setString(1, param);
			pst.setString(2, param);
			result = pst.executeQuery();
			if(result.next()) 
			{ 	
				user.setUserId(result.getString("userId"));
				user.setNickName(result.getString("nickName"));
				user.setPictureUrl(result.getString("pictureUrl"));
				user.setEmail(result.getString("email"));
				return user;
			}
		}
		catch(SQLException x){
			System.out.println("LoginVerificationManager-getUserProfile");
			System.out.println("Exception select"+x.toString());
		}
		finally {
			Close();
		}
		return null;
	}
	
	public void createAccount(UserProfile userProfile)
	{
		try
		{
			pst = con.prepareStatement(insertAccountTableSQL);
			pst.setString(1, userProfile.getUserId());
			pst.setString(2, userProfile.getPassword());
			pst.setString(3, userProfile.getEmail());
			pst.setString(4, userProfile.getNickName());
			pst.executeUpdate();
			
		} catch (final SQLException x)
		{
			System.out.println("LoginVerificationManager-createAccount");
			System.out.println("Exception insert" + x.toString());
		} finally
		{
			Close();
		}
	}
	
	public void insertFavoriteCourse(UserProfile userProfile)
	{
		try
		{
			pst = con.prepareStatement(insertFavoriteCourseSQL);
			for (String str: userProfile.getFavoriteCourses())
			{
				pst.setString(1, userProfile.getUserId());
				pst.setString(2, str);
				pst.executeUpdate();
			}
			
		} catch (final SQLException x)
		{
			System.out.println("LoginVerificationManager-insertFavoriteCourse");
			System.out.println("Exception insert" + x.toString());
		} finally
		{
			Close();
		}
	}
	
	public void insertGoogleAccount(UserProfile userProfile)
	{
		try
		{
			pst = con.prepareStatement(insertGoogleAccountSQL);
			pst.setString(1, userProfile.getUserId());
			pst.setString(2, userProfile.getEmail());
			pst.setString(3, userProfile.getNickName());
			pst.setString(4, userProfile.getPictureUrl());
			pst.executeUpdate();
			
		} catch (final SQLException x)
		{
			System.out.println("LoginVerificationManager-insertGoogleAccount");
			System.out.println("Exception insert" + x.toString());
		} finally
		{
			Close();
		}
	}
	
	public void Close() {
		try {
			if(result!=null) {
				result.close();
			}
			if(stat!=null) {
				stat.close();
			}
			if(pst!=null) {
				pst.close();
			}
		}
		catch(SQLException e) {
			System.out.println("LoginVerificationManager Close Exception :" + e.toString()); 
		}		
	} 
	
	public void conClose() {
		try {
			if(con!=null) {
				con.close();
			}
		}
		catch(SQLException e) {
			System.out.println("LoginVerificationManager Close Exception :" + e.toString()); 
		}
	}
	
	public static void main(String []args)
	{
//		LoginVerificationManager lvdm = new LoginVerificationManager();
	}
}
