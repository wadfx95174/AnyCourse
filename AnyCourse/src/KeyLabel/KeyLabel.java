package KeyLabel;

public class KeyLabel
{
	private int keyLabelID;
	private int unitID;
	private String userID;
	private String keyLabelName;
	private String nickName;	
	private int beginTime;
	private int endTime;
	private int share;
	private String shareTime;
	private int likes;
	
	
	public int getKeyLabelID() {
		return keyLabelID;
	}


	public void setKeyLabelID(int keyLabelID) {
		this.keyLabelID = keyLabelID;
	}


	public int getUnitID() {
		return unitID;
	}


	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}


	public String getUserID() {
		return userID;
	}


	public void setUserID(String userID) {
		this.userID = userID;
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
		return "KeyLabel [keyLabelID=" + keyLabelID + ", unitID=" + unitID + ", userID=" + userID + ", keyLabelName="
				+ keyLabelName + ", beginTime=" + beginTime + ", endTime=" + endTime + ", share=" + share
				+ ", shareTime=" + shareTime + ", likes=" + likes + ", toString()=" + super.toString() + "]";
	}
}
