package Personal.WatchRecord;

public class WatchRecord {
	public String user_id;
	public int unit_id;
	public String watch_time;	
	public String school_name;
	public String unit_name;
	public String video_url;
	public int likes;
	public String video_img_src;
	
	
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
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public String getVideo_img_src() {
		return video_img_src;
	}
	public void setVideo_img_src(String video_img_src) {
		this.video_img_src = video_img_src;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(int unit_id) {
		this.unit_id = unit_id;
	}
	public String getWatch_time() {
		return watch_time;
	}
	public void setWatch_time(String watch_time) {
		this.watch_time = watch_time;
	}
}
