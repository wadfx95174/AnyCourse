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
	})
}) 



//									<div class="member-info">
//										<img src="../../dist/img/user2-160x160.jpg" class="img-circle"
//											alt="User Image" />
//										<h4>小馬</h4>
//									</div>