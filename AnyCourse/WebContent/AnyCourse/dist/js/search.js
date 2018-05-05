//var ajax_url="http://140.121.197.130:8400/";
var ajax_url="http://localhost:8080/";

function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
}
//才能知道要對哪個ID做動作
function getID(id){
    checkID = id;
}
var array = [];
$(document).ready(function(){
    checkLogin("", "../../");
	$.ajax({
		method:"GET",
		cache :false,
		url:ajax_url+'AnyCourse/SearchServlet.do',
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
							+'<div class="btn-group" style="top: -5px;">'
							+'<button type="button" class="btn btn-noColor dropdown-toggle"'
							+'data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'
							+'<span class="caret"></span>'
							+'</button>'
							+'<ul class="dropdown-menu">'
							+'<li><a data-toggle="modal" data-target="#addToCoursePlan_List" onclick="getID('+i+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>新增至課程計畫</a>'
							+'</li>'
							+'</ul>'
							+'</div>'
							+'<a class="list-group-item" href="PlayerInterface.html?type='+ (response[i].units[0].videoUrl.split("/")[2]=='www.youtube.com'?1:2) + '&unit_id='+response[i].units[0].unitId+'&list_id='+response[i].courselistId+'">'
							+'<div class="media">'
							+'<div class="media-body">'
							+'<div class="col-xs-4 pull-left" style="padding-left: 0px;">'
							+'<div class="embed-responsive embed-responsive-16by9">'
							+'<img id="img" class="style-scope yt-img-shadow" alt="" width="230"'
							+'src="'+ (response[i].units[0].videoImgSrc != "" ? response[i].units[0].videoImgSrc : "https://i.imgur.com/eKSYvRv.png") +'">' 
							+'</div>'
							+'</div>'
							+'<div class="col-xs-8>'
							+'<h4 class="media-heading">'
							+'<b>清單名稱:' + response[i].listName + '</b>'
							+'</h4>'
							+'<p style="margin-bottom: 5px;">開課大學:' + response[i].schoolName + '</p>'
							+'<p style="margin-bottom: 5px;">授課教師:' + response[i].teacher + '</p>'
							+'<p style="margin-bottom: 5px;">' + response[i].likes +' 人喜歡</p>'
							+'<p style="margin-bottom: 5px;height:70px;overflow:auto;">課程簡介:' + response[i].courseInfo + '</p>'
							+'</div>'
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
							+'<div class="btn-group" style="top: -5px;">'
							+'<button type="button" class="btn btn-noColor dropdown-toggle"'
							+'data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'
							+'<span class="caret"></span>'
							+'</button>'
							+'<ul class="dropdown-menu">'
							+'<li><a data-toggle="modal" data-target="#addToCoursePlan" onclick="getID('+i+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>新增至課程計畫</a>'
							+'</li>'
							+'</ul>'
							+'</div>'
							+'<a class="list-group-item" href="PlayerInterface.html?type='+ (response[i].units[0].videoUrl.split("/")[2]=='www.youtube.com'?1:2) + '&unit_id='+response[i].units[0].unitId+'">'
							+'<div class="media">'
							+'<div class="media-body">'
							+'<div class="col-xs-4 pull-left" style="padding-left: 0px;">'
							+'<div class="embed-responsive embed-responsive-16by9">'
							+'<img id="img" class="style-scope yt-img-shadow" alt="" width="230"'
							+'src="'+ (response[i].units[0].videoImgSrc != "" ? response[i].units[0].videoImgSrc : "https://i.imgur.com/eKSYvRv.png") +'">' 
							+'</div>'
							+'</div>'
							+'<h4 class="media-heading">'
							+'<b>影片名稱:' + response[i].units[0].unitName + '</b>'
							+'</h4>'
							+'<br>'
							+'<p style="margin-bottom: 5px;">開課大學:' + response[i].units[0].schoolName + '</p>'
							+'<br>'
							+'<p style="margin-bottom: 5px;">' + response[i].units[0].likes +' 人喜歡</p>'
							+'</div>'
							+'</div>'
							+'</a></li>'
					);
				}
			}
			//影片新增至課程計畫
			$('#addToCoursePlanButton').click(function(){
				
				$.ajax({
					url : ajax_url+'AnyCourse/SearchServlet.do',
					method : 'POST',
					cache: false,
					data:{
						action:'addToCoursePlan',
						unit_id:array[checkID]["units"][checkID]["unitId"]
					},
					error:function(){
						console.log("addToCoursePlan Error!");
					}
				})
			});
			//清單整個新增至課程計畫
			$('#addToCoursePlanButton_List').click(function(e){
				e.preventDefault();
				console.log(array[checkID]["units"][checkID]["unitId"]);
				$.ajax({
					url : ajax_url+'AnyCourse/HomePageServlet.do',
					method : 'POST',
					cache: false,
					data:{
						action:'addToCoursePlan_List',
						courselist_id:array[checkID]["courselistId"]
					},
					error:function(e){
						console.log("addToCoursePlan_List Error!");
					}
				})
			});
		},
		error: function(){
			console.log("search fail");
		}
	});
});