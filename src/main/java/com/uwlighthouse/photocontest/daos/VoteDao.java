package main.java.com.uwlighthouse.photocontest.daos;

import java.util.List;

import main.java.com.uwlighthouse.photocontest.databaseobjects.User;
import main.java.com.uwlighthouse.photocontest.databaseobjects.Vote;

import org.hibernate.criterion.Restrictions;

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
