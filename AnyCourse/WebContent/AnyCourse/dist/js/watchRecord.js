
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
						+'<div class="pull-left" style="padding-left: 0px;">'
						+'<div class="embed-responsive embed-responsive-16by9">'
						+'<img id="img" class="style-scope yt-img-shadow" alt="" width="230"'
						+'src="'+ (response[i].videoImgSrc != "" ? response[i].videoImgSrc : "https://i.imgur.com/eKSYvRv.png") +'">' 
						+'</div>'
						+'</div>'
						+'<div class="media-body">'
						+'<h4 class="media-heading">'
						+'<b class="video-name">' + response[i].unitName + '</b>'
						+'</h4>'
						+'<br>'
						+'<p class="unitUi">' + response[i].schoolName + '</p>'
						+'<br>'
						+'<p class="unitUi">' + response[i].likes +' 人喜歡</p>'
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
