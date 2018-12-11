var googleEvents;	// 存 Google Calendar Events
var fullEvents;		// 存 FullCalendar Events

var matchEvents = new Map();	// 存 fullEvents 裡面有 Google EventId 的
var unmatchEvents = [];			// 存 fullEvnets 不在 matchEvnets 裡面的

var googleCalendarId = ""; // 存 Google Calendar Id

var deleteFlag = false; // 存 刪除按鈕是否按下
var googleFlag = false; // 存 是否登入 Google

var systemEvents;

/* ----------------------------------- Calendar Page Init ----------------------------------- */
$(function () {
	checkLogin("../", "../../../");

    // 從資料庫取得行事曆的資料，並更新至頁面
    $.ajax({
		url: ajaxURL+'AnyCourse/CalendarServlet.do',
		type: 'GET',
		dataType: 'json', 
		cache :false,
		data: {
			method: 'getEvent'
		},
		error: function(){
			console.log('get CalendarInfo error!');
		},
		success: function(response){
			console.log(response);
			fullEvents = response;
		}
	}).then(function(){
		// console.log(fullEvents);
	
		// 初始化事件設定 (上)
		initEventObject();
		// 初始化事件物件 (中)
		getCoursePlanEvent();
		getVideoListEvent();
		initEventSelecter();
		// 初始化自訂事件 (下)
		initCustomEvent();
		// 初始化行事曆
		initCalendar(fullEvents);
	});
    systemEvents = $('#calendar').fullCalendar('getEventSources');
})

/* ----------------------------------- 初始化事件設定 (上) ----------------------------------- */
// 初始化事件設定 (上)
function initEventObject()
{
	var currColor = "#3c8dbc"; // 預設為天空藍
    var colorChooser = $("#color-chooser-btn"); // 選擇顏色的按鈕
    
    $("#color-chooser > li > a").click(function (e) {
        e.preventDefault();
        // 存顏色
        currColor = $(this).css("color");
        // 設置 selected event & 自訂事件按鈕 的顏色
  	    $('#selectedEvent').css({"background-color": currColor, "border-color": currColor});
		$('#add-new-event').css({"background-color": currColor, "border-color": currColor});
    });
	
    // 讓已選事件可以被拖動
    $('#selectedEvent').draggable({
        zIndex: 1070,
        revert: true, // will cause the event to go back to its
        revertDuration: 0  //  original position after the drag
	});
	
	$('#click-remove').click(function (e) { 
		e.preventDefault();
		deleteFlag = !deleteFlag;
		$(this).toggleClass('btn-secondary btn-danger');
		$(this).text(deleteFlag ? '取消刪除' : '刪除事件');
	});
}

/* ----------------------------------- 初始化事件物件 (中) ----------------------------------- */
// 選擇 課程計畫/清單
function initEventSelecter()
{
    $('#sel1').change(function(){
    	var selectedIndex = $('#sel1')[0].selectedIndex;
//        $('#external-events').hide(); 
        $('.pagination').hide();
        if(selectedIndex == 0) {
            $('#external-events').show(); 
        } else {
            $('#list-events-' + (selectedIndex - 1)).show(); 
        } 
    });

	$('#external-events').slimScroll({
		  height:'100px;'
	});
}

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
  			// console.log('coursePlan result:');
  			// console.log(result);
  			coursePlanList = result;
  			for(var i = 0; i < coursePlanList.length; i++)
  			{
	  			$('#external-events').append(
	  				'<li><a class="external-event-a" href="javascript:void(0)">'+coursePlanList[i].unitName+'</a></li>'
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
//	  		    	console.log($(this).data('eventObject'));
	  		    	$('#selectedEvent').text($(this).text());
	  		    	$('#selectedEvent').data($(this).data());
	  		        $('#checkSelectedListBtn').removeAttr('disabled');
	  		    });
  			});
  		}
  	});
}

function getVideoListEvent()
{
	$.ajax({
		url: ajaxURL+'AnyCourse/CalendarServlet.do',
		method: 'GET',
		cache: false,
		data:{
			method:'getVideoList'
		},
		error:function(){
			console.log('get videoList error');
		},
		success:function(result){
			// console.log('videoList result:');
			// console.log(result);
			videoList = result;
			for(var i = 0; i < videoList.length; i++)
			{
				$('#event').append('<ul class="pagination list-events" style="height:160px;" id="list-events-' + i + '"></ul>');
				$('#sel1').append('<option>' + videoList[i].listName + '</option>')
				$('#list-events-' + i).append(
					'<li><a class="list-event-a" href="javascript:void(0)">'+videoList[i].unitName+'</a></li>'
				);
				$('#list-events-' + i).hide();
			}
			$('.list-events li').each(function(index){
				// 宣告EventObject (可以不用start跟end)
				var eventObject = {
					  title: videoList[index].unitName,
					  unitId: videoList[index].unitId,
					  type: videoList[index].videoType
					};
				// 把eventObject存到DOM裡面，之後就可以取得
				$(this).data('eventObject', eventObject);
				$(this).click(function(){
//					console.log($(this).data('eventObject'));
					$('#selectedEvent').text($(this).text());
					$('#selectedEvent').data($(this).data());
					$('#checkSelectedListBtn').removeAttr('disabled');
				});
			});
		}
	});
}
/* ----------------------------------- 初始化自訂事件 (下) ----------------------------------- */
// 自訂事件
function initCustomEvent()
{
    $("#add-new-event").click(function (e) {
        e.preventDefault();
        // 取使用者輸入的title
        var val = $("#new-event").val();
        if (val.length == 0) {
            return;
        }

        // 輸出事件
        $('#external-events').append(
  		    '<li><a class="external-event-a" href="javascript:void(0)">'+val+'</a></li>'
  	    );
      
        // 添加點擊事件
        $('#external-events li').last().click(function(){
    	    $('#selectedEvent').text($(this).text());
	        $('#selectedEvent').data('eventObject', {title: $(this).text()});
	    });

        // 清空輸入欄位
        $("#new-event").val("");
	})
}

/* ----------------------------------- FullCalendar ----------------------------------- */
function initCalendar(eventSrc)
{
	console.log(eventSrc);
	var leng = 1 + (( ( $(window).width()-100 ) / $(window).height() ) - 1) * 0.5;
	console.log(leng);
	$('#calendar').fullCalendar({
		aspectRatio: (leng),
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
        // 資料庫中的事件
        events: eventSrc,
        // 日期格式
        timeFormat: 'hh:mm a',
        // 行事曆的操作
        editable: true,
        droppable: true,
        selectable: true,
    	// 在week跟day select時顯示時間
        selectHelper: true,
        // ----用拖曳的方式選擇日期或時間----
        select: function(start, end) {
      	    // 新增一個event Object接selectedEvent
        	tmpEventObject = $('#selectedEvent').data('eventObject');
      	    // 若沒有選擇則不做任何事
      	    if (tmpEventObject == undefined)
      	    	return;
      	    var selectedObject = {
  	      	    title: tmpEventObject.title,
  	      	    start: start.format('YYYY-MM-DD HH:mm:ss'),
  	      	    end: end.format('YYYY-MM-DD HH:mm:ss'),
  	      	    allDay: !start.hasTime() && !end.hasTime(),
  	      	    backgroundColor: $('#selectedEvent').css('background-color'),
  	      	    borderColor:  $('#selectedEvent').css('border-color'),
  	      	    url: tmpEventObject.unitId + '/' + tmpEventObject.type,
  	      	    googleEventId: null
      	    }
      	    
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
          			// console.log(response);
          			selectedObject.id = response;
          			// 把新增的Object更新到fullcalendar
        			$('#calendar').fullCalendar('renderEvent', selectedObject, true)
		            {	
	 	        	    //alert(copiedEventObject.id + " " + copiedEventObject.title + " " + copiedEventObject.url + " " + copiedEventObject.start + " " + copiedEventObject.end + " " + copiedEventObject.allDay);
		            };
		            fullEvents.push(selectedObject); 
          		}
          	}); // end ajax
      	    $('#calendar').fullCalendar('unselect');
      	},
        
        // 當外部事件放進Calendar時觸發
        drop: function (date) {
            // 用一個變數去接丟下的 Object
            var originalEventObject = $(this).data('eventObject');

      	    // 若 Object 為undefined (沒有選擇) 則不做任何事
      	    if (originalEventObject == undefined)
      	    {
      	    	return;
      	    }
      	    
            // 複製一個 object，避免多個物件指向同一個物件
            var copiedEventObject = $.extend({}, originalEventObject);

            copiedEventObject.backgroundColor = $(this).css("background-color");
            copiedEventObject.borderColor = $(this).css("border-color");
            copiedEventObject.start = date.toISOString();
            copiedEventObject.allDay = !date.hasTime();
            copiedEventObject.url = copiedEventObject.unitId + '/' + copiedEventObject.type;
            copiedEventObject.googleEventId = null;
//            console.log(date.toISOString());
          
            $.ajax({
      		    url : ajaxURL+'AnyCourse/CalendarServlet.do',
      		    method: 'POST',
      		    cache :false,
      		    data: {
      			    title: copiedEventObject.title,
      			    url: copiedEventObject.url,
      			    start: copiedEventObject.start,//copiedEventObject.start.toISOString(),
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
		            fullEvents.push(copiedEventObject);
      		    }
            }); // end ajax
        }, // end drop
        
        // ----點擊事件----
        eventClick: function(event, jsEvent, view) { 
      	    console.log(event);
      	    // 如果移除按鈕為勾選狀態
	        if (deleteFlag) 
	        {
        	    if (confirm('確定要刪除 "'+event.title+'"')) 
        	    {
              	    // 若是Google日曆的event，則直接刪除Google日曆上的event
        		    // if (event.googleEventId != null)
        		    // {
	        		//     deleteEvent(event);
        			//     $('#calendar').fullCalendar('removeEvents', event.id)
        		    // }
        		    // 刪除資料庫		  
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
    			    fullEvents.splice(findEventIndexById(event.id), 1); // 刪除 1 個元素
        	    }
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
      	    // if (event.googleEventId != null)
  		    // {
      	 	//     updateEvent(event);
  		    // }
      	    // 更新資料庫
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
      	    fullEvents.splice(findEventIndexById(event.id), 1); // 刪除 1 個元素
      	    addToFullEvents(event);
        },
    	// ----移動事件----
        eventDrop: function(event, delta, revertFunc) {
      	    console.log(event.id + " " + event.title + " was dropped on " + event.start.format());
      	    console.log(event.allDay);
      	    
      	    // 若是Google日曆的event，則直接更新到Google日曆
      	    // if (event.googleEventId != null)
  		    // {
      	    // 	// 若沒有end且不是allday，預設為開始時間 +2 小時
      	    // 	if (event.end == null)
      	    // 	{
      	    // 		if (event.allDay != true)
      	    // 		{
          	//     		event.end = moment(event.start);
          	//     		event.end.hour(event.end.hour()+2);
      	    // 		}
      	    // 		else
      	    // 		{
          	//     		event.end = moment(event.start);
          	//     		event.end.day(event.end.day()+1);
      	    // 		}
      	    // 	}
      	 	//     updateEvent(event);
  		    // }
      	    // 更新資料庫
      	    if (event.end != null)
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
  	        fullEvents.splice(findEventIndexById(event.id), 1); // 刪除 1 個元素
  	        addToFullEvents(event); 
        } // end eventDrop
    }); // end fullCalendar
} // end function initCalendar

// 用 Id 找 fullEvents 裡面物件的 index
function findEventIndexById(id)
{
	console.log('id: ' + id);
	console.log($.map(fullEvents, function(item, index) {
			return item.id
		}).indexOf(id));
	return $.map(fullEvents, function(item, index) {
 				return item.id
 			}).indexOf(id)
}

//加入一個 FullCalendar Object 到 Array裡面
function addToFullEvents(event)
{
	var newEventObject = {
		id: event.id,
		title: event.title,
		url: event.url,
		start: event.start.format('YYYY-MM-DD HH:mm:ss'),
		end: event.end != null ? event.end.format('YYYY-MM-DD HH:mm:ss') : null, 	// 切換 allDay 會有  null 產生
		backgroundColor: event.backgroundColor,
		borderColor: event.borderColor,
		allDay: event.allDay
	};
	if (event.googleEventId != undefined)
	{
		newEventObject.googleEventId = event.googleEventId;
		console.log(event.googleEventId);
	}
	fullEvents.push(newEventObject);
}

/* ----------------------------------- Modal ----------------------------------- */

// 設定Modal裡面的值
function setSelectedList()
{
	var outputList = [];
	var title = $('#selectedEvent').text();
	$('#modalTitle').text(title);
    $('#SelectList').html('');
    // 從 fullEvents 裡面挑出所有 selected的 title 加進 outputList
    for (var i = 0; i < fullEvents.length; i++)
	{
    	if (fullEvents[i].title == title)
    		outputList.push(fullEvents[i]);
	}
    // 有符合的結果
    if (outputList.length > 0)
    {
    	// 將outputList按照日期排序
    	outputList = outputList.sort(function (a, b) {
        	return timeCompare(a.start.format==undefined ? a.start : a.start._i, b.start.format==undefined ? b.start : b.start._i) ? 1 : -1;
        });
    	// 輸出到 Modal
        for (var i = 0; i < outputList.length; i++)
        {
        	$('#SelectList').append(
                	'<li class="list-group-item">'+
            		'<a id="item-0" href="#" data-dismiss="modal" onclick="jumpToDate(\'' + (outputList[i].start.format==undefined ? outputList[i].start.split(' ')[0] : outputList[i].start._i) + '\')">'+
            		'<div class="row">&emsp;&emsp;'+
            		//'2018 年 3 月 4 日 下午 8 點 30 分'+
            		toChineseTimeFormat(outputList[i].start.format==undefined ? outputList[i].start : outputList[i].start._i, outputList[i].allDay) +
            		'</div>'+
            		'</a>'+
            		'</li>'
            );
        }
    }
    // 無結果
    else
    {
    	$('#SelectList').append('此事件無行程！');
    }
}

//  2018-05-15 20:30:00.0 -> 2018 年 5 月 15 日 下午 8 點 30 分
function toChineseTimeFormat(time, allday)
{
	var array = time.split(/-|:|T| |\./);
	var format = array[0] + ' 年 ' + ~~array[1] + ' 月 ' + ~~array[2] + ' 日 ';		// ~~可去掉開頭0
	format += allday == true ? ' 整天' : (array[3] >= 12 ? '下午 ' + ~~(array[3]-12) : '上午 ' + ~~(array[3])) + ' 點' + (array[4] > 0 ? ' ' + ~~array[4] + ' 分' : ' 整');
	return format;
}

// 輸入兩個 time, 若 timeA 較大則回傳 true，否則回傳 false
function timeCompare(timeA, timeB)
{
	var arrayA = timeA.split(/-|:|T| |\./);
	var arrayB = timeB.split(/-|:|T| |\./);
	for (var i = 0; i < 6; i++)
	{
		if (arrayA[i] > arrayB[i])
			return true;
		else if (arrayA[i] < arrayB[i])
			return false;
	}
	return true;
}

// FullCalendar 跳到該日期
function jumpToDate(date)
{
	$('#calendar').fullCalendar('gotoDate', date );
}

//// 設置 fullEvents 最後一個元素 start & end 至固定規格
//function eventTimeFormat(index)
//{
//	fullEvents[index].start = fullEvents[index].start._i;
//	fullEvents[index].end = fullEvents[index].end._i;
//}

/* ----------------------------------- Google ----------------------------------- */
document.write('<script async defer src="https://apis.google.com/js/api.js"'
      +'onload="this.onload=function(){};handleClientLoad()"'
      +'onreadystatechange="if (this.readyState === "complete") this.onload()">'
      +'</script>');

// Client ID and API key from the Developer Console
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

/**
 *  Called when the signed in status changes, to update the UI
 *  appropriately. After a sign-in, the API is called.
 */
function updateSigninStatus(isSignedIn) {
    if (isSignedIn) {
		var promiseGoogle = $.ajax({
			url: ajaxURL+'AnyCourse/CalendarServlet.do',
			type: 'GET',
			dataType: 'text', 
			cache :false,
			data: {
				method: 'getGCId'
			},
			error: function(){
				console.log('get GoogleCalendar error!');
			},
			success: function(response){ // 回傳 id
				if (response != "")
				{
					googleCalendarId = response;
					console.log(googleCalendarId);
				}
				else
				{
					console.log("insert");
					insertCalendar();
				}
			}
		});
		promiseGoogle.then(function(){
			authorizeButton.style.display = 'none';
			
			$('#import-button,#export-button').css('display','inline');
			$('#import-button').click(function(){importGoogleEvent();});
			$('#export-button').click(function(){exportGoogleEvent();});

		    // 開始時間從當下時間往前推一個月
			var date = new Date()
			date.setMonth(date.getMonth()-1);
			// 取得google calendar events
		    gapi.client.calendar.events.list({
		        'calendarId': googleCalendarId,	// 取個人日曆
		        'timeMin': date.toISOString(),	// 從哪天開始
		        'showDeleted': false,
		        'singleEvents': true,
		        'maxResults': 10000,	// 最多多少個事件
		        'orderBy': 'startTime'
		    }).then(function(response) {
				console.log(response);
				googleEvents = response.result.items; 
				// checkEventMatch();
		    })
		})
    } 
    else {
    	authorizeButton.style.display = 'inline';
    }
}

function importGoogleEvent()
{
    // 開始時間從當下時間往前推一個月
	var date = new Date()
	date.setMonth(date.getMonth()-1);
	// 取得google calendar events
	gapi.client.calendar.events.list({
        'calendarId': googleCalendarId,	// 取個人日曆
        'timeMin': date.toISOString(),	// 從哪天開始
        'showDeleted': false,
        'singleEvents': true,
        'maxResults': 10000,	// 最多多少個事件
        'orderBy': 'startTime'
    }).then(function(response) {
		console.log(response);
		googleEvents = response.result.items; 
		// checkEventMatch();

		checkMatch()
		var tempMatchEvents = matchEvents;
//		matchEvents.forEach(function(value, key){
//			
//		});
		for (var i = 0; i < googleEvents.length; i++)
		{
			if (matchEvents.has(googleEvents[i].id))
			{
//				console.log(getDateTimeFormat(googleEvents[i].start) + ' ' + getDateTimeFormat(googleEvents[i].end));
				var fullId = parseInt(googleEvents[i].id.substring(12, googleEvents[i].id.length - 14));
				var eventIndex = findEventIndexById(fullId);
//				console.log(getDateTimeFormat(googleEvents[i].start));
//				console.log(getDateTimeFormat(googleEvents[i].end));
				// 將 Google 日期及時間設置到 系統行事曆
				fullEvents[eventIndex].start = getDateTimeFormat(googleEvents[i].start);
				fullEvents[eventIndex].end = getDateTimeFormat(googleEvents[i].end);
				fullEvents[eventIndex].allDay = fullEvents[eventIndex].start.length <= 10;
				// 更新資料庫
	      	    if (fullEvents[eventIndex].end != null)
	  	            $.post(ajaxURL+"AnyCourse/CalendarServlet.do",
	      			    {
	      		  		    id: fullEvents[eventIndex].id,
	      		  		    title: fullEvents[eventIndex].title,
	      		  		    url: fullEvents[eventIndex].url,
	      		  		    start: fullEvents[eventIndex].start,
	      		  		    end: fullEvents[eventIndex].end,
	      		  		    allDay: fullEvents[eventIndex].allDay,
	      		  		    method: "update"
	      			    });
	  	        else 
	  		        $.post(ajaxURL+"AnyCourse/CalendarServlet.do",
	      			    {
	      		  		    id: fullEvents[eventIndex].id,
	      		  		    title: fullEvents[eventIndex].title,
	      		  		    url: fullEvents[eventIndex].url,
	      		  		    start: fullEvents[eventIndex].start,
	      		  		    allDay: fullEvents[eventIndex].allDay,
	      		  		    method: "update"
						  });
				tempMatchEvents.delete(googleEvents[i].id);
			}
		}
//		$('#calendar').fullCalendar('refetchEvents');
		$('#calendar').fullCalendar('removeEvents'  )
		$('#calendar').fullCalendar('addEventSource', fullEvents )
		for (var value of tempMatchEvents.values())
		{
			$.ajax({
				url : ajaxURL+'AnyCourse/CalendarServlet.do',
				method: 'POST',
				cache :false,
				data: {
					eventId: value.id,
						 method: "delete"
				},
				error: function(){
					console.log("Delete Event Fail");
				},
				success: function(){
					// 從fullcalendar上移除
					$('#calendar').fullCalendar('removeEvents', value.id)
				}
			});
			fullEvents.splice(findEventIndexById(value.id), 1); // 刪除 1 個元素
		}
		alert('匯入成功');
    })
}

function exportGoogleEvent()
{
	// console.log('EXPORT');

	checkMatch()
	for (var i = 0; i < googleEvents.length; i++)
	{
		if (matchEvents.has(googleEvents[i].id))
		{
			var fullId = parseInt(googleEvents[i].id.substring(12, googleEvents[i].id.length - 14));
			var eventIndex = findEventIndexById(fullId);
			console.log(eventIndex);
			console.log(fullEvents[eventIndex]);

			updateEvent(fullEvents[eventIndex]);
		}
		else
		{
			var request = gapi.client.calendar.events.delete({
				'calendarId': googleCalendarId,
				'eventId': googleEvents[i].id,
			});
			request.execute();
		}
	}
	for (var i = 0; i < unmatchEvents.length; i++)
	{
		addEvent(unmatchEvents[i]);
	}
	alert('匯出成功');
}

function getDateTimeFormat(timeObj)
{
	return timeObj.dateTime != undefined ? timeObj.dateTime : timeObj.date;
}

function checkMatch()
{
	// 初始化
	matchEvents.clear();
	unmatchEvents = [];
	for (var i = 0; i < fullEvents.length; i++)
	{
		if (fullEvents[i].googleEventId != undefined)
			matchEvents.set(fullEvents[i].googleEventId, fullEvents[i]);
		else
			unmatchEvents.push(fullEvents[i]);
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
	console.log(event);
	console.log(date);
	console.log(event.url);
	console.log(event.start);
	console.log(event.end);
	if (!event.allDay)	// 存 AllDay (dateTime)
	{
		var request = gapi.client.calendar.events.insert({
	        'calendarId': googleCalendarId,
	        'resource': {
		        'summary': event.title,
		        'location': '',
		        'description':event.url,
		        'id':'fullcalendar' + event.id + date,
		        'start': {
		          'dateTime': moment(event.start).toISOString(),
		          'timeZone': 'Asia/Taipei'
		        },
		        'end': {
		          'dateTime': event.end != undefined ? moment(event.end).toISOString() : moment(event.start).toISOString(),
		          'timeZone': 'Asia/Taipei'
		        }
		     }
	    });
	}
	else	// 存非 AllDay (date)
	{
		var request = gapi.client.calendar.events.insert({
	        'calendarId': googleCalendarId,
	        'resource': {
		        'summary': event.title,
		        'location': '',
		        'description':event.url,
		        'id':'fullcalendar' + event.id + date,
		        'start': {
			          'date': event.start.split(' ')[0],
			          'timeZone': 'Asia/Taipei'
		        },
		        'end': {
			          'date': event.end != undefined ? event.end.split(' ')[0] : event.start.split(' ')[0],
			          'timeZone': 'Asia/Taipei'
		        }
		     }
	    });
	}
    request.execute(function(response)
    {
    	if (!response.error)
    	{
    		event.googleEventId = response.id;
			$.ajax({
        		url : ajaxURL+'AnyCourse/CalendarServlet.do',
        		method: 'POST',
        		cache:false,
        		data: {
        			eventId: event.id,
        			googleEventId: event.googleEventId,
        		  	method: "insertGCEvent"
        		},
        		error: function(){
        			console.log("insertGC Event Fail");
        		},
        		success: function(){
        			console.log("success insertGC");
        			fullEvents[findEventIndexById(event.id)].googleEventId = event.googleEventId;
        		}
        	}); // end ajax  
    	} // end if
    });
}

// 更新到Google Calendar
function updateEvent(event)
{
	console.log(event);
	if (!event.allDay)
	{
		console.log('!allday');
		var request = gapi.client.calendar.events.patch({
	        'calendarId': googleCalendarId,
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
	        'calendarId': googleCalendarId,
	        'eventId': event.googleEventId,
	        'resource': {
		        'allDay': true,
		        'start': {
			      'dateTime': null,
//		          'date': event.start.format('YYYY-MM-DD'),
			      date: event.start.split(' ')[0],
		          'timeZone': 'Asia/Taipei'
		        },
		        'end': {
				  'dateTime': null,
//		          'date': event.end.format('YYYY-MM-DD'),
			      date: event.end.split(' ')[0],
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
	console.log(event);
	var request = gapi.client.calendar.events.delete({
        'calendarId': googleCalendarId,
        'eventId': event.googleEventId,
    });
    request.execute();
}

// 新增 Google Calendar
function insertCalendar() {
    return gapi.client.calendar.calendars.insert({
      "resource": {
        "summary": "AnyCourse"
      }
    }).then(function(response) {
        // Handle the results here (response.result has the parsed body).
        console.log("Response", response);
        $.ajax({
			url: ajaxURL+'AnyCourse/CalendarServlet.do',
			type: 'POST',
			cache :false,
			data: {
				method: 'setGCId',
				gcId: response.result.id
			},
			error: function(){
				console.log('set GoogleCalendarId error!');
			},
			success: function(){
				googleCalendarId = response.result.id;
				console.log(googleCalendarId);
			}
        })
    },function(err) { console.error("Execute error", err); });
}