package com.uwlighthouse.photocontest.databaseobjects;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	private Integer id;
	private String facebookId;
	private String name;
	private Set<Picture> picture;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "fb_id", nullable = false, length = 32)
	public String getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	@Column(name = "name", nullable = false, length = 255)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = LAZY, mappedBy = "user")
	public Set<Picture> getPicture() {
		return picture;
	}
	public void setPicture(Set<Picture> picture) {
		this.picture = picture;
	}
}
