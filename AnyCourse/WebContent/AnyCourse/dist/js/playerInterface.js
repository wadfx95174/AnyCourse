
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
var maxIndex = 0;

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
    video=$("#myvideo")[0];
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
				+'<a href="#" class = "self dkl" id = "self-dkl-' + index + '" style="color: #FFF"><li class="list-group-submenu-item">刪除</li></a>'
				+'<a href="#" class = "self ekl" id = "self-ekl-' + index + '" style="color: #FFF"><li class="list-group-submenu-item primary">編輯</li></a>'
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
    	video.currentTime=keyLabelArray[parseInt(this.getAttribute("id").split("-")[2])].beginTime;
    })
    // 點擊個人標籤刪除按鈕，送去資料庫刪除    --------------------*未完成
    $(document).on('click', '.self.dkl', function(event) 
    {
    	var element = $(this).parent().parent();
    	var index = parseInt(this.getAttribute("id").split("-")[2]);
    	$.ajax({
    		url : 'http://localhost:8080/AnyCourse/KeyLabelServlet.do',
    		method : 'POST',
    	    data : {
    	    	"method" : "delete",
//    		    	"user_id" : 1,  //id 1要改成session的id
    	    	"keyLabelId" : keyLabelArray[index].keyLabelId
    		},
    		success:function(result){
    			element.remove();
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
    	var index = parseInt(this.getAttribute("id").split("-")[2]);
    	
    	//*
//    		keyLabelArray[maxIndex] = keyLabelArray[index];
//    		keyLabelArray[maxIndex].keyLabelId = ??;
    	//*
    	
    	$.ajax({
    		url : 'http://localhost:8080/AnyCourse/KeyLabelServlet.do',
    		method : 'POST',
    	    data : {
    	    	"method" : "insert",
    	    	"userId" : 1,  //id 1要改成session的id
    	    	"keyLabelName" : keyLabelArray[index].keyLabelName,
    	    	"beginTime" : keyLabelArray[index].beginTime,
    	    	"endTime" : keyLabelArray[index].endTime,
    	    	"unitId" : keyLabelArray[index].unitId
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
						var index = parseInt(this.getAttribute("id").split("-")[2]);
						addToTempKeyLabel(index);
					})
    	}, // end success
		error:function(){alert('failed');}
	});	// end ajax

//----------------------------------------------keyLabel----------------------------------------------//    
});

