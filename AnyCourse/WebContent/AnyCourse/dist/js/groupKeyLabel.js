function formatTime(seconds) {
    return [
        parseInt(seconds / 60 / 60),
        parseInt(seconds / 60 % 60),
        parseInt(seconds % 60)
    ]
        .join(":")
        .replace(/\b(\d)\b/g, "0$1");
}

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

$(document).ready(function() {
    checkLogin("../", "../../../");
	// 載入頁面時，先檢查有沒有 groupId 這個參數
	checkGroupId();
	//取得資料庫的資料
	$.ajax({
		url : ajaxURL+'AnyCourse/GroupKeyLabelServlet.do',
        method : 'GET', 
        data: { groupId: get('groupId') },
		cache :false,
		success:function(result){
			// 在 success 時設置好網址
			setGroupUrl();
			if(window.screen.width > 480){
				$('#keyLabelHeader').append('<div class="col-xs-1 text-center"><i class="fa fa-tags"></i></div>'
						+'<div class="col-xs-2 text-center"><strong>標籤</strong></div>'
						+'<div class="col-xs-5 text-center"><strong>單元</strong></div>'
						+'<div class="col-xs-3 text-center"><strong>時間</strong></div>'
						+'<div class="col-xs-1 text-center"></div>');
				for(var i = 0 ;i < result.length;i++){
					
	    			$('#KeyLabelList').append('<li class="list-group-item" id="searchRecordID_'+ (i+1) +'">'
							+'<div class="row">'
							+'<div class="col-xs-1 text-center"><i class="fa fa-tags"></i></div>'
							+'<div class="keyLabelName col-xs-2 text-center"><a href="../PlayerInterface.html?type='+ (result[i].url.split("/")[2]=="www.youtube.com"?1:2) + '&unitId='+result[i].unitId+'">' + result[i].keyLabelName + '</a></div>'
							+'<div class="unitName col-xs-5 text-center">' + result[i].unitName + '</div>'
							+'<div class="col-xs-3">'
							+'<div class="col-xs-12 col-md-6 text-center">' + formatTime(result[i].beginTime) + '</div>'
							+'<div class="col-xs-12 col-md-6 text-center">' + formatTime(result[i].endTime) + '</div>'
							+'</div>'
							+'</div></li>');
				}
			}
			else{
				$('#keyLabelHeader').append('<div class="col-xs-1 text-center"><i class="fa fa-tags"></i></div>'
						+'<div class="col-xs-4 text-center"><strong>標籤</strong></div>'
						+'<div class="col-xs-6 text-center"><strong>單元</strong></div>');
				for(var i = 0 ;i < result.length;i++){
					
	    			$('#KeyLabelList').append('<li class="list-group-item" id="searchRecordID_'+ (i+1) +'">'
							+'<div class="row">'
							+'<div class="col-xs-1 text-center"><i class="fa fa-tags"></i></div>'
							+'<div class="keyLabelName col-xs-4 text-center"><a href="../PlayerInterface.html?type='+ (result[i].url.split("/")[2]=="www.youtube.com"?1:2) + '&unitId='+result[i].unitId+'">' + result[i].keyLabelName + '</a></div>'
							+'<div class="unitName col-xs-6 text-center">' + result[i].unitName + '</div>'
							+'</div></li>');
				}
			}
    		
    	},
		error:function(){
			// 當 servlet 沒有回傳東西 -> 非該群組成員
			$('.content-wrapper').first().html('<div><h2 style="text-align:center; padding-top:50px;">很抱歉，您尚未加入該群組</h2></div>');
			console.log('get groupId error');
		}
	});
});