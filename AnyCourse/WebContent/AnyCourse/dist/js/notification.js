//var ajaxURL="http://140.121.197.131:7603/";
var ajaxURL="http://localhost:8080/";

var checkNotificationId;
var backgroundColor;

//載入頁面
$(document).ready(function() {

	ws.onmessage = function(event){
		console.log(event);
		var number = $('#notificationNumber').text();//暫存原本有幾個通知的變數
		var data = JSON.parse(event.data);

		if(number > 1){
			var num = parseInt(number)+1;
			$('#notificationNumber').text(num);
			$('#notificationHeader').text("You have " + num + " notifications");
		}
		else if(number == 1){
			$('#notificationNumber').text(2);
			$('#notificationHeader').text("You have 2 notifications");
		}
		else if(number == 0){
			$('#notificationNumber').text(1);
			$('#notificationHeader').text("You have 1 notification");
		}
		$('#notificationList').append(
			'<li onclick="setNotificationIsBrowse('+data.notificationId+')" style="background-color: Silver;">'
            +'<a href="'+ data.url +'">'
            +'<i class="fa fa-comment text-red"></i>'+ data.nickName +'回應了你的留言'
            +'</a>'
            +'</li>'
	    );
    };

    var notifications = 0;//還未點擊過的通知數量
    //append通知
	$.ajax({
		url : ajaxURL+'AnyCourse/NotificationServlet.do',
		method : 'GET',
		cache :false,
		success:function(result){
			
			if(result[0].type.match("playerInterfaceReply") != null){
				for(var i = 0; i < result.length ;i++){
					if(result[i].isBrowse == 0)notifications++;
				}
				//沒有通知時不需要顯示有幾個通知
				if(notifications == 0){
					$('#notificationNumber').text();
				}
				//有通知才要顯示有幾個通知
				else{
					$('#notificationNumber').text(notifications);
				}
				
				if(notifications == 0 && notifications == 1){
					$('#notificationHeader').text("You have " + notifications + " notification");
				}
				else{
					$('#notificationHeader').text("You have " + notifications + " notifications");
				}
	    		for(var i = 0 ;i < result.length;i++){
	    			if(result[i].isBrowse == 0)backgroundColor = 'style="background-color: Silver;"';
	    			else if(result[i].isBrowse == 1)backgroundColor = 'style="background-color: #f4f4f4;"';
	    			$('#notificationList').append(
	    					'<li onclick="setNotificationIsBrowse('+result[i].notificationId+')"'+ backgroundColor+'>'
	                        +'<a href="'+ result[i].url +'">'
	                        +'<i class="fa fa-comment text-red"></i>'+ result[i].nickname +'回應了你的留言'
	                        +'</a>'
	                        +'</li>'
		    		);
				}
			}

			
    	},
		error:function(){
			console.log("notification.js append notification error");
		}
	});

	
});

////////////////點擊任一通知後，將該通知的isBrowse改為1，代表點擊過/////////////////
function setNotificationIsBrowse(notificationId){
	$.ajax({
		url:ajaxURL + 'AnyCourse/NotificationServlet.do',
		method:'POST',
		cache:false,
		data:{
			action: "setNotificationIsBrowse",
			notificationId: notificationId
		},
		success:function(){
			$('#notificationId_'+notificationId).css("background-color", "#f4f4f4");
		},
		error:function(){
			console.log("notification.js set isBrowse error");
		}
	});
}
/////////////////////////////////////////////////////////////////////////////////