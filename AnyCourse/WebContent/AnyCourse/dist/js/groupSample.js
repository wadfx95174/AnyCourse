//----------------------------------------- In JS -----------------------------------------

// 取網址列的參數
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
      
      // 載入頁面時，先檢查有沒有 groupId 這個參數
      checkGroupId();

      // 把 groupId 送到 Servlet 檢查
      $.ajax({
		url: ajaxURL + 'AnyCourse/AnnouncementServlet.do',
		method: 'GET',
		data: {
			'groupId': get('groupId')
		},
            success: function (response) {
                  // 在 success 時設置好網址
                  setGroupUrl();
            },
		error: function(){
                  // 當 servlet 沒有回傳東西 -> 非該群組成員
                  $('.content-wrapper').first().html('<div><h2 style="text-align:center; padding-top:50px;">很抱歉，您尚未加入該群組</h2></div>');
                  console.log('get groupId error');
		}
      });
})

//----------------------------------------- In Servlet -----------------------------------------
/*
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
      // 檢查是否為該群組
      HttpSession session = request.getSession();
      Map<String, Integer> groups = (Map<String, Integer>)session.getAttribute("groups");

      // 檢查 session 裡面有沒有傳進來的 groupId
      if (groups.containsValue(Integer.parseInt(request.getParameter("groupId"))))  
             有  -> 進行回傳動作
      else 
            沒有 -> 不回傳 (可直接省略不寫)
}
*/