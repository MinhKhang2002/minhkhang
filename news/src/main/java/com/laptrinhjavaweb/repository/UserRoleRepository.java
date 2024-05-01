package com.laptrinhjavaweb.repository;

import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    List<UserRoleEntity> findByUserId(Long userId);

    Long findRoleIdByUserId(long userId);

    void deleteByUserId(long userId);
}
