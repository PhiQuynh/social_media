package com.example.project.service;

import com.example.project.dto.FriendList;
import com.example.project.ennity.FriendshipStatus;
import org.springframework.http.ResponseEntity;

public interface FriendService {
    ResponseEntity<?> acceptFriend(Long id_user);
    ResponseEntity<?> notAcceptFriend(Long id_user);
    ResponseEntity<?> getListFriend();
    ResponseEntity<?> addFriends(Long id_friend);
}
