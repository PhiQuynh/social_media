package com.example.project.service;

import com.example.project.config.Constants;
import com.example.project.config.jwt.AuthUserDetails;
import com.example.project.dto.FriendList;
import com.example.project.dto.UserPost;
import com.example.project.ennity.Friend;
import com.example.project.ennity.FriendshipStatus;
import com.example.project.ennity.Post;
import com.example.project.ennity.User;
import com.example.project.payload.ResponseSucces;
import com.example.project.repository.FriendRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FriendServicesImpl implements FriendService {

    public final FriendRepository friendRepository;

    public final UserRepository userRepository;

//    @Override
//    public ResponseEntity<?> addFriends(Long id_friend) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
//        User currentUser = userRepository.findByUsername(userDetails.getUsername());
//        User friend = userRepository.findById(id_friend).orElse(null);
//        if (friend == null) {
//            return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.EXITS_USER), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        Friend existingFriendship = friendRepository.findByUserAndFriend(currentUser, friend);
//        Friend existingInvitation = friendRepository.findByUserAndFriend(friend, currentUser);
//
//        if (existingInvitation != null && existingFriendship != null) {
//            FriendshipStatus invitationStatus = existingInvitation.getStatus();
//            if (invitationStatus.equals(FriendshipStatus.ACCEPTED) || invitationStatus.equals(FriendshipStatus.NOTACCEPTED)) {
//                // Xóa lời mời kết bạn cũ
//                friendRepository.delete(existingInvitation);
//                friendRepository.delete(existingFriendship);
//            }
//        }
//        if (existingFriendship != null) {
//            FriendshipStatus friendshipStatus = existingFriendship.getStatus();
//            if (friendshipStatus.equals(FriendshipStatus.PENDING) || friendshipStatus.equals(FriendshipStatus.ACCEPTED) || friendshipStatus.equals(FriendshipStatus.NOTACCEPTED)) {
//                return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.EXIST_MESSAGE), HttpStatus.BAD_REQUEST);
//            }
//        }
//        Friend newFriendship = new Friend();
//        newFriendship.setUser(currentUser);
//        newFriendship.setFriend(friend);
//        newFriendship.setStatus(FriendshipStatus.PENDING);
//        friendRepository.save(newFriendship);
//        return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, Constants.SEND_REQUEST), HttpStatus.OK);
//    }

    @Override
    public ResponseEntity<?> addFriends(Long id_friend) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();

        User currentUser = userRepository.findByUsername(userDetails.getUsername());
        User friend = userRepository.findById(id_friend).orElse(null);

        if (friend == null) {
            return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.EXITS_USER), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Friend existingFriendship = friendRepository.findByUserAndFriend(currentUser, friend);
        Friend existingInvitation = friendRepository.findByUserAndFriend(friend, currentUser);
        if (existingInvitation != null) {
            if( existingInvitation.getStatus().equals(FriendshipStatus.PENDING) || existingInvitation.getStatus().equals(FriendshipStatus.ACCEPTED)){

                return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.EXIST_MESSAGE), HttpStatus.BAD_REQUEST);
            } else {
                friendRepository.delete(existingInvitation);
            }
        }
        if (existingFriendship != null ) {
            FriendshipStatus status = existingFriendship.getStatus();
            if (status.equals(FriendshipStatus.PENDING) || status.equals(FriendshipStatus.ACCEPTED)) {
                return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.EXIST_MESSAGE), HttpStatus.BAD_REQUEST);
            } else {
                friendRepository.delete(existingFriendship);
            }
        }
        Friend newFriendship = new Friend();
        newFriendship.setUser(currentUser);
        newFriendship.setFriend(friend);
        newFriendship.setStatus(FriendshipStatus.PENDING);
        friendRepository.save(newFriendship);

        return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, Constants.SEND_REQUEST), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> acceptFriend( Long id_user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();

        User currentUser = userRepository.findByUsername(userDetails.getUsername());
        User friend = userRepository.findById(id_user).orElse(null);

        if (friend == null) {
            return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.EXITS_USER), HttpStatus.OK);
        }
        Friend existingFriendship = friendRepository.findByUserAndFriend(currentUser, friend);
        if (existingFriendship != null) {
            FriendshipStatus status = existingFriendship.getStatus();
            if (status.equals(FriendshipStatus.PENDING) || status.equals(FriendshipStatus.ACCEPTED) || status.equals(FriendshipStatus.NOTACCEPTED)) {
                return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.EXIST_MESSAGE), HttpStatus.OK);
            }
        }
        Friend friendRequest = friendRepository.findByUserAndFriend(friend, currentUser);
        if (friendRequest == null || friendRequest.getStatus().equals("PENDING")) {
            return ResponseEntity.badRequest().body("No pending friend request from the specified user.");
        }
        Friend idFriend = friendRepository.findByUserIdUser(id_user);
        Friend newFriendship = new Friend();
        newFriendship.setId(idFriend.getId());
        newFriendship.setUser(friend);
        newFriendship.setFriend(currentUser);
        newFriendship.setStatus(FriendshipStatus.ACCEPTED);
        friendRepository.save(newFriendship);

        return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, Constants.ACCEPT_FRIEND), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> notAcceptFriend( Long id_user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();

        User currentUser = userRepository.findByUsername(userDetails.getUsername());
        User friend = userRepository.findById(id_user).orElse(null);

        if (friend == null) {
            return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.EXITS_USER), HttpStatus.OK);
        }
        Friend existingFriendship = friendRepository.findByUserAndFriend(currentUser, friend);
        if (existingFriendship != null) {
            FriendshipStatus status = existingFriendship.getStatus();
            if (status.equals(FriendshipStatus.PENDING) || status.equals(FriendshipStatus.ACCEPTED) || status.equals(FriendshipStatus.NOTACCEPTED)) {
                return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.EXIST_MESSAGE), HttpStatus.OK);
            }
        }
        Friend friendRequest = friendRepository.findByUserAndFriend(friend, currentUser);
        if (friendRequest == null || friendRequest.getStatus().equals("PENDING")) {
            return ResponseEntity.badRequest().body("No pending friend request from the specified user.");
        }
        Friend idFriend = friendRepository.findByUserIdUser(id_user);
        Friend newFriendship = new Friend();
        newFriendship.setId(idFriend.getId());
        newFriendship.setUser(friend);
        newFriendship.setFriend(currentUser);
        newFriendship.setStatus(FriendshipStatus.NOTACCEPTED);
        friendRepository.save(newFriendship);
        return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, Constants.NOT_ACCEPT_FRIEND), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getListFriend(){
        Map<String, Object> reponse = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        List<Friend> friendLists = friendRepository.listFriendAndInfo(userDetails.getUser().getIdUser(), userDetails.getUser().getIdUser());
        List<FriendList> friendList = friendLists.stream().map(
                friend -> getFriend(friend)
        ).toList();
        reponse.put("code", "200");
        reponse.put("count", friendList.size());
        reponse.put("post", friendList);

        return new ResponseEntity<Map<String, Object>>(reponse, HttpStatus.OK);
    }

    public FriendList getFriend(Friend friend){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        FriendList.FriendListBuilder builder = FriendList.builder();
       if(!Objects.equals(friend.getUser().getIdUser(), userDetails.getUser().getIdUser())
               && Objects.equals(friend.getFriend().getIdUser(), userDetails.getUser().getIdUser())){
           if(friend.getUser().getIdUser() != null){
               builder.id_friend(friend.getUser().getIdUser());
           }
           if(friend.getUser().getUsername() != null){
               builder.username(friend.getUser().getUsername());
           }
       }
       else {
               if(friend.getFriend().getIdUser() != null){
                   builder.id_user(friend.getFriend().getIdUser());
               }
               if(friend.getFriend().getUsername() != null){
                   builder.username(friend.getFriend().getUsername());
               }
           }
        return builder.build();
    }
}

