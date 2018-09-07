function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
}

// 設置每個群組內的網址 (ex. 公告、討論區...)
function setGroupUrl()
{
      var groupId = get('groupId');
      $('.tabClass>a').each(function () {
            $(this).attr("href", $(this).attr("href") + '?groupId=' + groupId);
      });
}

// 檢查網址是否沒有 groupId，若沒有則跳轉至首頁
function checkGroupId()
{
      if (get('groupId') == undefined)
            window.location = ajaxURL + 'AnyCourse/AnyCourse/HomePage.html';
}

$(function(){
	checkLogin("../", "../../../");
	// 載入頁面時，先檢查有沒有 groupId 這個參數
	checkGroupId();
	$.ajax({
		url: ajaxURL + 'AnyCourse/ManagementServlet.do',
		method: 'GET',
		data: {
			'groupId': get('groupId')
		},
		success: function(result){
			setGroupUrl();
			console.log(result);
			for (var i = 0; i < result.managers.length; i++)
			{
				$('#manager-area').append('<div class="manager-info">'
										+	'<img src="../../dist/img/user2-160x160.jpg" class="img-circle"'
										+	'alt="User Image" />'
										+ 	'<h4>' + result.managers[i].userName + '</h4>'
										+ '</div>');
			}
			for (var i = 0; i < result.members.length; i++)
			{
				$('#member-area').append('<div class="manager-info">'
										+	'<img src="../../dist/img/user2-160x160.jpg" class="img-circle"'
										+	'alt="User Image" />'
										+ 	'<h4>' + result.members[i].userName + '</h4>'
										+ '</div>');
			}
//			$('section.content').first().append('<a href="CalendarPage.html?groupId=' + get('groupId') + '"><button>Calendar</button></a>');
		},
		error: function(){
			// 當 servlet 沒有回傳東西 -> 非該群組成員
			$('.content-wrapper').first().html('<div><h2 style="text-align:center; padding-top:50px;">很抱歉，您尚未加入該群組</h2></div>');
			console.log('get groupId error');
		}
	});

	//跑出modal時直接把focus放在modal中的格子
	$('#addModal').on('shown.bs.modal', function () {
	  $('#named').focus();
  	});
	

	//邀請組員
	$('#addUserButton').on("click",function(){
		//判斷有無輸入使用者名稱或帳號
		if($("#named").val()==""){
			$("#unAddModal").modal("show");
		}
		else{
			//送出邀請通知，包括WebSocket
			$.ajax({
				url: ajaxURL + 'AnyCourse/GroupMemberServlet.do',
				method: 'POST',
				data: {
					'action':"checkUserExist",
					'groupId':get('groupId'),
					'user':$("#named").val()//userId or nickName
				},
				success: function(response){
					// console.log(response);

					//資料庫中沒有這個使用者
					if(response.situation.match("invalidUser") != null){
						$("#inviteInvalidModal").modal("show");
					}
					//資料庫中有這個使用者，但該使用者已經在該群組中
					else if(response.situation.match("hasJoinedTheUser") != null){
						$("#inviteHasJoinedModal").modal("show");
					}
					//資料庫中有這個使用者
					else if(response.situation.match("legalUser") != null){
						
						$.ajax({
							url: ajaxURL + 'AnyCourse/NotificationServlet.do',
							method: 'POST',
							data: {
								'action':"sendGroupInviteNotification",
								'groupId':get('groupId'),
								'toUserId':response.toUserId
							},
							success: function(resp){
								console.log(resp);

								ws.send(JSON.stringify({
					                type: "groupInvitation",
					                toUserId: resp.toUserId,
					                notificationId: resp.notificationId,
					                nickname: resp.nickname,
					                groupId: resp.groupId,
					                groupName: resp.groupName
					            }));

								$("#inviteSuccess").text("邀請"+$("#named").val()+"成功");
								$('#inviteSuccessModal').modal( 'show' );
							},
							error: function(xhr, ajaxOptions, thrownError){
								// console.log(xhr);
								// console.log(thrownError);
								console.log("managment.js sendGroupInviteNotification error");
							}
						});
					}
				},
				error: function(xhr, ajaxOptions, thrownError){
					// console.log(xhr);
					// console.log(thrownError);
					console.log("managment.js invite error");
				}
			});
			
		}
	});
}) 

function setDialogClear(){
	//清空群組邀請的輸入框
	$('#named').val("");
}


//									<div class="member-info">
//										<img src="../../dist/img/user2-160x160.jpg" class="img-circle"
//											alt="User Image" />
//										<h4>小馬</h4>
//									</div>