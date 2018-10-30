var player;
var keyLabelArray;
var maxIndex = 0;
var exmaxIndex = 0;
var selectId = 0;
var element;		//存DOM元素

$(document).ready(function(){
	function get(name)
    {
	   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
	      return decodeURIComponent(name[1]);
   	}
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
    		$('#vid').append('<video controls="" name="media" id = "myvideo" ><source src="'+response.videoUrl+'" type="video/mp4"></video>');
    		
    		$('#unitName')[0].append(response.unitName);
            $('#schoolName')[0].append(response.schoolName);
    	    player=$("#myvideo")[0];
    	    $('#likesNum').text(response.likes);
    	    $('#introduction').append(response.courseInfo);
			seekTo(get("time") == null ? 0 : parseInt(get("time")));
			
			player.onended = function(){
				setTimeout(function(){
					window.location.href = $('#recommendList li a').attr('href');
				}, 3000)
			}
    	}
    });
    
	var jwPlayerActive = false;
	var videoStateTexts = [  
        'HAVE_NOTHING','HAVE_METADATA','HAVE_CURRENT_DATA',  
        'HAVE_FUTURE_DATA','HAVE_ENOUGH_DATA'  
    ]; 
	var pr = ['currentTime', 'duration', 'readyState','paused','muted','volume','ended'];  
	var statelist = $('#statelist');
	
//    $("#play").click(function(){  
//        player.play(); //播放影片  
//		
//    });  
//  
//    $("#pause").click(function(){  
//        player.pause(); //停止播放  
//    }); 
    
	var t = setInterval(function(){  	//每100毫秒執行一次
		if (player)
		{
			var now=(player["currentTime"]/player["duration"] * 100);	//percent
			$('.meter').css('width', now + '%');
		}
    },500);  
	
	
//----------------------------------------------video----------------------------------------------//
	
    

//----------------------------------------------keyLabel----------------------------------------------//    
//---------------------------抓影片結束時間，並儲存----------------------------------------------//
//---------------------------要設perconalPlan跟watchRecord兩個table-------------------------//
    window.onbeforeunload = function(event) {
    	console.log(getCurrentTime());
        $.ajax({
        	url:ajaxURL+'AnyCourse/PlayerInterfaceServlet.do',
        	method: 'POST',
            //必須取消非同步，否則還沒執行完就已經跳轉至其他頁面
            async:false,
        	data:{
        		"action": 'setVideoCloseTime',//代表要設定關閉頁面的時間
        		"currentTime":getCurrentTime(),//關閉的時間
        		"duration":getDuration(),//影片總共有多長時間
        		"unitId": get("unitId")
        	},
        	error: function(){
        		console.log("setVideoEndTime failed!");
        	}
        })
    }; 
//---------------------------抓影片結束時間，並儲存----------------------------------------------//
});

function seekTo(sec)
{
	player.currentTime = sec;
}

function getDuration()
{
	return Math.floor(player["duration"]);
}

function getCurrentTime()
{
	return  Math.floor(player["currentTime"]);
}
