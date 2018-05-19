//////////////localhost用來測試、IP那個用來部屬再tomcat///////////////////////
var ajaxURL="http://140.121.197.130:8400/AnyCourse/HomePageServlet.do";
//var ajaxURL="http://localhost:8080/";
///////////////////////////////////////////////////////////////////////
/////////////////////////引用swiper這個套件///////////////////////////////
document.write("<script type='text/javascript' src='dist/js/swiper.min.js'></script>");


var homePageList;//Array，用來存影片、清單的資訊
var checkID;//用來存使用者點擊哪個物件的ID，用這個ID與homePageList這個Array搭配使用，就可以對該物件操作，像是新增、刪除等等

$(document).ready(function(){
	homePageList = new Array();
	checkLogin("pages/", "../");
	
	//swiper-button
	var swiperButton = '</div><div class="swiper-button-next"></div><div class="swiper-button-prev"></div>'
		+'</div></div></section>'
	var homePageListID = 1;//每個card的ID
	//首頁初始化，推薦影片、清單等等
	$.ajax({
		url : ajaxURL,
		method : 'GET',
		cache :false,
		success:function(result){
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
							+swiperButton);
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
							+swiperButton);
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
							+swiperButton);
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
							+swiperButton);
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
							+swiperButton);
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
				//亂數決定順序
				var temp;
				//推薦10個影片
				for(var j = 0 ;j < Object.keys(result[i]).length;j++){
					temp = Math.floor(Math.random()*Object.keys(result[i]).length);
					var course = result[i][temp];
					result[i][temp] = result[i][j];
					result[i][j] = course;
				}
				for(var j = 0 ;j < 10;j++){ 
					$('#courseList_1').append(
						'<div class="swiper-slide col-sm-5ths col-xs-5ths" style="padding:5px;" id="homePageListID_'+ homePageListID +'">'
						+'<div class="card">'
						+'<div class="embed-responsive embed-responsive-16by9" onclick="jumpToPlayerInterface('+ result[i][j].unitID + ',' + result[i][j].videoType+')">'
						+'<img id="img" class="cardWidth style-scope yt-img-shadow" width="300px;" alt="" src="'+result[i][j].videoImgSrc+'">'
						+'</div>'
						+'<div class="card-block" style="padding-top:0px;" onclick="jumpToPlayerInterface('+ result[i][j].unitID + ',' + result[i][j].videoType+')">'
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
						+'<li><a data-toggle="modal" data-target="#addToCourseList" onclick="getID('+homePageListID+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>新增至課程清單</a></li>'
						+'<li><a data-toggle="modal" data-target="#addToCoursePlan" onclick="getID('+homePageListID+')" style="cursor:pointer;"> <i class="fa fa-tasks"></i>新增至課程計畫</a></li>'
						+'</ul></div></div></div></div>'
					);
					$('.snip1492').tooltip({title:result[i][j].listName , scontainer: "body", placement:"bottom", animation: true}); 
					homePageList[homePageListID] = new Array(2);
					homePageList[homePageListID][0] = result[i][j].unitID;
					homePageList[homePageListID][1] = result[i][j].courselistID;
					homePageListID++;
				}
				
				//初始化swiper套件
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
			//影片新增至課程計畫
			$('#addToCoursePlanButton').click(function(){
				$.ajax({
					url : ajaxURL,
					method : 'POST',
					cache: false,
					data:{
						action:'addToCoursePlan',
						unitID:homePageList[checkID][0]
					},
					error:function(){
						console.log("addToCoursePlan Error!");
					}
				})
			});
			//將清單整個新增至課程計畫
			$('#addToCoursePlanButton_List').click(function(e){
				e.preventDefault();
				console.log(homePageList[checkID][0]);
				$.ajax({
					url : ajaxURL,
					method : 'POST',
					cache: false,
					data:{
						action:'addToCoursePlan_List',
						courselistID:homePageList[checkID][1]
					},
					error:function(e){
						console.log("addToCoursePlan_List Error!");
					}
				})
			});
  	},
		error:function(){}
	});
 });
//跳轉至播放介面，且是單個影片
//需要傳影片ID、影片類型兩個參數，且放在網址後面
function jumpToPlayerInterface(unitID,type){
    url = "pages/PlayerInterface.html?unitID="+unitID+"&type="+type;//此處拼接內容
    window.location.href = url;
}
//跳轉至播放介面，且是清單
//需要傳影片ID、影片類型、清單ID三個參數，且放在網址後面
function jumpToPlayerInterfaceWithList(unitID,type,listID){
	url = "pages/PlayerInterface.html?unitID="+unitID+"&type="+type+"&listID="+listID;
	window.location.href = url;
}
//取得某個物件的ID，才能知道要對誰做動作
function getID(id){
    checkID = id;
}

///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////進到首頁時，就自動重新整理(使用的方法為在網址上多加#loaded這段網址)//////////////////////////////////////////

window.onload = function() {
    if(!window.location.hash) {
        window.location = window.location + '#loaded';
        window.location.reload();
    }
}
