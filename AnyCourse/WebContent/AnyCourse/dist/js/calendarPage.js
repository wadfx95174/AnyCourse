 $(function () {

	  	checkLogin("../", "../../../");
	  
	  	$.ajax({
	  		url:'http://140.121.197.130:8400/AnyCourse/CalendarServlet.do',
	  		method:'GET',
	  		cache :false,
	  		data:{
	  			method:'getCoursePlan'
	  		},
	  		error:function(){
	  			console.log('get coursePlan error');
	  		},
	  		success:function(result){
	  			console.log('coursePlan result:');
	  			console.log(result);
	  			coursePlanList = result;
	  			for(var i = 0; i < coursePlanList.length; i++)
	  			{
		  			$('#external-events').append(
		  				'<li><a href="javascript:void(0)">'+coursePlanList[i].unit_name+'</a></li>'
		  			);
	  			}
	  	        ini_events($('#external-events li'));
	  		}
	  	});
	  

        // 讓已選事件可以被拖動
        $('#selectedEvent').draggable({
          zIndex: 1070,
          revert: true, // will cause the event to go back to its
          revertDuration: 0  //  original position after the drag
        });
	  	
        // ----初始化外部事件----
        var index = 0;	// 跑each迴圈用
        function ini_events(ele) {
          ele.each(function () {
            // 宣告EventObject (可以不用start跟end)
            var eventObject = {
              title: coursePlanList[index].unit_name,
              unitId: coursePlanList[index].unit_id,
              type: coursePlanList[index].video_type
            };

            // 把eventObject存到DOM裡面，之後就可以取得
            $(this).data('eventObject', eventObject);
            
            $(this).click(function(){
            	console.log($(this).data('eventObject'));		//********************************
            	$('#selectedEvent').text($(this).text());
            	$('#selectedEvent').data($(this).data());
            });
            index++;
          });
        }

        // ----初始化行事曆----
        
        // calendar的Date型態
        var date = new Date();
        var d = date.getDate(),
                m = date.getMonth(),
                y = date.getFullYear();
        
		var m = moment() // Start, End to dateTime
        
        // 從資料庫取得行事曆的資料，並更新至頁面
        $.ajax({
			url: 'http://140.121.197.130:8400/AnyCourse/CalendarServlet.do',
			type: 'GET',
			dataType: "json", 
			cache :false,
			data: {
				method:"getEvent"
			},
			error: function(){
				console.log('get CalendarInfo error!');
			},
			success: function(response){
				console.log(response);
				$('#calendar').fullCalendar({
			          header: {
			            left: 'prev,next today',
			            center:  'title',
			            right: 'month,agendaWeek,agendaDay'
			          },
			          buttonText: {
			            today: 'today',
			            month: 'month',
			            week: 'week',
			            day: 'day'
			          },
			          events: response,
			          timeFormat: 'hh:mm a',
			          editable: true,
			          droppable: true,
			          selectable: true,
			          selectHelper: true,	// 在week跟day select時顯示時間
			          select: function(start, end, allDay) {
			        	    // 新增一個event Object接selectedEvent
			        	    var selectedObject = $('#selectedEvent').data('eventObject');
			        	    if (selectedObject == undefined)return;
			        	    selectedObject.start = start.format('YYYY-MM-DD HH:mm:ss');
			        	    selectedObject.end = end.format('YYYY-MM-DD HH:mm:ss');
			        	    selectedObject.backgroundColor = $('#selectedEvent').css('background-color'),
			        	    selectedObject.borderColor =  $('#selectedEvent').css('border-color'),
			        	    // 送到資料庫更新
			        	    $.ajax({
			            		url : 'http://140.121.197.130:8400/AnyCourse/CalendarServlet.do',
			            		method: 'POST',
			            		cache: false,
			            		data: {
		        	                title: selectedObject.title,
		   	        		  		url: selectedObject.unitId + '/' + selectedObject.type,
		        	                start: selectedObject.start,
		        	                end: selectedObject.end,
		        	                backgroundColor: selectedObject.backgroundColor,
		   	        		  		borderColor: selectedObject.borderColor,
		   	        		  		method: "insert"
			            		},
			            		error: function(){
			            			console.log('InsertFail');
			            		},
			            		success: function(response){
			            			console.log(response);
			            			selectedObject.id = response;
		   	        				//$('#calendar').fullCalendar('updateEvent', event);
			            			
			            			// 把新增的Object更新到fullcalendar
		   	        				$('#calendar').fullCalendar('renderEvent', selectedObject, true)
						            {	
					 	        	    //alert(copiedEventObject.id + " " + copiedEventObject.title + " " + copiedEventObject.url + " " + copiedEventObject.start + " " + copiedEventObject.end + " " + copiedEventObject.allDay);
						            }; // end renderEvent
						            
			            		} // end success
			            	}); // end ajax
			        	    $('#calendar').fullCalendar('unselect');
			        	},
			          
			          // 當外部事件放進Calendar時觸發
			          drop: function (date) {

			            // retrieve the dropped element's stored Event Object
			            var originalEventObject = $(this).data('eventObject');

			            // we need to copy it, so that multiple events don't have a reference to the same object
			            var copiedEventObject = $.extend({}, originalEventObject);

			            var startTime = window.prompt("start:");
			            var endTime = window.prompt("end:");
			            
			            // assign it the date that was reported
			            copiedEventObject.start = new Date(date + parseInt(startTime) * 60000);
			            copiedEventObject.end = new Date(date + parseInt(endTime) * 60000);
			            copiedEventObject.backgroundColor = $(this).css("background-color");
			            copiedEventObject.borderColor = $(this).css("border-color");
			            var startTime = $.fullCalendar.moment(copiedEventObject.start);
			            var endTime = $.fullCalendar.moment(copiedEventObject.end);
			            console.log(startTime.format());
			            console.log(endTime);
			            
			            $.ajax({
		            		url : 'http://140.121.197.130:8400/AnyCourse/CalendarServlet.do',
		            		method: 'POST',
		            		cache :false,
		            		data: {
		            			title: copiedEventObject.title,
	   	        		  		url: copiedEventObject.unitId + '/' + copiedEventObject.type,
	   	        		  		start: startTime.format('YYYY-MM-DD HH:mm:ss'),//copiedEventObject.start.toISOString(),
	   	        		  		end: endTime.format('YYYY-MM-DD HH:mm:ss'),//copiedEventObject.end.toISOString(),
	   	        		  		backgroundColor: copiedEventObject.backgroundColor,
	   	        		  		borderColor: copiedEventObject.borderColor,
	   	        		  		method: "insert"
		            		},
		            		error: function(){
		            			console.log("InsertFail");
		            		},
		            		success: function(response){
	   	        				copiedEventObject.id = response;
	   	        				//$('#calendar').fullCalendar('updateEvent', event);
	   	        				$('#calendar').fullCalendar('renderEvent', copiedEventObject, true)
					            {	
				 	        	    //alert(copiedEventObject.id + " " + copiedEventObject.title + " " + copiedEventObject.url + " " + copiedEventObject.start + " " + copiedEventObject.end + " " + copiedEventObject.allDay);
					            	
					            }; // end renderEvent
					            
		            		} // end success
		            	}); // end ajax
			            
			            // 若勾選抓取後移除，則移除該元件
			            if ($('#drop-remove').is(':checked')) {
			              $(this).remove();
			            }
			          } // end drop
			          
			          // ----點擊事件----
			          ,eventClick: function(event, jsEvent, view) { 
			        	  console.log(event);

				          if ($('#click-remove').is(':checked')) 
				          {
				        	  if (confirm('確定要刪除 "'+event.title+'"')) 
				        	  {
				        		  $.ajax({
					            		url : 'http://140.121.197.130:8400/AnyCourse/CalendarServlet.do',
					            		method: 'POST',
					            		cache :false,
					            		data: {
					            			eventId: event.id,
				   	        		  		method: "delete"
					            		},
					            		error: function(){
					            			console.log("Delete Event Fail");
					            		},
					            		success: function(){
					            			// 從fullcalendar上移除
							        		$('#calendar').fullCalendar('removeEvents', event.id)
					            		} // end success
					            	}); // end ajax
				        	  }
				        	  return false;
				          }
				          
			        	  // 前往該事件網址
				          else if (event.url) 
			        	  {
				        	  var urls = event.url.split('/');
				        	  url = "../PlayerInterface.html?unit_id="+urls[0]+"&type="+urls[1];//此處拼接內容
				        	  window.location.href = url;
			        	      return false;
			        	  }

			          }
			      	  // ----移動事件----
			          ,eventDrop: function(event, delta, revertFunc) {
			        	 console.log(event.id + " " + event.title + " was dropped on " + event.start.format());
		
			        	 // 如果取消改變，就把event還原回去不動作(revertFunc())
			        	 //if (!confirm("Are you sure about this change?")) {
			        	 //  revertFunc();
			        	 //}
			        	    
		        	     if (event.end != null)
		        	        $.post("http://140.121.197.130:8400/AnyCourse/CalendarServlet.do", 
		        			  {
		        		  		 id: event.id,
		        		  		 title: event.title,
		        		  		 url: event.url,
		        		  		 start: event.start.toISOString(),
		        		  		 end: event.end.toISOString(),
		        		  		 method: "update"
		        			  });
		        	     else 
		        		    $.post("http://140.121.197.130:8400/AnyCourse/CalendarServlet.do", 
		        			  {
		        		  		 id: event.id,
		        		  		 title: event.title,
		        		  		 url: event.url,
		        		  		 start: event.start.toISOString(),
		        		  		 method: "update"
		        			  });
			          } // end eventDrop
			    }); // end fullcalendar
			} // end succuss
		})
		
        /* ADDING EVENTS */
        var currColor = "#3c8dbc"; //Red by default
        //Color chooser button
        var colorChooser = $("#color-chooser-btn");
        $("#color-chooser > li > a").click(function (e) {
          e.preventDefault();
          //Save color
          currColor = $(this).css("color");
          //Add color effect to button
          $('#add-new-event').css({"background-color": currColor, "border-color": currColor});
      	  $('#selectedEvent').css({"background-color": currColor, "border-color": currColor});
        });
        $("#add-new-event").click(function (e) {
          e.preventDefault();
          //Get value and make sure it is not null
          var val = $("#new-event").val();
          if (val.length == 0) {
            return;
          }

          //Create events
          var event = $("<div />");
          event.css({"background-color": currColor, "border-color": currColor, "color": "#fff"}).addClass("external-event");
          event.html(val);
          $('#external-events').prepend(event);

          //Add draggable funtionality
          ini_events(event);

          //Remove event from text input
          $("#new-event").val("");
        })
		})