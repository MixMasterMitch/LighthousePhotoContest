// These are the functions and needed to interact with Facebook
// See http://developers.facebook.com/docs/howtos/login/getting-started/

//Contains data about the user. Undefined until the user logs in.
var user;
			
window.fbAsyncInit = function() {
	var fbInit = {
		appId      : '381115968629714', // App ID
		channelUrl : 'channel.html', // Channel File
		status     : true, // check login status
		cookie     : true, // enable cookies to allow the server to access the session
		xfbml      : true  // parse XFBML
	};
	
	// For testing
	if (window.location.hostname === "localhost") {
		fbInit.appId = '451771748204012';
	}
	
	FB.init(fbInit);
	
	//Automatically logs the user in if possible
	FB.getLoginStatus(
		function(response) {
			console.log("got user status");
			if(response.status === 'connected') {
				loadFbUserData(response.authResponse.userID);
			}
		}
	);
};
 
// Load the Facebook JS SDK
(function () {
	$("<script>", {
		id: "facebook-jssdk",
		src: "http://connect.facebook.net/en_US/all.js"
	}).insertBefore($("script").first());
})();
console.log("fb loading");

// Forces the user to log into Facebook and then calls the 
// callback method when the user profile has been loaded.
function fbLogin(callback) {
	FB.login(function(response) {
		if (response.authResponse) {
			loadFbUserData(response.authResponse.userID, callback);
		}
	});
}

// Loads the user profile with the given id and calls callback when finished.
function loadFbUserData(id, callback) {
	$.ajax({
		url: "http://graph.facebook.com/" + id + "?fields=name",
		dataType: "JSON"
	}).done(function(userData) {
		console.log("got user data");
		user = userData;
		$("#login").toggle();
		$("#user").html(user.name);
		$("#user").toggle();
		if (callback) {
			callback();
		}
	});
}

