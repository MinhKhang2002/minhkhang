package com.laptrinhjavaweb.api;

import java.util.List;

import com.laptrinhjavaweb.dto.CategoryDTO;
import com.laptrinhjavaweb.entity.CategoryEntity;
import com.laptrinhjavaweb.repository.CategoryRepository;
import com.laptrinhjavaweb.service.impl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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
}
