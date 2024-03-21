package com.laptrinhjavaweb.dto;

import com.laptrinhjavaweb.entity.ImageEntity;

import java.util.ArrayList;
import java.util.List;

public class NewDTO extends AbstractDTO<NewDTO> {
	
	private String title;
	private String content;
	private String shortDescription;
	private String categoryCode;
	private String thumbnail;
	private String role;
	private Integer status;
	private List<ImageEntity> images = new ArrayList<>();
//	private String categories;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<ImageEntity> getImages() {
		return images;
	}

	public void setImages(List<ImageEntity> images) {
		this.images = images;
	}

	/*public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}*/
}
