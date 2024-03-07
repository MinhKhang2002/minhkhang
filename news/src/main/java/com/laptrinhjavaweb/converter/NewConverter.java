package com.laptrinhjavaweb.converter;

import com.laptrinhjavaweb.entity.CategoryEntity;
import com.laptrinhjavaweb.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.laptrinhjavaweb.dto.NewDTO;
import com.laptrinhjavaweb.entity.NewEntity;

@Component
public class NewConverter {

	@Autowired
	private CategoryRepository categoryRepository;
	
	public NewEntity toEntity(NewDTO dto) {
		NewEntity entity = new NewEntity();
		entity.setTitle(dto.getTitle());
		entity.setContent(dto.getContent());
		entity.setShortDescription(dto.getShortDescription());
		entity.setThumbnail(dto.getThumbnail());
		// Thiết lập giá trị của category thông qua categoryCode
		/*CategoryEntity categoryEntity = categoryRepository.findOneByCode(dto.getCategoryCode());
		entity.setCategory(categoryEntity);*/
		return entity;
	}
	
	public NewDTO toDTO(NewEntity entity) {
		NewDTO dto = new NewDTO();
		if (entity.getId() != null) {
			dto.setId(entity.getId());
		}
		dto.setTitle(entity.getTitle());
		dto.setContent(entity.getContent());
		dto.setShortDescription(entity.getShortDescription());

		// Kiểm tra xem getCategory() trả về có phải là null không
		CategoryEntity category = entity.getCategory();
		if (category != null) {
			dto.setCategoryCode(category.getCode());
		}
//		dto.setCategoryCode(entity.getCategory().getCode());
		dto.setThumbnail(entity.getThumbnail());
//		dto.setThumbnail(cloudinaryService.generateTransformedImageUrl(entity.getThumbnail(), 300, 200));
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setModifiedDate(entity.getModifiedDate());
		dto.setModifiedBy(entity.getModifiedBy());
		dto.setStatus(entity.getStatus());
		return dto;
	}
	
	public NewEntity toEntity(NewDTO dto, NewEntity entity) {
		entity.setTitle(dto.getTitle());
		entity.setContent(dto.getContent());
		entity.setShortDescription(dto.getShortDescription());
		entity.setThumbnail(dto.getThumbnail());
		return entity;
	}
}