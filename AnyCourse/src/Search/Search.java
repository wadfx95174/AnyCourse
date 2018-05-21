package Search;

import java.util.ArrayList;

public class Search
{
	private int courselistId;
	private String schoolName;
	private String listName;
	private String teacher;
	private String departmentName;
	private String courseInfo;
	private String creator;
	private int share;
	private int likes;
	private ArrayList<Unit> units;
	public Search()
	{
		units = new ArrayList<Unit>();
	}
	public int getCourselistId()
	{
		return courselistId;
	}
	public void setCourselistId(int courselistId)
	{
		this.courselistId = courselistId;
	}
	public String getSchoolName()
	{
		return schoolName;
	}
	public void setSchoolName(String schoolName)
	{
		this.schoolName = schoolName;
	}
	public String getListName()
	{
		return listName;
	}
	public void setListName(String listName)
	{
		this.listName = listName;
	}
	public String getTeacher()
	{
		return teacher;
	}
	public void setTeacher(String teacher)
	{
		this.teacher = teacher;
	}
	public String getDepartmentName()
	{
		return departmentName;
	}
	public void setDepartmentName(String departmentName)
	{
		this.departmentName = departmentName;
	}
	public String getCourseInfo()
	{
		return courseInfo;
	}
	public void setCourseInfo(String courseInfo)
	{
		this.courseInfo = courseInfo;
	}
	public String getCreator()
	{
		return creator;
	}
	public void setCreator(String creator)
	{
		this.creator = creator;
	}
	public int getShare()
	{
		return share;
	}
	public void setShare(int share)
	{
		this.share = share;
	}
	public int getLikes()
	{
		return likes;
	}
	public void setLikes(int likes)
	{
		this.likes = likes;
	}
	public ArrayList<Unit> getUnits()
	{
		return units;
	}
	public void setUnits(ArrayList<Unit> units)
	{
		this.units = units;
	} 
	public void addUnit(Unit unit)
	{
		this.units.add(unit);
	}
	@Override
	public String toString()
	{
		return "Search [courselistId=" + courselistId + ", schoolName=" + schoolName + ", listName=" + listName
				+ ", teacher=" + teacher + ", departmentName=" + departmentName + ", creator=" + creator + ", share=" + share + ", likes=" + likes + ", units=" + units + "]";
	}
	/*", courseInfo=" + courseInfo + */
}
