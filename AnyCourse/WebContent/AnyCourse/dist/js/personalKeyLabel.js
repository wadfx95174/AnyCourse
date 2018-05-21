//var ajaxURL="http://140.121.197.130:8400/";
var ajaxURL="http://localhost:8080/";


function check_all(obj,cName) 
{ 
    var checkboxs = document.getElementsByName(cName); 
    for(var i=0;i<checkboxs.length;i++){checkboxs[i].checked = obj.checked;} 
} 

function formatTime(seconds) {
    return [
        parseInt(seconds / 60 / 60),
        parseInt(seconds / 60 % 60),
        parseInt(seconds % 60)
    ]
        .join(":")
        .replace(/\b(\d)\b/g, "0$1");
}

$(document).ready(function() {
	

	checkLogin("../", "../../../");
	//取得資料庫的資料
	$.ajax({
		url : ajaxURL+'AnyCourse/PersonalKeyLabelServlet.do',
		method : 'GET', 
		cache :false,
		data: {
			userId : 1
		},
		success:function(result){
    		for(var i = 0 ;i < result.length;i++){
    			$('#KeyLabelList').append('<li class="list-group-item" id="searchRecordID_'+ (i+1) +'">'
						+'<div class="row">'
						+'<div class="col-xs-1 text-center"><i class="fa fa-tags"></i></div>'
						+'<div class="col-xs-2 text-center"><a href="../PlayerInterface.html?type='+ (result[i].url.split("/")[2]=="www.youtube.com"?1:2) + '&unit_id='+result[i].unitId+'">' + result[i].keyLabelName + '</a></div>'
						+'<div class="col-xs-5 text-center">' + result[i].unitName + '</div>'
						+'<div class="col-xs-3">'
						+'<div class="col-xs-12 col-md-6 text-center">' + formatTime(result[i].beginTime) + '</div>'
						+'<div class="col-xs-12 col-md-6 text-center">' + formatTime(result[i].endTime) + '</div>'
						+'</div>'
//						+'<div class="col-xs-1 text-center">'
//						+'<button type="button" data-toggle="modal" data-target="#deleteModal1" onclick="setID('+(i+1)+')"><i class="fa fa-trash-o" data-toggle="tooltip" data-placement="top" title="刪除"></i></button>'
//						+'</div>'
						+'</div></li>');
			}
    	},
		error:function(){console.log('failed');}
	});
});