package Note;
    
public class PictureNote {
	public int picture_note_id;
	public int unit_id;
	public String user_id;
	public String picture_note_url;
	public int share;
	public String share_time;
	public int likes;
	public int getPicture_note_id() {
		return picture_note_id;
	}
	public void setPicture_note_id(int picture_note_id) {
		this.picture_note_id = picture_note_id;
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
	public String getPicture_note_url() {
		return picture_note_url; 
	}
	public void setPicture_note_url(String picture_note_url) {
		this.picture_note_url = picture_note_url;
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
	
		
}
