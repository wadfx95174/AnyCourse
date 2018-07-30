package Group.Management;

public class Member
{
	private String userId;
	private String userName;
	private boolean identity;
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public boolean isIdentity()
	{
		return identity;
	}
	public void setIdentity(boolean identity)
	{
		this.identity = identity;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
}
