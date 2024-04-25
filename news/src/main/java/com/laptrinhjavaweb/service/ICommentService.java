package com.laptrinhjavaweb.service;

import com.laptrinhjavaweb.dto.CommentDTO;
import com.laptrinhjavaweb.entity.CommentEntity;

import java.util.List;

public interface ICommentService {

    void createComment(CommentDTO commentDTO);

    List<CommentDTO> getAllCommentByNewsId(Long newId);
}

