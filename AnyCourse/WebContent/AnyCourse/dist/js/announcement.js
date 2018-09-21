var id;                // 暫存 公告 id
var title = '';        // 暫存 公告標題
var content = '';      // 暫存 公告內容
var currentIndex = 0;  // 暫存 公告 index
var sameUserFlag = false;     // 表示是否有自己的公告

let changeBtn = '<button id="change-btn" class="btn btn-primary">更改</button>';
let deleteBtn = '<button id="delete-btn" class="btn btn-danger" data-toggle="modal" data-target="#DeleteModal">刪除</button>';
let submitBtn = '<button id="submit-btn" class="btn btn-primary" data-toggle="modal" data-target="#ChangeModal">送出</button>';
let cancelBtn = '<button id="cancel-btn" class="btn btn-danger">取消</button>';
let createBtn = '<button id="create-btn" class="btn btn-primary">新增</button>';

function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
}

// 設置每個群組內的網址 (ex. 公告、討論區...)
function setGroupUrl()
{
      var groupId = get('groupId');
      $('.tabClass>a').each(function () {
            $(this).attr("href", $(this).attr("href") + '?groupId=' + groupId);
      });
}

// 檢查網址是否沒有 groupId，若沒有則跳轉至首頁
function checkGroupId()
{
      if (get('groupId') == undefined)
            window.location = ajaxURL + 'AnyCourse/AnyCourse/HomePage.html';
}

$(function(){
      checkLogin("../", "../../../");
      checkGroupId();
	$.ajax({
		url: ajaxURL + 'AnyCourse/AnnouncementServlet.do',
		method: 'GET',
		data: {
			'groupId': get('groupId')
		},
		success: function(result){
                  // 在 success 時設置好網址
                  setGroupUrl();
                  console.log(result);
                  result = result.sort(function (a, b) {
                        return timeCompare(a.time, b.time) ? -1 : 1;
                  });
                  for (currentIndex = 0; currentIndex < result.length; currentIndex++)
                  {
                        // 設置個人Box (一個一個的公告)
                        createBox(currentIndex, result[currentIndex].nickName);
                        // 塞公告標題
                        $("#body-" + currentIndex).children('div.announcement-area').append('<h2>' + result[currentIndex].title + '</h2>');
                        // 塞公告內容
                        var contents = result[currentIndex].content.split('\n');
                        for (var index in contents)
                        {
                              $("#body-" + currentIndex).children('div.announcement-area').append('<p>' + contents[index] + '</p>');
                        }
                        // 添加更改的按鈕 (改自己的公告)
                        if (result[currentIndex].sameUser)
                        {
                              setBoxButton(currentIndex);
                              $('#submit-btn, #cancel-btn').toggle();
                              sameUserFlag = true;
                        }
                  }
                  // 若沒有自己的公告，則在下方設置新增按鈕
                  if (!sameUserFlag)
                  {
                        $('#main-area').append(createBtn);
                  }
		},
		error: function(){
                  // 當 servlet 沒有回傳東西 -> 非該群組成員
                  $('.content-wrapper').first().html('<div><h2 style="text-align:center; padding-top:50px;">很抱歉，您尚未加入該群組</h2></div>');
                  console.log('get groupId error');
		}
      })

      // 點擊新增按鈕
      $(document).on('click', '#create-btn', function(){
            $(this).toggle();
            id = currentIndex++;
            createBox(id, $('#user-name').text().split('，')[0]);
            $('#body-' + id + '>div').toggleClass('announcement-area change-area');
            $('.change-area').html('<h4>標題</h4><input type="text" class="form-control">');
            $('.change-area').append('<h4>內容</h4><textarea class="form-control"></textarea>');
            setBoxButton(id);
            $('#change-btn, #delete-btn').toggle();
      });

      // 點擊更改按鈕
      $(document).on('click', '#change-btn', function(){
            // 取當前的公告 並暫存起來
            id = $(this).parent().attr("id").split('-')[1];
            title = $('#body-' + id + '>div.announcement-area>h2').text();
            content = '';
            $('#body-' + id + '>div.announcement-area>p').each(function(){
                  content += $(this).text() + '\n';
            });
            // 將 公告區域 更改為 編輯區域
            $('#body-' + id + '>div').toggleClass('announcement-area change-area');
            $('.change-area').html('<h4>標題</h4><input type="text" class="form-control" value="' + title + '">');
            $('.change-area').append('<h4>內容</h4><textarea class="form-control">' + content + '</textarea>');
      });

      $(document).on('click', '#delete-btn', function(){
            id = $(this).parent().attr("id").split('-')[1];
      });

      // 點擊刪除按鈕
      $(document).on('click', '#modal-delete-button', function(){
            $.ajax({
                  type: "post",
                  url: ajaxURL + 'AnyCourse/AnnouncementServlet.do',
                  data: {
                        'method': 'delete',
                        'groupId': get('groupId')
                  },
                  success: function() {
                        removeBox(id);
                        sameUserFlag = !sameUserFlag;
                  },
                  error: function() {
                        console.log('delete error');
                  }
            });
      });

      // 點擊送出按鈕 
      $(document).on('click', '#submit-btn', function(){
            // 取更改後的值
            var changeTitle = $('.change-area>input').val();
            var changeContent =  $('.change-area>textarea').val();
            // 送到資料庫更改
            if (sameUserFlag)
                  // 更改
                  $.ajax({
                        type: "post",
                        url: ajaxURL + 'AnyCourse/AnnouncementServlet.do',
                        data: {
                              'method': 'update',
                              'groupId': get('groupId'),
                              'title': changeTitle,
                              'content': changeContent
                        },
                        success: function() {
                              // 設置新的公告內容
                              setAnnouncement(id, changeTitle, changeContent);
                              //通知群組其他成員
                              groupAnnouncementNotify();
                        },
                        error: function() {
                              console.log('update error');
                        }
                  });
            else
                  // 新增
                  $.ajax({
                        type: "post",
                        url: ajaxURL + 'AnyCourse/AnnouncementServlet.do',
                        data: {
                              'method': 'insert',
                              'groupId': get('groupId'),
                              'title': changeTitle,
                              'content': changeContent
                        },
                        success: function() {
                              // 設置新的公告內容
                              setAnnouncement(id, changeTitle, changeContent);
                              sameUserFlag = !sameUserFlag;
                              //通知群組其他成員
                              groupAnnouncementNotify();
                        },
                        error: function() {
                              console.log('insert error');
                        }
                  });
      });

      // 點擊取消按鈕
      $(document).on('click', '#cancel-btn', function(){
            // 若為編輯的公告則塞回去原本的公告
            // 若為新增的公告則塞回新增按鈕
            sameUserFlag ? setAnnouncement(id, title, content) : removeBox(id);
      });

      // 切換按鈕事件
      $(document).on('click', '#change-btn, #submit-btn, #cancel-btn', function(){
            $('#change-btn, #delete-btn, #submit-btn, #cancel-btn').toggle();
      });

      // 設置公告標題及內容
      function setAnnouncement(targetId, title, content)
      {
            $('#body-' + targetId + '>div').toggleClass('announcement-area change-area');
            $('#body-' + targetId + '>div.announcement-area').html('<h2>' + title + '</h2>');
            var contents = content.split('\n');
            for (var ele in contents)
            {
                  // $('#body-' + targetId + '>div.announcement-area').append('<p>' + contents[ele] + '</p>');
                  $("#body-" + targetId).children('div.announcement-area').append('<p>' + contents[ele] + '</p>');
            }
      }

      function createBox(index, nickName)
      {
            $('#main-area').append('<div class="box box-primary">'
                              +'<!-- box-header -->'
                              +'<div class="box-header ui-sortable-handle" id="header-' + index + '">'
                              +'      <img src="../../dist/img/user2-160x160.jpg" class="img-circle" alt="User Image" />'
                              +'      <h3 class="box-title">'
                              +'            <strong>' + nickName + '</strong>'
                              +'      </h3>'
                              +'</div>'
                              +'<!-- /.box-header -->'
                              +'<!-- box-body -->'
                              +'<div class="box-body" id="body-' + index + '">'
                              +'      <div class="announcement-area">'
                              +'      </div>'
                              +'</div>'
                              +'<!-- /.box-body -->'
                              +'<div class="box-footer clearfix no-border"></div>'
                              +'</div>');
      }

      function setBoxButton(targetId)
      {
            $("#header-" + targetId).append(deleteBtn + changeBtn);
            $('#body-' + targetId).append(cancelBtn + submitBtn);
      }

      function removeBox(targetId)
      {
            $('#body-' + targetId).parent().remove();
            $('#main-area').append(createBtn);
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
}) 
//通知群組其他成員
function groupAnnouncementNotify(){
      $.ajax({
            url:ajaxURL + 'AnyCourse/NotificationServlet.do',
            method:'POST',
            cache:false,
            data:{
                  'action': "groupAnnouncement",
                  'groupId': get('groupId'),
                  'url': ajaxURL + "AnyCourse/AnyCourse/pages/Group/Announcement.html?groupId=" + get('groupId')
            },
            success:function(response){
                  console.log(response);

                  for(var i = 0;i < response.length;i ++){
                        ws.send(JSON.stringify({
                            type: "groupAnnouncement",
                            toUserId: response[i].toUserId,
                            notificationId: response[i].notificationId,
                            nickname: response[i].nickname,
                            groupId: response[i].groupId,
                            groupName: response[i].groupName,
                            url: response[i].url
                        }));
                  }
            },
            error:function(xhr, ajaxOptions, thrownError){
                  console.log(xhr);
                  console.log(thrownError);
                  console.log("announcement.js insert notification error");
            }
      });
}