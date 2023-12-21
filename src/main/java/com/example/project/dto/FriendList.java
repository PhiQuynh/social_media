package com.example.project.dto;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendList {
    public Long id_friend;
    public Long id_user;
//    public String name;
//    public String birthday;
//    public String profession;
//    public String country;
//    public String name_file;
    public String username;
}
