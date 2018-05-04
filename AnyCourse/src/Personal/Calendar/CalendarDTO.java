package Personal.Calendar;

public class CalendarDTO
{
	private int id;
	private String title;
	private String url;
	private String start;
	private String end;
	private String backgroundColor;
	private String borderColor;
	private boolean allDay;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getStart()
	{
		return start;
	}
	public void setStart(String start)
	{
		this.start = start;
	}
	public String getEnd()
	{
		return end;
	}
	public void setEnd(String end)
	{
		this.end = end;
	}
	public String getBackgroundColor()
	{
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}
	public String getBorderColor()
	{
		return borderColor;
	}
	public void setBorderColor(String borderColor)
	{
		this.borderColor = borderColor;
	}
	public boolean isAllDay()
	{
		return allDay;
	}
	public void setAllDay(boolean allDay)
	{
		this.allDay = allDay;
	}
	@Override
	public String toString()
	{
		return "CalendarDTO [id=" + id + ", title=" + title + ", url=" + url + ", start=" + start + ", end=" + end
				+ ", backgroundColor=" + backgroundColor + ", borderColor=" + borderColor + ", allDay=" + allDay + "]";
	}
}
