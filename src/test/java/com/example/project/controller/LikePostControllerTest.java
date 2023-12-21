package com.example.project.controller;

import com.example.project.dto.AddLikeComment;
import com.example.project.dto.AddLikePost;
import com.example.project.service.LikePostServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LikePostControllerTest {

    @InjectMocks @Spy
    LikePostController likePostController;

    @Mock
    LikePostServiceImpl likePostService;

    @Test
    void addLikeComment() {
        AddLikeComment addLikeComment = new AddLikeComment();
        Mockito.when(likePostService.addLikeComment(Mockito.isA(AddLikeComment.class))).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
        Assertions.assertEquals(HttpStatus.OK, likePostController.addLikeComment(addLikeComment).getStatusCode());
    }

    @Test
    void addLikePost() {
        AddLikePost addLikePost = new AddLikePost();
        Mockito.when(likePostService.addLikePost(Mockito.isA(AddLikePost.class))).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
        Assertions.assertEquals(HttpStatus.OK, likePostController.addLikePost(addLikePost).getStatusCode());
    }
}