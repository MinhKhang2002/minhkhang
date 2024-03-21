package com.laptrinhjavaweb.dto;

public class ImageDTO extends AbstractDTO<ImageDTO>{
    private String thumbnail;
    private Long newId;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Long getNewId() {
        return newId;
    }

    public void setNewId(Long newId) {
        this.newId = newId;
    }
}
