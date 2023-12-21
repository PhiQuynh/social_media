package com.example.project.controller;

import com.example.project.dto.*;
import com.example.project.payload.LoginResponse;
import com.example.project.service.UserServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    @Spy
    AuthController userController;

    @Test
    void register() throws Exception {
        Register register = new Register();
        Mockito.when(userService.registerUser(Mockito.isA(Register.class))).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
        assertEquals(HttpStatus.OK, userController.register(register).getStatusCode());
    }

    @Test
    void login(){
        Login login = new Login();
        LoginResponse loginResponse = new LoginResponse();
        Mockito.when(userService.loginUser(Mockito.isA(Login.class))).thenReturn(null);
        assertEquals(null, userController.loginUser(login));
    }

    @Test
    void forgotPassword() {
        ResetUser resetUser = new ResetUser();
        Mockito.when(userService.forgotPassword(Mockito.isA(ResetUser.class))).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
        assertEquals(HttpStatus.OK, userController.forgotPassword(resetUser).getStatusCode());
    }

    @Test
    void resetPassword() {
        UpdatePassword updatePassword = new UpdatePassword();
        updatePassword.setConfirmpassword("testpassword");
        Mockito.when(userService.resetPassword(Mockito.anyString(),Mockito.isA(UpdatePassword.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        assertEquals(HttpStatus.OK, userController.resetPassword("kjfklssfsdf",updatePassword).getStatusCode());
    }
//
//    @Test
//    void deleteUser() throws Exception {
//        Mockito.when(userService.deleteUser(Mockito.anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
//        assertEquals(HttpStatus.OK, userController.deleteUser(1L).getStatusCode());
//    }

    @Test
    void verifyOtp() {
            String enteredOTP = "123456";
            OtpLogin otpLogin = new OtpLogin();
            otpLogin.setUsername("username@gmail.com");
            LoginResponse expectedResponse = new LoginResponse("success");
            Mockito.when(userService.verifyOTP(eq(enteredOTP), eq(otpLogin))).thenReturn(expectedResponse);
            LoginResponse actualResponse = userController.verifyOtp(enteredOTP, otpLogin);
            assertEquals(expectedResponse, actualResponse);
            verify(userService, times(1)).verifyOTP(eq(enteredOTP), eq(otpLogin));
        }

}