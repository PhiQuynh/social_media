package com.example.project.dto;

import lombok.Data;

@Data
public class UpdateComment {

    public String content_comment;
    public Long id_post;
    public Long id_user;
    public Long id_comment;
}
