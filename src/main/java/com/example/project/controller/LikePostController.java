package com.example.project.controller;

import com.example.project.dto.AddLikeComment;
import com.example.project.dto.AddLikePost;
import com.example.project.service.LikePostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/like")
public class LikePostController {
    @Autowired
    public LikePostServiceImpl likePostService;

    @PostMapping("/comment")
    public ResponseEntity<?> addLikeComment(@RequestBody AddLikeComment addLikeComment) {
        return likePostService.addLikeComment(addLikeComment);
    }

    @PostMapping("/post")
    public ResponseEntity<?> addLikePost(@RequestBody AddLikePost addLikePost){
        return likePostService.addLikePost(addLikePost);
    }
}
