package com.example.project.service;

import com.example.project.config.Constants;
import com.example.project.config.jwt.JwtTokenProvider;
import com.example.project.config.jwt.JwtTokenUtil;
import com.example.project.dto.Login;
import com.example.project.dto.OtpLogin;
import com.example.project.dto.Register;
import com.example.project.dto.ResetUser;
import com.example.project.ennity.OTP;
import com.example.project.ennity.Role;
import com.example.project.ennity.User;
import com.example.project.payload.LoginResponse;
import com.example.project.payload.ResponseSucces;
import com.example.project.repository.OtpRepository;
import com.example.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    OtpRepository otpRepository;

    @Mock
    JwtTokenProvider tokenProvider;

    @Mock
    JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    UserServiceImpl userService;


    @Test
    void registerUser_username_exits(){
        Register addUserRequest = new Register();
        addUserRequest.setUsername("existing_user@gmail.com");
        addUserRequest.setPassword("passwords");
        when(userRepository.findByUsername(addUserRequest.getUsername())).thenReturn(new User());
        ResponseEntity<?> response = userService.registerUser(addUserRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void registerUser_username_invalid(){
        // Arrange
        Register addUserRequest = new Register();
        addUserRequest.setUsername("invalid_username");
        addUserRequest.setPassword("passwords");
        ResponseEntity<?> response = userService.registerUser(addUserRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.USER_ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.ERR_USERNAME, ((ResponseSucces) response.getBody()).getMessage());
    }

    @Test
    void registerUser_password_invalid(){
        Register addUserRequest = new Register();
        addUserRequest.setUsername("a@gmail.com");
        addUserRequest.setPassword("short");
        ResponseEntity<?> response = userService.registerUser(addUserRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.USER_ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.MESS_LENGTH, ((ResponseSucces) response.getBody()).getMessage());
    }

    @Test
    void registerUser_success() {
        Register addUserRequest = new Register();
        addUserRequest.setUsername("johndoe@example.com");
        addUserRequest.setPassword("passwords");
        Role role = new Role();
        role.setId_role(1L);
        User user = new User();
        user.setRole(role);
        ResponseEntity<?> response = userService.registerUser(addUserRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.SUCCCES_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.MESSAGE_SUCCES_ADD, ((ResponseSucces) response.getBody()).getMessage());
    }
    @Test
    public void testRegisterUser_EmptyUsername() {
        Register addUserRequest = new Register();
        addUserRequest.setUsername("");
        addUserRequest.setPassword("password");
        ResponseEntity<?> response = userService.registerUser(addUserRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.USER_ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.NULL_USERNAME, ((ResponseSucces) response.getBody()).getMessage());
    }
    @Test
    public void testRegisterUser_EmptyPassword() {
        Register addUserRequest = new Register();
        addUserRequest.setUsername("john.doe@example.com");
        addUserRequest.setPassword("");
        ResponseEntity<?> response = userService.registerUser(addUserRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.USER_ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.NULL_PASSWORD, ((ResponseSucces) response.getBody()).getMessage());
    }

    @Test
    public void testRegisterUser_Password_long() {
        // Arrange
        Register addUserRequest = new Register();
        addUserRequest.setUsername("john.doe@example.com");
        addUserRequest.setPassword("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
        String encodedPassword = "encodedPassword";
        Mockito.when(passwordEncoder.encode(addUserRequest.getPassword())).thenReturn(encodedPassword);
        Role role = new Role();
        role.setId_role(1L);
        User user = new User();
        user.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));
        user.setUsername(addUserRequest.getUsername());
        user.setRole(role);
        ResponseEntity<?> response = userService.registerUser(addUserRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.USER_ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.MESS_LENGTH, ((ResponseSucces) response.getBody()).getMessage());
        verify(passwordEncoder, Mockito.times(2)).encode(addUserRequest.getPassword());
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() {
        Register addUserRequest = new Register();
        addUserRequest.setUsername("john.doe@example.com");
        addUserRequest.setPassword("password");
        // Giả lập user đã tồn tại
        User existingUser = new User();
        when(userRepository.findByUsername(addUserRequest.getUsername())).thenReturn(existingUser);
        ResponseEntity<?> response = userService.registerUser(addUserRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.USER_ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.USERNAME_EXIST_MESSAGE, ((ResponseSucces) response.getBody()).getMessage());
    }

//    @Test
//    void loginUser_vaild() {
//        Login login = new Login();
//        login.setUsername("john.doe@example.com");
//        login.setPassword("password");
//        User user = new User();
//        user.setUsername(login.getUsername());
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        user.setPassword(passwordEncoder.encode(login.getPassword()));
//        when(userRepository.existsByUsername(login.getUsername())).thenReturn(true);
//        when(userRepository.findByUsername(login.getUsername())).thenReturn(user);
//        when(otpRepository.findByUsername(login.getUsername())).thenReturn(new ArrayList<>());
//        Random random = mock(Random.class);
//        when(random.nextInt(900000)).thenReturn(123456);
//        userService.generateOTP();
//        String result = userService.loginUser(login);
//        assertEquals("123456", result);
//        verify(userRepository).existsByUsername(login.getUsername());
//        verify(userRepository).findByUsername(login.getUsername());
//        verify(otpRepository).findByUsername(login.getUsername());
//        verify(otpRepository).delete((OTP) any(OTP.class));
//        verify(otpRepository).save((OTP) any(OTP.class));
//    }

    @Test
    void loginUser_invalid_username(){
        Login login = new Login();
        login.setUsername("john.doe@example.com");
        login.setPassword("password");
        when(userRepository.existsByUsername(login.getUsername())).thenReturn(false);
        String result = userService.loginUser(login);
        assertEquals("This username could not be found", result);
        verify(userRepository).existsByUsername(login.getUsername());
        verify(userRepository, never()).findByUsername(login.getUsername());
        verify(otpRepository, never()).findByUsername(login.getUsername());
    }

    @Test
    void loginUser_invalid_password(){
        Login login = new Login();
        login.setUsername("john.doe@example.com");
        login.setPassword("password");
        User user = new User();
        user.setUsername(login.getUsername());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode("differentPassword"));
        when(userRepository.existsByUsername(login.getUsername())).thenReturn(true);
        when(userRepository.findByUsername(login.getUsername())).thenReturn(user);
        String result = userService.loginUser(login);
        assertEquals("Invalid password not match", result);
        verify(userRepository).existsByUsername(login.getUsername());
        verify(userRepository).findByUsername(login.getUsername());
        verify(otpRepository, never()).findByUsername(login.getUsername());
    }

//    @Test
//    void generateOTP(){
//      OTP otp = new OTP();
//        Random random = mock(Random.class);
//        when(random.nextInt(900000)).thenReturn(Integer.valueOf("123456"));
//        otp.setOtp(random.toString());
//        String otpCode = userService.generateOTP();
//        assertEquals("123456", otpCode);
//        verify(random).nextInt(900000);
//    }

    @Test
    public void verifyOTP_ValidOTP() {
        String enteredOTP = "123456";
        OtpLogin otpLogin = new OtpLogin();
        otpLogin.setUsername("abc.doe@example.com");
        List<OTP> otpList = new ArrayList<>();
        OTP otp = new OTP();
        otp.setOtp("123456");
        otpList.add(otp);
        when(otpRepository.findByUsername(otpLogin.getUsername())).thenReturn(otpList);
        when(tokenProvider.generateTokens(otpLogin.getUsername())).thenReturn("access_token");
        LoginResponse result = userService.verifyOTP(enteredOTP, otpLogin);
        assertEquals("access_token", result.getAccessToken());
        verify(otpRepository).findByUsername(otpLogin.getUsername());
        verify(tokenProvider).generateTokens(otpLogin.getUsername());
    }

    @Test
    public void verifyOTP_InvalidOTP() {
        String enteredOTP = "654321";
        OtpLogin otpLogin = new OtpLogin();
        otpLogin.setUsername("john.doe@example.com");
        List<OTP> otpList = new ArrayList<>();
        OTP otp = new OTP();
        otp.setOtp("123456");
        otpList.add(otp);
        when(otpRepository.findByUsername(otpLogin.getUsername())).thenReturn(otpList);
        LoginResponse result = userService.verifyOTP(enteredOTP, otpLogin);
        assertEquals("500", result.getErrors().get("code"));
        assertEquals("Nhập sai username hoặc mã OTP", result.getErrors().get("message"));
        verify(otpRepository).findByUsername(otpLogin.getUsername());
        verify(tokenProvider, never()).generateTokens(anyString());
    }

    @Test
    public void verifyOTP_NoOTPFound() {
        String enteredOTP = "123456";
        OtpLogin otpLogin = new OtpLogin();
        otpLogin.setUsername("john.doe@example.com");
        when(otpRepository.findByUsername(otpLogin.getUsername())).thenReturn(new ArrayList<>());
        when(tokenProvider.generateTokens(otpLogin.getUsername())).thenReturn("access_token");
        LoginResponse result = userService.verifyOTP(enteredOTP, otpLogin);
        assertEquals("access_token", result.getAccessToken());
        verify(otpRepository).findByUsername(otpLogin.getUsername());
        verify(tokenProvider).generateTokens(otpLogin.getUsername());
    }

    @Test
    public void testForgotPassword_UserExists() {
        ResetUser register = new ResetUser();
        register.setUsername("exampleUser");
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", "exampleToken");
        when(userRepository.existsByUsername(register.getUsername())).thenReturn(true);
        User user = new User();
        user.setUsername(register.getUsername());
        when(userRepository.findByUsername(register.getUsername())).thenReturn(user);
        when(jwtTokenUtil.generateUserNameToken(user.getUsername())).thenReturn("exampleToken");
        ResponseEntity<?> result = userService.forgotPassword(register);
        assertEquals(ResponseEntity.ok(response), result);
        verify(userRepository).existsByUsername(register.getUsername());
        verify(userRepository).findByUsername(register.getUsername());
        verify(jwtTokenUtil).generateUserNameToken(user.getUsername());
        verify(userRepository).save(user);
    }

//    @Test
//    public void testForgotPassword_UserDoesNotExist() {
//        ResetUser register = new ResetUser();
//        register.setUsername("exampleUser");
//        User user = new User();
//        user.setUsername("exampleUser");
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "Không tìm thấy username này !");
////        when(userRepository.findByUsername(user.getUsername())).thenReturn(user.);
//        when(userRepository.existsByUsername(register.getUsername())).thenReturn(false);
//        ResponseEntity<?> result = userService.forgotPassword(register);
//        assertEquals(ResponseEntity.ok(response), result);
//        verify(userRepository).existsByUsername(register.getUsername());
//        verify(userRepository, never()).findByUsername(user.getUsername());
//        verify(jwtTokenUtil, never()).generateUserNameToken(Mockito.anyString());
//        verify(userRepository, never()).save((User) any(User.class));
//    }

//    @Test
//    public void testForgotPassword_UserIsNull() {
//        User user = new User();
//        user.setIdUser(1L);
//        user.setUsername("exampleUser");
//        ResetUser register = new ResetUser();
//        register.setUsername("exampleUser");
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "Email not found");
//        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);
//        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
//        ResponseEntity<?> result = userService.forgotPassword(register);
//        assertEquals(ResponseEntity.ok(response), result);
//        verify(userRepository).existsByUsername(user.getUsername());
//        verify(userRepository).findByUsername(user.getUsername());
//        verify(jwtTokenUtil, never()).generateUserNameToken(anyString());
//        verify(userRepository, never()).save((User) any(User.class));
//    }

    @Test
    void resetPassword() {
    }
}