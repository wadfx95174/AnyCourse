document.write("<script type='text/javascript' src='dist/js/swiper.min.js'></script>");

$(document).ready(function(){
	checkLogin("pages/", "");
	//重複利用的字串，尾巴
	var str = '</div></div><div class="card-footer"><div class="btn-group show-on-hover dropup">'
	+'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">新增至' 
	+'<span class="caret caret-up"></span></button><ul class="dropdown-menu drop-up" role="menu">'
	+'<li><a href="#"> <i class="ion ion-clipboard"></i>新增至課程清單</a></li>'
	+'<li><a href="#"> <i class="fa fa-tasks"></i>新增至課程計畫</a></li>'
	+'</ul></div></div></div></div>';
	//swiper-button
	var str2 = '</div><div class="swiper-button-next"></div><div class="swiper-button-prev"></div>'
		+'</div></div></section>'
	
	$.ajax({
		url : 'http://localhost:8080/AnyCourse/HomePageServlet.do',
		method : 'GET',
		success:function(result){
//			console.log(Object.keys(result[0]).length);
			for(var i = 0;i < result.length ; i++){
				
				//推薦影片
				if(result[i][0].type == 1){
					$('.content-wrapper').append(
							'<section class="content-header">'
							+'<h4><b>推薦影片</b></h4>'
							+'</section>'
							+'<section class="content">'
							+'<div class="row">'
							+'<div class="swiper-container">'
							+'<div class="swiper-wrapper" id="courseList_1">'
							+str2);
					//亂數決定順序
					var temp;
					//推薦10個影片
					//Object.keys(result[i]).length
					for(var j = 0 ;j < Object.keys(result[i]).length;j++){
//						console.log(result[i][j]);
						temp = Math.floor(Math.random()*Object.keys(result[i]).length);
						var course_info = result[i][temp];
						result[i][temp] = result[i][j];
						result[i][j] = course_info;
					}
					for(var j = 0 ;j < 10;j++){ 
						$('#courseList_1').append(
							'<div class="swiper-slide col-sm-5ths col-xs-5ths" style="padding:5px;">'
							+'<div class="card">'
							+'<figure class="snip1492">'
							+'<div class="embed-responsive embed-responsive-16by9" onclick="jumpToPlayerInterface('+ result[i][j].unit_id + ',' + result[i][j].video_type+')">'
							+'<img id="img" class="cardWidth style-scope yt-img-shadow" width="300px;" alt="" src="'+result[i][j].video_img_src+'">'
							+'</div>'
							+'<i class="fa fa-play"></i>'
							+'<a href="#" onclick="jumpToPlayerInterface('+ result[i][j].unit_id + ',' + result[i][j].video_type+')"></a>'
							+'</figure>'
							
							+'<div class="card-block" style="padding-top:0px;" onclick="jumpToPlayerInterface('+ result[i][j].unit_id + ',' + result[i][j].video_type+')">'
							+'<h4 class="card-title" >' + result[i][j].unit_name + '</h4>'
							+'<div class="card-text">清單名稱:'+ result[i][j].list_name +'</div>'
							+'<div class="meta">'
							+'<a>'+ result[i][j].school_name +'&nbsp;&nbsp;'+ result[i][j].teacher +'教師</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>'+result[i][j].unitLikes+'</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>類型 : 影片</a>'
							+str
						);
						$('.snip1492').tooltip({title:result[i][j].list_name , scontainer: "body", placement:"bottom", animation: true}); 
					}
				}
				//推薦清單
				else if(result[i][0].type == 2){
					$('.content-wrapper').append(
							'<section class="content-header">'
							+'<h4><b>推薦清單</b></h4>'
							+'</section>'
							+'<section class="content">'
							+'<div class="row">'
							+'<div class="swiper-container">'
							+'<div class="swiper-wrapper" id="courseList_2">'
							+str2);
					//亂數決定順序
					var temp;
					for(var j = 0 ;j < Object.keys(result[i]).length;j++){
						temp = Math.floor(Math.random()*Object.keys(result[i]).length);
						var course_info = result[i][temp];
						result[i][temp] = result[i][j];
						result[i][j] = course_info;
					}
					for(var j = 0 ;j < 10;j++){
						$('#courseList_2').append(
							'<div class="swiper-slide col-sm-5ths col-xs-5ths" style="padding:5px;">'
							+'<div class="card">'
							+'<figure class="snip1492">'
							+'<div class="embed-responsive embed-responsive-16by9" onclick="jumpToPlayerInterfaceWithList('+ result[i][j].unit_id + ',' + result[i][j].video_type +',' + result[i][j].courselist_id + ')">'
							+'<img id="img" class="cardWidth style-scope yt-img-shadow" width="300px;" alt="" src="'+result[i][j].video_img_src+'">'
							+'</div>'
							+'<i class="fa fa-play"></i>'
							+'<a href="#" onclick="jumpToPlayerInterfaceWithList('+ result[i][j].unit_id + ',' + result[i][j].video_type +',' + result[i][j].courselist_id + ')"></a>'
							+'</figure>'
							
							+'<div class="card-block" style="padding-top:0px;" onclick="jumpToPlayerInterfaceWithList('+ result[i][j].unit_id + ',' + result[i][j].video_type +',' + result[i][j].courselist_id + ')">'
							+'<h4 class="card-title" >' + result[i][j].list_name + '</h4>'
							+'<div class="meta">'
							+'<a>'+ result[i][j].school_name +'&nbsp;&nbsp;'+ result[i][j].teacher +'教師</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>'+result[i][j].listLikes+'</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>類型 : 清單</a>'
							+str
						);
						$('.snip1492').tooltip({title:result[i][j].list_name , scontainer: "body", placement:"bottom", animation: true}); 
					}
				}
				//課程清單
				else if(result[i][0].type == 3){
					$('.content-wrapper').append(
							'<section class="content-header">'
							+'<h4><b>課程清單</b></h4>'
							+'</section>'
							+'<section class="content">'
							+'<div class="row">'
							+'<div class="swiper-container">'
							+'<div class="swiper-wrapper" id="courseList_3">'
							+str2);
					//亂數決定順序
					var temp;
					for(var j = 0 ;j < result[i][0].num;j++){
						temp = Math.floor(Math.random()*result[i][0].num);
						var course_info = result[i][temp];
						result[i][temp] = result[i][j];
						result[i][j] = course_info;
					}
					for(var j = 0 ;j < result[i][0].num;j++){
						
						$('#courseList_3').append(
							'<div class="swiper-slide col-sm-5ths col-xs-5ths" style="padding:5px;">'
							+'<div class="card">'
							+'<figure class="snip1492">'
							+'<div class="embed-responsive embed-responsive-16by9" onclick="jumpToPlayerInterfaceWithList('+ result[i][j].unit_id + ',' + result[i][j].video_type +',' + result[i][j].courselist_id + ')">'
							+'<img id="img" class="cardWidth style-scope yt-img-shadow" width="300px;" alt="" src="'+result[i][j].video_img_src+'">'
							+'</div>'
							+'<i class="fa fa-play"></i>'
							+'<a href="#" onclick="jumpToPlayerInterfaceWithList('+ result[i][j].unit_id + ',' + result[i][j].video_type +',' + result[i][j].courselist_id + ')"></a>'
							+'</figure>'
							
							+'<div class="card-block" style="padding-top:0px;" onclick="jumpToPlayerInterfaceWithList('+ result[i][j].unit_id + ',' + result[i][j].video_type +',' + result[i][j].courselist_id + ')">'
							+'<h4 class="card-title" >' + result[i][j].list_name + '</h4>'
							+'<div class="meta">'
							+'<a>'+ result[i][j].school_name +'&nbsp;&nbsp;'+ result[i][j].teacher +'教師</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>'+result[i][j].listLikes+'</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>類型 : 清單</a>'
							+str
						);
						$('.snip1492').tooltip({title:result[i][j].list_name , scontainer: "body", placement:"bottom", animation: true}); 
					}
				}
				//想要觀看
				else if(result[i][0].type == 4){
					$('.content-wrapper').append(
							'<section class="content-header">'
							+'<h4><b>想要觀看</b></h4>'
							+'</section>'
							+'<section class="content">'
							+'<div class="row">'
							+'<div class="swiper-container">'
							+'<div class="swiper-wrapper" id="courseList_4">'
							+str2);
					//亂數決定順序
					var temp;
					for(var j = 0 ;j < result[i][0].oorder;j++){
						temp = Math.floor(Math.random()*result[i][0].oorder);
						var course_info = result[i][temp];
						result[i][temp] = result[i][j];
						result[i][j] = course_info;
					}
					for(var j = 0 ;j < result[i][0].oorder;j++){
						$('#courseList_4').append(
							'<div class="swiper-slide col-sm-5ths col-xs-5ths" style="padding:5px;">'
							+'<div class="card">'
							+'<figure class="snip1492">'
							+'<div class="embed-responsive embed-responsive-16by9" onclick="jumpToPlayerInterface('+ result[i][j].unit_id + ',' + result[i][j].video_type+')">'
							+'<img id="img" class="cardWidth style-scope yt-img-shadow" width="300px;" alt="" src="'+result[i][j].video_img_src+'">'
							+'</div>'
							+'<i class="fa fa-play"></i>'
							+'<a href="#" onclick="jumpToPlayerInterface('+ result[i][j].unit_id + ',' + result[i][j].video_type+')"></a>'
							+'</figure>'
							
							+'<div class="card-block" style="padding-top:0px;" onclick="jumpToPlayerInterface('+ result[i][j].unit_id + ',' + result[i][j].video_type+')">'
							+'<h4 class="card-title" >' + result[i][j].unit_name + '</h4>'
							+'<div class="card-text">清單名稱:'+ result[i][j].list_name +'</div>'
							+'<div class="meta">'
							+'<a>'+ result[i][j].school_name +'&nbsp;&nbsp;'+ result[i][j].teacher +'教師</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>'+result[i][j].unitLikes+'</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>類型 : 影片</a>'
							+str
						);
						$('.snip1492').tooltip({title:result[i][j].list_name , scontainer: "body", placement:"bottom", animation: true}); 
					}
				}
				//正在觀看
				else if(result[i][0].type == 5){
					$('.content-wrapper').append(
							'<section class="content-header">'
							+'<h4><b>正在觀看</b></h4>'
							+'</section>'
							+'<section class="content">'
							+'<div class="row">'
							+'<div class="swiper-container">'
							+'<div class="swiper-wrapper" id="courseList_5">'
							+str2);
					//亂數決定順序
					var temp;
					for(var j = 0 ;j < result[i][0].oorder;j++){
						temp = Math.floor(Math.random()*result[i][0].oorder);
						var course_info = result[i][temp];
						result[i][temp] = result[i][j];
						result[i][j] = course_info;
					}
					for(var j = 0 ;j < result[i][0].oorder;j++){
						$('#courseList_5').append(
							'<div class="swiper-slide col-sm-5ths col-xs-5ths" style="padding:5px;">'
							+'<div class="card">'
							+'<figure class="snip1492">'
							+'<div class="embed-responsive embed-responsive-16by9" onclick="jumpToPlayerInterface('+ result[i][j].unit_id + ',' + result[i][j].video_type+')">'
							+'<img id="img" class="cardWidth style-scope yt-img-shadow" width="300px;" alt="" src="'+result[i][j].video_img_src+'">'
							+'</div>'
							+'<i class="fa fa-play"></i>'
							+'<a href="#" onclick="jumpToPlayerInterface('+ result[i][j].unit_id + ',' + result[i][j].video_type+')"></a>'
							+'</figure>'
							
							+'<div class="card-block" style="padding-top:0px;" onclick="jumpToPlayerInterface('+ result[i][j].unit_id + ',' + result[i][j].video_type+')">'
							+'<h4 class="card-title" >' + result[i][j].unit_name + '</h4>'
							+'<div class="card-text">清單名稱:'+ result[i][j].list_name +'</div>'
							+'<div class="meta">'
							+'<a>'+ result[i][j].school_name +'&nbsp;&nbsp;'+ result[i][j].teacher +'教師</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>'+result[i][j].unitLikes+'</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>類型 : 影片</a>'
							+str
						);
						$('.snip1492').tooltip({title:result[i][j].list_name , scontainer: "body", placement:"bottom", animation: true}); 
					}
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
			}
//			videoListArray = new Array(result.length);
//	  		for(var i = 0 ;i < result.length;i++){
//	  			
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
//							url : 'http://localhost:8080/AnyCourse/HomePageServlet.do',
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
  	},
		error:function(){alert('failed');}
	});
	
	  
	 
 }());
	
function jumpToPlayerInterface(unit_id,type){
    url = "pages/PlayerInterface.html?unit_id="+unit_id+"&type="+type;//此處拼接內容
    window.location.href = url;
}
function jumpToPlayerInterfaceWithList(unit_id,type,list_id){
	url = "pages/PlayerInterface.html?unit_id="+unit_id+"&type="+type+"&list_id="+list_id;
	window.location.href = url;
}


///////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////


