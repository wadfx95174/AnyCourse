package Personal.PersonalNote;

public class PersonalTextNote {
	public int text_note_id;
	public int unit_id;
	public String user_id;
	public String text_note;
	public int share;
	public String share_time;
	public int likes;
	public String school_name;
	public String unit_name;
	public String video_url;
	
	public int getText_note_id() {
		return text_note_id;
	}
	public void setText_note_id(int text_note_id) {
		this.text_note_id = text_note_id;
	}
	public int getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(int unit_id) {
		this.unit_id = unit_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getText_note() {
		return text_note;
	}
	public void setText_note(String text_note) {
		this.text_note = text_note;
	}
	public int getShare() {
		return share;
	}
	public void setShare(int share) {
		this.share = share;
	}
	public String getShare_time() {
		return share_time;
	}
	public void setShare_time(String share_time) {
		this.share_time = share_time;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public String getSchool_name() {
		return school_name;
	}
	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}
	public String getUnit_name() {
		return unit_name;
	}
	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	
}
