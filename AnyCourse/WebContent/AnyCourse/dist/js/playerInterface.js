//var ajaxURL="http://140.121.197.130:8400/";
var ajaxURL="http://localhost:8080/";
var video;		// 播放器
var keyLabelArray;		// 存放個人標籤
var exKeyLabelArray;	// 存放交流標籤
var maxIndex = 0;		// 標籤的Index
var selectId = 0;		// 暫存已選擇的項目
var element;			// 存DOM元素
var shareState = ['分享','收回'];		// 分享的狀態

var $progress = $('.progress'),
$duration = $('.duration'),
$currentTime = $('.current-time');

$('#noteArea').slimScroll({
    height: '200px'
  });
$('.tab-content').slimScroll({
    height: '500px'
  });
$('#keyLabel1').slimScroll({
    height: '130px'
  });
$('#keyLabel2').slimScroll({
    height: '130px'
  });

// 取得url的參數
function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
       return decodeURIComponent(name[1]);
}

// 將秒數設為固定格式 (hh:mm:ss)
function formatTime(seconds) 
{
    return [
        parseInt(seconds / 60 / 60),
        parseInt(seconds / 60 % 60),
        parseInt(seconds % 60)
    ]
        .join(":")
        .replace(/\b(\d)\b/g, "0$1");
}

$(document).ready(function(){
    checkLogin("", "../../");
    
    
    $( "#slider-range" ).slider({
        range: true,
        min: 0,
        max: 0,
        values: [ 0, 0 ],
        slide: function( event, ui ) {
            $( "#bVideoTime" ).val( formatTime(ui.values[ 0 ]) );
            $( "#eVideoTime" ).val( formatTime(ui.values[ 1 ]) );
          }
        });
    $( "#bVideoTime" ).val( $( "#slider-range" ).slider( "values", 0 ) );
    $( "#eVideoTime" ).val( $( "#slider-range" ).slider( "values", 1 ) );
    
    $("#addKeyLabel,#submitKL,#cancelKL").click(function(){
    	$("#slider").toggle();
    	$("#addKeyLabel").toggle();
    });
    
    
    $("#editNote").click(function(){
    	if(typeof($("#textArea").attr("disabled")) === "string"){
    		$("#textArea").removeAttr("disabled");
    		$("#noteFooter").slideToggle();
    	}
    	else{
    		$("#textArea").attr("disabled","false");
    		$("#noteFooter").slideToggle();
    	}   	
    });
    $("#cancelNote").click(function(){
    	$("#textArea").attr("disabled","false");
        $("#noteFooter").slideToggle();
    });
    $( function() {
	    $( "#keyLabel1, #keyLabel2" ).sortable({
	    }).disableSelection();
    });

//----------------------------------------------video----------------------------------------------//
    if (get('listId') != undefined)
    {
    	$.ajax({
        	url: ajaxURL+'AnyCourse/PlayerInterfaceServlet.do',
        	method: 'POST',
        	cache :false,
        	data: {
        		courselistId: get('listId'),
        		action: 'getVideoList',//代表要取videoList
        	},
        	success: function(result){
//        		console.log(result);
        		
        	    $('#list').attr('class',  'box box-primary');
        	    $('#list').append('<div class="box-header ui-sortable-handle">'
        	                    + '<i class="ion ion-clipboard"></i>'
        	                    + '<h3 class="box-title"><strong>'+result[0].listName+'</strong></h3>'
        	                    + '</div>'
        	                    + '<div class="box-body">'
        	                    + '   <div class="list-group"  id="listbox">');
        	    for (var i = 0; i < result.length; i++)
        	    {
        	    	$('#listbox').append('      <a href="PlayerInterface.html?type='+ (result[i].videoUrl.split("/")[2]=='www.youtube.com'?1:2) + '&unitId='+result[i].unitId+'&listId='+get('listId')+'" class="list-group-item">'
		    	                    + '         <div class="media">'
		    	                    + '            <div class="col-xs-6 pull-left" style="padding-left: 0px;">'
		    	                    + '               <div class="embed-responsive embed-responsive-16by9">          '
		    	                    + '                   <img id="img" class="style-scope yt-img-shadow" alt="" width="210" src="'+result[i].videoImgSrc+'">'        
		    	                    + '               </div>'
		    	                    + '            </div>'
		    	                    + '            <div class="media-body">'
		    	                    + '               <h3 class="media-heading">'+result[i].unitName+'</h3>'
		    	                    + '               <p>讚數:'+result[i].likes+'</p>'
		    	                    + '            </div>' 
		    	                    + '         </div>'
		    	                    + '      </a>');
        	    }
        	    $('#list').append('   </div>'                           
        	                    + '</div>'); 
        	    $('#listbox').slimScroll({
        	        height: '350px'
        	      });
        	    $('#recommend').slimScroll({
        	        height: '350px'
        	      });
        	},
        	error: function(){
        		console.log("post fail");
        	}
        });
    }
    else {
    	$('#recommend').slimScroll({
    	    height: '600px'
    	  });
    }
    
    // 判斷要執行哪個js
    var oHead = document.getElementsByTagName('HEAD').item(0); 
    var oScript= document.createElement("script"); 
    oScript.type = "text/javascript"; 
    oScript.src= (get('type') == "1") ? "../dist/js/youtubePlayer.js" : "../dist/js/jwPlayer.js"; 
    oHead.appendChild(oScript); 
    
//----------------------------------------------keyLabel----------------------------------------------//   
    // 取資料庫個人標籤
    $.ajax({
		url : ajaxURL+'AnyCourse/KeyLabelServlet.do',
		method : 'GET', 
		cache :false,
		data : 
		{
			"method" : "getPKL",
			"unitId" : get("unitId")
		},
		success:function(result){
			keyLabelArray = result;
    		for(maxIndex = 0 ;maxIndex < result.length; maxIndex++)
    		{
    			addToSelfKeyLabel(maxIndex);
			}
    	},
		error:function(){console.log('getPKL fail');}
	});	// end ajax
    
    // 取資料庫交流標籤
    $.ajax({
		url : ajaxURL+'AnyCourse/ExchangeKeyLabelServlet.do',
		method : 'GET', 
		cache :false,
		data : {"unitId" : get("unitId")},
		success:function(result){
			exKeyLabelArray = result;
    		for(exmaxIndex = 0 ;exmaxIndex < result.length; exmaxIndex++)
    		{
    			$('#exchangeKeylabel').append(
    					'<div id="exK_' + exKeyLabelArray[exmaxIndex].userId + '" class=" col-xs-12">'+
    					'<img src="https://ppt.cc/fxYEnx@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
    					'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + exKeyLabelArray[exmaxIndex].nickName + '</h4>'+
    					'<li class="list-group-item">'+ exKeyLabelArray[exmaxIndex].keyLabelName+
    					'<ul class="list-group-submenu">'+
    					'<a href="#" class = "ukl exchange" id = "exchange-ukl-' + exmaxIndex + '" style="color: #FFF"><li class="list-group-submenu-item lightBlue">使用</li></a>'+
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
		error:function(){console.log('get ExangeKeyLabel failed');}
	});	// end ajax
    
    
    // 設置個人重點標籤
    function addToSelfKeyLabel(index)
    {
    	var share = keyLabelArray[index].share ? '<a href="#" class = "self skl" id = "self-skl-' + index + '" style="color: #FFF"><li class="list-group-submenu-item muted">收回</li></a>' : '<a href="#" class = "self skl" id = "self-skl-' + index + '" style="color: #FFF"><li class="list-group-submenu-item info">分享</li></a>'

    	$('#keyLabel1').append('<li class="list-group-item">'
				+ keyLabelArray[index].keyLabelName
				+'<ul class="list-group-submenu">'
				+'<a href="#" class = "self dkl" id = "self-dkl-' + index + '" style="color: #FFF" data-toggle="modal" data-target="#klDeleteModal"><li class="list-group-submenu-item danger">刪除</li></a>'
				+'<a href="#" class = "self ekl" id = "self-ekl-' + index + '" style="color: #FFF"><li class="list-group-submenu-item primary">編輯</li></a>'
				+ share
				+'<a href="#" class = "self ukl" id = "self-ukl-' + index + '" style="color: #FFF"><li class="list-group-submenu-item lightBlue">使用</li></a>'
				+'</ul>'
				+'</li>');
    }

    // 設置暫存重點標籤
    function addToTempKeyLabel(index)
    {
		$('#keyLabel2').append('<li class="list-group-item">'
				+ exKeyLabelArray[index].keyLabelName
				+'<ul class="list-group-submenu">'
				+'<a href="javascript:void(0)" class = "temp dkl" id = "temp-dkl-' + index + '" style="color: #FFF"><li class="list-group-submenu-item danger">刪除</li></a>'
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
 
//----------------------------------------------keyLabel event----------------------------------------------//   
    
    // 點擊重點標籤後，影片(currentTime)跳至該位置beginTime
    $(document).on('click', '.self.ukl', function(event) 
    {
    	selectId = parseInt(this.getAttribute("id").split("-")[2]);
    	var beginTime = keyLabelArray[selectId].beginTime;
    	var endTime = keyLabelArray[selectId].endTime;
    	seekTo(beginTime);
    	$('.keyLabelDiv').css('margin-left', (beginTime / getDuration() * 100) + '%');
    	$('.keyLabelDiv').css('width', ((endTime - beginTime) / getDuration() * 100) + '%');
    	$('.keyLabelDiv').attr('data-original-title', keyLabelArray[selectId].keyLabelName);
    });
    
    // 點擊重點標籤後，影片(currentTime)跳至該位置beginTime
    $(document).on('click', '.temp.ukl ,.exchange', function(event) 
    {
    	selectId = parseInt(this.getAttribute("id").split("-")[2]);
    	var beginTime = exKeyLabelArray[selectId].beginTime;
    	var endTime = exKeyLabelArray[selectId].endTime;
    	seekTo(beginTime);
    	$('.keyLabelDiv').css('margin-left', (beginTime / getDuration() * 100) + '%');
    	$('.keyLabelDiv').css('width', ((endTime - beginTime) / getDuration() * 100) + '%');
    	$('.keyLabelDiv').attr('data-original-title', exKeyLabelArray[selectId].keyLabelName);
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
    		url : ajaxURL+'AnyCourse/KeyLabelServlet.do',
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
        		url : ajaxURL+'AnyCourse/KeyLabelServlet.do',
        		method : 'POST',
        		cache :false,
        	    data : {
        	    	"method" : "insert",
        	    	"keyLabelName" : klName,
        	    	"beginTime" : klBeginTime,
        	    	"endTime" : klEndTime,
        	    	"unitId" : get("unitId")
        		},
        		dataType : 'json',
        		cache: false,
        		success:function(result){
        			keyLabelArray[maxIndex] = result;
            		addToSelfKeyLabel(maxIndex++);
            		console.log('新增成功');

        	    	seekTo(klBeginTime);
        	    	$('.keyLabelDiv').css('margin-left', (klBeginTime / getDuration() * 100) + '%');
        	    	$('.keyLabelDiv').css('width', ((klEndTime - klBeginTime) / getDuration() * 100) + '%');
        	    	$('.keyLabelDiv').attr('data-original-title', klName);
            	},
        		error:function(){console.log('failed');}
        	});
    	}
    	else
    	{
    		console.log("標籤名稱不可為空!!");
    	}
    })
    
    // 點擊暫存標籤刪除按鈕，消除該標籤
    $(document).on('click', '.temp.dkl', function(event) 
    {
    	$(this).parent().parent().remove();
    });
    
    // 點擊個人標籤分享按鈕
    $(document).on('click', '.skl', function(event) 
    {
    	var ele = $(this).find('li');	// 存該重點標籤的物件
    	element = $(this).parent().parent();
    	selectId = parseInt(this.getAttribute("id").split("-")[2]);
    	$.ajax({
    		url : ajaxURL+'AnyCourse/KeyLabelServlet.do',
    		method : 'POST',
    	    data : {
    	    	"method" : "share",
    	    	"keyLabelId" : keyLabelArray[selectId].keyLabelId,
    	    	"share" : (keyLabelArray[selectId].share + 1) % 2
    		},
    		cache: false,
    		success:function(){
    			keyLabelArray[selectId].share = (keyLabelArray[selectId].share + 1) % 2
    			ele.toggleClass('muted info');
    			ele.text(shareState[keyLabelArray[selectId].share]);
        	},
    		error:function(){console.log('share failed');}
    	});
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
    		url : ajaxURL+'AnyCourse/KeyLabelServlet.do',
    		method : 'POST',
    		cache :false,
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
    	    	$('.keyLabelDiv').css('margin-left', (klBeginTime / getDuration() * 100) + '%');
    	    	$('.keyLabelDiv').css('width', ((klEndTime - klBeginTime) / getDuration() * 100) + '%');
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
//	    		keyLabelArray[maxIndex] = keyLabelArray[selectId];
//	    		keyLabelArray[maxIndex].keyLabelId = ??;
    	//*
    	
    	$.ajax({
    		url : ajaxURL+'AnyCourse/KeyLabelServlet.do',
    		method : 'POST',
    		cache :false,
    	    data : {
    	    	"method" : "insert",
    	    	"keyLabelName" : exKeyLabelArray[selectId].keyLabelName,
    	    	"beginTime" : exKeyLabelArray[selectId].beginTime,
    	    	"endTime" : exKeyLabelArray[selectId].endTime,
    	    	"unitId" : exKeyLabelArray[selectId].unitId
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
    

    // 按下添加標籤按鈕，隱藏按鈕並顯示slider
    $("#addKeyLabel").click(function(){
    	
  	    // 設置slider的最大時間
  	    $( "#slider-range" ).slider( "option", "max", getDuration());
  	    $( "#slider-range" ).slider( "values", [ 0, getDuration() ] );
  		
  	    // 調整slider會跳至影片該處
  	    $( "#slider-range" ).slider({
  		    stop: function( event, ui ) {
  			    seekTo(Math.floor(ui.value));
  		    }
  	    });
    });
 //----------------------------------------------進度條----------------------------------------------// 
    
    // 點選progress, seek to 該位置
    $progress.on('click', function(e){
        var percent = ((e.pageX-$progress.offset().left)/$progress.width());
        var seek = percent*getDuration();
        $('.meter').css('width', percent*100 + '%');
        seekTo(seek);
    });
    
    // 滑鼠移開進度條回復原狀
	var animateUp = function(){
        $(this).animate({
            height:5,		// 高度
            duration:10		// 寬度
        }, function(){
            $progress.removeClass('open');
            $progress.one('mouseenter', animateDown);
        });
    };
    
    // 滑鼠移上去進度條增寬
    var animateDown = function(){
        $(this).animate({
            height:20,		//高度
            duration:10		//寬度
        }, function(){
            $progress.addClass('open');
            $progress.one('mouseleave', animateUp);
        });
    };
    
    // 預設為沒open
    $progress.one('mouseenter', animateDown);

//----------------------------------------------按讚----------------------------------------------// 
    
    $('#likesButton').click(function(){
    	//按讚
    	if($('#likesIcon').hasClass('fa-heart-o')){
    		$('#likesIcon').removeClass('fa-heart-o');
        	$('#likesIcon').addClass('fa-heart');
        	$.ajax({
        		url: ajaxURL+'AnyCourse/PlayerInterfaceServlet.do',
            	method: 'POST',
            	cache :false,
            	data:{
            		action:'like',
            		unitId:get('unitId'),
            		like:1,//1代表喜歡
            	},
            	success:function(result){
            		console.log(result.likes);
            		$('#likesNum').text(result.likes);
            	},
            	error:function(){
            		console.log("Like Fail!");
            	}
        	})
    	}
    	//收回讚
    	else{
    		$('#likesIcon').removeClass('fa-heart');
        	$('#likesIcon').addClass('fa-heart-o');
        	$.ajax({
        		url: ajaxURL+'AnyCourse/PlayerInterfaceServlet.do',
            	method: 'POST',
            	cache :false,
            	data:{
            		action:'like',
            		unitId:get('unitId'),
            		like:0//0代表收回讚
            	},
            	success:function(result){
            		console.log(result.likes);
            		$('#likesNum').text(result.likes);
            	},
            	error:function(){
            		console.log("UnLike Fail!");
            	}
        	})
    	}
    })
//----------------------------------------------按讚----------------------------------------------// 
//----------------------------------------------播放介面之推薦影片-------------------------------// 
    $.ajax({
    	url: ajaxURL+'AnyCourse/PlayerInterfaceServlet.do',
    	method: 'POST',
    	cache :false,
    	data:{
    		action:'getRecommendation',
    		unitId:get('unitId'),
    	},
    	success:function(result){
    		console.log(result);
    		for(var i = 0;i < result.length; i++){
    			$('#recommendList').append(
        				'<li>'
    					+'<a class="list-group-item" onclick="jumpToPlayerInterface('+ result[i].unitId + ',' + result[i].type+')">'
    					+'<div class="media">'
    					+'<div class="pull-left" style="padding-left: 0px;">'
    					+'<div class="embed-responsive embed-responsive-16by9 col-xs-12">'
    					+'<img id="img" class="style-scope yt-img-shadow" alt="" width="250"'
    					+'src="' + result[i].videoImgSrc + '">' 
    					+'</div>'
    					+'</div>'
    					+'<div class="media-body">'
    					+'<h5 class="unitUi">'
    					+'<b>影片名稱:' + result[i].unitName + '</b>'
    					+'</h5>'
    					+'<p class="unitUi">開課大學:' + result[i].schoolName + '</p>'
    					+'<p class="unitUi">授課教師:' + result[i].teacher + '老師</p>'
    					+'<p class="unitUi">讚數:' + result[i].likes.toLocaleString() +'</p>'
    					+'</div>'
    					+'</div>'
    					+'</a></li>'
        		);
    		}
    	},
    	error:function(){
    		console.log("Like Fail!");
    	}
    })
    
//----------------------------------------------播放介面之推薦影片------------------------------------// 
    
});


function jumpToPlayerInterface(unitId,type){
    url = "../PlayerInterface.html?unitId="+unitId+"&type="+type;//此處拼接內容
    window.location.href = url;
}