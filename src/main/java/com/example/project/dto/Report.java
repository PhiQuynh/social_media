package com.example.project.dto;

import lombok.Data;

@Data
public class Report {

    public int countPost;
    public int countFriend;
    public int countLike;
    public int countComment;

    public Report(int countPost, int countComment, int countFriend, int countLike) {
        this.countPost = countPost;
        this.countComment = countComment;
        this.countFriend = countFriend;
        this.countLike = countLike;
    }


    public Report() {

    }
}
