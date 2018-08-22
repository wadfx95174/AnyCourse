package Forum;

public class Reply {
	private int replyId;//回覆的ID
	private int commentId;//提問的ID
	private String userId;//回覆的使用者ID
	private String nickName;//回覆的使用者名稱
	private String replyTime;
	private String replyContent;//回覆內容
	private String commentUserId;//提問的使用者ID
	
	public int getReplyId() {
		return replyId;
	}
	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public String getUserId() {
		return userId;
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
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public String getCommentUserId() {
		return commentUserId;
	}
	public void setCommentUserId(String commentUserId) {
		this.commentUserId = commentUserId;
	}
	@Override
	public String toString() {
		return "Reply [replyId=" + replyId + ", commentId=" + commentId + ", userId=" + userId + ", nickName="
				+ nickName + ", replyTime=" + replyTime + ", replyContent=" + replyContent + "]";
	}
}
