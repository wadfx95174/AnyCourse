package HomePage;

public class HomePage {
	private String user_id;
	private int unit_id;
	private String list_name;
	private String unit_name;
	private String school_name;
	private String course_info;
	private String teacher;
	private int unitLikes;
	private float recommended_score;
	private String video_url;
	private int listLikes;
	private String video_img_src;
	private int courselist_id;
	private int share;
	private int oorder;
	private int num;
	private int video_type;
	/*
	 * 1代表推薦影片，2代表推薦清單，3代表課程清單，4代表想要觀看，5代表正在觀看
	 * 6代表台大，7代表清大，8代表交大，9代表成大，10代表政大，11代表中央，12代表台科大，13南台科大
	 */
	private int type;
	public HomePage() {}
	public int getVideo_type() {
		return video_type;
	}
	public String getCourse_info() {
		return course_info;
	}
	public String getUser_id() {
		return user_id;
	}
	public int getUnit_id() {
		return unit_id;
	}
	public String getList_name() {
		return list_name;
	}
	public String getUnit_name() {
		return unit_name;
	}
	public String getSchool_name() {
		return school_name;
	}
	public String getTeacher() {
		return teacher;
	}
	public int getUnitLikes() {
		return unitLikes;
	}
	public float getRecommended_score() {
		return recommended_score;
	}
	public String getVideo_url() {
		return video_url;
	}
	public int getListLikes() {
		return listLikes;
	}
	public String getVideo_img_src() {
		return video_img_src;
	}
	public int getCourselist_id() {
		return courselist_id;
	}
	public int getShare() {
		return share;
	}
	private int getType() {
		return type;
	}
	public int getOorder() {
		return oorder;
	}
	public void setVideo_type(int video_type) {
		this.video_type = video_type;
	}
	public void setCourse_info(String course_info) {
		this.course_info = course_info;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public void setUnit_id(int unit_id) {
		this.unit_id = unit_id;
	}
	public void setList_name(String list_name) {
		this.list_name = list_name;
	}
	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}
	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public void setUnitLikes(int unitLikes) {
		this.unitLikes = unitLikes;
	}
	public void setRecommended_score(float recommended_score) {
		this.recommended_score = recommended_score;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	public void setListLikes(int listLikes) {
		this.listLikes = listLikes;
	}
	public void setVideo_img_src(String video_img_src) {
		this.video_img_src = video_img_src;
	}
	public void setCourselist_id(int courselist_id) {
		this.courselist_id = courselist_id;
	}
	public void setShare(int share) {
		this.share = share;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setOorder(int oorder) {
		this.oorder = oorder;
	}
}
