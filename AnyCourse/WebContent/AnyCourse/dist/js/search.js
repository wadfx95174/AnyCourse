function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
}
var array = [];
$(document).ready(function(){
	$.ajax({
		method:"GET",
		url:'http://localhost:8080/AnyCourse/SearchServlet.do',
		data: {
			search_query: get('search_query')
		},
		success: function(response){
			console.log(response);
			array = response;
			for (var i = 0; i < response.length; i++)
			{
				if (response[i].courselistId > 0)
				{
					$('#result').append(
							'<li>'
							+'<a class="list-group-item" href="PlayerInterface.html?type='+ (response[i].units[0].videoUrl.split("/")[2]=='www.youtube.com'?1:2) + '&unit_id='+response[i].units[0].unitId+'">'
							+'<div class="media">'
							+'<div class="media-body">'
							+'<div class="col-xs-4 pull-left" style="padding-left: 0px;">'
							+'<div class="embed-responsive embed-responsive-16by9">'
							+'<img id="img" class="style-scope yt-img-shadow" alt="" width="230"'
							+'src="'+response[i].units[0].videoImgSrc+'">' 
							+'</div>'
							+'</div>'
							+'<h4 class="media-heading">'
							+'<b>清單名稱:' + response[i].listName + '</b>'
							+'</h4>'
							+'<p style="margin-bottom: 5px;">開課大學:' + response[i].schoolName + '</p>'
							+'<p style="margin-bottom: 5px;">授課教師:' + response[i].teacher + '</p>'
							+'<p style="margin-bottom: 5px;">讚數:' + response[i].likes +'</p>'
							+'<p style="margin-bottom: 5px;">課程簡介:' + response[i].courseInfo + '</p>'
							+'</div>'
							+'</div>'
							+'</a></li>'
					);
//					for (var j = 0; j < response[i].units.length; j++)
//					{
//						$('#video_list_'+i).append('<a href="PlayerInterface.html?type='+ (response[i].units[j].videoUrl.split("/")[2]=='www.youtube.com'?1:2) + '&unit_id='+response[i].units[j].unitId+'">'+response[i].units[j].unitName+'</a>');
//					}
				}
				else
				{
					$('#result').append(
							'<li>'
							+'<a class="list-group-item" href="PlayerInterface.html?type='+ (response[i].units[0].videoUrl.split("/")[2]=='www.youtube.com'?1:2) + '&unit_id='+response[i].units[0].unitId+'">'
							+'<div class="media">'
							+'<div class="media-body">'
							+'<div class="col-xs-4 pull-left" style="padding-left: 0px;">'
							+'<div class="embed-responsive embed-responsive-16by9">'
							+'<img id="img" class="style-scope yt-img-shadow" alt="" width="230"'
							+'src="'+response[i].units[0].videoImgSrc+'">' 
							+'</div>'
							+'</div>'
							+'<h4 class="media-heading">'
							+'<b>影片名稱:' + response[i].units[0].unitName + '</b>'
							+'</h4>'
							+'<br>'
							+'<p style="margin-bottom: 5px;">開課大學:' + response[i].units[0].schoolName + '</p>'
							+'<br>'
							+'<p style="margin-bottom: 5px;">讚數:' + response[i].units[0].likes +'</p>'
							+'</div>'
							+'</div>'
							+'</a></li>'
					);
				}
			}
		},
		error: function(){
			alert("fail");
		}
	});
});