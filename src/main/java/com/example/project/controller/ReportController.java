package com.example.project.controller;

import com.example.project.dto.Report;
import com.example.project.service.ReportServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequiredArgsConstructor
public class ReportController {

@GetMapping("/api/v1/report")
public String exportStatisticsToExcel(HttpServletResponse response) {
    ReportServiceImpl statisticsCalculator = new ReportServiceImpl();
    Report statistics = statisticsCalculator.getReportByUserId();

     // Thay thế bằng đường dẫn và tên file Excel bạn muốn xuất

    try (Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("Statistics");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Số bài viết ");
        headerRow.createCell(1).setCellValue("Số comment");
        headerRow.createCell(2).setCellValue("Bạn bè");
        headerRow.createCell(3).setCellValue("Số like");

        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(statistics.getCountPost());
        dataRow.createCell(1).setCellValue(statistics.getCountComment());
        dataRow.createCell(2).setCellValue(statistics.getCountFriend());
        dataRow.createCell(3).setCellValue(statistics.getCountLike());

        response.setHeader("Content-Disposition", "attachment; filename=report.xlsx");
        OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
    } catch (IOException e) {
        e.printStackTrace();
        return "Lỗi trong quá trình xuất file Excel.";
    }
    return "Kết quả đã được xuất thành công thành file Excel.";
}
}
