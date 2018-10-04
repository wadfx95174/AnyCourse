var state;
var commentId =null;
var replyId =null;
var groupId = 1;
var userId;
var nickName;
var commentTime;
var commentContent;
var replyTime;
var replyContent;
var urlId;

// 取網址列的參數
function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
}

// 設置每個群組內的網址 (ex. 公告、討論區...)
function setGroupUrl()
{
      var groupId = get('groupId');
      $('.tabClass>a').each(function () {
            $(this).attr("href", $(this).attr("href") + '?groupId=' + groupId);
      });
}

// 檢查網址是否沒有 groupId，若沒有則跳轉至首頁
function checkGroupId()
{
      if (get('groupId') == undefined)
            window.location = ajaxURL + 'AnyCourse/AnyCourse/HomePage.html';
}


//新增comment
function setComment(){	
	
	if($("#groupcommentArea").val() !== ''){
		var dt = new Date();
		commentContent = document.getElementById('groupcommentArea').value;
		$("#groupcommentArea").val('');
		$.ajax({
			url : ajaxURL+'AnyCourse/GroupCommentServlet.do',
			method : 'POST',
			cache :false,
			data : {
				"state" : "insert",	
				"groupId" : get('groupId'),
				"userId" : userId,
				"nickName" : nickName,
				"commentContent" : commentContent,			
			},
			success : function(result) {
				$('#groupcomment-body').append(  	 
						'<div id="com_' + result.commentId + '" class="B col-xs-12" >'+	
						'<img src="https://ppt.cc/fxYEnx@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
						'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + result.nickName + '</h4>'+
						'<h5 style="float:right;">' + result.commentTime + '</h5>'+														
						'<textarea class="col-xs-12" rows="2" cols="50" id="comment_' + result.commentId + '" disabled="disabled" style="float:left;">' + result.commentContent + '</textarea>'+																			
						'<button id="comment4_' + result.commentId + '" type="button" class="btn btn-default btnCss" style="display:none;" onclick="updateComment(this.id)">確認</button>'+
						'<button id="comment5_' + result.commentId + '" type="button" class="btn btn-default btnCss" style="display:none;" onclick="can(this.id)">取消</button>'+
						'<button id="comment1_' + result.commentId + '" type="button" class="btn btn-default btnCss" onclick="deleteComment(this.id)">刪除</button>'+
						'<button id="comment2_' + result.commentId + '" type="button" class="btn btn-default btnCss" onclick="edit(this.id)">編輯</button>'+
						'<button id="comment3_' + result.commentId + '" type="button" class="btn btn-default btnCss" onclick="displayReply(this.id)">回覆</button>'+																														
						'</div>'+
						'<div id="reply_div_' + result.commentId + '" class="B col-xs-12" style="display:none;">'+
						'<textarea id="reply_area_' + result.commentId +'" class="col-xs-12" rows="3" cols="50" placeholder="Reply something..." style="float:left;"></textarea>'+
						'<button id="setReply_' + result.commentId +'"type="button" class="btn btn-default btnCss" onclick="setReply(this.id)">確認</button>'+
						'<button id="hide_' + result.commentId +'" type="button" class="btn btn-default btnCss" onclick="displayReply(this.id)">取消</button>'+
						'</div>'
	    			);
			},
			error: function(){
                // 當 servlet 沒有回傳東西 -> 非該群組成員
                $('.content-wrapper').first().html('<div><h2 style="text-align:center; padding-top:50px;">很抱歉，您尚未加入該群組</h2></div>');
                console.log('get groupId error');
			}
		})				
	}	
}
//載入頁面
$(document).ready(function() {
	checkLogin("../", "../../../");
    
    // 載入頁面時，先檢查有沒有 groupId 這個參數
    checkGroupId();
	
	$.ajax({
		url : ajaxURL+'AnyCourse/GroupCommentServlet.do',
		method : 'GET',
		cache :false,
		data : {
			"groupId" : get('groupId'),
		},
		success:function(result){
			setGroupUrl();
			
    		for(var i = 0 ;i < result.length;i++){
    			
    			$('#groupcomment-body').append( 	 
						'<div id="com_' + result[i].commentId + '" class="B col-xs-12">'+
						'<img src="https://ppt.cc/fxYEnx@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
						'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + result[i].nickName + '</h4>'+
						'<h5 style="float:right;">' + result[i].commentTime + '</h5>'+														
						'<textarea class="col-xs-12" rows="2" cols="50" id="comment_' + result[i].commentId + '" disabled="disabled" style="float:left;">' + result[i].commentContent + '</textarea>'+																			
						'<button id="comment4_' + result[i].commentId + '" type="button" class="btn btn-default btnCss" style="display:none;" onclick="updateComment(this.id)">確認</button>'+
						'<button id="comment5_' + result[i].commentId + '" type="button" class="btn btn-default btnCss" style="display:none;" onclick="can(this.id)">取消</button>'+
						'<button id="comment1_' + result[i].commentId + '" type="button" class="btn btn-default btnCss" onclick="deleteComment(this.id)">刪除</button>'+
						'<button id="comment2_' + result[i].commentId + '" type="button" class="btn btn-default btnCss" onclick="edit(this.id)">編輯</button>'+
						'<button id="comment3_' + result[i].commentId + '" type="button" class="btn btn-default btnCss" onclick="displayReply(this.id)">回覆</button>'+																																				
						'</div>'+
						'<div id="reply_div_' + result[i].commentId + '" class="B col-xs-12" style="display:none;">'+
						'<textarea id="reply_area_' + result[i].commentId +'" class="col-xs-12" rows="3" cols="50" placeholder="Reply something..." style="float:left;"></textarea>'+
						'<button id="setReply_' + result[i].commentId +'"type="button" class="btn btn-default btnCss" onclick="setReply(this.id)">確認</button>'+
						'<button id="hide_' + result[i].commentId +'" type="button" class="btn btn-default btnCss" onclick="displayReply(this.id)">取消</button>'+
						'</div>'
	    			);    			
			}	
    		$.ajax({
    			url : ajaxURL+'AnyCourse/GroupReplyServlet.do',
    			method : 'GET',
    			data: {
    				'groupId': get('groupId')
    			},  
    			cache :false,
    			success : function(result) {
    				for(var i = 0 ;i < result.length;i++){
    				$('#com_'+result[i].commentId).append( 	
    						'<div id="rep_' + result[i].replyId + '"class="col-xs-12 C" >'+
    						'<img src="https://ppt.cc/fi5Q0x@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
    						'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + result[i].nickName +'</h4>'+
    						'<h5 style="float:right;">' + result[i].replyTime +'</h5>'+													
    						'<textarea class="col-xs-12" rows="2" cols="50" id="reply_' + result[i].replyId + '" disabled="disabled" style="float:left;">' + result[i].replyContent + '</textarea>'+																			
    						'<button id="reply3_' + result[i].replyId + '_' + result[i].commentId +'" type="button" class="btn btn-default btnCss" style="display:none;" onclick="updateReply(this.id)">確認</button>'+
    						'<button id="reply4_' + result[i].replyId + '_' + result[i].commentId +'" type="button" class="btn btn-default btnCss" style="display:none;" onclick="can2(this.id)">取消</button>'+	
    						'<button id="reply1_' + result[i].replyId + '_' + result[i].commentId +'" type="button" class="btn btn-default btnCss" onclick="deleteReply(this.id)">刪除</button>'+
    						'<button id="reply2_' + result[i].replyId + '_' + result[i].commentId +'" type="button" class="btn btn-default btnCss" onclick="edit2(this.id)">編輯</button>'+							
    						'</div>'																	
    					);
    				}
    			},
    			error: function(){
                    // 當 servlet 沒有回傳東西 -> 非該群組成員
                    $('.content-wrapper').first().html('<div><h2 style="text-align:center; padding-top:50px;">很抱歉，您尚未加入該群組</h2></div>');
                    console.log('get groupId error');
    			}
    		})
    	},
		error:function(){}
	});
	
});
//跳出reply框
function displayReply(input){
	var id = input.split('_')[1]; 
	$("#reply_div_" + id ).toggle();
}
//新增reply
function setReply(input){

	var id = input.split('_')[1];
	var url = location.href;
	
	if($("#reply_area_"+id).val() !== ''){
		var dt = new Date();
		replyContent = document.getElementById('reply_area_'+id).value;
		$("#reply_area_"+id).val('');		
		$.ajax({
			url : ajaxURL+'AnyCourse/GroupReplyServlet.do',
			method : 'POST',
			cache :false,
			data : {
				"state" : "insert",	
				"commentId" : id,
//				"userId" : userId,
				"nickName" : nickName,
				"replyContent" : replyContent,
				"groupId" : get('groupId'),
			},
			success : function(result) {
				urlId = url+"#rep_"+result.replyId;
//				alert(urlId);
				$('#com_'+id).append( 	
						'<div id="rep_' + result.replyId + '"'+'name="rep_' + result.replyId +'" class="col-xs-12 C" >'+
						'<img src="https://ppt.cc/fi5Q0x@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
						'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + result.nickName +'</h4>'+
						'<h5 style="float:right;">' + result.replyTime +'</h5>'+													
						'<textarea class="col-xs-12" rows="2" cols="50" id="reply_' + result.replyId + '" disabled="disabled" style="float:left;">' + result.replyContent + '</textarea>'+																			
						'<button id="reply3_' + result.replyId + '_' + result.commentId +'" type="button" class="btn btn-default btnCss" style="display:none;" onclick="updateReply(this.id)">確認</button>'+
						'<button id="reply4_' + result.replyId + '_' + result.commentId +'" type="button" class="btn btn-default btnCss" style="display:none;" onclick="can2(this.id)">取消</button>'+	
						'<button id="reply1_' + result.replyId + '_' + result.commentId +'" type="button" class="btn btn-default btnCss" onclick="deleteReply(this.id)">刪除</button>'+
						'<button id="reply2_' + result.replyId + '_' + result.commentId +'" type="button" class="btn btn-default btnCss" onclick="edit2(this.id)">編輯</button>'+							
						'</div>'																
	    			);	
				$("#reply_div_" + id ).toggle();
//				$.ajax({					
//					url : ajaxURL+'AnyCourse/NotificationServlet.do',
//					method : 'POST',
//					cache :false,
//					data : {
//						"state" : "insert",	
//						"commentId" : id,
////						"userId" : userId,
////						"nickName" : nickName,
////						"replyContent" : replyContent,	
//						"url" : urlId,
//					},
//					success : function(result) {
//						
//					},
//					error: function(){
//	                    // 當 servlet 沒有回傳東西 -> 非該群組成員
//	                    $('.content-wrapper').first().html('<div><h2 style="text-align:center; padding-top:50px;">很抱歉，您尚未加入該群組</h2></div>');
//	                    console.log('get groupId error');
//	  		}
//				})
			},
			error: function (jqXHR, textStatus, errorThrown) {
	         }
			})			
	}			
}
//刪除reply
function deleteReply(input){	

	var id = input.split('_')[1];
	
	$.ajax({
		url : ajaxURL+'AnyCourse/GroupReplyServlet.do',
		method : 'POST',
		cache :false,
		data : {
			"state" : "delete",
			"replyId" : id,					
		},				
		success : function(data) {
			$("#rep_"+id).remove();
		},
	});
}
//刪除comment   
function deleteComment(input){

	var id = input.split('_')[1];
	
	$.ajax({
		url : ajaxURL+'AnyCourse/GroupCommentServlet.do',
		method : 'POST',
		cache :false,
		data : {
			"state" : "delete",
			"commentId" : id,	
		},				
		success : function(data) {
			$("#com_"+id).remove();
			$.ajax({
				url : ajaxURL+'AnyCourse/GroupReplyServlet.do',
				method : 'POST',
				data : {
					"state" : "delete2",
					"commentId" : id,	
				},				
				success : function(data) {
				},
			});
		},
	});	
}
//comment編輯框點選取消,comment1_+id 刪除button,comment2_+id 編輯button,comment3_+id 回覆button,comment4_+id 確認button,comment5_+id 取消button
function can(input){
	var id = input.split('_')[1];
	$("#comment_"+id).attr("disabled","false");
	$("#comment1_"+id).toggle();
	$("#comment2_"+id).toggle();
	$("#comment3_"+id).toggle();
	$("#comment4_"+id).toggle();
	$("#comment5_"+id).toggle();
	
}
//跳出comment編輯框,comment1_+id 刪除button,comment2_+id 編輯button,comment3_+id 回覆button,comment4_+id 確認button,comment5_+id 取消button
function edit(input){
	var id = input.split('_')[1];
	$("#comment_"+id).removeAttr("disabled");
	$("#comment1_"+id).toggle();
	$("#comment2_"+id).toggle();
	$("#comment3_"+id).toggle();
	$("#comment4_"+id).toggle();
	$("#comment5_"+id).toggle();	
}
//update comment,comment1_+id 刪除button,comment2_+id 編輯button,comment3_+id 回覆button,comment4_+id 確認button,comment5_+id 取消button
function updateComment(input){

	var id = input.split('_')[1];

	commentContent = document.getElementById('comment_'+id).value;

	$.ajax({  
		url : ajaxURL+'AnyCourse/GroupCommentServlet.do',
		method : 'POST',
		cache :false,
		data : {
			"state" : "update",
			"groupId" : get('groupId'),
			"commentId" : id,
			"userId" : userId,
			"nickName" : nickName,
			"commentContent" : commentContent	
		},				
		success : function(data) {
			$('#comment_'+id).append( data.commentContent);
			commentId = data.commentId;
			$("#comment_"+id).attr("disabled","false");
			$("#comment1_"+id).toggle();
			$("#comment2_"+id).toggle();
			$("#comment3_"+id).toggle();
			$("#comment4_"+id).toggle();
			$("#comment5_"+id).toggle();
		},
		error : function() {
		}
	});
};
//reply編輯框點選取消,reply1_+id 刪除button,reply2_+id 編輯button,reply3_+id 確認button,reply4_+id 取消button
function can2(input){
	var id = input.split('_')[1];
	var id2 = input.split('_')[2];
	$("#reply_"+id).attr("disabled","false");
	$("#reply1_"+id+"_"+id2).toggle();
	$("#reply2_"+id+"_"+id2).toggle();
	$("#reply3_"+id+"_"+id2).toggle();
	$("#reply4_"+id+"_"+id2).toggle();
	
}
//跳出reply編輯框,reply1_+id 刪除button,reply2_+id 編輯button,reply3_+id 確認button,reply4_+id 取消button
function edit2(input){
	var id = input.split('_')[1];
	var id2 = input.split('_')[2];
	$("#reply_"+id).removeAttr("disabled");
	$("#reply1_"+id+"_"+id2).toggle();
	$("#reply2_"+id+"_"+id2).toggle();
	$("#reply3_"+id+"_"+id2).toggle();
	$("#reply4_"+id+"_"+id2).toggle();	
}
//update reply,reply1_+id 刪除button,reply2_+id 編輯button,reply3_+id 確認button,reply4_+id 取消button
function updateReply(input){

	var id = input.split('_')[1];
	var id2 = input.split('_')[2];

	replyContent = document.getElementById('reply_'+id).value;

	$.ajax({  
		url : ajaxURL+'AnyCourse/GroupReplyServlet.do',
		method : 'POST',
		cache :false,
		data : {
			"state" : "update",
			"replyId" : id,
			"commentId" : id2,
			"userId" : userId,
			"nickName" : nickName,
			"replyContent" : replyContent,	
		},				
		success : function(data) {
			$('#reply_'+id).append( data.replyContent);
			replyId = data.replyId;
			$("#reply_"+id).attr("disabled","false");
			$("#reply1_"+id+"_"+id2).toggle();
			$("#reply2_"+id+"_"+id2).toggle();
			$("#reply3_"+id+"_"+id2).toggle();
			$("#reply4_"+id+"_"+id2).toggle();
		},
		error : function() {
		}
	});
};



//var state;
//var commentId =null;
//var replyId =null;
//var groupId = 1;
//var userId;
//var nickName;
//var commentTime;
//var commentContent;
//var replyTime;
//var replyContent;
//var urlId;
//
//function get(name)
//{
//   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
//      return decodeURIComponent(name[1]);
//	}
////新增comment
//function setComment(){	
//	if($("#commentArea").val() !== ''){
//		var dt = new Date();
//		commentContent = document.getElementById('commentArea').value;
//		$("#commentArea").val('');
//		$.ajax({
//			url : ajaxURL+'AnyCourse/GroupCommentServlet.do',
//			method : 'POST',
//			cache :false,
//			data : {
//				"state" : "insert",	
//				"groupId" : groupId,
//				"userId" : userId,
//				"nickName" : nickName,
//				"commentContent" : commentContent,			
//			},
//			success : function(result) {
//				$('#comment-body').append(  	 
//						'<div id="com_' + result.commentId + '" class="B" >'+	
//						'<img src="https://ppt.cc/fxYEnx@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
//						'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + result.nickName + '</h4>'+
//						'<h5 style="float:right;">' + result.commentTime + '</h5>'+														
//						'<textarea class="col-xs-12" rows="2" cols="50" id="comment_' + result.commentId + '" disabled="disabled" style="float:left;">' + result.commentContent + '</textarea>'+																			
//						'<button id="comment4_' + result.commentId + '" type="button" class="btn btn-default btnCss" style="display:none;" onclick="updateComment(this.id)">確認</button>'+
//						'<button id="comment5_' + result.commentId + '" type="button" class="btn btn-default btnCss" style="display:none;" onclick="can(this.id)">取消</button>'+
//						'<button id="comment1_' + result.commentId + '" type="button" class="btn btn-default btnCss" onclick="deleteComment(this.id)">刪除</button>'+
//						'<button id="comment2_' + result.commentId + '" type="button" class="btn btn-default btnCss" onclick="edit(this.id)">編輯</button>'+
//						'<button id="comment3_' + result.commentId + '" type="button" class="btn btn-default btnCss" onclick="displayReply(this.id)">回覆</button>'+																														
//						'</div>'+
//						'<div id="reply_div_' + result.commentId + '" class="B" style="display:none;">'+
//						'<textarea id="reply_area_' + result.commentId +'" class="col-xs-12" rows="3" cols="50" placeholder="Reply something..." style="float:left;"></textarea>'+
//						'<button id="setReply_' + result.commentId +'"type="button" class="btn btn-default btnCss" onclick="setReply(this.id)">確認</button>'+
//						'<button id="hide_' + result.commentId +'" type="button" class="btn btn-default btnCss" onclick="displayReply(this.id)">取消</button>'+
//						'</div>'
//	    			);
//			},
//			error: function (jqXHR, textStatus, errorThrown) {
//	         }
//		})				
//	}	
//}
////載入頁面
//$(document).ready(function() {
//	$.ajax({
//		url : ajaxURL+'AnyCourse/GroupCommentServlet.do',
//		method : 'GET',
//		cache :false,
//		data : {
//			"groupId" : groupId,
//		},
//		success:function(result){
//    		for(var i = 0 ;i < result.length;i++){
//    			
//    			$('#comment-body').append( 	 
//						'<div id="com_' + result[i].commentId + '" class="B">'+
//						'<img src="https://ppt.cc/fxYEnx@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
//						'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + result[i].nickName + '</h4>'+
//						'<h5 style="float:right;">' + result[i].commentTime + '</h5>'+														
//						'<textarea class="col-xs-12" rows="2" cols="50" id="comment_' + result[i].commentId + '" disabled="disabled" style="float:left;">' + result[i].commentContent + '</textarea>'+																			
//						'<button id="comment4_' + result[i].commentId + '" type="button" class="btn btn-default btnCss" style="display:none;" onclick="updateComment(this.id)">確認</button>'+
//						'<button id="comment5_' + result[i].commentId + '" type="button" class="btn btn-default btnCss" style="display:none;" onclick="can(this.id)">取消</button>'+
//						'<button id="comment1_' + result[i].commentId + '" type="button" class="btn btn-default btnCss" onclick="deleteComment(this.id)">刪除</button>'+
//						'<button id="comment2_' + result[i].commentId + '" type="button" class="btn btn-default btnCss" onclick="edit(this.id)">編輯</button>'+
//						'<button id="comment3_' + result[i].commentId + '" type="button" class="btn btn-default btnCss" onclick="displayReply(this.id)">回覆</button>'+																																				
//						'</div>'+
//						'<div id="reply_div_' + result[i].commentId + '" class="B" style="display:none;">'+
//						'<textarea id="reply_area_' + result[i].commentId +'" class="col-xs-12" rows="3" cols="50" placeholder="Reply something..." style="float:left;"></textarea>'+
//						'<button id="setReply_' + result[i].commentId +'"type="button" class="btn btn-default btnCss" onclick="setReply(this.id)">確認</button>'+
//						'<button id="hide_' + result[i].commentId +'" type="button" class="btn btn-default btnCss" onclick="displayReply(this.id)">取消</button>'+
//						'</div>'
//	    			);    			
//			}	
//    		$.ajax({
//    			url : ajaxURL+'AnyCourse/GroupReplyServlet.do',
//    			method : 'GET',
//    			cache :false,
//    			success : function(result) {
//    				for(var i = 0 ;i < result.length;i++){
//    				$('#com_'+result[i].commentId).append( 	
//    						'<div id="rep_' + result[i].replyId + '"class="col-xs-12 C" >'+
//    						'<img src="https://ppt.cc/fi5Q0x@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
//    						'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + result[i].nickName +'</h4>'+
//    						'<h5 style="float:right;">' + result[i].replyTime +'</h5>'+													
//    						'<textarea class="col-xs-12" rows="2" cols="50" id="reply_' + result[i].replyId + '" disabled="disabled" style="float:left;">' + result[i].replyContent + '</textarea>'+																			
//    						'<button id="reply3_' + result[i].replyId + '_' + result[i].commentId +'" type="button" class="btn btn-default btnCss" style="display:none;" onclick="updateReply(this.id)">確認</button>'+
//    						'<button id="reply4_' + result[i].replyId + '_' + result[i].commentId +'" type="button" class="btn btn-default btnCss" style="display:none;" onclick="can2(this.id)">取消</button>'+	
//    						'<button id="reply1_' + result[i].replyId + '_' + result[i].commentId +'" type="button" class="btn btn-default btnCss" onclick="deleteReply(this.id)">刪除</button>'+
//    						'<button id="reply2_' + result[i].replyId + '_' + result[i].commentId +'" type="button" class="btn btn-default btnCss" onclick="edit2(this.id)">編輯</button>'+							
//    						'</div>'																	
//    					);
//    				}
//    			},
//    			error: function (jqXHR, textStatus, errorThrown) {
//    		     }
//    		})
//    	},
//		error:function(){}
//	});
//	
//});
////跳出reply框
//function displayReply(input){
//	var id = input.split('_')[1]; 
//	$("#reply_div_" + id ).toggle();
//}
////新增reply
//function setReply(input){
//	var id = input.split('_')[1];
//	var url = location.href;
//	
//	if($("#reply_area_"+id).val() !== ''){
//		var dt = new Date();
//		replyContent = document.getElementById('reply_area_'+id).value;
//		$("#reply_area_"+id).val('');		
//		$.ajax({
//			url : ajaxURL+'AnyCourse/GroupReplyServlet.do',
//			method : 'POST',
//			cache :false,
//			data : {
//				"state" : "insert",	
//				"commentId" : id,
////				"userId" : userId,
//				"nickName" : nickName,
//				"replyContent" : replyContent,			
//			},
//			success : function(result) {
//				urlId = url+"#rep_"+result.replyId;
////				alert(urlId);
//				$('#com_'+id).append( 	
//						'<div id="rep_' + result.replyId + '"'+'name="rep_' + result.replyId +'" class="col-xs-12 C" >'+
//						'<img src="https://ppt.cc/fi5Q0x@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
//						'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + result.nickName +'</h4>'+
//						'<h5 style="float:right;">' + result.replyTime +'</h5>'+													
//						'<textarea class="col-xs-12" rows="2" cols="50" id="reply_' + result.replyId + '" disabled="disabled" style="float:left;">' + result.replyContent + '</textarea>'+																			
//						'<button id="reply3_' + result.replyId + '_' + result.commentId +'" type="button" class="btn btn-default btnCss" style="display:none;" onclick="updateReply(this.id)">確認</button>'+
//						'<button id="reply4_' + result.replyId + '_' + result.commentId +'" type="button" class="btn btn-default btnCss" style="display:none;" onclick="can2(this.id)">取消</button>'+	
//						'<button id="reply1_' + result.replyId + '_' + result.commentId +'" type="button" class="btn btn-default btnCss" onclick="deleteReply(this.id)">刪除</button>'+
//						'<button id="reply2_' + result.replyId + '_' + result.commentId +'" type="button" class="btn btn-default btnCss" onclick="edit2(this.id)">編輯</button>'+							
//						'</div>'																
//	    			);	
//				$("#reply_div_" + id ).toggle();
//				$.ajax({					
//					url : ajaxURL+'AnyCourse/NotificationServlet.do',
//					method : 'POST',
//					cache :false,
//					data : {
//						"state" : "insert",	
//						"commentId" : id,
////						"userId" : userId,
////						"nickName" : nickName,
////						"replyContent" : replyContent,	
//						"url" : urlId,
//					},
//					success : function(result) {
//						
//					},
//					error: function (jqXHR, textStatus, errorThrown) {
////						alert("BBB");
//			         }
//				})
//			},
//			error: function (jqXHR, textStatus, errorThrown) {
//	         }
//			})			
//	}			
//}
////刪除reply
//function deleteReply(input){
//	var id = input.split('_')[1];
//	
//	$.ajax({
//		url : ajaxURL+'AnyCourse/GroupReplyServlet.do',
//		method : 'POST',
//		cache :false,
//		data : {
//			"state" : "delete",
//			"replyId" : id,					
//		},				
//		success : function(data) {
//			$("#rep_"+id).remove();
//		},
//	});
//}
////刪除comment   
//function deleteComment(input){
//	var id = input.split('_')[1];
//	
//	$.ajax({
//		url : ajaxURL+'AnyCourse/GroupCommentServlet.do',
//		method : 'POST',
//		cache :false,
//		data : {
//			"state" : "delete",
//			"commentId" : id,					
//		},				
//		success : function(data) {
//			$("#com_"+id).remove();
//			$.ajax({
//				url : ajaxURL+'AnyCourse/GroupReplyServlet.do',
//				method : 'POST',
//				data : {
//					"state" : "delete2",
//					"commentId" : id,					
//				},				
//				success : function(data) {
//				},
//			});
//		},
//	});	
//}
////comment編輯框點選取消,comment1_+id 刪除button,comment2_+id 編輯button,comment3_+id 回覆button,comment4_+id 確認button,comment5_+id 取消button
//function can(input){
//	var id = input.split('_')[1];
//	$("#comment_"+id).attr("disabled","false");
//	$("#comment1_"+id).toggle();
//	$("#comment2_"+id).toggle();
//	$("#comment3_"+id).toggle();
//	$("#comment4_"+id).toggle();
//	$("#comment5_"+id).toggle();
//	
//}
////跳出comment編輯框,comment1_+id 刪除button,comment2_+id 編輯button,comment3_+id 回覆button,comment4_+id 確認button,comment5_+id 取消button
//function edit(input){
//	var id = input.split('_')[1];
//	$("#comment_"+id).removeAttr("disabled");
//	$("#comment1_"+id).toggle();
//	$("#comment2_"+id).toggle();
//	$("#comment3_"+id).toggle();
//	$("#comment4_"+id).toggle();
//	$("#comment5_"+id).toggle();	
//}
////update comment,comment1_+id 刪除button,comment2_+id 編輯button,comment3_+id 回覆button,comment4_+id 確認button,comment5_+id 取消button
//function updateComment(input){
//	var id = input.split('_')[1];
//
//	commentContent = document.getElementById('comment_'+id).value;
//
//	$.ajax({  
//		url : ajaxURL+'AnyCourse/GroupCommentServlet.do',
//		method : 'POST',
//		cache :false,
//		data : {
//			"state" : "update",
//			"groupId" : groupId,
//			"commentId" : id,
//			"userId" : userId,
//			"nickName" : nickName,
//			"commentContent" : commentContent	
//		},				
//		success : function(data) {
//			$('#comment_'+id).append( data.commentContent);
//			commentId = data.commentId;
//			$("#comment_"+id).attr("disabled","false");
//			$("#comment1_"+id).toggle();
//			$("#comment2_"+id).toggle();
//			$("#comment3_"+id).toggle();
//			$("#comment4_"+id).toggle();
//			$("#comment5_"+id).toggle();
//		},
//		error : function() {
//		}
//	});
//};
////reply編輯框點選取消,reply1_+id 刪除button,reply2_+id 編輯button,reply3_+id 確認button,reply4_+id 取消button
//function can2(input){
//	var id = input.split('_')[1];
//	var id2 = input.split('_')[2];
//	$("#reply_"+id).attr("disabled","false");
//	$("#reply1_"+id+"_"+id2).toggle();
//	$("#reply2_"+id+"_"+id2).toggle();
//	$("#reply3_"+id+"_"+id2).toggle();
//	$("#reply4_"+id+"_"+id2).toggle();
//	
//}
////跳出reply編輯框,reply1_+id 刪除button,reply2_+id 編輯button,reply3_+id 確認button,reply4_+id 取消button
//function edit2(input){
//	var id = input.split('_')[1];
//	var id2 = input.split('_')[2];
//	$("#reply_"+id).removeAttr("disabled");
//	$("#reply1_"+id+"_"+id2).toggle();
//	$("#reply2_"+id+"_"+id2).toggle();
//	$("#reply3_"+id+"_"+id2).toggle();
//	$("#reply4_"+id+"_"+id2).toggle();	
//}
////update reply,reply1_+id 刪除button,reply2_+id 編輯button,reply3_+id 確認button,reply4_+id 取消button
//function updateReply(input){
//	var id = input.split('_')[1];
//	var id2 = input.split('_')[2];
//
//	replyContent = document.getElementById('reply_'+id).value;
//
//	$.ajax({  
//		url : ajaxURL+'AnyCourse/GroupReplyServlet.do',
//		method : 'POST',
//		cache :false,
//		data : {
//			"state" : "update",
//			"replyId" : id,
//			"commentId" : id2,
//			"userId" : userId,
//			"nickName" : nickName,
//			"replyContent" : replyContent	
//		},				
//		success : function(data) {
//			$('#reply_'+id).append( data.replyContent);
//			replyId = data.replyId;
//			$("#reply_"+id).attr("disabled","false");
//			$("#reply1_"+id+"_"+id2).toggle();
//			$("#reply2_"+id+"_"+id2).toggle();
//			$("#reply3_"+id+"_"+id2).toggle();
//			$("#reply4_"+id+"_"+id2).toggle();
//		},
//		error : function() {
//		}
//	});
//};
//
//
//
//
//
//	   
