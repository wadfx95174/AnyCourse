 $(function () {

	  	checkLogin("../", "../../../");
	  
	  	$.ajax({
	  		url:'http://localhost:8080/AnyCourse/CalendarServlet.do',
	  		method:'GET',
	  		data:{
	  			method:'getCoursePlan'
	  		},
	  		error:function(){
	  			console.log('get coursePlan error');
	  		},
	  		success:function(result){
	  			console.log('coursePlan result:');
	  			console.log(result);
	  			for(var i = 0; i < result.length; i++)
	  			{
		  			$('#external-events').append(
		  				'<div class="external-event">'+result[i].unit_name+'</div>'
		  			);
	  			}
	  	        ini_events($('#external-events div.external-event'), result);
	  		}
	  	});
	  
        // ----初始化外部事件----
        function ini_events(ele, coursePlanList) {
          var index = 0;	// 跑each迴圈用
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
            	$('#selectedEvent').css('background-color', $(this).css('background-color'));
            });

            // 讓事件可以被拖動
            $(this).draggable({
              zIndex: 1070,
              revert: true, // will cause the event to go back to its
              revertDuration: 0  //  original position after the drag
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
			url: 'http://localhost:8080/AnyCourse/CalendarServlet.do',
			type: 'GET',
			dataType: "json", 
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
			        	    var title = prompt('Event Title:');
			        	    if (title) {
			        	    	$('#calendar').fullCalendar('renderEvent',
			        	            {
			        	                title: title,
			        	                start: start,
			        	                end: end,
			        	                allDay: allDay
			        	            },
			        	            true // make the event "stick"
			        	        );
			        	    }
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
		            		url : 'http://localhost:8080/AnyCourse/CalendarServlet.do',
		            		method: 'POST',
		            		data: {
		            			title: copiedEventObject.title,
	   	        		  		//url: copiedEventObject.url,
	   	        		  		start: startTime.format('YYYY-MM-DD HH:mm:ss'),//copiedEventObject.start.toISOString(),
	   	        		  		end: endTime.format('YYYY-MM-DD HH:mm:ss'),//copiedEventObject.end.toISOString(),
	   	        		  		backgroundColor: copiedEventObject.backgroundColor,
	   	        		  		borderColor: copiedEventObject.borderColor,
	   	        		  		method: "insert"
		            		},
		            		error: function(){
		            			alert("InsertFail");
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
			        	  //event.start.add(1, 'days');//modify 
			        	  //alert('Event: ' + event.start.toISOString());
			        	  console.log(event);
			        	  //console.log(jsEvent);
			        	  //2018-03-28T18:35:00
			        	  
			        	  
			        	  // 前往該事件網址
			        	  if (event.url) {
//			        	      window.open(event.url);
				        	  //url = "../PlayerInterface.html?unit_id="+unit_id+"&type="+type;//此處拼接內容
				        	  url = "../PlayerInterface.html?unit_id=1&type=1";//此處拼接內容
				        	  window.location.href = url;
			        	      return false;
			        	    }

			        	  
			        	  
			        	  var m = moment();
			        	  $('#calendar').fullCalendar('updateEvent', event);//notify change 
			        	 }
			      	  // ----移動事件----
			          ,eventDrop: function(event, delta, revertFunc) {
			        	 console.log(event.id + " " + event.title + " was dropped on " + event.start.format());
		
			        	 // 如果取消改變，就把event還原回去不動作(revertFunc())
			        	 //if (!confirm("Are you sure about this change?")) {
			        	 //  revertFunc();
			        	 //}
			        	    
		        	     if (event.end != null)
		        	        $.post("http://localhost:8080/AnyCourse/CalendarServlet.do", 
		        			  {
		        		  		 id: event.id,
		        		  		 title: event.title,
		        		  		 url: event.url,
		        		  		 start: event.start.toISOString(),
		        		  		 end: event.end.toISOString(),
		        		  		 method: "update"
		        			  });
		        	     else 
		        		    $.post("http://localhost:8080/AnyCourse/CalendarServlet.do", 
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