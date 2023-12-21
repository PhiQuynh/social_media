package com.example.project.service;

import com.example.project.config.Constants;
import com.example.project.config.jwt.AuthUserDetails;
import com.example.project.config.jwt.JwtTokenProvider;
import com.example.project.config.jwt.JwtTokenUtil;
import com.example.project.dto.*;
import com.example.project.ennity.OTP;
import com.example.project.ennity.Role;
import com.example.project.ennity.User;
import com.example.project.payload.LoginResponse;
import com.example.project.payload.ResponseSucces;
import com.example.project.repository.OtpRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public final PasswordEncoder passwordEncoder;
    public final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    public final JwtTokenUtil jwtTokenUtil;
    public final OtpRepository otpRepository;


    @Override
    public ResponseEntity<?> registerUser(Register addUserRequest) {
        try{
            Role r = new Role();
            r.setId_role(1L);
            // Kiểm tra xem username đã tồn tại hay chưa
            Optional<User> existingUser = Optional.ofNullable(userRepository.findByUsername(addUserRequest.getUsername()));
            if (existingUser.isPresent()) {
                return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.USERNAME_EXIST_MESSAGE), HttpStatus.BAD_REQUEST);
            }
            var user = User.builder()
                    .username(addUserRequest.getUsername())
                    .password(passwordEncoder.encode(addUserRequest.getPassword()))
                    .role(r)
                    .build();
            if(addUserRequest.getUsername() == ""){
                return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.NULL_USERNAME), HttpStatus.BAD_REQUEST);
            } else if(addUserRequest.getUsername().length() > 125){
                return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.MESS_LENGTH), HttpStatus.BAD_REQUEST);
            } else if(!addUserRequest.getUsername().matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$") ){
                return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.ERR_USERNAME), HttpStatus.BAD_REQUEST);
            }
            if(addUserRequest.getPassword() == ""){
                return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.NULL_PASSWORD), HttpStatus.BAD_REQUEST);
            }else if(addUserRequest.getPassword().length() < 8 ){
                return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.MESS_LENGTH), HttpStatus.BAD_REQUEST);
            } else if(addUserRequest.getPassword().length() >12){
                return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.MESS_LENGTH), HttpStatus.BAD_REQUEST);
            }
            userRepository.save(user);
            return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, Constants.MESSAGE_SUCCES_ADD), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseSucces(Constants.ERR_CODE, Constants.MESSAGE_ERR_ADD), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public String loginUser(Login login) {
        String otpCode = generateOTP();
        // Xóa otp hiện có cho user
        String message = "This username could not be found";
        if(!userRepository.existsByUsername(login.getUsername())){
            return message;
        }
       User user = userRepository.findByUsername(login.getUsername());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            message = "Invalid password not match";
            return message;
        }
        List<OTP> existingOtps = otpRepository.findByUsername(login.getUsername());
        if (!existingOtps.isEmpty()) {
            for (OTP existingOtp : existingOtps) {
                otpRepository.delete(existingOtp);
            }
        }
        OTP generateOtp = new OTP();
        generateOtp.setUsername(login.getUsername());
        generateOtp.setOtp(otpCode);
        otpRepository.save(generateOtp);
        // Trả về mã OTP cho đối tượng LoginResponse
        return otpCode;
    }
    public LoginResponse verifyOTP(String enteredOTP, OtpLogin otpLogin) {

        List<OTP> otpList = otpRepository.findByUsername(otpLogin.getUsername());
        if (otpList.isEmpty()) {
            // Không tìm thấy mã OTP cho người dùng
            String accessToken = tokenProvider.generateTokens(otpLogin.getUsername());
            return new LoginResponse(accessToken);
        } else {
            boolean isOTPMatched = false;
            for (OTP otp : otpList) {
                if (otp.getOtp().equals(enteredOTP)) {
                    isOTPMatched = true;
                    break;
                }
            }
            if (isOTPMatched) {
                // Mã OTP khớp, tạo access token
                String accessToken = tokenProvider.generateTokens(otpLogin.getUsername());
                return new LoginResponse(accessToken);
            } else {
                // Mã OTP không khớp, trả về thông báo lỗi
                Map<String, String> message = new HashMap<>();
                message.put("code", "500");
                message.put("message", "Nhập sai username hoặc mã OTP");
                return new LoginResponse(message);
            }
        }
    }
    @Override
    public ResponseEntity<?> forgotPassword(ResetUser register) {
        Map<String, String> response = new HashMap<>();
        if(!userRepository.existsByUsername(register.getUsername())){
             response.put("message", "Không tìm thấy username này !");
        }
        User user = userRepository.findByUsername(register.getUsername());
        if(user == null ){
            response.put("message", "Email not found");
        }
            String token = jwtTokenUtil.generateUserNameToken(user.getUsername());
            user.setTokenPassword(token);
            userRepository.save(user);
            response.put("accessToken",token);
            return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> resetPassword(String token, UpdatePassword register){
        Map<String, String> response = new HashMap<>();
        User user = userRepository.findByTokenPassword(token);
        if(user == null){
            response.put("message", "Token in valid");
        }
        user.setPassword(passwordEncoder.encode(register.getConfirmpassword()));
        user.setTokenPassword(null);
        userRepository.save(user);
        response.put("message", "Update mật khẩu thành công");
        return ResponseEntity.ok(response);
    }
    String generateOTP() {
        // Sinh mã OTP ngẫu nhiên
        Random random = new Random();
        // Sinh số ngẫu nhiên từ 100000 đến 999999
        int otp = random.nextInt(900000) + 100000;
        return String.valueOf(otp);
    }
}
