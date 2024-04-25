package com.laptrinhjavaweb.api;

import com.laptrinhjavaweb.dto.CommentDTO;
import com.laptrinhjavaweb.dto.UserDTO;
import com.laptrinhjavaweb.entity.CommentEntity;
import com.laptrinhjavaweb.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentAPI {

    @Autowired
    private ICommentService commentService;

    @PostMapping
    public String createComment(@RequestBody CommentDTO commentDTO) {
        commentService.createComment(commentDTO);
        return "post thành cong";
    }
    @GetMapping("/{newId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByNewsId(@PathVariable Long newId) {
        List<CommentDTO> comments = commentService.getAllCommentByNewsId(newId);
        return ResponseEntity.ok(comments);
    }
}
