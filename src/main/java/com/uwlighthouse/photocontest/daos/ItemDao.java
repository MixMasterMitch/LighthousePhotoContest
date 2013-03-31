package com.uwlighthouse.photocontest.daos;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.uwlighthouse.photocontest.databaseobjects.Item;

public class ItemDao extends GenericDao<Item, Integer> {
	@SuppressWarnings("unchecked")
	public Item findByWeek(int week) {
		List<Item> weeksItemList = getCriteria().add(Restrictions.eq("week", week)).list();
		if (weeksItemList.size() > 0) {
			return weeksItemList.get(0);
		}
		return null;
	}
}
