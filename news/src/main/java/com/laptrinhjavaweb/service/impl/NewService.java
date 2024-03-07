package com.laptrinhjavaweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

	@Override
	public NewDTO save(NewDTO newDTO, String loggedInUser, String action) {
		NewEntity newEntity = new NewEntity();

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

		if ("PUT".equalsIgnoreCase(action)) {
			// Nếu là PUT, thêm thông tin người sửa bài viết
			newEntity.setModifiedBy(loggedInUser);
		}

		newEntity = newRepository.save(newEntity);
		return newConverter.toDTO(newEntity);
	}

	@Override
	public void delete(long[] ids) {
		for(long item: ids) {
			newRepository.deleteById(item);
		}
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

	/*@Override
	public List<NewDTO> findAllByStatus(Integer status) {
		List<NewDTO> result = new ArrayList<>();
		List<NewEntity> entities = newRepository.findAllByStatus(status);
		for (NewEntity item : entities) {
			NewDTO newDTO = newConverter.toDTO(item);
			// Lấy giá trị categoryCode từ category
			if (item.getCategory() != null) {
				newDTO.setCategoryCode(item.getCategory().getCode());
			}
			result.add(newDTO);
		}
		return result;
	}*/

}
