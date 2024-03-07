package com.laptrinhjavaweb.repository;

import com.laptrinhjavaweb.dto.NewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.laptrinhjavaweb.entity.NewEntity;

import java.util.List;

public interface NewRepository extends JpaRepository<NewEntity, Long>{
	NewEntity findOneById(Long id);
	List<NewEntity> findByCategory_Code(String categoryCode);

    NewEntity findOneByIdAndStatus(Long id, int i);
	List<NewEntity> findAllByStatus(Integer status, Pageable pageable);
	long countByStatus(Integer status);
	List<NewEntity> findAllByStatus(Integer status);
}
