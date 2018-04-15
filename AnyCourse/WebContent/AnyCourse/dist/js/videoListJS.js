$('#unit').slimScroll({
    height: '400px;'
  });
$('#list').slimScroll({
    height: '400px;'
  });
var checkID;
$(document).ready(function() {
  $('#addModal').on('shown.bs.modal', function () {
    $('#named').focus();
  });

  $('#editModal').on('shown.bs.modal', function () {
    $('#edited').focus();
  });

  $(".todo-list").sortable({
    placeholder: "sort-highlight",
    handle: ".handle",
    forcePlaceholderSize: true,
    zIndex: 999999
	});
  
  var videoListID = 0;
  $("#addListButton").click(function(){
    $('#videoListUL').append('<li id = "videoListID_'+videoListID+'"'+'>'
                            +'<span class="handle ui-sortable-handle">'
                            +'<i class="fa fa-ellipsis-v">'
                            +'</i><i class="fa fa-ellipsis-v"></i>'
                            +'</span>'
                            +'<span class="text" id="videoListText_'+videoListID+'"'+'>'+$("#named").val()+'</span>'
                            +'<div class="tools">'
                            +'<button type="button" data-toggle="tooltip" data-placement="top" title="新增至..."><i class="fa fa-plus"></i></button>'
                            +'<button type="button" data-toggle="tooltip" data-placement="top" title="分享至..."><i class="fa fa-share-square-o"></i></button>'
                            +'<button type="button" data-toggle="modal" data-target="#editModal" onclick="getID('+videoListID+')"><i class="fa fa-edit"  data-toggle="tooltip" data-placement="top" title="編輯"></i></button>'
                            +'<button type="button" data-toggle="modal" data-target="#deleteModal1" onclick="getID('+videoListID+')"><i class="fa fa-trash-o" data-toggle="tooltip" data-placement="top" title="刪除"></i></button>'
                            +'</div>'
                            +'</li>');
    $('#named').val("");
    videoListID++;
  });
  
  // $('#videoListUL').on("click",".fa.fa-trash-o", function(e){
  //   e.preventDefault(); $(.fa.fa-trash-o).parent().parent().parent('li').remove();
  // })

  //編輯list名稱
  $("#editListButton").click(function(e){
    var newName = $("#edited").val();
    e.preventDefault();
    $("#videoListText_"+checkID).text(newName);
    $('#edited').val("");
  });

  //刪list的item
  $("#deleteListButton1").click(function(e){
    e.preventDefault();
    $("#videoListID_"+checkID).remove();
  });
  //刪除unitVideo
  $("#deleteListButton2").click(function(e){
    e.preventDefault();
    $("#videoItem_"+checkID).remove();
  });


});
function getID(id){
    checkID = id;
  }