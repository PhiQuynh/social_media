package com.example.project.controller;

import com.example.project.dto.AddComment;
import com.example.project.dto.UpdateComment;
import com.example.project.service.CommentServiceImpl;
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
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @InjectMocks @Spy
    private CommentController commentController;

    @Mock
    CommentServiceImpl commentService;

    @Test
    void addComment(){
        AddComment addComment = new AddComment();
        Mockito.when(commentService.addComment(Mockito.isA(AddComment.class))).thenReturn(new ResponseEntity<>(null, HttpStatus.CREATED));
        Assertions.assertEquals(HttpStatus.CREATED, commentController.addComment(addComment).getStatusCode());
    }

    @Test
    void updateComment(){

        UpdateComment updateComment = new UpdateComment();
        Mockito.when(commentService.updateComment(Mockito.isA(UpdateComment.class))).thenReturn(new ResponseEntity<>(null, HttpStatus.CREATED));
        Assertions.assertEquals(HttpStatus.CREATED, commentController.updateComment(updateComment).getStatusCode());

    }

    @Test
    void deleteComment() throws Exception {
        Long id_comment = 1L;
        Mockito.when(commentService.deleteComment(Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
        Assertions.assertEquals(HttpStatus.NO_CONTENT, commentController.deleteComment(id_comment).getStatusCode());

    }

    @Test
    void getCommentByPost() {
        Long id_post = 1L;
        Map<String, Object> objectMap = new HashMap<>();
        Mockito.when(commentService.getCommentByPost(Mockito.anyLong())).thenReturn(new ResponseEntity<>(objectMap, HttpStatus.CREATED));
        Assertions.assertEquals(HttpStatus.CREATED, commentController.getCommentByPost(id_post).getStatusCode());
    }

}