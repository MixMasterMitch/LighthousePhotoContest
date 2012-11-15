// These are the functions and neccessary to interact with Facebook
// See http://developers.facebook.com/docs/howtos/login/getting-started/
window.fbAsyncInit = function() {
	FB.init({
		appId      : '381115968629714', // App ID
		channelUrl : 'channel.html', // Channel File
		status     : true, // check login status
		cookie     : true, // enable cookies to allow the server to access the session
		xfbml      : true  // parse XFBML
	});
};
 
// Load the SDK Asynchronously
(function(d) {
	var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
	if (d.getElementById(id)) {
		return;
	}
	js = d.createElement('script'); 
	js.id = id; 
	js.async = true;
	js.src = "http://connect.facebook.net/en_US/all.js";
	ref.parentNode.insertBefore(js, ref);
}(document));

// First checks if the user is already logged in and authenticated
// If the user isn't, then the user is prompted to log in and authenticate
function getFbId(callback) {
	FB.getLoginStatus(function(response) {
		if (response.status === 'connected') {// logged in and authenticated
			callback(response.authResponse.userID);
 	  	} else {// not logged in and/or authenticated
 	  		//login and authenticate
  	  		FB.login(function(response) {
				if (response.authResponse) {
					callback(response.authResponse.userID);
				}
  	  		});
 	  	}
  	});
}