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
			
			$('#notificationList').append(
				'<li onclick="setNotificationIsBrowse('+data.notificationId+')" style="background-color: Silver;">'
		        +'<a href="'+ data.url +'">'
		        +'<i class="fa fa-comment text-red"></i>'+ data.nickName +'回應了你的留言'
		        +'</a>'
		        +'</li>'
		    );

		}
		//群組邀請
		else if(data.type.match("groupInvitation") != null){

			var groupId = data.groupId;
			$('#notificationList').append(
				'<li onclick="setNotificationIsBrowse('+data.notificationId+')" style="background-color: Silver;">'
                +'<a data-toggle="modal" href="#groupInvitePrompt">'
                +'<i class="fa fa-comment text-red"></i>您收到來自' + data.nickname + '的群組邀請'
                +'</a>'
                +'</li>'
			);

			$('#groupInvitePromptButton').click(function(){

				$.ajax({
					url:ajaxURL + 'AnyCourse/GroupMemberServlet.do',
					method:'POST',
					cache:false,
					data:{
						'action': "checkJoinGroup",
						'groupId': groupId
					},
					success:function(response){
						if(response == "true"){
							$('#groupHasJoinedModal').modal('show');
						}
						else{
							$.ajax({
								url:ajaxURL + 'AnyCourse/GroupMemberServlet.do',
								method:'POST',
								cache:false,
								data:{
									'action': "agreeGroupInvitation",
									'groupId': groupId
								},
								success:function(resp){
									$('#groupJoinSuccessModal').modal('show');
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
			});
			$('#')
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

				if(result[i].type.match("playerInterfaceReply") != null){

	    			$('#notificationList').append(
    					'<li onclick="setNotificationIsBrowse('+result[i].notificationId+')"'+ backgroundColor+'>'
                        +'<a href="'+ result[i].url +'">'
                        +'<i class="fa fa-comment text-red"></i>'+ result[i].nickname +'回應了你的留言'
                        +'</a>'
                        +'</li>'
		    		);
	    			
				}
				else if(result[i].type.match("groupInvitation") != null){
					var groupId = result[i].groupId;
					$('#notificationList').append(
    					'<li onclick="setNotificationIsBrowse('+result[i].notificationId+')"'+ backgroundColor+'>'
                        +'<a data-toggle="modal" href="#groupInvitePrompt">'
                        +'<i class="fa fa-comment text-red"></i>您收到來自' + result[i].nickname + '的群組邀請'
                        +'</a>'
                        +'</li>'
		    		);
		    		$('#groupInvitePromptButton').click(function(){

						$.ajax({
							url:ajaxURL + 'AnyCourse/GroupMemberServlet.do',
							method:'POST',
							cache:false,
							data:{
								'action': "checkJoinGroup",
								'groupId': groupId
							},
							success:function(response){
								if(response == "true"){
									$('#groupHasJoinedModal').modal('show');
								}
								else{
									$.ajax({
										url:ajaxURL + 'AnyCourse/GroupMemberServlet.do',
										method:'POST',
										cache:false,
										data:{
											'action': "agreeGroupInvitation",
											'groupId': groupId
										},
										success:function(resp){
											$('#groupJoinSuccessModal').modal('show');
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
					});
				}
			}

			
    	},
		error:function(){
			console.log("notification.js append notification error");
		}
	});




	//---------------------------------群組邀請提示----------------------------------------//
	$("body").append('<div class="modal fade" id="groupInvitePrompt" tabindex="-1" role="dialog"'
		+'aria-labelledby="groupInvitePromptLabel" aria-hidden="true">'
		
		+'<div class="modal-dialog" role="document">'
			+'<div class="modal-content">'
				+'<div class="modal-header">'
			    	+'<b class="modal-title" style="font-size:24px;">提示</b>'
			    +'</div>'
				+'<div class="modal-body">'
					+'<h4>確定要加入該群組嗎?</h4>'
				+'</div>'
				+'<div class="modal-footer">'
					+'<button id="groupInvitePromptButton" type="button"'
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
					+'<h4>您已加入該群組</h4>'
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
					+'<h4>您成功加入該群組</h4>'
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
		success:function(response){
			// var number = $('#notificationNumber').text();//暫存原本有幾個通知的變數
			// console.log($('#notificationNumber').text())
			// if(number > 2){
			// 	var num = parseInt(number)-1;
			// 	$('#notificationNumber').text(num);
			// 	$('#notificationHeader').text("You have " + num + " notifications");
			// }
			// else if(number == 2){
			// 	$('#notificationNumber').text(1);
			// 	$('#notificationHeader').text("You have 1 notification");
			// }
			// else if(number == 1){
			// 	$('#notificationNumber').text();
			// 	$('#notificationHeader').text("You have 0 notification");
			// }


			// $('#notificationId_'+notificationId).css("background-color", "#f4f4f4");
		},
		error:function(xhr, ajaxOptions, thrownError){
			console.log(xhr);
			console.log(thrownError);
			console.log("notification.js set isBrowse error");
		}
	});
}
//--------------------------------------------------------------------------------------//

function reloadPage(){
	location.reload();
}