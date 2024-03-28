package com.laptrinhjavaweb.dto;

public class CommentDTO extends AbstractDTO<CommentDTO>{
    private String content_comment;
    private Long userId; // Đây là id của người dùng tạo comment
    private Long newId;

    public Long getNewId() {
        return newId;
    }

    public void setNewId(Long newId) {
        this.newId = newId;
    }

    public String getContent_comment() {
        return content_comment;
    }

    public void setContent_comment(String content_comment) {
        this.content_comment = content_comment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}
