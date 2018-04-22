$( document ).ready(function() {
     $.ajax({
    	 url: 'http://localhost:8080/AnyCourse/AnyCourse/pages/examples/LoginVerificationServlet.do',
    	 method : 'GET',
    	 data: {
             method : "checkLogin"
         },
    	 dataType: 'json',
   		 success:function(result){
   			 if(result.userId == null)
   				 alert("請先登入");
   			 else
   				 alert("您已登入");
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