package Group.Member;

public class GroupMember {
	private String situation;
	private String toUserId;
	
	public GroupMember() {
		this.toUserId = null;
	}
	
	public String getSituation() {
		return situation;
	}
	public void setSituation(String situation) {
		this.situation = situation;
	}
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
}
