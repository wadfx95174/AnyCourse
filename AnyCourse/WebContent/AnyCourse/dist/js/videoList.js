//////////////localhost用來測試、IP那個用來部屬再tomcat///////////////////////
var ajaxURL="http://140.121.197.130:8400/";
//var ajaxURL="http://localhost:8080/";
///////////////////////////////////////////////////////////////////////
//////////////////////////////設置拉霸///////////////////////////////////
$('#unit').slimScroll({
    height: '400px;'
});
$('#list').slimScroll({
    height: '400px;'
});
///////////////////////////////////////////////////////////////////////

var checkID;
var videoListArray;//存放清單資料的陣列
var unitArray;//存放影片資料的陣列

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
		url : ajaxURL+'AnyCourse/VideoListServlet.do',
		method : 'GET',
		cache :false,
		data:{
			action:'selectList'//代表要selectList
		},
		success:function(result){
			videoListArray = new Array(result.length);
	  		for(var i = 0 ;i < result.length;i++){
	  			$('#videoListUL').append('<li id = "videoListID_'+videoListID+'" onclick="getID('+videoListID+')" <a href="#unitSection" style="color:black;">'
	                    +'<div class="row">'
	  					+'<div class="handle ui-sortable-handle col-xs-1 col-md-1">'
	                    +'<i class="fa fa-ellipsis-v"></i>'
	                    +'</div>'
	                    +'<div class="text col-xs-8 col-md-8" id="videoListText_'+videoListID+'">'+result[i].listName+'</div>'
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
						url : ajaxURL+'AnyCourse/VideoListServlet.do',
						method : 'GET',
						cache :false,
						data : {
							action : 'selectUnit',//代表要selectUnit
							schoolName : videoListArray[checkID-1][5],
						    listName : videoListArray[checkID-1][1]
						},
						success:function(resultUnit){
							//清除原先檢視的unit
							$('#unit li').each(function(){
								$(this).remove();
							}); 
							unitArray = new Array(resultUnit.length);
							for(var k = 0 ;k < resultUnit.length;k++){
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
										+'</a></li></ul></div>'
										+'<span class="pull-right">'
										+'<i class="fa fa-times" data-toggle="modal" data-target="#deleteModal2"'
										+'onclick="getID('+unitVideoID+')" style="cursor: pointer;"></i>'
										+'</span>'
										+'<a class="list-group-item" onclick="jumpToPlayerInterface('+ resultUnit[k].unitID + ',' + resultUnit[k].videoType+',' + resultUnit[k].courselistID+')">'
										+'<div class="media">'
										+'<div class="pull-left" style="padding-left: 0px;">'
										+'<div class="embed-responsive embed-responsive-16by9 col-xs-12">'
										+'<img id="img" class="style-scope yt-img-shadow" alt="" width="250"'
										+'src="' + resultUnit[k].videoImgSrc + '">' 
										+'</div></div>'
										+'<div class="media-body">'
										+'<h4 class="unitUi">'
										+'<b>影片名稱:' + resultUnit[k].unitName + '</b>'
										+'</h4>'
										+'<p class="unitUi">開課大學:' + resultUnit[k].schoolName + '</p>'
										+'<p class="unitUi">授課教師:' + resultUnit[k].teacher + '老師</p>'
										+'<p class="unitUi">讚數:' + resultUnit[k].likes.toLocaleString() +'</p>'
										+'<p class="unitUi">課程簡介:' + resultUnit[k].courseInfo + '</p>'
										+'</div></div></a></li>'
								);
								unitVideoID++;
							}
							for(var z = 0;z < resultUnit.length;z++){
								unitArray[z] = resultUnit[z].unitID;
							}
							//影片新增至課程計畫
							$('#addToCoursePlanButton').click(function(){
								$.ajax({
									url : ajaxURL+'AnyCourse/HomePageServlet.do',
									method : 'POST',
									cache: false,
									data:{
										action:'addToCoursePlan',
										unitID:unitArray[checkID-1]
									},
									error:function(){
										console.log("addToCoursePlan Error!");
									}
								})
							});
					    },
						error:function(){console.log('Display Video failed');}
					});
				});
				videoListID++;
				videoListArray[i] = new Array(6);
			}
	  		for(var i = 0 ;i < result.length;i++){
	  			for(var j = 0 ; j < 6;j++){
	  				if(j == 0)videoListArray[i][j] = result[i].courselistID;
	  				else if(j == 1)videoListArray[i][j] = result[i].listName;
	  				else if(j == 2)videoListArray[i][j] = result[i].userID;
	  				else if(j == 3)videoListArray[i][j] = result[i].creator;
	  				else if(j == 4)videoListArray[i][j] = result[i].oorder;
	  				else videoListArray[i][j] = result[i].schoolName;
	  			}
	  		}
		},
		error:function(){console.log('Display VideoList failed');}
	});
  
	//新增courseList
	$("#addListButton").click(function(){
		if($("#named").val()==""){
			$("#unAdd").dialog( "open" );
		}
		else{
			$.ajax({
				url : ajaxURL+'AnyCourse/VideoListServlet.do',
				method : 'POST', 
				cache :false,
				data : {
					action : 'insert',//代表要insert
					listName : $("#named").val()
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
					$('#unit li').each(function(){
						$(this).remove();
					});
				})
				videoListID++;
			  },
			  error:function(){console.log('Add VideoList failed');}
			});
		}
	});

	//編輯courseList名稱
	$("#editListButton").click(function(e){
		//不等於null代表使用者就是該清單的creator
		if(videoListArray[checkID-1][2].match(videoListArray[checkID-1][3]) != null){
			if($("#edited").val()==""){
				$("#unNewEdit").dialog( "open" );
			}
			else{
				$.ajax({
					url : ajaxURL+'AnyCourse/VideoListServlet.do',
					method : 'POST',
					cache :false,
					data : {
				    	/*
				    	   videoListArray[][0] = courselistID;
				  			videoListArray[][1] = listName;
				  			videoListArray[][2] = userID;
				  			videoListArray[][3] = creator;
				  			videoListArray[][4] = oorder;
				  			videoListArray[][5] = schoolName;
				    	 */
				    	action : 'update',//代表要delete
				    	courselistID : videoListArray[checkID-1][0],
				    	listName : $("#edited").val(),
						creator : videoListArray[checkID-1][3]
					},
					success:function(result){
						$("#videoListText_"+checkID).text($("#edited").val());
					    $('#edited').val("");
			    	},
			    	error:function(){console.log('Edit VideoList Name failed');}
				});
			}
		}
		//使用者不是該清單的creator
		else{
			$("#canNotEdit").dialog( "open" );
		}
	});

	//刪courseList的item
	$("#deleteListButton1").click(function(e){
		$.ajax({
			url : ajaxURL+'AnyCourse/VideoListServlet.do',
			method : 'POST',
			cache :false,
		    data : {
		    	/*
		    	   videoListArray[][0] = courselistID;
		  			videoListArray[][1] = listName;
		  			videoListArray[][2] = userID;
		  			videoListArray[][3] = creator;
		  			videoListArray[][4] = oorder;
		  			videoListArray[][5] = schoolName;
		    	 */
		    	action : 'remove',//代表要delete
		    	courselistID : videoListArray[checkID-1][0],
		    	listName : videoListArray[checkID-1][1],
				creator : videoListArray[checkID-1][3],
				oorder : videoListArray[checkID-1][4]
		    },
			success:function(result){
				$("#videoListID_"+checkID).remove();
	    	},
			error:function(){console.log('Delete VideoList failed');}
		});
	});
	//刪除unitVideo
	$("#deleteListButton2").click(function(e){
		$("#videoItem_"+checkID).remove();
	});
  
	//清單整個新增至課程計畫
	$('#addToCoursePlanButton_List').click(function(e){
		$.ajax({
			url : ajaxURL+'AnyCourse/HomePageServlet.do',
			method : 'POST',
			cache: false,
			data:{
				action:'addToCoursePlan_List',
				courselistID:videoListArray[checkID-1][0]
			},
			error:function(e){
				console.log("addToCoursePlan_List Error!");
			}
		})
	});
	
	$("#unNewEdit,#unAdd,#canNotEdit").dialog({
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
//跳轉頁面
function jumpToPlayerInterface(unitID,type,listID){
    url = "../PlayerInterface.html?unitID="+unitID+"&type="+type+"&listID="+listID;//此處拼接內容
    window.location.href = url;
}