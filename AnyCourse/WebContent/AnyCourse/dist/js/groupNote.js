$(document).ready(function(){
	checkLogin("../", "../../../");
	$.ajax({
		method:"GET",
		cache :false,
		url:ajaxURL+'AnyCourse/GroupTextNoteServlet.do',
		success: function(response){
			console.log(response);
			array = response;
			for (var i = 0; i < response.length; i++)
			{			
					$('#result').append(
							'<li>'
							+'<ul class="list-group list-group-horizontal">'
							+'<li class="list-group-item col-xs-4">'
							+'<div class="personalNote "><a class="list-group-item" href="../PlayerInterface.html?type='+ (response[i].videoUrl.split("/")[2]=='www.youtube.com'?1:2) + '&unitId='+response[i].unitId+'">'					
							+'<h4 class="media-heading">'
							+'<b>影片名稱:' + response[i].unitName + '</b>'
							+'</h4>'						
							+'<p style="margin-bottom: 5px;">開課大學:' + response[i].schoolName + '</p>'					
							+'<p style="margin-bottom: 5px;">讚數:' + response[i].likes +'</p>'
							+'</a></div></li>'
							+'<li class="list-group-item col-xs-4">'
							+'<div class="personalNote ">' + response[i].textNote + '</div>'
							+'</li>'
							+'<li class="list-group-item col-xs-4">'
							+'<div class="personalNote " id="personalPictureNote_'+ response[i].unitId  +'"></div>'
							+'</li>'
							+'</ul>'				
							+'</li>'+
							'<div class="ffs-gal-view view'+ response[i].unitId +'">'+
							'<h1 id="picture"></h1>'+ 
							'<img class="ffs-gal-prev ffs-gal-nav prev' + response[i].unitId + ' nav'+ response[i].unitId +'" src="../../plugins/Gallery-Popup-jQuery-Fs-Gal/img/prev.svg" alt="Previous picture" title="Previous picture" />'+									     
							'<img class="ffs-gal-next ffs-gal-nav next' + response[i].unitId + ' nav'+ response[i].unitId +'" src="../../plugins/Gallery-Popup-jQuery-Fs-Gal/img/next.svg" alt="Next picture" title="Next picture" />'+
							'<img class="ffs-gal-close close'+ response[i].unitId +'"  src="../../plugins/Gallery-Popup-jQuery-Fs-Gal/img/close.svg" alt="Close gallery" title="Close gallery" />'+
							'</div>'
					);
				}
			$.ajax({
				method:"GET",
				cache :false,
				url:ajaxURL+'AnyCourse/GroupPictureNoteServlet.do',
				success: function(response){
					console.log(response);
					array = response;
					for (var i = 0; i < response.length; i++)
					{			
						$('#personalPictureNote_'+response[i].unitId).append(
								'<img id="no_'+ response[i].unitId +'_'+ response[i].userId+'_'+ response[i].pictureNoteId +'" class="ffs-gal p'+ response[i].unitId +'" src="' + response[i].pictureNoteUrl +'" alt="pictureNote_' + response[i].pictureNoteId + '" data-url="' + response[i].pictureNoteUrl + '" />'
						);
					}
					
				},
				error: function(){
					alert("fail");
				}
			});		
		},
		error: function(){
			alert("fail");
		}		
	});
});

var id;
$('document').ready(function() {
  //Make gallery objects clickable
  $(document).on('click','.ffs-gal', function() {
	id = (this.id).split("_")[1];  
	
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