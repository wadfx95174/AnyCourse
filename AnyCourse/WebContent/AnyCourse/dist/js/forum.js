var state;
var comment_id =null;
var reply_id =null;
var unit_id = 1;
var user_id = 111;
var nick_name = "jerry";
var comment_time;
var comment_content;
var reply_time;
var reply_content;
//var ajax_url="http://140.121.197.130:8400/";
var ajax_url="http://localhost:8080/";

function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
	}

function setComment(){	
	if($("#comment_area").val() !== ''){
		var dt = new Date();
		comment_content = document.getElementById('comment_area').value;
		$("#comment_area").val('');
		$.ajax({
			url : ajax_url+'AnyCourse/CommentServlet.do',
			method : 'POST',
			cache :false,
			data : {
				"state" : "insert",	
				"unit_id" : get("unit_id"),
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
						'<img src="https://ppt.cc/fxYEnx@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
						'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + result.nick_name + '</h4>'+
						'<h5 style="float:right;">' + result.comment_time + '</h5>'+														
						'<textarea class="col-xs-12" rows="2" cols="50" id="comment_' + result.comment_id + '" disabled="disabled" style="float:left;">' + result.comment_content + '</textarea>'+																			
						'<button id="comment4_' + result.comment_id + '" type="button" class="btn btn-default btn_css" style="display:none;" onclick="update_comment(this.id)">確認</button>'+
						'<button id="comment5_' + result.comment_id + '" type="button" class="btn btn-default btn_css" style="display:none;" onclick="can(this.id)">取消</button>'+
						'<button id="comment1_' + result.comment_id + '" type="button" class="btn btn-default btn_css" onclick="delete_comment(this.id)">刪除</button>'+
						'<button id="comment2_' + result.comment_id + '" type="button" class="btn btn-default btn_css" onclick="edit(this.id)">編輯</button>'+
						'<button id="comment3_' + result.comment_id + '" type="button" class="btn btn-default btn_css" onclick="display_reply(this.id)">回覆</button>'+																														
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
//	             alert(jqXHR.responseText);
//	             alert(jqXHR.status);
//	             alert(jqXHR.readyState);
//	             alert(jqXHR.statusText);
	             /*弹出其他两个参数的信息*/
//	             alert(textStatus);
//	             alert(errorThrown);
	         }
		})				
	}	
}

$(document).ready(function() {
	$.ajax({
		url : ajax_url+'AnyCourse/CommentServlet.do',
		method : 'GET',
		cache :false,
		data : {
			"unit_id" : get("unit_id"),
		},
		success:function(result){
//			alert(result);
    		for(var i = 0 ;i < result.length;i++){
    			
    			$('#comment-body').append( 	 
						'<div id="com_' + result[i].comment_id + '" class="B">'+
						'<img src="https://ppt.cc/fxYEnx@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
						'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + result[i].nick_name + '</h4>'+
						'<h5 style="float:right;">' + result[i].comment_time + '</h5>'+														
						'<textarea class="col-xs-12" rows="2" cols="50" id="comment_' + result[i].comment_id + '" disabled="disabled" style="float:left;">' + result[i].comment_content + '</textarea>'+																			
						'<button id="comment4_' + result[i].comment_id + '" type="button" class="btn btn-default btn_css" style="display:none;" onclick="update_comment(this.id)">確認</button>'+
						'<button id="comment5_' + result[i].comment_id + '" type="button" class="btn btn-default btn_css" style="display:none;" onclick="can(this.id)">取消</button>'+
						'<button id="comment1_' + result[i].comment_id + '" type="button" class="btn btn-default btn_css" onclick="delete_comment(this.id)">刪除</button>'+
						'<button id="comment2_' + result[i].comment_id + '" type="button" class="btn btn-default btn_css" onclick="edit(this.id)">編輯</button>'+
						'<button id="comment3_' + result[i].comment_id + '" type="button" class="btn btn-default btn_css" onclick="display_reply(this.id)">回覆</button>'+																																				
						'</div>'+
						'<div id="reply_div_' + result[i].comment_id + '" class="B" style="display:none;">'+
						'<textarea id="reply_area_' + result[i].comment_id +'" class="col-xs-12" rows="3" cols="50" placeholder="Reply something..." style="float:left;"></textarea>'+
						'<button id="setReply_' + result[i].comment_id +'"type="button" class="btn btn-default btn_css" onclick="setReply(this.id)">確認</button>'+
						'<button id="hide_' + result[i].comment_id +'" type="button" class="btn btn-default btn_css" onclick="display_reply(this.id)">取消</button>'+
						'</div>'
	    			);    			
			}	
    		$.ajax({
    			url : ajax_url+'AnyCourse/ReplyServlet.do',
    			method : 'GET',
    			cache :false,
//    			data : {
//    				"unit_id" : get("unit_id"),
//    			},
    			success : function(result) {
//    				alert(result.comment_id);
//    				alert(result.user_id);
//    				alert(result.nick_name);
//    				alert(result.comment_time);
//    				alert(result.comment_content);
//    				console.log(result);
    				for(var i = 0 ;i < result.length;i++){
    				$('#com_'+result[i].comment_id).append( 	
    						'<div id="rep_' + result[i].reply_id + '"class="col-xs-12 C" >'+
    						'<img src="https://ppt.cc/fi5Q0x@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
    						'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + result[i].nick_name +'</h4>'+
    						'<h5 style="float:right;">' + result[i].reply_time +'</h5>'+													
    						'<textarea class="col-xs-12" rows="2" cols="50" id="reply_' + result[i].reply_id + '" disabled="disabled" style="float:left;">' + result[i].reply_content + '</textarea>'+																			
    						'<button id="reply3_' + result[i].reply_id + '_' + result[i].comment_id +'" type="button" class="btn btn-default btn_css" style="display:none;" onclick="update_reply(this.id)">確認</button>'+
    						'<button id="reply4_' + result[i].reply_id + '_' + result[i].comment_id +'" type="button" class="btn btn-default btn_css" style="display:none;" onclick="can2(this.id)">取消</button>'+	
    						'<button id="reply1_' + result[i].reply_id + '_' + result[i].comment_id +'" type="button" class="btn btn-default btn_css" onclick="delete_reply(this.id)">刪除</button>'+
    						'<button id="reply2_' + result[i].reply_id + '_' + result[i].comment_id +'" type="button" class="btn btn-default btn_css" onclick="edit2(this.id)">編輯</button>'+							
    						'</div>'																	
    					);	
//    					alert("OKOK");
    				}
//    				$("#reply_div_" + id ).toggle();
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
		$("#reply_div_" + id ).toggle();	    			
//	});
		
}

function setReply(input){
	var id = input.split('_')[1];
	
	if($("#reply_area_"+id).val() !== ''){
		var dt = new Date();
		reply_content = document.getElementById('reply_area_'+id).value;
		$("#reply_area_"+id).val('');		
		$.ajax({
			url : ajax_url+'AnyCourse/ReplyServlet.do',
			method : 'POST',
			cache :false,
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
						'<img src="https://ppt.cc/fi5Q0x@.png" class="img-circle" style="float:left;height:42px;width:42px;">'+
						'<h4 style="float:left;">&nbsp;&nbsp;&nbsp;' + result.nick_name +'</h4>'+
						'<h5 style="float:right;">' + result.reply_time +'</h5>'+													
						'<textarea class="col-xs-12" rows="2" cols="50" id="reply_' + result.reply_id + '" disabled="disabled" style="float:left;">' + result.reply_content + '</textarea>'+																			
						'<button id="reply3_' + result.reply_id + '_' + result.comment_id +'" type="button" class="btn btn-default btn_css" style="display:none;" onclick="update_reply(this.id)">確認</button>'+
						'<button id="reply4_' + result.reply_id + '_' + result.comment_id +'" type="button" class="btn btn-default btn_css" style="display:none;" onclick="can2(this.id)">取消</button>'+	
						'<button id="reply1_' + result.reply_id + '_' + result.comment_id +'" type="button" class="btn btn-default btn_css" onclick="delete_reply(this.id)">刪除</button>'+
						'<button id="reply2_' + result.reply_id + '_' + result.comment_id +'" type="button" class="btn btn-default btn_css" onclick="edit2(this.id)">編輯</button>'+							
						'</div>'
						
						
//						'<div id="rep_' + result.reply_id + '"class="col-xs-12 C" >'+
//						'<h4 style="float:left;">' + result.nick_name +'</h4>'+
//						'<h5 style="float:right;">' + result.reply_time +'</h5>'+													
//						'<textarea class="col-xs-12" rows="2" cols="50" id="reply_' + result.reply_id + '" disabled="disabled" style="float:left;">' + result.reply_content + '</textarea>'+																			
//						'<button id="delete_' + result.reply_id + '" type="button" class="btn btn-default btn_css" onclick="delete_reply(this.id)">刪除</button>'+
//						'<button id="reply_' + result.reply_id + '" type="button" class="btn btn-default btn_css" onclick="">編輯</button>'+							
//						'</div>'																	
	    			);	
				$("#reply_div_" + id ).toggle();
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

function delete_reply(input){
	var id = input.split('_')[1];
	
	$.ajax({
		url : ajax_url+'AnyCourse/ReplyServlet.do',
		method : 'POST',
		cache :false,
		data : {
			"state" : "delete",
			"reply_id" : id,					
		},				
		success : function(data) {
			$("#rep_"+id).remove();
		},
	});
}

function delete_comment(input){
	var id = input.split('_')[1];
	
	$.ajax({
		url : ajax_url+'AnyCourse/CommentServlet.do',
		method : 'POST',
		cache :false,
		data : {
			"state" : "delete",
			"comment_id" : id,					
		},				
		success : function(data) {
			$("#com_"+id).remove();
			$.ajax({
				url : ajax_url+'AnyCourse/ReplyServlet.do',
				method : 'POST',
				data : {
					"state" : "delete2",
					"comment_id" : id,					
				},				
				success : function(data) {
//					alert("OK");
				},
			});
		},
	});	
}

function can(input){
	var id = input.split('_')[1];
	$("#comment_"+id).attr("disabled","false");
	$("#comment1_"+id).toggle();
	$("#comment2_"+id).toggle();
	$("#comment3_"+id).toggle();
	$("#comment4_"+id).toggle();
	$("#comment5_"+id).toggle();
	
}

function edit(input){
	var id = input.split('_')[1];
	$("#comment_"+id).removeAttr("disabled");
	$("#comment1_"+id).toggle();
	$("#comment2_"+id).toggle();
	$("#comment3_"+id).toggle();
	$("#comment4_"+id).toggle();
	$("#comment5_"+id).toggle();	
}

function update_comment(input){
	var id = input.split('_')[1];

	comment_content = document.getElementById('comment_'+id).value;

	$.ajax({  
		url : ajax_url+'AnyCourse/CommentServlet.do',
		method : 'POST',
		cache :false,
		data : {
			"state" : "update",
			"unit_id" : get("unit_id"),
			"comment_id" : id,
			"user_id" : user_id,
			"nick_name" : nick_name,
//			"comment_time" : dt.toLocaleString(),
			"comment_content" : comment_content	
		},				
		success : function(data) {
			$('#comment_'+id).append( data.comment_content);
			comment_id = data.comment_id;
			$("#comment_"+id).attr("disabled","false");
			$("#comment1_"+id).toggle();
			$("#comment2_"+id).toggle();
			$("#comment3_"+id).toggle();
			$("#comment4_"+id).toggle();
			$("#comment5_"+id).toggle();
		},
		error : function() {
//			alert("error");
		}
	});
};

function can2(input){
	var id = input.split('_')[1];
	var id2 = input.split('_')[2];
	$("#reply_"+id).attr("disabled","false");
	$("#reply1_"+id+"_"+id2).toggle();
	$("#reply2_"+id+"_"+id2).toggle();
	$("#reply3_"+id+"_"+id2).toggle();
	$("#reply4_"+id+"_"+id2).toggle();
	
}

function edit2(input){
	var id = input.split('_')[1];
	var id2 = input.split('_')[2];
	$("#reply_"+id).removeAttr("disabled");
	$("#reply1_"+id+"_"+id2).toggle();
	$("#reply2_"+id+"_"+id2).toggle();
	$("#reply3_"+id+"_"+id2).toggle();
	$("#reply4_"+id+"_"+id2).toggle();	
}

function update_reply(input){
	var id = input.split('_')[1];
	var id2 = input.split('_')[2];

	reply_content = document.getElementById('reply_'+id).value;

	$.ajax({  
		url : ajax_url+'AnyCourse/ReplyServlet.do',
		method : 'POST',
		cache :false,
		data : {
			"state" : "update",
			"reply_id" : id,
			"comment_id" : id2,
			"user_id" : user_id,
			"nick_name" : nick_name,
//			"comment_time" : dt.toLocaleString(),
			"reply_content" : reply_content	
		},				
		success : function(data) {
			$('#reply_'+id).append( data.reply_content);
			reply_id = data.reply_id;
			$("#reply_"+id).attr("disabled","false");
			$("#reply1_"+id+"_"+id2).toggle();
			$("#reply2_"+id+"_"+id2).toggle();
			$("#reply3_"+id+"_"+id2).toggle();
			$("#reply4_"+id+"_"+id2).toggle();
		},
		error : function() {
//			alert("error");
		}
	});
};





	   
