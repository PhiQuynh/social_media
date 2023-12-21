package com.example.project.controller;

import com.example.project.ennity.FileInfo;
import com.example.project.payload.ReponseMessage;
import com.example.project.service.FileDBServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileController {

        private final FileDBServiceImpl fileDBService;

        @GetMapping("/{filename:.+}")
        public ResponseEntity<Resource> viewImage(@PathVariable String filename) {
            Resource imageResource = fileDBService.load(filename);

            HttpHeaders headers = new HttpHeaders();
            // Thay đổi MediaType tùy thuộc vào loại ảnh
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(imageResource, headers, HttpStatus.OK);
        }
        @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ReponseMessage> uploadFile(@RequestParam("file")MultipartFile file) {
            String message = "";
            try {
                String randomFileName = fileDBService.save(file);
                message = randomFileName;
                return ResponseEntity.status(HttpStatus.OK).body(new ReponseMessage(message));
            } catch (Exception e) {
                message = "Upload ảnh thất bại" ;
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ReponseMessage(message));

            }
        }

}
