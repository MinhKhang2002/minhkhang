package com.laptrinhjavaweb.repository;

import com.laptrinhjavaweb.dto.NewDTO;
import com.laptrinhjavaweb.entity.CategoryEntity;
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

    int countByCategoryAndStatus(String category, int status);

	List<NewEntity> findByCategoryCodeAndStatus(String category, int status, Pageable pageable);

	int countByCategoryCodeAndStatus(String category, int status);

	int countByCreatedByAndStatus(String createBy, int status);

    List<NewEntity> findByCreatedByAndStatus(String createdBy, int status, Pageable pageable);

	/*List<NewEntity> findByCategories(String categories, Pageable pageable);

	int countByCategoriesAndStatus(String category, int status);
	int countByCategories(String category);*/
}
