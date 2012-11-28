var clickedPicture;

window.onload = function() {
	loadPictures();
	loadPreviousWinners();
	
	if (window.location.hash) {
		toLocation(window.location.hash);
	} else {
		toLocation("#pictures");
	}
	
	$("body").keydown(function(event) {
		if (event.keyCode == 39) {
			updateSelectedPicture(clickedPicture.next());
		}
		if (event.keyCode == 37) {
			updateSelectedPicture(clickedPicture.prev());
		}
 	});
	
	// Add event handlers
	$(".glass, #cancel").click(toggleSelectedPicture);
	$("#selectedPicture").hover(
		function() {
			$("#selectedPicture > *").not("#selectedPictureImg").css("opacity", 1);
		},
		function() {
			$("#selectedPicture > *").not("#selectedPictureImg").css("opacity", 0);
		}
	);
	$("#voteButton").click(function() {
		if (user) {
			vote();
		} else {
			fbLogin(vote);
		}
	});
	$("#login").click(fbLogin);
	$("#uploadForm form").ajaxForm(function(response) {
		hideAll();
		if (response === 'upload_success') {
			toLocation("#thanksForUploading");
		} else {
			toLocation("#uploadFailed");
		}
	})
	$("#user").hover(showUserOptions, hideUserOptions);
	$("#userOptions").hover(showUserOptions, hideUserOptions);
	$("#upload").click(function() {
		$("#uploadForm form p.name").html(user.name);
		$("#nameField").attr("value", user.name);
		$("#idField").attr("value", user.id);
		toLocation("#uploadForm");
	});
	$("#viewStandings").click(function() {
		toLocation("#standings");
	});
	$("#viewPreviousWinners").click(function() {
		toLocation("#previousWinners");
	});
	
}

// Hides the userOptions element
function hideUserOptions() {
	$("#userOptions").addClass("hidden");
	$("#user").removeClass("userHover");
}

// Shows the userOptions element
function showUserOptions() {
	$("#userOptions").removeClass("hidden");
	$("#user").addClass("userHover");
}

//Updates the selectedPicture to be the picture that was just clicked.
function updateSelectedPicture(picture) {
	$("#selectedPictureImg").attr("src", picture.children()[0].src);
	$("#caption").text(picture.children()[0].alt);
	clickedPicture = picture;
}

// Toggles the selected picture
function toggleSelectedPicture() {
	$("#selectedPicture").toggle();
	$(".glass").toggle();
}

// Rotates the given element cw or ccw by a random amount up to the given maximum.
function rotateElement(element, maxRotation) {
	var rotation = Math.floor(Math.random() * 2 * maxRotation - maxRotation + 1);
	element.css("-webkit-transform", "rotate(" + rotation + "deg) translate3d( 0, 0, 0)");
	element.css("-moz-transform", "rotate(" + rotation + "deg) translate3d( 0, 0, 0)");
	element.css("transform", "rotate(" + rotation + "deg) translate3d( 0, 0, 0)");
}

// Removes any rotation on the given element.
function straightenElement(element) {
	element.css("-webkit-transform", "rotate(0deg) translate3d( 0, 0, 0)");
	element.css("-moz-transform", "rotate(0deg) translate3d( 0, 0, 0)");
	element.css("transform", "rotate(0deg) translate3d( 0, 0, 0)");
}

// Moves the given element to the left or right by a random amount up to the given maximum.
function shiftElement(element, maxShift) {
	element.css("margin-left", Math.floor(Math.random() * 2 * maxShift - maxShift + 1));
}

// Scales the given element by the given scale factor.
function scaleElement(element, scaleFactor) {
	element.css("-webkit-transform", "scale(" + scaleFactor + "," + scaleFactor + ")");
	element.css("-moz-transform", "scale(" + scaleFactor + "," + scaleFactor + ")");
	element.css("transform", "scale(" + scaleFactor + "," + scaleFactor + ")");
}

// Increases the z-index of the given element by the given number of indexes.
function bringForward(element, zIndexes) {
	element.css("z-index", Math.max(0, zIndexes + parseInt(element.css("z-index"))));
}

function loadPicture(element, src, caption, photographer) {
	$("<img>", {
		"src": src,
		"alt": caption
	}).load(function() {
		var picture = $("<li>", {
			"class": "picture"
		});
		$(this).appendTo(picture);
		if (photographer) {
			$("<p>", {text: photographer}).appendTo(picture);
		}
		picture.appendTo($(element));
		
		// Randomly shift and rotate pictures
		rotateElement(picture, 4);
		shiftElement(picture, 7);
		picture.hover(
			function() {
				scaleElement($(this), 1.2);
				bringForward($(this), 100);
			}, function() {
				rotateElement($(this), 4);
				bringForward($(this), -100);
			}
		);
		picture.click(function() {
			updateSelectedPicture($(this));
			toggleSelectedPicture();
		})
	});
}

// Submits the Facebook user's vote for the selected picture
function vote() {
	$.post("servlets/VoteServlet", 
		{
			facebook_id: user.id, 
			image: $("#selectedPictureImg").attr("src"), 
			name: user.name
		}, function(response) {
			hideAll();
			if (response === 'already_voted') {
				toLocation("#alreadyVoted");
			} else {
				toLocation("#thanksForVoting");
			}
		}
	);
}

// Hides ever element except the header element
function hideAll() {
	$("body > *").not("#header").hide();
}

function loadPictures() {
	$.get('servlets/PictureServlet', {dataType: "application/json"}, function(data) {
		$("#pictures").empty();
		$.each(data, function(index, value) {
			loadPicture("#pictures", value.url, value.caption);
		});
	});
}

function loadPreviousWinners() {
	$.get('servlets/PictureServlet', {previousWinners: "true", dataType: "application/json"}, function(data) {
		$("#previousWinners").empty();
		$.each(data, function(index, value) {
			loadPicture("#previousWinners", value.url, value.caption, value.photographer);
		});
	});
}

// Displays the given element and adds it to the browser history.
// The given element should represent the id (with #) of and element in the HTML.
function toLocation(element) {
	hideAll();
	hideUserOptions();
	$(element).show();
	history.pushState(null, null, element);
	window.addEventListener("popstate", function(e) {
		hideAll();
		hideUserOptions();
		$(window.location.hash).show();
	});
}