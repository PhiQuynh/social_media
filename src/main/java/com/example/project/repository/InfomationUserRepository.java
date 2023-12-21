package com.example.project.repository;

import com.example.project.ennity.InformationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfomationUserRepository extends JpaRepository<InformationUser, Long> {
//    InformationUser findbyIdUser(Long id_user);
    InformationUser findByUserIdUser(Long id_user);

    void deleteByUserIdUser(Long id_user);
}
