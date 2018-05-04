document.write("<script type='text/javascript' src='../dist/js/swiper.min.js'></script>");

function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
	}

$(document).ready(function() {
	$.ajax({
		url : 'http://140.121.197.130:8400/AnyCourse/ExchangeTextNoteServlet.do',
		method : 'GET',
		cache :false,
		data : {					
			"unit_id" : get("unit_id")
		},
		success:function(result){
//			alert(result);
			
			
    		for(var i = 0 ;i < result.length;i++){
    			
    			$('#exchange_note').append( 	 
						'<div id="exT_' + result[i].user_id + '" class=" col-xs-12">'+
						'<img src="https://ppt.cc/fxYEnx@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
						'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + result[i].nick_name + '</h4>'+
						'<h5 style="float:right;">' + result[i].share_time + '</h5>'+														
						'<textarea class="col-xs-12" rows="4" cols="50" id="exText_' + result[i].text_note_id + '" disabled="disabled" style="float:left;">' + result[i].text_note + '</textarea>'+																			
					//	'<div class="row">'+
					//	'<div class="swiper-container" style="clear:both;">'+
					//	'<div class="swiper-wrapper" id="exP_' + result[i].user_id +'" style="clear:both;">'+
						'<div id="exP_' + result[i].user_id +'"  style="clear:both;">'+		
					//	'</div><div class="swiper-button-next"></div><div class="swiper-button-prev"></div>'+						
					//	'</div></div>'+		
						'</div>'+
						'<div class="ffs-gal-view view'+ result[i].user_id +'">'+
						'<h1 id="picture"></h1>'+ 
						'<img class="ffs-gal-prev ffs-gal-nav prev' + result[i].user_id + ' nav'+ result[i].user_id +'" src="../plugins/Gallery-Popup-jQuery-Fs-Gal/img/prev.svg" alt="Previous picture" title="Previous picture" />'+									     
						'<img class="ffs-gal-next ffs-gal-nav next' + result[i].user_id + ' nav'+ result[i].user_id +'" src="../plugins/Gallery-Popup-jQuery-Fs-Gal/img/next.svg" alt="Next picture" title="Next picture" />'+
						'<img class="ffs-gal-close close'+ result[i].user_id +'"  src="../plugins/Gallery-Popup-jQuery-Fs-Gal/img/close.svg" alt="Close gallery" title="Close gallery" />'+
						'</div>'+																																										
						'</div>'						
	    			); 
    			
    			
			}	
    		$.ajax({
    			url : 'http://140.121.197.130:8400/AnyCourse/ExchangePictureNoteServlet.do',
    			method : 'GET',
    			cache :false,
    			data : {					
					"unit_id" : get("unit_id")
				},
    			success : function(result) {
//    				alert(result.comment_id);
//    				alert(result.user_id);
//    				alert(result.nick_name);
//    				alert(result.comment_time);
//    				alert(result.comment_content);
//    				console.log(result);
    				for(var i = 0 ;i < result.length;i++){
    				$('#exP_'+result[i].user_id).append( 
//    						'<div class="swiper-slide col-xs-2 col-sm-2" style="padding:0px;">'+
    						'<img id="no_'+ result[i].user_id +'_'+ result[i].picture_note_id +'" class="ffs-gal p'+ result[i].user_id +'" src="' + result[i].picture_note_url +'" alt="picture_note_' + result[i].picture_note_id + '" data-url="' + result[i].picture_note_url + '" />'
//    						+'</div>'
    					);	
    				}
//    				$("#reply_div_" + id ).toggle();
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
    		         /*弹出jqXHR对象的信息*/
//    		         alert(jqXHR.responseText);
//    		         alert(jqXHR.status);
//    		         alert(jqXHR.readyState);
//    		         alert(jqXHR.statusText);
    		         /*弹出其他两个参数的信息*/
//    		         alert(textStatus);
//    		         alert(errorThrown);
    		     }
    		})
    	},
		error:function(){}
	});
	
});


//$.ajax({
//	url : 'http://140.121.197.130:8400/AnyCourse/ExchangeKeyLabelServlet.do',
//	method : 'GET', 
//	cache :false,
//	
//	success:function(result){
//		keyLabelArray = result;
//		console.log(keyLabelArray);
//		alert(keyLabelArray);
//		for(maxIndex = 0 ;maxIndex < result.length; maxIndex++){
////			alert("OKOK");
//			$('#exchange_keylabel').append(
//					'<div id="exK_' + keyLabelArray[maxIndex].userId + '" class=" col-xs-12">'+
//					'<img src="https://ppt.cc/fxYEnx@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
//					'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + keyLabelArray[maxIndex].nick_name + '</h4>'+
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
  //$('.fs-gal').click(function() {
	id = (this.id).split("_")[1];  
	
	//Gallery navigation
   //$('.view1 .nav1').click(function() {
  $(document).on('click','.view'+id + ' .nav'+id, function() {	
  //$('.view'+id + ' .nav'+id).click(function() {
	  
    var index = $(this).data('img-index');
    var img = $($('.p'+id).get(index));
    fsGal_DisplayImage(img);
	
  });
  //Close gallery
  
 //$(vv).click(function() {
  $(document).on('click','.view'+id + ' .close'+id, function() {	
  //$('.view'+id + ' .close'+id).click(function() {
	  //alert(typeof(id));
	  //alert(id);
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
  
    fsGal_DisplayImage($(this));
  });
  //Display gallery
  function fsGal_DisplayImage(obj) {
    //Clear navigation buttons
	//id = (obj.id).split("_")[1];
	
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
