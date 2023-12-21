package com.example.project.service;

import com.example.project.config.Constants;
import com.example.project.config.jwt.AuthUserDetails;
import com.example.project.dto.AddComment;
import com.example.project.dto.GetComment;
import com.example.project.dto.UpdateComment;
import com.example.project.ennity.Comment;
import com.example.project.ennity.Post;
import com.example.project.payload.ResponseSucces;
import com.example.project.repository.CommentRepository;
import com.example.project.repository.PostRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl {
    public final PostRepository postRepository;
    public final CommentRepository commentRepository;
    public final UserRepository userRepository;


    @Transactional
    public ResponseEntity<?> addComment(AddComment addComment ){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
            Comment comment = new Comment();
            comment.setContent_comment(addComment.getContent_comment());
            Post post = postRepository.findById(addComment.getId_post()).orElseThrow();
            comment.setPost(post);
            comment.setUser(userRepository.findByUsername(userDetails.getUsername()));
            commentRepository.save(comment);
            return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, Constants.MESSAGE_SUCCES_COMMENT), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseSucces(Constants.ERR_CODE, Constants.MESSAGE_ERR_COMMENT), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<?> updateComment(UpdateComment updateComment) {
        try {
            if(commentRepository.existsById(updateComment.getId_comment())){
                return new ResponseEntity<>(new ResponseSucces(Constants.ERR_CODE, Constants.MESSAGE_ERR_UPDATE_COMMENT), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Comment comment = commentRepository.findById(updateComment.getId_comment()).orElseThrow();
            comment.setContent_comment(updateComment.getContent_comment());
            commentRepository.save(comment);
            return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, Constants.MESSAGE_SUCCES_UPDATE_COMMENT), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseSucces(Constants.ERR_CODE, Constants.MESSAGE_ERR_UPDATE_COMMENT), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteComment(Long id_comment) throws Exception{
            if(commentRepository.existsById(id_comment)){

            } else  {
                throw new Exception(Constants.ERR_MESSAGE);
            }
            commentRepository.deleteById(id_comment);
            return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, Constants.MESSAGE_DELETE_SUCCES),HttpStatus.OK);

    }
    @Transactional
    public ResponseEntity<Map<String, Object>> getCommentByPost(Long id_post) {
        Map<String, Object> reponse = new HashMap<>();
        List<Comment> comments = commentRepository.findByPostId(id_post);
        if(postRepository.existsById(id_post)){

        } else {
            new ResponseEntity<>(new ResponseSucces(Constants.ERR_CODE, Constants.EXITS_POST), HttpStatus.BAD_REQUEST);
        }
        List<GetComment> commentList = comments.stream().map(
                comment -> getComment(comment)
        ).toList();
        reponse.put("code","200");
        reponse.put("count", commentList.size());
        reponse.put("post", commentList);

        return new ResponseEntity<Map<String, Object>>(reponse, HttpStatus.OK);
    }
    private GetComment getComment (Comment comment) {
        GetComment.GetCommentBuilder builder = GetComment.builder()
                .content_comment(comment.getContent_comment())
                .id_comment(comment.getIdComment())
                .id_user(comment.getUser().getIdUser());

        return builder.build();
    }


}
