window.onload = function() {
	
	// Load pictures (To be done via AJAX in the future)
	loadPicture("pictures/berty_2.jpeg", "'Now we see but a poor reflection as in a mirror; then we shall see face to face.' 1 Corinthians 13:12");
	loadPicture("pictures/jacob_2.jpeg", "Even Garth finds wisdom in Proverbs.");
	loadPicture("pictures/eli_2.jpeg", "'The light shines in the darkness and the darkness can never extinguish it' John 1:5");
	loadPicture("pictures/mitch_2.jpeg", "'There's more, God's word warns us of danger and directs us to hidden treasure.' Psalm 19:11");
	loadPicture("pictures/tore_2.jpeg", "Man cannot live on waffles alone ...");
	
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
		}
		fbLogin(vote);
	})
}

// Updates the selectedPicture to be the picture that was just clicked.
function updateSelectedPicture() {
	$("#selectedPictureImg").attr("src", $(this).children()[0].src);
	$("#caption").text($(this).children()[0].alt);
	toggleSelectedPicture();
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

// Posts the given parameters to the given URL path using a hidden form.
// http://stackoverflow.com/questions/133925/javascript-post-request-like-a-form-submit
function postToUrl(path, params) {
	var hiddenForm = $("<form>", {method: "post", action: path});
	
    for(var key in params) {
        if(params.hasOwnProperty(key)) {
            $("<input>", {
            	type: "hidden",
            	name: "key",
            	value: params[key]
            }).appendTo(hiddenForm);
         }
    }

    hiddenForm.appendTo($("body"));
    hiddenForm.submit();
}

// Loads the given picture and injects it into the page
function loadPicture(src, caption) {
	$("<img>", {
		"src": src,
		"alt": caption
	}).load(function() {
		var picture = $("<li>", {
			"class": "picture"
		});
		$(this).appendTo(picture);
		picture.appendTo($("#pictures"));
		
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
		picture.click(updateSelectedPicture);
	});
}

// Submits the Facebook user's vote for the selected picture
function vote() {
	postToUrl("vote_submit.php", {"facebook_id": user.id, "picture_id": $("#selectedPictureImg").attr("src")});
}