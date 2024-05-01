package com.laptrinhjavaweb.repository;

import com.laptrinhjavaweb.dto.NewDTO;
import com.laptrinhjavaweb.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.laptrinhjavaweb.entity.NewEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
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

	/*@Query("SELECT n.createdDate, COUNT(n) FROM NewEntity n WHERE n.createdDate BETWEEN :startDate AND :endDate GROUP BY n.createdDate")
	List<Object[]> countByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);*/
	@Query("SELECT n.createdDate, COUNT(n) FROM NewEntity n WHERE n.createdDate BETWEEN :startDate AND :endDate AND n.status = :status GROUP BY n.createdDate")
	List<Object[]> countByDateRangeWithStatus(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("status") int status);

	// Xoá bài viết có category_id liên quan khi xoá thể loại
	List<NewEntity> findByCategoryId(long categoryId);
}
