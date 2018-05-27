//var ajaxURL="http://140.121.197.130:8400/";
var ajaxURL="http://localhost:8080/";

$(document).ready(function(){
	checkLogin("../", "../../../");
	$.ajax({
		method:"GET",
		cache :false,
		url:ajaxURL+'AnyCourse/WatchRecordServlet.do',
		success: function(response){
			console.log(response);
			array = response;
			for (var i = 0; i < response.length; i++)
			{
				
				$('#result').append(
						'<li>'
						+'<a class="list-group-item" href="../PlayerInterface.html?type='+ (response[i].videoUrl.split("/")[2]=='www.youtube.com'?1:2) + '&unitId='+response[i].unitId+'">'
						+'<div class="media">'
						+'<div class="media-body">'
						+'<div class="col-xs-4 pull-left" style="padding-left: 0px;">'
						+'<div class="embed-responsive embed-responsive-16by9">'
						+'<img id="img" class="style-scope yt-img-shadow" alt="" width="230"'
						+'src="'+response[i].videoImgSrc+'">' 
						+'</div>'
						+'</div>'
						+'<h4 class="media-heading">'
						+'<b>影片名稱:' + response[i].unitName + '</b>'
						+'</h4>'
						+'<br>'
						+'<p style="margin-bottom: 5px;">開課大學:' + response[i].schoolName + '</p>'
						+'<br>'
						+'<p style="margin-bottom: 5px;">讚數:' + response[i].likes +'</p>'
						+'</div>'
						+'</div>'
						+'</a></li>'
				);
			}
		},
		error: function(){
			alert("fail");
		}
	});
});
