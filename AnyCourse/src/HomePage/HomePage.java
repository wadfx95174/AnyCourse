package HomePage;

public class HomePage {
	private int accountID;
	private String userID;
	private int unitID;
	private String listName;
	private String unitName;
	private String schoolName;
	private String courseInfo;
	private String teacher;
	private int unitLikes;
	private int listLikes;
	private String videoImgSrc;
	private int courselistID;
	private int share;
	private int num;
	private int videoType;
	/*
	 * 1代表推薦影片，2代表推薦清單，3代表課程清單，4代表想要觀看，5代表正在觀看
	 * 6代表台大，7代表清大，8代表交大，9代表成大，10代表政大，11代表中央，12代表台科大，13南台科大
	 */
	private int type;//
	public HomePage() {}
	public int getAccountID() {
		return accountID;
	}
	public void setAccountID(int accountID) {
		this.accountID = accountID;
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
	public String getListName() {
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getCourseInfo() {
		return courseInfo;
	}
	public void setCourseInfo(String courseInfo) {
		this.courseInfo = courseInfo;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public int getUnitLikes() {
		return unitLikes;
	}
	public void setUnitLikes(int unitLikes) {
		this.unitLikes = unitLikes;
	}
	public int getListLikes() {
		return listLikes;
	}
	public void setListLikes(int listLikes) {
		this.listLikes = listLikes;
	}
	public String getVideoImgSrc() {
		return videoImgSrc;
	}
	public void setVideoImgSrc(String videoImgSrc) {
		this.videoImgSrc = videoImgSrc;
	}
	public int getCourselistID() {
		return courselistID;
	}
	public void setCourselistID(int courselistID) {
		this.courselistID = courselistID;
	}
	public int getShare() {
		return share;
	}
	public void setShare(int share) {
		this.share = share;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getVideoType() {
		return videoType;
	}
	public void setVideoType(int videoType) {
		this.videoType = videoType;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
