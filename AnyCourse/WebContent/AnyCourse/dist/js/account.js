var ajaxURL="http://anycourse.cs.ntou.edu.tw:7603/";
//var ajaxURL="http://localhost:8080/";

var ws;

$('.sidebar-menu').slimScroll({
    height: window.screen.height
});

$(document).on('click', '#logout', function logout()
{
    disconnectUser();
})

$(document).ready(function(){
	var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;  
    po.src = 'https://apis.google.com/js/client:plusone.js?onload=render';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);  
})

      
// 取消連結
function disconnectUser() {  
    var revokeUrl = 'https://accounts.google.com/o/oauth2/revoke?token=' + gapi.auth.getToken().access_token;  
    $.ajax({  
        type: 'GET',  
        url: revokeUrl,  
        async: false,  
        contentType: "application/json",  
        dataType: 'jsonp',  
    });  
}  

function checkLogin(htmlUrl, servletUrl)
{
	$.ajax({
    	url: ajaxURL+'AnyCourse/AccountServlet.do',
    	method: 'POST',
    	cache :false,
        async:false,
    	success: function(result){
    		if (result.userId)
    		{
                returnValue = true;
    			$('.navbar-nav').append(
                        ////////////////////通知///////////////////////////////////
                        '<li class="dropdown notifications-menu">'
                            +'<a href="#" class="dropdown-toggle" data-toggle="dropdown">'
                              +'<i class="fa fa-bell-o"></i>'
                              +'<span id="notificationNumber" class="label label-warning"></span>'
                            +'</a>'
                            +'<ul class="dropdown-menu">'
                              +'<li id="notificationHeader" class="header">'
                              +'</li>'
                              +'<li>'
                                +'<ul id="notificationList" class="menu">'
                                +'</ul>'
                              +'</li>'
                              +'<li class="footer"></li>'
                            +'</ul>'
                          +'</li>'
                        ////////////////////////////////////////////////////////////
                        ////////////////////使用者頭像///////////////////////////////
						+'<li class="dropdown user user-menu"><a href="#"'
						+'	class="dropdown-toggle" data-toggle="dropdown"> <img'
						+'		src="'+ajaxURL+'AnyCourse/AnyCourse/dist/img/user2-160x160.jpg" class="user-image"'
						+'		alt="User Image" /> <span>'+result.nickName+'</span>'
						+'</a>'
						+'	<ul class="dropdown-menu">'
						+'		<!-- User image -->'
						+'		<li class="user-header"><img'
						+'			src="'+ajaxURL+'AnyCourse/AnyCourse/dist/img/user2-160x160.jpg" class="img-circle"'
						+'			alt="User Image" />'
						+'			<p>'
						+               result.nickName
						+'			</p></li>'
						+'		<li class="user-footer">'
						+'			<div class="pull-left">'
						+'				<a href="#" class="btn btn-default btn-flat">設定</a>'
						+'			</div>'
						+'			<div class="pull-right">'
						+'				<a href="'+servletUrl+'AccountServlet.do" id="logout" class="btn btn-default btn-flat">登出</a>'
						+'			</div>'
						+'		</li>'
						+'	</ul></li>'
                        ///////////////////////////////////////////////////////////
    			);
    			$('#user-name').html(result.nickName + '，您好！');
    			$('.sidebar-menu').append(
    					'<li class="header">功能列表</li>'
    					+'<li class="active">'
    					   +'<a href="'+ htmlUrl +'../HomePage.html">'
    					       +'<i class="fa fa-home"></i> '
    					       +'<span>首頁</span>'
    					   +'</a>'
    					+'</li>'
    					+'<li>'
    					   +'<a href="'+ htmlUrl +'Personal/VideoListPage.html">'
    					       +'<i class="ion ion-clipboard"></i>'
    					       +'<span>課程清單</span>'
    					   +'</a>'
    					+'</li>'
    					+'<li>'
    					   +'<a href="'+ htmlUrl +'Personal/CoursePlanPage.html">'
    					       +'<i class="fa fa-tasks"></i>'
    					       +'<span>課程計畫</span>'
    					   +'</a>'
    					+'</li>'
    					+'<li>'
    					   +'<a href="'+ htmlUrl +'Personal/CalendarPage.html">'
    					       +'<i class="fa fa-calendar"></i>'
    					       +'<span>行事曆</span>'
    					   +'</a>'
    					+'</li>'
    					+'<li>'
    					   +'<a href="'+ htmlUrl +'Personal/NotePage.html">'
    					       +'<i class="fa fa-book"></i>'
    					       +'<span>筆記</span>'
    					   +'</a>'
    					+'</li>'
    					+'<li>'
    					   +'<a href="'+ htmlUrl +'Personal/KeyLabelPage.html">'
    					       +'<i class="fa fa-tags"></i>'
    					       +'<span>重點標籤</span>'
    					   +'</a>'
    					+'</li>'
    					+'<li class="treeview">'
    					   +'<a href="#">'
    					       +'<i class="fa fa-user"></i> '
    					       +'<span>群組頁面</span><i class="fa fa-angle-left pull-right"></i>'
    					   +'</a>'
    					   +'<ul id="group" class="treeview-menu">'
    					       +'<li>'
    					           +'<a href="'+ htmlUrl +'Group/CreateGroup.html">'
    					               +'<span>---建立群組---</span>'
    					           +'</a>'
    					       +'</li>'
    					   +'</ul>'
    					+'</li>'
    					+'<li class="treeview">'
    					   +'<a href="#">'
    					       +'<i class="fa fa-user"></i> '
    					       +'<span>個人頁面</span><i class="fa fa-angle-left pull-right"></i>'
    					   +'</a>'
    					   +'<ul class="treeview-menu">'
    					       +'<li>'
    					           +'<a href="'+ htmlUrl +'Personal/WatchRecordPage.html">'
    					               +'<i class="fa fa-eye"></i>'
    					               +'<span>觀看紀錄</span>'
    					           +'</a>'
    					       +'</li>'
    					       +'<li>'
    					           +'<a href="'+ htmlUrl +'Personal/SearchRecordPage.html">'
    					               +'<i class="fa fa-clock-o"></i>'
    					               +'<span>查詢紀錄</span>'
    					           +'</a>'
    					       +'</li>'
    					   +'</ul>'
    					+'</li>'
    					+'<li>'
    					+'<a href="'+ htmlUrl +'Setting.html">'
    					+'<i class="fa fa-cog"></i>'
    					+'<span>設定</span>'
    					+'</a>'
    					+'</li>'
    					+'<li>'
    					+'<a href="'+ htmlUrl +'Instructions.html"> '
    					+'<i class="fa fa-question-circle"></i> '
    					+'<span>說明</span>'
    					+'</a>'
    					+'</li>'
    			);
    			$('.treeview').click(function(){
    				$(this).toggleClass('active');
    			})

    			for (var keys in result.groups)
				{
                    console.log(result.groups);
        			$('#group').append('<li>'
        					+'<a href="'+ htmlUrl +'Group/Announcement.html?groupId=' + result.groups[keys] + '">'
        					+'<span>' + keys + '</span>'
        					+'</a>'
        					+'</li>')
				}

                //////////////////////登入時連接WebSocket///////////////////////////
                ws = new WebSocket (getRootUri() + "/AnyCourse/WebSocket");

                //開啟連接WebSocket，並送出userId存在該WebSocket的session中
                ws.onopen = function(event){
                    ws.send(JSON.stringify({
                        type: "connection",
                        userId: result.userId
                    }));
                };
                ///////////////////////////////////////////////////////////////////
    		}
    		else
    		{
                returnValue = false;
    			$('.navbar-nav').append(
    					'<li class="dropdown user user-menu">'
    					+'<a href="'+htmlUrl+'login.html">'
						+'  <span>登入</span>'
						+'</a>'
						+'</li>'
    					+'<li class="dropdown user user-menu">'
    					+'<a href="'+htmlUrl+'register.html">'
						+'  <span>註冊</span>'
						+'</a>'
						+'</li>'
    			);
    			$('#user-name').html('訪客，您好！');
    			$('.sidebar-menu').append(
    					'<li class="header">功能列表</li>'
    					+'<li class="treeview">'
    					+'<a href="'+ htmlUrl +'../HomePage.html">'
    					+'<i class="fa fa-home"></i> '
    					+'<span>首頁</span>'
    					+'</a>'
    					+'</li>'
    					+'<li class="treeview">'
    					+'<a href="'+ htmlUrl +'Instructions.html"> '
    					+'<i class="fa fa-question-circle"></i> '
    					+'<span>說明</span>'
    					+'</a>'
    					+'</li>'
    			);
    		}
    	},
    	error: function(){
    		console.log("get Account error");
    	}
    });
}

////////////////////////////////取得root網址///////////////////////////////////
function getRootUri() {
    return "ws://" + (document.location.hostname == "" ? "localhost" : document.location.hostname) + ":" +
            (document.location.port == "" ? "8080" : document.location.port);
};
//////////////////////////////////////////////////////////////////////////////