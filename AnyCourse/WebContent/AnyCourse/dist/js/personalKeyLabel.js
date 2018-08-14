
//var ajaxURL="http://140.121.197.131:7603/";
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
		success:function(result){
			if(window.screen.width > 480){
				$('#keyLabelHeader').append('<div class="col-xs-1 text-center"><i class="fa fa-tags"></i></div>'
						+'<div class="col-xs-2 text-center"><strong>標籤</strong></div>'
						+'<div class="col-xs-5 text-center"><strong>單元</strong></div>'
						+'<div class="col-xs-3 text-center"><strong>時間</strong></div>'
						+'<div class="col-xs-1 text-center"></div>');
				for(var i = 0 ;i < result.length;i++){
					
	    			$('#KeyLabelList').append('<li class="list-group-item" id="searchRecordID_'+ (i+1) +'">'
							+'<div class="row">'
							+'<div class="col-xs-1 text-center"><i class="fa fa-tags"></i></div>'
							+'<div class="keyLabelName col-xs-2 text-center"><a href="../PlayerInterface.html?type='+ (result[i].url.split("/")[2]=="www.youtube.com"?1:2) + '&unitId='+result[i].unitId+'">' + result[i].keyLabelName + '</a></div>'
							+'<div class="unitName col-xs-5 text-center">' + result[i].unitName + '</div>'
							+'<div class="col-xs-3">'
							+'<div class="col-xs-12 col-md-6 text-center">' + formatTime(result[i].beginTime) + '</div>'
							+'<div class="col-xs-12 col-md-6 text-center">' + formatTime(result[i].endTime) + '</div>'
							+'</div>'
							+'</div></li>');
				}
			}
			else{
				$('#keyLabelHeader').append('<div class="col-xs-1 text-center"><i class="fa fa-tags"></i></div>'
						+'<div class="col-xs-4 text-center"><strong>標籤</strong></div>'
						+'<div class="col-xs-6 text-center"><strong>單元</strong></div>');
				for(var i = 0 ;i < result.length;i++){
					
	    			$('#KeyLabelList').append('<li class="list-group-item" id="searchRecordID_'+ (i+1) +'">'
							+'<div class="row">'
							+'<div class="col-xs-1 text-center"><i class="fa fa-tags"></i></div>'
							+'<div class="keyLabelName col-xs-4 text-center"><a href="../PlayerInterface.html?type='+ (result[i].url.split("/")[2]=="www.youtube.com"?1:2) + '&unitId='+result[i].unitId+'">' + result[i].keyLabelName + '</a></div>'
							+'<div class="unitName col-xs-6 text-center">' + result[i].unitName + '</div>'
							+'</div></li>');
				}
			}
    		
    	},
		error:function(){console.log('failed');}
	});
});