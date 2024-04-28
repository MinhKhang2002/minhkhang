package com.laptrinhjavaweb.api;

import com.laptrinhjavaweb.controller.LoginController;
import com.laptrinhjavaweb.entity.ImageEntity;
import com.laptrinhjavaweb.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.laptrinhjavaweb.api.output.NewOutput;
import com.laptrinhjavaweb.dto.NewDTO;
import com.laptrinhjavaweb.service.INewService;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
//			Pageable pageable = PageRequest.of(page - 1, limit);
			Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("modifiedDate").descending());
			result.setListResult(newService.findAll(pageable, status)); // Truyền tham số status vào phương thức findAll
			result.setTotalPage((int) Math.ceil((double) (newService.totalItem(status)) / limit));
		} else {
			result.setListResult(newService.findAll());
		}
		return result;
	}

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
//		model.setStatus(1);

		// Thiết lập ID cho model
		model.setId(id);

		// Thực hiện cập nhật bài viết với thông tin người dùng
		NewDTO updatedNew = newService.save(model, loggedInUser, "PUT");

		newService.updateStatus(id, 1);
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
//			pageable = PageRequest.of(page - 1, limit);
			pageable = PageRequest.of(page - 1, limit, Sort.by("modifiedDate").descending());
			result.setPage(page);
			result.setListResult(newService.findByCategoryAndStatus(queryCategory, status, pageable)); // Phân trang theo thể loại và status là 1
			result.setTotalPage((int) Math.ceil((double) totalItem / limit));
		} else {
			// Xử lý khi không có categories được chỉ định
//			pageable = PageRequest.of(page - 1, limit);
			pageable = PageRequest.of(page - 1, limit, Sort.by("modifiedDate").descending());
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
//			pageable = PageRequest.of(page - 1, limit);
			pageable = PageRequest.of(page - 1, limit, Sort.by("modifiedDate").descending());
			result.setPage(page);
			result.setListResult(newService.findByCreatedByAndStatus(queryUserName, status, pageable)); // Phân trang theo thể loại và status là 1
			result.setTotalPage((int) Math.ceil((double) totalItem / limit));
		} else {
			// Xử lý khi không có categories được chỉ định
//			pageable = PageRequest.of(page - 1, limit);
			pageable = PageRequest.of(page - 1, limit, Sort.by("modifiedDate").descending());
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
//			pageable = PageRequest.of(page - 1, limit);
			pageable = PageRequest.of(page - 1, limit, Sort.by("modifiedDate").descending());
			int totalItem = newService.totalItemByCategoryAndStatus(queryCategory, status);
			result.setListResult(newService.findByCategoryAndStatus(queryCategory, status, pageable));
			/*result.setTotalPage((int) Math.ceil((double) (newService.totalItem(status)) / limit));*/
			result.setTotalPage((int) Math.ceil((double) totalItem / limit));
		} else {
			// Xử lý khi không có categories được chỉ định
//			pageable = PageRequest.of(page - 1, limit);
			pageable = PageRequest.of(page - 1, limit, Sort.by("modifiedDate").descending());
			result.setPage(page);
			int totalItem = newService.totalItem(status); // Đếm số lượng bài viết có status là 1
			result.setListResult(newService.findAll(pageable, status)); // Phân trang tất cả bài viết có status là 1
			result.setTotalPage((int) Math.ceil((double) totalItem / limit));
		}
		return result;
	}

	// Duyệt bài viết
	@PutMapping(value = "/new/{id}/approve")
	public ResponseEntity<?> approveNew(@PathVariable("id") Long id) {
		newService.updateStatus(id, 1); // 1 là trạng thái đã duyệt
		return ResponseEntity.ok().build();
	}

	// Từ chối duyệt bài viết
	@PutMapping(value = "/new/{id}/notApprove")
	public ResponseEntity<?> notApproveNew(@PathVariable("id") Long id) {
		newService.updateStatus(id, 2); // 2 là trạng thái không được duệt
		return ResponseEntity.ok().build();
	}

	// Lấy các bài viết không được duyệt
	@GetMapping(value = "/detailNotApprove")
	public NewOutput showNotApprove(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
							 @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {
		NewOutput result = new NewOutput();
		int status = 2; // Đặt giá trị status là 1
		if (page != null && limit != null) {
			result.setPage(page);
//			Pageable pageable = PageRequest.of(page - 1, limit);
			Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("modifiedDate").descending());
			result.setListResult(newService.findAll(pageable, status)); // Truyền tham số status vào phương thức findAll
			result.setTotalPage((int) Math.ceil((double) (newService.totalItem(status)) / limit));
		} else {
			result.setListResult(newService.findAll());
		}
		return result;
	}

	@GetMapping(value = "/image")
	public ResponseEntity<List<ImageEntity>> getAllImages() {
		List<ImageEntity> imageEntities = newService.getAllImage();
		return ResponseEntity.ok(imageEntities);
	}

	@GetMapping(value = "/newPaging/categoryCode")
	public NewOutput PagingCategory(@RequestParam(value = "categoryCode", required = false, defaultValue = "") String categoryCode,
												@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
												@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {
		NewOutput result = new NewOutput();
		int status = 1; // Đặt giá trị status là 0
		Pageable pageable;

		if (categoryCode != null && !categoryCode.isEmpty()) {
			result.setPage(page);
//			pageable = PageRequest.of(page - 1, limit);
			pageable = PageRequest.of(page - 1, limit, Sort.by("modifiedDate").descending());
			int totalItem = newService.totalItemByCategoryAndStatus(categoryCode, status);
			result.setListResult(newService.findByCategoryAndStatus(categoryCode, status, pageable));
			/*result.setTotalPage((int) Math.ceil((double) (newService.totalItem(status)) / limit));*/
			result.setTotalPage((int) Math.ceil((double) totalItem / limit));
		} else {
			// Xử lý khi không có categories được chỉ định
			pageable = PageRequest.of(page - 1, limit, Sort.by("modifiedDate").descending());
			result.setPage(page);
			int totalItem = newService.totalItem(status); // Đếm số lượng bài viết có status là 1
			result.setListResult(newService.findAll(pageable, status)); // Phân trang tất cả bài viết có status là 1
			result.setTotalPage((int) Math.ceil((double) totalItem / limit));
		}
		return result;
	}

	@GetMapping("/new/countByDateRange")
	public ResponseEntity<List<Map<String, Object>>> countNewPostsByDateRange(
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		if (endDate == null) {
			endDate = new Date(); // Nếu không có endDate, sẽ lấy ngày hiện tại
		}

		List<Map<String, Object>> counts = newService.getNewPostCountsByDateRange(startDate, endDate);
		return ResponseEntity.ok(counts);
	}

	@GetMapping("/new/countByLast7Days")
	public ResponseEntity<List<Map<String, Object>>> countNewPostsLast7Days() {
		Calendar calendar = Calendar.getInstance();

		// Lấy ngày và giờ hiện tại
		Date currentDate = calendar.getTime();

		// Trừ 7 ngày từ ngày hiện tại
		calendar.add(Calendar.DAY_OF_MONTH, - 7);
		Date endDate = calendar.getTime();

		// Trừ 7 ngày từ ngày hiện tại
		calendar.add(Calendar.DAY_OF_MONTH, - 6);
		Date startDate = calendar.getTime();

		List<Map<String, Object>> counts = newService.getNewPostCountsByDateRange(startDate, endDate);
		return ResponseEntity.ok(counts);
	}

	@GetMapping("/new/countByLastMonth")
	public ResponseEntity<List<Map<String, Object>>> countNewPostsLastMonth() {
		Calendar calendar = Calendar.getInstance();

		// Đặt endDate là ngày cuối cùng của tháng trước
		calendar.add(Calendar.MONTH, -1);
//		calendar.set(Calendar.DAY_OF_MONTH, 1); // Đặt ngày là 1 để lấy đầu tháng
		Date endDate = calendar.getTime();

//		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); // Đặt ngày là ngày cuối cùng của tháng
		calendar.add(Calendar.MONTH, -1);
		Date startDate = calendar.getTime();

		List<Map<String, Object>> counts = newService.getNewPostCountsByDateRange(startDate, endDate);
		return ResponseEntity.ok(counts);
	}


	@GetMapping("/new/countByLastQuarter")
	public ResponseEntity<List<Map<String, Object>>> countNewPostsLastQuarter() {
		Calendar calendar = Calendar.getInstance();

		// Di chuyển ngày hiện tại 3 tháng trước để có được thời điểm cuối cùng của quý hiện tại
		calendar.add(Calendar.MONTH, -3);
		Date endDate = calendar.getTime();

		// Di chuyển thêm 9 tháng (3 quý) để đến thời điểm cuối cùng của quý trước đó
		calendar.add(Calendar.MONTH, -3);
		Date startDate = calendar.getTime();

		List<Map<String, Object>> counts = newService.getNewPostCountsByDateRange(startDate, endDate);
		return ResponseEntity.ok(counts);
	}
}