
$(document).ready(function(){
	$( ".column" ).sortable({
	    connectWith: ".column",
	    handle: ".portlet-header",
	    cancel: ".portlet-toggle",
	    placeholder: "portlet-placeholder ui-corner-all"
	  });

	  $( ".portlet" )
	    .addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" )
	    .find( ".portlet-header" )
	      .addClass( "ui-widget-header ui-corner-all" )
	      .prepend( "<span class='ui-icon ui-icon-minusthick portlet-toggle'></span>");

	  $( ".portlet-toggle" ).on( "click", function() {
	    var icon = $( this );
	    icon.toggleClass( "ui-icon-minusthick ui-icon-plusthick" );
	    icon.closest( ".portlet" ).find( ".portlet-content" ).toggle();
	  });
//	$.ajax({
//		url : 'http://localhost:8080/AnyCourse/VideoListServlet.do',
//		method : 'GET',
//		data:{
//			"action":selectList//代表要selectList
//		},
//		success:function(result){
//			videoListArray = new Array(result.length);
//	  		for(var i = 0 ;i < result.length;i++){
//	  			$('#videoListUL').append('<li id = "videoListID_'+videoListID+'" onclick="getID('+videoListID+')">'
//	                    +'<span class="handle ui-sortable-handle">'
//	                    +'<i class="fa fa-ellipsis-v">'
//	                    +'</i><i class="fa fa-ellipsis-v"></i>'
//	                    +'</span>'
//	                    +'<span class="text" id="videoListText_'+videoListID+'">'+result[i].list_name+'</span>'
//	                    +'<div class="tools">'
//	                    +'<button type="button" data-toggle="tooltip" data-placement="top" title="新增至..."><i class="fa fa-plus"></i></button>'
//	                    +'<button type="button" data-toggle="tooltip" data-placement="top" title="分享至..."><i class="fa fa-share-square-o"></i></button>'
//	                    +'<button type="button" data-toggle="modal" data-target="#editModal" onclick="getID('+videoListID+')"><i class="fa fa-edit"  data-toggle="tooltip" data-placement="top" title="編輯"></i></button>'
//	                    +'<button type="button" data-toggle="modal" data-target="#deleteModal1" onclick="getID('+videoListID+')"><i class="fa fa-trash-o" data-toggle="tooltip" data-placement="top" title="刪除"></i></button>'
//	                    +'</div>'
//	                    +'</li>');
//	  			//把modal設為空
//				  $('#named').val("");
//				  
//				  //點擊清單，顯示單元影片
//				  $("#videoListID_"+videoListID).on("click" , function(){
//					  
//					  $.ajax({
//							url : 'http://localhost:8080/AnyCourse/VideoListServlet.do',
//							method : 'GET',
//						    data : {
//						    	"action" : selectUnit,//代表要selectUnit
//						    	"school_name" : videoListArray[checkID-1][5],
//						    	"list_name" : videoListArray[checkID-1][1]
//							},
//							success:function(resultUnit){
//								//清除原先檢視的unit
//								$('#unit li').each(function(){
//								    $(this).remove();
//								}); 
//								unitArray = new Array(resultUnit.length);
//								for(var k = 0 ;k < resultUnit.length;k++){
//									console.log(resultUnit[k].videoType);
//									$("#unit").append(
//											'<li id="videoItem_'+unitVideoID+'">'
//											+'<span class="handle ui-sortable-handle">' 
//											+'<i class="fa fa-ellipsis-h"></i>'
//											+'</span>' 
//											+'<span class="pull-right">'
//											+'<i class="fa fa-times" data-toggle="modal" data-target="#deleteModal2"'
//											+'onclick="getID('+unitVideoID+')" style="cursor: pointer;"></i>'
//											+'</span>'
//											+'<a class="list-group-item" onclick="jumpToPlayerInterface('+ resultUnit[k].unit_id + ',' + resultUnit[k].videoType+')">'
//											+'<div class="media">'
//											+'<div class="col-xs-4 pull-left" style="padding-left: 0px;">'
//											+'<div class="embed-responsive embed-responsive-16by9">'
//											+'<img id="img" class="style-scope yt-img-shadow" alt="" width="230"'
//											+'src="' + resultUnit[k].video_img_src + '">' 
//											+'</div>'
//											+'</div>'
//											+'<div class="media-body">'
//											+'<h4 class="media-heading">'
//											+'<b>影片名稱:' + resultUnit[k].unit_name + '</b>'
//											+'</h4>'
//											+'<p style="margin-bottom: 5px;">開課大學:' + resultUnit[k].school_name + '</p>'
//											+'<p style="margin-bottom: 5px;">授課教師:' + resultUnit[k].teacher + '老師</p>'
//											+'<p style="margin-bottom: 5px;">課程簡介:' + resultUnit[k].course_info + '</p>'
//											+'<p style="margin-bottom: 5px;">讚數:' + resultUnit[k].likes.toLocaleString() +'</p>'
//											+'</div>'
//											+'</div>'
//											+'</a></li>'
//									);
//									unitVideoID++;
//									unitArray[k] = new Array(3);
//								}
////								for(var i = 0 ;i < result.length;i++){
//////						  			console.log(result[i].user_id);
//////						  			console.log(result[i].creator);
////						  			for(var j = 0 ; j < 5;j++){
////						  				if(j == 0)videoListArray[i][j] = result[i].courselist_id;
////						  				else if(j == 1)videoListArray[i][j] = result[i].list_name;
////						  				else if(j == 2)videoListArray[i][j] = result[i].user_id;
////						  				else if(j == 3)videoListArray[i][j] = result[i].creator;
////						  				else videoListArray[i][j] = result[i].oorder;
//////						  				console.log(videoListArray[i][j]);
////						  			}
////								}
//								
//					  			
//								
//					    	},
//							error:function(){alert('failed');}
//						});
//				  });
//				  videoListID++;
////				  console.log("123");
//	  			videoListArray[i] = new Array(6);
//			}
////	  		console.log(result.length);
//	  		for(var i = 0 ;i < result.length;i++){
////	  			console.log(result[i].user_id);
////	  			console.log(result[i].creator);
//	  			for(var j = 0 ; j < 6;j++){
//	  				if(j == 0)videoListArray[i][j] = result[i].courselist_id;
//	  				else if(j == 1)videoListArray[i][j] = result[i].list_name;
//	  				else if(j == 2)videoListArray[i][j] = result[i].user_id;
//	  				else if(j == 3)videoListArray[i][j] = result[i].creator;
//	  				else if(j == 4)videoListArray[i][j] = result[i].oorder;
//	  				else videoListArray[i][j] = result[i].school_name;
////	  				console.log(videoListArray[i][j]);
//	  			}
//	  		}
//  	},
//		error:function(){alert('failed');}
//	});
	  
	  
	 
 }());
	
function jumpToPlayerInterface(unit_id,type){
    url = "pages/PlayerInterface.html?unit_id="+unit_id+"&type="+type;//此處拼接內容
    window.location.href = url;
}


///////////////////////////////////////////////////////////////////////////////////////////////////




