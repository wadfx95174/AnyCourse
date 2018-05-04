package WebScraper.Output;

import java.util.ArrayList;

public class OutputFormat {
	private String university;
	private String courseName;
	private String department;
	private String teacher;
	private String courseInfo;
	private ArrayList<String> unitName;
	private ArrayList<String> unitURL;
	private ArrayList<String> unitImgSrc;
	private ArrayList<String> lectureName;
	private ArrayList<String> lecture;
	
	public OutputFormat() {
		unitName = new ArrayList<String>();
		unitURL = new ArrayList<String>();
		unitImgSrc = new ArrayList<String>();
		lectureName = new ArrayList<String>();
		lecture = new ArrayList<String>();
	}
	
	public void setUniversity(String university) {
		this.university = university;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public void setCourseInfo(String courseInfo) {
		
		this.courseInfo = courseInfo;
	}
	public void setUnitName(String unitName) {
		this.unitName.add(unitName);
	}
	public void setUnitURL(String unitURL) {
		this.unitURL.add(unitURL);
	}
	public void setUnitImgSrc(String unitImgSrc) {
		this.unitImgSrc.add(unitImgSrc);
	}
	public void setLectureName(String lectureName) {
		this.lectureName.add(lectureName);
	}
	public void setLecture(String lecture) {
		this.lecture.add(lecture);
	}
	
	public String getUniversity(){
		return university;
	}
	public String getCourseName(){
		return courseName;
	}
	public String getDepartment(){
		return department;
	}
	public String getTeacher(){
		return teacher;
	}
	public String getCourseInfo(){
		return courseInfo;	
	}
	public ArrayList<String> getUnitName() {
		return unitName;
	}
	public ArrayList<String> getUnitURL(){
		return unitURL;
	}
	public ArrayList<String> getUnitImgSrc(){
		return unitImgSrc;
	}

	public ArrayList<String> getLectureName(){
		return lectureName;
	}
	public ArrayList<String> getLecture(){
		return lecture;
	}

	@Override
	public String toString()
	{
		return "OutputFormat [university=" + university + ", courseName=" + courseName + ", department=" + department
				+ ", teacher=" + teacher + ", courseInfo=" + courseInfo + ", unitName=" + unitName + ", unitURL="
				+ unitURL + ", unitImgSrc=" + unitImgSrc + ", lectureName=" + lectureName + ", lecture=" + lecture
				+ "]";
	}
//	public String toString() {
//		return (getUniversity()+getCourseName()+getUnitName()+getUnitURL()+getLectureName()
//		+getLecture()+getCourseInfo()/*+department+teacher*/);
//	}
}