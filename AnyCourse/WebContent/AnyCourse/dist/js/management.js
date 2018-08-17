//var ajaxURL="http://140.121.197.131:7603/";
var ajaxURL="http://localhost:8080/";

function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
}

$(function(){
	checkLogin("../", "../../../");
	$.ajax({
		url: ajaxURL + 'AnyCourse/ManagementServlet.do',
		method: 'GET',
		data: {
			'groupId': get('groupId')
		},
		success: function(result){
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
			console.log('get groupId error');
		}
	})
}) 



//									<div class="member-info">
//										<img src="../../dist/img/user2-160x160.jpg" class="img-circle"
//											alt="User Image" />
//										<h4>小馬</h4>
//									</div>