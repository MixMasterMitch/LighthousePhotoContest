package main.java.com.uwlighthouse.photocontest.daos;

import java.util.List;

import main.java.com.uwlighthouse.photocontest.databaseobjects.Picture;
import main.java.com.uwlighthouse.photocontest.databaseobjects.User;

import org.hibernate.criterion.Restrictions;

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
}
