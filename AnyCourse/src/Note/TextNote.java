package Note;
   
public class TextNote {
	
	public int textNoteID;
	public int unitID;
	public String userID;
	public String nickName;
	public String textNote;
	public int share;
	public String shareTime;
	public int likes;
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public int getTextNoteID() {
		return textNoteID;
	}


	public void setTextNoteID(int textNoteID) {
		this.textNoteID = textNoteID;
	}


	public int getUnitID() {
		return unitID;
	}


	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}


	public String getUserID() {
		return userID;
	}


	public void setUserID(String userID) {
		this.userID = userID;
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
		return "TextNote [textNoteID=" + textNoteID + ", unitID=" + unitID + ", userID=" + userID
				+ ", textNote=" + textNote + ", share=" + share + ", shareTime=" + shareTime + ", likes=" + likes
				+ "]";
	}
	
	
	
}
