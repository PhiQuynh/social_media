package com.example.project.ennity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Table(name = "information_user")
@Entity
public class InformationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_infomation_user", nullable = false)
    public Long id_infomation_user;

    @Column(name = "name")
    public String name;

    @Column(name = "birthday")
    public String birthday;

    @Column(name = "profession")
    public String profession;

    @Column(name = "country")
    public String country;

    @OneToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @JsonBackReference
    private User user;

    @OneToOne
    @JoinColumn(name = "name_file", referencedColumnName = "name_file")
    private FileInfo name_file;
}
