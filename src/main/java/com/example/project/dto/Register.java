package com.example.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Register {
    @Schema(example = "username01@gmail.com")
    public String username;

    @Schema(example = "username01")
    public String password;

//    @Schema(example = "1")
//    public Long role;
}
