package com.laptrinhjavaweb.model;

import java.sql.Timestamp;

public class ChartModel extends AbstractModel<ChartModel>{
	private Timestamp date;
	private Timestamp dateNews;
	private int numberOfPosts;
	private int numberOfComments;

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public int getNumberOfPosts() {
		return numberOfPosts;
	}

	public void setNumberOfPosts(int numberOfPosts) {
		this.numberOfPosts = numberOfPosts;
	}

	public int getNumberOfComments() {
		return numberOfComments;
	}

	public void setNumberOfComments(int numberOfComments) {
		this.numberOfComments = numberOfComments;
	}

	public Timestamp getDateNews() {
		return dateNews;
	}

	public void setDateNews(Timestamp dateNews) {
		this.dateNews = dateNews;
	}
}
