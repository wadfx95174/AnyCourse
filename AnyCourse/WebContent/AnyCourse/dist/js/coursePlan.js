$('#unit').slimScroll({
    height: '420px;'
    });
$('.box-body').slimScroll({
	  height: '420px;'
	  });
var listArray;
$(document).ready(function() {
	checkLogin("../", "../../../");
	var str3='<li class="portlet">'
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
	var videoID = 1;
	$.ajax({
		url:'http://140.121.197.130:8400/AnyCourse/CoursePlanServlet.do',
		method:'GET',
		cache :false,
//		data:{
//			action:'select',//初始化
//			
//		},
		success:function(result){
			listArray = new Array(result.length);
			for(var i = 0;i < result.length; i++){
//				console.log(result[i].teacher);
				//想要觀看
				if(result[i].status == 1){
					
					$('#wantList').append(str3
							+'<a class="portlet-content" id="'+videoID+'" onclick="jumpToPlayerInterface('+ result[i].unit_id + ',' + result[i].video_type+')">'
							+'<div class="info-card">'
							+'<div class="embed-responsive embed-responsive-16by9">'
							+'<img id="img" class="style-scope yt-img-shadow" alt="" width="350" src="'+result[i].video_img_src+'">' 
							+'</div>'
							+'<div class="info-card-details animate">'
							+'<div class="info-card-header">'
							+'<h1>'+ result[i].unit_name +'</h1>'
							+'<h3>'+ result[i].school_name +'</h3>'
							+'</div>'
							+'<div class="info-card-detail">'
							+'<p>授課教師:'+result[i].teacher+'</p>'
							+'<p>清單名稱:'+result[i].list_name+'</p>'
							+'<p>'+result[i].list_name+'人喜歡</p>'
							+'</div>'
							+'</div>'
							+'</div>'
							+'</a>'
							+'</li>'
					);
				}
				//正在觀看
				else if(result[i].status == 2){
					$('#ingList').append(str3
							+'<a class="portlet-content" id="'+videoID+'" onclick="jumpToPlayerInterface('+ result[i].unit_id + ',' + result[i].video_type+')">'
							+'<div class="info-card">'
							+'<div class="embed-responsive embed-responsive-16by9">'
							+'<img id="img" class="style-scope yt-img-shadow" alt="" width="350" src="'+result[i].video_img_src+'">' 
							+'</div>'
							+'<div class="info-card-details animate">'
							+'<div class="info-card-header">'
							+'<h1>'+ result[i].unit_name +'</h1>'
							+'<h3>'+ result[i].school_name +'</h3>'
							+'</div>'
							+'<div class="info-card-detail">'
							+'<p>授課教師:'+result[i].teacher+'</p>'
							+'<p>清單名稱:'+result[i].list_name+'</p>'
							+'<p>'+result[i].list_name+'人喜歡</p>'
							+'</div>'
							+'</div>'
							+'</div>'
							+'</a>'
							+'</li>'
					);
				}
				//已觀看完
				else if(result[i].status == 3){
					$('#doneList').append(str3
							+'<a class="portlet-content" id="'+videoID+'" onclick="jumpToPlayerInterface('+ result[i].unit_id + ',' + result[i].video_type+')">'
							+'<div class="info-card">'
							+'<div class="embed-responsive embed-responsive-16by9">'
							+'<img id="img" class="style-scope yt-img-shadow" alt="" width="350" src="'+result[i].video_img_src+'">' 
							+'</div>'
							+'<div class="info-card-details animate">'
							+'<div class="info-card-header">'
							+'<h1>'+ result[i].unit_name +'</h1>'
							+'<h3>'+ result[i].school_name +'</h3>'
							+'</div>'
							+'<div class="info-card-detail">'
							+'<p>授課教師:'+result[i].teacher+'</p>'
							+'<p>清單名稱:'+result[i].list_name+'</p>'
							+'<p>'+result[i].list_name+'人喜歡</p>'
							+'</div>'
							+'</div>'
							+'</div>'
							+'</a>'
							+'</li>'
					);
				}
				videoID++;
			}
//			console.log(result.length);
			for(var i = 1 ; i <= result.length;i++){
				listArray[i] = result[i-1].unit_id;
//				console.log(listArray[i]);
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
	    	var start_pos = ui.item.index();//獲取被移動的影片被移動"前"在該sortable的index
	    	ui.item.data('start_pos',start_pos);
	    	var id = ui.item.context.children[3].id;//拿item的小孩
	    	ui.item.data('id',id);
	    	var sender = ui.item.parent().attr("id");
	    	ui.item.data('sender',sender);
	    	
	    },
	    //當使用者停止排序且DOM位置改變時觸發該事件
	    update:function(event,ui){
	    	var oldIndex = ui.item.data('start_pos');
	    	var newIndex = ui.item.index();//獲取被移動的影片移動"後"在後來的sortable的index
	    	var received = ui.item.parent().attr('id');//獲取被移動的影片被移動"後"是在哪個sortable
	    	var sender = ui.item.data('sender');
	    	var id = ui.item.data('id');
	    	
	    	//原本她會執行兩次(sender被更新，及received也被更新，這句共讓他只執行一次)
	    	if (this === ui.item.parent()[0]) {
//	    		console.log(sender);
//	    		console.log(oldIndex);
	    		$.ajax({
	    			url:'http://localhost:8080/AnyCourse/CoursePlanServlet.do',
		    		method:'POST',
		    		cache :false,
		    		data:{
		    			action: 'sortable',
	    				oldIndex: oldIndex + 1,
		    			newIndex: newIndex + 1,//因為抓到的index是從0開始算，而資料庫是從1開始，所以要加1
	    				sender: sender,
		    			received: received,
		    			unit_id: listArray[id]//unit_id
		    		},
//		    		success:function(result){},
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
function jumpToPlayerInterface(unit_id,type){
    url = "../PlayerInterface.html?unit_id="+unit_id+"&type="+type;//此處拼接內容
    window.location.href = url;
}