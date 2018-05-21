package LoginVerification;

import java.util.ArrayList;

public class UserProfile
{
	private String userID;
	private String googleID;
	private String password;
	private String calendarID;
	private String email;
	private String nickName;
	private String pictureUrl;
	private ArrayList<String> favoriteCourses; 
	
	public UserProfile()
	{
		googleID = null;
		calendarID = null;
		pictureUrl = null;
		favoriteCourses = new ArrayList<String>();
	}
	
	public String getUserID()
	{
		return userID;
	}
	public void setUserID(String userID)
	{
		this.userID = userID;
	}
	public String getGoogleID()
	{
		return googleID;
	}
	public void setGoogleID(String googleID)
	{
		this.googleID = googleID;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getCalendarID()
	{
		return calendarID;
	}
	public void setCalendarID(String calendarID)
	{
		this.calendarID = calendarID;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getNickName()
	{
		return nickName;
	}
	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}
	public String getPictureUrl()
	{
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl)
	{
		this.pictureUrl = pictureUrl;
	}
	public ArrayList<String> getFavoriteCourses()
	{
		return favoriteCourses;
	}
	public void setFavoriteCourses(ArrayList<String> favoriteCourses)
	{
		this.favoriteCourses = favoriteCourses;
	}
	public void addFavoriteCourse(String courseName)
	{
		favoriteCourses.add(courseName);
	}
}
