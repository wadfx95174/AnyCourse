var ajax_url="http://140.121.197.130:8400/";
//var ajax_url="http://localhost:8080/";

$(document).ready(function(){
	checkLogin("../", "../../../");
	$.ajax({
		method:"GET",
		cache :false,
		url:ajax_url+'AnyCourse/PersonalTextNoteServlet.do',
		success: function(response){
			console.log(response);
			array = response;
			for (var i = 0; i < response.length; i++)
			{			
					$('#result').append(
							'<li>'
							+'<ul class="list-group list-group-horizontal">'
							+'<li class="list-group-item col-xs-4">'
							+'<div class="personalNote "><a class="list-group-item" href="../PlayerInterface.html?type='+ (response[i].video_url.split("/")[2]=='www.youtube.com'?1:2) + '&unit_id='+response[i].unit_id+'">'					
							+'<h4 class="media-heading">'
							+'<b>影片名稱:' + response[i].unit_name + '</b>'
							+'</h4>'						
							+'<p style="margin-bottom: 5px;">開課大學:' + response[i].school_name + '</p>'					
							+'<p style="margin-bottom: 5px;">讚數:' + response[i].likes +'</p>'
							+'</a></div></li>'
							+'<li class="list-group-item col-xs-4">'
							+'<div class="personalNote ">' + response[i].text_note + '</div>'
							+'</li>'
							+'<li class="list-group-item col-xs-4">'
							+'<div class="personalNote " id="personalPictureNote_'+ response[i].unit_id  +'"></div>'
							+'</li>'
							+'</ul>'				
							+'</li>'+
							'<div class="ffs-gal-view view'+ response[i].unit_id +'">'+
							'<h1 id="picture"></h1>'+ 
							'<img class="ffs-gal-prev ffs-gal-nav prev' + response[i].unit_id + ' nav'+ response[i].unit_id +'" src="../../plugins/Gallery-Popup-jQuery-Fs-Gal/img/prev.svg" alt="Previous picture" title="Previous picture" />'+									     
							'<img class="ffs-gal-next ffs-gal-nav next' + response[i].unit_id + ' nav'+ response[i].unit_id +'" src="../../plugins/Gallery-Popup-jQuery-Fs-Gal/img/next.svg" alt="Next picture" title="Next picture" />'+
							'<img class="ffs-gal-close close'+ response[i].unit_id +'"  src="../../plugins/Gallery-Popup-jQuery-Fs-Gal/img/close.svg" alt="Close gallery" title="Close gallery" />'+
							'</div>'
					);
				}
			$.ajax({
				method:"GET",
				cache :false,
				url:ajax_url+'AnyCourse/PersonalPictureNoteServlet.do',
				success: function(response){
					console.log(response);
					array = response;
					for (var i = 0; i < response.length; i++)
					{			
							$('#personalPictureNote_'+response[i].unit_id).append(
									'<img id="no_'+ response[i].unit_id +'_'+ response[i].user_id+'_'+ response[i].picture_note_id +'" class="ffs-gal p'+ response[i].unit_id +'" src="' + response[i].picture_note_url +'" alt="picture_note_' + response[i].picture_note_id + '" data-url="' + response[i].picture_note_url + '" />'
							);
//						}
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

