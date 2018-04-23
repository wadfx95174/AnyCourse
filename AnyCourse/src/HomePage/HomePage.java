package HomePage;

public class HomePage {
	private String user_id;
	private int unit_id;
	private String list_name;
	private String unit_name;
	private String school_name;
	private String teacher;
	private int likes;
	
	public HomePage() {}
	
	public void setUserID(String user_id) {
		this.user_id = user_id;
	}
	public void setUnitID(int unit_id) {
		this.unit_id = unit_id;
	}
	public void setListName(String list_name) {
		this.list_name = list_name;
	}
	public void setUnitName(String unit_name) {
		this.unit_name = unit_name;
	}
	public void setSchoolName(String school_name) {
		this.school_name = school_name;
	}
	public String getUserID() {
		return user_id;
	}
	public int getUnitID() {
		return unit_id;
	}
}
