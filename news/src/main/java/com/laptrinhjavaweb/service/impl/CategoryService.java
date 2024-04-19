package com.laptrinhjavaweb.service.impl;

import com.laptrinhjavaweb.converter.CategoryConverter;
import com.laptrinhjavaweb.dto.CategoryDTO;
import com.laptrinhjavaweb.entity.CategoryEntity;
import com.laptrinhjavaweb.repository.CategoryRepository;
import com.laptrinhjavaweb.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryConverter categoryConverter;

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categoryConverter.toDtoList(categories);
    }

    @Override
    public Long addCategory(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = categoryConverter.toEntity(categoryDTO);
        categoryEntity = categoryRepository.save(categoryEntity);
        return categoryEntity.getId();
    }
    @Override
    public CategoryEntity  getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("category not found"));
    }
    @Override
    public Long updateCategory(long id, CategoryDTO categoryDTO) {
        CategoryEntity existCategory = getCategoryById(id);
        if (existCategory!=null){
            existCategory.setName(categoryDTO.getName());
            existCategory.setCode(categoryDTO.getCode());
            categoryRepository.save(existCategory);
        }


        return existCategory.getId();
    }

}
