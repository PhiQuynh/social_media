package com.example.project.payload;


import lombok.Data;

@Data
public class ResponseSucces {
    private int code;
    private String message;

    public ResponseSucces(int succcesCode, String messageSuccesAdd) {
        this.code = succcesCode;
        this.message = messageSuccesAdd;
    }
}