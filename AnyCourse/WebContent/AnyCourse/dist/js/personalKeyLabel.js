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
			console.log(result);
			if(window.screen.width > 480){
				$('#keyLabelHeader').append('<div class="col-xs-1 text-center"><i class="fa fa-tags"></i></div>'
						+'<div class="col-xs-2 text-center"><strong>標籤</strong></div>'
						+'<div class="col-xs-5 text-center"><strong>單元</strong></div>'
						+'<div class="col-xs-3 text-center"><strong>時間</strong></div>'
						+'<div class="col-xs-1 text-center">'
						+'</div>');
				for(var i = 0 ;i < result.length;i++){
					
	    			$('#KeyLabelList').append('<li class="list-group-item" id="searchRecordID_'+ (i+1) +'">'
							+'<div class="row">'
							+'<div class="col-xs-1 text-center"><i class="fa fa-tags"></i></div>'
							+'<div class="keyLabelName col-xs-2 text-center"><a href="../PlayerInterface.html?type='+ (result[i].url.split("/")[2]=="www.youtube.com"?1:2) + '&unitId='+result[i].unitId+'">' + result[i].keyLabelName + '</a></div>'
							+'<div class="unitName col-xs-5 text-center">' + result[i].unitName + '</div>'
							+'<div class="col-xs-3">'
							+'<div class="col-xs-12 col-md-6 text-center">' + formatTime(result[i].beginTime) + '</div>'
							+'<div class="col-xs-12 col-md-6 text-center">' + formatTime(result[i].endTime) + '</div>'
							+'<div class="personalNoteShare"><div class="btn-group show-on-hover dropup" >'
							+'<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">分享' 
							+'<span class="caret caret-up"></span></button><ul class="dropdown-menu drop-up" role="menu">'
							+'<li><a data-toggle="modal" data-target="#addToGroupKeyLabel" id="personalKeyLabel_'+ result[i].unitId  +'" onclick="getKeyLabelId('+result[i].keyLabelId+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>分享至群組</a></li>'
							+'</ul></div></div>'
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
	//取得該使用者的所有群組(用來放在下拉式選單，讓使用者選擇要分享哪個群組)
	$.ajax({
		url : ajaxURL+'AnyCourse/PersonalKeyLabelServlet.do',
		method : 'POST',
		data:{
			method : 'getAllGroup'
		},
		cache :false,
		success:function(result){
			console.log(result);
			if(result.length == 0){
				$('#addToGroupKeyLabelModalBody').append('<option value="null">無</option>');
			}
			else{
				for(var i = 0;i < result.length;i++){
					$('#addToGroupKeyLabelModalBody').append('<option value="'+result[i].groupId+'">'+result[i].groupName+'</option>');
				}
			}					
		},
		error:function(){
			console.log("append GroupName to modal error");
		}
	});

	//將筆記分享至指定群組
	$('#addToGroupKeyLabelButton').click(function(){
		$.ajax({
			url:ajaxURL+'AnyCourse/PersonalKeyLabelServlet.do',
			method:'POST',
			cache:false,
			data:{
				method:'insertToGroup',
				groupId:$('#addToGroupKeyLabelModalBody').val(),
				KeyLabelId:checkKeyLabelId
			},
			success:function(){
				// ---------------通知----------------------
				shareKeyLabelToGroup();
			},
			error:function(e){
				console.log("add personalNote to group error");
			}
		});
	});
});
function getKeyLabelId(id){
	checkKeyLabelId = id;
}
function shareKeyLabelToGroup(){
	$.ajax({
		url:ajaxURL + 'AnyCourse/NotificationServlet.do',
		method:'POST',
		cache:false,
		data:{
			'action': "groupNotification",
			'groupId': $('#addToGroupKeyLabelModalBody').val(),
			'url': ajaxURL + "AnyCourse/AnyCourse/pages/Group/KeyLabelPage.html?groupId=" + $('#addToGroupKeyLabelModalBody').val(),
			'type': "shareKeyLabelToGroup"
		},
		success:function(response){
			console.log(response);

			for(var i = 0;i < response.length; i++){
				ws.send(JSON.stringify({
	                type: response[i].type,
	                toUserId: response[i].toUserId,
	                notificationId: response[i].notificationId,
	                nickname: response[i].nickname,
	                groupId: response[i].groupId,
	                groupName: response[i].groupName,
	                url: response[i].url
	            }));
			}
		},
		error:function(xhr, ajaxOptions, thrownError){
			console.log(xhr);
			console.log(thrownError);
			console.log("videoList.js groupNotification error");
		}
	});
}