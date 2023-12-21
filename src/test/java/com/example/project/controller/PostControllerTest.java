package com.example.project.controller;
import com.example.project.dto.AddPost;
import com.example.project.service.PostServiceImpl;

import com.example.project.dto.GetPost;
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
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {
    @Mock
    PostServiceImpl postService;

    @InjectMocks @Spy
    PostController postController;

    @Test
    void addPost() {
        AddPost addPost = new AddPost();
        Mockito.when(postService.addPost(Mockito.isA(AddPost.class))).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
        Assertions.assertEquals(HttpStatus.OK, postController.addPost(addPost).getStatusCode());
    }

    @Test
    void updatePost() {
        AddPost updatePost = new AddPost();
        Mockito.when(postService.updatePost(Mockito.isA(AddPost.class), Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
        Assertions.assertEquals(HttpStatus.OK, postController.updatePost(1L, updatePost).getStatusCode());
    }

    @Test
    void timeline() {
        Mockito.when(postService.timeline()).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
        Assertions.assertEquals(HttpStatus.OK, postController.timeline().getStatusCode());
    }

    @Test
    void getPostById() throws Exception {
        GetPost getPost = new GetPost();
        getPost.setName_file("abc.doc");
        getPost.setId_user(1L);
        Mockito.when(postService.getPostById(Mockito.anyLong())).thenReturn(getPost);
        Assertions.assertEquals(1L, postController.getPostById(1L).getId_user());
    }

    @Test
    void deletePost() throws Exception {
        Mockito.when(postService.deletePost(Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
        Assertions.assertEquals(HttpStatus.OK, postController.deletePost(1L).getStatusCode());
    }
}