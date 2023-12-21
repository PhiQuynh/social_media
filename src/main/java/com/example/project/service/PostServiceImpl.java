package com.example.project.service;

import com.example.project.config.Constants;
import com.example.project.config.jwt.AuthUserDetails;
import com.example.project.config.jwt.JwtTokenProvider;
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
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl {

    public final FileRepository fileRepository;
    public final PostRepository postRepository;
    public final UserRepository userRepository;
    private final Path root = Paths.get("uploads");


    public ResponseEntity<?> addPost(AddPost addPost) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
            Post post = new Post();
            post.setUser(userRepository.findByUsername(userDetails.getUsername()));

            // Kiểm tra và gán giá trị cho trường content_post
            if (addPost.getContent_post() != null) {
                post.setContent_post(addPost.getContent_post());
            }
            // Kiểm tra và gán giá trị cho trường id_file
            if (addPost.getName_file() != null) {
                if(!fileRepository.existsByNameFile(addPost.getName_file())){
                    throw new IllegalArgumentException("Ảnh này không tồn tại");
                }
                FileInfo fileInfo = fileRepository.findByNameFile(addPost.getName_file());
                post.setName_file(fileInfo);
            }
            if(addPost.getContent_post().isEmpty() && addPost.getName_file().isEmpty()){
                throw new IllegalArgumentException("Bạn hãy nhập thông tin để tao bài viết mới");
            }
            // Kiểm tra nếu cả hai trường content_post và id_file đều không có giá trị
            if (addPost.getContent_post() == null && addPost.getName_file() == null) {
                throw new IllegalArgumentException("At least one of content_post or id_file must be provided.");
            }
            // Lưu bài đăng mới vào cơ sở dữ liệu
            postRepository.save(post);

            return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, Constants.MESSAGE_SUCCES_ADDPOST), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseSucces(Constants.ERR_CODE, Constants.MESSAGE_ERR_ADDPOST), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> updatePost(AddPost updatePost, Long id_post) {
        try {
            if (!postRepository.existsById(id_post)) {
                return new ResponseEntity<>(new ResponseSucces(Constants.ERR_CODE, Constants.NOT_FOUND_POST), HttpStatus.NOT_FOUND);
            }
            Post post = postRepository.findById(id_post).orElseThrow();
            // Cập nhật trường content_post nếu có thay đổi
            if (updatePost.getContent_post() != null) {
                post.setContent_post(updatePost.getContent_post());
            }
            // Nếu updatePost.getId_file() không null, cập nhật trường id_file
            if (updatePost.getName_file() != null) {
                if(!fileRepository.existsByNameFile(updatePost.getName_file())){
                    throw new IllegalArgumentException("Ảnh này không tồn tại");
                }
                FileInfo fileInfo = fileRepository.findByNameFile(updatePost.getName_file());
                post.setName_file(fileInfo);
            }
            if(updatePost.getContent_post().isEmpty() && updatePost.getName_file().isEmpty()){
                throw new IllegalArgumentException("Bạn hãy nhập thông tin để update lại bài viết");
            }
            postRepository.save(post);
            return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, Constants.MESSAGE_UPDATE_POST_SUCCES), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseSucces(Constants.ERR_CODE, Constants.MESSAGE_UPDATE_POST_ERR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> deletePost(Long id_post) throws Exception {
        if (postRepository.existsById(id_post)) {
        } else {
            throw new Exception(Constants.ERR_MESSAGE);
        }
        postRepository.deleteById(id_post);
        return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, Constants.MESSAGE_DELETE_SUCCES), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> timeline() {
        Map<String, Object> reponse = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        List<Post> postFriendList = postRepository.findFriendPostByIdUser(userDetails.getUser().getIdUser(), userDetails.getUser().getIdUser());
        List<FriendPost> postList = postFriendList.stream().map(
                post -> getPost(post)
        ).toList();
        reponse.put("code", "200");
        reponse.put("count", postList.size());
        reponse.put("post", postList);
        return new ResponseEntity<>(reponse, HttpStatus.OK);
    }

    FriendPost getPost(Post post) {
        FriendPost.FriendPostBuilder builder = FriendPost.builder()
                .id_user(post.getUser().getIdUser())
                .id_post(post.getId());
        if(post.getName_file() != null) {
            builder.name_file(post.getName_file().getNameFile());
        }
        if(post.getContent_post() != null){
            builder.content_post(post.getContent_post());
        }
        return builder.build();
    }

    @Transactional
    public GetPost getPostById(Long id_post) throws Exception {
        if (postRepository.existsById(id_post)) {

        } else {
            throw new Exception(Constants.ERR_MESSAGE);
        }
        return getPosst(postRepository.findById(id_post).orElseThrow());
    }

     public GetPost getPosst(Post post) {
        GetPost.GetPostBuilder builder = GetPost.builder();
                if(post.getName_file() != null) {
                    builder.name_file(post.getName_file().getNameFile());
                }
                if(post.getContent_post() != null){
                    builder.content_post(post.getContent_post());
                }
                if(post.getUser() != null){
                    builder.id_user(post.getUser().getIdUser());
                }
        return builder.build();
    }

    public Resource load(Long id_post) {
        Optional<Post> postOptional = postRepository.findById(id_post);
        try {
           if(postOptional.get().getName_file() == null){
               throw new RuntimeException("Bạn không thêm ảnh vào bài viết");
           }
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                String name_file = post.getName_file().getNameFile();
                Path file = root.resolve(name_file);
                Resource resource = new UrlResource(file.toUri());
                if (resource.exists() || resource.isReadable()) {
                    return resource;
                } else {
                    throw new RuntimeException("Could not read the file!");
                }
            }
            return null;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getPostByUser() {
        Map<String, Object> reponse = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());

        List<Post> postFriendList = postRepository.listPostByIdUser(user.getIdUser());
        List<UserPost> postList = postFriendList.stream().map(
                post -> userPost(post)
        ).toList();

        reponse.put("code", "200");
        reponse.put("count", postList.size());
        reponse.put("post", postList);

        return new ResponseEntity<Map<String, Object>>(reponse, HttpStatus.OK);
    }

    public UserPost userPost(Post post) {
        UserPost.UserPostBuilder builder = UserPost.builder()
                .id_post(post.getId());
        if(post.getContent_post() != null){
            builder.content_post(post.getContent_post());
        }
        if(post.getName_file() != null){
            builder.name_file(post.getName_file().getNameFile());
        }
        return builder.build();
    }

}
