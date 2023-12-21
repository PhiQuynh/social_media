package com.example.project.service;

import com.example.project.dto.*;
import com.example.project.ennity.User;
import com.example.project.payload.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> registerUser(Register register) throws Exception ;
//    ResponseEntity<?> deleteUser(Long id_user) throws Exception;
    ResponseEntity<?> forgotPassword(ResetUser resetUser);
    String loginUser(Login login) ;
    ResponseEntity<?> resetPassword(String token, UpdatePassword register);
}
