package com.example.project.repository;

import com.example.project.ennity.Comment;
import com.example.project.ennity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
//   @Query(value = "SELECT c.id_comment, c.content_comment, c.id_user \n" +
//           "           FROM comment as c left join post as p on (c.id_post = p.id_post) \n" +
//           "           WHERE p.id_post = ?1", nativeQuery = true)
//   List<Comment> getCommentsByPostId(Long id_post);
   List<Comment> findByPostId(Long id_post);


}
