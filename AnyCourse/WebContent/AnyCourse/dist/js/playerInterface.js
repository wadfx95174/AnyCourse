
$('#noteArea').slimScroll({
    height: '200px'
  });
$('.tab-content').slimScroll({
    height: '300px'
  });
$('#keyLabel1').slimScroll({
    height: '130px'
  });
$('#keyLabel2').slimScroll({
    height: '130px'
  });
$('#recommend').slimScroll({
    height: '612px'
  });


var video;
var keyLabelArray;
var maxIndex = 0;
var selectId = 0;
var element;		//存DOM元素

$(document).ready(function(){
    $("#editNote").click(function(){
//    	alert("AAA");
//    	alert($("#text_area").attr("disabled"));
    	if(typeof($("#text_area").attr("disabled")) === "string"){
    		$("#text_area").removeAttr("disabled");
    		$("#noteFooter").slideToggle();
    	}
    	else{
//    		alert("OK");
    		$("#text_area").attr("disabled","false");
    		$("#noteFooter").slideToggle();
    	}   	
    });
    $("#cancelNote").click(function(){
    	$("#text_area").attr("disabled","false");
        $("#noteFooter").slideToggle();
    });
    $( function() {
	    $( "#keyLabel1, #keyLabel2" ).sortable({
	//      connectWith: ".connectedSortable"
	    }).disableSelection();
    });

//----------------------------------------------video----------------------------------------------//

    function get(name)
    {
	   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
	      return decodeURIComponent(name[1]);
   	}
    
    var oHead = document.getElementsByTagName('HEAD').item(0); 
    var oScript= document.createElement("script"); 
    oScript.type = "text/javascript"; 
    oScript.src= (get('type') == "1") ? "../dist/js/youtubePlayer.js" : "../dist/js/jwPlayer.js"; 
    oHead.appendChild(oScript); 
    
//    $.ajax({
//    	url: 'http://localhost:8080/AnyCourse/PlayerInterfaceServlet.do',
//    	method: 'GET',
//    	data: {
//    		"method": 'getVideo',
//    		"type" : get('type'),
//    		"unitId": get('unit_id')
//    	},
//    	error: function(){
//    	},
//    	success: function(response){
//    		var videotype = response.videoUrl.split('.')[1];
//    		if (videotype == "youtube")
//    		{
//    			
//    		}
//    		else
//    		{
//    			$('#vid').append('<video controls="" name="media" id = "myvideo" ><source src="'+response.videoUrl+'" type="video/mp4"></video>');
//    		}
////    		$('source').attr('src', response);		//  <div id="youTubePlayer"></div>
//    		$('h3')[0].append(response.unitName);
////    		$('#introduction').append(response.)
//    	    video=$("#myvideo")[0];
//    	}
//    });
//    alert(get('id'));
    
    
    
	
    
//----------------------------------------------keyLabel----------------------------------------------//    
});