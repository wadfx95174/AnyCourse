//////////////localhost用來測試、IP那個用來部屬再tomcat///////////////////////
//var ajaxURL="http://140.121.197.130:8400/";
var ajaxURL="http://localhost:8080/";
///////////////////////////////////////////////////////////////////////

/////////////////////////////一次選取全部/////////////////////////////////
function checkAll(obj,cName) 
{ 
    var checkboxs = document.getElementsByName(cName); 
    for(var i=0;i<checkboxs.length;i++){checkboxs[i].checked = obj.checked;} 
} 

var checkId;
var searchRecordArray;//用來儲存搜尋結果的資料
$(document).ready(function() {
	checkLogin("../", "../../../");
	
	
	//取得使用者的觀看紀錄資料
	$.ajax({
		url : ajaxURL+'AnyCourse/SearchRecordServlet.do',
		method : 'GET', 
		cache :false,
		success:function(result){
			searchRecordArray = new Array(result.length);
    		for(var i = 0 ;i < result.length;i++){
    			$('#SearcRecordList').append('<li class="list-group-item" id="searchRecordId_'+ (i+1) +'">'
						+'<div class="row">'
						+'<div class="col-xs-1 text-left">'
						+'<input name="checkboxItem" type="checkbox"/>'
						+'</div>'
						+'<div class="col-xs-6" id="searchRecordWord_'+ (i+1) +'"><a href="../SearchResult.html?searchQuery='+result[i].searchWord+'">' + result[i].searchWord + '</a></div>'
						+'<div class="col-xs-5" id="searchRecordTime_'+ (i+1) +'">' + result[i].searchTime + '</div>'
						+'<div class="col-xs-1">'
						+'<button type="button" data-toggle="modal" data-target="#deleteModal1" onclick="getId('+(i+1)+')"><i class="fa fa-trash-o" data-toggle="tooltip" data-placement="top" title="刪除"></i></button>'
						+'</div>'
						+'</div></li>');
    			for(var j = 0 ; j < 3;j++){
    				searchRecordArray[i] = new Array(2);
    			}
			}
    		for(var i = 0 ;i < result.length;i++){
    			for(var j = 0 ; j < 2;j++){
    				if(j == 0)searchRecordArray[(i+1)][j] = result[i].searchWord;
    				else searchRecordArray[(i+1)][j] = result[i].searchTime
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
			url : ajaxURL+'AnyCourse/SearchRecordServlet.do',
			method : 'POST',
			cache :false,
		    data : {
		    	"searchWord" : searchRecordArray[checkId][0],
				"searchTime" : searchRecordArray[checkId][1]
			},
			success:function(result){
	    		$("#searchRecordId_"+checkId).remove();
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
//		    	"searchWord" : searchRecordArray[checkId][0],
//				"searchTime" : searchRecordArray[checkId][1]
//			},
//			success:function(result){
//	    		$("#searchRecordId_"+checkId).remove();
//	    		
//	    	},
//			error:function(){alert('failed');}
//		});
//	    
//	  });
});
function getId(id){
    checkId = id;
  }