package com.example.project.service;

import com.example.project.config.Constants;
import com.example.project.config.jwt.AuthUserDetails;
import com.example.project.dto.AddComment;
import com.example.project.dto.UpdateComment;
import com.example.project.ennity.Comment;
import com.example.project.ennity.Post;
import com.example.project.ennity.User;
import com.example.project.payload.ResponseSucces;
import com.example.project.repository.CommentRepository;
import com.example.project.repository.PostRepository;
import com.example.project.repository.UserRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks @Spy
    CommentServiceImpl commentService;

    @Test
    void test_addComment_success() {
        User user = new User();
        user.setUsername("abc");
        user.setIdUser(1L);
        AuthUserDetails authUserDetails = new AuthUserDetails();
        authUserDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        AddComment addComment = new AddComment();
        addComment.setContent_comment("abc");
        addComment.setId_post(1L);
        Post post = new Post();
        post.setId(1L);
        Comment comment = new Comment();
        when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(post));
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        when(commentRepository.save(Mockito.isA(Comment.class))).thenReturn(comment);
        Assertions.assertEquals(HttpStatus.OK, commentService.addComment(addComment).getStatusCode());
    }

    @Test
    void testAddComment_err() {
        AddComment addComment = new AddComment();
        addComment.setId_post(1L);
        addComment.setContent_comment("abc");
        ResponseEntity<?> response = commentService.addComment(addComment);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Constants.ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.MESSAGE_ERR_COMMENT, ((ResponseSucces) response.getBody()).getMessage());
    }

    @Test
    void testupdateComment_success() {
        UpdateComment updateComment = new UpdateComment();
        Comment comment = new Comment();
        comment.setIdComment(1L);
        comment.setContent_comment("abc");
        Mockito.when(commentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(comment));
        when(commentRepository.save(Mockito.isA(Comment.class))).thenReturn(comment);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, commentService.updateComment(updateComment).getStatusCode());
    }

    @Test
    void testUpdateComment_NotFound() {
        UpdateComment updateComment = new UpdateComment();
        updateComment.setId_comment(1L);
        updateComment.setContent_comment("abc");
        when(commentRepository.existsById(Mockito.anyLong())).thenReturn(true);
        ResponseEntity<?> response = commentService.updateComment(updateComment);
        verify(commentRepository, times(1)).existsById(updateComment.getId_comment());
        verify(commentRepository, never()).findById(updateComment.getId_comment());
        verify(commentRepository, never()).save(any(Comment.class));
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.MESSAGE_ERR_UPDATE_COMMENT, ((ResponseSucces) response.getBody()).getMessage());
    }

    @Test
    void testUpdateComment_Exception(){
        UpdateComment updateComment = new UpdateComment();
        updateComment.setId_comment(1L);
        updateComment.setContent_comment("abc");
        when(commentRepository.existsById(updateComment.getId_comment())).thenReturn(false);
        when(commentRepository.findById(Mockito.anyLong())).thenThrow(new RuntimeException("Don't find by id"));
        ResponseEntity<?> response = commentService.updateComment(updateComment);
        verify(commentRepository, times(1)).existsById(updateComment.getId_comment());
        verify(commentRepository, times(1)).findById(updateComment.getId_comment());
        verify(commentRepository, never()).save(any(Comment.class));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.MESSAGE_ERR_UPDATE_COMMENT,((ResponseSucces) response.getBody()).getMessage());
    }

    @Test
    void testDeleteComment_sucess() throws Exception {
        Long id_comment = 1L;
        when(commentRepository.existsById(Mockito.anyLong())).thenReturn(true);
        ResponseEntity response = commentService.deleteComment(id_comment);
        verify(commentRepository, times(1)).existsById(id_comment);
        verify(commentRepository, times(1)).deleteById(id_comment);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.SUCCCES_CODE, ( ((ResponseSucces) response.getBody()).getCode()));
        assertEquals(Constants.MESSAGE_DELETE_SUCCES, ((ResponseSucces) response.getBody()).getMessage());
    }

    @Test
    void testDeleteComment_err() {
        Long id_comment = 1L;
        when(commentRepository.existsById(id_comment)).thenReturn(false);
        assertThrows(Exception.class, () -> commentService.deleteComment(id_comment));
        verify(commentRepository, times(1)).existsById(id_comment);
        verify(commentRepository, never()).deleteById(id_comment);
    }

    @Test
    void getCommentByPost_success() {
        Long id_post = 1L ;
        Map<String, Object> reponse = new HashMap<>();
        List<Comment> commentList = commentRepository.findByPostId(id_post);

    }

}