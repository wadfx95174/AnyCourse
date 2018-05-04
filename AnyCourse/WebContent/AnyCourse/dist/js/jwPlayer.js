var video;
//var keyLabels = [];
var keyLabelArray;
var maxIndex = 0;
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
		url:'http://localhost:8080/AnyCourse/PlayerInterfaceServlet.do',
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
    	url: 'http://localhost:8080/AnyCourse/PlayerInterfaceServlet.do',
    	method: 'GET',
    	cache :false,
    	data: {
    		"method": 'getVideo',
    		"unitId": get('unit_id')
    	},
    	error: function(){
    	},
    	success: function(response){
    		$('#vid').append('<video controls="" name="media" id = "myvideo" ><source src="'+response.videoUrl+'" type="video/mp4"></video>');
    		
    		$('h3')[0].append(response.unitName);
    	    video=$("#myvideo")[0];
//    	    console.log(response.personalLike);
//    	    if(response.personalLike == 0){$('#likesIcon').addClass('fa-heart-o');}
//    	    else if(reponse.personalLike == 1){$('#likesIcon').addClass('fa-heart');}
    	    $('#likesNum').text(response.likes);
    	    $('#introduction').append(response.courseInfo);
    	}
    });
    
	// 按下添加標籤按鈕，隱藏按鈕並顯示slider
	$("#addKeyLabel").click(function(){
	    // 設置slider的最大時間
	    $( "#slider-range" ).slider( "option", "max", Math.floor(video["duration"]));
		$( "#slider-range" ).slider( "values", [ 0,  Math.floor(video["duration"])]);


	    // 調整slider會跳至影片該處
	    $( "#slider-range" ).slider({
	  	    stop: function( event, ui ) {
	  		    video.currentTime = (Math.floor(ui.value));
		    }
	    });
    });
    
	var jwPlayerActive = false;
	var videoStateTexts = [  
        'HAVE_NOTHING','HAVE_METADATA','HAVE_CURRENT_DATA',  
        'HAVE_FUTURE_DATA','HAVE_ENOUGH_DATA'  
    ]; 
	var pr = ['currentTime', 'duration', 'readyState','paused','muted','volume','ended'];  
	var statelist = $('#statelist');
	
    var $progress = $('.progress'),
      $duration = $('.duration'),
      $currentTime = $('.current-time');
	
//    $("#play").click(function(){  
//        video.play(); //播放影片  
//		
//    });  
//  
//    $("#pause").click(function(){  
//        video.pause(); //停止播放  
//    }); 
    
	var t = setInterval(function(){  	//每100毫秒執行一次
		if (video)
		{
			var now=(video["currentTime"]/video["duration"] * 100);	//percent
			$('.meter').css('width', now + '%');
		}
    },500);  
	
	
//----------------------------------------------video----------------------------------------------//
	

//----------------------------------------------keyLabel----------------------------------------------//    
	//取得資料庫的資料    
	

    // On progress click, seek to a position.
	// 點選progress, seek to 該位置
    $progress.on('click', function(e){
      var percent = ((e.pageX-$progress.offset().left)/$progress.width());
      var seek = percent*video["duration"];
      $('.meter').css('width', percent*100 + '%');
      video.currentTime = seek;
    });

    // Set up the expand animation.
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

    // 設置個人重點標籤
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
    	video.currentTime = beginTime;
    	$('.keyLabelDiv').css('margin-left', (beginTime / video['duration'] * 100) + '%');
    	$('.keyLabelDiv').css('width', ((endTime - beginTime) / video['duration'] * 100) + '%');
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
    		cache :false,
    	    data : {
    	    	"method" : "delete",
    	    	"keyLabelId" : keyLabelArray[selectId].keyLabelId
    		},
    		success:function(result){
    			element.remove();
        	},
    		error:function(){console.log('failed');}
    	});
    });
    // 新增重點標籤
    $(document).on('click', '#submitKL', function(event)
    {
    	var klName = $('#keyLabelName').val();
    	var klBeginTime = $( "#slider-range" ).slider( "values", 0 );
    	var klEndTime = $( "#slider-range" ).slider( "values", 1 );
    	if (klName != "")
    	{
    		$.ajax({
        		url : 'http://localhost:8080/AnyCourse/KeyLabelServlet.do',
        		method : 'POST',
        	    data : {
        	    	"method" : "insert",
        	    	"keyLabelName" : klName,
        	    	"beginTime" : klBeginTime,
        	    	"endTime" : klEndTime,
        	    	"unitId" : get("unit_id")
        		},
        		dataType : 'json',
        		cache: false,
        		success:function(result){
        			keyLabelArray[maxIndex] = result;
            		addToSelfKeyLabel(maxIndex++);
            		//alert('新增成功');

                	video.currentTime = klBeginTime;
                	$('.keyLabelDiv').css('margin-left', (klBeginTime / video['duration'] * 100) + '%');
                	$('.keyLabelDiv').css('width', ((klEndTime - klBeginTime) / video['duration'] * 100) + '%');
                	$('.keyLabelDiv').attr('data-original-title', klName);
            	},
        		error:function(){console.log('failed');}
        	});
    	}
    	else
    	{
    		alert("標籤名稱不可為空!!");
    	}
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
    		error:function(){console.log('failed');}
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
//    		keyLabelArray[maxIndex] = keyLabelArray[selectId];
//    		keyLabelArray[maxIndex].keyLabelId = ??;
    	//*
    	
    	$.ajax({
    		url : 'http://localhost:8080/AnyCourse/KeyLabelServlet.do',
    		method : 'POST',
    		cache :false,
    	    data : {
    	    	"method" : "insert",
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
    		error:function(){console.log('failed');}
    	});
    });
    
    $.ajax({
		url : 'http://localhost:8080/AnyCourse/KeyLabelServlet.do',
		method : 'GET', 
		cache :false,
		data : {
			"method" : "getPKL",
			"unit_id" : get("unit_id")
		},
		success:function(result){
			keyLabelArray = result;
    		for(maxIndex = 0 ;maxIndex < result.length; maxIndex++){
    			addToSelfKeyLabel(maxIndex);
			} // end for
    		
    	}, // end success
		error:function(){console.log('getPKL fail');}
	});	// end ajax
    
    $.ajax({
		url : 'http://localhost:8080/AnyCourse/ExchangeKeyLabelServlet.do',
		method : 'GET', 
		cache :false,
		data : {					
			"unit_id" : get("unit_id")
		},
		success:function(result){
			//alert("OK");
			keyLabelArray = result;
    		for(maxIndex = 0 ;maxIndex < result.length; maxIndex++){
    			$('#exchange_keylabel').append(
    					'<div id="exK_' + keyLabelArray[maxIndex].userId + '" class=" col-xs-12">'+
    					'<img src="https://ppt.cc/fxYEnx@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
    					'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + keyLabelArray[maxIndex].nick_name + '</h4>'+
    					'<li class="list-group-item">'+ keyLabelArray[maxIndex].keyLabelName+
    					'<ul class="list-group-submenu">'+
    					'<a href="#" class = "ukl exchange" id = "exchange-ukl-' + maxIndex + '" style="color: #FFF"><li class="list-group-submenu-item lightBlue">使用</li></a>'+
    					'</ul>'+
    					'</li>'+
    					'</div>'
    					);
			} // end for
    		
    		// 點選交流區的重點標籤，暫存區出現
    		$('.list-group-submenu').on('click', '.exchange', function(event) 
			{
				selectId = parseInt(this.getAttribute("id").split("-")[2]);
				addToTempKeyLabel(selectId);
			})
    	}, // end success
		error:function(){console.log('getEKL fail');}
	});	// end ajax

//----------------------------------------------keyLabel----------------------------------------------//    
//---------------------------抓影片結束時間，並儲存----------------------------------------------//
//---------------------------要設perconal_plan跟watch_record兩個table-------------------------//
    window.onbeforeunload = function(event) { 
        console.log(video["currentTime"]);
        console.log(video["duration"]);
        console.log(get("unit_id"));
        
        $.ajax({
        	url:'http://localhost:8080/AnyCourse/PlayerInterfaceServlet.do',
        	method: 'POST',
        	data:{
        		"action": 'setVideoCloseTime',//代表要設定關閉頁面的時間
        		"currentTime":Math.floor(video["currentTime"]),//關閉的時間
        		"duration":Math.floor(video["duration"]),//影片總共有多長時間
        		"unitId": get("unit_id")
        	},
        	error: function(){
        		console.log("setVideoEndTime failed!");
        	}
        })
    }; 
//---------------------------抓影片結束時間，並儲存----------------------------------------------//
});
