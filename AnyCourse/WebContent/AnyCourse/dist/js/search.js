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
			console.log(array[1].units[1].videoUrl);
			/*
			 <a href="PlayerInterface.html" class="list-group-item">
                  <div class="col-xs-3" style="padding-left: 0px;">
                     <div class="embed-responsive embed-responsive-16by9">          
                        <img id="img" class="style-scope yt-img-shadow" alt="" width="210" src="https://i.ytimg.com/vi/B-AoHE6dPnk/hqdefault.jpg">        
                     </div>
                  </div>
                 <div class="col-xs-9 ">
                     <div class="col-md-3 col-xs-12 ">
                         <h3>導涵式公式的推廣</h3>
                         <p>中央大學 王老師</p>
                         <p>讚數:216,165</p>
                      </div>
                      <div class="col-md-9 col-xs-12 ">
                         <h3>簡介</h3>
                         <p>中央大學 王老師中央大學 王老師中央大學 王老師，中央大學 王老師中央大學 王老師</p>
                      </div>
                  </div>
             </a>
			 */ 
			for (var i = 0; i < response.length; i++)
			{
				$('#result').append('<div class="list-group-item">'
					  + '<a href="PlayerInterface.html?type='+ (response[i].units[0].videoUrl.split("/")[2]=='www.youtube.com'?1:2) + '&unit_id='+response[0].units[1].unitId+'">'
					  + '<div class="col-md-3 col-xs-3" style="padding-left: 0px;">'
					  + '<div class="embed-responsive embed-responsive-16by9">'
					  + '<img id="img" class="style-scope yt-img-shadow" alt="" width="210" src="'+response[i].units[1].videoImgSrc+'">'
					  + '</div>'
					  + '</div>'
					  + '<div class="col-md-2 col-xs-4 ">'
					  + '<h3>'+response[i].schoolName+' '+response[i].listName+'</h3>'
					  + '<p>'+response[i].teacher+'</p>'
					  + '<p>'+response[i].likes+'人喜歡'+'</p>'
					  + '</div>'
					  + '</a>'
					  + '<div class="col-md-7 col-xs-5 id="video_list_'+i+'">'
					  + '<h3>影片列表</h3></div></div>');
				for (var j = 0; j < response[i].units.length; j++)
				{

					$('#video_list_'+i).append('<a href="PlayerInterface.html?type='+ (response[i].units[j].videoUrl.split("/")[2]=='www.youtube.com'?1:2) + '&unit_id='+response[i].units[j].unitId+'">'+response[i].units[j].unitName+'</a>');
				}
				
			}
		},
		error: function(){
			alert("fail");
		}
	});
});