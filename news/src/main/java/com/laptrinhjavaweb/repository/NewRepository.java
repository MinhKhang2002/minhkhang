package com.laptrinhjavaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laptrinhjavaweb.entity.NewEntity;

import java.util.List;

public interface NewRepository extends JpaRepository<NewEntity, Long>{
	NewEntity findOneById(Long id);
	List<NewEntity> findByCategory_Code(String categoryCode);
}
