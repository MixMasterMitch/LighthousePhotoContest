package com.uwlighthouse.photocontest.server;

import javax.servlet.http.HttpServlet;

public class VoteServlet extends HttpServlet {
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		getParameterValues("facebook_id");
		getParameterValues("photo_id");
		
	}
	
}
