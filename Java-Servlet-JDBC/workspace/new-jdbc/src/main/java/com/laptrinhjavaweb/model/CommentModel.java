package com.laptrinhjavaweb.model;

public class CommentModel extends AbstractModel<CommentModel>{
	
	private String content;
	private Long userId;
	private Long newId;
	private UserModel user = new UserModel();
	private NewModel news = new NewModel();
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getNewId() {
		return newId;
	}
	public void setNewId(Long newId) {
		this.newId = newId;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public NewModel getNews() {
		return news;
	}

	public void setNews(NewModel news) {
		this.news = news;
	}
}
