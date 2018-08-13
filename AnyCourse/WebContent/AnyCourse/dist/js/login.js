//var ajaxURL="http://140.121.197.131:7603/";
var ajaxURL="http://localhost:8080/";

var isClick = false;

 
$('#googleLogin').click(function(){
	isClick = true;
});
// 初始化函数  
function render() {  
    gapi.signin.render('googleLogin', {  
        'callback': 'signinCallback',  
        'approvalprompt': 'auto',  
        'clientid': '645783857059-6faluf0otn6641vrdlm2e4oc2tgagbbo.apps.googleusercontent.com',  
        'cookiepolicy': 'single_host_origin',  
        'requestvisibleactions': 'http://schemas.google.com/AddActivity',  
        'scope': 'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email'  
    });  
}  
function signinCallback(authResult) {  
    if (authResult && isClick) {  
        if(authResult["error"]==undefined){  
            gapi.client.load("oauth2","v2",function(){  
                var request=gapi.client.oauth2.userinfo.get();  
                request.execute(function(obj){
                	profile = obj;
                    $.ajax({
           	     	 url: ajaxURL+'AnyCourse/LoginVerificationServlet.do',
           	     	 method : 'POST',
           	     	 data: {
           	              method : "googleLogin",
           	              Account : profile.email.split('@')[0],
           	              Email : profile.email,
           	              Nickname : profile.name,
           	              PictureUrl : profile.picture
           	          },
           	         success:function(result){
           	        	  url = "../HomePage.html";//此處拼接內容
           	        	  window.location.href = url;
           	         },
           	      	 error:function(){
           	      		console.log('google login fail'); 
           	      	 }
           	 	 })
                });  
            });  
        }  
    }  
}  
      
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

$( document ).ready(function() {
    var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;  
    po.src = 'https://apis.google.com/js/client:plusone.js?onload=render';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);  
     $.ajax({
    	 url: ajaxURL+'AnyCourse/LoginVerificationServlet.do',
    	 method : 'GET',
    	 cache :false,
    	 data: {
             method : "checkLogin"
         },
    	 dataType: 'json',
   		 success:function(result){
//   			 if(result.userId == null)
//   				 alert("請先登入");
//   			 else
//   				 alert("您已登入");
   	     },
   		 error:function(){
   		 }
	 })
});
  
$(function () {
  $('input').iCheck({
    checkboxClass: 'icheckbox_square-blue',
    radioClass: 'iradio_square-blue',
    increaseArea: '20%' // optional
  });
});