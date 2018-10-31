
var userId;
function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
}

$(document).ready(function(){
	checkLogin("../", "../../../");
	$.ajax({
		method:"GET",
		cache :false,
		url:ajaxURL+'AnyCourse/PersonalTextNoteServlet.do',
		success: function(response){
			var videoId = 1;
			// console.log(response);
			// array = response;
			unitArray = new Array(response.length);
			for (var i = 0; i < response.length; i++)
			{			
				$('#result').append(
						'<ul class="list-group list-group-horizontal">'
						+'<li class="list-group-item col-xs-1">'
						+'<div class="personalNoteShare"><div class="btn-group show-on-hover dropup" >'
						+'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">分享' 
						+'<span class="caret caret-up"></span></button><ul class="dropdown-menu drop-up" role="menu">'
						+'<li><a data-toggle="modal" data-target="#addToGroupNote" id="personalNote_'+ response[i].unitId  +'" onclick="getUnitId('+videoId+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>分享至群組</a></li>'
						+'</ul></div></div>'
						+'</li>'
						+'<li class="list-group-item col-xs-3">'
						+'<div class="personalNote" style="height: 285px;"><a class="list-group-item" style="height: 285px;" href="../PlayerInterface.html?type='+ (response[i].videoUrl.split("/")[2]=='www.youtube.com'?1:2) + '&unitId='+response[i].unitId+'">'					
						+'<h4 class="media-heading">'
						+'<b>影片名稱:' + response[i].unitName + '</b>'
						+'</h4>'
						+'<p style="margin-bottom: 5px;">開課大學:' + response[i].schoolName + '</p>'					
						+'<p style="margin-bottom: 5px;">讚數:' + response[i].likes +'</p>'
						+'</a></div></li>'
						+'<li class="list-group-item col-xs-4">'
						+'<div class="personalNoteDiv" style="overflow:auto;height: 285px;">' + response[i].textNote + '</div>'
						+'</li>'
						+'<li class="list-group-item col-xs-4">'
						+'<div class="personalNoteDiv" style="overflow:auto;height: 285px;" id="personalPictureNote_'+ response[i].unitId  +'"></div>'
						+'</li>'
						+'</ul>'				
						+
						'<div class="ffs-gal-view view'+ response[i].unitId +'">'+
						'<h1 id="picture"></h1>'+ 
						'<img class="ffs-gal-prev ffs-gal-nav prev' + response[i].unitId + ' nav'+ response[i].unitId +'" src="../../plugins/Gallery-Popup-jQuery-Fs-Gal/img/prev.svg" alt="Previous picture" title="Previous picture" />'+									     
						'<img class="ffs-gal-next ffs-gal-nav next' + response[i].unitId + ' nav'+ response[i].unitId +'" src="../../plugins/Gallery-Popup-jQuery-Fs-Gal/img/next.svg" alt="Next picture" title="Next picture" />'+
						'<img class="ffs-gal-close close'+ response[i].unitId +'"  src="../../plugins/Gallery-Popup-jQuery-Fs-Gal/img/close.svg" alt="Close gallery" title="Close gallery" />'+
						'</div>'
				);
				videoId++;
				unitArray[i] = response[i].unitId;
			}
			$.ajax({
				method:"GET",
				cache :false,
				url:ajaxURL+'AnyCourse/PersonalPictureNoteServlet.do',
				success: function(response){
					// console.log(response);
					// array = response;
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
	
	//取得該使用者的所有群組(用來放在下拉式選單，讓使用者選擇要分享哪個群組)
	$.ajax({
		url : ajaxURL+'AnyCourse/GroupNoteServlet.do',
		method : 'Get',
		data:{
			"userId" : userId
		},
		cache :false,
		success:function(result){
			if(result.length == 0){
				$('#addToGroupNoteModalBody').append('<option value="null">無</option>');
			}
			else{
				for(var i = 0;i < result.length;i++){
					$('#addToGroupNoteModalBody').append('<option value="'+result[i].groupId+'">'+result[i].groupName+'</option>');
				}
			}					
		},
		error:function(){
			console.log("append GroupName to modal error");
		}
	});

	//將筆記分享至指定群組
	$('#addToGroupNoteButton').click(function(){
		
		$.ajax({
			url:ajaxURL+'AnyCourse/GroupNoteServlet.do',
			method:'POST',
			cache:false,
			data:{
				state:'insert',
				groupId:$('#addToGroupNoteModalBody').val(),
				unitId:unitArray[checkUnitId-1]
			},
			success:function(){
				shareNoteToGroup();
				$("#shareNoteModal").modal('show');
			},
			error:function(e){
				console.log("add personalNote to group error");
			}
		});
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

//分享個人筆記至指定群組
function shareNoteToGroup(){
	$.ajax({
		url:ajaxURL + 'AnyCourse/NotificationServlet.do',
		method:'POST',
		cache:false,
		data:{
			'action': "groupNotification",
			'groupId': $('#addToGroupNoteModalBody').val(),
			'url': ajaxURL + "AnyCourse/AnyCourse/pages/Group/Note.html?groupId=" + $('#addToGroupNoteModalBody').val(),
			'type': "shareNoteToGroup"
		},
		success:function(response){
			console.log(response);

			for(var i = 0;i < response.length;i ++){
				ws.send(JSON.stringify({
	                type: response[i].type,
	                toUserId: response[i].toUserId,
	                notificationId: response[i].notificationId,
	                nickname: response[i].nickname,
	                groupId: response[i].groupId,
	                groupName: response[i].groupName,
	                url: response[i].url
	            }));
			}
		},
		error:function(xhr, ajaxOptions, thrownError){
			console.log(xhr);
			console.log(thrownError);
			console.log("personalNote.js shareNoteToGroup error");
		}
	});
}

function getUnitId(id){
	checkUnitId = id;
}