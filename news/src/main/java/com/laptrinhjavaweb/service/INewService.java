package com.laptrinhjavaweb.service;

import java.util.List;

import com.laptrinhjavaweb.entity.CategoryEntity;
import com.laptrinhjavaweb.entity.NewEntity;
import org.springframework.data.domain.Pageable;

import com.laptrinhjavaweb.dto.NewDTO;

public interface INewService {
	NewDTO save(NewDTO newDTO, String loggedInUser, String action);
//	NewDTO update(NewDTO newDTO);
	void delete(long[] ids);
	List<NewDTO> findAll(Pageable pageable, Integer status);
	List<NewDTO> findAll(Pageable pageable);
	List<NewDTO> findAll();
	int totalItem(Integer status);
	int totalItem();
	NewDTO getNewById(Long id);
	List<NewDTO> findByCategory(String category);
	void updateStatus(long[] ids, int status);
	void updateStatus(Long id, int status);
//	List<NewDTO> findAllByStatus(Integer status);
	/*List<NewDTO> findByCategories(String category, Pageable pageable);
	int countByCategoriesAndStatus(String category, int status);
	int countByCategories(String category);*/
	int totalItemByCategoryAndStatus(String category, int status);
	int totelItemByCreateByAndStatus(String createBy, int status);
	List<NewDTO> findByCategoryAndStatus(String category, int status, Pageable pageable);
	List<NewDTO> findByCreatedByAndStatus(String createdBy, int status, Pageable pageable);
}
