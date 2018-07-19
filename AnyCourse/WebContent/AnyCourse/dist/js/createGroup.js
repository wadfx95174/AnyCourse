//var ajaxURL="http://140.121.197.130:8400/";
var ajaxURL="http://localhost:8080/";

$(function(){
	checkLogin("../", "../../../");
}) 

$('#submit').click(function(){
	console.log('submit');
	$('#group-form').attr('action', ajaxURL + 'AnyCourse/ManagementServlet.do');
})