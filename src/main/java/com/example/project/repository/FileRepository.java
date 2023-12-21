package com.example.project.repository;

import com.example.project.ennity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileInfo, Long> {
    boolean existsByNameFile(String filename);
    FileInfo findByNameFile(String nameFile);
}
