package com.example.project.repository;

import com.example.project.ennity.LikePost;
import com.example.project.ennity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikePostRepository extends JpaRepository<LikePost, Long> {

    LikePost findByUserIdUserAndPostId(Long idUser, Long id);

    LikePost findByUserIdUserAndCommentIdComment(Long iduser, Long id);
}
