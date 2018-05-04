//var ajax_url="http://140.121.197.130:8400/";
var ajax_url="http://localhost:8080/";

function check_all(obj,cName) 
{ 
    var checkboxs = document.getElementsByName(cName); 
    for(var i=0;i<checkboxs.length;i++){checkboxs[i].checked = obj.checked;} 
} 

var checkID;
var searchRecordArray;
$(document).ready(function() {
	checkLogin("../", "../../../");
	
	
	//取得資料庫的資料
	$.ajax({
		url : ajax_url+'AnyCourse/SearchRecordServlet.do',
		method : 'GET', 
		cache :false,
		success:function(result){
			searchRecordArray = new Array(result.length);
    		for(var i = 0 ;i < result.length;i++){
    			$('#SearcRecordList').append('<li class="list-group-item" id="searchRecordID_'+ (i+1) +'">'
						+'<div class="row">'
						+'<div class="col-xs-1 text-left">'
						+'<input name="checkboxItem" type="checkbox"/>'
						+'</div>'
						+'<div class="col-xs-7" id="searchRecordWord_'+ (i+1) +'"><a href="../SearchResult.html?search_query='+result[i].search_word+'">' + result[i].search_word + '</a></div>'
						+'<div class="col-xs-3" id="searchRecordTime_'+ (i+1) +'">' + result[i].search_time + '</div>'
						+'<div class="col-xs-1">'
						+'<button type="button" data-toggle="modal" data-target="#deleteModal1" onclick="setID('+(i+1)+')"><i class="fa fa-trash-o" data-toggle="tooltip" data-placement="top" title="刪除"></i></button>'
						+'</div>'
						+'</div></li>');
    			for(var j = 0 ; j < 3;j++){
    				searchRecordArray[i] = new Array(3);
    			}
			}
    		for(var i = 0 ;i < result.length;i++){
    			for(var j = 0 ; j < 3;j++){
    				if(j == 0)searchRecordArray[(i+1)][j] = result[i].user_id;
    				else if(j == 1)searchRecordArray[(i+1)][j] = result[i].search_word;
    				else searchRecordArray[(i+1)][j] = result[i].search_time
//    				console.log(searchRecordArray[i][j]);
    			}
    		}
    	},
		error:function(){console('failed');}
	});
	
	
	//搜尋記錄清單的scroll
	$('#searchList').slimScroll({
	    height: '420px;'
	});
	
	//刪list的item
	$("#deleteListButton1").click(function(e){
	    e.preventDefault();
	    $.ajax({
			url : ajax_url+'AnyCourse/SearchRecordServlet.do',
			method : 'POST',
			cache :false,
		    data : {
		    	"user_id" : searchRecordArray[checkID][0],
		    	"search_word" : searchRecordArray[checkID][1],
				"search_time" : searchRecordArray[checkID][2]
			},
			success:function(result){
	    		$("#searchRecordID_"+checkID).remove();
	    		
	    	},
			error:function(){console.log('failed');}
		});
	    
	  });
	
	//刪除選取的紀錄
//	$("#deleteListButton2").click(function(e){
//	    e.preventDefault();
//	    $.ajax({
//			url : 'http://localhost:8080/AnyCourse/SearchRecordServlet.do',
//			method : 'POST',
//		    data : {
//		    	"user_id" : searchRecordArray[checkID][0],
//		    	"search_word" : searchRecordArray[checkID][1],
//				"search_time" : searchRecordArray[checkID][2]
//			},
//			success:function(result){
//	    		$("#searchRecordID_"+checkID).remove();
//	    		
//	    	},
//			error:function(){alert('failed');}
//		});
//	    
//	  });
});
function setID(id){
    checkID = id;
  }