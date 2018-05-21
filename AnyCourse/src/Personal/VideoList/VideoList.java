package Personal.VideoList;

public class VideoList {
	private int courselistId;
	private String listName;
	private String creator;
	private String userId;
	private String schoolName;
	private int share;
	private int likes;
	private int oorder;
	
	public VideoList() {}

	public int getCourselistId() {
		return courselistId;
	}

	public void setCourselistId(int courselistId) {
		this.courselistId = courselistId;
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

	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getOorder() {
		return oorder;
	}

	public void setOorder(int oorder) {
		this.oorder = oorder;
	}
}
