package com.example.project.repository;

import com.example.project.ennity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT p.content_post, p.id_post,p.id_user, p.name_file,f.id_friend\n" +
            "            FROM post as p left join friend as f on (p.id_user = f.id_friend OR p.id_user = f.id_user)\n" +
            "            WHERE (f.id_user = ? AND f.status = 'ACCEPTED') OR (f.id_friend = ?  AND f.status = 'ACCEPTED')", nativeQuery = true)
    List<Post> findFriendPostByIdUser(Long id_user, Long id_friend);

    @Query(value = "select u.id_user, p.content_post, p.id_post,p.name_file\n" +
            "FROM post as p left join user as u on(p.id_user = u.id_user)\n" +
            "where u.id_user= ?", nativeQuery = true)
    List<Post> listPostByIdUser(Long id_user);
}
