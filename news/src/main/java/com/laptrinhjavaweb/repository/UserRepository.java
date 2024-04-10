package com.laptrinhjavaweb.repository;

import com.laptrinhjavaweb.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserName(String userName);
    UserEntity findByUserNameAndPassword(String userName, String password);

    @Query("SELECT ur.roleId FROM UserRoleEntity ur WHERE ur.userId = :userId")
    Long findRoleIdByUserId(@Param("userId") Long userId);

    boolean existsByUserName(String username);
}