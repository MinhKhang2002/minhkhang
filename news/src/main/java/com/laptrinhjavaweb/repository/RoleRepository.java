package com.laptrinhjavaweb.repository;

import com.laptrinhjavaweb.entity.NewEntity;
import com.laptrinhjavaweb.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
