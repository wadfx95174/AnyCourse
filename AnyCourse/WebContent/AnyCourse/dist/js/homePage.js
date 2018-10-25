/////////////////////////引用swiper這個套件///////////////////////////////
document.write("<script type='text/javascript' src='dist/js/swiper.min.js'></script>");


var homePageList;//Array，用來存影片、清單的資訊
var checkId;//用來存使用者點擊哪個物件的Id，用這個Id與homePageList這個Array搭配使用，就可以對該物件操作，像是新增、刪除等等

$(document).ready(function(){
	homePageList = new Array();
	checkLogin("pages/", "../");

	//swiper-button
	var swiperButton = '</div><div class="swiper-button-next"></div><div class="swiper-button-prev"></div>'
		+'</div></div></section>'
	var homePageListId = 1;//每個card的Id
	//首頁初始化，推薦影片、清單等等
	$.ajax({
		url : ajaxURL+'AnyCourse/HomePageServlet.do',
		method : 'GET',
		cache :false,
		success:function(result){
			console.log(result);
			for(var i = 0;i < result.length ; i++){
				//亂數決定順序
				var temp;
				for(var j = 0 ;j < Object.keys(result[i]).length;j++){
					temp = Math.floor(Math.random()*Object.keys(result[i]).length);
					var courseInfo = result[i][temp];
					result[i][temp] = result[i][j];
					result[i][j] = courseInfo;
				}
				//推薦影片
				if(result[i][0].type == 1){
					$('.content-wrapper').append(
							'<section class="content-header">'
							+'<h4>推薦影片</h4>'
							+'</section>'
							+'<section class="content">'
							+'<div class="row">'
							+'<div class="swiper-container">'
							+'<div class="swiper-wrapper" id="courseList_1">'
							+swiperButton);
					for(var j = 0 ;j < 10;j++){ 
						$('#courseList_1').append(
							'<div class="swiper-slide col-sm-5ths col-xs-5ths" style="padding:5px;" id="homePageListId_'+ homePageListId +'">'
							+'<div class="card">'
							+'<figure class="snip1492">'
							+'<div class="embed-responsive embed-responsive-16by9" onclick="jumpToPlayerInterface('+ result[i][j].unitId + ',' + result[i][j].videoType+')">'
							+'<img id="img" class="cardWidth style-scope yt-img-shadow" width="300px;" alt="" src="'+result[i][j].videoImgSrc+'">'
							+'</div>'
							+'<i class="fa fa-play"></i>'
							+'<a href="#" onclick="jumpToPlayerInterface('+ result[i][j].unitId + ',' + result[i][j].videoType+')"></a>'
							+'</figure>'
							+'<div class="card-block" style="padding-top:0px;" onclick="jumpToPlayerInterface('+ result[i][j].unitId + ',' + result[i][j].videoType+')">'
							+'<h4 class="card-title" >' + result[i][j].unitName + '</h4>'
							+'<div class="card-text">清單:'+ result[i][j].listName +'</div>'
							+'<div class="meta">'
							+'<a>'+ result[i][j].schoolName +'&nbsp;&nbsp;'+ result[i][j].teacher +'教師</a>'
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
							+'<li><a data-toggle="modal" data-target="#addToVideoList" onclick="getId('+homePageListId+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>新增至課程清單</a></li>'
							+'<li><a data-toggle="modal" data-target="#addToCoursePlan" onclick="getId('+homePageListId+')" style="cursor:pointer;"> <i class="fa fa-tasks"></i>新增至課程計畫</a></li>'
							+'</ul></div></div></div></div>'
						);
						$('.snip1492').tooltip({title:result[i][j].listName , scontainer: "body", placement:"bottom", animation: true}); 
						homePageList[homePageListId] = new Array(2);
						homePageList[homePageListId][0] = result[i][j].unitId;
						homePageList[homePageListId][1] = result[i][j].courselistId;
						homePageList[homePageListId][2] = result[i][j].creator;
						homePageListId++;
					}
				}
				//推薦清單
				else if(result[i][0].type == 2){
					$('.content-wrapper').append(
							'<section class="content-header">'
							+'<h4>推薦清單</h4>'
							+'</section>'
							+'<section class="content">'
							+'<div class="row">'
							+'<div class="swiper-container">'
							+'<div class="swiper-wrapper" id="courseList_2">'
							+swiperButton);
					for(var j = 0 ;j < 10;j++){
						$('#courseList_2').append(
							'<div class="swiper-slide col-sm-5ths col-xs-5ths" style="padding:5px;" id="homePageListId_'+ homePageListId +'">'
							+'<div class="card">'
							+'<figure class="snip1492">'
							+'<div class="embed-responsive embed-responsive-16by9" onclick="jumpToPlayerInterfaceWithList('+ result[i][j].unitId + ',' + result[i][j].videoType +',' + result[i][j].courselistId + ')">'
							+'<img id="img" class="cardWidth style-scope yt-img-shadow" width="300px;" alt="" src="'+result[i][j].videoImgSrc+'">'
							+'</div>'
							+'<i class="fa fa-play"></i>'
							+'<a href="#" onclick="jumpToPlayerInterfaceWithList('+ result[i][j].unitId + ',' + result[i][j].videoType +',' + result[i][j].courselistId + ')"></a>'
							+'</figure>'
							+'<div class="card-block" style="padding-top:0px;" onclick="jumpToPlayerInterfaceWithList('+ result[i][j].unitId + ',' + result[i][j].videoType +',' + result[i][j].courselistId + ')">'
							+'<h4 class="card-title" >' + result[i][j].listName + '</h4>'
							+'<div class="meta">'
							+'<a>'+ result[i][j].schoolName +'&nbsp;&nbsp;'+ result[i][j].teacher +'教師</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>'+result[i][j].listLikes+'</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>類型 : 清單</a>'
							+'</div></div><div class="card-footer"><div class="btn-group show-on-hover dropup">'
							+'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">新增至' 
							+'<span class="caret caret-up"></span></button><ul class="dropdown-menu drop-up" role="menu">'
							+'<li><a data-toggle="modal" data-target="#addToVideoListList" onclick="getId('+homePageListId+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>新增至課程清單</a></li>'
							+'<li><a data-toggle="modal" data-target="#addToCoursePlanList" onclick="getId('+homePageListId+')" style="cursor:pointer;"> <i class="fa fa-tasks"></i>新增至課程計畫</a></li>'
							+'</ul></div></div></div></div>'
						);
						$('.snip1492').tooltip({title:result[i][j].listName , scontainer: "body", placement:"bottom", animation: true}); 
						homePageList[homePageListId] = new Array(2);
						homePageList[homePageListId][0] = result[i][j].unitId;
						homePageList[homePageListId][1] = result[i][j].courselistId;
						homePageList[homePageListId][2] = result[i][j].creator;
						homePageListId++;
					}
				}
				//課程清單
				else if(result[i][0].type == 3){
					$('.content-wrapper').append(
							'<section class="content-header">'
							+'<h4>課程清單</h4>'
							+'</section>'
							+'<section class="content">'
							+'<div class="row">'
							+'<div class="swiper-container">'
							+'<div class="swiper-wrapper" id="courseList_3">'
							+swiperButton);
					for(var j = 0 ;j < Object.keys(result[i]).length;j++){
						$('#courseList_3').append(
							'<div class="swiper-slide col-sm-5ths col-xs-5ths" style="padding:5px;" id="homePageListId_'+ homePageListId +'">'
							+'<div class="card">'
							+'<figure class="snip1492">'
							+'<div class="embed-responsive embed-responsive-16by9" onclick="jumpToPlayerInterfaceWithList('+ result[i][j].unitId + ',' + result[i][j].videoType +',' + result[i][j].courselistId + ')">'
							+'<img id="img" class="cardWidth style-scope yt-img-shadow" width="300px;" alt="" src="'+result[i][j].videoImgSrc+'">'
							+'</div>'
							+'<i class="fa fa-play"></i>'
							+'<a href="#" onclick="jumpToPlayerInterfaceWithList('+ result[i][j].unitId + ',' + result[i][j].videoType +',' + result[i][j].courselistId + ')"></a>'
							+'</figure>'
							+'<div class="card-block" style="padding-top:0px;" onclick="jumpToPlayerInterfaceWithList('+ result[i][j].unitId + ',' + result[i][j].videoType +',' + result[i][j].courselistId + ')">'
							+'<h4 class="card-title" >' + result[i][j].listName + '</h4>'
							+'<div class="meta">'
							+'<a>'+ result[i][j].schoolName +'&nbsp;&nbsp;'+ result[i][j].teacher +'教師</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>'+result[i][j].listLikes+'</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>類型 : 清單</a>'
							+'</div></div><div class="card-footer"><div class="btn-group show-on-hover dropup">'
							+'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">新增至' 
							+'<span class="caret caret-up"></span></button><ul class="dropdown-menu drop-up" role="menu">'
							+'<li><a data-toggle="modal" data-target="#addToCoursePlanList" onclick="getId('+homePageListId+')" style="cursor:pointer;"> <i class="fa fa-tasks"></i>新增至課程計畫</a></li>'
							+'</ul></div></div></div></div>'
						);
						$('.snip1492').tooltip({title:result[i][j].listName , scontainer: "body", placement:"bottom", animation: true}); 
						homePageList[homePageListId] = new Array(2);
						homePageList[homePageListId][0] = result[i][j].unitId;
						homePageList[homePageListId][1] = result[i][j].courselistId;
						homePageList[homePageListId][2] = result[i][j].creator;
						homePageListId++;
					}
				}
				//想要觀看
				else if(result[i][0].type == 4){
					$('.content-wrapper').append(
							'<section class="content-header">'
							+'<h4>想要觀看</h4>'
							+'</section>'
							+'<section class="content">'
							+'<div class="row">'
							+'<div class="swiper-container">'
							+'<div class="swiper-wrapper" id="courseList_4">'
							+swiperButton);
				}
				//正在觀看
				else if(result[i][0].type == 5){
					$('.content-wrapper').append(
							'<section class="content-header">'
							+'<h4>正在觀看</h4>'
							+'</section>'
							+'<section class="content">'
							+'<div class="row">'
							+'<div class="swiper-container">'
							+'<div class="swiper-wrapper" id="courseList_5">'
							+swiperButton);
				}
				if(result[i][0].type == 4||result[i][0].type == 5){
					for(var j = 0 ;j < Object.keys(result[i]).length;j++){
						$('#courseList_'+result[i][0].type).append(
							'<div class="swiper-slide col-sm-5ths col-xs-5ths" style="padding:5px;" id="homePageListId_'+ homePageListId +'">'
							+'<div class="card">'
							+'<figure class="snip1492">'
							+'<div class="embed-responsive embed-responsive-16by9" onclick="jumpToPlayerInterface('+ result[i][j].unitId + ',' + result[i][j].videoType+')">'
							+'<img id="img" class="cardWidth style-scope yt-img-shadow" width="300px;" alt="" src="'+result[i][j].videoImgSrc+'">'
							+'</div>'
							+'<i class="fa fa-play"></i>'
							+'<a href="#" onclick="jumpToPlayerInterface('+ result[i][j].unitId + ',' + result[i][j].videoType+')"></a>'
							+'</figure>'
							+'<div class="card-block" style="padding-top:0px;" onclick="jumpToPlayerInterface('+ result[i][j].unitId + ',' + result[i][j].videoType+')">'
							+'<h4 class="card-title" >' + result[i][j].unitName + '</h4>'
							+'<div class="card-text">清單:'+ result[i][j].listName +'</div>'
							+'<div class="meta">'
							+'<a>'+ result[i][j].schoolName +'&nbsp;&nbsp;'+ result[i][j].teacher +'教師</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>'+result[i][j].unitLikes+'</a>'
							+'</div>'
							+'<div class="meta">'
							+'<a>類型 : 影片</a>'
							+'</div></div><div class="card-footer"><div class="btn-group show-on-hover dropup">'
							+'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">新增至' 
							+'<span class="caret caret-up"></span></button><ul class="dropdown-menu drop-up" role="menu">'
							+'<li><a data-toggle="modal" data-target="#addToVideoList" onclick="getId('+homePageListId+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>新增至課程清單</a></li>'
							+'</ul></div></div></div></div>'
						);
						$('.snip1492').tooltip({title:result[i][j].listName , scontainer: "body", placement:"bottom", animation: true}); 
						homePageList[homePageListId] = new Array(2);
						homePageList[homePageListId][0] = result[i][j].unitId;
						homePageList[homePageListId][1] = result[i][j].courselistId;
						homePageList[homePageListId][2] = result[i][j].creator;
						homePageListId++;
					}
				}
//				//台大
//				else if(result[i][0].type == 6){
//					
//				}
//				//清大
//				else if(result[i][0].type == 7){
//					
//				}
//				//交大
//				else if(result[i][0].type == 8){
//	
//				}
//				//成大
//				else if(result[i][0].type == 9){
//	
//				}
//				//政大
//				else if(result[i][0].type == 10){
//	
//				}
				if(window.screen.width >= 1024){
					var swiper = new Swiper('.swiper-container', {
					      slidesPerView: 4,
					      spaceBetween: 0,
					      slidesPerGroup: 4,
					      loop: false,
					      loopFillGroupWithBlank: true,
					      navigation: {
					        nextEl: '.swiper-button-next',
					        prevEl: '.swiper-button-prev',
					      },
					    });
				}
				else if(window.screen.width <= 480){
					var swiper = new Swiper('.swiper-container', {
					      slidesPerView: 1,
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
				else {
					var swiper = new Swiper('.swiper-container', {
					      slidesPerView: 2,
					      spaceBetween: 0,
					      slidesPerGroup: 2,
					      loop: false,
					      loopFillGroupWithBlank: true,
					      navigation: {
					        nextEl: '.swiper-button-next',
					        prevEl: '.swiper-button-prev',
					      },
					    });
				}
			}
			//因為放在外面的話跟初始化首頁的ajax(就是這個外面的ajax)會同時跑，這個會跑比較快，所以抓不到陣列
			//個別影片新增至課程計畫
			$('#addToCoursePlanButton,#addToCoursePlanButtonClose').click(function(){
				$.ajax({
					url : ajaxURL+'AnyCourse/HomePageServlet.do',
					method : 'POST',
					cache: false,
					data:{
						action:'addToCoursePlan',
						unitId:homePageList[checkId][0],
						creator:homePageList[checkId][2]
					},
					error:function(){
						console.log("addToCoursePlan Error!");
					}
				})
			});
			//清單整個新增至課程計畫
			$('#addToCoursePlanButtonList,#addToCoursePlanButtonListClose').click(function(){
				$.ajax({
					url : ajaxURL+'AnyCourse/HomePageServlet.do',
					method : 'POST',
					cache: false,
					data:{
						action:'addToCoursePlanList',
						courselistId:homePageList[checkId][1],
						creator:homePageList[checkId][2]
					},
					error:function(e){
						console.log("addToCoursePlanList Error!");
					}
				});
			});
			//將影片新增至指定清單
			$('#addToVideoListButton').click(function(){
				if($('#addToVideoListModalBody').val() != null){
					$.ajax({
						url : ajaxURL+'AnyCourse/HomePageServlet.do',
						method : 'POST',
						cache: false,
						data:{
							action:'addToVideoList',
							courselistId:$('#addToVideoListModalBody').val(),
							unitId:homePageList[checkId][0]
						},
						success:function(result){
							for(var i = 0;i < result.length;i++){
								$('#addToVideoListModalBody').append('<option value="'+result[i].courselistId+'">'+result[i].listName+'</option>');
							}
						},
						error:function(){
							console.log("add video to courselist error");
						}
					});
				}
			});
			//清單整個新增至課程清單
			$('#addToVideoListButtonList,#addToVideoListButtonListClose').click(function(){
				$.ajax({
					url : ajaxURL+'AnyCourse/HomePageServlet.do',
					method : 'POST',
					cache: false,
					data:{
						action:'addToVideoListList',
						courselistId:homePageList[checkId][1]
					},
					error:function(e){
						console.log("add list to courselist error");
					}
				});
			});
		},
		error:function(){
			console.log("get videoList and video error");
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
			if(result.length == 0){
				$('#addToVideoListModalBody').append('<option value="null">無</option>');
			}
			else{
				for(var i = 0;i < result.length;i++){
					$('#addToVideoListModalBody').append('<option value="'+result[i].courselistId+'">'+result[i].listName+'</option>');
				}
			}
			
			
		},
		error:function(){
			console.log("append videoList to modal error");
		}
	});
	
 });

//跳轉至播放介面
function jumpToPlayerInterface(unitId,type){
    url = "pages/PlayerInterface.html?unitId="+unitId+"&type="+type;//此處拼接內容
    window.location.href = url;
}
//跳轉至播放介面，且是清單
function jumpToPlayerInterfaceWithList(unitId,type,listId){
	url = "pages/PlayerInterface.html?unitId="+unitId+"&type="+type+"&listId="+listId;
	window.location.href = url;
}
//才能知道要對哪個Id做動作
function getId(id){
    checkId = id;
}
///////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////進入首頁時重新整理//////////////////////////////////////////

window.onload = function() {
    if(!window.location.hash) {
        window.location = window.location + '#loaded';
        window.location.reload();
    }
}