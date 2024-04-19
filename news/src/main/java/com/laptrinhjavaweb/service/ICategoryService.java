package com.laptrinhjavaweb.service;

import java.util.List;
import com.laptrinhjavaweb.dto.CategoryDTO;
import com.laptrinhjavaweb.entity.CategoryEntity;

public interface ICategoryService {
    List<CategoryDTO> getAllCategories();
    Long addCategory(CategoryDTO categoryDTO);
     Long updateCategory(long id, CategoryDTO categoryDTO);
     CategoryEntity  getCategoryById(Long id) ;

}

