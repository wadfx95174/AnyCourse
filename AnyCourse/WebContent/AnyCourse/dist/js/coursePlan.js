$('#unit').slimScroll({
    height: '420px;'
    });
$('.box-body').slimScroll({
	  height: '420px;'
	  });
$( ".column" ).sortable({
    connectWith: ".column",
    handle: ".portlet-header",
    cancel: ".portlet-toggle",
    placeholder: "portlet-placeholder ui-corner-all"
  });
$(document).ready(function() {
	  checkLogin("../", "../../../");
});