package com.example.project.service;

import com.example.project.config.Constants;
import com.example.project.config.jwt.AuthUserDetails;
import com.example.project.dto.AddPost;
import com.example.project.dto.FriendPost;
import com.example.project.dto.GetPost;
import com.example.project.dto.UserPost;
import com.example.project.ennity.FileInfo;
import com.example.project.ennity.Post;
import com.example.project.ennity.User;
import com.example.project.payload.ResponseSucces;
import com.example.project.repository.FileRepository;
import com.example.project.repository.PostRepository;
import com.example.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static javax.swing.text.html.HTML.Tag.U;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    FileRepository fileRepository;

    @Mock
    PostRepository postRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    PostServiceImpl postService;

    @Test
    void addPost_success_contentPost() {
        AddPost addPost = new AddPost();
        addPost.setContent_post("Test content");
        User user = new User();
        user.setUsername("username01@gmail.com");
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        when(postRepository.save(any(Post.class))).thenReturn(new Post());
        ResponseEntity<?> response = postService.addPost(addPost);
        assertNotNull(response);
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.SUCCCES_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.MESSAGE_SUCCES_ADDPOST, ((ResponseSucces) response.getBody()).getMessage());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Kiểm tra các phương thức đã được gọi trên các mock object repository
        Mockito.verify(SecurityContextHolder.getContext(), Mockito.times(1)).getAuthentication();
        Mockito.verify(authentication, Mockito.times(1)).getPrincipal();
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
        Mockito.verify(postRepository, Mockito.times(1)).save(any(Post.class));
    }

    @Test
    void addPost_success_image(){
        AddPost addPost = new AddPost();
        addPost.setName_file("4716d60a-b611-4c4b-bc8c-2409194a259a");
        User user = new User();
        user.setUsername("username01@gmail.com");
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        when(fileRepository.findByNameFile(Mockito.anyString())).thenReturn(new FileInfo());
        when(postRepository.save(any(Post.class))).thenReturn(new Post());
        ResponseEntity<?> response = postService.addPost(addPost);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.SUCCCES_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.MESSAGE_SUCCES_ADDPOST, ((ResponseSucces) response.getBody()).getMessage());
        // Kiểm tra các phương thức đã được gọi trên các mock object repository
        Mockito.verify(SecurityContextHolder.getContext(), Mockito.times(1)).getAuthentication();
        Mockito.verify(authentication, Mockito.times(1)).getPrincipal();
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
        Mockito.verify(fileRepository, Mockito.times(1)).findByNameFile(Mockito.anyString());
        Mockito.verify(postRepository, Mockito.times(1)).save(any(Post.class));
    }

    @Test
    void addPost_err(){
        AddPost addPost = new AddPost();
        User user = new User();
        user.setUsername("username01@gmail.com");
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        ResponseEntity<?> response = postService.addPost(addPost);
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.MESSAGE_ERR_ADDPOST, ((ResponseSucces) response.getBody()).getMessage());
        // Kiểm tra các phương thức đã được gọi trên các mock object repository
        Mockito.verifyNoInteractions(fileRepository);
        Mockito.verifyNoInteractions(postRepository);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void updatePost_ExitsPost() {
        Long postId = 1L;
        AddPost updatePost = new AddPost();
        updatePost.setContent_post("Updated content");
        Post existingPost = new Post();
        existingPost.setId(postId);
        when(postRepository.existsById(postId)).thenReturn(true);
        when(postRepository.findById(postId)).thenReturn(java.util.Optional.of(existingPost));
        when(fileRepository.findByNameFile(Mockito.anyString())).thenReturn(new FileInfo());
        when(postRepository.save(any(Post.class))).thenReturn(existingPost);
        ResponseEntity<?> response = postService.updatePost(updatePost, postId);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.SUCCCES_CODE,((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.MESSAGE_UPDATE_POST_SUCCES, ((ResponseSucces) response.getBody()).getMessage());
        // Kiểm tra các phương thức đã được gọi trên các mock object repository
        Mockito.verify(postRepository, Mockito.times(1)).existsById(postId);
        Mockito.verify(postRepository, Mockito.times(1)).findById(postId);
//        Mockito.verify(fileRepository, Mockito.times(1)).findByNameFile(Mockito.anyString());
        Mockito.verify(postRepository, Mockito.times(1)).save(any(Post.class));
    }

    @Test
    void updatePost_notExits(){
        Long postId = 1L;
        AddPost updatePost = new AddPost();
        updatePost.setContent_post("Updated content");
        when(postRepository.existsById(postId)).thenReturn(false);
        ResponseEntity<?> response = postService.updatePost(updatePost, postId);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.ERR_CODE,((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.NOT_FOUND_POST, ((ResponseSucces) response.getBody()).getMessage());
        // Kiểm tra các phương thức đã được gọi trên các mock object repository
        Mockito.verify(postRepository, Mockito.times(1)).existsById(postId);
        Mockito.verifyNoInteractions(fileRepository);
    }
    @Test
    void updatePost_Exception(){
        Long postId = 1L;
        AddPost updatePost = new AddPost();
        updatePost.setContent_post("Updated content");
        when(postRepository.existsById(postId)).thenReturn(true);
        when(postRepository.findById(postId)).thenThrow(new RuntimeException());
        ResponseEntity<?> response = postService.updatePost(updatePost, postId);
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        // Kiểm tra các phương thức đã được gọi trên các mock object repository
        Mockito.verify(postRepository, Mockito.times(1)).existsById(postId);
        Mockito.verify(postRepository, Mockito.times(1)).findById(postId);
        Mockito.verifyNoInteractions(fileRepository);
    }

    @Test
    void deletePost_ExitsPost() throws Exception {
        Long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(true);
        ResponseEntity<?> response = postService.deletePost(postId);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.SUCCCES_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.MESSAGE_DELETE_SUCCES, ((ResponseSucces) response.getBody()).getMessage());
        // Kiểm tra xem phương thức deleteById đã được gọi với đúng tham số
        Mockito.verify(postRepository, Mockito.times(1)).deleteById(postId);
    }

    @Test
    void deletePost_NotExits(){
        Long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(false);
        assertThrows(Exception.class, () -> postService.deletePost(postId));
        // Kiểm tra xem phương thức deleteById không được gọi
        Mockito.verify(postRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }

    @Test
    void test_timeline() {
        User user = new User();
        user.setIdUser(1L);
        user.setUsername("username01@gmail.com");
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        Long idUser = userDetails.getUser().getIdUser();
        Post post1 = new Post();
        post1.setId(1L);
        post1.setUser(new User());
        post1.setContent_post("Post 1 content");
        Post post2 = new Post();
        post2.setId(2L);
        post2.setUser(new User());
        post2.setContent_post("Post 2 content");
        List<Post> postFriendList = List.of(post1, post2);
        when(postRepository.findFriendPostByIdUser(idUser, idUser)).thenReturn(postFriendList);
        ResponseEntity<?> response = postService.timeline();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Kiểm tra giá trị trả về của response
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("200", responseBody.get("code"));
        assertEquals(postFriendList.size(), responseBody.get("count"));
        assertNotNull(responseBody.get("post"));
        List<FriendPost> postList = (List<FriendPost>) responseBody.get("post");
        assertEquals(postFriendList.size(), postList.size());
        // Kiểm tra xem method findFriendPostByIdUser đã được gọi đúng một lần với tham số là userId
        Mockito.verify(postRepository, Mockito.times(1)).findFriendPostByIdUser(idUser, idUser);
    }

    @Test
    public void test_getPost() {
        User user = new User();
        user.setIdUser(1L);
        Post post = new Post();
        post.setUser(new User());
        post.setId(1L);
        post.setContent_post("Test content");
        FriendPost friendPost = postService.getPost(post);
        assertNotNull(friendPost);
        assertEquals(post.getUser().getIdUser(), friendPost.getId_user());
        assertEquals(post.getId(), friendPost.getId_post());
        assertEquals(post.getContent_post(), friendPost.getContent_post());
    }

    @Test
    void getPostById_ExitsPost() throws Exception {
        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);
        when(postRepository.existsById(postId)).thenReturn(true);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        GetPost result = postService.getPostById(postId);
        assertNotNull(result);
        assertEquals(post.getContent_post(), result.getContent_post());
        // Kiểm tra xem method existsById đã được gọi với đúng tham số
        Mockito.verify(postRepository, Mockito.times(1)).existsById(postId);
        // Kiểm tra xem method findById đã được gọi với đúng tham số
        Mockito.verify(postRepository, Mockito.times(1)).findById(postId);
    }

    @Test
    void getPostById_NotExitsPost(){
        Long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(false);
        assertThrows(Exception.class, () -> postService.getPostById(postId));
        // Kiểm tra xem method existsById đã được gọi với đúng tham số
        Mockito.verify(postRepository, Mockito.times(1)).existsById(postId);
        // Kiểm tra xem method findById không được gọi
        Mockito.verify(postRepository, Mockito.never()).findById(Mockito.anyLong());
    }

    @Test
    void test_getPosst(){
        User user = new User();
        user.setIdUser(1L);
        Post post = new Post();
        post.setUser(new User());
        post.setContent_post("Test content");
        GetPost getPost = postService.getPosst(post);
        assertNotNull(getPost);
        assertEquals(post.getUser().getIdUser(), getPost.getId_user());
        assertEquals(post.getContent_post(), getPost.getContent_post());
    }

    @Test
    void test_load() {

    }

    @Test
    void test_getPostByUser(){
        User user = new User();
        user.setIdUser(1L);
        user.setUsername("username01@gmail.com");
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        UserPost post1 = new UserPost();
        post1.setId_post(1L);
        post1.setContent_post("Post 1 content");
        UserPost post2 = new UserPost();
        post2.setId_post(2L);
        post2.setContent_post("Post 2 content");
        List<Post> postFriendList = List.of();
        List<UserPost> userPostList = List.of(post1, post2);
        when(userRepository.findByUsername(userDetails.getUsername())).thenReturn(user);
        when(postRepository.listPostByIdUser(userDetails.getUser().getIdUser())).thenReturn(postFriendList);
        ResponseEntity<?> response = postService.getPostByUser();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("200", responseBody.get("code"));
        assertEquals(postFriendList.size(), responseBody.get("count"));
        assertNotNull(responseBody.get("post"));
        List<UserPost> postList = (List<UserPost>) responseBody.get("post");
        assertEquals(postFriendList.size(), postList.size());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(userDetails.getUsername());
        Mockito.verify(postRepository, Mockito.times(1)).listPostByIdUser(userDetails.getUser().getIdUser());
    }

    @Test
    void test_userPost(){
        Post post = new Post();
        post.setId(1L);
        post.setContent_post("Test content");
        UserPost userPost = postService.userPost(post);
        assertNotNull(userPost);
        assertEquals(post.getId(), userPost.getId_post());
        assertEquals(post.getContent_post(), userPost.getContent_post());
        assertNull(userPost.getName_file());
    }
}