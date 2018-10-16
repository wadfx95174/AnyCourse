var checkNotificationId;
var backgroundColor;

var groupArray;
var checkId;

//載入頁面
$(document).ready(function() {
	var noId = 1;
	if(ws){
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

		//播放介面-討論區，通知提問者
		if(data.type.match("playerInterfaceComment") != null){

			appendNotification(noId,data.notificationId
						, data.url, 'style="background-color: Silver;"'
						,data.nickname + '回應了你的留言');

		}
		//播放介面-討論區，通知其他回覆者
		else if(data.type.match("playerInterfaceReply") != null){

			appendNotification(noId,data.notificationId
						, data.url, 'style="background-color: Silver;"'
						,data.nickname +'也回應了'+ data.commentNickname +'的留言');
				
		}
		//群組邀請
		else if(data.type.match("groupInvitation") != null){

			var groupName = data.groupName;
			var groupId = data.groupId;

			appendGroupInvitation(noId,groupName,data.notificationId,'style="background-color: Silver;"');

			$('#groupInviteText').text("確定要加入「" + groupName + "」嗎?");
			$('#groupInviteButton').click(function(){

				GroupInvitation(groupId,groupName);

			});
		}
		//群組有新成員加入
		else if(data.type.match("groupMemberJoin") != null){

			appendNotification(noId,data.notificationId, data.url
				,'style="background-color: Silver;"'
				,data.nickname + '加入了「' + data.groupName + '」');

		}
		//群組公告
		else if(data.type.match("groupAnnouncement") != null){

			appendNotification(noId,data.notificationId, data.url
				,'style="background-color: Silver;"'
				,data.nickname + '在「' + data.groupName + '」發出公告');

		}
		//群組共同清單
		else if(data.type.match("shareVideoListToGroup") != null){

			appendNotification(noId,data.notificationId, data.url
				,'style="background-color: Silver;"'
				,data.nickname + '在「' + data.groupName + '」分享課程清單');

		}
		//群組共同計劃
		else if(data.type.match("shareCoursePlanToGroup") != null){

			appendNotification(noId,data.notificationId, data.url
				,'style="background-color: Silver;"'
				,data.nickname + '在「' + data.groupName + '」分享課程計畫');

		}
		//群組討論區_提問
		else if(data.type.match("groupNewComment") != null){

			appendNotification(noId,data.notificationId, data.url
				,'style="background-color: Silver;"'
				,data.nickname + '在「' + data.groupName + '」留言');

		}
		//群組討論區_通知提問者
		else if(data.type.match("groupComment") != null){
			
			appendNotification(noId,data.notificationId
						,data.url, 'style="background-color: Silver;"'
						,data.nickname + '回覆你在「' + data.groupName + '」中的留言');

		}
		//群組討論區_通知其他回覆者
		else if(data.type.match("groupReply") != null){

			appendNotification(noId,data.notificationId
						,data.url, 'style="background-color: Silver;"'
						,data.nickname + '也回應「' + data.groupName + '」中'+ data.commentNickname +'的留言');

		}
		//群組筆記
		else if(data.type.match("shareNoteToGroup") != null){
			appendNotification(noId,data.notificationId, data.url
				,'style="background-color: Silver;"'
				,data.nickname + '在「' + data.groupName + '」分享筆記');
		}
		//群組重點標籤
		else if(data.type.match("shareKeyLabelToGroup") != null){
			appendNotification(noId,data.notificationId, data.url
				,'style="background-color: Silver;"'
				,data.nickname + '在「' + data.groupName + '」分享重點標籤');
		}
		noId++;
    };
	}
	

    var notifications = 0;//還未點擊過的通知數量
    
	$.ajax({
		url : ajaxURL+'AnyCourse/NotificationServlet.do',
		method : 'GET',
		cache :false,
		data:{
			'action': "getNotification"
		},
		success:function(result){
			console.log(result);
			groupIdArray = new Array();
			groupNameArray = new Array();
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

			groupArray = new Array(result.length);
			for(var i = 0 ;i < result.length;i++){

				if(result[i].isBrowse == 0)backgroundColor = 'style="background-color: Silver;"';
	    		else if(result[i].isBrowse == 1)backgroundColor = 'style="background-color: #f4f4f4;"';

	    		//播放介面的討論區回覆
				if(result[i].type.match("playerInterfaceComment") != null){

					appendNotification(noId,result[i].notificationId
						, result[i].url, backgroundColor
						,result[i].nickname + '回應了你的留言');

				}
				//播放介面-討論區，通知其他回覆者
				else if(result[i].type.match("playerInterfaceReply") != null){

					appendNotification(noId,result[i].notificationId
						, result[i].url, backgroundColor
						,result[i].nickname +'也回應了'+ result[i].commentNickname +'的留言');
					
				}
				//群組邀請
				else if(result[i].type.match("groupInvitation") != null){

					var groupName = result[i].groupName;
					var groupId = result[i].groupId;
					
					appendGroupInvitation(noId,groupName,result[i].notificationId,backgroundColor);

					$('#notificationId_'+noId).click(function(){
						$('#groupInviteText').text("確定要加入「" + result[checkId-1].groupName + "」嗎?");
						$('#groupInviteButton').click(function(){

		    				GroupInvitation(result[checkId-1].groupId,result[checkId-1].groupName);

						});
					})

		    		
				}
				//群組有新成員加入
				else if(result[i].type.match("groupMemberJoin") != null){

					appendNotification(noId,result[i].notificationId
						, result[i].url, backgroundColor
						,result[i].nickname + '加入了「' + result[i].groupName + '」');
				
				}
				//群組公告
				else if(result[i].type.match("groupAnnouncement") != null){

					appendNotification(noId,result[i].notificationId
						, result[i].url, backgroundColor
						,result[i].nickname + '在「' + result[i].groupName + '」發出公告');
				
				}
				//群組共同清單
				else if(result[i].type.match("shareVideoListToGroup") != null){

					appendNotification(noId,result[i].notificationId
						, result[i].url, backgroundColor
						,result[i].nickname + '在「' + result[i].groupName + '」分享課程清單');

				}
				//群組共同計畫
				else if(result[i].type.match("shareCoursePlanToGroup") != null){

					appendNotification(noId,result[i].notificationId
						,result[i].url, backgroundColor
						,result[i].nickname + '在「' + result[i].groupName + '」分享課程計畫');

				}
				//群組討論區_提問
				else if(result[i].type.match("groupNewComment") != null){

					appendNotification(noId,result[i].notificationId
						,result[i].url, backgroundColor
						,result[i].nickname + '在「' + result[i].groupName + '」留言');

				}
				//群組討論區_通知提問者
				else if(result[i].type.match("groupComment") != null){

					appendNotification(noId,result[i].notificationId
						,result[i].url, backgroundColor
						,result[i].nickname + '回覆你在「' + result[i].groupName + '」中的留言');
				
				}
				//群組討論區_通知其他回覆者
				else if(result[i].type.match("groupReply") != null){

					appendNotification(noId,result[i].notificationId
						,result[i].url, backgroundColor
						,result[i].nickname + '也回應「' + result[i].groupName + '」中'+ result[i].commentNickname +'的留言');
				
				}
				//群組筆記
				else if(result[i].type.match("shareNoteToGroup") != null){
					appendNotification(noId,result[i].notificationId
						,result[i].url, backgroundColor
						,result[i].nickname + '在「' + result[i].groupName + '」分享筆記');
				}
				//群組重點標籤
				else if(result[i].type.match("shareKeyLabelToGroup") != null){
					appendNotification(noId,result[i].notificationId
						,result[i].url, backgroundColor
						,result[i].nickname + '在「' + result[i].groupName + '」分享重點標籤');
				}

				noId++;
				groupArray[i] = new Array(2);
				groupArray[i][0] = result[i].groupId;
				groupArray[i][0] = result[i].groupName;

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
function setNotificationIsBrowse(notificationId,noId){
	checkId = noId;
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
			//已經加入該群組
			if(response == "true"){
				$('#groupHasJoinedText').text('您已加入'+groupName);
				$('#groupHasJoinedModal').modal('show');
			}
			//尚未加入群組，可以加入
			else{
				$.ajax({
					url:ajaxURL + 'AnyCourse/GroupMemberServlet.do',
					method:'POST',
					cache:false,
					data:{
						'action': "joinGroup",
						'groupId': groupId
					},
					success:function(resp){
						console.log(resp);

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
								// console.log(resp);

								$('#groupJoinSuccessText').text("您成功加入"+groupName);
								$('#groupJoinSuccessModal').modal('show');

								for(var i = 0;i < resp.length;i ++){
									ws.send(JSON.stringify({
						                type: "groupMemberJoin",
						                toUserId: resp[i].toUserId,
						                notificationId: resp[i].notificationId,
						                nickname: resp[i].nickname,
						                groupId: groupId,
						                groupName: groupName,
						                url: resp[i].url
						            }));
								}
							},
							error:function(xhr, ajaxOptions, thrownError){
								console.log(xhr);
								console.log(thrownError);
								console.log("notification.js agree group invitation error");
							}
						});
					},
					error:function(xhr, ajaxOptions, thrownError){
						// console.log(xhr);
						// console.log(thrownError);
						console.log("notification.js join group error");
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

//--------------------------------append通知(群組邀請)-----------------------------------//
function appendGroupInvitation(noId,groupName, notificationId, backgroundColor){
	
	$('#notificationList').append(
		'<li id="notificationId_'+noId+'" onclick="setNotificationIsBrowse('+notificationId+','+noId+')"'+ backgroundColor+'>'
        +'<a data-toggle="modal" href="#groupInviteModal">'
        +'<i class="fa fa-comment text-red"></i>您收到來自「' + groupName + '」的群組邀請'
        +'</a>'
        +'</li>'
	);
}
//--------------------------------------------------------------------------------------//

//--------------------------------append通知(群組邀請除外)-----------------------------------//
function appendNotification(noId,notificationId, url, backgroundColor,message){
			
	$('#notificationList').append(
		'<li id="notificationId_'+noId+'" onclick="setNotificationIsBrowse('+notificationId+','+noId+')"'+ backgroundColor+'>'
	    +'<a href="'+ url +'">'
	    +'<i class="fa fa-comment text-red"></i>' + message
	    +'</a>'
	    +'</li>'
	);
}
//--------------------------------------------------------------------------------------//
