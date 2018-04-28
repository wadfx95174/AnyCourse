
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
    height: '300px'
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
    
    $('#list').slimScroll({
        height: '300px'
      });
    $('#list').attr('class',  'box box-primary');
    $('#list').append('<div class="box-header ui-sortable-handle">'
                      + '    <i class="ion ion-clipboard"></i>'
                      + '     <h3 class="box-title"><strong>清單A</strong></h3>'
                     + '  </div><!-- /.box-header -->'
                     + '   <div class="box-body">'
                     + '      <div class="list-group"  id="list">'
                      + '        <a href="PlayerInterface.html" class="list-group-item">'
                      + '           <div class="media">'
                      + '              <div class="col-xs-6 pull-left" style="padding-left: 0px;">'
                      + '                 <div class="embed-responsive embed-responsive-16by9">          '
                       + '                   <img id="img" class="style-scope yt-img-shadow" alt="" width="210" src="https://i.ytimg.com/vi/B-AoHE6dPnk/hqdefault.jpg">'        
                      + '                 </div>'
                      + '              </div>'
                     + '               <div class="media-body">'
                      + '                 <h3 class="media-heading">導涵式公式的推廣</h3>'
                     + '                  <p>中央大學 王老師</p>'
                      + '                 <p>讚數:216,165</p>'
                      + '              </div>'
                      + '           </div>'
                      + '        </a>'
                       + '    </div>'                           
                     + '   </div><!--box-body-->'); 
    
    
	
    
//----------------------------------------------keyLabel----------------------------------------------//    
});