var checkListId;
var checkUnitId;
var videoListArray;//存放清單資料的陣列
var unitArray;//存放影片資料的陣列

$(document).ready(function() {
	//呼叫modal時直接把focus放在modal中的格子
	$('#addModal').on('shown.bs.modal', function () {
	  $('#named').focus();
  	});

  	$('#editModal').on('shown.bs.modal', function () {
  		$('#edited').focus();
  	});
  	//使li可拖拉
  	$(".todo-list").sortable({
  		placeholder: "sort-highlight",
    	handle: ".handle",
    	forcePlaceholderSize: true,
    	zIndex: 999999
  	});

  	checkLogin("../", "../../../");
  	// 載入頁面時，先檢查有沒有 groupId 這個參數
    checkGroupId();
  
  	var videoListId = 1;
  	var unitVideoId = 1;
  	//取得資料庫的資料(courselist&list)
	$.ajax({
		url : ajaxURL+'AnyCourse/GroupVideoListServlet.do',
		method : 'GET',
		cache :false,
		data:{
			action:'getList',
			groupId:get('groupId')
		},
		success:function(result){
			console.log(result);
			// 在 success 時設置好網址
            setGroupUrl();
			videoListArray = new Array(result.length);
	  		for(var i = 0 ;i < result.length;i++){
	  			$('#videoListUL').append('<li id="videoListId_'+videoListId+'" onclick="getListId('+videoListId+')">'
	  					+'<div class="row">'
	                    +'<div class="handle ui-sortable-handle col-xs-1">'
	                    +'<i class="fa fa-ellipsis-v"></i>'
	                    +'</div>'
	                    +'<div class="col-xs-7">'
	                    +'<a href="#unitSection" style="color:black;">'
	                    +'<div style="width:100%;" class="text" id="videoListText_'+videoListId+'">'+result[i].listName+'</div>'
	                    +'</a>'
	                    +'</div>'
	                    +'<div class="btn-group col-xs-1 col-md-1">'
						+'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">' 
						+'<span class="caret caret-up"></span></button>'
						+'<ul class="dropdown-menu dropdown-menu-right" role="menu">'
						+'<li><a data-toggle="modal" data-target="#addToVideoList" onclick="getListId('+videoListId+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>新增至個人課程清單</a></li>'
						+'<li><a data-toggle="modal" data-target="#addToCoursePlanList" onclick="getListId('+videoListId+')" style="cursor:pointer;"> <i class="fa fa-tasks"></i>新增至個人課程計畫</a></li>'
						// +'<li><a data-toggle="modal" data-target="#editModal" onclick="getListId('+videoListId+')" style="cursor:pointer;"> <i class="fa fa-edit""></i>編輯</a></li>'
						// +'<li><a data-toggle="modal" data-target="#deleteModal1" onclick="getListId('+videoListId+')" style="cursor:pointer;"> <i class="fa fa-trash-o""></i>刪除</a></li>'
						+'</ul></div></div></li>');
	  			//把modal設為空
				$('#named').val("");
				//點擊清單，顯示單元影片
				$('#videoListId_'+videoListId).on("click" , function(){
					unitVideoId = 1;
					$.ajax({
						url : ajaxURL+'AnyCourse/GroupVideoListServlet.do',
						method : 'GET',
						cache :false,
						data : {
							action : 'getUnit',
							courselistId : videoListArray[checkListId-1][0],
							groupId:get('groupId')
						},
						success:function(resultUnit){
							console.log(resultUnit);
							//清除原先檢視的unit
							$('#unit li').each(function(){
								$(this).remove();
							}); 
							
							unitArray = new Array(resultUnit.length);
							var img;
							var wid;
							if(window.screen.width >= 1280){
								img = '<img id="img" class="style-scope yt-img-shadow" style="width:200px;"'
								wid = 'width:385px;'
							}
							else if(window.screen.width >= 1024 && window.screen.width < 1280){
								img = '<img id="img" class="style-scope yt-img-shadow" style="width:170px;"'
								wid = 'width:250px;'
							}
							else if(window.screen.width <= 480){
								img = '<img id="img" class="style-scope yt-img-shadow" style="width:100px;"'
								if(window.screen.width > 400)wid = 'width:200px;'
								else if(window.screen.width > 350 && window.screen.width <= 375)wid = 'width:150px;'
								else if(window.screen.width < 350) wid = 'width:110px;'
								else wid = 'width:165px;'
							}
							else{
								if(window.screen.width >= 960){
									img = '<img id="img" class="style-scope yt-img-shadow" style="width:200px;"'
									wid = 'width:410px;'
								}
								else{
									img = '<img id="img" class="style-scope yt-img-shadow" style="width:170px;"'
									wid = 'width:260px;'
								}
							}
							for(var k = 0 ;k < resultUnit.length;k++){
								$("#unit").append(
										'<li id="videoItem_'+unitVideoId+'">'
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
										+'<a class=" waves-effect waves-block" data-toggle="modal" data-target="#addToAssignVideoList" onclick="getUnitId('+unitVideoId+')">'
										+'<i class="ion ion-clipboard"></i>新增至個人課程清單</a>'
										+'</li>'
										+'<li>'
										+'<a class=" waves-effect waves-block" data-toggle="modal" data-target="#addToCoursePlan" onclick="getUnitId('+unitVideoId+')">'
										+'<i class="fa fa-tasks"></i>新增至個人課程計畫</a>'
										+'</li>'
										+'</ul></div>'
										+'<span class="pull-right">'
										+'<i class="fa fa-times" data-toggle="modal" data-target="#deleteModal2"'
										+'onclick="getUnitId('+unitVideoId+')" style="cursor: pointer;"></i>'
										+'</span>'
										+'<a class="list-group-item" onclick="jumpToPlayerInterface('+ resultUnit[k].unitId + ',' + resultUnit[k].videoType+',' + resultUnit[k].courselistId+')">'
										+'<div class="media">'
										+'<div class="media-left">'
										+'<div class="embed-responsive embed-responsive-16by9" style="width:100%; padding-bottom:80%;">'
										+img
										+'src="' + resultUnit[k].videoImgSrc + '">' 
										+'</div></div>'
										+'<div class="media-body">'
										+'<h4 class="unitUi" style="'+wid+'">' + resultUnit[k].unitName + '</h4>'
										+'<p class="unitUi" style="'+wid+'">' + resultUnit[k].schoolName + '</p>'
										+'<p class="unitUi" style="'+wid+'">' + resultUnit[k].teacher + '老師</p>'
										+'<p class="unitUi" style="'+wid+'">' + resultUnit[k].courseInfo + '</p>'
										+'</div></div></a></li>'
								);
								unitVideoId++;
								unitArray[k] = new Array(3);
								unitArray[k][0] = resultUnit[k].unitId;
								unitArray[k][1] = resultUnit[k].userId;
								unitArray[k][2] = resultUnit[k].creator;
							}
					    },
						error:function(){console.log('Display Video failed');}
					});
				});
				videoListId++;
				videoListArray[i] = new Array(6);
				videoListArray[i][0] = result[i].courselistId;
  				videoListArray[i][1] = result[i].listName;
  				videoListArray[i][2] = result[i].userId;
  				videoListArray[i][3] = result[i].creator;
  				videoListArray[i][4] = result[i].oorder;
  				videoListArray[i][5] = result[i].schoolName;
			}
		},
		error:function(){
			// 當 servlet 沒有回傳東西 -> 非該群組成員
          	$('.content-wrapper').first().html('<div><h2 style="text-align:center; padding-top:50px;">很抱歉，您尚未加入該群組</h2></div>');
          	console.log('get groupId error');
		}
	});

	
	//取得該使用者的所有清單(用來放在下拉式選單，讓使用者選擇要加入哪個清單)
	$.ajax({
		url : ajaxURL+'AnyCourse/HomePageServlet.do',
		method : 'POST',
		data:{
			action:'getVideoListName'
		},
		cache :false,
		success:function(result){
			console.log(result);
			for(var i = 0;i < result.length;i++){
				$('#addToAssignVideoListModalBody').append('<option value="'+result[i].courselistId+'">'+result[i].listName+'</option>');
			}
		},
		error:function(){
			console.log("groupVideoList.js getVideoListName error");
		}
	});

  
	//新增courseList
	// $("#addListButton").click(function(){
	// 	if($("#named").val()==""){
	// 		$("#unAdd").dialog( "open" );
	// 	}
	// 	else{
	// 		$.ajax({
	// 			url : ajaxURL+'AnyCourse/GroupVideoListServlet.do',
	// 			method : 'POST', 
	// 			cache :false,
	// 			data : {
	// 				action : 'insert',//代表要insert
	// 				listName : $("#named").val()
	// 			},
	// 			success:function(result){
	// 				$('#videoListUL').append('<li id = "videoListId_'+videoListId+'" onclick="getListId('+videoListId+')">'
	// 	  					+'<div class="row">'
	// 	                    +'<div class="handle ui-sortable-handle col-xs-1 col-md-1">'
	// 	                    +'<i class="fa fa-ellipsis-v"></i>'
	// 	                    +'</div>'
	// 	                    +'<div class="col-xs-7">'
	// 	                    +'<a href="#unitSection" style="color:black;">'
	// 	                    +'<div class="text col-xs-8 col-md-8" id="videoListText_'+videoListId+'">'+$("#named").val()+'</div>'
	// 	                    +'</a>'
	// 	                    +'</div>'
	// 	                    +'<div class="btn-group col-xs-1 col-md-1">'
	// 						+'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">' 
	// 						+'<span class="caret caret-up"></span></button>'
	// 						+'<ul class="dropdown-menu dropdown-menu-right" role="menu">'
	// 						+'<li><a data-toggle="modal" data-target="#addToVideoList" onclick="getListId('+videoListId+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>新增至個人課程清單</a></li>'
	// 					+'<li><a data-toggle="modal" data-target="#addToCoursePlanList" onclick="getListId('+videoListId+')" style="cursor:pointer;"> <i class="fa fa-tasks"></i>新增至個人課程計畫</a></li>'
	// 					// +'<li><a data-toggle="modal" data-target="#editModal" onclick="getListId('+videoListId+')" style="cursor:pointer;"> <i class="fa fa-edit""></i>編輯</a></li>'
	// 					// +'<li><a data-toggle="modal" data-target="#deleteModal1" onclick="getListId('+videoListId+')" style="cursor:pointer;"> <i class="fa fa-trash-o""></i>刪除</a></li>'
	// 						+'</ul></div></div></li>');
	// 			//把modal設為空
	// 			$('#named').val("");
	// 			//點擊清單，顯示單元影片
	// 			$("#videoListUL").on("click","#videoListId_"+videoListId, function(){
	// 				$('#unit li').each(function(){
	// 					$(this).remove();
	// 				});
	// 			})
	// 			videoListId++;
	// 		  },
	// 		  error:function(){console.log('Add VideoList failed');}
	// 		});
	// 	}
	// });

	//編輯courseList名稱
	// $("#editListButton").click(function(e){
	// 	//不等於null代表使用者就是該清單的creator
	// 	if(videoListArray[checkListId-1][2].match(videoListArray[checkListId-1][3]) != null){
	// 		if($("#edited").val()==""){
	// 			$("#unNewEdit").dialog( "open" );
	// 		}
	// 		else{
	// 			$.ajax({
	// 				url : ajaxURL+'AnyCourse/GroupVideoListServlet.do',
	// 				method : 'POST',
	// 				cache :false,
	// 				data : {
				    	
	// 			    	   videoListArray[][0] = courselistId;
	// 			  			videoListArray[][1] = listName;
	// 			  			videoListArray[][2] = userId;
	// 			  			videoListArray[][3] = creator;
	// 			  			videoListArray[][4] = oorder;
	// 			  			videoListArray[][5] = schoolName;
				    	 
	// 			    	action : 'update',//代表要edit
	// 			    	courselistId : videoListArray[checkListId-1][0],
	// 			    	listName : $("#edited").val(),
	// 					creator : videoListArray[checkListId-1][3]
	// 				},
	// 				success:function(result){
	// 					$("#videoListText_"+checkListId).text($("#edited").val());
	// 				    $('#edited').val("");
	// 		    	},
	// 		    	error:function(){console.log('Edit VideoList Name failed');}
	// 			});
	// 		}
	// 	}
	// 	//使用者不是該清單的creator
	// 	else{
	// 		$("#canNotEdit").dialog( "open" );
	// 	}
	// });

	//刪courseList的item
	// $("#deleteListButton1").click(function(e){
	// 	$.ajax({
	// 		url : ajaxURL+'AnyCourse/GroupVideoListServlet.do',
	// 		method : 'POST',
	// 		cache :false,
	// 	    data : {
		    	
	// 	    	   videoListArray[][0] = courselistId;
	// 	  			videoListArray[][1] = listName;
	// 	  			videoListArray[][2] = userId;
	// 	  			videoListArray[][3] = creator;
	// 	  			videoListArray[][4] = oorder;
	// 	  			videoListArray[][5] = schoolName;
		    	 
	// 	    	action : 'remove',//代表要delete
	// 	    	courselistId : videoListArray[checkListId-1][0],
	// 	    	listName : videoListArray[checkListId-1][1],
	// 			creator : videoListArray[checkListId-1][3],
	// 			oorder : videoListArray[checkListId-1][4]
	// 	    },
	// 		success:function(result){
	// 			$("#videoListId_"+checkListId).remove();
	//     	},
	// 		error:function(){console.log('Delete VideoList failed');}
	// 	});
	// });

	//刪除unitVideo
	// $("#deleteListButton2").click(function(e){
	// 	//不等於null代表使用者就是該清單的creator
	// 	if(unitArray[checkUnitId-1][1].match(unitArray[checkUnitId-1][2]) != null){
	// 		$.ajax({
	// 			url : ajaxURL+'AnyCourse/GroupVideoListServlet.do',
	// 			method : 'POST',
	// 			cache :false,
	// 		    data : {
	// 		    	action : 'removeUnitVideo',//代表要delete
	// 		    	courselistId : videoListArray[checkListId-1][0],
	// 		    	unitId : unitArray[checkUnitId-1][0]
	// 		    },
	// 			success:function(result){
	// 				$("#videoItem_"+checkUnitId).remove();
	// 	    	},
	// 			error:function(){console.log('Delete unitVideo failed');}
	// 		});
	// 	}
	// 	else{
	// 		$("#unDelete").dialog( "open" );
	// 	}
	// });
	
	//將共同清單中的個別影片新增至課程計畫
	$('#addToCoursePlanButton,#addToCoursePlanButtonClose').click(function(){
		$.ajax({
			url : ajaxURL+'AnyCourse/HomePageServlet.do',
			method : 'POST',
			cache: false,
			data:{
				action:'addToCoursePlan',
				unitId:unitArray[checkUnitId-1][0],
				creator:unitArray[checkUnitId-1][2]
			},
			error:function(){
				console.log("groupVideoList.js addToCoursePlan Error!");
			}
		})
	});
  
	// 清單整個新增至個人課程計畫
	$('#addToCoursePlanListButton,#addToCoursePlanListCloseButton').click(function(e){
		$.ajax({
			url:ajaxURL+'AnyCourse/GroupVideoListServlet.do',
			method:'POST',
			cache:false,
			data:{
				action:'addToCoursePlanList',
				courselistId:videoListArray[checkListId-1][0],
				creator:videoListArray[checkListId-1][3]
			},
			error:function(e){
				console.log("groupVideoList.js addToCoursePlanList Error!");
			}
		});
	});


	
	// 將共同清單中的完整指定清單加入個人課程清單
	$('#addToVideoListButton,#addToVideoListCloseButton').click(function(){
		$.ajax({
			url:ajaxURL+'AnyCourse/GroupVideoListServlet.do',
			method:'POST',
			cache:false,
			data:{
				action:'addToVideoList',
				courselistId:videoListArray[checkListId-1][0] 
			},
			error:function(e){
				console.log("groupVideoList.js addToVideoList Error!")
			}
		});
	});

	//將共同清單中的影片新增至指定清單
	$('#addToAssignVideoListButton').click(function(){
		$.ajax({
			url : ajaxURL+'AnyCourse/HomePageServlet.do',
			method : 'POST',
			cache: false,
			data:{
				action:'addToVideoList',
				courselistId:$('#addToAssignVideoListModalBody').val(),
				unitId:unitArray[checkUnitId-1][0]
			},
			error:function(){
				console.log("groupVideoList.js addToAssignVideoList error");
			}
		});
	});
	
	$("#unNewEdit,#unAdd,#canNotEdit,#unDelete").dialog({
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
function getListId(id){
	checkListId = id;
}
function getUnitId(id){
	checkUnitId = id;
}
//跳轉頁面
function jumpToPlayerInterface(unitId,type,listId){
    url = "../PlayerInterface.html?unitId="+unitId+"&type="+type+"&listId="+listId;//此處拼接內容
    window.location.href = url;
}




// 取網址列的參數
function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
}

// 設置每個群組內的網址 (ex. 公告、討論區...)
function setGroupUrl()
{
      var groupId = get('groupId');
      $('.tabClass>a').each(function () {
            $(this).attr("href", $(this).attr("href") + '?groupId=' + groupId);
      });
}

// 檢查網址是否沒有 groupId，若沒有則跳轉至首頁
function checkGroupId()
{
      if (get('groupId') == undefined)
            window.location = ajaxURL + 'AnyCourse/AnyCourse/HomePage.html';
}

//////////////////////////////設置拉霸///////////////////////////////////
$('#unit').slimScroll({
    height: '400px;'
});
$('#videoListUL').slimScroll({
    height: '400px;'
});
///////////////////////////////////////////////////////////////////////