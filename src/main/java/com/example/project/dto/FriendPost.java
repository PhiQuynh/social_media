package com.example.project.dto;

import io.micrometer.observation.ObservationFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendPost {
    public String content_post;
    public String name_file;
    public Long id_user;
    public Long id_post;

}
