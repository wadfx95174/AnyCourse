$('#result').slimScroll({
	height: '400px;'
});

$(document).ready(function(){
	checkLogin("../", "../../../");
	$.ajax({
		method:"GET",
		cache :false,
		url:ajaxURL+'AnyCourse/WatchRecordServlet.do',
		success: function(response){
			console.log(response);
			array = response;
			var img;
			if(window.screen.width > 480){
				img = '<img id="img" class="style-scope yt-img-shadow" style="width:200px;"'
			}
			else{
				img = '<img id="img" class="style-scope yt-img-shadow" style="width:100%;"'
			}
			for (var i = 0; i < response.length; i++)
			{
				
				$('#result').append(
						'<li>'
						+'<a class="list-group-item" href="../PlayerInterface.html?type='+ (response[i].videoUrl.split("/")[2]=='www.youtube.com'?1:2) + '&unitId='+response[i].unitId+'">'
						+'<div class="media">'
						+'<div class="pull-left" style="padding-left: 0px;">'
						+'<div class="embed-responsive embed-responsive-16by9" style="width:100%; padding-bottom:80%;">'
						+img
						+'src="'+ (response[i].videoImgSrc != "" ? response[i].videoImgSrc : "https://i.imgur.com/eKSYvRv.png") +'">' 
						+'</div>'
						+'</div>'
						+'<div class="media-body">'
						+'<h4 class="media-heading video-name">' + response[i].unitName +'</h4>'
						+'<p class="unitUi">' + response[i].schoolName + '</p>'
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
