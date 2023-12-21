package com.example.project.ennity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "file")
@Entity
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_file", nullable = false)
    public Long id_file;

    @Column(name = "name_file")
    public String nameFile;

    @Column(name = "url_file")
    public String url_file;

    @OneToOne
    InformationUser informationUser;

    @OneToOne
    Post post;
    public FileInfo(String name_file, String url_file) {
        this.nameFile = name_file;
        this.url_file = url_file;
    }

    public FileInfo() {

    }

}
