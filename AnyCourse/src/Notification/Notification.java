package Notification;

public class Notification {
	private int  notificationId;
	private String userId;//回覆者的ID
	private String type;
	private String nickname;//回覆者的名稱
	private int groupId;
	private String groupName;
	private String releaseTime;
	private String url;
	private String message;
	private String toUserId;//提問者的ID
	private int isBrowse;
	private String commentNickname;//(討論區)通知其他回覆者時，要在通知裡面放入提問者的名字
	
	public int getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public int getIsBrowse() {
		return isBrowse;
	}
	public void setIsBrowse(int isBrowse) {
		this.isBrowse = isBrowse;
	}
	public String getCommentNickname() {
		return commentNickname;
	}
	public void setCommentNickname(String commentNickname) {
		this.commentNickname = commentNickname;
	}
	
	
}
