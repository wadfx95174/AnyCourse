package Personal.WatchRecord;

public class WatchRecord {
	public String userID;
	public int unitID;
	public String watchTime;	
	public String schoolName;
	public String unitName;
	public String videoUrl;
	public int likes;
	public String videoImgSrc;
	
	
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public String getVideoImgSrc() {
		return videoImgSrc;
	}
	public void setVideoImgSrc(String videoImgSrc) {
		this.videoImgSrc = videoImgSrc;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public int getUnitID() {
		return unitID;
	}
	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}
	public String getWatchTime() {
		return watchTime;
	}
	public void setWatchTime(String watchTime) {
		this.watchTime = watchTime;
	}
}
