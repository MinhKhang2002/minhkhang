package com.laptrinhjavaweb.entity;

import javax.persistence.*;

@Entity
@Table(name = "comment")
public class CommentEntity extends BaseEntity{
    @Column(name = "content_comment")
    private String content_comment;

    public String getContent_comment() {
        return content_comment;
    }

    public void setContent_comment(String content_comment) {
        this.content_comment = content_comment;
    }
    @ManyToOne
    @JoinColumn(name = "new_id")
    private NewEntity news;

    public NewEntity getNews() {
        return news;
    }

    public void setNews(NewEntity news) {
        this.news = news;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // Constructors, getters, and setters

    // User
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
