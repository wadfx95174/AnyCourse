		var filechooser = document.getElementById('filechooser');
		var previewer = document.getElementById('previewer');
		//資料庫欄位  
		var state;
		var textNoteId =null;
		var pictureNoteId =null;
		var unitId;
		var userId;
		var textNote;
		var pictureNoteUrl;
		var share = 0;
		var shareTime;
		var likes = 0;
		// 200 KB 对应的字节数
		var maxsize = 200 * 1024;
		
		function get(name)
		{
		   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
		      return decodeURIComponent(name[1]);
			}
		
		
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
			if($('#categoryList').val() == "Null"){
				$("#errorModal").modal('show');
			}
			else{
			var dataUrl = dataUrl;
			filechooser.value = '';
				$.ajax({
					url : ajaxURL+'AnyCourse/PictureNoteServlet.do',
					method : 'POST',
					cache :false,
					data : {
						"state" : "insert",
						"unitId" : get("unitId"),
						"userId" : userId,
						"pictureNoteUrl" : dataUrl,
						"share" : share,
						"shareTime" : shareTime,
						"likes" : likes,
						"categoryId" : $('#categoryList').val()						
					},
					success : function(result) {
						$('#container').append( 	 
								'<img id="pictureNote_' +  result.pictureNoteId + '" class="fs-gal" src="' + result.pictureNoteUrl +'" alt="pictureNote_' + result.pictureNoteId + '" data-url="' + result.pictureNoteUrl + '" />' 
			    			);	
						
					},
					error: function (jqXHR, textStatus, errorThrown) {
		             }
				})				
			}	
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

			// 壓縮
			var base64data = canvas.toDataURL(fileType, 0.3);

			var initSize = img.src.length;
			console.log('压缩前：', initSize);
			console.log('压缩后：', base64data.length);
			console.log('压缩率：',
					100 * (initSize - base64data.length) / initSize, "%");

			canvas = ctx = null;
			return base64data;
		} 
		
		$(document).ready(function() {
			$.ajax({
				url : ajaxURL+'AnyCourse/TextNoteServlet.do',
				method : 'GET',
				cache :false,
				data : {					
					"unitId" : get("unitId")
				},
				success:function(result){
		    		for(var i = 0 ;i < result.length;i++){
		    			if(result[i].share == 1){
		    				$('#shareNote').text("已分享");
							$('#shareNote').removeClass('btn-primary');
				        	$('#shareNote').addClass('btn-danger');
						}	    			
		    			$('#textArea').append( result[i].textNote + "<br>");
		    			textNoteId = result[i].textNoteId;
		    			userId = result[i].userId;
					}
		    	},
				error:function(){}
			});
		});
		
		$(document).ready(function() {
			printPicture();
		});
		
		function printPicture(){
			$.ajax({
				url : ajaxURL+'AnyCourse/PictureNoteServlet.do',
				method : 'GET',
				cache :false,
				data : {					
					"unitId" : get("unitId")
				},
				success:function(result){
		    		for(var i = 0 ;i < result.length;i++){
		    			$('#container').append( 
		    					'<img id="pictureNote_' +  result[i].pictureNoteId + '" class="fs-gal" src="' + result[i].pictureNoteUrl +'" alt="pictureNote_' + result[i].pictureNoteId + '" data-url="' + result[i].pictureNoteUrl + '" />'	
		    			);	
		    			userId = result[i].userId;
					}	    		
		    	},
				error:function(){}
			});
		}
		
		$('#chooseButton').click(function(){
//			alert($('#categoryList').val());
			if($('#categoryList').val() != "Null"){
				$('#chooseNoteCategory').text("已選擇");
				$('#chooseNoteCategory').removeClass('btn-primary');
	        	$('#chooseNoteCategory').addClass('btn-danger');
	        	shareNote();
	        	
			}
			else{$('#chooseNoteCategory').text("選擇分類");
				$('#chooseNoteCategory').removeClass('btn-danger');
	        	$('#chooseNoteCategory').addClass('btn-primary');
	        	notShareNote();
			}
		});
		
		$('#addListButton').click(function(){
			$.ajax({
				url : ajaxURL+'AnyCourse/NoteCategoryServlet.do',
				method : 'POST',
				cache :false,
				data : {
					"state" : "insert",
					"userId" : userId,
					"categoryName" : $("#named").val()
				},
				success : function(categoryName) {
					console.log(categoryName);
				},
			});
			
		});
		
		$(document).ready(function() {
			$.ajax({
				url : ajaxURL+'AnyCourse/NoteCategoryServlet.do',
				method : 'GET',
				cache :false,
				data : {					
					"userId" : userId
				},
				success:function(result){
					for(var i = 0 ;i < result.length;i++){
		    			
		    			$('#categoryList').append( 		    					
					         '<option value="'+ result[i].categoryId +'">'+ result[i].categoryName +'</option>'

		    			);
					}
		    	},
				error:function(){alert("aaa")}
			});
		});
		
		function setText(){	
			        
			if(textNoteId == null)
				{
					if($('#categoryList').val() == "Null"){
						$("#errorModal").modal('show');
					}
					else{
						setTextNote();
						$("#textArea").attr("disabled","false");
			    		$("#noteFooter").slideToggle();
					}					
				}
			else{

					if($('#categoryList').val() == "Null"){
						$("#errorModal").modal('show');
					}
					else{
						updateTextNote();
						$("#textArea").attr("disabled","false");
			    		$("#noteFooter").slideToggle();
					}
				}	
		};
		
		function setTextNote(){
			textNote = document.getElementById('textArea').value;
			$.ajax({
				url : ajaxURL+'AnyCourse/TextNoteServlet.do',
				method : 'POST',
				cache :false,
				data : {
					"state" : "insert",
					"unitId" : get("unitId"),
					"userId" : userId,
					"textNote" : textNote,
					"share" : share,
					"shareTime" : shareTime,
					"likes" : likes,
					"categoryId" : $('#categoryList').val()	
				},
				success : function(textNote) {
					console.log(textNote);
				},
			});
			$.ajax({
				url : ajaxURL+'AnyCourse/TextNoteServlet.do',
				method : 'GET',
				cache :false,
				data : {					
					"unitId" : get("unitId")
				},
				success:function(result){
		    		for(var i = 0 ;i < result.length;i++){
		    			
		    			$('#textArea').append( result[i].textNote + "<br>");
		    			textNoteId = result[i].textNoteId;
					}
		    	},
				error:function(){}
			});
		};
		
		function updateTextNote(){
			textNote = document.getElementById('textArea').value;
			$.ajax({  
				url : ajaxURL+'AnyCourse/TextNoteServlet.do',
				method : 'POST',
				cache :false,
				data : {
					"state" : "update",
					"textNoteId" : textNoteId,
					"unitId" : get("unitId"),
					"userId" : userId,
					"textNote" : textNote,
					"share" : share,
					"shareTime" : shareTime,
					"likes" : likes,
					"categoryId" : $('#categoryList').val()	
				},				
				success : function(data) {
					$('#textArea').append( data.textNote );
	    			textNoteId = data.textNoteId;
				},
				error : function() {
				}
			});
		};
		
		function deletePictureNote(){
			var zzz= $("#picture").text();
			var id = zzz.split('_')[2];
			$.ajax({
				url : ajaxURL+'AnyCourse/PictureNoteServlet.do',
				method : 'POST',
				cache :false,
				data : {
					"state" : "delete",
					"pictureNoteId" : id,					
				},				
				success : function(data) {
					$("#pictureNote_"+id).remove();
				},
			});
		}
		
		function shareNote(){
			
			$.ajax({  
				url : ajaxURL+'AnyCourse/TextNoteServlet.do',
				method : 'POST',
				cache :false,
				data : {
					"state" : "share",					
					"unitId" : get("unitId"),				
				},				
				success : function(data) {
					$("#shareModal").modal('show');
				},
				error : function() {
				}
			});

		}
		function notShareNote(){
			
			$.ajax({  
				url : ajaxURL+'AnyCourse/TextNoteServlet.do',
				method : 'POST',
				cache :false,
				data : {
					"state" : "notShare",					
					"unitId" : get("unitId"),				
				},				
				success : function(data) {
					$("#notShareModal").modal('show');
				},
				error : function() {
				}
			});
		}
		
		$('#shareNote').click(function(){
			if($('#shareNote').hasClass('btn-primary')){
				$('#shareNote').text("已分享");
				$('#shareNote').removeClass('btn-primary');
	        	$('#shareNote').addClass('btn-danger');
	        	shareNote();
	        	
			}
			else{$('#shareNote').text("分享");
				$('#shareNote').removeClass('btn-danger');
	        	$('#shareNote').addClass('btn-primary');
	        	notShareNote();
			}
		})
		
