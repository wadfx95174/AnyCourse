
$('.box-body').slimScroll({
      height: '420px;'
});

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

$(document).ready(function(){
      checkLogin("../", "../../../");
      
      // 載入頁面時，先檢查有沒有 groupId 這個參數，若沒有則跳傳至首頁
      checkGroupId();


      var listId = 1;
      // 把 groupId 送到 Servlet 檢查
      $.ajax({
		url: ajaxURL + 'AnyCourse/GroupCoursePlanServlet.do',
		method: 'GET',
            cache:false,
		data: {
                  action : 'getVideoList',
			groupId: get('groupId')
		},
            success: function (response) {
                  // console.log(response);
                  // 在 success 時設置好網址
                  setGroupUrl();

                  listArray = new Array(response.length);
                  for(var i = 0; i < response.length;i++){
                        $('#allVideoListUL').append('<li id="videoListId_'+listId+'" onclick="getListId('+listId+')">'
                                                      +'<div class="row">'
                                                  +'<div class="col-xs-12">'
                                                        +'<a href="#unitSection" style="color:black;">'
                                                              +'<div style="width:100%;" class="text">'+response[i].listName
                                                              +'</div>'
                                                        +'</a>'
                                                  +'</div>'
                                          +'</div>'
                                          +'</li>');
                        $('#videoListId_'+listId).on("click" , function(){
                              getSeparateUnit();
                        });
                        listId++;
                        listArray[i] = new Array(2);
                        listArray[i][0] = response[i].courselistId;
                        listArray[i][1] = response[i].listName;
                  }
            },
		error: function(){
                  // 當 servlet 沒有回傳東西 -> 非該群組成員
                  $('.content-wrapper').first().html('<div><h2 style="text-align:center; padding-top:50px;">很抱歉，您尚未加入該群組</h2></div>');
                  console.log('get groupId error');
		}
      });
      
      //該按鈕用來獲取所有影片
      $("#coursePlanSelectAll").click(function(e){
            $.ajax({
                  url : ajaxURL+'AnyCourse/GroupCoursePlanServlet.do',
                  method : "GET",
                  cache : false,
                  data:{
                        action : "getAllUnit",
                        groupId: get('groupId')
                  },
                  success:function(response){
                        console.log(response);
                        $('#wantList li,#ingList li,#doneList li').each(function(){
                              $(this).remove();
                        });
                        unitArray = new Array(response.length);
                        showUnitUL(response,unitArray);
                  },
                  error:function(){
                        console.log("get all unit error")
                  }
            })
      });
      
      //刪除影片
      $("#deleteVideoButton").click(function(e){
            //不等於null代表使用者就是該清單的creator
            $.ajax({
                  url : ajaxURL+'AnyCourse/GroupCoursePlanServlet.do',
                  method : 'POST',
                  cache :false,
                data : {
                  action : 'deleteVideo',//代表要delete
                  unitId : unitArray[checkUnitId-1][0]
                },
                  success:function(result){
                        $("#videoId_"+checkUnitId).remove();
            },
                  error:function(){console.log('Delete coursePlan video failed');}
            });
      });
      
      //取得該使用者的所有清單(用來放在下拉式選單，讓使用者選擇要加入哪個清單)
      $.ajax({
            url : ajaxURL+'AnyCourse/HomePageServlet.do',
            method : 'POST',
            data:{
                  action:'getVideoListName'
            },
            cache :false,
            success:function(result){
                  for(var i = 0;i < result.length;i++){
                        $('#addToVideoListModalBody').append('<option value="'+result[i].courselistId+'">'+result[i].listName+'</option>');
                  }
            },
            error:function(){
                  console.log("append videoList to modal error");
            }
      });
      

//----------------------jQueryUI的sortable套件，並且運用start、update兩個事件去把排序資料存進資料庫中-----------------//
      // $( ".column" ).sortable({
      //     connectWith: ".column",
      //     handle: ".portlet-header",
      //     cancel: ".portlet-toggle",
      //     placeholder: "portlet-placeholder ui-corner-all",
      //     //當排序開始時觸發該事件
      //     start:function(event,ui){
      //       //獲取被移動的影片被移動"前"在該sortable的index，並把他存在item的data中
      //       ui.item.data('startPos',ui.item.index());
      //       //拿item的小孩，並把他存在item的data中
      //       ui.item.data('id',ui.item.context.children[3].id);
      //       ui.item.data('sender',ui.item.parent().attr("id"));
      //     },
      //     //當使用者停止排序且DOM位置改變時觸發該事件
      //     update:function(event,ui){
      //       var oldIndex = ui.item.data('startPos');
      //       var newIndex = ui.item.index();//獲取被移動的影片移動"後"在後來的sortable的index
      //       var received = ui.item.parent().attr('id');//獲取被移動的影片被移動"後"是在哪個sortable
      //       var sender = ui.item.data('sender');
      //       var id = ui.item.data('id');
            
      //       //原本她會執行兩次(sender被更新，及received也被更新，這句共讓他只執行一次)
      //       if (this === ui.item.parent()[0]) {
      //             $.ajax({
      //                   url:ajaxURL+'AnyCourse/GroupCoursePlanServlet.do',
      //                   method:'POST',
      //                   cache :false,
      //                   data:{
      //                         action: 'sortable',
      //                         oldIndex: oldIndex + 1,
      //                         newIndex: newIndex + 1,//因為抓到的index是從0開始算，而資料庫是從1開始，所以要加1
      //                         sender: sender,
      //                         received: received,
      //                         unitId: unitArray[id-1][0]//unitId
      //                   },
      //                   error:function(){
      //                         console.log("CoursePlan Sort Error!");
      //                   }
      //             })
      //       }
      //     }
      // });
//----------------------/.jQueryUI的sortable套件，並且運用start、update兩個事件去把排序資料存進資料庫中-----------------//





});
function getListId(id){
      checkListId = id;
}
function getUnitId(id){
      checkUnitId = id;
}
//跳轉至播放介面
function jumpToPlayerInterface(unitId,type,time,groupId){
    url = "../PlayerInterface.html?unitId="+unitId+"&type="+type+"&time="+time+"&groupId="+groupId;//此處拼接內容
    window.location.href = url;
}



//取得個別清單的單元影片
function getSeparateUnit(){
      $.ajax({
            url:ajaxURL+'AnyCourse/GroupCoursePlanServlet.do',
            method:'GET',
            cache:false,
            data:{
                  action : 'getUnit',
                  courselistId : listArray[checkListId-1][0],
                  groupId: get('groupId')
            },
            success:function(response){
                  $('#wantList li,#ingList li,#doneList li').each(function(){
                        $(this).remove();
                  });
                  console.log(response);
                  unitArray = new Array(response.length);
                  showUnitUL(response,unitArray);
            },
            error:function(xhr, ajaxOptions, thrownError){
                  console.log("get coursePlan unit failed");
                  console.log(xhr.status);
                  console.log(xhr.responseText);
                  console.log(thrownError);
            }
      });
}

function showUnitUL(result,unitArray){
      // console.log(result);
      var videoId = 1;
      var listType;
      for(var j = 0;j < result.length;j++){
            //想要觀看
            if(result[j].status == 1)listType = '#wantList';
            //正在觀看
            else if(result[j].status == 2)listType = '#ingList';
            //已觀看完
            else if(result[j].status == 3)listType = '#doneList';

            $(listType).append('<li id ="videoId_'+videoId+'" class="portlet" style="cursor:pointer;">'
                        +'<span class="handle portlet-header">'
                        +'<i class="fa fa-ellipsis-h"></i>'
                        +'</span>'
                        +'<div class="btn-group" style="top: -5px;">'
                        +'<button type="button" class="btn btn-noColor dropdown-toggle"'
                        +'data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'
                        +'<span class="caret"></span>'
                        +'</button>'
                        +'<ul class="dropdown-menu">'
                        +'<li><a data-toggle="modal" data-target="#addToVideoList" onclick="getUnitId('+videoId+')" style="cursor:pointer;" class=" waves-effect waves-block">'
                        +'<i class="ion ion-clipboard"></i>新增至個人課程清單'
                        +'</a></li>'
                        +'<li><a data-toggle="modal" data-target="#addToCoursePlan" onclick="getUnitId('+videoId+')" style="cursor:pointer;" class=" waves-effect waves-block">'
                        +'<i class="fa fa-tasks"></i>新增至個人課程計畫'
                        +'</a></li>'
                        +'</ul>'
                        +'</div>'
                        +'<span class="pull-right">' 
                        +'<i class="fa fa-times" data-toggle="modal" data-target="#deleteModal"'
                        +'onclick="getUnitId('+videoId+')" style="cursor: pointer;"></i>'
                        +'</span>'
                        +'<a class="portlet-content" id="'+videoId+'" onclick="jumpToPlayerInterface('+ result[j].unitId + ',' + result[j].videoType + ',' + result[j].lastTime + ',' + result[j].groupId +')">'
                        +'<div class="info-card">'
                        +'<div class="embed-responsive embed-responsive-16by9">'
                        +'<img id="img" class="style-scope yt-img-shadow" alt="" style="width:100%;" src="'+result[j].videoImgSrc+'">' 
                        +'</div>'
                        +'<div class="info-card-details animate">'
                        +'<div class="info-card-header">'
                        +'<h3 class="unitNameTitle">'+ result[j].unitName +'</h3>'
                        +'<h4>'+ result[j].schoolName +'</h4>'
                        +'</div>'
                        +'<div class="info-card-detail">'
                        +'<h4>授課教師:'+result[j].teacher+'</h4>'
                        +'<h4>清單名稱:'+result[j].listName+'</h4>'
                        +'<h4>'+result[j].unitLikes+'人喜歡</h4>'
                        +'</div>'
                        +'</div>'
                        +'</div>'
                        +'</a>'
                        +'</li>');
            videoId++;
            unitArray[j] = new Array(2);
            unitArray[j][0] = result[j].unitId;
            unitArray[j][1] = result[j].courselistId;
            unitArray[j][2] = result[j].creator;
      }

      //將共同計畫中的影片新增至個人的指定清單
      $('#addToVideoListButton').click(function(){
            $.ajax({
                  url : ajaxURL+'AnyCourse/HomePageServlet.do',
                  method : 'POST',
                  cache: false,
                  data:{
                        action:'addToVideoList',
                        courselistId:$('#addToVideoListModalBody').val(),
                        unitId:unitArray[checkUnitId-1][0]
                  },
                  error:function(){
                        console.log("add video to courselist error");
                  }
            });
      });

      //將共同計畫中的影片新增至個人課程計畫
      $('#addToCoursePlanButton,#addToCoursePlanButtonClose').click(function(){
            $.ajax({
                  url : ajaxURL+'AnyCourse/HomePageServlet.do',
                  method : 'POST',
                  cache: false,
                  data:{
                        action:'addToCoursePlan',
                        unitId:unitArray[checkUnitId-1][0],
                        creator:unitArray[checkUnitId-1][2]
                  },
                  error:function(){
                        console.log("groupVideoList.js addToCoursePlan Error!");
                  }
            })
      });
}