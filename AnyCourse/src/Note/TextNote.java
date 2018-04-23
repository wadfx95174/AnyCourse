package Note;
   
public class TextNote {
	
	public int text_note_id;
	public int unit_id;
	public String user_id;
	public String text_note;
	public int share;
	public String share_time;
	public int likes;
	
	
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


	@Override
	public String toString() {
		return "TextNote [text_note_id=" + text_note_id + ", unit_id=" + unit_id + ", user_id=" + user_id
				+ ", text_note=" + text_note + ", share=" + share + ", share_time=" + share_time + ", likes=" + likes
				+ "]";
	}
	
	
	
}
