
$('#noteArea').slimScroll({
    height: '150px'
  });
$('.tab-content').slimScroll({
    height: '300px'
  });
$('#keyLabel1').slimScroll({
    height: '130px'
  });
$('#keyLabel2').slimScroll({
    height: '130px'
  });
$('#recommend').slimScroll({
    height: '612px'
  });


var video;
//var keyLabels = [];

var keyLabelArray;

$(document).ready(function(){
    $("#editNote").click(function(){
        $("#noteFooter").slideToggle();
    });
    $("#cancelNote").click(function(){
        $("#noteFooter").slideToggle();
    });

    $( function() {
    $( "#keyLabel1, #keyLabel2" ).sortable({
//      connectWith: ".connectedSortable"
    }).disableSelection();
    });
    
//----------------------------------------------video----------------------------------------------//
    var video=$("#myvideo")[0];
	var jwPlayerActive = false;
	var videoStateTexts = [  
        'HAVE_NOTHING','HAVE_METADATA','HAVE_CURRENT_DATA',  
        'HAVE_FUTURE_DATA','HAVE_ENOUGH_DATA'  
    ]; 
	var pr = ['currentTime', 'duration', 'readyState','paused','muted','volume','ended'];  
	var statelist = $('#statelist');
    $("#play").click(function(){  
        video.play(); //播放影片  
		
    });  
  
    $("#pause").click(function(){  
        video.pause(); //停止播放  
    }); 
	
	var t = setInterval(function(){  	//每100毫秒執行一次
		if (video)
		{
			statelist.empty();  
			var now=(video["currentTime"]/video["duration"])*618;
			$(".now_position").css("left",now);
			if(video.currentTime>5 && video.currentTime<5.2){
				video.currentTime=10;
			}
			$("#Jw-player-progress").val(video["currentTime"]/video["duration"] * 100);
            $("progress").val(video["currentTime"]/video["duration"] * 100);
            
			// $(".now_position").css("left","");
	        $('#state').text(videoStateTexts[video.readyState]);  
	        // for(x in pr){  
	        //     statelist.append($('<li><storng>'+ pr[x]  + ' : ' + video[pr[x]] + '</strong></li>'));  
	        // }  
		}
        
    },500);  
	
	$(".changeto").click(function(){
		$go_track=$(this).attr("alt");
		video.currentTime=$go_track;
		//video[currentTime]=$go_track;
	});
	
	//跳轉時間
	// var clicking=false;
	// $(".track_bar").mousedown(function(e){
	// 	clicking=true;
	// 	video.currentTime=(e.pageX*video["duration"])/618-100.5;
	// });
	// $(".track_bar").mouseup(function(e){
	// 	clicking=false;
	// });
	// $(".track_bar").mousemove(function(e){
	// 	if(clicking == false) return;
	// 	video.currentTime=(e.pageX*video["duration"])/618-100.5;
	// });
	function jwPlayerCurrentTimeSeek(currentTime)
	{
		video.currentTime = currentTime;
	}

	// function jwPlayerSeeking()
	// {
	// 	if (video.seeking)jwPlayerActive = false;
	// }

	$("#Jw-player-progress").change(function (e) {
        var value = e.target.value;
        $("progress").val(value);
        jwPlayerCurrentTimeSeek(value * video["duration"] /100);
	});
	// function jwPlayerCurrentTimeChange(currentTime)
	// {
 //        $("progress").val(currentTime);
 //        jwPlayerCurrentTimeChange(currentTime * video["duration"] /100);
	// }
//----------------------------------------------video----------------------------------------------//
	

//----------------------------------------------keyLabel----------------------------------------------//    
	//取得資料庫的資料    
    $.ajax({
		url : 'http://localhost:8080/AnyCourse/KeyLabelServlet.do',
		method : 'GET', 
		data : {
			"unit_id" : 1		//unitid
		},
		success:function(result){
			keyLabelArray = result;
    		for(var i = 0 ;i < result.length;i++){
    			// 把資料放到個人標籤
    			if (keyLabelArray[i].userId == 1)	//id 1要改成session的id
				{
    				$('#keyLabel1').append('<li class="list-group-item">'
    						+ result[i].keyLabelName
    						+'<ul class="list-group-submenu">'
    						+'<a href="#" style="color: #FFF"><li class="list-group-submenu-item">刪除</li></a>'
    						+'<a href="#" style="color: #FFF"><li class="list-group-submenu-item primary">編輯</li></a>'
    						+'<a href="#" style="color: #FFF"><li class="list-group-submenu-item info">分享</li></a>'
    						+'<a href="#" class = "kl" id = "kl-' + i + '" style="color: #FFF"><li class="list-group-submenu-item lightBlue">使用</li></a>'
    						+'</ul>'
    						+'</li>');
				}
    			else
				{
    				$('#keyLabel2').append('<li class="list-group-item">'
    						+ result[i].keyLabelName
    						+'<ul class="list-group-submenu">'
    						+'<a href="javascript:void(0)" style="color: #FFF"><li class="list-group-submenu-item">刪除</li></a>'
    						+'<a href="javascript:void(0)" style="color: #FFF"><li class="list-group-submenu-item primary">添加</li></a>'
    						+'<a href="#" class = "kl" id = "kl-' + i + '" style="color: #FFF"><li class="list-group-submenu-item lightBlue">使用</li></a>'
    						+'</ul>'
    						+'</li>');
				}
    			
			}
    		
    		// 重點標籤 從右滑出
    		$(function () {
    		    $('.list-group-item').on('mouseover', function(event) {
    		        event.preventDefault();
    		        $(this).closest('li').addClass('open');
    		    });
    		      $('.list-group-item').on('mouseout', function(event) {
    		        event.preventDefault();
    		        $(this).closest('li').removeClass('open');
    		    });
    	    });
    		// 點選重點標籤後，影片(currentTime)跳至該位置beginTime
    		$(".kl").click(function()
			{
				video.currentTime=keyLabelArray[parseInt(this.getAttribute("id").split("-")[1])].beginTime;
			})
    	}, // end success
		error:function(){alert('failed');}
	});	// end ajax

//----------------------------------------------keyLabel----------------------------------------------//    
});
