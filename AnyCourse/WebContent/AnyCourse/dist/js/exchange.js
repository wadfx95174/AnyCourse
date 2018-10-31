
document.write("<script type='text/javascript' src='../dist/js/swiper.min.js'></script>");

function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
	}

$(document).ready(function() {
	$.ajax({
		url : ajaxURL+'AnyCourse/ExchangeTextNoteServlet.do',
		method : 'GET',
		cache :false,
		data : {					
			"unitId" : get("unitId")
		},
		success:function(result){
    		for(var i = 0 ;i < result.length;i++){
    			
    			$('#exchangeNote').append( 	 
						'<div id="exT_' + result[i].userId + '" class=" col-xs-12">'+
						'<img src="https://ppt.cc/fxYEnx@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
						'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + result[i].nickName + '</h4>'+
						'<a class="btn pull-right noteLikesClass" style="color: black;" id="noteLikesButton_'+ result[i].userId +'" onclick="noteLikes(this.id)">'+
						'<i id="noteLikesIcon_'+ result[i].userId +'" class="fa fa-heart-o" role="button"></i>'+
						'<span id="noteLikesNum_'+ result[i].userId +'" style="padding-bottom: 10px;">0</span>'+
						'</a>'+
						'<h5 style="float:right;">' + result[i].shareTime + '</h5>'+														
						'<textarea class="col-xs-12" rows="4" cols="50" id="exText_' + result[i].textNoteId + '" disabled="disabled" style="float:left;">' + result[i].textNote + '</textarea>'+
						'<div id="exP_' + result[i].userId +'"  style="clear:both;">'+	
						'</div>'+
						'<div class="ffs-gal-view view'+ result[i].userId +'">'+
						'<h1 id="picture"></h1>'+ 
						'<img class="ffs-gal-prev ffs-gal-nav prev' + result[i].userId + ' nav'+ result[i].userId +'" src="../plugins/Gallery-Popup-jQuery-Fs-Gal/img/prev.svg" alt="Previous picture" title="Previous picture" />'+									     
						'<img class="ffs-gal-next ffs-gal-nav next' + result[i].userId + ' nav'+ result[i].userId +'" src="../plugins/Gallery-Popup-jQuery-Fs-Gal/img/next.svg" alt="Next picture" title="Next picture" />'+
						'<img class="ffs-gal-close close'+ result[i].userId +'"  src="../plugins/Gallery-Popup-jQuery-Fs-Gal/img/close.svg" alt="Close gallery" title="Close gallery" />'+
						'</div>'+																																										
						'</div>'						
	    			); 
    			
    			
			}	
    		$.ajax({
    			url : ajaxURL+'AnyCourse/ExchangePictureNoteServlet.do',
    			method : 'GET',
    			cache :false,
    			data : {					
					"unitId" : get("unitId")
				},
    			success : function(result) {
    				for(var i = 0 ;i < result.length;i++){
    				$('#exP_'+result[i].userId).append( 
    						'<img id="no_'+ result[i].userId +'_'+ result[i].pictureNoteId +'" class="ffs-gal p'+ result[i].userId +'" src="' + result[i].pictureNoteUrl +'" alt="pictureNote_' + result[i].pictureNoteId + '" data-url="' + result[i].pictureNoteUrl + '" />'
    					);	
    				}
    				var swiper = new Swiper('.swiper-container', {
    				      slidesPerView: 3,
    				      spaceBetween: 0,
    				      slidesPerGroup: 1,
    				      loop: false,
    				      loopFillGroupWithBlank: true,
    				      navigation: {
    				        nextEl: '.swiper-button-next',
    				        prevEl: '.swiper-button-prev',
    				      },
    				    });
    			},
    			error: function (jqXHR, textStatus, errorThrown) {
    		     }
    		})
    	},
		error:function(){}
	});
	
});

//----------------------------------------------筆記按讚----------------------------------------------// 

function noteLikes(input){
	
	var iconId = input.split('_')[1];
//	alert(iconId);
    //取目前的按讚數
    var tempNoteLikeNum = parseInt($('#noteLikesNum_'+ iconId).text());

	if($('#noteLikesIcon_'+ iconId).hasClass('fa-heart-o')){
		$('#noteLikesIcon_'+ iconId).removeClass('fa-heart-o');
    	$('#noteLikesIcon_'+ iconId).addClass('fa-heart');
        $('#noteLikesNum_'+ iconId).text(tempNoteLikeNum+1);
        $.ajax({
    		url: ajaxURL+'AnyCourse/Servlet.do',
        	method: 'POST',
        	cache :false,
        	data:{
        		action:'like',
        		unitId:get('unitId'),
        		like:1,//1代表喜歡
        	},
        	error:function(){
        		console.log("Like Fail!");
        	}
    	})
	}
	//收回讚
	else{
		$('#noteLikesIcon_'+ iconId).removeClass('fa-heart');
    	$('#noteLikesIcon_'+ iconId).addClass('fa-heart-o');
        console.log(tempNoteLikeNum);
        $('#noteLikesNum_'+ iconId).text(tempNoteLikeNum-1);
    	$.ajax({
    		url: ajaxURL+'AnyCourse/Servlet.do',
        	method: 'POST',
        	cache :false,
        	data:{
        		action:'like',
        		unitId:get('unitId'),
        		like:0//0代表收回讚
        	},
        	error:function(){
        		console.log("UnLike Fail!");
        	}
    	})
	}
}



//$.ajax({
//	url : ajaxURL+'AnyCourse/ExchangeKeyLabelServlet.do',
//	method : 'GET', 
//	cache :false,
//	
//	success:function(result){
//		keyLabelArray = result;
//		console.log(keyLabelArray);
//		alert(keyLabelArray);
//		for(maxIndex = 0 ;maxIndex < result.length; maxIndex++){
////			alert("OKOK");
//			$('#exchangeKeylabel').append(
//					'<div id="exK_' + keyLabelArray[maxIndex].userId + '" class=" col-xs-12">'+
//					'<img src="https://ppt.cc/fxYEnx@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
//					'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + keyLabelArray[maxIndex].nickName + '</h4>'+
//					'<li class="list-group-item">'+ keyLabelArray[maxIndex].keyLabelName+
//					'<ul class="list-group-submenu">'+
//					'<a href="#" class = "ukl exchange" id = "exchange-ukl-' + maxIndex + '" style="color: #FFF"><li class="list-group-submenu-item lightBlue">使用</li></a>'+
//					'</ul>'+
//					'</li>'+
//					'</div>'
//					);
//		} // end for
//		
//		// 點選交流區的重點標籤，暫存區出現
//		$('.list-group-submenu').on('click', '.exchange', function(event) 
//		{
//			
//			selectId = parseInt(this.getAttribute("id").split("-")[2]);
//			addToTempKeyLabel(selectId);
//		})
//	}, // end success
//	error:function(){console.log('getEKL fail');}
//});	// end ajax
//
////設置暫存重點標籤
//function addToTempKeyLabel(index)
//{
//	$('#keyLabel2').append('<li class="list-group-item">'
//			+ keyLabelArray[index].keyLabelName
//			+'<ul class="list-group-submenu">'
//			+'<a href="javascript:void(0)" class = "temp dkl" id = "temp-dkl-' + index + '" style="color: #FFF"><li class="list-group-submenu-item">刪除</li></a>'
//			+'<a href="javascript:void(0)" class = "temp akl" id = "temp-akl-' + index + '" style="color: #FFF"><li class="list-group-submenu-item primary">添加</li></a>'
//			+'<a href="#" class = "temp ukl" id = "temp-ukl-' + index + '" style="color: #FFF"><li class="list-group-submenu-item lightBlue">使用</li></a>'
//			+'</ul>'
//			+'</li>');
//}

var id;
$('document').ready(function() {
  //Make gallery objects clickable
  $(document).on('click','.ffs-gal', function() {
  
  id = (this.id).split("_")[1];  
	
  //Gallery navigation
  $(document).on('click','.view'+id + ' .nav'+id, function() {	
  
	  
    var index = $(this).data('img-index');
    var img = $($('.p'+id).get(index));
    fsGalDisplayImage(img);
	
  });
  //Close gallery
  $(document).on('click','.view'+id + ' .close'+id, function() {	
    $('.view'+id).fadeOut();
  });
  //Keyboard navigation
  $('body').keydown(function(e) {
    if (e.keyCode == 37) {
      $('.view'+id + ' .prev'+id).click(); //Left arrow
    }
    else if(e.keyCode == 39) { // right
      $('.view'+id + ' .next'+id).click(); //Right arrow
    }
    else if(e.keyCode == 27) { // right
      $('.view'+id + ' .close'+id).click(); //ESC
    }
  });
  
    fsGalDisplayImage($(this));
  });
  //Display gallery
  function fsGalDisplayImage(obj) {
    //Clear navigation buttons
    $('.view'+id + '>.prev'+id).fadeOut();
	
    $('.view'+id + '>.next'+id).fadeOut();
    //Set current image
	
    var title = obj.attr('alt');
    if (!title || title == '') { title = obj.attr('title'); }
    $('.view'+id +' > h1').text(title);
    if (!title || title == '') { $('.view'+id +' > h1').fadeOut(); }
    else { $('.view'+id +' > h1').fadeIn(); }
    var img = obj.data('url');
    $('.view'+id).css('background-image', 'url('+img+')');
    //Create buttons
	
    var current = $('.p'+id).index(obj);
    var prev = current - 1;
    var next = current + 1;
    if (prev >= 0) {
      $('.view'+id +' > .prev'+id).data('img-index', prev);
      $('.view'+id +' > .prev'+id).fadeIn();
    }
    if (next < $('.p'+id).length) {
      $('.view'+id +' > .next'+id).data('img-index', next);
      $('.view'+id +' > .next'+id).fadeIn();
    }
    $('.view'+id).fadeIn(); //Display gallery
	
  }
  
});
