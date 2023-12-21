package com.example.project.controller;
import com.example.project.ennity.FileInfo;
import com.example.project.payload.ReponseMessage;
import com.example.project.service.FileDBServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {

    @Mock
    FileDBServiceImpl fileDBService;

    @InjectMocks @Spy
    FileController fileController;

    @Test
    void viewImage() {
        String filename = "abc.png";
        Resource resource = mock(Resource.class);
        when(fileDBService.load(filename)).thenReturn(resource);
        ResponseEntity<Resource> response = fileController.viewImage(filename);
        verify(fileDBService, times(1)).load(filename);
        HttpHeaders headers = response.getHeaders();
        MediaType mediaType = headers.getContentType();
        HttpStatus status = (HttpStatus) response.getStatusCode();
        assertEquals(HttpStatus.OK, status);
        assertEquals(MediaType.IMAGE_JPEG, mediaType);
        assertEquals(resource, response.getBody());
    }

    @Test
    void testUploadFile_Success() {
        MultipartFile file = mock(MultipartFile.class);
        String originalFilename = "abc.png";
        String originalFilenames = null;
//        when(file.getOriginalFilename()).thenReturn(originalFilename);
        ResponseEntity<ReponseMessage> response = fileController.uploadFile(file);
        verify(fileDBService, times(1)).save(file);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ReponseMessage responseBody = response.getBody();
        assertEquals(HttpStatus.OK, status);
        assertNotNull(responseBody);
        assertEquals("Upload ảnh thành công :" + originalFilenames, responseBody.getMessage());
    }

    @Test
    void testUploadFile_Err(){
        MultipartFile file = mock(MultipartFile.class);
        String originalFilename = "abc.png";
        doThrow(RuntimeException.class).when(fileDBService).save(file);
        ResponseEntity<ReponseMessage> response = fileController.uploadFile(file);
        verify(fileDBService, times(1)).save(file);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ReponseMessage responseBody = response.getBody();
        assertEquals(HttpStatus.EXPECTATION_FAILED, status);
        assertNotNull(responseBody);
        assertEquals("Upload ảnh thất bại", responseBody.getMessage());
    }
}