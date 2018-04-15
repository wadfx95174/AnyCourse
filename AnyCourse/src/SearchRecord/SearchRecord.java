package SearchRecord;

public class SearchRecord {
	
	private String search_word;
	private String search_time;
	private String user_id;
	
	public SearchRecord() {}
	public void setUserID(String user_id) {
		this.user_id = user_id;
	}
	public void setSearchWord(String search_word) {
		this.search_word = search_word;
	}
	public void setSearchTime(String search_time) {
		this.search_time = search_time;
	}
	public String getUserID() {
		return user_id;
	}
	public String getSearchWord() {
		return search_word;
	}
	public String getSearchTime() {
		return search_time;
	}

}
