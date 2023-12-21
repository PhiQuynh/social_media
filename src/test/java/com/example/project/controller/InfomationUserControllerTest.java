package com.example.project.controller;

import com.example.project.dto.UpdateUser;
import com.example.project.ennity.InformationUser;
import com.example.project.service.InformationUserSerVice;
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
class InfomationUserControllerTest {

    @InjectMocks @Spy
    InfoUserController infomationUserController;

    @Mock
    InformationUserSerVice informationUserSerVice;

    @Test
    void addProfile() {
        UpdateUser updateUser = new UpdateUser();
        Mockito.when(informationUserSerVice.addProfile(updateUser)).thenReturn(new ResponseEntity<>(null,HttpStatus.OK));
        Assertions.assertEquals(HttpStatus.OK, infomationUserController.addProfile(updateUser).getStatusCode());
    }

    @Test
    void editProfile() {
        UpdateUser updateUser = new UpdateUser();
        Mockito.when(informationUserSerVice.editProfile(updateUser,1L)).thenReturn(new ResponseEntity<>(null, HttpStatus.CREATED));
        Assertions.assertEquals(HttpStatus.CREATED, infomationUserController.editProfile(1L, updateUser).getStatusCode());
    }

    @Test
    void getUserById() {
        Mockito.when(informationUserSerVice.getUserById()).thenReturn(null);
        Assertions.assertEquals(null, infomationUserController.getUserById());
    }

    @Test
    void deleteInfo() {
        Mockito.when(informationUserSerVice.deleteInfo()).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
        Assertions.assertEquals(HttpStatus.OK, infomationUserController.deleteInfo().getStatusCode());
    }
}