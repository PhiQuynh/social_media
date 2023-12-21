package com.example.project.controller;

import com.example.project.dto.AddPost;
import com.example.project.dto.GetPost;
import com.example.project.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    public final PostServiceImpl postService;

    @PostMapping
    public ResponseEntity<?> addPost(@RequestBody AddPost addPost) {
        return postService.addPost(addPost);
    }

    @PutMapping("/{id_post}")
    public ResponseEntity<?> updatePost(@PathVariable Long id_post, @RequestBody AddPost updatePost){
        return postService.updatePost(updatePost, id_post);
    }
    @GetMapping("/timeline")
    public ResponseEntity<?> timeline(){
        return postService.timeline();
    }

    @GetMapping("/{id_post}")
    public GetPost getPostById(@PathVariable Long id_post) throws Exception {
        return postService.getPostById(id_post);
    }

    @DeleteMapping("/{id_post}")
    public ResponseEntity<?> deletePost(@PathVariable Long id_post) throws Exception{
        return postService.deletePost(id_post);
    }

    @GetMapping("image/{id_post}")
    public ResponseEntity<?> getPostImageById(@PathVariable Long id_post) {
            Resource imageResource = postService.load(id_post);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Thay đổi MediaType tùy thuộc vào loại ảnh

            return new ResponseEntity<>(imageResource, headers, HttpStatus.OK);
    }

    @GetMapping("/getPostByUser")
    public ResponseEntity<?> getPostByUser() {
        return postService.getPostByUser();
    }
}
