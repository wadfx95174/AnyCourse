package Search;

public class Unit
{
	private int unitId;
	private String unitName;
	private String videoUrl;
	private int likes;
	private String videoImgSrc;
	public int getUnitId()
	{
		return unitId;
	}
	public void setUnitId(int unitId)
	{
		this.unitId = unitId;
	}
	public String getUnitName()
	{
		return unitName;
	}
	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}
	public String getVideoUrl()
	{
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl)
	{
		this.videoUrl = videoUrl;
	}
	public int getLikes()
	{
		return likes;
	}
	public void setLikes(int likes)
	{
		this.likes = likes;
	}
	public String getVideoImgSrc()
	{
		return videoImgSrc;
	}
	public void setVideoImgSrc(String videoImgSrc)
	{
		this.videoImgSrc = videoImgSrc;
	}
	@Override
	public String toString()
	{
		return "Unit [unitId=" + unitId + ", unitName=" + unitName + ", videoUrl=" + videoUrl + ", likes=" + likes
				+ ", videoImgSrc=" + videoImgSrc + "]";
	}
}
