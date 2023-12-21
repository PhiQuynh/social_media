package com.example.project.service;

import com.example.project.config.Constants;
import com.example.project.config.jwt.AuthUserDetails;
import com.example.project.dto.AddLikeComment;
import com.example.project.dto.AddLikePost;
import com.example.project.ennity.Comment;
import com.example.project.ennity.LikePost;
import com.example.project.ennity.Post;
import com.example.project.payload.ResponseSucces;
import com.example.project.repository.CommentRepository;
import com.example.project.repository.LikePostRepository;
import com.example.project.repository.PostRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class  LikePostServiceImpl {
    public final UserRepository userRepository;
    public final PostRepository postRepository;
    public final CommentRepository commentRepository;
    public final LikePostRepository likePostRepository;

        public ResponseEntity<?> addLikePost(AddLikePost addLikePost) {
            // Lấy thông tin xác thực và người dùng hiện tại từ SecurityContextHolder
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
            // Kiểm tra sự tồn tại của id_post
            Post post = postRepository.findById(addLikePost.getId_post()).orElse(null);
            if (post != null) {
                // Kiểm tra xem người dùng đã thích bài viết chưa
                LikePost existingLike = likePostRepository.findByUserIdUserAndPostId(userDetails.getUser().getIdUser(), post.getId());
                if (existingLike != null) {
//                    // Người dùng đã thích bài viết, chuyển sang unlike
                    if (existingLike.getLike_post().equals("like")) {
                        // Nếu đang là trạng thái "like" và người dùng ấn lần nữa, chuyển sang "unlike"
                        existingLike.setLike_post("unlike");
                        likePostRepository.save(existingLike);
                        return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, "Bạn không thích bài viết này?"), HttpStatus.OK);
                    } else {
                        // Nếu đang là trạng thái "unlike" và người dùng ấn lần nữa, chuyển sang "like"
                        existingLike.setLike_post("like");
                        likePostRepository.save(existingLike);
                        return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, "Bạn đã thích bài viết này!"), HttpStatus.OK);
                    }
                } else {
                    // Người dùng chưa thích bài viết, thêm lượt thích mới
                    LikePost newLike = new LikePost();
                    newLike.setUser(userDetails.getUser());
                    newLike.setPost(post);
                    newLike.setLike_post("like");
                    likePostRepository.save(newLike);
                    return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, "Bạn đã thích bài viết này!"), HttpStatus.OK);
                }
            } else {
                // Bài viết không tồn tại
                return new ResponseEntity<>(new ResponseSucces(Constants.ERR_CODE, "Bài viết không tồn tại"), HttpStatus.BAD_REQUEST);
            }
        }
    public ResponseEntity<?> addLikeComment(AddLikeComment likeComment) {
        // Lấy thông tin xác thực và người dùng hiện tại từ SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        // Kiểm tra sự tồn tại của id_comment
        Comment comment = commentRepository.findById(likeComment.getId_comment()).orElse(null);
        if (comment != null) {
            // Kiểm tra xem người dùng đã thích comment chưa
            LikePost existingLike = likePostRepository.findByUserIdUserAndCommentIdComment(userDetails.getUser().getIdUser(), comment.getIdComment());
            if (existingLike != null) {
//                    // Người dùng đã thích comment, chuyển sang unlike
                if (existingLike.getLike_post().equals("like")) {
                    // Nếu đang là trạng thái "like" và người dùng ấn lần nữa, chuyển sang "unlike"
                    existingLike.setLike_post("unlike");
                    likePostRepository.save(existingLike);
                    return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, "Bạn không thích comment này?"), HttpStatus.OK);
                } else {
                    // Nếu đang là trạng thái "unlike" và người dùng ấn lần nữa, chuyển sang "like"
                    existingLike.setLike_post("like");
                    likePostRepository.save(existingLike);
                    return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, "Bạn đã thích comment này!"), HttpStatus.OK);
                }
            } else {
                // Người dùng chưa thích bài viết, thêm lượt thích mới
                LikePost newLike = new LikePost();
                newLike.setUser(userDetails.getUser());
                newLike.setComment(comment);
                newLike.setLike_post("like");
                likePostRepository.save(newLike);
                return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, "Bạn đã thích comment này!"), HttpStatus.OK);
            }
        } else {
            // Bài viết không tồn tại
            return new ResponseEntity<>(new ResponseSucces(Constants.ERR_CODE, "Comment này không tồn tại"), HttpStatus.BAD_REQUEST);
        }
    }
}
