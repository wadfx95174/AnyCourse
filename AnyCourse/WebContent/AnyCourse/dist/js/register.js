var ajaxURL="http://anycourse.cs.ntou.edu.tw:7603/";
//var ajaxURL="http://localhost:8080/";

// 各欄位正確與否 :boolean 
var nickNameCheck =false;
var userIdCheck = false;
var passwordCheck = false;
var confirmPwCheck = false;
var emailCheck = false;

$( document ).ready(function() {
    var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;  
    po.src = 'https://apis.google.com/js/client:plusone.js?onload=render';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);  
});
$(function () {
  $('input').iCheck({
    checkboxClass: 'icheckbox_square-blue',
    radioClass: 'iradio_square-blue',
    increaseArea: '20%' // optional
  });
});

  if ($('#nickName').val())nickName();
  if ($('#userId').val())userId();
  if ($('#password').val())password();
  if ($('#confirmPw').val())confirmPw();
  if ($('#email').val())email();

//檢查"暱稱"欄位 
$('#nickName').change(function nickName(){
 nickNameCheck = false;
    var reg = /^[0-9a-zA-Z\u3E00-\u9FA5]{1,16}$/;//1-16字節，可以中文
    var nickName = $("#nickName").val();
    
    if (nickName == "") {
		$("#nickNamePrompt").html("暱稱不能為空！");
		return false;
	} else if (!reg.test(nickName)) {
		$("#nickNamePrompt").html("不合法的格式！(1~16個字)")
		return false;
	} else {
		$("#nickNamePrompt").html("");
		nickNameCheck = true;
		return true;
	}
});

// 檢查"帳號"欄位 
$('#userId').change(function userId() {
      userIdCheck = false;
      var reg = /^[0-9a-zA-Z\u3E00-\u9FA5]{2,15}$/;//3-16字節
      var userName = $("#userId").val();
      
      if (userName == "") {
          $("#userIdPrompt").html("帳號不能為空！");
          return false;
      } else if (!reg.test(userName)) {
          $("#userIdPrompt").html("不合法的格式！(3~16個英數組合)");
          return false;
      } else {
          var userFlag = false;
          $.ajaxSetup({
              async : false
          });
          
      // 檢查帳號是否已存在
	  $.ajax({
              url : ajaxURL+'AnyCourse/LoginVerificationServlet.do',
              cache :false,
              data : {
            	  method : "checkExist",
                  userId : $("#userId").val()
              },
              success : function(data) {
                  if (data.userId != null) {
                      $("#userIdPrompt").html("此帳號已存在！");
                  } else {
                      $("#userIdPrompt").html("");
                      userIdCheck = true;
                      return userFlag = true;
                  }
              },
              error : function()
              {
            	  //alert("fail");
              }
          }); // end ajax
          return userFlag;
      } // end else
});

//檢查"密碼"欄位
$('#password').change(function password(){
	confirmPw();
	passwordCheck = false;
    var userPwd = $('#password').val();
    
    if (userPwd == "") {
        $("#passwordPrompt").html("密碼不能為空！");
        return false;
    } else if (userPwd.length < 3 || userPwd.length > 16) {
        $("#passwordPrompt").html("長度不符合！");
        return false;
    } else {
        $("#passwordPrompt").html("");
    	passwordCheck = true;
        return true;
    } 
});

// 檢查"確認密碼"欄位
$('#confirmPw').change(confirmPw);
function confirmPw(){
	confirmPwCheck = false;
	var a=$('#password').val();
    var pwd=$.trim(a);
    var b=$('#confirmPw').val();
    var repwd=$.trim(b);
    
    if (repwd == "") {
        $("#confirmPwPrompt").html("確認密碼不能為空！");
        return false;
    } else if (pwd !== repwd) {
        $("#confirmPwPrompt").html("密碼不一致！");
        return false;
    } else {
        $("#confirmPwPrompt").html("");
        confirmPwCheck = true;
        return true;
    } 
}

// 檢查"email"欄位
$('#email').change(function email(){
	emailCheck = false;
	var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z]+$/;
  	var email = $('#email').val();
  	if (email == ""){
        $("#emailPrompt").html("信箱不能為空！");
        return false;
  	} else if (!reg.test(email)) {
  		$("#emailPrompt").html("不合法的信箱格式！");
  		return false;
  	} else {
  		$("#emailPrompt").html("");
  		emailCheck = true;
  		return true;
  	}
})

// 檢查欄位是否填寫正確
function checkInput(form){
	if (!nickNameCheck)
	{
		alert("請再填寫一次暱稱！");
		return false;
	}
	if (!userIdCheck)
	{
		alert("請再填寫一次帳號！");
		return false;
	}
	if (!passwordCheck)
	{
		alert("請再填寫一次密碼！");
		return false;
	}
	if (!confirmPwCheck)
	{
		alert("請再填寫一次確認密碼！");
		return false;
	}
	if (!emailCheck)
	{
		alert("請再填寫一次信箱！");
		return false;
	}
	return true;
}
//------------------Google 登入---------------------
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
