package com.example.project.controller;

import com.example.project.dto.UpdateUser;
import com.example.project.payload.InformationReponse;
import com.example.project.service.InformationUserSerVice;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/infomation")
@RequiredArgsConstructor
public class InfoUserController {

    public final InformationUserSerVice informationUserSerVice;
    @PostMapping
    public ResponseEntity<?> addProfile(@RequestBody UpdateUser updateUser) {
        return informationUserSerVice.addProfile(updateUser);
    }

    @PutMapping("/{id_infomation_user}")
    public ResponseEntity<?> editProfile( @PathVariable Long id_infomation_user ,@RequestBody UpdateUser editUser) {
       return informationUserSerVice.editProfile(editUser, id_infomation_user);
    }

    @GetMapping
    public InformationReponse getUserById(){
        return informationUserSerVice.getUserById();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteInfo() {
        return informationUserSerVice.deleteInfo();
    }

    @GetMapping("image/{id_infomation_user}")
    public ResponseEntity<?> getPostImageById(@PathVariable Long id_infomation_user) {
        Resource imageResource = informationUserSerVice.load(id_infomation_user);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Thay đổi MediaType tùy thuộc vào loại ảnh

        return new ResponseEntity<>(imageResource, headers, HttpStatus.OK);
    }
}
