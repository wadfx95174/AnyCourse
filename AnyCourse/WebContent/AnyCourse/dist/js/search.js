var notFoundFlag = false;
var resultArray = new Map();
var listArray = [];
var unitArray = [];

var listButton = true;	// true 為 顯示清單， false 為 顯示單元

var eachPage = 10;		// 每頁幾個
var unitIndex = eachPage;
var listIndex = eachPage;
var totalPage = 0;		// 共幾頁 (預設0)
var currentUnitPage = 0;	// 目前單元頁 (預設0)
var currentListPage = 0;	// 目前清單頁 (預設0)
var totalList = 0;	// 記錄總共幾筆 清單結果
var totalUnit = 0;	// 記錄總共幾筆 單元結果

function get(name)
{
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
      return decodeURIComponent(name[1]);
}
//才能知道要對哪個Id做動作
function getId(id){
    checkId = id;
}
var array = [];
$(document).ready(function(){
	checkLogin("", "../../");
	// 先新增搜尋紀錄
	$.ajax({
		url:ajaxURL+'AnyCourse/SearchServlet.do',
		data: {
			action: 'insertRecord',
			searchQuery: get('searchQuery')
		},
		success: function(response){
			// nothing
		},
		error: function(){
			console.log('insertRecord fail');
		}
	})
	// 精準搜尋
	$.ajax({
		method:"GET",
//		cache :false,			//設為true 不用每次都搜尋一次
		url:ajaxURL+'AnyCourse/SearchServlet.do',
		data: {
			action: 'search',
			searchQuery: get('searchQuery'),
			queryMethod: 'precise'
		},
		dataType: "json",
		success: function(response){
//			$('#loading').hide();
			$('#result').show();
			for (var i = 0; i < response.length; i++)
			{
				// resultArray.set(response[i].courselistId != null ? response[i].courselistId : response[i].units[i].unitId , response[i]);
				// 課程
				if (response[i].courselistId != 0 && response[i].units[0] != null)
				{
					resultArray.set(response[i].courselistId, response[i]);
					listArray.push(response[i]);
				}
				// 單元
				else if (response[i].units[0] != null)
				{
					resultArray.set(response[i].units[0].unitId, response[i]);
					unitArray.push(response[i]);
				}
			}
			console.log(response);
			checkResult();
		},
		error: function(){
			if (notFoundFlag)
			{
//				$('#loading').hide();
				$('#result').show();
				$('#result').append('<br>很抱歉，查無'+get('searchQuery')+'結果');
				console.log("search fail");
			}
			else
				notFoundFlag = true;
		}
	});
	// 模糊搜尋
	$.ajax({
		method:"GET",
//		cache :false,			//設為true 不用每次都搜尋一次
		url:ajaxURL+'AnyCourse/SearchServlet.do',
		data: {
			action: 'search',
			searchQuery: get('searchQuery'),
			queryMethod: 'fuzzy'
		},
		dataType: "json",
		success: function(response){
			$('#loading').hide();
			$('#result').show();
			
			var fuzzyResult = []; 
			for (var i = 0; i < response.length; i++)
			{
				// if (!resultArray.has(response[i].courselistId != null ? response[i].courselistId : response[i].units[i].unitId))
				// 	fuzzyResult.push(response[i]);
				// 課程
				if (response[i].courselistId != 0 && response[i].units[0] != null && !resultArray.has(response[i].courselistId))
				{
					listArray.push(response[i]);
				}
				// 單元
				else if (response[i].units[0] != null && !resultArray.has(response[i].units[0].unitId))	// 有可能錯!!!!!!!!!!!!!!!!!!!!!!!!!!
				{
					unitArray.push(response[i]);
				}
			}
			console.log(response);
			checkResult();
		},
		error: function(){
			if (notFoundFlag)
			{
				$('#loading').hide();
				$('#result').show();
				$('#result').append('<br>很抱歉，查無'+get('searchQuery')+'結果');
				console.log("search fail");
			}
			else
				notFoundFlag = true;
		}
	});

	$("#listResult").click(function(){
		listButton = true;
		setData();
	});
	$("#unitResult").click(function(){
		listButton = false;
		setData();
	})

	$("#lRight").click(function(event){
		console.log(event);
		currentListPage++;
//		$("#lNow").text(currentListPage);
		if(currentListPage+1>totalPage){
			$("#lRight").removeAttr("disabled");
			/*如果是最后一页，就禁用a标签*/
		}
		else{
			$("#lRight").attr('disabled',"true");
			 /*如果不是最后一页，就重新启用a标签*/
		}

		if(currentListPage-1<1){ 
			$("#lLeft").removeAttr("disabled");
			/*如果是第一页，就禁用a标签*/
		}
		else{
			$("#lLeft").attr('disabled',"true");
			/*如果不是第一页，就重新启用a标签*/
		}
		$("#resultList").empty();/*清空上一页显示的数据*/
		listDisplay(listIndex,listIndex=listIndex+eachPage);
		/*显示新一页的数据，*/
	});

	$("#lLeft").click(function(){
		currentListPage--;/*每次点击上一页，页数-1*/
		$("#lNow").text(currentListPage);  //改变分页按钮上显示的页数
		if(currentListPage-1<1){
			$("#lLeft").removeAttr("disabled");
			/*如果是第一页，就禁用a标签*/
		}
		else{
			$("#lLeft").attr('disabled',"true");
			/*如果不是第一页，就重新启用a标签*/
		}

		if(currentListPage+1>totalPage){
			$("#lRight").removeAttr("disabled");
			/*如果是最后一页，就禁用a标签*/
		} 
		else{
			$("#lRight").attr('disabled',"true");
			/*如果不是最后一页，就重新启用a标签*/
		}
		$("#resultList").empty();/*清空上一页显示的数据*/
		listDisplay(listIndex=listIndex-2*eachPage,listIndex=listIndex+eachPage);
		/*显示新一页的数据，*/                   
	});

	$("#uRight").click(function(event){
		console.log(event);
		currentUnitPage++;
//		$("#uNow").text(currentUnitPage);
		if(currentUnitPage+1>totalPage){
			$("#uRight").removeAttr("disabled");
			/*如果是最后一页，就禁用a标签*/
		}
		else{
			$("#uRight").attr('disabled',"true");
			 /*如果不是最后一页，就重新启用a标签*/
		}

		if(currentUnitPage-1<1){ 
			$("#uLeft").removeAttr("disabled");
			/*如果是第一页，就禁用a标签*/
		}
		else{
			$("#uLeft").attr('disabled',"true");
			/*如果不是第一页，就重新启用a标签*/
		}
		$("#resultUnit").empty();/*清空上一页显示的数据*/
		unitDisplay(unitIndex,unitIndex=unitIndex+eachPage);
		/*显示新一页的数据，*/
	});

	$("#uLeft").click(function(){
		currentUnitPage--;/*每次点击上一页，页数-1*/
		$("#uNow").text(currentUnitPage);  //改变分页按钮上显示的页数
		if(currentUnitPage-1<1){
			$("#uLeft").removeAttr("disabled");
			/*如果是第一页，就禁用a标签*/
		}
		else{
			$("#uLeft").attr('disabled',"true");
			/*如果不是第一页，就重新启用a标签*/
		}

		if(currentUnitPage+1>totalPage){
			$("#uRight").removeAttr("disabled");
			/*如果是最后一页，就禁用a标签*/
		} 
		else{
			$("#uRight").attr('disabled',"true");
			/*如果不是最后一页，就重新启用a标签*/
		}
		$("#resultUnit").empty();/*清空上一页显示的数据*/
		unitDisplay(unitIndex=unitIndex-2*eachPage,unitIndex=unitIndex+eachPage);
		/*显示新一页的数据，*/                   
	});

	//影片新增至課程計畫
	$('#addToCoursePlanButton').click(function(){
		
		$.ajax({
			url : ajaxURL+'AnyCourse/SearchServlet.do',
			method : 'POST',
			cache: false,
			data:{
				action:'addToCoursePlan',
				unitId:array[checkId]["units"][checkId]["unitId"]
			},
			error:function(){
				console.log("addToCoursePlan Error!");
			}
		})
	});
	//清單整個新增至課程計畫
	$('#addToCoursePlanButtonList').click(function(e){
		e.preventDefault();
		console.log(array[checkId]["units"][checkId]["unitId"]);
		$.ajax({
			url : ajaxURL+'AnyCourse/HomePageServlet.do',
			method : 'POST',
			cache: false,
			data:{
				action:'addToCoursePlanList',
				courselistId:array[checkId]["courselistId"]
			},
			error:function(e){
				console.log("addToCoursePlanList Error!");
			}
		})
	});
});

function checkResult(){
	// array = response;
	// 找不到結果
	setData();
	if (resultArray.length == 0)
	{
		// 第一次先跳過，第二次顯示查無結果
		if (notFoundFlag)
		{
			console.log('0');
			// $('#result').append('<br>很抱歉，查無 "<strong>'+get('searchQuery')+'</strong>" 結果');
		}
		else
			notFoundFlag = true;
	}
	// 找到結果並顯示
	else
	{
		printResult();
	}
}

function printResult()
{
	// 顯示清單
	// if (listButton)
	// {
		// 清單結果有內容
		if (listArray.length != 0)
		{
			currentListPage = 1;
			// $('#result').empty();
			$('#listArea').show();
			// 第一次顯示
			if (totalList == 0)
			{
				totalList = listArray.length;
				listDisplay(0, totalList > 10 ? 10 : totalList);
				totalPage = 1;
			}
			// 第二次顯示，結果少於 10 筆 (只有1頁)
			else if (totalList < 10)
			{
				listDisplay(totalList, listArray.length > 10 ? 10 : listArray.length);	// *取巧寫法*
				totalList = listArray.length;
				totalPage = 1;
			}
			// 第二次顯示，結果多餘 10 筆 (1頁以上)
			else
			{
				totalPage = ((listArray.length - 1) / eachPage) + 1;
			}
			
		}
		// 清單結果沒有內容
		else
		{
			// $('#result').append('<br>很抱歉，查無 "<strong>'+get('searchQuery')+'</strong>" 結果');
		}
	// }
	// 顯示單元
	// else
	// {
		// 單元結果有內容
		if (unitArray.length != 0)
		{
			currentUnitPage = 1;
			// $('#result').empty();
			$('#unitArea').show();
			// 第一次顯示
			if (totalUnit == 0)
			{
				totalUnit = unitArray.length;
				unitDisplay(0, totalUnit > 10 ? 10 : totalUnit);
				totalPage = 1;
			}
			// 第二次顯示，結果少於 10 筆 (只有1頁)
			else if (totalUnit < 10)
			{
				unitDisplay(totalUnit, unitArray.length > 10 ? 10 : unitArray.length);	// *取巧寫法*
				totalUnit = unitArray.length;
				totalPage = 1;
			}
			// 第二次顯示，結果多餘 10 筆 (1頁以上)
			else
			{
				totalPage = ((unitArray.length - 1) / eachPage) + 1;
			}
			
		}
		// 清單結果沒有內容
		else
		{
			// $('#result').append('<br>很抱歉，查無 "<strong>'+get('searchQuery')+'</strong>" 結果');
		}
	// }
}

// 調整參數
function setData()
{
	// 清單
	if (listButton && listArray.length > 0)
	{
		totalPage = ((listArray.length - 1) / eachPage) + 1;
	}
	// 單元
	else if (!listButton && unitArray.length > 0)
	{
		totalPage = ((unitArray.length - 1) / eachPage) + 1;
	}
}

// 顯示課程 (第幾頁, 一頁幾項)
function listDisplay(begin, end){
	var str; 
	console.log(begin + ' ' + end);
    for(var i = begin; i < end && i < listArray.length; i++){
		console.log(listArray[i]);
		$('#resultList').append(
					'<li>'
					+'<div class="btn-group" style="top: -5px;">'
					+'<button type="button" class="btn btn-noColor dropdown-toggle"'
					+'data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'
					+'<span class="caret"></span>'
					+'</button>'
					+'<ul class="dropdown-menu">'
					+'<li><a data-toggle="modal" data-target="#addToCoursePlanList" onclick="getId('+i+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>新增至課程計畫</a>'
					+'</li>'
					+'</ul>'
					+'</div>'
					+'<a class="list-group-item" href="PlayerInterface.html?type='+ (listArray[i].units[0].videoUrl.split("/")[2]=='www.youtube.com'?1:2) + '&unitId='+listArray[i].units[0].unitId+'&listId='+listArray[i].courselistId+'">'

					+'<div class="media">'
					+'<div class="pull-left" style="padding-left: 0px;">'
					+'<div class="embed-responsive embed-responsive-16by9 col-xs-12">'
					+'<img id="img" class="style-scope yt-img-shadow" alt="" width="230"'
					+'src="'+ (listArray[i].units[0].videoImgSrc != "" ? listArray[i].units[0].videoImgSrc : "https://i.imgur.com/eKSYvRv.png") +'">' 
					+'</div></div>'

					+'<div class="media-body">'
					+'<h4 class="media-heading">'
					+'<b>清單名稱：' + listArray[i].listName + '</b>'
					+'</h4>'
					+'<p class="unitUi">' + listArray[i].schoolName +'&nbsp;&nbsp;' + listArray[i].teacher + ' </p>'
					+'<p class="unitUi">' + listArray[i].likes +' 人喜歡</p>'
					+'<p class="unitUi description">課程簡介：' + listArray[i].courseInfo + '</p>'
					+'</div>'
					+'</div>'
					+'</a></li>'
		);
    }
}
// 顯示單元 (第幾頁, 一頁幾項)
function unitDisplay(begin, end)
{
	var str; 
    for(var i = begin; i < end && i < unitArray.length; i++){
		$('#resultUnit').append(
					'<li>'
					+'<div class="btn-group" style="top: -5px;">'
					+'<button type="button" class="btn btn-noColor dropdown-toggle"'
					+'data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'
					+'<span class="caret"></span>'
					+'</button>'
					+'<ul class="dropdown-menu">'
					+'<li><a data-toggle="modal" data-target="#addToCoursePlan" onclick="getId('+i+')" style="cursor:pointer;"> <i class="ion ion-clipboard"></i>新增至課程計畫</a>'
					+'</li>'
					+'</ul>'
					+'</div>'
					+'<a class="list-group-item" href="PlayerInterface.html?type='+ (unitArray[i].units[0].videoUrl.split("/")[2]=='www.youtube.com'?1:2) + '&unitId='+unitArray[i].units[0].unitId+'">'
					
					+'<div class="media">'
					+'<div class="pull-left" style="padding-left: 0px;">'
					+'<div class="embed-responsive embed-responsive-16by9 col-xs-12">'
					+'<img id="img" class="style-scope yt-img-shadow" alt="" width="230"'
					+'src="'+ (unitArray[i].units[0].videoImgSrc != "" ? unitArray[i].units[0].videoImgSrc : "https://i.imgur.com/eKSYvRv.png") +'">' 
					+'</div></div>'
					
					+'<div class="media-body">'
					+'<h4 class="media-heading">'
					+'<b>影片名稱：' + unitArray[i].units[0].unitName + '</b>'
					+'</h4>'
					+'<br>'
					+'<p class="unitUi">' + unitArray[i].units[0].schoolName + '</p>'
					+'<br>'
					+'<p class="unitUi">' + unitArray[i].units[0].likes +' 人喜歡</p>'
					+'</div>'
					+'</div>'
					+'</a></li>'
		);
    }
}