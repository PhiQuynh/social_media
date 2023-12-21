package com.example.project.controller;

import com.example.project.ennity.FriendshipStatus;
import com.example.project.service.FriendServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class FriendController {
    @Autowired
    public FriendServicesImpl friendServices;

    @PutMapping("accept-friend/{id_user}")
    public ResponseEntity<?> acceptFriend(@PathVariable Long id_user){
        return friendServices.acceptFriend(id_user);
    }

    @PutMapping("not-accept/{id_user}")
    public ResponseEntity<?> notacceptFriend(@PathVariable Long id_user){
        return friendServices.notAcceptFriend(id_user);
    }

    @PostMapping("/addFriend/{id_friend}")
    public ResponseEntity<?> addFriends(@PathVariable Long id_friend){
        return friendServices.addFriends(id_friend);
    }

    @GetMapping("/listFriend")
    public ResponseEntity<?> getListFriend(){
        return friendServices.getListFriend();
    }
}
