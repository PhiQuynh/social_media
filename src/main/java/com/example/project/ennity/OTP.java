package com.example.project.ennity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Table(name = "otp")
@Entity
public class OTP {
    @Column(name = "username", nullable = false)
    public String username;

    @Column(name = "otp", nullable = false)
    public String otp;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOtp")
    public Long idOtp;
}
