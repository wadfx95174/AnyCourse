var state;
var commentId =null;
var replyId =null;
var unitId;
var userId;
var nickName;
var commentTime;
var commentContent;
var replyTime;
var replyContent;
var urlId;

function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
}
//新增comment
function setComment(){	
	if($("#commentArea").val() !== ''){
		var dt = new Date();
		commentContent = document.getElementById('commentArea').value;
		$("#commentArea").val('');
		$.ajax({
			url : ajaxURL+'AnyCourse/CommentServlet.do',
			method : 'POST',
			cache :false,
			data : {
				"state" : "insert",	
				"unitId" : get("unitId"),
				"userId" : userId,
				"nickName" : nickName,
				"commentContent" : commentContent,			
			},
			success : function(result) {
				$('#comment-body').append(  	 
						'<div id="com_' + result.commentId + '" class="B" >'+	
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
						'<div id="reply_div_' + result.commentId + '" class="B" style="display:none;">'+
						'<textarea id="reply_area_' + result.commentId +'" class="col-xs-12" rows="3" cols="50" placeholder="Reply something..." style="float:left;"></textarea>'+
						'<button id="setReply_' + result.commentId +'"type="button" class="btn btn-default btnCss" onclick="setReply(this.id)">確認</button>'+
						'<button id="hide_' + result.commentId +'" type="button" class="btn btn-default btnCss" onclick="displayReply(this.id)">取消</button>'+
						'</div>'
	    			);
			},
			error: function (jqXHR, textStatus, errorThrown) {
	         }
		})				
	}	
}
//載入頁面
$(document).ready(function() {
	$.ajax({
		url : ajaxURL+'AnyCourse/CommentServlet.do',
		method : 'GET',
		cache :false,
		data : {
			"unitId" : get("unitId")
		},
		success:function(result){
    		for(var i = 0 ;i < result.length;i++){
    			
    			$('#comment-body').append( 	 
						'<div id="com_' + result[i].commentId + '" class="B">'+
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
						'<div id="reply_div_' + result[i].commentId + '" class="B" style="display:none;">'+
						'<textarea id="reply_area_' + result[i].commentId +'" class="col-xs-12" rows="3" cols="50" placeholder="Reply something..." style="float:left;"></textarea>'+
						'<button id="setReply_' + result[i].commentId +'"type="button" class="btn btn-default btnCss" onclick="setReply(this.id)">確認</button>'+
						'<button id="hide_' + result[i].commentId +'" type="button" class="btn btn-default btnCss" onclick="displayReply(this.id)">取消</button>'+
						'</div>'
	    			);    			
			}
    		$.ajax({
    			url : ajaxURL+'AnyCourse/ReplyServlet.do',
    			method : 'GET',
    			cache :false,
    			data:{
    				unitId: get("unitId")
    			},
    			success : function(response) {

    				for(var i = 0 ;i < response.length;i++){
    				$('#com_'+response[i].commentId).append( 	
    						'<div id="rep_' + response[i].replyId + '"class="col-xs-12 C" >'+
    						'<img src="https://ppt.cc/fi5Q0x@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
    						'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + response[i].nickName +'</h4>'+
    						'<h5 style="float:right;">' + response[i].replyTime +'</h5>'+													
    						'<textarea class="col-xs-12" rows="2" cols="50" id="reply_' + response[i].replyId + '" disabled="disabled" style="float:left;">' + response[i].replyContent + '</textarea>'+																			
    						'<button id="reply3_' + response[i].replyId + '_' + response[i].commentId +'" type="button" class="btn btn-default btnCss" style="display:none;" onclick="updateReply(this.id)">確認</button>'+
    						'<button id="reply4_' + response[i].replyId + '_' + response[i].commentId +'" type="button" class="btn btn-default btnCss" style="display:none;" onclick="can2(this.id)">取消</button>'+	
    						'<button id="reply1_' + response[i].replyId + '_' + response[i].commentId +'" type="button" class="btn btn-default btnCss" onclick="deleteReply(this.id)">刪除</button>'+
    						'<button id="reply2_' + response[i].replyId + '_' + response[i].commentId +'" type="button" class="btn btn-default btnCss" onclick="edit2(this.id)">編輯</button>'+							
    						'</div>'																	
    					);
    				}
    			},
    			error: function (jqXHR, textStatus, errorThrown) {
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
	// console.log("input:"+input);
	var id = input.split('_')[1];
	var url = location.href;
	
	if($("#reply_area_"+id).val() !== ''){
		var dt = new Date();
		replyContent = document.getElementById('reply_area_'+id).value;
		$("#reply_area_"+id).val('');		
		$.ajax({
			url : ajaxURL+'AnyCourse/ReplyServlet.do',
			method : 'POST',
			cache :false,
			data : {
				state : "insert",	
				commentId : id,
				nickName : nickName,
				replyContent : replyContent			
			},
			success : function(result) {
				// console.log(result);
				urlId = url.split("#")[0]+"#rep_"+result.replyId;
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

				///////////////////通知提問者有人回復他/////////////////////////////
				//檢查提問者與回覆者是否為不同人，不同人才通知
				if(result.commentUserId != result.userId){
					$.ajax({
						url : ajaxURL+'AnyCourse/NotificationServlet.do',
						method : 'POST',
						cache :false,
						data : {
							'action' : "playerInterfaceComment",
							'commentId' : id,
							'url' : urlId,
							'type': "playerInterfaceComment"
						},
						success : function(response) {
							console.log(response);
							ws.send(JSON.stringify({
			                    type: response.type,
			                    toUserId: response.toUserId,
			                    notificationId: response.notificationId,
			                    nickname: response.nickname,
			                    url: response.url
			                }));
						},
						error: function (jqXHR, textStatus, errorThrown) {
							console.log("forum.js playerInterfaceComment error");
				        }
					});
				}
				///////////////////////////////////////////////////////////////////

				/////////////////////////通知其他回覆者/////////////////////////////
				$.ajax({
					url : ajaxURL+'AnyCourse/NotificationServlet.do',
					method : 'POST',
					cache :false,
					data : {
						'action' : "playerInterfaceReply",
						'commentId' : id,
						'url' : urlId,
						'type': "playerInterfaceReply"
					},
					success : function(response) {
						console.log(response);
						if(response){
							for(var i = 0;i < response.length; i++){
								ws.send(JSON.stringify({
				                    type: response[i].type,
				                    toUserId: response[i].toUserId,
				                    notificationId: response[i].notificationId,
				                    nickname: response[i].nickname,
				                    url: response[i].url,
				                    commentNickname: response[i].commentNickname
				                }));
		                	}
						}
						
					},
					error: function (jqXHR, textStatus, errorThrown) {
						console.log("forum.js playerInterfaceReply error");
			        }
				});
				///////////////////////////////////////////////////////////////////
			},
			error: function (jqXHR, textStatus, errorThrown) {
				console.log("forum.js insert reply error");
	        }
		});			
	}			
}
//刪除reply
function deleteReply(input){
	var id = input.split('_')[1];
	
	$.ajax({
		url : ajaxURL+'AnyCourse/ReplyServlet.do',
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
		url : ajaxURL+'AnyCourse/CommentServlet.do',
		method : 'POST',
		cache :false,
		data : {
			"state" : "delete",
			"commentId" : id,					
		},				
		success : function(data) {
			$("#com_"+id).remove();
			$.ajax({
				url : ajaxURL+'AnyCourse/ReplyServlet.do',
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
		url : ajaxURL+'AnyCourse/CommentServlet.do',
		method : 'POST',
		cache :false,
		data : {
			"state" : "update",
			"unitId" : get("unitId"),
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
		url : ajaxURL+'AnyCourse/ReplyServlet.do',
		method : 'POST',
		cache :false,
		data : {
			"state" : "update",
			"replyId" : id,
			"commentId" : id2,
			"userId" : userId,
			"nickName" : nickName,
			"replyContent" : replyContent	
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