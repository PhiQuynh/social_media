package com.example.project.repository;

import com.example.project.ennity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtpRepository extends JpaRepository<OTP, Long> {
    List<OTP> findByUsername(String username);

    boolean existsByOtp(String otp);
    boolean existsByUsername(String username);
    void deleteByUsername(String username);

//    void deleteAll(Optional<OTP> existingOtps);
}
