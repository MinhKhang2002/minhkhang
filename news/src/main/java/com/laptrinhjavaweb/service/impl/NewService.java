package com.laptrinhjavaweb.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import com.laptrinhjavaweb.entity.ImageEntity;
import com.laptrinhjavaweb.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.converter.NewConverter;
import com.laptrinhjavaweb.dto.NewDTO;
import com.laptrinhjavaweb.entity.CategoryEntity;
import com.laptrinhjavaweb.entity.NewEntity;
import com.laptrinhjavaweb.repository.CategoryRepository;
import com.laptrinhjavaweb.repository.NewRepository;
import com.laptrinhjavaweb.service.INewService;

@Service
public class NewService implements INewService{

	@Autowired
	private NewRepository newRepository;
	
	@Autowired 
	private CategoryRepository categoryRepository;
	
	@Autowired
	private NewConverter newConverter;

	@Autowired
	private ImageRepository imageRepository;

	@Override
	public NewDTO save(NewDTO newDTO, String loggedInUser, String action) {
		NewEntity newEntity = new NewEntity();

		// Kiểm tra nếu không phải là PUT (cập nhật) và không phải là PUT
		if ("POST".equalsIgnoreCase(action)) {
			// Đặt status thành 0 khi thêm mới bài viết
			newDTO.setStatus(0);
		}

		if (newDTO.getId() != null) {
			NewEntity oldNewEntity = newRepository.findOneById(newDTO.getId());
			newEntity = newConverter.toEntity(newDTO, oldNewEntity);
		} else {
			newEntity = newConverter.toEntity(newDTO);
		}

		CategoryEntity categoryEntity = categoryRepository.findOneByCode(newDTO.getCategoryCode());
		newEntity.setCategory(categoryEntity);

		// Thiết lập người tạo và người sửa
		newEntity.setCreatedBy(loggedInUser);

		/*if ("PUT".equalsIgnoreCase(action)) {
			// Nếu là PUT, thêm thông tin người sửa bài viết
			newEntity.setModifiedBy(loggedInUser);
			// Đặt status thành 1 khi thêm mới bài viết
			newDTO.setStatus(1);
		}*/
		if ("PUT".equals(action)) {
			// Nếu là PUT, thêm thông tin người sửa bài viết
			newEntity.setModifiedBy(loggedInUser);
		}

		newEntity = newRepository.save(newEntity);
		return newConverter.toDTO(newEntity);
	}

	/*@Override
	public void delete(long[] ids) {
		for(long item: ids) {
			newRepository.deleteById(item);
		}
	}*/
	@Override
	public void delete(long[] ids) {
		List<Long> idList = Arrays.stream(ids).boxed().collect(Collectors.toList());
		newRepository.deleteAllById(idList);
	}

	@Override
	public List<NewDTO> findAll(Pageable pageable, Integer status) {
		List<NewDTO> result = new ArrayList<>();
		List<NewEntity> entities = newRepository.findAllByStatus(status, pageable);
		for (NewEntity item : entities) {
			NewDTO newDTO = newConverter.toDTO(item);
			// Lấy giá trị categoryCode từ category
			if (item.getCategory() != null) {
				newDTO.setCategoryCode(item.getCategory().getCode());
			}
			result.add(newDTO);
		}
		return result;
	}

	@Override
	public List<NewDTO> findAll(Pageable pageable) {
		List<NewDTO> result = new ArrayList<>();
		List<NewEntity> entities = newRepository.findAll(pageable).getContent();
		for (NewEntity item : entities) {
			NewDTO newDTO = newConverter.toDTO(item);
			// Lấy giá trị categoryCode từ category
			if (item.getCategory() != null) {
				newDTO.setCategoryCode(item.getCategory().getCode());
			}
			result.add(newDTO);
		}
		return result;
	}

	@Override
	public int totalItem(Integer status) {
		return (int) newRepository.countByStatus(status);
	}

	@Override
	public int totalItem() {
		return (int) newRepository.count();
	}

	/*@Override
	public NewDTO getNewById(Long id) {
		NewEntity newEntity = newRepository.findOneByIdAndStatus(id, 1); // Thêm điều kiện status = 1 vào truy vấn

		if (newEntity != null) {
			NewDTO newDTO = newConverter.toDTO(newEntity);
			// Kiểm tra xem newEntity có chứa category hay không
			if (newEntity.getCategory() != null) {
				// Nếu có, thì set categoryCode vào DTO
				newDTO.setCategoryCode(newEntity.getCategory().getCode());
			}
			return newDTO;
		} else {
			// Trả về null hoặc xử lý theo ý bạn
			return null;
		}
	}*/
	@Override
	public NewDTO getNewById(Long id) {
		NewEntity newEntity = newRepository.findOneById(id);

		if (newEntity != null) {
			NewDTO newDTO = newConverter.toDTO(newEntity);
			// Kiểm tra xem newEntity có chứa category hay không
			if (newEntity.getCategory() != null) {
				// Nếu có, thì set categoryCode vào DTO
				newDTO.setCategoryCode(newEntity.getCategory().getCode());
			}
			return newDTO;
		} else {
			// Trả về null hoặc xử lý theo ý bạn
			return null;
		}
	}

	@Override
	public List<NewDTO> findByCategory(String category) {
		List<NewEntity> entities = newRepository.findByCategory_Code(category);
		return entities.stream().map(newConverter::toDTO).collect(Collectors.toList());
	}

	@Override
	public void updateStatus(long[] ids, int status) {
		for(long id : ids) {
			Optional<NewEntity> optionalNewEntity = newRepository.findById(id);
			if (optionalNewEntity.isPresent()) {
				NewEntity newEntity = optionalNewEntity.get();
				newEntity.setStatus(status);
				newRepository.save(newEntity);
			} else {
				// Xử lý nếu không tìm thấy bài viết
			}
		}
	}

	@Override
	public void updateStatus(Long id, int status) {
		Optional<NewEntity> optionalNewEntity = newRepository.findById(id);
		if (optionalNewEntity.isPresent()) {
			NewEntity newEntity = optionalNewEntity.get();
			newEntity.setStatus(status);
			newRepository.save(newEntity);
		} else {
			// Xử lý nếu không tìm thấy bài viết
		}
	}

	/*@Override
	public int totalItemByCategoryAndStatus(String category, int status) {
		return newRepository.countByCategoryAndStatus(category, status);
	}*/

	@Override
	public int totalItemByCategoryAndStatus(String category, int status) {
		// Đảm bảo rằng category không null và không trống
		if (category == null || category.isEmpty()) {
			throw new IllegalArgumentException("Category cannot be null or empty");
		}
		// Gọi phương thức countByCategoryCodeAndStatus thay vì countByCategoryAndStatus
		return newRepository.countByCategoryCodeAndStatus(category, status);
	}

	@Override
	public int totelItemByCreateByAndStatus(String createBy, int status) {
		return newRepository.countByCreatedByAndStatus(createBy, status);
	}

	@Override
	public List<NewDTO> findByCategoryAndStatus(String category, int status, Pageable pageable) {
		List<NewDTO> result = new ArrayList<>();
		List<NewEntity> entities = newRepository.findByCategoryCodeAndStatus(category, status, pageable);
		for (NewEntity item : entities) {
			NewDTO newDTO = newConverter.toDTO(item);
			// Lấy giá trị categoryCode từ category
			if (item.getCategory() != null) {
				newDTO.setCategoryCode(item.getCategory().getCode());
			}
			result.add(newDTO);
		}
		return result;
	}

	@Override
	public List<NewDTO> findByCreatedByAndStatus(String createdBy, int status, Pageable pageable) {
		List<NewDTO> result = new ArrayList<>();
		List<NewEntity> entities = newRepository.findByCreatedByAndStatus(createdBy, status, pageable);
		for (NewEntity item : entities) {
			NewDTO newDTO = newConverter.toDTO(item);
			// Thêm logic xử lý tương tự cho categoryCode
			if (item.getCategory() != null) {
				newDTO.setCategoryCode(item.getCategory().getCode());
			}
			result.add(newDTO);
		}
		return result;
	}

	@Override
	public List<ImageEntity> getAllImage() {
		return imageRepository.findAll();
	}

	@Override
	public List<NewDTO> findAll() {
		List<NewDTO> result = new ArrayList<>();
		List<NewEntity> entities = newRepository.findAll();
		for (NewEntity item : entities) {
			NewDTO newDTO = newConverter.toDTO(item);
			// Lấy giá trị categoryCode từ category
			if (item.getCategory() != null) {
				newDTO.setCategoryCode(item.getCategory().getCode());
			}
			result.add(newDTO);
		}
		return result;
	}


	public List<Map<String, Object>> getNewPostCountsByDateRange(Date startDate, Date endDate) {
		List<Object[]> counts = newRepository.countByDateRangeWithStatus(startDate, endDate, 1); // Thêm điều kiện status = 1

		// Tạo map để lưu trữ kết quả
		Map<String, Long> dateCountsMap = new HashMap<>();

		// Xử lý kết quả trả về từ cơ sở dữ liệu
		for (Object[] count : counts) {
			Date originalDate = (Date) count[0];

			// Chuyển đổi ngày về múi giờ cục bộ
			LocalDate localDate = originalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			// Chuyển đổi lại ngày sang định dạng chuẩn
			String dateString = localDate.toString();

			Long currentCount = (Long) count[1];
			dateCountsMap.put(dateString, dateCountsMap.getOrDefault(dateString, 0L) + currentCount);
		}

		// Tạo danh sách ngày trong khoảng từ startDate đến endDate
		List<LocalDate> dateRange = new ArrayList<>();
		LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		for (LocalDate date = startLocalDate; !date.isAfter(endLocalDate); date = date.plusDays(1)) {
			dateRange.add(date);
		}

		// Tạo kết quả cuối cùng
		List<Map<String, Object>> result = new ArrayList<>();

		for (LocalDate date : dateRange) {
			String dateString = date.toString();
			Long count = dateCountsMap.getOrDefault(dateString, 0L);

			Map<String, Object> formattedEntry = new HashMap<>();
			formattedEntry.put("date", dateString);
			formattedEntry.put("dayOfWeek", date.getDayOfWeek().toString()); // Thêm thứ vào kết quả
			formattedEntry.put("count", count);
			result.add(formattedEntry);
		}

		return result;
	}

}
