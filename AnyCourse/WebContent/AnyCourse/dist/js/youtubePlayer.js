var player;
var uid;

//var ajaxURL="http://140.121.197.130:8400/AnyCourse/PlayerInterfaceServlet.do";
var ajaxURL="http://localhost:8080/AnyCourse/PlayerInterfaceServlet.do";
function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
}

var setVideoCloseTime = 0;

$(document).ready(function(){
	init();
	//設瀏覽紀錄，或是已經有瀏覽紀錄則加1，並檢查他是否已經有按讚
	$.ajax({
		url:ajaxURL,
    	method: 'POST',
    	cache :false,
    	data: {
    		"action": 'setIsBrowse',
    		"unitId": get('unit_id')
    	},
    	error: function(){
    		console.log("SetIsBrowse Error!!!")
    	},
    	success: function(response){
//    		console.log(response);
//    		console.log("aaa");
    	    console.log(response.personalLike);
    	    if(response.personalLike == 0){$('#likesIcon').addClass('fa-heart-o');}
    	    else if(response.personalLike == 1){$('#likesIcon').addClass('fa-heart');}
    	}
	})
	
  //---------------------------抓影片結束時間，並儲存----------------------------------------------//
  //---------------------------要設perconal_plan跟watch_record兩個table-------------------------//
    window.onbeforeunload = function(event) { 
    	var current = player.getCurrentTime();
    	var duration = player.getDuration();
        console.log(Math.floor(duration));
        $.ajax({
        	url:ajaxURL,
        	method: 'POST',
        	cache :false,
        	data:{
        		"action": 'setVideoCloseTime',//代表要設定關閉頁面的時間
        		"currentTime": Math.floor(current),//關閉的時間
        		"duration": Math.floor(duration),//影片總共有多長時間
        		"unitId" : get("unit_id")
        	},
        	error: function(){
        		console.log("setVideoEndTime failed!");
        	}
        })
    }; 
  //---------------------------抓影片結束時間，並儲存----------------------------------------------//
})

//--------------------------youtube iframe api-----------------------------
function onYouTubeIframeAPIReady() {
//	console.log(get('unit_id'));
	$.ajax({
		url: ajaxURL,
		method: 'GET',
		cache :false,
		data: {
			"method": 'getVideo',
			"unitId": get('unit_id')
		},
		error: function(){
		},
		success: function(response){
//			console.log(response.videoUrl);
			uid = response.videoUrl.split('/')[4];

			player = new YT.Player('youTubePlayer', {
			      height: '390',
			      width: '640',
			      videoId: uid,     //影片ID
			      playerVars: {
			          autoplay: 1,
			          start: get("time")
			        },
			      events: {                   //哪些狀態執行哪些func
			        'onReady': onPlayerReady,   //ready後會執行 onPlayerReady func
			      }

			    });
			    player.personalPlayer = {'currentTimeSliding': false,  //初始化參數，滑動bar會用到
			                                    'errors': []};
			    
			$('h3')[0].append(response.unitName);
//			if(response.personalLike == 0){$('#likesIcon').addClass('fa-heart-o');}
//    	    else if(reponse.personalLike == 1){$('#likesIcon').addClass('fa-heart');}
    	    $('#likesNum').text(response.likes);
    	    $('#introduction').append(response.courseInfo);
		}
	});
    
}
// ready後用到的func
function onPlayerReady(event) {   
    player.setPlaybackRate(1);   //影片速率
    event.target.playVideo();   //  播放
}


  // var done = false;
  // function onPlayerStateChange(event) {
  //   if (event.data == YT.PlayerState.PLAYING && !done) {
  //     setTimeout(stopVideo, 6000);
  //     done = true;
  //   }
  // }
  
function stopVideo() 
{
    player.stopVideo();
}

// 跳轉到幾秒
function seekTo(sec)
{
    player.seekTo(sec, true);
}
  
function getDuration()
{
	return player.getDuration();
}
  
function getCurrentTime()
{
	return player.getCurrentTime();
}
  
function changeId(videoid)
{
	player.loadVideoById("videoid", 0, "default");
}

// timechange會用到
function youTubePlayerActive() 
{
    return player && player.hasOwnProperty('getPlayerState');
}

// 移動完後影片跳轉到該位置
function youTubePlayerCurrentTimeChange(currentTime) 
{
    player.personalPlayer.currentTimeSliding = false;
    if (youTubePlayerActive()) {
        player.seekTo(currentTime*player.getDuration()/100, true);
    }
}

// 移動中
function youTubePlayerCurrentTimeSlide() 
{
    player.personalPlayer.currentTimeSliding = true;
}

function youTubePlayerDisplay() 
{
    if (youTubePlayerActive()) {
        var state = player.getPlayerState();

        var current = player.getCurrentTime();
        var duration = player.getDuration();
        var currentPercent = (current && duration
                              ? current*100/duration
                              : 0);

        if (!current) {
            current = 0;
        }
        if (!duration) {
            duration = 0;
        }
        // 下方進度條跟著影片動
        if (!player.personalPlayer.currentTimeSliding) {
        	$('.meter').css('width', currentPercent + '%');
        }
    }
}

function init() {
    // Load YouTube library
    var tag = document.createElement('script');

    tag.src = 'https://www.youtube.com/iframe_api';

    var first_script_tag = document.getElementsByTagName('script')[0];

    first_script_tag.parentNode.insertBefore(tag, first_script_tag);


    // Set timer to display infos
    setInterval(youTubePlayerDisplay, 100);   // 每秒(100毫秒)執行funct 一次
}
    