package com.uwlighthouse.photocontest.daos;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.uwlighthouse.photocontest.databaseobjects.User;
import com.uwlighthouse.photocontest.databaseobjects.Vote;

public class VoteDao extends GenericDao<Vote, Integer> {
	public Vote findByUserAndWeek(User user, int week) {
		@SuppressWarnings("unchecked")
		List<Vote> matchingVotes = getCriteria()
		.add(Restrictions.eq("user", user))
		.createCriteria("picture")
		.add(Restrictions.eq("week", week))
		.list();
		if (matchingVotes.size() == 1) {
			return matchingVotes.get(0);
		} else {
			return null;
		}
	}
}
