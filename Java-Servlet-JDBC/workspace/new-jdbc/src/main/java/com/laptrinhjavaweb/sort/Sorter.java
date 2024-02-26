package com.laptrinhjavaweb.sort;

public class Sorter {

	private String SortName;
	private String SortBy;
	
	public Sorter(String SortName, String SortBy) {
		this.setSortName(SortName);
		this.setSortBy(SortBy);
	}

	public String getSortName() {
		return SortName;
	}

	public void setSortName(String sortName) {
		SortName = sortName;
	}

	public String getSortBy() {
		return SortBy;
	}

	public void setSortBy(String sortBy) {
		SortBy = sortBy;
	}
}
