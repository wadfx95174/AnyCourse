package Account;

import java.util.Map;

public class Account
{
	private String userId;
	private String nickName;
	private String pictureUrl;
	private Map<String, Integer> groups;
	public String getUserId() {
		return userId;
	}
	public Map<String, Integer> getGroups()
	{
		return groups;
	}
	public void setGroups(Map<String, Integer> groups)
	{
		this.groups = groups;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
}
