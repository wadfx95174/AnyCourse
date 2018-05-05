package PlayerInterface;

public class Unit {
	private String user_id;
	private int unitId;
	private String schoolName;
	private String listName;
	private String unitName;
	private String videoUrl;
	private int likes;
	private String videoImgSrc;
	private String courseInfo;
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private int personalLike;
	private String teacher;
	private int type;

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
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

	public String getCourseInfo() {
		return courseInfo;
	}

	public void setCourseInfo(String courseInfo) {
		this.courseInfo = courseInfo;
	}

	public String getUserID() {
		return user_id;
	}

	public void setUserID(String user_id) {
		this.user_id = user_id;
	}

	public int getPersonalLike() {
		return personalLike;
	}

	public void setPersonalLike(int personalLike) {
		this.personalLike = personalLike;
	}

}
