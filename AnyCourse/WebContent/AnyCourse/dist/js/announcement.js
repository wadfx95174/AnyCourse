//var ajaxURL="http://140.121.197.131:7603/";
var ajaxURL="http://localhost:8080/";

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
                  var sameUserFlag = false;
                  for (var i = 0; i < result.length; i++)
                  {
                        $('#main-area').append('<div class="box box-primary">'
                              +'<!-- box-header -->'
                              +'<div class="box-header ui-sortable-handle" id="header-' + i + '">'
                              +'      <img src="../../dist/img/user2-160x160.jpg" class="img-circle" alt="User Image" />'
                              +'      <h3 class="box-title">'
                              +'            <strong>' + result[i].nickName + '</strong>'
                              +'      </h3>'
                              +'</div>'
                              +'<!-- /.box-header -->'
                              +'<!-- box-body -->'
                              +'<div class="box-body announcement-area" id="body-' + i + '">'
                              +'      <h2>' + result[i].title + '</h2>'
                              +'</div>'
                              +'<!-- /.box-body -->'
                              +'<div class="box-footer clearfix no-border"></div>'
                              +'</div>');
                        // 添加更改的按鈕 (改自己的公告)
                        if (result[i].sameUser)
                        {
                              let changeBtn = '<button class="btn btn-primary">更改</button>';
                              let deleteBtn = '<button class="btn btn-danger">刪除</button>';
                              $("#header-" + i).append(deleteBtn + changeBtn);
                              sameUserFlag = true;
                        }
                        // 塞公告內容
                        var contents = result[i].content.split('\n');
                        for (var index in contents)
                        {
                              $("#body-" + i).append('<p>' + contents[index] + '</p>');
                        }
                  }
                  // 若沒有自己的公告，則在下方設置新增按鈕
                  if (sameUserFlag)
                  {
                        let createBtn = '<button class="btn btn-primary">新增</button>';
                        $('#main-area').append(createBtn);
                  }
		},
		error: function(){
                  // 當 servlet 沒有回傳東西 -> 非該群組成員
                  $('.content-wrapper').first().html('<div><h2 style="text-align:center; padding-top:50px;">很抱歉，您尚未加入該群組</h2></div>');
                  console.log('get groupId error');
		}
	})
}) 