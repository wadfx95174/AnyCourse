		var filechooser = document.getElementById('filechooser');
		var previewer = document.getElementById('previewer');
		//資料庫欄位  
		var state;
		var text_note_id =null;
		var picture_note_id =null;
		var unit_id = 1;
		var user_id = 111;
		var text_note = "123456";
		var picture_note_url;
		var share = 0;
		var share_time;
		var likes = 0;
		// 200 KB 对应的字节数
		var maxsize = 200 * 1024;

		
			

		filechooser.onchange = function() {
			var files = this.files;
			var file = files[0];

			// 接受 jpeg, jpg, png 类型的图片
			if (!/\/(?:jpeg|jpg|png)/i.test(file.type))
				return;

			var reader = new FileReader();

			reader.onload = function() {
				var result = this.result;
				var img = new Image();

				// 如果图片小于 200kb，不压缩
				if (result.length <= maxsize) {
					toPreviewer(result);
					return;
				}

				img.onload = function() {
					var compressedDataUrl = compress(img, file.type);
					toPreviewer(compressedDataUrl);

					img = null;
				};

				img.src = result;
			};

			reader.readAsDataURL(file);
		};

		function toPreviewer(dataUrl) {
			var dataUrl = dataUrl;
//			previewer.src = dataUrl;
//			previewer1.src = dataUrl;
			//	console.log(dataUrl);
			filechooser.value = '';

				
				$.ajax({
					url : 'http://localhost:8080/AnyCourse/PictureNoteServlet.do',
					method : 'POST',
					data : {
						"state" : "insert",
						"unit_id" : unit_id,
						"user_id" : user_id,
						"picture_note_url" : dataUrl,
					//	"text_note" : text_note,
						"share" : share,
						"share_time" : share_time,
						"likes" : likes
					},
					success : function(result) {
//						alert(result.picture_note_id);
						$('#container').append( 	 
								'<img id="picture_note_' +  result.picture_note_id + '" class="fs-gal" src="' + result.picture_note_url +'" alt="picture_note_' + result.picture_note_id + '" data-url="' + result.picture_note_url + '" />' 
//								+ '<button id = "delete_' + result.picture_note_id +'"  type="button" onclick="getID(this.id)" onclick ="deletePicture_note()"  style="float:right">Delete</button>'
								
//			    				'<button id="myBtn_' + result.picture_note_id + '" onclick="getID(this.id)">'+
//									'<img id="previewer11" src="'+ result.picture_note_url + '" class = "min">'+
//								"</button>"	+"<br>" + 							
//								'<div id="myModal_' + result.picture_note_id + '" class="modal">' +
//								'<div class="modal-content" >' +
//								'<button id = "close_' + result.picture_note_id +'"  type="button" class="close" style="float:right">Close</button>'+
//								'<div style="height:600px;overflow:auto;">' +
//								'<img id="previewer1" src="'+ result.picture_note_url + '"class="max"> ' +
//								'</div>' +						
//								'<button id = "delete_' + result.picture_note_id +'"  type="button"  onclick ="deletePicture_note()" class="close" style="float:right">Delete</button>'+							
//							    '</div>' +		    				
//			    				'</div>'
			    			);	
						
					},
					error: function (jqXHR, textStatus, errorThrown) {
		                 /*弹出jqXHR对象的信息*/
//		                 alert(jqXHR.responseText);
//		                 alert(jqXHR.status);
//		                 alert(jqXHR.readyState);
//		                 alert(jqXHR.statusText);
		                 /*弹出其他两个参数的信息*/
//		                 alert(textStatus);
//		                 alert(errorThrown);
		             }
				})				
				
		}
		
		
		
		function compress(img, fileType) {
			var canvas = document.createElement("canvas");
			var ctx = canvas.getContext('2d');

			var width = img.width;
			var height = img.height;

			canvas.width = width;
			canvas.height = height;

			ctx.fillStyle = "#fff";
			ctx.fillRect(0, 0, canvas.width, canvas.height);

			ctx.drawImage(img, 0, 0, width, height);

			// 压缩
			var base64data = canvas.toDataURL(fileType, 0.3);

			var initSize = img.src.length;
			console.log('压缩前：', initSize);
			console.log('压缩后：', base64data.length);
			console.log('压缩率：',
					100 * (initSize - base64data.length) / initSize, "%");

			canvas = ctx = null;
			//console.log(base64data);
			return base64data;
		} 
			

		 
		$(document).ready(function() {
			$.ajax({
				url : 'http://localhost:8080/AnyCourse/TextNoteServlet.do',
				method : 'GET',
				success:function(result){
//					alert(result);
		    		for(var i = 0 ;i < result.length;i++){
//		    			alert(result[i].text_note);
		    			
		    			$('#text_area').append( result[i].text_note + "<br>");
		    			text_note_id = result[i].text_note_id;
					}
//		    		alert(text_note_id);
		    	},
				error:function(){}
			});
		});
		
//		
//		var id = null;
//		
//		function getID(input){
//			id = null;
//			var modal = null;
//			var btn =  null;
//			var span =  null;
//			id = input.split('_')[1]; 
//						
////			alert(id);
//			
//			modal = document.getElementById('myModal_'+id);
//
//    		// Get the button that opens the modal
//    		btn = document.getElementById("myBtn_"+id);
//    		// Get the <span> element that closes the modal
//    		
//    		span = document.getElementById("close_"+id);
//    		span2 = document.getElementById("delete_"+id);
//
//    		// When the user clicks the button, open the modal 
//    		
//    			modal.style.display = "block";
//    		
//    		span2.onclick = function() {
//    			deletePicture_note();
//        		modal.style.display = "none";
//        	}
//
//    		// When the user clicks on <span> (x), close the modal
//    		span.onclick = function() {
//    			modal.style.display = "none";
//    		}
//    		
//    		// When the user clicks anywhere outside of the modal, close it
//    		window.onclick = function(event) {
//    			if (event.target == modal) {
//    				modal.style.display = "none";
//    			}
//    		}	
//		}
		
		$(document).ready(function() {
			printPicture();
		});
		
		function printPicture(){
			$.ajax({
				url : 'http://localhost:8080/AnyCourse/PictureNoteServlet.do',
				method : 'GET',
				success:function(result){
		    		for(var i = 0 ;i < result.length;i++){
//		    			alert(result[i].picture_note_url);
//		    			alert(result[i].picture_note_id);
		    			$('#container').append( 
		    					'<img id="picture_note_' +  result[i].picture_note_id + '" class="fs-gal" src="' + result[i].picture_note_url +'" alt="picture_note_' + result[i].picture_note_id + '" data-url="' + result[i].picture_note_url + '" />'	
		    					
//		    				'<button id="myBtn_' + result[i].picture_note_id + '" onclick="getID(this.id)">'+
//								'<img id="previewer11" src="'+ result[i].picture_note_url + '" class = "min">'+
//							"</button>"	+"<br>" + 							
//							'<div id="myModal_' + result[i].picture_note_id + '" class="modal">' +
//							'<div class="modal-content" >' +
//							'<button id = "close_' + result[i].picture_note_id +'"  type="button" class="close" style="float:right">Close</button>'+
//							'<div style="height:600px;overflow:auto;">' +
//							'<img id="previewer1" src="'+ result[i].picture_note_url + '"class="max"> ' +
//							'</div>' +						
//							'<button id = "delete_' + result[i].picture_note_id +'"  type="button"  onclick ="deletePicture_note()" class="close" style="float:right">Delete</button>'+							
//						    '</div>' +		    				
//		    				'</div>'
		    			);	    			
					}	    		
		    	},
				error:function(){}
			});
		}
		
		function setText(){	
			        
			if(text_note_id == null)
				{
//					alert("OK");
					setText_note();
					$("#text_area").attr("disabled","false");
		    		$("#noteFooter").slideToggle();
				}
			else{
					updateText_note();
					$("#text_area").attr("disabled","false");
		    		$("#noteFooter").slideToggle();
				}	
		};
		
		function setText_note(){
			//document.getElementById("text_area").innerHTML += document.getElementById('text_note').value + "<br>";
			text_note = document.getElementById('text_area').value;
			$.ajax({
				url : 'http://localhost:8080/AnyCourse/TextNoteServlet.do',
				method : 'POST',
				data : {
					"state" : "insert",
					"unit_id" : unit_id,
					"user_id" : user_id,
					//"picture_note_url" : dataUrl,
					"text_note" : text_note,
					"share" : share,
					"share_time" : share_time,
					"likes" : likes
				},
				success : function(text_note) {
					console.log(text_note);
				},
			});
			$.ajax({
				url : 'http://localhost:8080/AnyCourse/TextNoteServlet.do',
				method : 'GET',
				success:function(result){
//					alert(result);
		    		for(var i = 0 ;i < result.length;i++){
//		    			alert(result[i].text_note);
		    			
		    			$('#text_area').append( result[i].text_note + "<br>");
		    			text_note_id = result[i].text_note_id;
					}
//		    		alert(text_note_id);
		    	},
				error:function(){}
			});
		};
		
		function updateText_note(){
			//document.getElementById("text_area").innerHTML += document.getElementById('text_note').value + "<br>";
			text_note = document.getElementById('text_area').value;
//			alert(text_note);
//			alert(text_note_id);
			$.ajax({  
				url : 'http://localhost:8080/AnyCourse/TextNoteServlet.do',
				method : 'POST',
				data : {
					"state" : "update",
					"text_note_id" : text_note_id,
					"unit_id" : unit_id,
					"user_id" : user_id,
					"text_note" : text_note,
					"share" : share,
					"share_time" : share_time,
					"likes" : likes
				},				
				success : function(data) {
//					alert(data);
					$('#text_area').append( data.text_note );
	    			text_note_id = data.text_note_id;
				},
				error : function() {
//					alert("error");
				}
			});
		};
		
		function deletePicture_note(){
//			document.getElementById('picture').value;
			var zzz= $("#picture").text();
//			alert(zzz);
			var id = zzz.split('_')[2];
//			alert(id);
			$.ajax({
				url : 'http://localhost:8080/AnyCourse/PictureNoteServlet.do',
				method : 'POST',
				data : {
					"state" : "delete",
					"picture_note_id" : id,					
				},				
				success : function(data) {
//					alert("OKOK");
					$("#picture_note_"+id).remove();
//					$("#modal_"+id).remove();
				},
			});
		}
		
