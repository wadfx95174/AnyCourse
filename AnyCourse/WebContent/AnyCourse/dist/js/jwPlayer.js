var ajax_url="http://140.121.197.130:8400/";
//var ajax_url="http://localhost:8080/";

var player;
//var keyLabels = [];
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
		url:ajax_url+'AnyCourse/PlayerInterfaceServlet.do',
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
//    		alert(response);
//    		console.log("aaa");
//    	    console.log(response.personalLike);
    	    if(response.personalLike == 0){$('#likesIcon').addClass('fa-heart-o');}
    	    else if(response.personalLike == 1){$('#likesIcon').addClass('fa-heart');}
    	}
	})
	$.ajax({
    	url: ajax_url+'AnyCourse/PlayerInterfaceServlet.do',
    	method: 'GET',
    	cache :false,
    	data: {
    		"method": 'getVideo',
    		"unitId": get('unit_id')
    	},
    	error: function(){
    	},
    	success: function(response){
    		$('#vid').append('<video controls="" autoplay="autoplay" name="media" id = "myvideo" ><source src="'+response.videoUrl+'" type="video/mp4"></video>');
    		
    		$('h3')[0].append(response.unitName);
    	    player=$("#myvideo")[0];
//    	    console.log(response.personalLike);
//    	    if(response.personalLike == 0){$('#likesIcon').addClass('fa-heart-o');}
//    	    else if(reponse.personalLike == 1){$('#likesIcon').addClass('fa-heart');}
    	    $('#likesNum').text(response.likes);
    	    $('#introduction').append(response.courseInfo);
    	    //player.currentTime = get("time") == null ? 0 : parseInt(get("time"));
    	    seekTo(get("time") == null ? 0 : parseInt(get("time")));
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
//---------------------------要設perconal_plan跟watch_record兩個table-------------------------//
    window.onbeforeunload = function(event) { 
        console.log(player["currentTime"]);
        console.log(player["duration"]);
        console.log(get("unit_id"));
        
        $.ajax({
        	url:ajax_url+'AnyCourse/PlayerInterfaceServlet.do',
        	method: 'POST',
        	data:{
        		"action": 'setVideoCloseTime',//代表要設定關閉頁面的時間
        		"currentTime":Math.floor(player["currentTime"]),//關閉的時間
        		"duration":Math.floor(player["duration"]),//影片總共有多長時間
        		"unitId": get("unit_id")
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
	return player["currentTime"];
}
