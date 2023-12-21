package com.example.project.controller;

import com.example.project.dto.Report;
import com.example.project.service.ReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    ReportServiceImpl reportService;

    @InjectMocks
    ReportController reportController;

    private MockHttpServletResponse response;

    @Test
    void exportStatisticsToExcel() throws Exception {
        int idUser = 1;
        Report report = new Report();
        report.setCountComment(10);
        report.setCountLike(10);
        report.setCountFriend(10);
        report.setCountPost(10);

//        when(reportService.getReportByUserId()).thenReturn(report);

    }
}