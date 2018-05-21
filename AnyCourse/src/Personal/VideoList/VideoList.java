package Personal.VideoList;

public class VideoList {
	private int courselistID;
	private String listName;
	private String creator;
	private String userID;
	private String schoolName;
	private int share;
	private int likes;
	private int oorder;
	
	public VideoList() {}

	public int getCourselistID() {
		return courselistID;
	}

	public void setCourselistID(int courselistID) {
		this.courselistID = courselistID;
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

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
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
