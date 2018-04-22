package Personal.VideoList;

public class UnitVideo {
	private String user_id;
	private String unit_name;
	private String course_info;
	private String school_name;
	private String teacher;
	private String video_img_src;
	private int videoType;//1:youtube。2:jwplayer
	private int courselist_id;
	private int oorder;
	private int likes;
	private int unit_id;
	
	
	public UnitVideo() {}
	
	public void setUserID(String user_id) {
		this.user_id = user_id;
	}
	public void setUnitName(String unit_name) {
		this.unit_name = unit_name;
	}
	public void setCourselistID(int courselist_id) {
		this.courselist_id = courselist_id;
	}
	public void setOorder(int oorder) {
		this.oorder = oorder;
	}
	public void setCourseInfo(String course_info) {
		this.course_info = course_info;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public void setSchoolName(String school_name) {
		this.school_name = school_name;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public void setUnitID(int unit_id) {
		this.unit_id = unit_id;
	}
	public void setVideoImgSrc(String video_img_src) {
		this.video_img_src = video_img_src;
	}
	public void setVideoType(int videoType) {
		this.videoType = videoType;
	}
	public String getUserID() {
		return user_id;
	}
	public String getUnitName() {
		return unit_name;
	}
	public int getCourselistID() {
		return courselist_id;
	}
	public int getOorder() {
		return oorder;
	}
	public String getCourseInfo() {
		return course_info;
	}
	public int getLikes() {
		return likes;
	}
	public String getSchoolName() {
		return school_name;
	}
	public String getTeacher() {
		return teacher;
	}
	public int getUnitID() {
		return unit_id;
	}
	public String getVideoImgSrc() {
		return video_img_src;
	}
	public int getVideoType() {
		return videoType;
	}
}
