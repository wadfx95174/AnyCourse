package Group.Management;

import java.util.ArrayList;

public class GroupInfo
{
	private String groupName;
	private int groupId;
	private ArrayList<Member> managers;
	private ArrayList<Member> members;
	public GroupInfo()
	{
		managers = new ArrayList<Member>();
		members = new ArrayList<Member>();
	}
	public String getGroupName()
	{
		return groupName;
	}
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}
	public int getGroupId()
	{
		return groupId;
	}
	public void setGroupId(int groupId)
	{
		this.groupId = groupId;
	}
	public ArrayList<Member> getMembers()
	{
		return members;
	}
	public void setMembers(ArrayList<Member> members)
	{
		this.members = members;
	}
	public ArrayList<Member> getManagers()
	{
		return managers;
	}
	public void setManagers(ArrayList<Member> managers)
	{
		this.managers = managers;
	}
	public void addMember(Member member)
	{
		this.members.add(member);
	}
	public void addManager(Member member)
	{
		this.managers.add(member);
	}
}
