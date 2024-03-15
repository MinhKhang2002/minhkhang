package com.laptrinhjavaweb.api;

import com.laptrinhjavaweb.controller.LoginController;
import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.repository.UserRepository;
import com.laptrinhjavaweb.service.IUserService;
import com.laptrinhjavaweb.service.impl.NewService;
import com.laptrinhjavaweb.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.laptrinhjavaweb.api.output.NewOutput;
import com.laptrinhjavaweb.dto.NewDTO;
import com.laptrinhjavaweb.service.INewService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
public class NewAPI {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private INewService newService;

	@Autowired UserService userService;

	@GetMapping(value = "/new")
	public NewOutput showNew(@RequestParam(value = "page", required = false) Integer page,
							 @RequestParam(value = "limit", required = false) Integer limit) {
		NewOutput result = new NewOutput();
		int status = 1; // Đặt giá trị status là 1
		if (page != null && limit != null) {
			result.setPage(page);
			Pageable pageable = PageRequest.of(page - 1, limit);
			result.setListResult(newService.findAll(pageable, status)); // Truyền tham số status vào phương thức findAll
			result.setTotalPage((int) Math.ceil((double) (newService.totalItem(status)) / limit));
		} else {
			result.setListResult(newService.findAll());
		}
		return result;
	}
	/*@GetMapping(value = "/new")
	public NewOutput showNew(@RequestParam(value = "page", required = false) Integer page,
							 @RequestParam(value = "limit", required = false) Integer limit,
							 HttpServletRequest request) {
		NewOutput result = new NewOutput();
		int status = 1; // Đặt giá trị status là 1

		// Lấy mã vai trò từ yêu cầu API thông qua interceptor
		String userRole = (String) request.getAttribute("userRole");

		if (page != null && limit != null) {
			result.setPage(page);
			if (userRole != null) {
				// Nếu có mã vai trò, sử dụng nó để lọc bài viết
				Pageable pageable = PageRequest.of(page - 1, limit);
				result.setListResult(newService.findAllByCategory(userRole, pageable, status));
				result.setTotalPage((int) Math.ceil((double) (newService.totalItemByCategory(userRole, status)) / limit));
			} else {
				// Nếu không có mã vai trò, hiển thị tất cả các bài viết
				Pageable pageable = PageRequest.of(page - 1, limit);
				result.setListResult(newService.findAll(pageable, status));
				result.setTotalPage((int) Math.ceil((double) (newService.totalItem(status)) / limit));
			}
		} else {
			result.setListResult(newService.findAll());
		}

		return result;
	}*/

	@GetMapping(value = "/new/{id}")
	public ResponseEntity<NewDTO> getNewById(@PathVariable Long id) {
		NewDTO newsDTO = newService.getNewById(id);

		if (newsDTO != null) {
			return new ResponseEntity<>(newsDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/new")
	public NewDTO createNew(@RequestBody NewDTO model, HttpSession session) {
		// Lấy thông tin người dùng từ session
		String loggedInUser = (String) session.getAttribute("loggedInUser");

		// Thêm thông tin người dùng vào trường createdBy của model
		model.setCreatedBy(loggedInUser);

		// Thực hiện lưu bài viết mới với thông tin người dùng
//		NewDTO savedNew = newService.save(model, loggedInUser, "POST");

		// Thêm logic của bạn ở đây

		return newService.save(model, loggedInUser, "POST");
	}

	@PutMapping("/new/{id}")
	public NewDTO updateNew(@RequestBody NewDTO model, @PathVariable("id") long id, HttpSession session) {
		// Lấy thông tin người dùng từ session
		String loggedInUser = (String) session.getAttribute("loggedInUser");

		// Thêm thông tin người dùng vào trường modifiedBy của model
		model.setModifiedBy(loggedInUser);

		// Thiết lập ID cho model
		model.setId(id);

		// Thực hiện cập nhật bài viết với thông tin người dùng
		NewDTO updatedNew = newService.save(model, loggedInUser, "PUT");

		// Thêm logic của bạn ở đây

		return updatedNew;
	}

	@DeleteMapping(value = "/new")
	public void deleteNew(@RequestBody long[] ids) {
		newService.delete(ids);
	}

	//Lọc bài viết theo thể loại mà ADMIN đã đăng nhập vào
	@GetMapping(value = "/new/category")
	public NewOutput showNewByCategory(@RequestParam(value = "category", required = false) String queryCategory,
									   HttpSession session,
									   @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
									   @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {
		NewOutput result = new NewOutput();
		int status = 1; // Đặt giá trị status là 1
		Pageable pageable;

		String categories = (String) session.getAttribute("categories");
		if (categories != null && !categories.isEmpty()) {
			// Sử dụng categories từ session để lọc bài viết
			queryCategory = categories;
		}

		if (queryCategory != null && !queryCategory.isEmpty()) {
			// Xử lý khi categories được chỉ định
			int totalItem = newService.totalItemByCategoryAndStatus(queryCategory, status); // Đếm số lượng bài viết theo thể loại và status là 1
			pageable = PageRequest.of(page - 1, limit);
			result.setPage(page);
			result.setListResult(newService.findByCategoryAndStatus(queryCategory, status, pageable)); // Phân trang theo thể loại và status là 1
			result.setTotalPage((int) Math.ceil((double) totalItem / limit));
		} else {
			// Xử lý khi không có categories được chỉ định
			pageable = PageRequest.of(page - 1, limit);
			result.setPage(page);
			int totalItem = newService.totalItem(status); // Đếm số lượng bài viết có status là 1
			result.setListResult(newService.findAll(pageable, status)); // Phân trang tất cả bài viết có status là 1
			result.setTotalPage((int) Math.ceil((double) totalItem / limit));
		}

		return result;
	}

	@GetMapping(value = "/new/userName")
	public NewOutput showNewByUserName(@RequestParam(value = "userName", required = false) String queryUserName,
									   HttpSession session,
									   @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
									   @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {
		NewOutput result = new NewOutput();
		int status = 1; // Đặt giá trị status là 1
		Pageable pageable;

		String userName = (String) session.getAttribute("loggedInUser");
		if (userName != null && !userName.isEmpty()) {
			// Sử dụng userName từ session để lọc bài viết
			queryUserName = userName;
		}

		if (queryUserName != null && !queryUserName.isEmpty()) {
			// Xử lý khi categories được chỉ định
			int totalItem = newService.totelItemByCreateByAndStatus(queryUserName, status); // Đếm số lượng bài viết theo userName và status là 1
			pageable = PageRequest.of(page - 1, limit);
			result.setPage(page);
			result.setListResult(newService.findByCreatedByAndStatus(queryUserName, status, pageable)); // Phân trang theo thể loại và status là 1
			result.setTotalPage((int) Math.ceil((double) totalItem / limit));
		} else {
			// Xử lý khi không có categories được chỉ định
			pageable = PageRequest.of(page - 1, limit);
			result.setPage(page);
			int totalItem = newService.totalItem(status); // Đếm số lượng bài viết có status là 1
			result.setListResult(newService.findAll(pageable, status)); // Phân trang tất cả bài viết có status là 1
			result.setTotalPage((int) Math.ceil((double) totalItem / limit));
		}

		return result;
	}

	@GetMapping(value = "/new/approve")
	public NewOutput showNewByStatusAndCategory(@RequestParam(value = "category", required = false) String queryCategory,
									 HttpSession session,
									 @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
									 @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {
		NewOutput result = new NewOutput();
		int status = 0; // Đặt giá trị status là 0
		Pageable pageable;

		String categories = (String) session.getAttribute("categories");
		if (categories != null && !categories.isEmpty()) {
			// Sử dụng categories từ session để lọc bài viết
			queryCategory = categories;
		}

		if (queryCategory != null && !queryCategory.isEmpty()) {
			result.setPage(page);
			pageable = PageRequest.of(page - 1, limit);
			int totalItem = newService.totalItemByCategoryAndStatus(queryCategory, status);
			result.setListResult(newService.findByCategoryAndStatus(queryCategory, status, pageable));
			/*result.setTotalPage((int) Math.ceil((double) (newService.totalItem(status)) / limit));*/
			result.setTotalPage((int) Math.ceil((double) totalItem / limit));
		} else {
			// Xử lý khi không có categories được chỉ định
			pageable = PageRequest.of(page - 1, limit);
			result.setPage(page);
			int totalItem = newService.totalItem(status); // Đếm số lượng bài viết có status là 1
			result.setListResult(newService.findAll(pageable, status)); // Phân trang tất cả bài viết có status là 1
			result.setTotalPage((int) Math.ceil((double) totalItem / limit));
		}
		return result;
	}

	/*@GetMapping(value = "/new/approve")
	public NewOutput showNewByStatus(@RequestParam(value = "page", required = false) Integer page,
									 @RequestParam(value = "limit", required = false) Integer limit) {
		NewOutput result = new NewOutput();
		int status = 0; // Đặt giá trị status là 0

		if (page != null && limit != null) {
			result.setPage(page);
			Pageable pageable = PageRequest.of(page - 1, limit);
			result.setListResult(newService.findAll(pageable, status)); // Truyền tham số status vào phương thức findAll
			result.setTotalPage((int) Math.ceil((double) (newService.totalItem(status)) / limit));
		} else {
			result.setListResult(newService.findAll());
		}
		return result;
	}*/

	@PutMapping(value = "/new/{id}/approve")
	public ResponseEntity<?> approveNew(@PathVariable("id") Long id) {
		newService.updateStatus(id, 1); // 1 là trạng thái đã duyệt
		return ResponseEntity.ok().build();
	}
}