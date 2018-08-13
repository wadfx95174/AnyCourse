//var ajaxURL="http://140.121.197.131:7603/";
var ajaxURL="http://localhost:8080/";

$(function(){
	checkLogin("../", "../../../");
}) 

$('#submit').click(function(){
	console.log('submit');
	$('#group-form').attr('action', ajaxURL + 'AnyCourse/ManagementServlet.do');
})