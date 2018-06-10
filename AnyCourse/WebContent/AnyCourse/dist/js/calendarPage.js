//var ajaxURL="http://140.121.197.130:8400/";
var ajaxURL="http://localhost:8080/";
var events;
var fullEvents;

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

$('#external-events').slimScroll({
	  height:'100px;'
});

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
	var date = moment().format("YYYYMMDDHHmmss");
	console.log(date);
	if (!event.allDay)
	{
		var request = gapi.client.calendar.events.insert({
	        'calendarId': 'primary',
	        'resource': {
		        'summary': event.title,
		        'location': '',
		        'description':event.unitId + '/' + event.type,
		        'id':'fullcalendar' + event.id + date,
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
		        'id':'fullcalendar' + event.id + date,
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
    		  response.url = response.description;
    		  response.start = response.start.dateTime ? (moment(response.start.dateTime).format('YYYY-MM-DD HH:mm:ss')) : (moment(response.start.date).format('YYYY-MM-DD'));
    		  response.end = response.end.dateTime ? (moment(response.end.dateTime).format('YYYY-MM-DD HH:mm:ss')) : (moment(response.end.date).format('YYYY-MM-DD'));
              response.eventType = 'g';
	          
			  $.ajax({
        		url : ajaxURL+'AnyCourse/CalendarServlet.do',
        		method: 'POST',
        		cache:false,
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
		console.log('!allday');
		var request = gapi.client.calendar.events.patch({
	        'calendarId': 'primary',
	        'eventId': event.id,
	        'resource': {
		        'start': {
				  'date': null,
		          'dateTime': event.start,
		          'timeZone': 'Asia/Taipei'
		        },
		        'end': {
		          'date': null,
		          'dateTime': event.end,
		          'timeZone': 'Asia/Taipei'
		        }
		     }
	    });
	}
	else
	{
		// end day null
//		console.log('allday');
		if (event.end == null)
		{
			event.end = event.start
		}
		console.log(event.end);
		var request = gapi.client.calendar.events.patch({
	        'calendarId': 'primary',
	        'eventId': event.id,
	        'resource': {
		        'allDay': true,
		        'start': {
			      'dateTime': null,
		          'date': event.start.format('YYYY-MM-DD'),
		          'timeZone': 'Asia/Taipei'
		        },
		        'end': {
				  'dateTime': null,
		          'date': event.end.format('YYYY-MM-DD'),
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
    var index = 0;	// 跑each迴圈用

    // calendar的Date型態
    var date = new Date();
    var d = date.getDate(),
            m = date.getMonth(),
            y = date.getFullYear();

	var m = moment() // Start, End to dateTime
	
    // ----初始化外部事件----
	getCoursePlanEvent();

    // 讓已選事件可以被拖動
    $('#selectedEvent').draggable({
      zIndex: 1070,
      revert: true, // will cause the event to go back to its
      revertDuration: 0  //  original position after the drag
    });
  	
    


    // ----初始化行事曆----
    
    
    // 從資料庫取得行事曆的資料，並更新至頁面
    $.ajax({
		url: ajaxURL+'AnyCourse/CalendarServlet.do',
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
			fullEvents = response;
			initCalendar(fullEvents);
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
      $('#external-events').append(
  		  '<li><a href="javascript:void(0)">'+val+'</a></li>'
  	  );
      
      //Add draggable funtionality
      $('#external-events li').last().click(function(){
    	  $('#selectedEvent').text($(this).text());
	      $('#selectedEvent').data('eventObject', {title: $(this).text()});
	  });

      //Remove event from text input
      $("#new-event").val("");
    })
    
})

function getCoursePlanEvent()
{
  	$.ajax({
  		url: ajaxURL+'AnyCourse/CalendarServlet.do',
  		method: 'GET',
  		cache: false,
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
	  				'<li><a href="javascript:void(0)">'+coursePlanList[i].unitName+'</a></li>'
	  			);
  			}
  			$('#external-events li').each(function(index){
  			    // 宣告EventObject (可以不用start跟end)
  			    var eventObject = {
  			          title: coursePlanList[index].unitName,
  		              unitId: coursePlanList[index].unitId,
  		              type: coursePlanList[index].videoType
  			    	};
  			    // 把eventObject存到DOM裡面，之後就可以取得
  			    $(this).data('eventObject', eventObject);
  			    $(this).click(function(){
	  		    	console.log($(this).data('eventObject'));		//********************************
	  		    	$('#selectedEvent').text($(this).text());
	  		    	$('#selectedEvent').data($(this).data());
	  		        $('#checkSelectedListBtn').removeAttr('disabled');
	  		    });
  			});
  		}
  	});
}

function initCalendar(eventSrc)
{
	var leng = 1 + (( ( $(window).width()-100 ) / $(window).height() ) - 1) * 0.5;
	console.log(leng);
	$('#calendar').fullCalendar({
//		height: $(window).height(),
//		contentHeight: $(window).width()*1.25,
		aspectRatio: (leng),
//		aspectRatio: 1.2,
		// 視圖的擺放
        header: {
          left: 'prev,next today',
          center:  'title',
          right: 'month,agendaWeek,agendaDay'
        },
        // 按鈕的文字
        buttonText: {
          today: '今天',
          month: '月',
          week: '周',
          day: '天'
        },
        // 結合Google日曆
        googleCalendarApiKey: 'AIzaSyAz3zOHiCMMnShglhMhAvFsRl10juJs2oo',
        eventSources:{
	        googleCalendarId: 'primary'
        },
        // 資料庫中的事件
        events: eventSrc,
        // 日期格式
        timeFormat: 'hh:mm a',
//	          allDayDefault: true,
        // 行事曆的操作
        editable: true,
        droppable: true,
        selectable: true,
    	// 在week跟day select時顯示時間
        selectHelper: true,
        
        // ----用拖曳的方式選擇日期或時間----
        select: function(start, end) {
      	    // 新增一個event Object接selectedEvent
      	    var selectedObject = $('#selectedEvent').data('eventObject');
//      	    console.log(selectedObject);
      	    // 若沒有選擇則不做任何事
      	    if (selectedObject == undefined)
      	    	return;
      	    
      	    selectedObject.start = start.format('YYYY-MM-DD HH:mm:ss');
      	    selectedObject.end = end.format('YYYY-MM-DD HH:mm:ss');
      	    selectedObject.allDay = !start.hasTime() && !end.hasTime();
      	    selectedObject.backgroundColor = $('#selectedEvent').css('background-color'),
      	    selectedObject.borderColor =  $('#selectedEvent').css('border-color'),
      	    selectedObject.url = selectedObject.unitId + '/' + selectedObject.type;
      	    // 送到資料庫更新
      	    $.ajax({
          		url : ajaxURL+'AnyCourse/CalendarServlet.do',
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
		            };
          		}
          	}); // end ajax
      	    $('#calendar').fullCalendar('unselect');
      	},
        
        // 當外部事件放進Calendar時觸發
        drop: function (date) {
            // retrieve the dropped element's stored Event Object
            var originalEventObject = $(this).data('eventObject');

      	    // 若沒有選擇則不做任何事
      	    if (originalEventObject == undefined)
      	    {
      	    	return;
      	    }
      	    
            // we need to copy it, so that multiple events don't have a reference to the same object
            var copiedEventObject = $.extend({}, originalEventObject);

            // assign it the date that was reported
//	            copiedEventObject.start = new Date(date);
//	            copiedEventObject.end = new Date(date);
            copiedEventObject.backgroundColor = $(this).css("background-color");
            copiedEventObject.borderColor = $(this).css("border-color");
            copiedEventObject.start = date.toISOString();
            copiedEventObject.allDay = !date.hasTime();
            copiedEventObject.url = copiedEventObject.unitId + '/' + copiedEventObject.type
//	            var startTime = $.fullCalendar.moment(copiedEventObject.start);
//	            var endTime = $.fullCalendar.moment(copiedEventObject.end);
//	            console.log(startTime.format());
            console.log(date.toISOString());
//	            console.log(!date.hasTime());
          
            $.ajax({
      		    url : ajaxURL+'AnyCourse/CalendarServlet.do',
      		    method: 'POST',
      		    cache :false,
      		    data: {
      			    title: copiedEventObject.title,
      			    url: copiedEventObject.url,
      			    start: copiedEventObject.start,//copiedEventObject.start.toISOString(),
//	        		  		start: startTime.format('YYYY-MM-DD HH:mm:ss'),//copiedEventObject.start.toISOString(),
//	        		  		end: endTime.format('YYYY-MM-DD HH:mm:ss'),//copiedEventObject.end.toISOString(),
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
		            };
      		    }
            }); // end ajax
          
          // 若勾選抓取後移除，則移除該元件
//            if ($('#drop-remove').is(':checked')) {
//          	    $(this).remove();
//            }
        }, // end drop
        
        // ----點擊事件----
        eventClick: function(event, jsEvent, view) { 
      	    console.log(event);
      	    // 如果移除按鈕為勾選狀態
	        if ($('#click-remove').is(':checked')) 
	        {
        	    if (confirm('確定要刪除 "'+event.title+'"')) 
        	    {
              	    // 若是Google日曆的event，則直接刪除Google日曆上的event
        		    if (event.eventType == 'g')
        		    {
	        		    deleteEvent(event);
        			    $('#calendar').fullCalendar('removeEvents', event.id)		
        		    }
        		    // 刪除資料庫
        		    else
        		    {		        			  
	        		    $.ajax({
		            		url : ajaxURL+'AnyCourse/CalendarServlet.do',
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
		            		}
		            	});
        		    }
        	    }
        	    return false;
	        }
	        // 如果添加到Google行事曆為勾選狀態，且事件非Google的事件
	        else if (($('#click-add').is(':checked')) && event.eventType != 'g')
	        {
	        	addEvent(event)
	        	return false;
	        }
	  	    // 都沒勾選且有網址，則前往該事件網址
	        else if (event.url && event.url != 'undefined/undefined') 
	      	{
		        var urls = event.url.split('/');
		        url = "../PlayerInterface.html?unitId="+urls[0]+"&type="+urls[1];
		      	window.location.href = url;
	      	    return false;
	      	}
	        return false;
        },
        // ----改變事件區間----
        eventResize: function(event){
      	    console.log(event);

      	    // 若是Google日曆的event，則直接更新到Google日曆
      	    if (event.eventType=='g')
  		    {
      	 	    updateEvent(event);
  		    }
      	    // 更新資料庫
      	    else
      	    {
      		    $.post(ajaxURL+"AnyCourse/CalendarServlet.do", 
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
        },
    	// ----移動事件----
        eventDrop: function(event, delta, revertFunc) {
      	    console.log(event.id + " " + event.title + " was dropped on " + event.start.format());
      	    console.log(event.allDay);
//      	    // 如果取消改變，就把event還原回去不動作(revertFunc())
//      	    if (!confirm("Are you sure about this change?")) {
//      	      revertFunc();
//      	    }
      	    // 若是Google日曆的event，則直接更新到Google日曆
      	    if (event.eventType=='g')
  		    {
      	    	// 若沒有end且不是allday，預設為開始時間 +2 小時
      	    	if (event.end == null)
      	    	{
      	    		if (event.allDay != true)
      	    		{
          	    		event.end = moment(event.start);
          	    		event.end.hour(event.end.hour()+2);
      	    		}
      	    		else
      	    		{
          	    		event.end = moment(event.start);
          	    		event.end.day(event.end.day()+1);
      	    		}
      	    	}
      	 	    updateEvent(event);
  		    }
      	    // 更新資料庫
      	    else if (event.end != null)
  	            $.post(ajaxURL+"AnyCourse/CalendarServlet.do",
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
  		        $.post(ajaxURL+"AnyCourse/CalendarServlet.do",
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
}

// 設定modal裡面的值
function setSelectedList()
{
	var title = $('#selectedEvent').text();
	$('#modalTitle').text(title);
    $('#SelectList').html('');
    for (var i = 0; i < fullEvents.length; i++)
	{
    	if (fullEvents[i].title == title)
    	    $('#SelectList').append(
    	        	'<li class="list-group-item">'+
    	    		'<a id="item-0" href="#" data-dismiss="modal" onclick="jumpToDate(\''+fullEvents[i].start.split(' ')[0]+'\')">'+
    	    		'<div class="row">'+
    	    		//'2018 年 3 月 4 日 下午 8 點 30 分'+
    	    		toChineseTimeFormat(fullEvents[i].start, fullEvents[i].allDay) +
    	    		'</div>'+
    	    		'</a>'+
    	    		'</li>'
    	    );
	}
}

//  2018-05-15 20:30:00.0 -> 2018 年 5 月 15 日 下午 8 點 30 分
function toChineseTimeFormat(time, allday)
{
	var array = time.split(/-|:| |\./);
	var format = array[0] + ' 年 ' + ~~array[1] + ' 月 ' + ~~array[2] + ' 日 ';		// ~~可去掉開頭0
	format += allday == true ? ' 整天' : (array[3] >= 12 ? '下午 ' + ~~(array[3]-12) : '上午 ' + ~~(array[3])) + ' 點' + (array[4] > 0 ? ' ' + ~~array[4] + ' 分' : ' 整');
	return format;
}

// fullcalendar 跳到該日期
function jumpToDate(date)
{
	$('#calendar').fullCalendar('gotoDate', date );
}
