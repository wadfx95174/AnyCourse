package Group.CoursePlan;

public class GroupCoursePlan {
	
	private int groupId;
	private int userId;
	private int unitId;
	private int deadlineTime;
	private int lastTime;
	private int status;
	private int oorder;
	
	private String schoolName;
	private String listName;
	private String teacher;
	private String unitName;
	private int unitLikes;
	private String videoImgSrc;
	private int videoType;
	private int courselistId;
	
	public GroupCoursePlan() {}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public int getDeadlineTime() {
		return deadlineTime;
	}

	public void setDeadlineTime(int deadlineTime) {
		this.deadlineTime = deadlineTime;
	}

	public int getLastTime() {
		return lastTime;
	}

	public void setLastTime(int lastTime) {
		this.lastTime = lastTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getOorder() {
		return oorder;
	}

	public void setOorder(int oorder) {
		this.oorder = oorder;
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

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public int getUnitLikes() {
		return unitLikes;
	}

	public void setUnitLikes(int unitLikes) {
		this.unitLikes = unitLikes;
	}

	public String getVideoImgSrc() {
		return videoImgSrc;
	}

	public void setVideoImgSrc(String videoImgSrc) {
		this.videoImgSrc = videoImgSrc;
	}

	public int getVideoType() {
		return videoType;
	}

	public void setVideoType(int videoType) {
		this.videoType = videoType;
	}

	public int getCourselistId() {
		return courselistId;
	}

	public void setCourselistId(int courselistId) {
		this.courselistId = courselistId;
	}

}
