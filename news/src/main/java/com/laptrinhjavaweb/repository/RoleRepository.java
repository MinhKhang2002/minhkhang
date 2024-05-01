package com.laptrinhjavaweb.repository;

import com.laptrinhjavaweb.entity.NewEntity;
import com.laptrinhjavaweb.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    List<RoleEntity> findByCategoriesContaining(String categoryCode);
}
