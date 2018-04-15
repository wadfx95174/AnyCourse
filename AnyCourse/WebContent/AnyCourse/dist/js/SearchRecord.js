function check_all(obj,cName) 
{ 
    var checkboxs = document.getElementsByName(cName); 
    for(var i=0;i<checkboxs.length;i++){checkboxs[i].checked = obj.checked;} 
} 
$('#searchList').slimScroll({
    height: '420px;'
});


$(document).ready(function() {
	$.ajax({
		url : 'http://localhost:8080/AnyCourse/SearchRecordServlet.do',
		method : 'GET',
//		data : {
//			"user_id" : user_id,
//			"search_word" : search_word,
//			"search_time" : search_time
//		},
		success:function(result){
//    		var json = JSON.parse(result);
    		for(var i = 0 ;i < result.length;i++){
    			$('#SearcRecordList').append('<li class="list-group-item">'
						+'<div class="row">'
						+'<div class="col-xs-1 text-left">'
						+'<input name="checkboxItem" type="checkbox"/>'
						+'</div>'
						+'<div class="col-xs-8" style="">' + result[i].Search_word + '</div>'
						+'<div class="col-xs-3" style="">' + result[i].Search_time + '</div>'
						+'</div></li>');
			}
    	},
		error:function(){alert('failed');}
	});
});