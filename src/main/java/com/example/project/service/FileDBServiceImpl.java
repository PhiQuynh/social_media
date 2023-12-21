package com.example.project.service;

import com.example.project.ennity.FileInfo;
import com.example.project.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileDBServiceImpl implements FileDBService{
    private final Path root = Paths.get("uploads");
    private final FileRepository fileRepository;
    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }
    @Override
    public String save(MultipartFile file) {
        // Tạo tên file ngẫu nhiên
        String randomFileName = UUID.randomUUID().toString();
        try {
            // Kiểm tra xem file có tồn tại và có phải là một tệp hình ảnh không
            if (file != null && !file.isEmpty() && isImageFile(file)) {

                // Tạo một đối tượng FileInfo để lưu thông tin về file
                FileInfo fileInfo = new FileInfo();
                fileInfo.setNameFile(randomFileName);

                // Lưu đường dẫn file vào thuộc tính url_file
                fileInfo.setUrl_file(root + "/" + randomFileName);

                // Lưu đối tượng FileInfo vào repository hoặc cơ sở dữ liệu
                fileRepository.save(fileInfo);

                // Sao chép file vào thư mục lưu trữ
                Files.copy(file.getInputStream(), this.root.resolve(randomFileName), StandardCopyOption.REPLACE_EXISTING);
            } else {
                throw new IllegalArgumentException("Invalid image file.");
            }
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
        return randomFileName;
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    boolean isImageFile(MultipartFile file) {
        String[] allowedExtensions = { "jpg", "jpeg", "png", "gif" };
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        return fileExtension != null && Arrays.asList(allowedExtensions).contains(fileExtension.toLowerCase());
    }

}
