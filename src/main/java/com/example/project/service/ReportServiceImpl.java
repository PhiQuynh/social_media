package com.example.project.service;

import com.example.project.config.jwt.AuthUserDetails;
import com.example.project.dto.Report;
import com.example.project.ennity.User;
import com.example.project.repository.PostRepository;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportServiceImpl {
    @Autowired
    public UserRepository userRepository;
    public Report getReportByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();

        int id_user = Math.toIntExact(userDetails.getUser().getIdUser());

        String postQuery = "SELECT COUNT(*) FROM post WHERE id_user = ?";
        String commentQuery = "SELECT COUNT(*) FROM comment WHERE id_user = ?";
        String friendQuery = "SELECT COUNT(*) FROM friend WHERE (friend.id_user = ? AND friend.status='ACCEPTED') OR (friend.id_friend = ? AND friend.status='ACCEPTED')";
        String likeQuery = "SELECT COUNT(*) FROM like_posts WHERE id_user = ? AND like_posts.like_post='like'";

        int countPost = 0;
        int countComment = 0;
        int countFriend = 0;
        int countLike = 0;

        try (Connection connection = DatabaseConnector.getConnection()) {
            countPost = getCount(connection, postQuery, id_user);
            countComment = getCount(connection, commentQuery, id_user);
            countFriend = getCounts(connection, friendQuery, id_user, id_user);
            countLike = getCount(connection, likeQuery, id_user);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return new Report(countPost, countComment, countFriend, countLike);
    }
        private int getCount(Connection connection, String query, int id_user) throws SQLException {
            int count = 0;

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id_user);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        count = resultSet.getInt(1);
                    }
                }
            }
            return count;
        }

    private int getCounts(Connection connection, String query, int id_user, int id_friend) throws SQLException {
        int count = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id_user);
            preparedStatement.setInt(2, id_friend);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        }
        return count;
    }
}
