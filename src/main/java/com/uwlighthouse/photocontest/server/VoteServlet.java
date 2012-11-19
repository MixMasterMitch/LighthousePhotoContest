package main.java.com.uwlighthouse.photocontest.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VoteServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7173979101288610134L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println(request.getParameterMap());
		System.out.println("fbid: " + request.getParameter("facebook_id"));
		System.out.println("photoid: " + request.getParameter("photo_id"));
	}
	
}
