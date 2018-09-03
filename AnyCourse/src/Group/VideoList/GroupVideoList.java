package Group.VideoList;

public class GroupVideoList {

	public GroupVideoList() {}
	
	private int groupId;
	private int courselistId;
	private int oorder;
	
	private String listName;
	private String creator;
	private String userId;
	private String schoolName;
//	private int share;
	private int unitLikes;
	
	private String unitName;
	private String courseInfo;
	private String teacher;
	private String videoImgSrc;
	private int unitId;
	private int videoType;//1:youtube。2:jwplayer
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getCourselistId() {
		return courselistId;
	}
	public void setCourselistId(int courselistId) {
		this.courselistId = courselistId;
	}
	public int getOorder() {
		return oorder;
	}
	public void setOorder(int oorder) {
		this.oorder = oorder;
	}
	public String getListName() {
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public int getUnitLikes() {
		return unitLikes;
	}
	public void setUnitLikes(int unitLikes) {
		this.unitLikes = unitLikes;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
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
	public String getVideoImgSrc() {
		return videoImgSrc;
	}
	public void setVideoImgSrc(String videoImgSrc) {
		this.videoImgSrc = videoImgSrc;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public int getVideoType() {
		return videoType;
	}
	public void setVideoType(int videoType) {
		this.videoType = videoType;
	}
}
