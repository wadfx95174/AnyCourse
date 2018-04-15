package SearchRecord;

public class SearchRecord {
	
	private String Search_word;
	private String Search_time;
	private String user_id;
	
	public SearchRecord() {}
	public void setUserID(String user_id) {
		this.user_id = user_id;
	}
	public void setSearchWord(String Search_word) {
		this.Search_word = Search_word;
	}
	public void setSearchTime(String Search_time) {
		this.Search_time = Search_time;
	}
	public String getUserID() {
		return user_id;
	}
	public String getSearchRecord() {
		return Search_word;
	}
	public String getSearchTime() {
		return Search_time;
	}

}
