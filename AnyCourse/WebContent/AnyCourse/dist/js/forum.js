var state;
var comment_id =null;
var reply_id =null;
var user_id = 111;
var nick_name = "jerry";
var comment_time;
var comment_content;
var reply_time;
var reply_content;


function setComment(){	
	if($("#comment_area").val() !== ''){
		var dt = new Date();
		comment_content = document.getElementById('comment_area').value;
		$("#comment_area").val('');
		$.ajax({
			url : 'http://localhost:8080/AnyCourse/CommentServlet.do',
			method : 'POST',
			data : {
				"state" : "insert",			
				"user_id" : user_id,
				"nick_name" : nick_name,
//				"comment_time" : dt.toLocaleString(),
				"comment_content" : comment_content,			
			},
			success : function(result) {
//				alert(result.comment_id);
//				alert(result.user_id);
//				alert(result.nick_name);
//				alert(result.comment_time);
//				alert(result.comment_content);
				$('#comment-body').append(  	 
						'<div id="com_' + result.comment_id + '" class="B" >'+
						'<h4 style="float:left;">' + result.nick_name + '</h4>'+
						'<h5 style="float:right;">' + result.comment_time + '</h5>'+														
						'<textarea class="col-xs-12" rows="2" cols="50" id="comment_' + result.comment_id + '" disabled="disabled" style="float:left;">' + result.comment_content + '</textarea>'+																			
						'<button id="comment_' + result.comment_id + '" type="button" class="btn btn-default btn_css" onclick="deleteComment()">刪除</button>'+
						'<button id="comment_' + result.comment_id + '" type="button" class="btn btn-default btn_css" onclick="updateComment()">編輯</button>'+
						'<button id="comment_' + result.comment_id + '" type="button" class="btn btn-default btn_css" onclick="display_reply(this.id)">回覆</button>'+																														
						'</div>'+
						'<div id="reply_div_' + result.comment_id + '" class="B" style="display:none;">'+
						'<textarea id="reply_area_' + result.comment_id +'" class="col-xs-12" rows="3" cols="50" placeholder="Reply something..." style="float:left;"></textarea>'+
						'<button id="setReply_' + result.comment_id +'"type="button" class="btn btn-default btn_css" onclick="setReply(this.id)">確認</button>'+
						'<button id="hide_' + result.comment_id +'" type="button" class="btn btn-default btn_css" onclick="display_reply(this.id)">取消</button>'+
						'</div>'
	    			);	
//				alert("OKOKOKOKOK");
			},
			error: function (jqXHR, textStatus, errorThrown) {
	             /*弹出jqXHR对象的信息*/
	             alert(jqXHR.responseText);
	             alert(jqXHR.status);
	             alert(jqXHR.readyState);
	             alert(jqXHR.statusText);
	             /*弹出其他两个参数的信息*/
	             alert(textStatus);
	             alert(errorThrown);
	         }
		})				
	}	
}

$(document).ready(function() {
	$.ajax({
		url : 'http://localhost:8080/AnyCourse/CommentServlet.do',
		method : 'GET',
		success:function(result){
//			alert(result);
    		for(var i = 0 ;i < result.length;i++){
    			
    			$('#comment-body').append( 	 
						'<div id="com_' + result[i].comment_id + '" class="B">'+
						'<h4 style="float:left;">' + result[i].nick_name + '</h4>'+
						'<h5 style="float:right;">' + result[i].comment_time + '</h5>'+														
						'<textarea class="col-xs-12" rows="2" cols="50" id="comment_' + result[i].comment_id + '" disabled="disabled" style="float:left;">' + result[i].comment_content + '</textarea>'+																			
						'<button id="comment_' + result[i].comment_id + '" type="button" class="btn btn-default btn_css" onclick="deleteComment()">刪除</button>'+
						'<button id="comment_' + result[i].comment_id + '" type="button" class="btn btn-default btn_css" onclick="updateComment()">編輯</button>'+
						'<button id="comment3_' + result[i].comment_id +'" type="button" class="btn btn-default btn_css" onclick="display_reply(this.id)">回覆</button>'+																														
						'</div>'+
						'<div id="reply_div_' + result[i].comment_id + '" class="B" style="display:none;">'+
						'<textarea id="reply_area_' + result[i].comment_id +'" class="col-xs-12" rows="3" cols="50" placeholder="Reply something..." style="float:left;"></textarea>'+
						'<button id="setReply_' + result[i].comment_id +'"type="button" class="btn btn-default btn_css" onclick="setReply(this.id)">確認</button>'+
						'<button id="hide_' + result[i].comment_id +'" type="button" class="btn btn-default btn_css" onclick="display_reply(this.id)">取消</button>'+
						'</div>'
	    			);   			
			}		    		
    	},
		error:function(){}
	});
});

function display_reply(input){
//	alert(input);
	var id = input.split('_')[1]; 
//	alert(id);
//	$("#comment3_" + id).click(function(){   
//		alert("1111");
		$("#reply_div_" + id ).slideToggle();	    			
//	});
		
}

function setReply(input){
	var id = input.split('_')[1];
	
	if($("#reply_area_"+id).val() !== ''){
		var dt = new Date();
		reply_content = document.getElementById('reply_area_'+id).value;
		$("#reply_area_"+id).val('');		
		$.ajax({
			url : 'http://localhost:8080/AnyCourse/ReplyServlet.do',
			method : 'POST',
			data : {
				"state" : "insert",	
				"comment_id" : id,
				"user_id" : user_id,
				"nick_name" : nick_name,
//				"reply_time" : dt.toLocaleString(),
				"reply_content" : reply_content,			
			},
			success : function(result) {
//				alert(result.comment_id);
//				alert(result.user_id);
//				alert(result.nick_name);
//				alert(result.comment_time);
//				alert(result.comment_content);
				$('#com_'+id).append( 	
						'<div id="rep_' + result.reply_id + '"class="col-xs-12 C" >'+
						'<h4 style="float:left;">' + result.nick_name +'</h4>'+
						'<h5 style="float:right;">' + result.reply_time +'</h5>'+													
						'<textarea class="col-xs-12" rows="2" cols="50" id="reply_' + result.reply_id + '" disabled="disabled" style="float:left;">' + result.reply_content + '</textarea>'+																			
						'<button id="reply_' + result.reply_id + '" type="button" class="btn btn-default btn_css" onclick="">刪除</button>'+
						'<button id="reply_' + result.reply_id + '" type="button" class="btn btn-default btn_css" onclick="">編輯</button>'+							
						'</div>'																	
	    			);	
				$("#reply_div_" + id ).slideToggle();
			},
			error: function (jqXHR, textStatus, errorThrown) {
	             /*弹出jqXHR对象的信息*/
	             alert(jqXHR.responseText);
	             alert(jqXHR.status);
	             alert(jqXHR.readyState);
	             alert(jqXHR.statusText);
	             /*弹出其他两个参数的信息*/
	             alert(textStatus);
	             alert(errorThrown);
	         }
		})				
	}			
}

$.ajax({
	url : 'http://localhost:8080/AnyCourse/ReplyServlet.do',
	method : 'GET',
	success : function(result) {
//		alert(result.comment_id);
//		alert(result.user_id);
//		alert(result.nick_name);
//		alert(result.comment_time);
//		alert(result.comment_content);
		console.log(result);
		for(var i = 0 ;i < result.length;i++){
		$('#com_'+result[i].comment_id).append( 	
				'<div id="rep_' + result[i].reply_id + '"class="col-xs-12 C" >'+
				'<h4 style="float:left;">' + result[i].nick_name +'</h4>'+
				'<h5 style="float:right;">' + result[i].reply_time +'</h5>'+													
				'<textarea class="col-xs-12" rows="2" cols="50" id="reply_' + result[i].reply_id + '" disabled="disabled" style="float:left;">' + result[i].reply_content + '</textarea>'+																			
				'<button id="reply_' + result[i].reply_id + '" type="button" class="btn btn-default btn_css" onclick="">刪除</button>'+
				'<button id="reply_' + result[i].reply_id + '" type="button" class="btn btn-default btn_css" onclick="">編輯</button>'+							
				'</div>'																	
			);	
//			alert("OKOK");
		}
//		$("#reply_div_" + id ).slideToggle();
	},
	error: function (jqXHR, textStatus, errorThrown) {
         /*弹出jqXHR对象的信息*/
         alert(jqXHR.responseText);
         alert(jqXHR.status);
         alert(jqXHR.readyState);
         alert(jqXHR.statusText);
         /*弹出其他两个参数的信息*/
         alert(textStatus);
         alert(errorThrown);
     }
})



	   
