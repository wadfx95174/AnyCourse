var player;
var uid;

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
		url:ajaxURL+'AnyCourse/PlayerInterfaceServlet.do',
    	method: 'POST',
    	cache :false,
    	data: {
    		"action": 'setIsBrowse',
    		"unitId": get('unitId')
    	},
    	error: function(){
    		console.log("SetIsBrowse Error!!!")
    	},
    	success: function(response){
    	    if(response.personalLike == 0){$('#likesIcon').addClass('fa-heart-o');}
    	    else if(response.personalLike == 1){$('#likesIcon').addClass('fa-heart');}
    	}
	})
	
  //---------------------------抓影片結束時間，並儲存------------------------------------------//
  //---------------------------要設perconalPlan跟watchRecord兩個table-------------------------//
    window.onbeforeunload = function(event) {
		    // console.log(Math.floor(getCurrentTime()));
      //   console.log(Math.floor(getDuration()));
      //   console.log(get("unitId"));
        if(get("groupId")){
          $.ajax({
            url:ajaxURL+'AnyCourse/PlayerInterfaceServlet.do',
            method: 'POST',
            cache :false,
              //必須取消非同步，否則還沒執行完就已經跳轉至其他頁面
            async:false,
            data:{
              "action": 'setGroupVideoCloseTime',
              "currentTime": Math.floor(getCurrentTime()),//關閉的時間
              "duration": Math.floor(getDuration()),//影片總共有多長時間
              "unitId": get("unitId"),
              "groupId": get("groupId")
            },
              error:function(xhr, ajaxOptions, thrownError){
                    console.log("youtubePlayer.js setGroupVideoCloseTime failed");
                    console.log(xhr);
                    console.log(xhr.status);
                    console.log(xhr.responseText);
                    console.log(thrownError);
              }
          });
        }
        else{
          $.ajax({
            url:ajaxURL+'AnyCourse/PlayerInterfaceServlet.do',
            method: 'POST',
            cache :false,
              //必須取消非同步，否則還沒執行完就已經跳轉至其他頁面
            async:false,
            data:{
              "action": 'setVideoCloseTime',
              "currentTime": Math.floor(getCurrentTime()),//關閉的時間
              "duration": Math.floor(getDuration()),//影片總共有多長時間
              "unitId": get("unitId")
            },
              error:function(xhr, ajaxOptions, thrownError){
                    console.log("youtubePlayer.js setVideoCloseTime failed");
                    console.log(xhr);
                    console.log(xhr.status);
                    console.log(xhr.responseText);
                    console.log(thrownError);
              }
          });
        }
    }; 
  //---------------------------/.抓影片結束時間，並儲存-----------------------------------------//
})

//--------------------------youtube iframe api-----------------------------
function onYouTubeIframeAPIReady() {
//	console.log(get('unitId'));
	$.ajax({
		url: ajaxURL+'AnyCourse/PlayerInterfaceServlet.do',
		method: 'GET',
		cache :false,
		data: {
			"method": 'getVideo',
			"unitId": get('unitId')
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
			          start: get("time")
			        },
			      events: {                   //哪些狀態執行哪些func
              'onReady': onPlayerReady,   //ready後會執行 onPlayerReady func
              'onStateChange': onPlayerStateChange
			      }

			    });
			    player.personalPlayer = {'currentTimeSliding': false,  //初始化參數，滑動bar會用到
			                                    'errors': []};
			    
			$('#unitName')[0].append(response.unitName);
      $('#schoolName')[0].append(response.schoolName);
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

    /*  取消開始時播放  */
    // event.target.playVideo();   //  播放
}

function onPlayerStateChange(event) {        
  if(event.data === 0) {          
    setTimeout(function(){
      window.location.href = $('#recommendList li a').attr('href');
    }, 3000)
  }
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

    var firstScriptTag = document.getElementsByTagName('script')[0];

    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);


    // Set timer to display infos
    setInterval(youTubePlayerDisplay, 100);   // 每秒(100毫秒)執行funct 一次
}
    