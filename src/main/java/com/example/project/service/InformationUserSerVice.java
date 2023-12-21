package com.example.project.service;

import com.example.project.config.Constants;
import com.example.project.config.jwt.AuthUserDetails;
import com.example.project.dto.UpdateUser;
import com.example.project.ennity.FileInfo;
import com.example.project.ennity.InformationUser;
import com.example.project.payload.InformationReponse;
import com.example.project.payload.ResponseSucces;
import com.example.project.repository.FileRepository;
import com.example.project.repository.InfomationUserRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InformationUserSerVice {

    private final Path root = Paths.get("uploads");
    private final InfomationUserRepository infomationUserRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    @Transactional
    public ResponseEntity<?> addProfile(UpdateUser updateUser) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
            InformationUser informationUser = new InformationUser();
            informationUser.setUser(userRepository.findByUsername(userDetails.getUsername()));
            if(updateUser.getName() != null){
                informationUser.setName(updateUser.getName());
            }
            if(updateUser.getCountry() != null){
                informationUser.setCountry(updateUser.getCountry());
            }
            if(updateUser.getBirthday() != null){
                informationUser.setBirthday(updateUser.getBirthday());
            }
            if(updateUser.getProfession() != null){
                informationUser.setProfession(updateUser.getProfession());
            }
            if (updateUser.getName_file() != null) {
                FileInfo fileInfo = fileRepository.findByNameFile(updateUser.getName_file());
                informationUser.setName_file(fileInfo);
            }
            infomationUserRepository.save(informationUser);
            return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, Constants.MESSAGE_SUCCES_UPDATE_ADDINFO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseSucces(Constants.ERR_CODE, Constants.MESSAGE_ERR_UPDATE_ADDINFO), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<?> editProfile(UpdateUser editUser, Long id_infomation_user) {
        try {
            if(infomationUserRepository.existsById(id_infomation_user)){

            } else {
                return new ResponseEntity<>(new ResponseSucces(Constants.ERR_CODE, Constants.EXITS_INFO), HttpStatus.BAD_REQUEST);
            }
            InformationUser informationUser = infomationUserRepository.findById(id_infomation_user).orElseThrow();
            if(editUser.getName() != null){
                informationUser.setName(editUser.getName());
            }
            if (editUser.getBirthday() != null) {
                informationUser.setBirthday(editUser.getBirthday());
            }
            if (editUser.getCountry() != null) {
                informationUser.setCountry(editUser.getCountry());
            }
            if (editUser.getProfession() != null) {
                informationUser.setProfession(editUser.getProfession());
            }
           if (editUser.getName_file() != null){
               FileInfo fileInfo = fileRepository.findByNameFile(editUser.getName_file());
               informationUser.setName_file(fileInfo);
           }
            infomationUserRepository.save(informationUser);
            return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, Constants.MESSAGE_SUCCES_UPDATE_EDITINFO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseSucces(Constants.ERR_CODE, Constants.MESSAGE_ERR_UPDATE_EDITINFO), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public InformationReponse getUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        return getUser(infomationUserRepository.findByUserIdUser(userRepository.findByUsername(userDetails.getUsername()).getIdUser()));
    }

    private InformationReponse getUser (InformationUser user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        InformationReponse.InformationReponseBuilder builder = InformationReponse.builder();
        if(userDetails.getUser().getIdUser() != null){
            builder.id_user(userDetails.getUser().getIdUser());
        }
        if(userDetails.getUsername() != null){
            builder.username(userDetails.getUsername());
        }
     if(user != null){
         if (user.getName() != null) {
             builder.name(user.getName());
         }
         if(user.getBirthday() != null){
             builder.birthday(user.getBirthday());
         }
         if(user.getCountry() != null){
             builder.country(user.getCountry());
         }
         if(user.getProfession() != null){
             builder.profession(user.getProfession());
         }
         if(user.getName_file() != null){
             builder.name_file(user.getName_file().getNameFile());
         }
        if(user.getId_infomation_user() != null){
            builder.id_infomation_user(user.getId_infomation_user());
        }
     }
        return builder.build();
    }
    @Transactional
    public ResponseEntity<?> deleteInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        if(infomationUserRepository.existsById(userRepository.findByUsername(userDetails.getUsername()).getIdUser())){
            return new ResponseEntity<>(new ResponseSucces(Constants.USER_ERR_CODE, Constants.EXITS_INFO), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        infomationUserRepository.deleteByUserIdUser(userRepository.findByUsername(userDetails.getUsername()).getIdUser());
        return new ResponseEntity<>(new ResponseSucces(Constants.SUCCCES_CODE, Constants.MESSAGE_DELETE_SUCCES),HttpStatus.OK);
    }

    public Resource load(Long id_infomation_user) {
        Optional<InformationUser> optional = infomationUserRepository.findById(id_infomation_user);
        try {
            if(optional.get().getName_file() == null){
                throw new RuntimeException("Bạn chưa thêm ảnh vào thông tin cá nhân");
            }
            if (optional.isPresent()) {
                InformationUser informationUser = optional.get();
                String nameFile = informationUser.getName_file().getNameFile();
                Path file = root.resolve(nameFile);
                Resource resource = new UrlResource(file.toUri());
                if (resource.exists() || resource.isReadable()) {
                    return resource;
                } else {
                    throw new RuntimeException("Could not read the file!");
                }
            }
            return null;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
