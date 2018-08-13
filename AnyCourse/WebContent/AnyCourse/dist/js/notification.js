var ajaxURL="http://140.121.197.131:7603/";
//var ajaxURL="http://localhost:8080/";

$(document).ready(function() {
	$.ajax({
		url : ajaxURL+'AnyCourse/NotificationServlet.do',
		method : 'GET',
		cache :false,
		
		success:function(result){
			$('#notificationNumber').text(result.length);
			$('#introduction').removeClass("active");
			$('#forum').addClass("active");
    		for(var i = 0 ;i < result.length;i++){    			
    			$('#notificationList').append( 	 
    					'<li>'+
                        '<a href="'+ result[i].url +'">'+
                        '<i class="fa fa-users text-red"></i>'+ result[i].nickName +'回應了你的留言'+
                        '</a>'+
                        '</li>'
	    			);    			
				}	   		   		
    	},
		error:function(){}
	});	
});