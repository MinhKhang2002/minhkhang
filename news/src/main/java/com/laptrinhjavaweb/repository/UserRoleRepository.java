package com.laptrinhjavaweb.repository;

import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
}
