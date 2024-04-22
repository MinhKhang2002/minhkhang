package com.laptrinhjavaweb.service.impl;

import com.laptrinhjavaweb.converter.CategoryConverter;
import com.laptrinhjavaweb.dto.CategoryDTO;
import com.laptrinhjavaweb.entity.CategoryEntity;
import com.laptrinhjavaweb.repository.CategoryRepository;
import com.laptrinhjavaweb.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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
    public void deleteCategory(long[] ids) {
        for (long id : ids) {
            try {
                categoryRepository.deleteById(id);
            } catch (DataIntegrityViolationException e) {
                throw new IllegalArgumentException("Không thể xóa thể loại với id: " + id +
                        ". Có ràng buộc khóa ngoại với các bản ghi khác.");
            } catch (Exception e) {
                throw new RuntimeException("Đã xảy ra lỗi khi xóa thể loại với id: " + id, e);
            }
        }

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
