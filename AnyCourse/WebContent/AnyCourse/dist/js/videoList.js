$('#unit').slimScroll({
    height: '400px;'
  });
$('#list').slimScroll({
    height: '400px;'
  });

var ajax_url="http://140.121.197.130:8400/";
//var ajax_url="http://localhost:8080/";


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

  checkLogin("../", "../../../");
  
  var videoListID = 1;
  var unitVideoID = 1;
  	//取得資料庫的資料(courselist&list)
	$.ajax({
		url : ajax_url+'AnyCourse/VideoListServlet.do',
		method : 'GET',
		cache :false,
		data:{
			"action":'selectList'//代表要selectList
		},
		success:function(result){
			videoListArray = new Array(result.length);
	  		for(var i = 0 ;i < result.length;i++){
	  			$('#videoListUL').append('<li id = "videoListID_'+videoListID+'" onclick="getID('+videoListID+')" <a href="#unitSection" style="color:black;">'
	                    +'<div class="row">'
	  					+'<div class="handle ui-sortable-handle col-xs-1 col-md-1">'
	                    +'<i class="fa fa-ellipsis-v"></i>'
	                    +'</div>'
	                    +'<div class="text col-xs-8 col-md-8" id="videoListText_'+videoListID+'">'+result[i].list_name+'</div>'
	                    
	                    
	                    +'<div class="btn-group col-xs-1 col-md-1">'
						+'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">' 
						+'<span class="caret caret-up"></span></button><ul class="dropdown-menu dropdown-menu-right" role="menu">'
						+'<li><a href="#" data-toggle="modal" data-target="#addToCoursePlan_List" onclick="getID('+videoListID+')" style="cursor:pointer;"> <i class="fa fa-tasks"></i>新增至課程計畫</a></li>'
						+'<li><a href="#" data-toggle="modal" data-target="#editModal" onclick="getID('+videoListID+')" style="cursor:pointer;"> <i class="fa fa-edit""></i>編輯</a></li>'
						+'<li><a href="#" data-toggle="modal" data-target="#deleteModal1" onclick="getID('+videoListID+')" style="cursor:pointer;"> <i class="fa fa-trash-o""></i>刪除</a></li>'
						+'</ul></div>'
	                    
	                    +'</a></li>');
	  			//把modal設為空
				  $('#named').val("");
				  
				  //點擊清單，顯示單元影片
				  $("#videoListID_"+videoListID).on("click" , function(){
					  unitVideoID = 1;
					  $.ajax({
							url : ajax_url+'AnyCourse/VideoListServlet.do',
							method : 'GET',
							cache :false,
						    data : {
						    	"action" : 'selectUnit',//代表要selectUnit
						    	"school_name" : videoListArray[checkID-1][5],
						    	"list_name" : videoListArray[checkID-1][1]
							},
							success:function(resultUnit){
								unitVideoID = 1;
								//清除原先檢視的unit
								$('#unit li').each(function(){
								    $(this).remove();
								}); 
								unitArray = new Array(resultUnit.length);
								for(var k = 0 ;k < resultUnit.length;k++){
//									console.log(resultUnit[k].videoType);
									$("#unit").append(
											'<li id="videoItem_'+unitVideoID+'"><a href="#unitSection" style="color:black;">'
											+'<span class="handle ui-sortable-handle">' 
											+'<i class="fa fa-ellipsis-h"></i>'
											+'</span>' 
											+'<div class="btn-group" style="top: -5px;">'
											+'<button type="button" class="btn btn-noColor dropdown-toggle"'
											+'data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'
											+'<span class="caret"></span>'
											+'</button>'
											+'<ul class="dropdown-menu">'
											+'<li>'
											+'<a href="#" class=" waves-effect waves-block" data-toggle="modal" data-target="#addToCoursePlan" onclick="getID('+unitVideoID+')">'
											+'<i class="ion ion-clipboard"></i>新增至課程計畫'
											+'</a>'
											+'</li>'
											+'</ul>'
											+'</div>'
											+'<span class="pull-right">'
											+'<i class="fa fa-times" data-toggle="modal" data-target="#deleteModal2"'
											+'onclick="getID('+unitVideoID+')" style="cursor: pointer;"></i>'
											+'</span>'
											+'<a class="list-group-item" onclick="jumpToPlayerInterface('+ resultUnit[k].unit_id + ',' + resultUnit[k].videoType+',' + resultUnit[k].courselist_id+')">'
											
											+'<div class="media">'
											+'<div class="pull-left" style="padding-left: 0px;">'
											+'<div class="embed-responsive embed-responsive-16by9 col-xs-12">'
											+'<img id="img" class="style-scope yt-img-shadow" alt="" width="250"'
											+'src="' + resultUnit[k].video_img_src + '">' 
											+'</div>'
											+'</div>'
											+'<div class="media-body">'
											
											+'<h4 class="unitUi">'
											+'<b>影片名稱:' + resultUnit[k].unit_name + '</b>'
											+'</h4>'
											+'<p class="unitUi">開課大學:' + resultUnit[k].school_name + '</p>'
											+'<p class="unitUi">授課教師:' + resultUnit[k].teacher + '老師</p>'
											+'<p class="unitUi">讚數:' + resultUnit[k].likes.toLocaleString() +'</p>'
											+'<p class="unitUi">課程簡介:' + resultUnit[k].course_info + '</p>'
											+'</div>'
											+'</div>'
											+'</a></li>'
									);
									unitVideoID++;
								}
								for(var z = 0;z < resultUnit.length;z++){
									unitArray[z] = resultUnit[z].unit_id;
//									console.log(unitArray[z]);
								}
					  			
								//影片新增至課程計畫
								$('#addToCoursePlanButton').click(function(){
									
//									alert(checkID);
									$.ajax({
										url : ajax_url+'AnyCourse/HomePageServlet.do',
										method : 'POST',
										cache: false,
										data:{
											action:'addToCoursePlan',
											unit_id:unitArray[checkID-1]
										},
										error:function(){
											console.log("addToCoursePlan Error!");
										}
									})
								});
					    	},
							error:function(){console.log('failed');}
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
		error:function(){console.log('failed');}
	});
  var remove = 1;
  var update = 2;
  
  //新增courseList
  $("#addListButton").click(function(){
	  if($("#named").val()==""){
		 $("#unAdd").dialog( "open" );
	  }
	  else{
		  $.ajax({
			  url : ajax_url+'AnyCourse/VideoListServlet.do',
			  method : 'POST', 
			  cache :false,
			  data : {
				  "action" : 'insert',//代表要insert
				  "list_name" : $("#named").val()
				},
			  success:function(result){
				  $('#videoListUL').append('<li id = "videoListID_'+videoListID+'"'+'>'
	                    +'<span class="handle ui-sortable-handle">'
	                    +'<i class="fa fa-ellipsis-v">'
	                    +'</i><i class="fa fa-ellipsis-v"></i>'
	                    +'</span>'
	                    +'<span class="text" id="videoListText_'+videoListID+'"'+'>'+$("#named").val()+'</span>'
	                    +'<div class="btn-group col-xs-1 col-md-1">'
						+'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">' 
						+'<span class="caret caret-up"></span></button><ul class="dropdown-menu dropdown-menu-right" role="menu">'
						+'<li><a href="#" data-toggle="modal" data-target="#addToCoursePlan_List" onclick="getID('+videoListID+')" style="cursor:pointer;"> <i class="fa fa-tasks"></i>新增至課程計畫</a></li>'
						+'<li><a href="#" data-toggle="modal" data-target="#editModal" onclick="getID('+videoListID+')" style="cursor:pointer;"> <i class="fa fa-edit""></i>編輯</a></li>'
						+'<li><a href="#" data-toggle="modal" data-target="#deleteModal1" onclick="getID('+videoListID+')" style="cursor:pointer;"> <i class="fa fa-trash-o""></i>刪除</a></li>'
						+'</ul></div>'
	                    +'</li>');
				  //把modal設為空
				  $('#named').val("");
				  //點擊清單，顯示單元影片
				  $("#videoListUL").on("click","#videoListID_"+videoListID, function(){
					  unitVideoID = 1;
					  $.ajax({
							url : ajax_url+'AnyCourse/VideoListServlet.do',
							method : 'GET',
							cache :false,
						    data : {
						    	"action" : 'selectUnit',//代表要selectUnit
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
											'<li id="videoItem_'+unitVideoID+'"><a href="#unitSection" style="color:black;">'
											+'<span class="handle ui-sortable-handle">' 
											+'<i class="fa fa-ellipsis-h"></i>'
											+'</span>' 
											+'<div class="btn-group" style="top: -5px;">'
											+'<button type="button" class="btn btn-noColor dropdown-toggle"'
											+'data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'
											+'<span class="caret"></span>'
											+'</button>'
											+'<ul class="dropdown-menu">'
											+'<li>'
											+'<a href="#" class=" waves-effect waves-block" data-toggle="modal" data-target="#addToCoursePlan" onclick="getID('+unitVideoID+')">'
											+'<i class="ion ion-clipboard"></i>新增至課程計畫'
											+'</a>'
											+'</li>'
											+'</ul>'
											+'</div>'
											+'<span class="pull-right">'
											+'<i class="fa fa-times" data-toggle="modal" data-target="#deleteModal2"'
											+'onclick="getID('+unitVideoID+')" style="cursor: pointer;"></i>'
											+'</span>'
											+'<a class="list-group-item" onclick="jumpToPlayerInterface('+ resultUnit[k].unit_id + ',' + resultUnit[k].videoType+')">'
											
											+'<div class="media">'
											+'<div class="pull-left" style="padding-left: 0px;">'
											+'<div class="embed-responsive embed-responsive-16by9 col-xs-12">'
											+'<img id="img" class="style-scope yt-img-shadow" alt="" width="250"'
											+'src="' + resultUnit[k].video_img_src + '">' 
											+'</div>'
											+'</div>'
											+'<div class="media-body">'
											
											+'<h4 class="unitUi">'
											+'<b>影片名稱:' + resultUnit[k].unit_name + '</b>'
											+'</h4>'
											+'<p class="unitUi">開課大學:' + resultUnit[k].school_name + '</p>'
											+'<p class="unitUi">授課教師:' + resultUnit[k].teacher + '老師</p>'
											+'<p class="unitUi">讚數:' + resultUnit[k].likes.toLocaleString() +'</p>'
											+'<p class="unitUi">課程簡介:' + resultUnit[k].course_info + '</p>'
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
							error:function(){console.log('failed');}
						});
					  
					  
					  
				  })
				  videoListID++;
				  
			  },
			  error:function(){console.log('failed');}
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
			url : ajax_url+'AnyCourse/VideoListServlet.do',
			method : 'POST',
			cache :false,
		    data : {
		    	/*
		    	   videoListArray[][0] = courselist_id;
		  			videoListArray[][1] = list_name;
		  			videoListArray[][2] = user_id;
		  			videoListArray[][3] = creator;
		  			videoListArray[][4] = oorder;
		  			videoListArray[][5] = school_name;
		    	 */
		    	"action" : 'update',//代表要delete
		    	"courselist_id" : videoListArray[checkID-1][0],
		    	"list_name" : $("#edited").val(),
		    	"user_id" : videoListArray[checkID-1][2],
				"creator" : videoListArray[checkID-1][3]
			},
			success:function(result){
				$("#videoListText_"+checkID).text($("#edited").val());
			    $('#edited').val("");
	    	},
			error:function(){console.log('failed');}
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
		url : ajax_url+'AnyCourse/VideoListServlet.do',
		method : 'POST',
		cache :false,
	    data : {
	    	/*
	    	   videoListArray[][0] = courselist_id;
	  			videoListArray[][1] = list_name;
	  			videoListArray[][2] = user_id;
	  			videoListArray[][3] = creator;
	  			videoListArray[][4] = oorder;
	  			videoListArray[][5] = school_name;
	    	 */
	    	"action" : 'remove',//代表要delete
	    	"courselist_id" : videoListArray[checkID-1][0],
	    	"list_name" : videoListArray[checkID-1][1],
	    	"user_id" : videoListArray[checkID-1][2],
			"creator" : videoListArray[checkID-1][3],
			"oorder" : videoListArray[checkID-1][4]
		},
		success:function(result){
			$("#videoListID_"+checkID).remove();
    		
    	},
		error:function(){console.log('failed');}
	});
  });
  //刪除unitVideo
  $("#deleteListButton2").click(function(e){
    e.preventDefault();
    $("#videoItem_"+checkID).remove();
  });
  
  	//清單整個新增至課程計畫
	$('#addToCoursePlanButton_List').click(function(e){
		e.preventDefault();
		$.ajax({
			url : ajax_url+'AnyCourse/HomePageServlet.do',
			method : 'POST',
			cache: false,
			data:{
				action:'addToCoursePlan_List',
				courselist_id:videoListArray[checkID-1][0]
			},
			error:function(e){
				console.log("addToCoursePlan_List Error!");
			}
		})
	});
	
	
  
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

function jumpToPlayerInterface(unit_id,type,list_id){
    url = "../PlayerInterface.html?unit_id="+unit_id+"&type="+type+"&list_id="+list_id;//此處拼接內容
    window.location.href = url;
}