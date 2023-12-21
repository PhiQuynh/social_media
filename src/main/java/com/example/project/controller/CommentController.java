package com.example.project.controller;

import com.example.project.dto.AddComment;
import com.example.project.dto.GetComment;
import com.example.project.dto.GetPost;
import com.example.project.dto.UpdateComment;
import com.example.project.ennity.Comment;
import com.example.project.service.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    @Autowired
    CommentServiceImpl commentService;

    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody AddComment addComment){
        return commentService.addComment(addComment);
    }

    @PutMapping
    public ResponseEntity<?> updateComment(@RequestBody UpdateComment updateComment){
        return commentService.updateComment(updateComment);
    }

    @DeleteMapping("/{id_comment}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id_comment) throws Exception{
        return commentService.deleteComment(id_comment);
    }

    @GetMapping("/getCommentByPost")
    public ResponseEntity<Map<String, Object>> getCommentByPost(@RequestParam Long id_post){
        return commentService.getCommentByPost(id_post);
    }
}
