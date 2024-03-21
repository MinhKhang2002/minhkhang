package com.laptrinhjavaweb.repository;

import com.laptrinhjavaweb.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

}
