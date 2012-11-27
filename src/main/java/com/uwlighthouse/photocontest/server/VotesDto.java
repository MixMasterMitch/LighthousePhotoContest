package com.uwlighthouse.photocontest.server;

public class VotesDto {
	private String name;
	private int votes;

	public VotesDto(String name, int votes) {
		super();
		this.name = name;
		this.votes = votes;
	}

	public VotesDto() {
		super();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getVotes() {
		return votes;
	}
	public void setVotes(int votes) {
		this.votes = votes;
	}
}
