var video;
$(function(){
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
   			var keyLabels = [];
   			for (var i = 1; i <= 8; i++)
   			{
   				keyLabels[i] = {"time": i};
   			}
			$(".kl").click(function()
			{
				video.currentTime=keyLabels[parseInt(this.getAttribute("id").split("-")[1])].time;
			})
});
