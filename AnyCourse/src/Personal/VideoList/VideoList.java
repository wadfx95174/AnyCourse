package Personal.VideoList;

public class VideoList {
	private int courselist_id;
	private String list_name;
	private String creator;
	private String user_id;
	private String school_name;
	private int share = 0;
	private int likes = 0;
	private int oorder;
	
	public VideoList() {}
	
	public void setCourselistID(int courselist_id) {
		this.courselist_id = courselist_id;
	}
	public void setListName(String list_name) {
		this.list_name = list_name;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public void setUserID(String user_id) {
		this.user_id = user_id;
	}
	public void setSchoolName(String school_name) {
		this.school_name = school_name;
	}
	public void setShare(int share) {
		this.share = share;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public void setOorder(int oorder) {
		this.oorder = oorder;
	}
	public int getCourselistID() {
		return courselist_id;
	}
	public String getListName() {
		return list_name;
	}
	public String getCreator() {
		return creator;
	}
	public String getUserID() {
		return user_id;
	}
	public String getSchoolName() {
		return school_name;
	}
	public int getShare() {
		return share;
	}
	public int getLikes() {
		return likes;
	}
	public int getOorder() {
		return oorder;
	}
}
