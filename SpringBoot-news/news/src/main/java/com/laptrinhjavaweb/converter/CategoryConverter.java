package com.laptrinhjavaweb.converter;

import com.laptrinhjavaweb.dto.CategoryDTO;
import com.laptrinhjavaweb.entity.CategoryEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryConverter {
    public CategoryDTO toDTO(CategoryEntity categoryEntity) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryEntity.getId());
        categoryDTO.setCode(categoryEntity.getCode());
        categoryDTO.setName(categoryEntity.getName());
        return categoryDTO;
    }

    public CategoryEntity toEntity(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCode(categoryDTO.getCode());
        categoryEntity.setName(categoryDTO.getName());
        return categoryEntity;
    }

    public List<CategoryDTO> toDtoList(List<CategoryEntity> entities) {
        // Khai báo một danh sách (List) để lưu trữ các đối tượng DTO
        List<CategoryDTO> dtos = new ArrayList<>();

        // Lặp qua từng đối tượng CategoryEntity trong danh sách entities
        for (CategoryEntity entity : entities) {

            /*// Gọi phương thức toDTO để chuyển đổi từ CategoryEntity sang CategoryDTO
            CategoryDTO dto = toDTO(entity);
            // Thêm đối tượng DTO đã chuyển đổi vào danh sách dtos
            dtos.add(dto);*/

            // Thay thế để ngắn gọn hơn cách trên
            // Gọi phương thức toDTO để chuyển đổi từ CategoryEntity sang CategoryDTO
            // Thêm đối tượng DTO đã chuyển đổi vào danh sách dtos
            dtos.add(toDTO(entity));
        }
        return dtos;
    }
}
