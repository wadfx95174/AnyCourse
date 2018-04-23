$('#unit').slimScroll({
    height: '400px;'
  });
$('#list').slimScroll({
    height: '400px;'
  });
var checkID;
var videoListArray;
var unitArray;

$(document).ready(function() {
  $('#addModal').on('shown.bs.modal', function () {
    $('#named').focus();
  });

  $('#editModal').on('shown.bs.modal', function () {
    $('#edited').focus();
  });

  $(".todo-list").sortable({
    placeholder: "sort-highlight",
    handle: ".handle",
    forcePlaceholderSize: true,
    zIndex: 999999
  });
  
  var selectList = 0;
  var selectUnit = 1;
  var videoListID = 1;
  var unitVideoID = 1;
  	//取得資料庫的資料(courselist&list)
	$.ajax({
		url : 'http://localhost:8080/AnyCourse/VideoListServlet.do',
		method : 'GET',
		data:{
			"action":selectList//代表要selectList
		},
		success:function(result){
			videoListArray = new Array(result.length);
	  		for(var i = 0 ;i < result.length;i++){
	  			$('#videoListUL').append('<li id = "videoListID_'+videoListID+'" onclick="getID('+videoListID+')">'
	                    +'<span class="handle ui-sortable-handle">'
	                    +'<i class="fa fa-ellipsis-v">'
	                    +'</i><i class="fa fa-ellipsis-v"></i>'
	                    +'</span>'
	                    +'<span class="text" id="videoListText_'+videoListID+'">'+result[i].list_name+'</span>'
	                    +'<div class="tools">'
	                    +'<button type="button" data-toggle="tooltip" data-placement="top" title="新增至..."><i class="fa fa-plus"></i></button>'
	                    +'<button type="button" data-toggle="tooltip" data-placement="top" title="分享至..."><i class="fa fa-share-square-o"></i></button>'
	                    +'<button type="button" data-toggle="modal" data-target="#editModal" onclick="getID('+videoListID+')"><i class="fa fa-edit"  data-toggle="tooltip" data-placement="top" title="編輯"></i></button>'
	                    +'<button type="button" data-toggle="modal" data-target="#deleteModal1" onclick="getID('+videoListID+')"><i class="fa fa-trash-o" data-toggle="tooltip" data-placement="top" title="刪除"></i></button>'
	                    +'</div>'
	                    +'</li>');
	  			//把modal設為空
				  $('#named').val("");
				  
				  //點擊清單，顯示單元影片
				  $("#videoListID_"+videoListID).on("click" , function(){
					  unitVideoID = 1;
					  $.ajax({
							url : 'http://localhost:8080/AnyCourse/VideoListServlet.do',
							method : 'GET',
						    data : {
						    	"action" : selectUnit,//代表要selectUnit
						    	"school_name" : videoListArray[checkID-1][5],
						    	"list_name" : videoListArray[checkID-1][1]
							},
							success:function(resultUnit){
								//清除原先檢視的unit
								$('#unit li').each(function(){
								    $(this).remove();
								}); 
								unitArray = new Array(resultUnit.length);
								for(var k = 0 ;k < resultUnit.length;k++){
									console.log(resultUnit[k].videoType);
									$("#unit").append(
											'<li id="videoItem_'+unitVideoID+'">'
											+'<span class="handle ui-sortable-handle">' 
											+'<i class="fa fa-ellipsis-h"></i>'
											+'</span>' 
											+'<span class="pull-right">'
											+'<i class="fa fa-times" data-toggle="modal" data-target="#deleteModal2"'
											+'onclick="getID('+unitVideoID+')" style="cursor: pointer;"></i>'
											+'</span>'
											+'<a class="list-group-item" onclick="jumpToPlayerInterface('+ resultUnit[k].unit_id + ',' + resultUnit[k].videoType+')">'
											+'<div class="media">'
											+'<div class="col-xs-4 pull-left" style="padding-left: 0px;">'
											+'<div class="embed-responsive embed-responsive-16by9">'
											+'<img id="img" class="style-scope yt-img-shadow" alt="" width="230"'
											+'src="' + resultUnit[k].video_img_src + '">' 
											+'</div>'
											+'</div>'
											+'<div class="media-body">'
											+'<h4 class="media-heading">'
											+'<b>影片名稱:' + resultUnit[k].unit_name + '</b>'
											+'</h4>'
											+'<p style="margin-bottom: 5px;">開課大學:' + resultUnit[k].school_name + '</p>'
											+'<p style="margin-bottom: 5px;">授課教師:' + resultUnit[k].teacher + '老師</p>'
											+'<p style="margin-bottom: 5px;">課程簡介:' + resultUnit[k].course_info + '</p>'
											+'<p style="margin-bottom: 5px;">讚數:' + resultUnit[k].likes.toLocaleString() +'</p>'
											+'</div>'
											+'</div>'
											+'</a></li>'
									);
									unitVideoID++;
									unitArray[k] = new Array(3);
								}
//								for(var i = 0 ;i < result.length;i++){
////						  			console.log(result[i].user_id);
////						  			console.log(result[i].creator);
//						  			for(var j = 0 ; j < 5;j++){
//						  				if(j == 0)videoListArray[i][j] = result[i].courselist_id;
//						  				else if(j == 1)videoListArray[i][j] = result[i].list_name;
//						  				else if(j == 2)videoListArray[i][j] = result[i].user_id;
//						  				else if(j == 3)videoListArray[i][j] = result[i].creator;
//						  				else videoListArray[i][j] = result[i].oorder;
////						  				console.log(videoListArray[i][j]);
//						  			}
//								}
								
					  			
								
					    	},
							error:function(){alert('failed');}
						});
				  });
				  videoListID++;
//				  console.log("123");
	  			videoListArray[i] = new Array(6);
			}
//	  		console.log(result.length);
	  		for(var i = 0 ;i < result.length;i++){
//	  			console.log(result[i].user_id);
//	  			console.log(result[i].creator);
	  			for(var j = 0 ; j < 6;j++){
	  				if(j == 0)videoListArray[i][j] = result[i].courselist_id;
	  				else if(j == 1)videoListArray[i][j] = result[i].list_name;
	  				else if(j == 2)videoListArray[i][j] = result[i].user_id;
	  				else if(j == 3)videoListArray[i][j] = result[i].creator;
	  				else if(j == 4)videoListArray[i][j] = result[i].oorder;
	  				else videoListArray[i][j] = result[i].school_name;
//	  				console.log(videoListArray[i][j]);
	  			}
	  		}
  	},
		error:function(){alert('failed');}
	});
  var insert = 0;
  var remove = 1;
  var update = 2;
  
  //新增courseList
  $("#addListButton").click(function(){
	  if($("#named").val()==""){
		 $("#unAdd").dialog( "open" );
	  }
	  else{
		  $.ajax({
			  url : 'http://localhost:8080/AnyCourse/VideoListServlet.do',
			  method : 'POST', 
			  data : {
				  "action" : insert,//代表要insert
				  "list_name" : $("#named").val()
				},
			  success:function(result){
				  $('#videoListUL').append('<li id = "videoListID_'+videoListID+'"'+'>'
	                    +'<span class="handle ui-sortable-handle">'
	                    +'<i class="fa fa-ellipsis-v">'
	                    +'</i><i class="fa fa-ellipsis-v"></i>'
	                    +'</span>'
	                    +'<span class="text" id="videoListText_'+videoListID+'"'+'>'+$("#named").val()+'</span>'
	                    +'<div class="tools">'
	                    +'<button type="button" data-toggle="tooltip" data-placement="top" title="新增至..."><i class="fa fa-plus"></i></button>'
	                    +'<button type="button" data-toggle="tooltip" data-placement="top" title="分享至..."><i class="fa fa-share-square-o"></i></button>'
	                    +'<button type="button" data-toggle="modal" data-target="#editModal" onclick="getID('+videoListID+')"><i class="fa fa-edit" data-toggle="tooltip" data-placement="top" title="編輯"></i></button>'
	                    +'<button type="button" data-toggle="modal" data-target="#deleteModal1" onclick="getID('+videoListID+')"><i class="fa fa-trash-o" data-toggle="tooltip" data-placement="top" title="刪除"></i></button>'
	                    +'</div>'
	                    +'</li>');
				  //把modal設為空
				  $('#named').val("");
				  //點擊清單，顯示單元影片
				  $("#videoListUL").on("click","#videoListID_"+videoListID, function(){
					  unitVideoID = 1;
					  $.ajax({
							url : 'http://localhost:8080/AnyCourse/VideoListServlet.do',
							method : 'GET',
						    data : {
						    	"action" : selectUnit,//代表要selectUnit
						    	"school_name" : videoListArray[checkID-1][5],
						    	"list_name" : videoListArray[checkID-1][1]
							},
							success:function(resultUnit){
								//清除原先檢視的unit
								$('#unit li').each(function(){
								    $(this).remove();
								}); 
								unitArray = new Array(resultUnit.length);
								for(var k = 0 ;k < resultUnit.length;k++){
									console.log(resultUnit[k].videoType);
									$("#unit").append(
											'<li id="videoItem_'+unitVideoID+'">'
											+'<span class="handle ui-sortable-handle">' 
											+'<i class="fa fa-ellipsis-h"></i>'
											+'</span>' 
											+'<span class="pull-right">'
											+'<i class="fa fa-times" data-toggle="modal" data-target="#deleteModal2"'
											+'onclick="getID('+unitVideoID+')" style="cursor: pointer;"></i>'
											+'</span>'
											+'<a class="list-group-item" onclick="jumpToPlayerInterface('+ resultUnit[k].unit_id + ',' + resultUnit[k].videoType+')">'
											+'<div class="media">'
											+'<div class="col-xs-4 pull-left" style="padding-left: 0px;">'
											+'<div class="embed-responsive embed-responsive-16by9">'
											+'<img id="img" class="style-scope yt-img-shadow" alt="" width="230"'
											+'src="' + resultUnit[k].video_img_src + '">' 
											+'</div>'
											+'</div>'
											+'<div class="media-body">'
											+'<h4 class="media-heading">'
											+'<b>影片名稱:' + resultUnit[k].unit_name + '</b>'
											+'</h4>'
											+'<p style="margin-bottom: 5px;">開課大學:' + resultUnit[k].school_name + '</p>'
											+'<p style="margin-bottom: 5px;">授課教師:' + resultUnit[k].teacher + '老師</p>'
											+'<p style="margin-bottom: 5px;">課程簡介:' + resultUnit[k].course_info + '</p>'
											+'<p style="margin-bottom: 5px;">讚數:' + resultUnit[k].likes.toLocaleString() +'</p>'
											+'</div>'
											+'</div>'
											+'</a></li>'
									);
									unitVideoID++;
									unitArray[k] = new Array(3);
								}
//								for(var i = 0 ;i < result.length;i++){
////						  			console.log(result[i].user_id);
////						  			console.log(result[i].creator);
//						  			for(var j = 0 ; j < 5;j++){
//						  				if(j == 0)videoListArray[i][j] = result[i].courselist_id;
//						  				else if(j == 1)videoListArray[i][j] = result[i].list_name;
//						  				else if(j == 2)videoListArray[i][j] = result[i].user_id;
//						  				else if(j == 3)videoListArray[i][j] = result[i].creator;
//						  				else videoListArray[i][j] = result[i].oorder;
////						  				console.log(videoListArray[i][j]);
//						  			}
//								}
								
					  			
								
					    	},
							error:function(){alert('failed');}
						});
					  
					  
					  
				  })
				  videoListID++;
				  
			  },
			  error:function(){alert('failed');}
		  });
	  }
  });
  
  
  // $('#videoListUL').on("click",".fa.fa-trash-o", function(e){
  //   e.preventDefault(); $(.fa.fa-trash-o).parent().parent().parent('li').remove();
  // })

  //編輯courseList名稱
  $("#editListButton").click(function(e){
    e.preventDefault();
	if($("#edited").val()==""){
		$("#unNewEdit").dialog( "open" );
	}
	else{
		$.ajax({
			url : 'http://localhost:8080/AnyCourse/VideoListServlet.do',
			method : 'POST',
		    data : {
		    	/*
		    	   videoListArray[][0] = courselist_id;
		  			videoListArray[][1] = list_name;
		  			videoListArray[][2] = user_id;
		  			videoListArray[][3] = creator;
		  			videoListArray[][4] = oorder;
		  			videoListArray[][5] = school_name;
		    	 */
		    	"action" : update,//代表要delete
		    	"courselist_id" : videoListArray[checkID-1][0],
		    	"list_name" : $("#edited").val(),
		    	"user_id" : videoListArray[checkID-1][2],
				"creator" : videoListArray[checkID-1][3]
			},
			success:function(result){
				$("#videoListText_"+checkID).text($("#edited").val());
			    $('#edited').val("");
	    	},
			error:function(){alert('failed');}
		});
	}
  });

  //刪courseList的item
  $("#deleteListButton1").click(function(e){
    e.preventDefault();
    
//    console.log(videoListArray[checkID-1][0]);
//    console.log(videoListArray[checkID-1][1]);
//    console.log(videoListArray[checkID-1][2]);
//    console.log(videoListArray[checkID-1][3]);
//    console.log(videoListArray[checkID-1][4]);
    $.ajax({
		url : 'http://localhost:8080/AnyCourse/VideoListServlet.do',
		method : 'POST',
	    data : {
	    	/*
	    	   videoListArray[][0] = courselist_id;
	  			videoListArray[][1] = list_name;
	  			videoListArray[][2] = user_id;
	  			videoListArray[][3] = creator;
	  			videoListArray[][4] = oorder;
	  			videoListArray[][5] = school_name;
	    	 */
	    	"action" : remove,//代表要delete
	    	"courselist_id" : videoListArray[checkID-1][0],
	    	"list_name" : videoListArray[checkID-1][1],
	    	"user_id" : videoListArray[checkID-1][2],
			"creator" : videoListArray[checkID-1][3],
			"oorder" : videoListArray[checkID-1][4]
		},
		success:function(result){
			$("#videoListID_"+checkID).remove();
    		
    	},
		error:function(){alert('failed');}
	});
  });
  //刪除unitVideo
  $("#deleteListButton2").click(function(e){
    e.preventDefault();
    $("#videoItem_"+checkID).remove();
  });
  
  ;
  
  //編輯時沒有輸入新的清單名稱
  $("#unNewEdit").dialog({
	  dialogClass: "dlg-no-close",
      autoOpen: false,
      show: {
        effect: "blind",
        duration: 500
      },
      hide: {
        effect: "fade",
        duration: 500
      },
      buttons: {
          "確定": function() {
              $(this).dialog("close");
          },
      }
      
   });
  //新增時沒有輸入清單名稱
  $("#unAdd").dialog({
	  dialogClass: "dlg-no-close",
      autoOpen: false,
      show: {
        effect: "blind",
        duration: 500
      },
      hide: {
        effect: "fade",
        duration: 500
      },
      buttons: {
          "確定": function() {
              $(this).dialog("close");
          },
      }
      
   });
});
function getID(id){
    checkID = id;
  }

function jumpToPlayerInterface(unit_id,type){
    url = "../PlayerInterface.html?unit_id="+unit_id+"&type="+type;//此處拼接內容
    window.location.href = url;
}