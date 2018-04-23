var youTubePlayer;
var uid;

$(document).ready(function(){
	$(".show-on-hover").hover(            
        function() {
            $('.dropdown-menu', this).not('.in .dropdown-menu').stop( true, true ).slideDown("fast");
            $(this).toggleClass('open');        
        },
        function() {
            $('.dropdown-menu', this).not('.in .dropdown-menu').stop( true, true ).slideUp("fast");
            $(this).toggleClass('open');       
        }
    );
	
	
	$.ajax({
		url : 'http://localhost:8080/AnyCourse/VideoListServlet.do',
		method : 'GET',
		data:{
			"action":selectList//代表要selectList
		},
		success:function(result){
			videoListArray = new Array(result.length);
	  		for(var i = 0 ;i < result.length;i++){
	  			$('#videoListUL').append('<li id = "videoListID_'+videoListID+'" onclick="getID('+videoListID+')">'
	                    +'<span class="handle ui-sortable-handle">'
	                    +'<i class="fa fa-ellipsis-v">'
	                    +'</i><i class="fa fa-ellipsis-v"></i>'
	                    +'</span>'
	                    +'<span class="text" id="videoListText_'+videoListID+'">'+result[i].list_name+'</span>'
	                    +'<div class="tools">'
	                    +'<button type="button" data-toggle="tooltip" data-placement="top" title="新增至..."><i class="fa fa-plus"></i></button>'
	                    +'<button type="button" data-toggle="tooltip" data-placement="top" title="分享至..."><i class="fa fa-share-square-o"></i></button>'
	                    +'<button type="button" data-toggle="modal" data-target="#editModal" onclick="getID('+videoListID+')"><i class="fa fa-edit"  data-toggle="tooltip" data-placement="top" title="編輯"></i></button>'
	                    +'<button type="button" data-toggle="modal" data-target="#deleteModal1" onclick="getID('+videoListID+')"><i class="fa fa-trash-o" data-toggle="tooltip" data-placement="top" title="刪除"></i></button>'
	                    +'</div>'
	                    +'</li>');
	  			//把modal設為空
				  $('#named').val("");
				  
				  //點擊清單，顯示單元影片
				  $("#videoListID_"+videoListID).on("click" , function(){
					  
					  $.ajax({
							url : 'http://localhost:8080/AnyCourse/VideoListServlet.do',
							method : 'GET',
						    data : {
						    	"action" : selectUnit,//代表要selectUnit
						    	"school_name" : videoListArray[checkID-1][5],
						    	"list_name" : videoListArray[checkID-1][1]
							},
							success:function(resultUnit){
								//清除原先檢視的unit
								$('#unit li').each(function(){
								    $(this).remove();
								}); 
								unitArray = new Array(resultUnit.length);
								for(var k = 0 ;k < resultUnit.length;k++){
									console.log(resultUnit[k].videoType);
									$("#unit").append(
											'<li id="videoItem_'+unitVideoID+'">'
											+'<span class="handle ui-sortable-handle">' 
											+'<i class="fa fa-ellipsis-h"></i>'
											+'</span>' 
											+'<span class="pull-right">'
											+'<i class="fa fa-times" data-toggle="modal" data-target="#deleteModal2"'
											+'onclick="getID('+unitVideoID+')" style="cursor: pointer;"></i>'
											+'</span>'
											+'<a class="list-group-item" onclick="jumpToPlayerInterface('+ resultUnit[k].unit_id + ',' + resultUnit[k].videoType+')">'
											+'<div class="media">'
											+'<div class="col-xs-4 pull-left" style="padding-left: 0px;">'
											+'<div class="embed-responsive embed-responsive-16by9">'
											+'<img id="img" class="style-scope yt-img-shadow" alt="" width="230"'
											+'src="' + resultUnit[k].video_img_src + '">' 
											+'</div>'
											+'</div>'
											+'<div class="media-body">'
											+'<h4 class="media-heading">'
											+'<b>影片名稱:' + resultUnit[k].unit_name + '</b>'
											+'</h4>'
											+'<p style="margin-bottom: 5px;">開課大學:' + resultUnit[k].school_name + '</p>'
											+'<p style="margin-bottom: 5px;">授課教師:' + resultUnit[k].teacher + '老師</p>'
											+'<p style="margin-bottom: 5px;">課程簡介:' + resultUnit[k].course_info + '</p>'
											+'<p style="margin-bottom: 5px;">讚數:' + resultUnit[k].likes.toLocaleString() +'</p>'
											+'</div>'
											+'</div>'
											+'</a></li>'
									);
									unitVideoID++;
									unitArray[k] = new Array(3);
								}
//								for(var i = 0 ;i < result.length;i++){
////						  			console.log(result[i].user_id);
////						  			console.log(result[i].creator);
//						  			for(var j = 0 ; j < 5;j++){
//						  				if(j == 0)videoListArray[i][j] = result[i].courselist_id;
//						  				else if(j == 1)videoListArray[i][j] = result[i].list_name;
//						  				else if(j == 2)videoListArray[i][j] = result[i].user_id;
//						  				else if(j == 3)videoListArray[i][j] = result[i].creator;
//						  				else videoListArray[i][j] = result[i].oorder;
////						  				console.log(videoListArray[i][j]);
//						  			}
//								}
								
					  			
								
					    	},
							error:function(){alert('failed');}
						});
				  });
				  videoListID++;
//				  console.log("123");
	  			videoListArray[i] = new Array(6);
			}
//	  		console.log(result.length);
	  		for(var i = 0 ;i < result.length;i++){
//	  			console.log(result[i].user_id);
//	  			console.log(result[i].creator);
	  			for(var j = 0 ; j < 6;j++){
	  				if(j == 0)videoListArray[i][j] = result[i].courselist_id;
	  				else if(j == 1)videoListArray[i][j] = result[i].list_name;
	  				else if(j == 2)videoListArray[i][j] = result[i].user_id;
	  				else if(j == 3)videoListArray[i][j] = result[i].creator;
	  				else if(j == 4)videoListArray[i][j] = result[i].oorder;
	  				else videoListArray[i][j] = result[i].school_name;
//	  				console.log(videoListArray[i][j]);
	  			}
	  		}
  	},
		error:function(){alert('failed');}
	});
 }());
	
function jumpToPlayerInterface(unit_id,type){
    url = "pages/PlayerInterface.html?unit_id="+unit_id+"&type="+type;//此處拼接內容
    window.location.href = url;
}

















var SETTINGS = {
	    navBarTravelling: false,
	    navBarTravelDirection: "",
	     navBarTravelDistance: 150
	}

	var colours = {
	    0: "#867100",
	    1: "#7F4200",
	    2: "#99813D",
	    3: "#40FEFF",
	    4: "#14CC99",
	    5: "#00BAFF",
	    6: "#0082B2",
	    7: "#B25D7A",
	    8: "#00FF17",
	    9: "#006B49",
	    10: "#00B27A",
	    11: "#996B3D",
	    12: "#CC7014",
	    13: "#40FF8C",
	    14: "#FF3400",
	    15: "#ECBB5E",
	    16: "#ECBB0C",
	    17: "#B9D912",
	    18: "#253A93",
	    19: "#125FB9",
	}

	document.documentElement.classList.remove("no-js");
	document.documentElement.classList.add("js");

	// Out advancer buttons
	var pnAdvancerLeft = document.getElementById("pnAdvancerLeft");
	var pnAdvancerRight = document.getElementById("pnAdvancerRight");
	// the indicator
	var pnIndicator = document.getElementById("pnIndicator");

	var pnProductNav = document.getElementById("pnProductNav");
	var pnProductNavContents = document.getElementById("pnProductNavContents");

	pnProductNav.setAttribute("data-overflowing", determineOverflow(pnProductNavContents, pnProductNav));

	// Set the indicator
	moveIndicator(pnProductNav.querySelector("[aria-selected=\"true\"]"), colours[0]);

	// Handle the scroll of the horizontal container
	var last_known_scroll_position = 0;
	var ticking = false;

	function doSomething(scroll_pos) {
	    pnProductNav.setAttribute("data-overflowing", determineOverflow(pnProductNavContents, pnProductNav));
	}

	pnProductNav.addEventListener("scroll", function() {
	    last_known_scroll_position = window.scrollY;
	    if (!ticking) {
	        window.requestAnimationFrame(function() {
	            doSomething(last_known_scroll_position);
	            ticking = false;
	        });
	    }
	    ticking = true;
	});


	pnAdvancerLeft.addEventListener("click", function() {
	    // If in the middle of a move return
	    if (SETTINGS.navBarTravelling === true) {
	        return;
	    }
	    // If we have content overflowing both sides or on the left
	    if (determineOverflow(pnProductNavContents, pnProductNav) === "left" || determineOverflow(pnProductNavContents, pnProductNav) === "both") {
	        // Find how far this panel has been scrolled
	        var availableScrollLeft = pnProductNav.scrollLeft;
	        // If the space available is less than two lots of our desired distance, just move the whole amount
	        // otherwise, move by the amount in the settings
	        if (availableScrollLeft < SETTINGS.navBarTravelDistance * 2) {
	            pnProductNavContents.style.transform = "translateX(" + availableScrollLeft + "px)";
	        } else {
	            pnProductNavContents.style.transform = "translateX(" + SETTINGS.navBarTravelDistance + "px)";
	        }
	        // We do want a transition (this is set in CSS) when moving so remove the class that would prevent that
	        pnProductNavContents.classList.remove("pn-ProductNav_Contents-no-transition");
	        // Update our settings
	        SETTINGS.navBarTravelDirection = "left";
	        SETTINGS.navBarTravelling = true;
	    }
	    // Now update the attribute in the DOM
	    pnProductNav.setAttribute("data-overflowing", determineOverflow(pnProductNavContents, pnProductNav));
	});

	pnAdvancerRight.addEventListener("click", function() {
	    // If in the middle of a move return
	    if (SETTINGS.navBarTravelling === true) {
	        return;
	    }
	    // If we have content overflowing both sides or on the right
	    if (determineOverflow(pnProductNavContents, pnProductNav) === "right" || determineOverflow(pnProductNavContents, pnProductNav) === "both") {
	        // Get the right edge of the container and content
	        var navBarRightEdge = pnProductNavContents.getBoundingClientRect().right;
	        var navBarScrollerRightEdge = pnProductNav.getBoundingClientRect().right;
	        // Now we know how much space we have available to scroll
	        var availableScrollRight = Math.floor(navBarRightEdge - navBarScrollerRightEdge);
	        // If the space available is less than two lots of our desired distance, just move the whole amount
	        // otherwise, move by the amount in the settings
	        if (availableScrollRight < SETTINGS.navBarTravelDistance * 2) {
	            pnProductNavContents.style.transform = "translateX(-" + availableScrollRight + "px)";
	        } else {
	            pnProductNavContents.style.transform = "translateX(-" + SETTINGS.navBarTravelDistance + "px)";
	        }
	        // We do want a transition (this is set in CSS) when moving so remove the class that would prevent that
	        pnProductNavContents.classList.remove("pn-ProductNav_Contents-no-transition");
	        // Update our settings
	        SETTINGS.navBarTravelDirection = "right";
	        SETTINGS.navBarTravelling = true;
	    }
	    // Now update the attribute in the DOM
	    pnProductNav.setAttribute("data-overflowing", determineOverflow(pnProductNavContents, pnProductNav));
	});

	pnProductNavContents.addEventListener(
	    "transitionend",
	    function() {
	        // get the value of the transform, apply that to the current scroll position (so get the scroll pos first) and then remove the transform
	        var styleOfTransform = window.getComputedStyle(pnProductNavContents, null);
	        var tr = styleOfTransform.getPropertyValue("-webkit-transform") || styleOfTransform.getPropertyValue("transform");
	        // If there is no transition we want to default to 0 and not null
	        var amount = Math.abs(parseInt(tr.split(",")[4]) || 0);
	        pnProductNavContents.style.transform = "none";
	        pnProductNavContents.classList.add("pn-ProductNav_Contents-no-transition");
	        // Now lets set the scroll position
	        if (SETTINGS.navBarTravelDirection === "left") {
	            pnProductNav.scrollLeft = pnProductNav.scrollLeft - amount;
	        } else {
	            pnProductNav.scrollLeft = pnProductNav.scrollLeft + amount;
	        }
	        SETTINGS.navBarTravelling = false;
	    },
	    false
	);

	// Handle setting the currently active link
	pnProductNavContents.addEventListener("click", function(e) {
	    var links = [].slice.call(document.querySelectorAll(".pn-ProductNav_Link"));
	    links.forEach(function(item) {
	        item.setAttribute("aria-selected", "false");
	    })
	    e.target.setAttribute("aria-selected", "true");
	    // Pass the clicked item and it's colour to the move indicator function
	    moveIndicator(e.target, colours[links.indexOf(e.target)]);
	});

	// var count = 0;
	function moveIndicator(item, color) {
	    var textPosition = item.getBoundingClientRect();
	    var container = pnProductNavContents.getBoundingClientRect().left;
	    var distance = textPosition.left - container;
	     var scroll = pnProductNavContents.scrollLeft;
	    pnIndicator.style.transform = "translateX(" + (distance + scroll) + "px) scaleX(" + textPosition.width * 0.01 + ")";
	    // count = count += 100;
	    // pnIndicator.style.transform = "translateX(" + count + "px)";
	    
	    if (color) {
	        pnIndicator.style.backgroundColor = color;
	    }
	}

	function determineOverflow(content, container) {
	    var containerMetrics = container.getBoundingClientRect();
	    var containerMetricsRight = Math.floor(containerMetrics.right);
	    var containerMetricsLeft = Math.floor(containerMetrics.left);
	    var contentMetrics = content.getBoundingClientRect();
	    var contentMetricsRight = Math.floor(contentMetrics.right);
	    var contentMetricsLeft = Math.floor(contentMetrics.left);
	     if (containerMetricsLeft > contentMetricsLeft && containerMetricsRight < contentMetricsRight) {
	        return "both";
	    } else if (contentMetricsLeft < containerMetricsLeft) {
	        return "left";
	    } else if (contentMetricsRight > containerMetricsRight) {
	        return "right";
	    } else {
	        return "none";
	    }
	}

	/**
	 * @fileoverview dragscroll - scroll area by dragging
	 * @version 0.0.8
	 * 
	 * @license MIT, see https://github.com/asvd/dragscroll
	 * @copyright 2015 asvd <heliosframework@gmail.com> 
	 */


	(function (root, factory) {
	    if (typeof define === 'function' && define.amd) {
	        define(['exports'], factory);
	    } else if (typeof exports !== 'undefined') {
	        factory(exports);
	    } else {
	        factory((root.dragscroll = {}));
	    }
	}(this, function (exports) {
	    var _window = window;
	    var _document = document;
	    var mousemove = 'mousemove';
	    var mouseup = 'mouseup';
	    var mousedown = 'mousedown';
	    var EventListener = 'EventListener';
	    var addEventListener = 'add'+EventListener;
	    var removeEventListener = 'remove'+EventListener;
	    var newScrollX, newScrollY;

	    var dragged = [];
	    var reset = function(i, el) {
	        for (i = 0; i < dragged.length;) {
	            el = dragged[i++];
	            el = el.container || el;
	            el[removeEventListener](mousedown, el.md, 0);
	            _window[removeEventListener](mouseup, el.mu, 0);
	            _window[removeEventListener](mousemove, el.mm, 0);
	        }

	        // cloning into array since HTMLCollection is updated dynamically
	        dragged = [].slice.call(_document.getElementsByClassName('dragscroll'));
	        for (i = 0; i < dragged.length;) {
	            (function(el, lastClientX, lastClientY, pushed, scroller, cont){
	                (cont = el.container || el)[addEventListener](
	                    mousedown,
	                    cont.md = function(e) {
	                        if (!el.hasAttribute('nochilddrag') ||
	                            _document.elementFromPoint(
	                                e.pageX, e.pageY
	                            ) == cont
	                        ) {
	                            pushed = 1;
	                            lastClientX = e.clientX;
	                            lastClientY = e.clientY;

	                            e.preventDefault();
	                        }
	                    }, 0
	                );

	                _window[addEventListener](
	                    mouseup, cont.mu = function() {pushed = 0;}, 0
	                );

	                _window[addEventListener](
	                    mousemove,
	                    cont.mm = function(e) {
	                        if (pushed) {
	                            (scroller = el.scroller||el).scrollLeft -=
	                                newScrollX = (- lastClientX + (lastClientX=e.clientX));
	                            scroller.scrollTop -=
	                                newScrollY = (- lastClientY + (lastClientY=e.clientY));
	                            if (el == _document.body) {
	                                (scroller = _document.documentElement).scrollLeft -= newScrollX;
	                                scroller.scrollTop -= newScrollY;
	                            }
	                        }
	                    }, 0
	                );
	             })(dragged[i++]);
	        }
	    }

	      
	    if (_document.readyState == 'complete') {
	        reset();
	    } else {
	        _window[addEventListener]('load', reset, 0);
	    }

	    exports.reset = reset;
	}));