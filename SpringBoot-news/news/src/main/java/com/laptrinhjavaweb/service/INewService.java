package com.laptrinhjavaweb.service;

import java.util.List;

import com.laptrinhjavaweb.entity.NewEntity;
import org.springframework.data.domain.Pageable;

import com.laptrinhjavaweb.dto.NewDTO;

public interface INewService {
	NewDTO save(NewDTO newDTO, String loggedInUser, String action);
//	NewDTO update(NewDTO newDTO);
	void delete(long[] ids);
	List<NewDTO> findAll(Pageable pageable);
	List<NewDTO> findAll();
	int totalItem();
	NewDTO getNewById(Long id);
	List<NewDTO> findByCategory(String category);
}
