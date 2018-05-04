document.write("<script type='text/javascript' src='dist/js/swiper.min.js'></script>");

var homePageList;
var checkID;
$(document).ready(function(){
	homePageList = new Array();
	checkLogin("pages/", "../");
	
	//swiper-button
	var str2 = '</div><div class="swiper-button-next"></div><div class="swiper-button-prev"></div>'
		+'</div></div></section>'
	var homePageListID = 1;
	$.ajax({
		url : 'http://localhost:8080/AnyCourse/HomePageServlet.do',
		method : 'GET',
		cache :false,
		success:function(result){
//			console.log(result);
//			console.log(result[0][0].type);
//			console.log(result.length);
//			console.log(result.length*result.)
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
							'<div class="swiper-slide col-sm-5ths col-xs-5ths" style="padding:5px;" id="homePageListID_'+ homePageListID +'">'
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
							+'<div class="card-text">清單:'+ result[i][j].list_name +'</div>'
							+'<div class="meta">'
							+'<a>'+ result[i][j].school_name +'&nbsp;&nbsp;'+ result[i][j].teacher +'教師</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>'+result[i][j].unitLikes+'</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>類型 : 影片</a>'
							+'</div>'
							+'</div><div class="card-footer"><div class="btn-group show-on-hover dropup">'
							+'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">新增至' 
							+'<span class="caret caret-up"></span></button><ul class="dropdown-menu drop-up" role="menu">'
							+'<li><a data-toggle="modal" data-target="#addToCourseList" onclick="getID('+homePageListID+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>新增至課程清單</a></li>'
							+'<li><a data-toggle="modal" data-target="#addToCoursePlan" onclick="getID('+homePageListID+')" style="cursor:pointer;"> <i class="fa fa-tasks"></i>新增至課程計畫</a></li>'
							+'</ul></div></div></div></div>'
						);
						$('.snip1492').tooltip({title:result[i][j].list_name , scontainer: "body", placement:"bottom", animation: true}); 
						homePageList[homePageListID] = new Array(2);
						homePageList[homePageListID][0] = result[i][j].unit_id;
						homePageList[homePageListID][1] = result[i][j].courselist_id;
//						console.log(homePageList);
//						console.log(homePageList[1][1]);
						homePageListID++;
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
							'<div class="swiper-slide col-sm-5ths col-xs-5ths" style="padding:5px;" id="homePageListID_'+ homePageListID +'">'
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
							+'</div></div><div class="card-footer"><div class="btn-group show-on-hover dropup">'
							+'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">新增至' 
							+'<span class="caret caret-up"></span></button><ul class="dropdown-menu drop-up" role="menu">'
							+'<li><a href="#" data-toggle="modal" data-target="#addToCourseList_List" onclick="getID('+homePageListID+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>新增至課程清單</a></li>'
							+'<li><a href="#" data-toggle="modal" data-target="#addToCoursePlan_List" onclick="getID('+homePageListID+')" style="cursor:pointer;"> <i class="fa fa-tasks"></i>新增至課程計畫</a></li>'
							+'</ul></div></div></div></div>'
						);
						$('.snip1492').tooltip({title:result[i][j].list_name , scontainer: "body", placement:"bottom", animation: true}); 
						homePageList[homePageListID] = new Array(2);
						homePageList[homePageListID][0] = result[i][j].unit_id;
						homePageList[homePageListID][1] = result[i][j].courselist_id;
						homePageListID++;
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
//					console.log(result[2][0].oorder);
					for(var j = 0 ;j < result[i][0].oorder;j++){
						temp = Math.floor(Math.random()*result[i][0].oorder);
						var course_info = result[i][temp];
						result[i][temp] = result[i][j];
						result[i][j] = course_info;
					}
					for(var j = 0 ;j < result[i][0].oorder;j++){
						
						$('#courseList_3').append(
							'<div class="swiper-slide col-sm-5ths col-xs-5ths" style="padding:5px;" id="homePageListID_'+ homePageListID +'">'
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
							+'</div></div><div class="card-footer"><div class="btn-group show-on-hover dropup">'
							+'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">新增至' 
							+'<span class="caret caret-up"></span></button><ul class="dropdown-menu drop-up" role="menu">'
							+'<li><a href="#" data-toggle="modal" data-target="#addToCourseList_List" onclick="getID('+homePageListID+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>新增至課程清單</a></li>'
							+'<li><a href="#" data-toggle="modal" data-target="#addToCoursePlan_List" onclick="getID('+homePageListID+')" style="cursor:pointer;"> <i class="fa fa-tasks"></i>新增至課程計畫</a></li>'
							+'</ul></div></div></div></div>'
						);
						$('.snip1492').tooltip({title:result[i][j].list_name , scontainer: "body", placement:"bottom", animation: true}); 
						homePageList[homePageListID] = new Array(2);
						homePageList[homePageListID][0] = result[i][j].unit_id;
						homePageList[homePageListID][1] = result[i][j].courselist_id;
						homePageListID++;
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
//					console.log(result[i][0].oorder);
					for(var j = 0 ;j < result[i][0].oorder;j++){
						temp = Math.floor(Math.random()*result[i][0].oorder);
						var course_info = result[i][temp];
						result[i][temp] = result[i][j];
						result[i][j] = course_info;
					}
					for(var j = 0 ;j < result[i][0].oorder;j++){
						$('#courseList_4').append(
							'<div class="swiper-slide col-sm-5ths col-xs-5ths" style="padding:5px;" id="homePageListID_'+ homePageListID +'">'
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
							+'<div class="card-text">清單:'+ result[i][j].list_name +'</div>'
							+'<div class="meta">'
							+'<a>'+ result[i][j].school_name +'&nbsp;&nbsp;'+ result[i][j].teacher +'教師</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>'+result[i][j].unitLikes+'</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>類型 : 影片</a>'
							+'</div></div><div class="card-footer"><div class="btn-group show-on-hover dropup">'
							+'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">新增至' 
							+'<span class="caret caret-up"></span></button><ul class="dropdown-menu drop-up" role="menu">'
							+'<li><a href="#" data-toggle="modal" data-target="#addToCourseList" onclick="getID('+homePageListID+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>新增至課程清單</a></li>'
							+'<li><a href="#" data-toggle="modal" data-target="#addToCoursePlan" onclick="getID('+homePageListID+')" style="cursor:pointer;"> <i class="fa fa-tasks"></i>新增至課程計畫</a></li>'
							+'</ul></div></div></div></div>'
						);
						$('.snip1492').tooltip({title:result[i][j].list_name , scontainer: "body", placement:"bottom", animation: true}); 
						homePageList[homePageListID] = new Array(2);
						homePageList[homePageListID][0] = result[i][j].unit_id;
						homePageList[homePageListID][1] = result[i][j].courselist_id;
						homePageListID++;
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
							'<div class="swiper-slide col-sm-5ths col-xs-5ths" style="padding:5px;" id="homePageListID_'+ homePageListID +'">'
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
							+'<div class="card-text">清單:'+ result[i][j].list_name +'</div>'
							+'<div class="meta">'
							+'<a>'+ result[i][j].school_name +'&nbsp;&nbsp;'+ result[i][j].teacher +'教師</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>'+result[i][j].unitLikes+'</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>類型 : 影片</a>'
							+'</div></div><div class="card-footer"><div class="btn-group show-on-hover dropup">'
							+'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">新增至' 
							+'<span class="caret caret-up"></span></button><ul class="dropdown-menu drop-up" role="menu">'
							+'<li><a href="#" data-toggle="modal" data-target="#addToCourseList" onclick="getID('+homePageListID+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>新增至課程清單</a></li>'
							+'<li><a href="#" data-toggle="modal" data-target="#addToCoursePlan" onclick="getID('+homePageListID+')" style="cursor:pointer;"> <i class="fa fa-tasks"></i>新增至課程計畫</a></li>'
							+'</ul></div></div></div></div>'
						);
						$('.snip1492').tooltip({title:result[i][j].list_name , scontainer: "body", placement:"bottom", animation: true}); 
						homePageList[homePageListID] = new Array(2);
						homePageList[homePageListID][0] = result[i][j].unit_id;
						homePageList[homePageListID][1] = result[i][j].courselist_id;
						homePageListID++;
					}
				}
				//台大
				else if(result[i][0].type == 6){
					
				}
				//清大
				else if(result[i][0].type == 7){
					
				}
				//交大
				else if(result[i][0].type == 8){
	
				}
				//成大
				else if(result[i][0].type == 9){
	
				}
				//政大
				else if(result[i][0].type == 10){
	
				}
//				console.log(homePageList[1]);
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
			//因為放在外面的話跟初始化首頁的ajax(就是這個外面的ajax)會同時跑，這個會跑比較快，所以抓不到陣列
			console.log(homePageList);
			//影片新增至課程計畫
			$('#addToCoursePlanButton').click(function(){
				
//				alert(checkID);
				$.ajax({
					url : 'http://localhost:8080/AnyCourse/HomePageServlet.do',
					method : 'POST',
					cache: false,
					data:{
						action:'addToCoursePlan',
						unit_id:homePageList[checkID][0]
					},
					error:function(){
						console.log("addToCoursePlan Error!");
					}
				})
			});
			console.log(homePageList);
			//清單整個新增至課程計畫
			$('#addToCoursePlanButton_List').click(function(e){
				e.preventDefault();
				console.log(homePageList[checkID][0]);
				$.ajax({
					url : 'http://localhost:8080/AnyCourse/HomePageServlet.do',
					method : 'POST',
					cache: false,
					data:{
						action:'addToCoursePlan_List',
						unit_id:homePageList[checkID][0],
						courselist_id:homePageList[checkID][1]
					},
					error:function(e){
						console.log("addToCoursePlan_List Error!");
					}
				})
			});
  	},
		error:function(){}
	});
	
	
	
	 
 }());
//跳轉至播放介面
function jumpToPlayerInterface(unit_id,type){
    url = "pages/PlayerInterface.html?unit_id="+unit_id+"&type="+type;//此處拼接內容
    window.location.href = url;
}
//跳轉至播放介面，且是清單
function jumpToPlayerInterfaceWithList(unit_id,type,list_id){
	url = "pages/PlayerInterface.html?unit_id="+unit_id+"&type="+type+"&list_id="+list_id;
	window.location.href = url;
}
//才能知道要對哪個ID做動作
function getID(id){
    checkID = id;
}

///////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////

window.onload = function() {
    if(!window.location.hash) {
        window.location = window.location + '#loaded';
        window.location.reload();
    }
}
