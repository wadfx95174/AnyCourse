var nickNameCheck =false;
var userIdCheck = false;
var passwordCheck = false;
var confirmPwCheck = false;
var emailCheck = false;
$(function () {
  $('input').iCheck({
    checkboxClass: 'icheckbox_square-blue',
    radioClass: 'iradio_square-blue',
    increaseArea: '20%' // optional
  });
});
$('#nickName').change(function nickName(){
 nickNameCheck = false;
    var reg = /^[0-9a-zA-Z\u3E00-\u9FA5]{1,16}$/;//1-16字節，可以中文
    var nickName = $("#nickName").val();
    if (nickName == "") {
		$("#nickNamePrompt").html("暱稱不能為空");
		return false;
	} else if (!reg.test(nickName)) {
		$("#nickNamePrompt").html("不合法的格式(1~16個字)")
		return false;
	} else {
		$("#nickNamePrompt").html("");
		nickNameCheck = true;
		return true;
	}
});
  
$('#userId').change(function userId() {
      //reg1 = /^[a-zA-Z][a-zA-Z0-9]{3,15}$/;// 帳號是否合法(字母開頭，允許4-16字節)
        
      userIdCheck = false;
      var reg = /^[0-9a-zA-Z\u3E00-\u9FA5]{3,15}$/;//4-16字節
      var userName = $("#userId").val();
      if (userName == "") {
          $("#userIdPrompt").html("帳號不能為空");
          return false;
      } else if (!reg.test(userName)) {
          $("#userIdPrompt").html("不合法的格式(4~16個英數組合)");
          return false;
      } else {
          var userFlag = false;
          $.ajaxSetup({
              async : false
          });
	  $.ajax({
              url : "http://localhost:8080/AnyCourse/AnyCourse/pages/examples/LoginVerificationServlet.do",
              data : {
            	  method : "checkExist",
                  userId : $("#userId").val()
              },
              success : function(data) {
                  if (data.userId != null) {
                      $("#userIdPrompt").html("此帳號已存在");
                  } else {
                      $("#userIdPrompt").html("");
                      userIdCheck = true;
                      return userFlag = true;
                  }
              },
              error : function()
              {
            	  alert("fail");
              }
          });
          return userFlag;
      }
});
  
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
  
$('#email').change(function email(){
	emailCheck = false;
	var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z]+$/;
  	var email = $('#email').val();
  	if (email == ""){
        $("#emailPrompt").html("信箱不能為空！");
        return false;
  	} else if (!reg.test(email)) {
  		$("#emailPrompt").html("不合法的信箱格式");
  		return false;
  	} else {
  		$("#emailPrompt").html("");
  		emailCheck = true;
  		return true;
  	}
})

function checkInput(form){
	if (!nickNameCheck)
	{
		alert("請再填寫一次暱稱");
		return false;
	}
	if (!userIdCheck)
	{
		alert("請再填寫一次帳號");
		return false;
	}
	if (!passwordCheck)
	{
		alert("請再填寫一次密碼");
		return false;
	}
	if (!confirmPwCheck)
	{
		alert("請再填寫一次確認密碼");
		return false;
	}
	if (!emailCheck)
	{
		alert("請再填寫一次信箱");
		return false;
	}
	return true;
}