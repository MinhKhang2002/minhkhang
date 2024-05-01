package com.laptrinhjavaweb.service.impl;

import com.laptrinhjavaweb.converter.CategoryConverter;
import com.laptrinhjavaweb.dto.CategoryDTO;
import com.laptrinhjavaweb.entity.CategoryEntity;
import com.laptrinhjavaweb.entity.NewEntity;
import com.laptrinhjavaweb.entity.RoleEntity;
import com.laptrinhjavaweb.repository.CategoryRepository;
import com.laptrinhjavaweb.repository.NewRepository;
import com.laptrinhjavaweb.repository.RoleRepository;
import com.laptrinhjavaweb.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryConverter categoryConverter;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private NewRepository newRepository;

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

    /*@Override
    public void deleteCategory(long[] ids) {
        for (long id : ids) {
            try {
                // Xoá các vai trò có liên quan đến thể loại
                deleteRolesByCategoryId(id);

                // Xoá các bài viết có category_id tương ứng
                deleteNewsByCategoryId(id);

                // Tiếp tục xoá thể loại
                categoryRepository.deleteById(id);
            } catch (DataIntegrityViolationException e) {
                throw new IllegalArgumentException("Không thể xóa thể loại với id: " + id +
                        ". Có ràng buộc khóa ngoại với các bản ghi khác.");
            } catch (Exception e) {
                throw new RuntimeException("Đã xảy ra lỗi khi xóa thể loại với id: " + id, e);
            }
        }

    }

    // Xoá bài viết có liên quan tới thể loại
    private void deleteNewsByCategoryId(long categoryId) {
        // Tìm và xoá các bài viết có category_id tương ứng
        List<NewEntity> news = newRepository.findByCategoryId(categoryId);
        newRepository.deleteAll(news);
    }

    // Xoá vai trò có liên quan tới thể loại
    private void deleteRolesByCategoryId(long categoryId) {
        // Tìm categoryCode của thể loại cần xoá
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            CategoryEntity category = categoryOptional.get();
            String categoryCode = category.getCode();

            // Tìm và xoá các vai trò có categoryCode tương ứng
            List<RoleEntity> roles = roleRepository.findByCategoriesContaining(categoryCode);
            roleRepository.deleteAll(roles);
        } else {
            throw new RuntimeException("Không tìm thấy thể loại với id: " + categoryId);
        }
    }*/
    @Override
    public void deleteCategory(long[] ids) {
        for (long id : ids) {
            try {
                // Kiểm tra xem categoryId có được sử dụng trong bảng Role hay không
                boolean usedInRole = isCategoryIdUsedInRole(id);
                // Kiểm tra xem categoryId có được sử dụng trong bảng New hay không
                boolean usedInNew = isCategoryIdUsedInNew(id);

                if (usedInRole || usedInNew) {
                    // Nếu có sử dụng trong bảng Role hoặc New, tiến hành xoá bảng đó trước
                    if (usedInRole) {
                        deleteRolesByCategoryId(id);
                    }
                    if (usedInNew) {
                        deleteNewsByCategoryId(id);
                    }
                    // Sau đó tiếp tục xoá thể loại
                    categoryRepository.deleteById(id);
                } else {
                    // Nếu không có sử dụng trong cả bảng Role và New, tiếp tục xoá thể loại trực tiếp
                    categoryRepository.deleteById(id);
                }
            } catch (DataIntegrityViolationException e) {
                throw new IllegalArgumentException("Không thể xóa thể loại với id: " + id +
                        ". Có ràng buộc khóa ngoại với các bản ghi khác.");
            } catch (Exception e) {
                throw new RuntimeException("Đã xảy ra lỗi khi xóa thể loại với id: " + id, e);
            }
        }
    }

    // Kiểm tra xem categoryId có được sử dụng trong bảng Role hay không
    private boolean isCategoryIdUsedInRole(long categoryId) {
        // Tìm categoryCode của thể loại cần xoá
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            CategoryEntity category = categoryOptional.get();
            String categoryCode = category.getCode();

            // Tìm và xoá các vai trò có categoryCode tương ứng
            List<RoleEntity> roles = roleRepository.findByCategoriesContaining(categoryCode);
            return !roles.isEmpty();
        } else {
            // Nếu không tìm thấy thể loại, nghĩa là không có vai trò nào sử dụng categoryId
            return false;
        }
    }

    // Kiểm tra xem categoryId có được sử dụng trong bảng New hay không
    private boolean isCategoryIdUsedInNew(long categoryId) {
        List<NewEntity> news = newRepository.findByCategoryId(categoryId);
        return !news.isEmpty();
    }

    // Xoá các bài viết có liên quan tới thể loại
    private void deleteNewsByCategoryId(long categoryId) {
        // Tìm và xoá các bài viết có category_id tương ứng
        List<NewEntity> news = newRepository.findByCategoryId(categoryId);
        newRepository.deleteAll(news);
    }

    // Xoá vai trò có liên quan tới thể loại
    private void deleteRolesByCategoryId(long categoryId) {
        // Tìm categoryCode của thể loại cần xoá
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            CategoryEntity category = categoryOptional.get();
            String categoryCode = category.getCode();

            // Tìm và xoá các vai trò có categoryCode tương ứng
            List<RoleEntity> roles = roleRepository.findByCategoriesContaining(categoryCode);
            roleRepository.deleteAll(roles);
        } else {
            throw new RuntimeException("Không tìm thấy thể loại với id: " + categoryId);
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
