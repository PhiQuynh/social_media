package com.example.project.controller;

import com.example.project.dto.*;
import com.example.project.payload.LoginResponse;
import com.example.project.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class AuthController {
    public final UserServiceImpl userService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register register) {
        return userService.registerUser(register);
    }
    @PostMapping("/login")
    public String loginUser(@RequestBody Login login) {
        return userService.loginUser(login);
    }

    @PostMapping("/otp")
    public LoginResponse verifyOtp(@RequestParam String enteredOTP, @RequestBody OtpLogin otpLogin) {
        return userService.verifyOTP(enteredOTP,otpLogin );
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ResetUser register) {
       return userService.forgotPassword(register);
    }
    @PutMapping("/resetpassword")
    public ResponseEntity<?> resetPassword(@RequestHeader String token,  @RequestBody UpdatePassword register){
       return userService.resetPassword(token, register);
    }

//    @DeleteMapping
//    public ResponseEntity<?> deleteUser(Long id_user) throws Exception {
//        return userService.deleteUser(id_user);
//    }
}
