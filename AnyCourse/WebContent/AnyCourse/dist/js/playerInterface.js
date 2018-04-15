
$('#noteArea').slimScroll({
    height: '150px'
  });
$('.tab-content').slimScroll({
    height: '300px'
  });
$('#keyLabel1').slimScroll({
    height: '130px'
  });
$('#keyLabel2').slimScroll({
    height: '130px'
  });
$('#recommend').slimScroll({
    height: '612px'
  });
$(function () {
    $('.list-group-item').on('mouseover', function(event) {
		event.preventDefault();
		$(this).closest('li').addClass('open');
	});
      $('.list-group-item').on('mouseout', function(event) {
    	event.preventDefault();
		$(this).closest('li').removeClass('open');
	});
});
$(document).ready(function(){
    $("#editNote").click(function(){
        $("#noteFooter").slideToggle();
    });
    $("#cancelNote").click(function(){
        $("#noteFooter").slideToggle();
    });

    $( function() {
    $( "#keyLabel1, #keyLabel2" ).sortable({
      connectWith: ".connectedSortable"
    }).disableSelection();
    });




    
});