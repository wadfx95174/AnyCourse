//var ajaxURL="http://140.121.197.130:8400/";
var ajaxURL="http://localhost:8080/";
//////////////////////////////設置拉霸///////////////////////////////////
$('#unit').slimScroll({
    height: '420px;'
    });
$('.box-body').slimScroll({
	  height: '420px;'
	  });
var listArray;//用來儲存影片的unitId
$(document).ready(function() {
	checkLogin("../", "../../../");
	//想要觀看、正在觀看、已觀看完的每個影片的前段HTML
	var ListBeginningHTML='<li class="portlet" style="cursor:pointer;">'
		+'<span class="handle portlet-header">'
		+'<i class="fa fa-ellipsis-h"></i>'
		+'</span>'
		+'<div class="btn-group" style="top: -5px;">'
		+'<button type="button" class="btn btn-noColor dropdown-toggle"'
		+'data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'
		+'<span class="caret"></span>'
		+'</button>'
		+'<ul class="dropdown-menu">'
		+'<li>'
		+'<a href="#" class=" waves-effect waves-block">'
		+'<i class="ion ion-clipboard"></i>新增至課程清單'
		+'</a>'
		+'</li>'
		+'</ul>'
		+'</div>'
		+'<span class="pull-right">' 
		+'<i class="fa fa-times" style="cursor: pointer;"></i>'
		+'</span>';
	var videoId = 1;//設置每個影片的Id
	$.ajax({
		url:ajaxURL+'AnyCourse/CoursePlanServlet.do',
		method:'GET',
		cache :false,
		success:function(result){
			var listType;
			listArray = new Array(result.length);
			for(var i = 0;i < result.length; i++){
				//想要觀看
				if(result[i].status == 1)listType = '#wantList';
				//正在觀看
				else if(result[i].status == 2)listType = '#ingList';
				//已觀看完
				else if(result[i].status == 3)listType = '#doneList';
					
				$(listType).append(ListBeginningHTML
						+'<a class="portlet-content" id="'+videoId+'" onclick="jumpToPlayerInterface('+ result[i].unitId + ',' + result[i].videoType + ',' + result[i].lastTime+')">'
						+'<div class="info-card">'
						+'<div class="embed-responsive embed-responsive-16by9">'
						+'<img id="img" class="style-scope yt-img-shadow" alt="" style="width:100%;" src="'+result[i].videoImgSrc+'">' 
						+'</div>'
						+'<div class="info-card-details animate">'
						+'<div class="info-card-header">'
						+'<h3 class="unitNameTitle">'+ result[i].unitName +'</h3>'
						+'<h4>'+ result[i].schoolName +'</h4>'
						+'</div>'
						+'<div class="info-card-detail">'
						+'<h4>授課教師:'+result[i].teacher+'</h4>'
						+'<h4>清單名稱:'+result[i].listName+'</h4>'
						+'<h4>'+result[i].likes+'人喜歡</h4>'
						+'</div>'
						+'</div>'
						+'</div>'
						+'</a>'
						+'</li>');
				
				videoId++;
			}
			for(var i = 1 ; i <= result.length;i++){
				listArray[i] = result[i-1].unitId;
			}
		},
		error:function(){}
	})
//----------------------jQueryUI的sortable套件，並且運用start、update兩個事件去把排序資料存進資料庫中-----------------//
	$( ".column" ).sortable({
	    connectWith: ".column",
	    handle: ".portlet-header",
	    cancel: ".portlet-toggle",
	    placeholder: "portlet-placeholder ui-corner-all",
	    //當排序開始時觸發該事件
	    start:function(event,ui){
	    	//獲取被移動的影片被移動"前"在該sortable的index，並把他存在item的data中
	    	ui.item.data('startPos',ui.item.index());
	    	//拿item的小孩，並把他存在item的data中
	    	ui.item.data('id',ui.item.context.children[3].id);
	    	ui.item.data('sender',ui.item.parent().attr("id"));
	    },
	    //當使用者停止排序且DOM位置改變時觸發該事件
	    update:function(event,ui){
	    	var oldIndex = ui.item.data('startPos');
	    	var newIndex = ui.item.index();//獲取被移動的影片移動"後"在後來的sortable的index
	    	var received = ui.item.parent().attr('id');//獲取被移動的影片被移動"後"是在哪個sortable
	    	var sender = ui.item.data('sender');
	    	var id = ui.item.data('id');
	    	
	    	//原本她會執行兩次(sender被更新，及received也被更新，這句共讓他只執行一次)
	    	if (this === ui.item.parent()[0]) {
	    		$.ajax({
	    			url:ajaxURL+'AnyCourse/CoursePlanServlet.do',
		    		method:'POST',
		    		cache :false,
		    		data:{
		    			action: 'sortable',
	    				oldIndex: oldIndex + 1,
		    			newIndex: newIndex + 1,//因為抓到的index是從0開始算，而資料庫是從1開始，所以要加1
	    				sender: sender,
		    			received: received,
		    			unitId: listArray[id]//unitId
		    		},
		    		error:function(){
		    			console.log("CoursePlan Sort Error!");
		    		}
		    	})
	    	}
	    }
	});
//----------------------/.jQueryUI的sortable套件，並且運用start、update兩個事件去把排序資料存進資料庫中-----------------//
	
});
//跳轉至播放介面
function jumpToPlayerInterface(unitId,type,time){
    url = "../PlayerInterface.html?unitId="+unitId+"&type="+type+"&time="+time;//此處拼接內容
    window.location.href = url;
}