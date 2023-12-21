package com.example.project.controller;

import com.example.project.service.FriendServicesImpl;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FriendControllerTest {

    @InjectMocks @Spy
    FriendController friendController;

    @Mock
    FriendServicesImpl friendServices;

    @Test
    void acceptFriend() {
        Mockito.when(friendServices.acceptFriend(Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
        Assertions.assertEquals(HttpStatus.OK, friendController.acceptFriend( 1L).getStatusCode());
    }

    @Test
    void notacceptFriend() {
        Mockito.when(friendServices.notAcceptFriend(Mockito.anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
        Assertions.assertEquals(HttpStatus.CREATED, friendController.notacceptFriend( 1L).getStatusCode());
    }

    @Test
    void addFriends() {
        Mockito.when(friendServices.addFriends(Mockito.anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        Assertions.assertEquals(HttpStatus.OK, friendController.addFriends(1L).getStatusCode());

    }
}