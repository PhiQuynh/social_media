package com.example.project.service;

import com.example.project.config.Constants;
import com.example.project.config.jwt.AuthUserDetails;
import com.example.project.dto.AddLikeComment;
import com.example.project.dto.AddLikePost;
import com.example.project.dto.UpdateComment;
import com.example.project.dto.UpdateUser;
import com.example.project.ennity.*;
import com.example.project.payload.ResponseSucces;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.project.repository.CommentRepository;
import com.example.project.repository.LikePostRepository;
import com.example.project.repository.PostRepository;
import com.example.project.repository.UserRepository;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikePostServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PostRepository postRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    LikePostRepository likePostRepository;

    @InjectMocks
    LikePostServiceImpl likePostService;

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void addLikePost_Like_success() {
        Post post = new Post();
        post.setId(1L);
        AddLikePost likePost = new AddLikePost();
        likePost.setLike_post("like");
        likePost.setId_post(1L);
        User user = new User();
        user.setUsername("username01@gmail.com");
        user.setIdUser(1L);
        LikePost like = new LikePost();
        like.setLike_post("unlike");
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(likePostRepository.findByUserIdUserAndPostId(user.getIdUser(), post.getId())).thenReturn(like);
        ResponseEntity<?> response = likePostService.addLikePost(likePost);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.SUCCCES_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals("Bạn đã thích bài viết này!", ((ResponseSucces) response.getBody()).getMessage());
        // Kiểm tra các phương thức đã được gọi trên các mock object repository
        Mockito.verify(SecurityContextHolder.getContext(), Mockito.times(1)).getAuthentication();
        Mockito.verify(authentication, Mockito.times(1)).getPrincipal();
//        Mockito.verify(userDetails, Mockito.times(1)).getUser().getIdUser();
        Mockito.verify(postRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(likePostRepository, Mockito.times(1)).findByUserIdUserAndPostId(anyLong(), anyLong());
        Mockito.verify(likePostRepository, Mockito.times(1)).save(like);
    }

    @Test
    void addLikePost_Unlike_success() {
        Post post = new Post();
        post.setId(1L);
        AddLikePost likePost = new AddLikePost();
        likePost.setLike_post("unlike");
        likePost.setId_post(1L);
        User user = new User();
        user.setUsername("username01@gmail.com");
        user.setIdUser(1L);
        LikePost existingLike = new LikePost();
        existingLike.setLike_post("like");
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(postRepository.findById(null)).thenReturn(Optional.of(post));
        when(likePostRepository.findByUserIdUserAndPostId(anyLong(), anyLong())).thenReturn(existingLike);
        ResponseEntity<?> response = likePostService.addLikePost(new AddLikePost());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.SUCCCES_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals("Bạn không thích bài viết này?", ((ResponseSucces) response.getBody()).getMessage());
        // Kiểm tra các phương thức đã được gọi trên các mock object repository
        Mockito.verify(SecurityContextHolder.getContext(), Mockito.times(1)).getAuthentication();
        Mockito.verify(authentication, Mockito.times(1)).getPrincipal();
        Mockito.verify(postRepository, Mockito.times(1)).findById(null);
        Mockito.verify(likePostRepository, Mockito.times(1)).findByUserIdUserAndPostId(anyLong(), anyLong());
        Mockito.verify(likePostRepository, Mockito.times(1)).save(existingLike);
    }

    @Test
    public void testAddLikePost_NotExistingPost() {
        Post post = new Post();
        post.setId(1L);
        User user = new User();
        user.setUsername("username01@gmail.com");
        user.setIdUser(1L);
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(postRepository.findById(null)).thenReturn(Optional.empty());
        ResponseEntity<?> response = likePostService.addLikePost(new AddLikePost());
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals("Bài viết không tồn tại", ((ResponseSucces) response.getBody()).getMessage());
        // Kiểm tra các phương thức đã được gọi trên các mock object repository
        Mockito.verify(postRepository, Mockito.times(1)).findById(null);
    }

    @Test
    public void testAddLikePost_NewLike() {
        Post post = new Post();
        post.setId(1L);
        User user = new User();
        user.setUsername("username01@gmail.com");
        user.setIdUser(1L);
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(postRepository.findById(null)).thenReturn(Optional.of(post));
        when(likePostRepository.findByUserIdUserAndPostId(anyLong(), anyLong())).thenReturn(null);
        ResponseEntity<?> response = likePostService.addLikePost(new AddLikePost());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Kiểm tra các phương thức đã được gọi trên các mock object repository
        Mockito.verify(SecurityContextHolder.getContext(), Mockito.times(1)).getAuthentication();
        Mockito.verify(authentication, Mockito.times(1)).getPrincipal();
//        Mockito.verify(userDetails, Mockito.times(1)).getUser();
        Mockito.verify(postRepository, Mockito.times(1)).findById(null);
        Mockito.verify(likePostRepository, Mockito.times(1)).findByUserIdUserAndPostId(anyLong(), anyLong());
        Mockito.verify(likePostRepository, Mockito.times(1)).save(any(LikePost.class));
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
        void addLikeComment_Like() {
        AddLikeComment likeComment = new AddLikeComment();
        likeComment.setId_comment(1L);
        likeComment.setLike_post("like");
        Comment comment = new Comment();
        comment.setIdComment(1L);
        User user = new User();
        user.setUsername("username01@gmail.com");
        user.setIdUser(1L);
        LikePost likePost = new LikePost();
        likePost.setLike_post("unlike");
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(comment));
        when(likePostRepository.findByUserIdUserAndCommentIdComment(Mockito.anyLong(), Mockito.anyLong())).thenReturn(likePost);
        ResponseEntity<?> response = likePostService.addLikeComment(likeComment);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.SUCCCES_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals("Bạn đã thích comment này!", ((ResponseSucces) response.getBody()).getMessage());
        // Kiểm tra các phương thức đã được gọi trên các mock object repository
        Mockito.verify(SecurityContextHolder.getContext(), Mockito.times(1)).getAuthentication();
        Mockito.verify(authentication, Mockito.times(1)).getPrincipal();
        Mockito.verify(commentRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(likePostRepository, Mockito.times(1)).findByUserIdUserAndCommentIdComment(anyLong(), anyLong());
        Mockito.verify(likePostRepository, Mockito.times(1)).save(likePost);
    }

    @Test
    void addLikeComment_unlike() {
        AddLikeComment likeComment = new AddLikeComment();
        likeComment.setId_comment(1L);
        likeComment.setLike_post("unlike");
        Comment comment = new Comment();
        comment.setIdComment(1L);
        User user = new User();
        user.setUsername("username01@gmail.com");
        user.setIdUser(1L);
        LikePost likePost = new LikePost();
        likePost.setLike_post("like");
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(comment));
        when(likePostRepository.findByUserIdUserAndCommentIdComment(Mockito.anyLong(), Mockito.anyLong())).thenReturn(likePost);
        ResponseEntity<?> response = likePostService.addLikeComment(likeComment);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.SUCCCES_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals("Bạn không thích comment này?", ((ResponseSucces) response.getBody()).getMessage());
        // Kiểm tra các phương thức đã được gọi trên các mock object repository
        Mockito.verify(SecurityContextHolder.getContext(), Mockito.times(1)).getAuthentication();
        Mockito.verify(authentication, Mockito.times(1)).getPrincipal();
        Mockito.verify(commentRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(likePostRepository, Mockito.times(1)).findByUserIdUserAndCommentIdComment(anyLong(), anyLong());
        Mockito.verify(likePostRepository, Mockito.times(1)).save(likePost);
    }

    @Test
    public void testAddLikeCommnent_NotExistingCommnent() {
        Post post = new Post();
        post.setId(1L);
        User user = new User();
        user.setUsername("username01@gmail.com");
        user.setIdUser(1L);
        AddLikeComment likeComment = new AddLikeComment();
        likeComment.setId_comment(1L);
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        ResponseEntity<?> response = likePostService.addLikeComment(likeComment);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals("Comment này không tồn tại", ((ResponseSucces) response.getBody()).getMessage());
        // Kiểm tra các phương thức đã được gọi trên các mock object repository
        Mockito.verify(commentRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }
    @Test
    public void testAddLikeComment_NewLike() {
        Comment comment = new Comment();
        comment.setIdComment(1L);
        User user = new User();
        user.setUsername("username01@gmail.com");
        user.setIdUser(1L);
        AddLikeComment likeComment = new AddLikeComment();
        likeComment.setId_comment(1L);
        likeComment.setLike_post("like");
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(comment));
        when(likePostRepository.findByUserIdUserAndCommentIdComment(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
        ResponseEntity<?> response = likePostService.addLikeComment(likeComment);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.SUCCCES_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals("Bạn đã thích comment này!", ((ResponseSucces) response.getBody()).getMessage());
        // Kiểm tra các phương thức đã được gọi trên các mock object repository
        Mockito.verify(SecurityContextHolder.getContext(), Mockito.times(1)).getAuthentication();
        Mockito.verify(authentication, Mockito.times(1)).getPrincipal();
        Mockito.verify(commentRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(likePostRepository, Mockito.times(1)).findByUserIdUserAndCommentIdComment(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(likePostRepository, Mockito.times(1)).save(any(LikePost.class));
    }
}