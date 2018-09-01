$(function(){
	checkLogin("../", "../../../");
}) 

$('#submit').click(function(){
	console.log('submit');
	$('#group-form').attr('action', ajaxURL + 'AnyCourse/ManagementServlet.do');
})