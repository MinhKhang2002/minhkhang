package com.laptrinhjavaweb.service;

import com.laptrinhjavaweb.dto.CommentDTO;
import com.laptrinhjavaweb.entity.CommentEntity;

import java.util.List;

public interface ICommentService {

    CommentEntity createComment(CommentDTO commentDTO);

    CommentDTO getCommentById(Long commentId);
    List<CommentDTO> getAllCommentByNewsId(Long newId);
}

