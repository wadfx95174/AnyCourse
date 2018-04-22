package LoginVerification;

import java.util.ArrayList;

public class UserProfile
{
	private String userId;
	private String googleId;
	private String password;
	private String calendarId;
	private String email;
	private String nickName;
	private String pictureUrl;
	private ArrayList<String> favoriteCourses; 
	
	public UserProfile()
	{
		googleId = null;
		calendarId = null;
		pictureUrl = null;
		favoriteCourses = new ArrayList<String>();
	}
	
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public String getGoogleId()
	{
		return googleId;
	}
	public void setGoogleId(String googleId)
	{
		this.googleId = googleId;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getCalendarId()
	{
		return calendarId;
	}
	public void setCalendarId(String calendarId)
	{
		this.calendarId = calendarId;
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
