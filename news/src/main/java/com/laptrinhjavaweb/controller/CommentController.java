package com.laptrinhjavaweb.controller;

import com.laptrinhjavaweb.dto.CommentDTO;
import com.laptrinhjavaweb.dto.UserDTO;
import com.laptrinhjavaweb.entity.CommentEntity;
import com.laptrinhjavaweb.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @PostMapping("/comments")
    public CommentEntity createComment(@RequestBody CommentDTO commentDTO) {
        return commentService.createComment(commentDTO);
    }
    @GetMapping("/comment/{newId}")
    public List<CommentDTO> showALL(@PathVariable Long newId) {
        return commentService.getAllCommentByNewsId(newId);
    }

}
