package Note;
    
public class PictureNote {
	public int pictureNoteId;
	public int unitId;
	public String userId;
	public String pictureNoteUrl;
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
	public int getPictureNoteId() {
		return pictureNoteId;
	}
	public void setPictureNoteId(int pictureNoteId) {
		this.pictureNoteId = pictureNoteId;
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
	public String getPictureNoteUrl() {
		return pictureNoteUrl; 
	}
	public void setPictureNoteUrl(String pictureNoteUrl) {
		this.pictureNoteUrl = pictureNoteUrl;
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
}
