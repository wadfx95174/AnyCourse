var ajax_url="http://140.121.197.130:8400/";
//var ajax_url="http://localhost:8080/";


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


function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
	}
function formatTime(seconds) {
    return [
        parseInt(seconds / 60 / 60),
        parseInt(seconds / 60 % 60),
        parseInt(seconds % 60)
    ]
        .join(":")
        .replace(/\b(\d)\b/g, "0$1");
}

var video;
var keyLabelArray;
var exKeyLabelArray;
var maxIndex = 0;
var selectId = 0;
var element;		//存DOM元素
var shareState = ['分享','收回'];

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
//    	alert("AAA");
//    	alert($("#text_area").attr("disabled"));
    	if(typeof($("#text_area").attr("disabled")) === "string"){
    		$("#text_area").removeAttr("disabled");
    		$("#noteFooter").slideToggle();
    	}
    	else{
//    		alert("OK");
    		$("#text_area").attr("disabled","false");
    		$("#noteFooter").slideToggle();
    	}   	
    });
    $("#cancelNote").click(function(){
    	$("#text_area").attr("disabled","false");
        $("#noteFooter").slideToggle();
    });
    $( function() {
	    $( "#keyLabel1, #keyLabel2" ).sortable({
	//      connectWith: ".connectedSortable"
	    }).disableSelection();
    });

//----------------------------------------------video----------------------------------------------//
    if (get('list_id') != undefined)
    {
    	$.ajax({
        	url: ajax_url+'AnyCourse/PlayerInterfaceServlet.do',
        	method: 'POST',
        	cache :false,
        	data: {
        		courselistId: get('list_id'),
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
        	    	$('#listbox').append('      <a href="PlayerInterface.html?type='+ (result[i].videoUrl.split("/")[2]=='www.youtube.com'?1:2) + '&unit_id='+result[i].unitId+'&list_id='+get('list_id')+'" class="list-group-item">'
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
    
    
    var oHead = document.getElementsByTagName('HEAD').item(0); 
    var oScript= document.createElement("script"); 
    oScript.type = "text/javascript"; 
    oScript.src= (get('type') == "1") ? "../dist/js/youtubePlayer.js" : "../dist/js/jwPlayer.js"; 
    oHead.appendChild(oScript); 
    
//----------------------------------------------keyLabel----------------------------------------------//   

//----------------------------------------------按讚----------------------------------------------// 
    
    $('#likesButton').click(function(){
    	//按讚
    	if($('#likesIcon').hasClass('fa-heart-o')){
//    		console.log("like");
    		$('#likesIcon').removeClass('fa-heart-o');
        	$('#likesIcon').addClass('fa-heart');
//        	console.log(get('unit_id'));
        	$.ajax({
        		url: ajax_url+'AnyCourse/PlayerInterfaceServlet.do',
            	method: 'POST',
            	cache :false,
            	data:{
            		action:'like',
            		unit_id:get('unit_id'),
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
//    		console.log("Un");
    		$('#likesIcon').removeClass('fa-heart');
        	$('#likesIcon').addClass('fa-heart-o');
        	$.ajax({
        		url: ajax_url+'AnyCourse/PlayerInterfaceServlet.do',
            	method: 'POST',
            	cache :false,
            	data:{
            		action:'like',
            		unit_id:get('unit_id'),
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
    	url: ajax_url+'AnyCourse/PlayerInterfaceServlet.do',
    	method: 'POST',
    	cache :false,
    	data:{
    		action:'getRecommendation',
    		unit_id:get('unit_id'),
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
        				
        				///////////
//        				+'<a href="PlayerInterface.html" class="list-group-item">'
//        				+'<div class="media">'
//        				+'<div class="col-xs-6 pull-left" style="padding-left: 0px;">'
//        				+'<div class="embed-responsive embed-responsive-16by9">'
//        				+'<img id="img" class="style-scope yt-img-shadow" alt="" width="210" src="https://i.ytimg.com/vi/B-AoHE6dPnk/hqdefault.jpg">'   
//        				+'</div>'
//        				+'</div>'
//        				+'<div class="media-body">'
//        				+'<h3 class="media-heading">導涵式公式的推廣</h3>'
//        				+'<p>中央大學 王老師</p>'
//        				+'<p>讚數:216,165</p>'
//        				+'</div>'
//        				+'</div>'
//        				+'</a>'
        		);
    		}
    		
//    		$('#likesNum').text(result.likes);
    	},
    	error:function(){
    		console.log("Like Fail!");
    	}
    })
    
//----------------------------------------------播放介面之推薦影片------------------------------------// 
    
});


function jumpToPlayerInterface(unit_id,type){
    url = "../PlayerInterface.html?unit_id="+unit_id+"&type="+type;//此處拼接內容
    window.location.href = url;
}