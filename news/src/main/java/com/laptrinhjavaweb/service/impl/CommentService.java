package com.laptrinhjavaweb.service.impl;

import com.laptrinhjavaweb.converter.CommentConverter;
import com.laptrinhjavaweb.dto.CommentDTO;
import com.laptrinhjavaweb.entity.CommentEntity;
import com.laptrinhjavaweb.repository.CommentRepository;
import com.laptrinhjavaweb.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentConverter commentConverter;

    @Override
    public CommentEntity createComment(CommentDTO commentDTO) {
        CommentEntity commentEntity = commentConverter.toEntity(commentDTO);
        return commentRepository.save(commentEntity);
    }

    @Override

    public CommentDTO getCommentById(Long commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy comment với id: " + commentId));
        return commentConverter.toDTO(commentEntity);
    }

    @Override
    public List<CommentDTO> getAllCommentByNewsId(Long newId) {
        List<CommentEntity> commentEntities = commentRepository.getAllCommentByNewsId(newId);
        return commentEntities.stream().map(commentConverter::toDTO).collect(Collectors.toList());
    }
}

