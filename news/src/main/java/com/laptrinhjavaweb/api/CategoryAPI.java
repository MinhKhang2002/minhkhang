package com.laptrinhjavaweb.api;

import com.laptrinhjavaweb.dto.CategoryDTO;
import com.laptrinhjavaweb.service.impl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@CrossOrigin
@RestController
public class CategoryAPI {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    @PostMapping("/add-Category")
    public Long createCategory(@RequestBody CategoryDTO categoryDTO,
                               HttpSession session) {
        // Lấy thông tin người dùng từ session
        String loggedInUser = (String) session.getAttribute("loggedInUser");

        // Thêm thông tin người dùng vào trường createdBy của model
        categoryDTO.setCreatedBy(loggedInUser);
        return categoryService.addCategory(categoryDTO);
    }
    @PutMapping("/categories/{id}")
    public  Long updateCategory(@PathVariable long id,
                                                  @RequestBody CategoryDTO categoryDTO){
        return categoryService.updateCategory(id, categoryDTO);
    }
    @DeleteMapping("/categories")
    public ResponseEntity<String>  deleteCategory(@RequestBody long[] ids){
        try {
            categoryService.deleteCategory(ids);
            return ResponseEntity.ok("Xóa thể loại thành công");
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Không thể xóa thể loại. Có ràng buộc khóa ngoại với các bản ghi khác.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể xóa");
        }

    }

}
