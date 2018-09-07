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

		//播放介面-討論區"回覆"通知
		if(data.type.match("playerInterfaceReply") != null){

			appendPlayerInterfaceForum(data.notificationId,data.url,data.nickName,'style="background-color: Silver;"');
			
		}
		//群組邀請
		else if(data.type.match("groupInvitation") != null){

			var groupName = data.groupName;
			var groupId = data.groupId;

			appendGroupInvitation(groupName,data.notificationId,'style="background-color: Silver;"');

			$('#groupInviteButton').click(function(){

				GroupInvitation(groupId,groupName);

			});
		}
    };

    var notifications = 0;//還未點擊過的通知數量
    //append通知
	$.ajax({
		url : ajaxURL+'AnyCourse/NotificationServlet.do',
		method : 'GET',
		cache :false,
		data:{
			'action': "getNotification"
		},
		success:function(result){
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

	    		//播放介面的討論區回覆
				if(result[i].type.match("playerInterfaceReply") != null){

					appendPlayerInterfaceForum(result[i].notificationId,result[i].url,result[i].nickName,backgroundColor);
	    			
				}
				//群組邀請
				else if(result[i].type.match("groupInvitation") != null){


					var groupName = result[i].groupName;
					var groupId = result[i].groupId;
					
					appendGroupInvitation(groupName,result[i].notificationId,backgroundColor);

		    		$('#groupInviteButton').click(function(){

		    			GroupInvitation(groupId,groupName);

					});
				}
			}

			
    	},
		error:function(){
			console.log("notification.js append notification error");
		}
	});




	//---------------------------------群組邀請提示----------------------------------------//
	$("body").append('<div class="modal fade" id="groupInviteModal" tabindex="-1" role="dialog"'
		+'aria-labelledby="groupInviteLabel" aria-hidden="true">'
		
		+'<div class="modal-dialog" role="document">'
			+'<div class="modal-content">'
				+'<div class="modal-header">'
			    	+'<b class="modal-title" style="font-size:24px;">群組邀請</b>'
			    +'</div>'
				+'<div class="modal-body">'
					+'<h4 id="groupInviteText"></h4>'
				+'</div>'
				+'<div class="modal-footer">'
					+'<button id="groupInviteButton" type="button"'
						+'class="btn btn-primary" data-dismiss="modal">確定</button>'
					+'<button type="button" class="btn btn-secondary"'
						+'data-dismiss="modal">取消</button>'
				+'</div>'
			+'</div>'
		+'</div>'
	+'</div>');
	//--------------------------------------------------------------------------------------//

	//-----------------------------加入失敗，已加入該群組提示---------------------------------//
	$("body").append('<div class="modal fade" id="groupHasJoinedModal" tabindex="-1" role="dialog"'
		+'aria-labelledby="groupHasJoinedLabel" aria-hidden="true">'
		
		+'<div class="modal-dialog" role="document">'
			+'<div class="modal-content">'
				+'<div class="modal-header">'
			    	+'<b class="modal-title" style="font-size:24px;">加入失敗</b>'
			    +'</div>'
				+'<div class="modal-body">'
					+'<h4 id="groupHasJoinedText"></h4>'
				+'</div>'
				+'<div class="modal-footer">'
					+'<button type="button" class="btn btn-secondary"'
						+'data-dismiss="modal">關閉</button>'
				+'</div>'
			+'</div>'
		+'</div>'
	+'</div>');

	//--------------------------------------------------------------------------------------//

	//--------------------------------成功加入該群組提示----------------------------------------//
	$("body").append('<div class="modal fade" id="groupJoinSuccessModal" tabindex="-1" role="dialog"'
		+'aria-labelledby="groupJoinSuccessLabel" aria-hidden="true">'
		
		+'<div class="modal-dialog" role="document">'
			+'<div class="modal-content">'
				+'<div class="modal-header">'
			    	+'<b class="modal-title" style="font-size:24px;">加入成功</b>'
			    +'</div>'
				+'<div class="modal-body">'
					+'<h4 id="groupJoinSuccessText"></h4>'
				+'</div>'
				+'<div class="modal-footer">'
					+'<button onclick=reloadPage() type="button" class="btn btn-secondary"'
						+'data-dismiss="modal">關閉</button>'
				+'</div>'
			+'</div>'
		+'</div>'
	+'</div>');

	//--------------------------------------------------------------------------------------//
});




//-------------------點擊任一通知後，將該通知的isBrowse改為1，代表點擊過-----------------//
function setNotificationIsBrowse(notificationId){
	$.ajax({
		url:ajaxURL + 'AnyCourse/NotificationServlet.do',
		method:'GET',
		cache:false,
		//出現這個錯誤時可以嘗試取消非同步，還有servlet的response的ContentType不能設為json
		//unexpected end of json input ajax
		async:false,
		data:{
			action: "setNotificationIsBrowse",
			notificationId: notificationId
		},
		success:function(response){},
		error:function(xhr, ajaxOptions, thrownError){
			console.log(xhr);
			console.log(thrownError);
			console.log("notification.js set isBrowse error");
		}
	});
}
//--------------------------------------------------------------------------------------//

//------------------------------------重新整理頁面---------------------------------------//
function reloadPage(){
	location.reload();
}
//--------------------------------------------------------------------------------------//

//--------------------------append通知(播放介面討論區回復)--------------------------------//
function appendPlayerInterfaceForum(notificationId,url,nickName,backgroundColor){
	$('#notificationList').append(
		'<li onclick="setNotificationIsBrowse('+notificationId+')"'+ backgroundColor+'>'
	    +'<a href="'+ url +'">'
	    +'<i class="fa fa-comment text-red"></i>'+ nickName +'回應了你的留言'
	    +'</a>'
	    +'</li>'
	);
}
//--------------------------------------------------------------------------------------//

//--------------------------------append通知(群組邀請)-----------------------------------//
function appendGroupInvitation(groupName,notificationId,backgroundColor){
	$('#groupInviteText').text("確定要加入「" + groupName + "」嗎?");
			
	$('#notificationList').append(
		'<li onclick="setNotificationIsBrowse('+notificationId+')"'+ backgroundColor+'>'
        +'<a data-toggle="modal" href="#groupInviteModal">'
        +'<i class="fa fa-comment text-red"></i>您收到來自「' + groupName + '」的群組邀請'
        +'</a>'
        +'</li>'
	);
}
//--------------------------------------------------------------------------------------//

//----------------檢查是否已經加入該群組，若無則加入，加入後送出WebSocket-------------------//
function GroupInvitation(groupId,groupName){
	console.log(groupId+"    "+groupName);
	$.ajax({
		url:ajaxURL + 'AnyCourse/GroupMemberServlet.do',
		method:'POST',
		cache:false,
		data:{
			'action': "checkJoinGroup",
			'groupId': groupId
		},
		success:function(response){
			//已經加入他群組
			if(response == "true"){
				$('#groupHasJoinedText').text('您以加入'+groupName);
				$('#groupHasJoinedModal').modal('show');
			}
			//尚未加入群組，可以加入
			else{
				$.ajax({
					url:ajaxURL + 'AnyCourse/NotificationServlet.do',
					method:'POST',
					cache:false,
					data:{
						'action': "agreeGroupInvitation",
						'groupId': groupId,
						'groupName': groupName,
						'url': ajaxURL + "AnyCourse/AnyCourse/pages/Group/Management.html?groupId=" + groupId
					},
					success:function(resp){
						console.log(resp);
						$('#groupJoinSuccessText').text("您成功加入"+groupName);
						$('#groupJoinSuccessModal').modal('show');

						// ws.send(JSON.stringify({
			   //              type: "groupMemberJoin",
			   //              toUserId: resp.toUserId,
			   //              notificationId: resp.notificationId,
			   //              nickname: resp.nickname,
			   //              groupId: groupId,
			   //              groupName: groupName
			   //              url: resp.url
			   //          }));
					},
					error:function(xhr, ajaxOptions, thrownError){
						console.log(xhr);
						console.log(thrownError);
						console.log("notification.js agree group invitation error");
					}
				});
			}
		},
		error:function(xhr, ajaxOptions, thrownError){
			console.log(xhr);
			console.log(thrownError);
			console.log("notification.js check join group error");
		}
	});
}
//--------------------------------------------------------------------------------------//