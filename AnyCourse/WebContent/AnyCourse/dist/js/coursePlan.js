$('#unit').slimScroll({
    height: '420px;'
    });
$('.box-body').slimScroll({
	  height: '420px;'
	  });
$( ".column" ).sortable({
    connectWith: ".column",
    handle: ".portlet-header",
    cancel: ".portlet-toggle",
    placeholder: "portlet-placeholder ui-corner-all"
  });
$(document).ready(function() {
	checkLogin("../", "../../../");
	$.ajax({
		url:'http://localhost:8080/AnyCourse/CoursePlanServlet.do',
		method:'GET',
		data:{
			action:'select',//初始化
			
		},
		success:function(result){
			$('#row').append('<section class="content col-md-4">'
					+'<div class="box box-primary">'
					+'<div class="box-header ui-sortable-handle">'
					+'<h3 class="box-title">'
					+'<strong>想要觀看</strong>'
					+'</h3>'
					+'</div>'
					+'<div class="box-body " style="height: 480px;" id="unit">'
					+'<ul class="todo-list column" id="list">'
					+'</ul>'
					+'</div>'
					+'<div class="box-footer clearfix no-border"></div>'
					+'</div>'
					+'</section>');
					for(var i = 0;i < result.length ; i++){
						$('#list').append('<li class="portlet">'
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
								+'<a href="../PlayerInterface.html" class="portlet-content">'
								+'<div class="info-card">'
								+'<div class="embed-responsive embed-responsive-16by9">'
								+'<img id="img" class="style-scope yt-img-shadow" alt="" width="350" src="https://i.ytimg.com/vi/g7eP3CTe6rI/hqdefault.jpg">' 
								+'</div>'
								+'<div class="info-card-details animate">'
								+'<div class="info-card-header">'
								+'<h1>理工微積分</h1>'
								+'<h3>國立中央大學</h3>'
								+'</div>'
								+'<div class="info-card-detail">'
								+'<p>...<br>...<br>...<br>...<br></p>'
								+'</div>'
								+'</div>'
								+'</div>'
								+'</a>'
								+'</li>'
								
						);
					}
		},
		error:function(){alert('failed');}
	})

  
	
});