package com.laptrinhjavaweb.converter;

import com.laptrinhjavaweb.dto.CategoryDTO;
import com.laptrinhjavaweb.dto.CommentDTO;
import com.laptrinhjavaweb.entity.CommentEntity;
import com.laptrinhjavaweb.entity.NewEntity;
import com.laptrinhjavaweb.entity.UserEntity;
import com.laptrinhjavaweb.repository.NewRepository;
import com.laptrinhjavaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    @Autowired
    private UserRepository userRepository; // Assume you have a UserRepository to fetch UserEntity
    @Autowired
    private NewRepository newRepository; // Assume you have a NewRepository to fetch NewEntity

    public CommentEntity toEntity(CommentDTO commentDTO) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setContent_comment(commentDTO.getContent_comment());

        UserEntity userEntity = userRepository.findById(commentDTO.getUserId()).orElse(null);
        commentEntity.setUser(userEntity);

        NewEntity newEntity = newRepository.findById(commentDTO.getNewId()).orElse(null);
        commentEntity.setNews(newEntity);

        return commentEntity;
    }

    public CommentDTO toDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        // Các phần chuyển đổi từ Entity sang DTO
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCreatedDate(commentEntity.getCreatedDate());
        commentDTO.setContent_comment(commentEntity.getContent_comment());
        commentDTO.setUserId(commentEntity.getUser().getId());
        commentDTO.setNewId(commentEntity.getNews().getId());
        return commentDTO;
    }
}

