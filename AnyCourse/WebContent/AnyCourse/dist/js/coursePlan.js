$('#unit').slimScroll({
    height: '420px;'
    });
$('.box-body').slimScroll({
	  height: '420px;'
	  });

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
		+'</span>' 
		
	$.ajax({
		url:'http://localhost:8080/AnyCourse/CoursePlanServlet.do',
		method:'GET',
//		data:{
//			action:'select',//初始化
//			
//		},
		success:function(result){
			for(var i = 0;i < result.length; i++){
//				console.log(result[i].teacher);
				//想要觀看
				if(result[i].status == 1){
					
					$('#wantList').append(str3
							+'<a class="portlet-content" onclick="jumpToPlayerInterface('+ result[i].unit_id + ',' + result[i].video_type+')">'
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
							+'<a class="portlet-content" onclick="jumpToPlayerInterface('+ result[i].unit_id + ',' + result[i].video_type+')">'
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
							+'<a class="portlet-content" onclick="jumpToPlayerInterface('+ result[i].unit_id + ',' + result[i].video_type+')">'
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
			}
			
					
		},
		error:function(){alert('failed');}
	})
	$( ".column" ).sortable({
	    connectWith: ".column",
	    handle: ".portlet-header",
	    cancel: ".portlet-toggle",
	    placeholder: "portlet-placeholder ui-corner-all",
	    //當排序開始時觸發該事件
	    start:function(event,ui){
	    	var start_pos = ui.item.index();//獲取被移動的影片被移動"前"在該sortable的index
	    	ui.item.data('start_pos',start_pos);
	    	
	    },
	    //當來自一個連接的sortable列表的一個項目被放置到另一個列表時觸發該事件。後者是事件目標
	    receive:function(event,ui){
	    	var sender = ui.sender.context.id;//獲取被移動的影片被移動"前"是在哪個sortable
	    	ui.item.data('sender',sender);
	    	var received = ui.item.parent().attr('id');//獲取被移動的影片被移動"後"是在哪個sortable
	    	ui.item.data('received',received);
//	    	console.log(ui.item.parent().attr('id'));
	    },
	    //當使用者停止排序且DOM位置改變時觸發該事件
	    update:function(event,ui){
	    	var oldIndex = ui.item.data('start_pos');
	    	var newIndex = ui.item.index();//獲取被移動的影片移動"後"在後來的sortable的index
	    	var sender = ui.item.data('sender');
	    	var received = ui.item.data('received');
//	    	console.log(ui.sender.index());
//	    	console.log(oldIndex);
//	    	console.log(newIndex);
	    	
	    	$.ajax({
	    		type:'post',
	    		url:'http://localhost:8080/AnyCourse/CoursePlanServlet.do',
	    		data:{
	    			action: 'sortable',
	    			oldIndex: oldIndex,
	    			newIndex: newIndex,
	    			sender: sender,
	    			received: received
	    		},
	    		success:function(result){},
	    		error:function(){
	    			console.log("CoursePlan Sort Error!");
	    		}
	    	})
	    }
	});
  
	
});
//跳轉至播放介面
function jumpToPlayerInterface(unit_id,type){
    url = "../PlayerInterface.html?unit_id="+unit_id+"&type="+type;//此處拼接內容
    window.location.href = url;
}