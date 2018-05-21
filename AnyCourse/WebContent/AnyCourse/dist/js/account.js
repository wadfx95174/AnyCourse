
$(document).on('click', '#logout', function logout()
{
    disconnectUser();
})
<<<<<<< HEAD
//var ajaxURL="http://140.121.197.130:8400/";
var ajaxURL="http://localhost:8080/";
=======
//var ajax_url="http://140.121.197.130:8400/";
var ajax_url="http://localhost:8080/";
>>>>>>> branch 'master' of https://github.com/wadfx95174/AnyCourse.git


$(document).on('click', '#logout', function logout()
{
    disconnectUser();
})

function checkLogin(htmlUrl, servletUrl)
{
	$.ajax({
    	url: ajaxURL+'AnyCourse/AccountServlet.do',
    	method: 'POST',
    	cache :false,
    	success: function(result){
    		if (result.userId)
    		{
    			$('.navbar-nav').append(
						'<li class="dropdown user user-menu"><a href="#"'
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
    			);
    			$('#user-name').html(result.nickName);
    			$('.sidebar-menu').append(
    					'<li class="header">功能列表</li>'
    					+'<li class="treeview">'
    					+'<a href="'+ htmlUrl +'../HomePage.html">'
    					+'<i class="fa fa-home"></i> '
    					+'<span>首頁</span>'
    					+'</a>'
    					+'</li>'
    					+'<li class="treeview">'
    					+'<a href="'+ htmlUrl +'Personal/VideoListPage.html">'
    					+'<i class="ion ion-clipboard"></i>'
    					+'<span>課程清單</span>'
    					+'</a>'
    					+'</li>'
    					+'<li class="treeview">'
    					+'<a href="'+ htmlUrl +'Personal/CoursePlanPage.html">'
    					+'<i class="fa fa-tasks"></i>'
    					+'<span>課程計畫</span>'
    					+'</a>'
    					+'</li>'
    					+'<li class="treeview">'
    					+'<a href="'+ htmlUrl +'Personal/CalendarPage.html">'
    					+'<i class="fa fa-calendar"></i>'
    					+'<span>行事曆</span>'
    					+'</a>'
    					+'</li>'
    					+'<li class="treeview">'
    					+'<a href="'+ htmlUrl +'Personal/NotePage.html">'
    					+'<i class="fa fa-book"></i>'
    					+'<span>筆記</span>'
    					+'</a>'
    					+'</li>'
    					+'<li class="treeview">'
    					+'<a href="'+ htmlUrl +'Personal/KeyLabelPage.html">'
    					+'<i class="fa fa-tags"></i>'
    					+'<span>重點標籤</span>'
    					+'</a>'
    					+'</li>'
    					+'<li class="active treeview">'
    					+'<a href="#">'
    					+'<i class="fa fa-user"></i> '
    					+'<span>個人頁面</span><i class="fa fa-angle-left pull-right"></i>'
    					+'</a>'
    					+'<ul class="treeview-menu ">'
    					+'<li class="active">'
    					+'<a href="'+ htmlUrl +'Personal/WatchRecordPage.html">'
    					+'<i class="fa fa-eye"></i>'
    					+'<span>觀看紀錄</span>'
    					+'</a>'
    					+'</li>'
    					+'<li class="active">'
    					+'<a href="'+ htmlUrl +'Personal/SearchRecordPage.html">'
    					+'<i class="fa fa-clock-o"></i>'
    					+'<span>查詢紀錄</span>'
    					+'</a>'
    					+'</li>'
    					+'</ul>'
    					+'</li>'
    					+'<li class="treeview">'
    					+'<a href="'+ htmlUrl +'Setting.html">'
    					+'<i class="fa fa-cog"></i>'
    					+'<span>設定</span>'
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
    		else
    		{
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