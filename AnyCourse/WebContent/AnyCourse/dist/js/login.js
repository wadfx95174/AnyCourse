$( document ).ready(function() {
     $.ajax({
    	 url: 'http://140.121.197.130:8400/AnyCourse/LoginVerificationServlet.do',
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