
document.write("<script type='text/javascript' src='../dist/js/swiper.min.js'></script>");

function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
	}

$(document).ready(function() {
	$.ajax({
		url : ajaxURL+'AnyCourse/HandoutServlet.do',
		method : 'GET',
		cache :false,
		data : {					
			"unitId" : get("unitId")
		},
		success:function(result){
    		for(var i = 0 ;i < result.length;i++){
    			
    			$('#handoutAndLikes').append( 	 
						'<a style=" margin: 10px;" href="'+ result[i].handoutUrl +'" target="_blank" title="'+ result[i].handoutName +'">'+ result[i].handoutName +'</a>'					
	    			);    			
			}	   		
    	},
		error:function(){}
	});
	
});

