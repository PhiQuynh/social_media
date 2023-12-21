package com.example.project.service;

import com.example.project.ennity.FileInfo;
import com.example.project.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileDBServiceImplTest {

    @Mock
    FileRepository fileRepository;

    @InjectMocks
    FileDBServiceImpl fileDBServices;

    @Test
    public void testInit() throws IOException {
        // Tạo mock cho FileRepository
        FileRepository fileRepository = mock(FileRepository.class);
        // Tạo đối tượng FileDBServiceImpl với FileRepository mock
        FileDBServiceImpl fileDBService = new FileDBServiceImpl(fileRepository);
        // Gọi phương thức init()
        fileDBService.init();
    }

    @Test
    void save_success() throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image".getBytes()
        );
        FileDBServiceImpl fileDBService = new FileDBServiceImpl(fileRepository);
        boolean result = fileDBService.isImageFile(multipartFile);
        assertTrue(result);
    }

    @Test
    void save_fail(){
        // Tạo một mock MultipartFile với phần mở rộng tệp không phải là hình ảnh
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file.txt",
                "file.txt",
                "text/plain",
                "test file".getBytes()
        );
        // Tạo đối tượng FileDBServiceImpl (hoặc lớp chứa phương thức isImageFile)
        FileDBServiceImpl fileDBService = new FileDBServiceImpl(fileRepository);
        // Gọi phương thức isImageFile() với mockMultipartFile
        boolean result = fileDBService.isImageFile(mockMultipartFile);
        // Kiểm tra kết quả
        assertFalse(result);
    }

    @Test
    void save_fileNull_fail(){
        // Tạo một đối tượng MultipartFile null
        MockMultipartFile nullMultipartFile = new MockMultipartFile(
                "text/plain",
                "test file".getBytes());
        // Tạo đối tượng FileDBServiceImpl (hoặc lớp chứa phương thức isImageFile)
        FileDBServiceImpl fileDBService = new FileDBServiceImpl(fileRepository);
        // Gọi phương thức isImageFile() với nullMultipartFile
        boolean result = fileDBService.isImageFile(nullMultipartFile);
        // Kiểm tra kết quả
        assertFalse(result);
    }

    @Test
    void load_success() {
//        // Tạo đường dẫn tới tệp tin tồn tại
//        String filename = "example.txt";
//        Path file = Paths.get("C:/storage", filename);
//        URI fileUri = file.toUri();
//
//        try {
//            // Tạo một đối tượng UrlResource thật
//          if(filename != null){
//              UrlResource realResource = new UrlResource(fileUri);
//              // Tạo đối tượng FileDBServiceImpl
//              FileDBServiceImpl fileService = new FileDBServiceImpl(fileRepository);
//              // Giả lập đối tượng UrlResource để trả về từ phương thức load()
//              UrlResource mockResource = mock(UrlResource.class);
//              // Giả lập đường dẫn và tạo tài nguyên từ đường dẫn giả lập
//              Path mockPath = mock(Path.class);
//              Resource result = fileService.load(filename);
//
//              // Kiểm tra kết quả
//              assertNotNull(result);
//              assertEquals(realResource.getClass(), result.getClass());
//              verify(mockResource, times(1)).exists();
//              verify(mockResource, times(1)).isReadable();
//              verify(mockResource, times(1)).toString();
//          } else {
//              throw new RuntimeException("Could not read the file!");
//          }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Test
    void load_fail(){
        // Đường dẫn tới tệp tin không tồn tại
        String filename = "nonexistent.txt";

        // Tạo đối tượng FileStorageService (hoặc lớp chứa phương thức load)
        FileDBServiceImpl fileStorageService = new FileDBServiceImpl(fileRepository);

        // Kiểm tra xem việc gọi phương thức load() có ném ra RuntimeException không
        assertThrows(RuntimeException.class, () -> {
            fileStorageService.load(filename);
        });
    }

}