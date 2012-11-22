package com.uwlighthouse.photocontest.daos;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.uwlighthouse.photocontest.databaseobjects.User;

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
