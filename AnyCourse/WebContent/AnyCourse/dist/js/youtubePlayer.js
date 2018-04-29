var youTubePlayer;
var uid;

$(document).ready(function(){
	function get(name)
	{
	   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
	      return decodeURIComponent(name[1]);
	}
	$.ajax({
		url: 'http://localhost:8080/AnyCourse/PlayerInterfaceServlet.do',
		method: 'GET',
		data: {
			"method": 'getVideo',
			"unitId": get('unit_id')
		},
		error: function(){
		},
		success: function(response){
			uid = response.videoUrl.split('/')[4];
			youTubePlayer.cueVideoById({
                videoId: uid
               });
			$('h3')[0].append(response.unitName);
	//		$('#introduction').append(response.)
//		    video=$("#myvideo")[0];
		}
	});
  //  初始化YoutubePlayer
	
	
	var $progress = $('.progress'),
    $duration = $('.duration'),
    $currentTime = $('.current-time');
	
	// 點選progress, seek to 該位置
    $progress.on('click', function(e){
      var percent = ((e.pageX-$progress.offset().left)/$progress.width());
      var seek = percent*youTubePlayer.getDuration();;
      $('.meter').css('width', percent*100 + '%');
      changeTo(seek);
    });
	
	var animateUp = function(){
	      $(this).animate({
	        height:5,
	        duration:10
	      }, function(){
	        $progress.removeClass('open');
	        $progress.one('mouseenter', animateDown);
	      });
	    };

	    var animateDown = function(){
	      $(this).animate({
	        height:20,
	        duration:10
	      }, function(){
	        $progress.addClass('open');
	        $progress.one('mouseleave', animateUp);
	      });
	    };
	    $progress.one('mouseenter', animateDown);
	    
	    function addToSelfKeyLabel(index)
	    {
	    	$('#keyLabel1').append('<li class="list-group-item">'
					+ keyLabelArray[index].keyLabelName
					+'<ul class="list-group-submenu">'
					+'<a href="#" class = "self dkl" id = "self-dkl-' + index + '" style="color: #FFF" data-toggle="modal" data-target="#klDeleteModal"><li class="list-group-submenu-item">刪除</li></a>'
					+'<a href="#" class = "self ekl" id = "self-ekl-' + index + '" style="color: #FFF" data-toggle="modal" data-target="#klEditModal"><li class="list-group-submenu-item primary">編輯</li></a>'
					+'<a href="#" class = "self skl" id = "self-skl-' + index + '" style="color: #FFF"><li class="list-group-submenu-item info">分享</li></a>'
					+'<a href="#" class = "self ukl" id = "self-ukl-' + index + '" style="color: #FFF"><li class="list-group-submenu-item lightBlue">使用</li></a>'
					+'</ul>'
					+'</li>');
	    }

	    // 設置暫存重點標籤
	    function addToTempKeyLabel(index)
	    {
			$('#keyLabel2').append('<li class="list-group-item">'
					+ keyLabelArray[index].keyLabelName
					+'<ul class="list-group-submenu">'
					+'<a href="javascript:void(0)" class = "temp dkl" id = "temp-dkl-' + index + '" style="color: #FFF"><li class="list-group-submenu-item">刪除</li></a>'
					+'<a href="javascript:void(0)" class = "temp akl" id = "temp-akl-' + index + '" style="color: #FFF"><li class="list-group-submenu-item primary">添加</li></a>'
					+'<a href="#" class = "temp ukl" id = "temp-ukl-' + index + '" style="color: #FFF"><li class="list-group-submenu-item lightBlue">使用</li></a>'
					+'</ul>'
					+'</li>');
	    }
	  //重點標籤 從右滑出
	    $(document).on('mouseover', '.list-group-item', function(event) {
	        event.preventDefault();
	        $(this).closest('li').addClass('open');
	    });

	    $(document).on('mouseout', '.list-group-item', function(event) {
	        event.preventDefault();
	        $(this).closest('li').removeClass('open');
	    });
	 // 設定重點標籤的事件
	    // 點擊重點標籤後，影片(currentTime)跳至該位置beginTime
	    $(document).on('click', '.ukl', function(event) 
	    {
	    	selectId = parseInt(this.getAttribute("id").split("-")[2]);
	    	var beginTime = keyLabelArray[selectId].beginTime;
	    	var endTime = keyLabelArray[selectId].endTime;
	    	changeTo(beginTime);
	    	$('.keyLabelDiv').css('margin-left', (beginTime / youTubePlayer.getDuration() * 100) + '%');
	    	$('.keyLabelDiv').css('width', ((endTime - beginTime) / youTubePlayer.getDuration() * 100) + '%');
	    	$('.keyLabelDiv').attr('data-original-title', keyLabelArray[selectId].keyLabelName);
	    });
	    // 點擊個人標籤刪除按鈕，設置在變數
	    $(document).on('click', '.self.dkl', function(event) 
	    {
	    	element = $(this).parent().parent();
	    	selectId = parseInt(this.getAttribute("id").split("-")[2]);
	    });
	    // 送去資料庫刪除 
	    $(document).on('click', '#deleteKlButton', function(event)
	    {
	    	$.ajax({
	    		url : 'http://localhost:8080/AnyCourse/KeyLabelServlet.do',
	    		method : 'POST',
	    	    data : {
	    	    	"method" : "delete",
//	    		    	"user_id" : 1,  //id 1要改成session的id
	    	    	"keyLabelId" : keyLabelArray[selectId].keyLabelId
	    		},
	    		success:function(result){
	    			element.remove();
	        	},
	    		error:function(){alert('failed');}
	    	});
	    });
	    // 新增重點標籤
	    $(document).on('click', '#createKlButton', function(event)
	    {
	    	var klName = $('#cKeyLabelName').val();
	    	var klBeginTime = parseInt($('#cBeginTime').val());
	    	var klEndTime = parseInt($('#cEndTime').val());
	    	
	    	$.ajax({
	    		url : 'http://localhost:8080/AnyCourse/KeyLabelServlet.do',
	    		method : 'POST',
	    	    data : {
	    	    	"method" : "insert",
	    	    	"userId" : 1,  //id 1要改成session的id
	    	    	"keyLabelName" : klName,
	    	    	"beginTime" : klBeginTime,
	    	    	"endTime" : klEndTime,
	    	    	"unitId" : 1   // id 要改
	    		},
	    		dataType : 'json',
	    		cache: false,
	    		success:function(result){
	    			keyLabelArray[maxIndex] = result;
	        		addToSelfKeyLabel(maxIndex++);
	        	},
	    		error:function(){alert('failed');}
	    	});
	    })
	    // 點擊暫存標籤刪除按鈕，消除該標籤
	    $(document).on('click', '.temp.dkl', function(event) 
	    {
	    	$(this).parent().parent().remove();
	    });
	    // 點擊個人標籤編輯按鈕，可編輯名稱
	    $(document).on('click', '.ekl', function(event) 
	    {
	    	element = $(this).parent().parent();
	    	selectId = parseInt(this.getAttribute("id").split("-")[2]);
	    	$('#eKeyLabelName').val(keyLabelArray[selectId].keyLabelName);
	    	$('#eBeginTime').val(keyLabelArray[selectId].beginTime);
	    	$('#eEndTime').val(keyLabelArray[selectId].endTime);
	    });
	    
	    // 送去資料庫更新
	    $(document).on('click', '#editKlButton', function(event)
	    {
	    	var klName = $('#eKeyLabelName').val();
	    	var klBeginTime = parseInt($('#eBeginTime').val());
	    	var klEndTime = parseInt($('#eEndTime').val());
	    	
	    	
	    	$.ajax({
	    		url : 'http://localhost:8080/AnyCourse/KeyLabelServlet.do',
	    		method : 'POST',
	    	    data : {
	    	    	"method" : "update",
	    	    	"keyLabelName" : klName,
	    	    	"beginTime" : klBeginTime,
	    	    	"endTime" : klEndTime,
	    	    	"keyLabelId" : keyLabelArray[selectId].keyLabelId
	    		},
	    		cache: false,
	    		success:function(result){
	    			keyLabelArray[selectId].keyLabelName = klName;
	    			keyLabelArray[selectId].beginTime = klBeginTime
	    			keyLabelArray[selectId].endTime = klEndTime
	    	    	$('.keyLabelDiv').css('margin-left', (klBeginTime / video['duration'] * 100) + '%');
	    	    	$('.keyLabelDiv').css('width', ((klEndTime - klBeginTime) / video['duration'] * 100) + '%');
	        	},
	    		error:function(){alert('failed');}
	    	});
	    })
	    
	    // 點擊添加按鈕，新增至個人重點標籤，上傳資料庫      *要改為資料庫傳回正確值
	    $(document).on('click', '.akl', function(event) 
	    {
	    	//	method 1: 移動
	    	//$('#keyLabel1').append($(this).parent().parent().detach());
	    	//	method 2: 添加
	    	element = $(this).parent().parent();
	    	selectId = parseInt(this.getAttribute("id").split("-")[2]);
	    	
	    	//*
//	    		keyLabelArray[maxIndex] = keyLabelArray[selectId];
//	    		keyLabelArray[maxIndex].keyLabelId = ??;
	    	//*
	    	
	    	$.ajax({
	    		url : 'http://localhost:8080/AnyCourse/KeyLabelServlet.do',
	    		method : 'POST',
	    	    data : {
	    	    	"method" : "insert",
	    	    	"userId" : 1,  //id 1要改成session的id
	    	    	"keyLabelName" : keyLabelArray[selectId].keyLabelName,
	    	    	"beginTime" : keyLabelArray[selectId].beginTime,
	    	    	"endTime" : keyLabelArray[selectId].endTime,
	    	    	"unitId" : keyLabelArray[selectId].unitId
	    		},
	    		dataType : 'json',
	    		cache: false,
	    		success:function(result){
	    			keyLabelArray[maxIndex] = result;
	        		addToSelfKeyLabel(maxIndex++);
	            	element.remove();
	        	},
	    		error:function(){alert('failed');}
	    	});
	    });
	    
	    $.ajax({
			url : 'http://localhost:8080/AnyCourse/KeyLabelServlet.do',
			method : 'GET', 
			data : {
				"unit_id" : 1		//unitid
			},
			success:function(result){
				keyLabelArray = result;
	    		for(maxIndex = 0 ;maxIndex < result.length; maxIndex++){
	    			// 設置個人標籤
	    			if (keyLabelArray[maxIndex].userId == 1)	//id 1要改成session的id
					{
	    				addToSelfKeyLabel(maxIndex);
					}
	    			// 設置交流區標籤
	    			else if (keyLabelArray[maxIndex].share == 1)
					{
	    				$('#exchange').append('<li class="list-group-item">'
	    						+ keyLabelArray[maxIndex].keyLabelName
	    						+'<ul class="list-group-submenu">'
	    						+'<a href="#" class = "ukl exchange" id = "exchange-ukl-' + maxIndex + '" style="color: #FFF"><li class="list-group-submenu-item lightBlue">使用</li></a>'
	    						+'</ul>'
	    						+'</li>');
	    				
					}
				} // end for
	    		
	    		// 點選交流區的重點標籤，暫存區出現
	    		$('.list-group-submenu').on('click', '.exchange', function(event) 
						{
	    					selectId = parseInt(this.getAttribute("id").split("-")[2]);
							addToTempKeyLabel(selectId);
						})
	    	}, // end success
			error:function(){alert('failed');}
		});	// end ajax
})

//--------------------------youtube iframe api-----------------------------
function onYouTubeIframeAPIReady() {
    youTubePlayer = new YT.Player('youTubePlayer', {
      height: '390',
      width: '640',
      videoId: uid,     //影片ID
      events: {                   //哪些狀態執行哪些func
        'onReady': onPlayerReady,   //ready後會執行 onPlayerReady func
      }

    });
    youTubePlayer.personalPlayer = {'currentTimeSliding': false,  //初始化參數，滑動bar會用到
                                    'errors': []};
  }
  //  ready後用到的func
  function onPlayerReady(event) {   
    youTubePlayer.setPlaybackRate(1);   //影片速率
    event.target.playVideo();   //  播放
  }


  // var done = false;
  // function onPlayerStateChange(event) {
  //   if (event.data == YT.PlayerState.PLAYING && !done) {
  //     setTimeout(stopVideo, 6000);
  //     done = true;
  //   }
  // }
  
  function stopVideo() {
    youTubePlayer.stopVideo();
  }

  //  跳轉到幾秒
  function changeTo(sec)
  {
    youTubePlayer.seekTo(sec, true);
  }
  
  function changeId(videoid)
  {
	  youTubePlayer.loadVideoById("videoid", 0, "default");
  }

  //  timechange會用到
  function youTubePlayerActive() {
    'use strict';
    return youTubePlayer && youTubePlayer.hasOwnProperty('getPlayerState');
  }

  //  移動完後影片跳轉到該位置
  function youTubePlayerCurrentTimeChange(currentTime) {
    'use strict';

    youTubePlayer.personalPlayer.currentTimeSliding = false;
    if (youTubePlayerActive()) {
        youTubePlayer.seekTo(currentTime*youTubePlayer.getDuration()/100, true);
    }
  }

  //  移動中
  function youTubePlayerCurrentTimeSlide() {
    'use strict';

    youTubePlayer.personalPlayer.currentTimeSliding = true;
  }

  function youTubePlayerDisplay() {
    'use strict';

    if (youTubePlayerActive()) {
        var state = youTubePlayer.getPlayerState();

        var current = youTubePlayer.getCurrentTime();
        var duration = youTubePlayer.getDuration();
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
        if (!youTubePlayer.personalPlayer.currentTimeSliding) {
        	$('.meter').css('width', currentPercent + '%');
        }
    }
  }
  (function () {
    'use strict';

    function init() {
        // Load YouTube library
        var tag = document.createElement('script');

        tag.src = 'https://www.youtube.com/iframe_api';

        var first_script_tag = document.getElementsByTagName('script')[0];

        first_script_tag.parentNode.insertBefore(tag, first_script_tag);


        // Set timer to display infos
        setInterval(youTubePlayerDisplay, 100);   // 每秒(100毫秒)執行funct 一次
    }


    if (window.addEventListener) {
        window.addEventListener('load', init);
    } else if (window.attachEvent) {
        window.attachEvent('onload', init);
    }
  }());
	