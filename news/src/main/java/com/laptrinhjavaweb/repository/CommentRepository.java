package com.laptrinhjavaweb.repository;

import com.laptrinhjavaweb.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> getAllCommentByNewsId(Long newId);
    // Đây là nơi bạn có thể thêm các phương thức tùy chỉnh cho repository của bạn nếu cần.

}
