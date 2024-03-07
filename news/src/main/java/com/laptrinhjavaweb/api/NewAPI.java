package com.laptrinhjavaweb.api;

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

import javax.servlet.http.HttpSession;
import java.util.List;

@CrossOrigin
@RestController
public class NewAPI {

	@Autowired
	private INewService newService;

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
							 @RequestParam(value = "status", required = false) Integer status) {
		NewOutput result = new NewOutput();
		if (page != null && limit != null) {
			result.setPage(page);
			Pageable pageable = PageRequest.of(page - 1, limit);
			if (status != null) {
				result.setListResult(newService.findAll(pageable, status));
				result.setTotalPage((int) Math.ceil((double) (newService.totalItem(status)) / limit));
			} else {
				result.setListResult(newService.findAll(pageable));
				result.setTotalPage((int) Math.ceil((double) (newService.totalItem()) / limit));
			}
		} else {
			result.setListResult(newService.findAll());
		}
		return result;
	}*/
	/*public NewOutput showNew(@RequestParam(value = "page", required = false) Integer page,
							 @RequestParam(value = "limit", required = false) Integer limit) {
		NewOutput result = new NewOutput();
		if(page != null && limit != null) {
			result.setPage(page);
			Pageable pageable =  PageRequest.of(page-1,limit);
			result.setListResult(newService.findAll(pageable));
			result.setTotalPage((int) Math.ceil( (double) (newService.totalItem()) / limit));
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
	@GetMapping(value = "/new/category/{category}")
	public NewOutput showNewByCategory(@PathVariable String category) {
		NewOutput result = new NewOutput();
		result.setListResult(newService.findByCategory(category));
		return result;
	}

	/*@GetMapping(value = "/new", params = "status")
	public List<NewDTO> showNewByStatus(@RequestParam(value = "status") int status) {
		return newService.findAllByStatus(status);
	}*/
	@GetMapping(value = "/new/approve")
	public NewOutput showNewByStatus(@RequestParam(value = "page", required = false) Integer page,
									 @RequestParam(value = "limit", required = false) Integer limit) {
		NewOutput result = new NewOutput();
		int status = 0; // Đặt giá trị status là 1
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

	/*@PutMapping("/approve")
	public ResponseEntity<String> approveNews(@RequestParam(value = "status") int status, @RequestBody long[] ids) {
		if (ids.length == 0) {
			return ResponseEntity.badRequest().body("Ids list is empty");
		}

		newService.updateStatus(ids, status);

		return ResponseEntity.ok("Updated successfully");
	}*/
	@PutMapping(value = "/new/{id}/approve")
	public ResponseEntity<?> approveNew(@PathVariable("id") Long id) {
		newService.updateStatus(id, 1); // 1 là trạng thái đã duyệt
		return ResponseEntity.ok().build();
	}
}