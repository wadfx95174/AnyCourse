package Personal.VideoList;

public class VideoList {
	private String list_name;
	private String creator;
	private static final int SHARE = 0;
	private static final int LIKES = 0;
	
	public VideoList() {}
	
	public void setListName(String list_name) {
		this.list_name = list_name;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getListName() {
		return list_name;
	}
	public String getCreator() {
		return creator;
	}
	public int getShare() {
		return SHARE;
	}
	public int getLikes() {
		return LIKES;
	}
}
