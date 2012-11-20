package main.java.com.uwlighthouse.photocontest.daos;

import java.util.List;

import main.java.com.uwlighthouse.photocontest.databaseobjects.User;

import org.hibernate.criterion.Restrictions;

public class UserDao extends GenericDao<User, Integer> {
	public User findByFacebookId(String facebookId) {
		@SuppressWarnings("unchecked")
		List<User> matchingUsers = getCriteria().add(Restrictions.eq("facebookId", facebookId)).list();
		if (matchingUsers.size() == 1) {
			return matchingUsers.get(0);
		} else {
			return null;
		}
	}
}
