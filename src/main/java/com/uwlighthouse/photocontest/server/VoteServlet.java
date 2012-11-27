package com.uwlighthouse.photocontest.server;

import static com.google.common.collect.Lists.newArrayList;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.uwlighthouse.photocontest.daos.PictureDao;
import com.uwlighthouse.photocontest.daos.UserDao;
import com.uwlighthouse.photocontest.daos.VoteDao;
import com.uwlighthouse.photocontest.databaseobjects.Picture;
import com.uwlighthouse.photocontest.databaseobjects.User;
import com.uwlighthouse.photocontest.databaseobjects.Vote;

public class VoteServlet extends HttpServlet {

	private static final long serialVersionUID = 7173979101288610134L;

	/**
	 * Stores a vote in RDS. Existing votes will not be overwritten.
	 * 
	 * The given request should contain facebook_id, name, and image parameters.
	 * 
	 * "vote_success" will be responded if the vote was cast, and "already_voted" if a vote for the user already existed.
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Find if the user already exists
		User user = new UserDao().findByFacebookId(request.getParameter("facebook_id"));

		// If not then create a new user
		if (user == null) {
			User newUser = new User();
			newUser.setFacebookId(request.getParameter("facebook_id"));
			newUser.setName(request.getParameter("name"));
			new UserDao().makePersistent(newUser);
			user = newUser;
		}

		// Find if the user already voted
		Vote vote = new VoteDao().findByUserAndWeek(user, PictureServlet.getCurrentWeekNumber());

		// If not then cast a vote
		if (vote == null) {
			Vote newVote = new Vote();
			newVote.setUser(user);
			String imageUrl = request.getParameter("image");
			String imageKey = imageUrl.substring(imageUrl.lastIndexOf("/week") + 1);
			newVote.setPicture(new PictureDao().findByImageKey(imageKey));
			newVote.setVoteTime(new Date());
			new VoteDao().makePersistent(newVote);
			response.getWriter().append("vote_success");
		} else {
			response.getWriter().append("already_voted");
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<VotesDto> votes = newArrayList();
		for (Picture picture : new PictureDao().findByWeek(PictureServlet.getCurrentWeekNumber())) {
			votes.add(new VotesDto(picture.getUser().getName(), picture.getVotes().size()));
		}

		// Convert to JSON
		response.setContentType("application/json");
		response.getWriter().print(new Gson().toJson(votes));
	}

}
