package com.uwlighthouse.photocontest.daos;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.uwlighthouse.photocontest.databaseobjects.Item;

public class ItemDao extends GenericDao<Item, Integer> {
	@SuppressWarnings("unchecked")
	public List<Item> findByWeek(int week) {
		return getCriteria().add(Restrictions.eq("week", week)).list();
	}
}
