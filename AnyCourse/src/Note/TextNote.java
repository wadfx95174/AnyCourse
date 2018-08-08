package Note;
   
public class TextNote {
	
	public int textNoteId;
	public int unitId;
	public String userId;
	public String nickName;
	public String textNote;
	public int share;
	public String shareTime;
	public int likes;
	public int categoryId;
	
	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public int getTextNoteId() {
		return textNoteId;
	}

	public void setTextNoteId(int textNoteId) {
		this.textNoteId = textNoteId;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTextNote() {
		return textNote;
	}

	public void setTextNote(String textNote) {
		this.textNote = textNote;
	}

	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public String getShareTime() {
		return shareTime;
	}

	public void setShareTime(String shareTime) {
		this.shareTime = shareTime;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}
	
	@Override
	public String toString() {
		return "TextNote [textNoteId=" + textNoteId + ", unitId=" + unitId + ", userId=" + userId
				+ ", textNote=" + textNote + ", share=" + share + ", shareTime=" + shareTime + ", likes=" + likes
				+ "]";
	}
}
