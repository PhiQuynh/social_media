package com.example.project.service;

import com.example.project.config.jwt.AuthUserDetails;
import com.example.project.dto.Report;
import com.example.project.ennity.User;
import com.example.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ReportServiceImpl reportService;

    @Test
    public void testGetReportByUserId() throws SQLException {

//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//                new AuthUserDetails(new User()), null);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        Connection connection = mock(Connection.class);
//        PreparedStatement preparedStatement = mock(PreparedStatement.class);
//        ResultSet resultSet = mock(ResultSet.class);
//
//        when(userRepository.findByUsername(anyString())).thenReturn(new User());
//        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
//        when(preparedStatement.executeQuery()).thenReturn(resultSet);
//        when(resultSet.next()).thenReturn(true);
//        when(resultSet.getInt(anyInt())).thenReturn(5);
//        Report report = reportService.getReportByUserId();
//        assertEquals(5, report.getCountPost());
//        assertEquals(5, report.getCountComment());
//        assertEquals(5, report.getCountFriend());
//        assertEquals(5, report.getCountLike());
//        verify(userRepository, times(1)).findByUsername(anyString());
//        verify(connection, times(4)).prepareStatement(anyString());
//        verify(preparedStatement, times(4)).setInt(anyInt(), anyInt());
//        verify(preparedStatement, times(4)).executeQuery();
//        verify(resultSet, times(4)).next();
//        verify(resultSet, times(16)).getInt(anyInt());
    }
}