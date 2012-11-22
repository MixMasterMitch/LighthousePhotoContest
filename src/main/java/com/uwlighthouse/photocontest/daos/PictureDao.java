package com.uwlighthouse.photocontest.daos;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.uwlighthouse.photocontest.daos.GenericDao;
import com.uwlighthouse.photocontest.databaseobjects.Picture;

public class PictureDao extends GenericDao<Picture, Integer> {
	public Picture findByImageKey(String imageKey) {
		@SuppressWarnings("unchecked")
		List<Picture> matchingPictures = getCriteria().add(Restrictions.eq("imageKey", imageKey)).list();
		if (matchingPictures.size() == 1) {
			return matchingPictures.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Picture> findByWeek(int week) {
		return getCriteria().add(Restrictions.eq("week", week)).list();
	}
}
