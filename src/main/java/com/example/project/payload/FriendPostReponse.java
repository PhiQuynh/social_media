package com.example.project.payload;

import com.example.project.dto.FriendPost;
import lombok.Data;

import java.util.List;

@Data
public class FriendPostReponse {
    public int code ;
    public int count ;
    public List<FriendPost> postList;
}
