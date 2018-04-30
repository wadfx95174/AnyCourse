function checkLogin(htmlUrl, servletUrl)
{
	$.ajax({
    	url: 'http://localhost:8080/AnyCourse/AccountServlet.do',
    	method: 'POST',
    	success: function(result){
    		if (result.userId)
    		{
    			$('.navbar-nav').append(
						'<li class="dropdown user user-menu"><a href="#"'
						+'	class="dropdown-toggle" data-toggle="dropdown"> <img'
						+'		src="http://localhost:8080/AnyCourse/AnyCourse/dist/img/user2-160x160.jpg" class="user-image"'
						+'		alt="User Image" /> <span class="hidden-xs">'+result.nickName+'</span>'
						+'</a>'
						+'	<ul class="dropdown-menu">'
						+'		<!-- User image -->'
						+'		<li class="user-header"><img'
						+'			src="http://localhost:8080/AnyCourse/AnyCourse/dist/img/user2-160x160.jpg" class="img-circle"'
						+'			alt="User Image" />'
						+'			<p>'
						+               result.nickName
						+'			</p></li>'
						+'		<li class="user-footer">'
						+'			<div class="pull-left">'
						+'				<a href="#" class="btn btn-default btn-flat">設定</a>'
						+'			</div>'
						+'			<div class="pull-right">'
						+'				<a href="'+servletUrl+'AccountServlet.do" class="btn btn-default btn-flat">登出</a>'
						+'			</div>'
						+'		</li>'
						+'	</ul></li>'
    			);
    			$('#user-name').html(result.nickName);
    		}
    		else
    		{
    			$('.navbar-nav').append(
    					'<li class="dropdown user user-menu">'
    					+'<a href="'+htmlUrl+'login.html">'
						+'  <span class="hidden-xs">登入</span>'
						+'</a>'
						+'</li>'
    					+'<li class="dropdown user user-menu">'
    					+'<a href="'+htmlUrl+'register.html">'
						+'  <span class="hidden-xs">註冊</span>'
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