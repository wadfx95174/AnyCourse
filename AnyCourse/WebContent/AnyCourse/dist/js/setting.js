//var ajax_url="http://140.121.197.130:8400/";
var ajax_url="http://localhost:8080/";

// 各欄位正確與否 :boolean 
var nickNameCheck =false;
var userIdCheck = false;
var passwordCheck = false;
var confirmPwCheck = false;
var emailCheck = false;


$(document).ready(function() {
	checkLogin("", "../../");	
	  if ($('#nickName').val())nickName();
	  if ($('#email').val())email();
	  if ($('#password').val())password();
	  if ($('#confirmPw').val())confirmPw();
})

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
    	var userFlag = false;
        $.ajaxSetup({
            async : false
        });
        $.ajax({
            url : ajax_url+"AnyCourse/LoginVerificationServlet.do",
            cache :false,
            data : {
          	    method : "checkPassword",
                userId : $("#userId").val()
                passWord
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