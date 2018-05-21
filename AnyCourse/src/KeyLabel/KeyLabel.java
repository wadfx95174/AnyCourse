package KeyLabel;

public class KeyLabel
{
	private int keyLabelId;
	private int unitId;
	private String userId;
	private String keyLabelName;
	private String nickName;	
	private int beginTime;
	private int endTime;
	private int share;
	private String shareTime;
	private int likes;
	
	
	public int getKeyLabelId() {
		return keyLabelId;
	}


	public void setKeyLabelId(int keyLabelId) {
		this.keyLabelId = keyLabelId;
	}


	public int getUnitId() {
		return unitId;
	}


	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getKeyLabelName() {
		return keyLabelName;
	}


	public void setKeyLabelName(String keyLabelName) {
		this.keyLabelName = keyLabelName;
	}


	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	public int getBeginTime() {
		return beginTime;
	}


	public void setBeginTime(int beginTime) {
		this.beginTime = beginTime;
	}


	public int getEndTime() {
		return endTime;
	}


	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}


	public int getShare() {
		return share;
	}


	public void setShare(int share) {
		this.share = share;
	}


	public String getShareTime() {
		return shareTime;
	}


	public void setShareTime(String shareTime) {
		this.shareTime = shareTime;
	}


	public int getLikes() {
		return likes;
	}


	public void setLikes(int likes) {
		this.likes = likes;
	}


	@Override
	public String toString()
	{
		return "KeyLabel [keyLabelId=" + keyLabelId + ", unitId=" + unitId + ", userId=" + userId + ", keyLabelName="
				+ keyLabelName + ", beginTime=" + beginTime + ", endTime=" + endTime + ", share=" + share
				+ ", shareTime=" + shareTime + ", likes=" + likes + ", toString()=" + super.toString() + "]";
	}
}
