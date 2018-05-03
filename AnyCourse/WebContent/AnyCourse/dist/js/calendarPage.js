document.write('<script async defer src="https://apis.google.com/js/api.js"'
      +'onload="this.onload=function(){};handleClientLoad()"'
      +'onreadystatechange="if (this.readyState === "complete") this.onload()">'
    +'</script>');

//Client ID and API key from the Developer Console
var CLIENT_ID = '645783857059-dgmft2d4jb7kgkl1f05l02gorl19r9d0.apps.googleusercontent.com';
var API_KEY = 'AIzaSyAz3zOHiCMMnShglhMhAvFsRl10juJs2oo';

// Array of API discovery doc URLs for APIs used by the quickstart
var DISCOVERY_DOCS = ["https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest"];

// Authorization scopes required by the API; multiple scopes can be
// included, separated by spaces.
var SCOPES = "https://www.googleapis.com/auth/calendar";

var authorizeButton = document.getElementById('authorize-button');

/**
 *  On load, called to load the auth2 library and API client library.
 */
function handleClientLoad() {
  gapi.load('client:auth2', initClient);
}

/**
 *  Initializes the API client library and sets up sign-in state
 *  listeners.
 */
function initClient() {
  gapi.client.init({
    apiKey: API_KEY,
    clientId: CLIENT_ID,
    discoveryDocs: DISCOVERY_DOCS,
    scope: SCOPES
  }).then(function () {
    // Listen for sign-in state changes.
    gapi.auth2.getAuthInstance().isSignedIn.listen(updateSigninStatus);

    // Handle the initial sign-in state.
    updateSigninStatus(gapi.auth2.getAuthInstance().isSignedIn.get());
    authorizeButton.onclick = handleAuthClick;
  });
}

var events;
/**
 *  Called when the signed in status changes, to update the UI
 *  appropriately. After a sign-in, the API is called.
 */
function updateSigninStatus(isSignedIn) {
  if (isSignedIn) {
    authorizeButton.style.display = 'none';
    // 開始時間從當下時間往前推一個月
	var date = new Date()
	date.setMonth(date.getMonth()-1);
	// 取得google calendar events
    gapi.client.calendar.events.list({
        'calendarId': 'primary',	// 取個人日曆
        'timeMin': date.toISOString(),	// 從哪天開始
        'showDeleted': false,
        'singleEvents': true,
        'maxResults': 10000,	// 最多多少個事件
        'orderBy': 'startTime'
      }).then(function(response) {
     	  console.log(response);
        events = response.result.items;
        // 更新每個事件到fullcalendar
        for (i = 0; i < events.length; i++) {
            var event = events[i];
            event.title = event.summary;
            event.start = event.start.dateTime ? (moment(event.start.dateTime).format('YYYY-MM-DD HH:mm:ss')) : (moment(event.start.date).format('YYYY-MM-DD'));
            event.end = event.end.dateTime ? (moment(event.end.dateTime).format('YYYY-MM-DD HH:mm:ss')) : (moment(event.end.date).format('YYYY-MM-DD'));
            event.backgroundColor = '#B22222',
            event.borderColor =  '#B22222',
            event.url = event.description;
            event.eventType = 'g',
            $('#calendar').fullCalendar('renderEvent', event, true);
        }
      })
  } else {
    authorizeButton.style.display = 'block';
  }
}

/**
 *  Sign in the user upon button click.
 */
function handleAuthClick(event) {
  gapi.auth2.getAuthInstance().signIn();
}

/**
 *  Sign out the user upon button click.
 */
//function handleSignoutClick(event) {
//  gapi.auth2.getAuthInstance().signOut();
//}

// 加入到 Google Calendar
function addEvent(event)
{
	if (!event.allDay)
	{
		var request = gapi.client.calendar.events.insert({
	        'calendarId': 'primary',
	        'resource': {
		        'summary': event.title,
		        'location': '',
		        'description':event.unitId + '/' + event.type,
		        'id':'fullcalendar'+event.id,
		        'start': {
		          'dateTime': event.start.toISOString(),
		          'timeZone': 'Asia/Taipei'
		        },
		        'end': {
		          'dateTime': event.end.toISOString(),
		          'timeZone': 'Asia/Taipei'
		        }
		     }
	    });
	}
	else
	{
		var request = gapi.client.calendar.events.insert({
	        'calendarId': 'primary',
	        'resource': {
		        'summary': event.title,
		        'location': '',
		        'description':event.unitId + '/' + event.type,
		        'id':'fullcalendar'+event.id,
		        'start': {
			          'date': event.start,
			          'timeZone': 'Asia/Taipei'
		        },
		        'end': {
			          'date': event.end,
			          'timeZone': 'Asia/Taipei'
		        }
		     }
	    });
	}
      request.execute(function(response){
//    	  events = response.result;
//    	  console.log(events);
    	  if (!response.error)
    	  {
    		  response.title = event.title;
    		  response.backgroundColor = '#B22222';
    		  response.borderColor =  '#B22222';
    		  response.url = response.summary;
    		  response.start = response.start.dateTime ? (moment(response.start.dateTime).format('YYYY-MM-DD HH:mm:ss')) : (moment(response.start.date).format('YYYY-MM-DD'));
    		  response.end = response.end.dateTime ? (moment(response.end.dateTime).format('YYYY-MM-DD HH:mm:ss')) : (moment(response.end.date).format('YYYY-MM-DD'));
              response.eventType = 'g';
	          
			  $.ajax({
        		url : 'http://localhost:8080/AnyCourse/CalendarServlet.do',
        		method: 'POST',
        		data: {
        			eventId: event.id,
        		  		method: "delete"
        		},
        		error: function(){
        			console.log("Delete Event Fail");
        		},
        		success: function(){
        			// 從fullcalendar上移除
	        		$('#calendar').fullCalendar('removeEvents', event.id);
	  	            $('#calendar').fullCalendar('renderEvent', response, true);
        		} // end success
        	}); // end ajax  
    	  }
      });
}

// 更新到Google Calendar
function updateEvent(event)
{
	if (!event.allDay)
	{
		var request = gapi.client.calendar.events.patch({
	        'calendarId': 'primary',
	        'eventId': event.id,
	        'resource': {
		        'summary': event.title,
		        'location': '',
		        'description': '',
		        'start': {
		          'dateTime': event.start,
		          'timeZone': 'Asia/Taipei'
		        },
		        'end': {
		          'dateTime': event.end,
		          'timeZone': 'Asia/Taipei'
		        }
		     }
	    });
	}
	else
	{
		var request = gapi.client.calendar.events.patch({
	        'calendarId': 'primary',
	        'eventId': event.id,
	        'resource': {
		        'summary': event.title,
		        'location': '',
		        'description': '',
		        'start': {
		          'date': event.start,
		          'timeZone': 'Asia/Taipei'
		        },
		        'end': {
		          'date': event.end,
		          'timeZone': 'Asia/Taipei'
		        }
		     }
	    });
	}
	
    request.execute();
}

// 刪除 Google Calendar 事件
function deleteEvent(event)
{
	var request = gapi.client.calendar.events.delete({
        'calendarId': 'primary',
        'eventId': event.id,
    });
    request.execute();
}

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
	  			coursePlanList = result;
	  			for(var i = 0; i < coursePlanList.length; i ++)
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
			url: 'http://localhost:8080/AnyCourse/CalendarServlet.do',
			type: 'GET',
			dataType: "json", 
			cache: false,
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
			          googleCalendarApiKey: 'AIzaSyAz3zOHiCMMnShglhMhAvFsRl10juJs2oo',
			          eventSources: 
			              {
				              googleCalendarId: 'primary'
			              },
			          events: response,
			          timeFormat: 'hh:mm a',
//			          allDayDefault: true,
			          editable: true,
			          droppable: true,
			          selectable: true,
			          selectHelper: true,	// 在week跟day select時顯示時間
			          select: function(start, end) {
//			        	    console.log(allDay);
			        	    // 新增一個event Object接selectedEvent
			        	    var selectedObject = $('#selectedEvent').data('eventObject');
			        	    console.log(selectedObject);
			        	    if (selectedObject == undefined)return;
			        	    selectedObject.start = start.format('YYYY-MM-DD HH:mm:ss');
			        	    selectedObject.end = end.format('YYYY-MM-DD HH:mm:ss');
			        	    selectedObject.allDay = !start.hasTime() && !end.hasTime();
			        	    selectedObject.backgroundColor = $('#selectedEvent').css('background-color'),
			        	    selectedObject.borderColor =  $('#selectedEvent').css('border-color'),
			        	    selectedObject.url = selectedObject.unitId + '/' + selectedObject.type;
			        	    // 送到資料庫更新
			        	    $.ajax({
			            		url : 'http://localhost:8080/AnyCourse/CalendarServlet.do',
			            		method: 'POST',
			            		cache: false,
			            		data: {
		        	                title: selectedObject.title,
		   	        		  		url: selectedObject.url,
		        	                start: selectedObject.start,
		        	                end: selectedObject.end,
		        	                allDay: selectedObject.allDay,
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

			            // assign it the date that was reported
//			            copiedEventObject.start = new Date(date);
//			            copiedEventObject.end = new Date(date);
			            copiedEventObject.backgroundColor = $(this).css("background-color");
			            copiedEventObject.borderColor = $(this).css("border-color");
			            copiedEventObject.start = date.toISOString();
			            copiedEventObject.allDay = !date.hasTime();
			            copiedEventObject.url = copiedEventObject.unitId + '/' + copiedEventObject.type
//			            var startTime = $.fullCalendar.moment(copiedEventObject.start);
//			            var endTime = $.fullCalendar.moment(copiedEventObject.end);
//			            console.log(startTime.format());
			            console.log(date.toISOString());
//			            console.log(!date.hasTime());
			            
			            $.ajax({
		            		url : 'http://localhost:8080/AnyCourse/CalendarServlet.do',
		            		method: 'POST',
		            		data: {
		            			title: copiedEventObject.title,
	   	        		  		url: copiedEventObject.url,
	   	        		  		start: copiedEventObject.start,//copiedEventObject.start.toISOString(),
//	   	        		  		start: startTime.format('YYYY-MM-DD HH:mm:ss'),//copiedEventObject.start.toISOString(),
//	   	        		  		end: endTime.format('YYYY-MM-DD HH:mm:ss'),//copiedEventObject.end.toISOString(),
	   	        		  		allDay: copiedEventObject.allDay,
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
				        		  if (event.eventType != 'g')
				        		  {
					        		  $.ajax({
						            		url : 'http://localhost:8080/AnyCourse/CalendarServlet.do',
						            		method: 'POST',
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
				        		  else
				        		  {
					        		  deleteEvent(event);
				        			  $('#calendar').fullCalendar('removeEvents', event.id)				        			  
				        		  }
				        	  }
				        	  return false;
				          }

				          else if (($('#click-add').is(':checked')) && event.eventType != 'g')
				          {
				        	  addEvent(event)
				        	 
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
			          ,eventResize: function(event){
			        	  console.log(event);
			        	  if (event.eventType=='g')
		        		  {
			        	 	  updateEvent(event);
		        		  }
			        	  else
			        	  {
			        		  $.post("http://localhost:8080/AnyCourse/CalendarServlet.do", 
				        			  {
					        		  		 id: event.id,
					        		  		 title: event.title,
					        		  		 url: event.url,
					        		  		 start: event.start.toISOString(),
					        		  		 end: event.end.toISOString(),
					        		  		 allDay: event.allDay,
					        		  		 method: "update"
				        			  });
			        	  }
			          }
			      	  // ----移動事件----
			          ,eventDrop: function(event, delta, revertFunc) {
			        	 console.log(event.id + " " + event.title + " was dropped on " + event.start.format());
			        	 console.log(event.allDay);
			        	 // 如果取消改變，就把event還原回去不動作(revertFunc())
			        	 //if (!confirm("Are you sure about this change?")) {
			        	 //  revertFunc();
			        	 //}
			        	 if (event.eventType=='g')
		        		 {
			        		 updateEvent(event);
		        		 }
			        	 else if (event.end != null)
		        	        $.post("http://localhost:8080/AnyCourse/CalendarServlet.do", 
		        			  {
		        		  		 id: event.id,
		        		  		 title: event.title,
		        		  		 url: event.url,
		        		  		 start: event.start.toISOString(),
		        		  		 end: event.end.toISOString(),
		        		  		 allDay: event.allDay,
		        		  		 method: "update"
		        			  });
		        	     else 
		        		    $.post("http://localhost:8080/AnyCourse/CalendarServlet.do", 
		        			  {
		        		  		 id: event.id,
		        		  		 title: event.title,
		        		  		 url: event.url,
		        		  		 start: event.start.toISOString(),
		        		  		 allDay: event.allDay,
		        		  		 method: "update"
		        			  });
			        	    $('#calendar').fullCalendar('refetchEvents');
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