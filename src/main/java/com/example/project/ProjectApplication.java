package com.example.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.example.project.ennity")
public class ProjectApplication {
	public static void main(String[] args) {
 		SpringApplication.run(ProjectApplication.class, args);
	}
}
