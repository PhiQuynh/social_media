package com.example.project.service;

import com.example.project.config.Constants;
import com.example.project.config.jwt.AuthUserDetails;
import com.example.project.dto.UpdateUser;
import com.example.project.ennity.*;
import com.example.project.payload.InformationReponse;
import com.example.project.payload.ResponseSucces;
import com.example.project.repository.FileRepository;
import com.example.project.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.project.repository.InfomationUserRepository;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InformationUserSerViceTest {

    @Mock
    InfomationUserRepository infomationUserRepository;

    @Mock
    FileRepository fileRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UrlResource urlResource;

    @InjectMocks
    InformationUserSerVice userSerVice;

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void addProfile_err() {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setName("abc");
        updateUser.setProfession("abc");
        updateUser.setBirthday("1-1-2000");
        updateUser.setCountry("Viet Nam");
        updateUser.setName_file("jhdf");
        User user = new User();
        user.setUsername("username01@gmail.com");
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        FileInfo fileInfo = new FileInfo();
        when(fileRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(fileInfo));
        ResponseEntity<?> response = userSerVice.addProfile(Mockito.isA(UpdateUser.class));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.MESSAGE_ERR_UPDATE_ADDINFO, ((ResponseSucces) response.getBody()).getMessage());
    }


    @Test
    void editProfile_fail() {
        // Arrange
        Long id_infomation_user = 1L;
        UpdateUser editUser = new UpdateUser();
        // Thiết lập behavior cho mock objects
        Mockito.when(infomationUserRepository.existsById(id_infomation_user)).thenReturn(true);
        ResponseEntity<?> response = userSerVice.editProfile(editUser, id_infomation_user);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.MESSAGE_ERR_UPDATE_EDITINFO, ((ResponseSucces) response.getBody()).getMessage());
    }

    @Test
    void editUser_notExitsInfo(){
        Long id_infomation_user = 1L;
        UpdateUser editUser = new UpdateUser();
        // Thiết lập behavior cho mock objects
        Mockito.when(infomationUserRepository.existsById(id_infomation_user)).thenReturn(false);
        ResponseEntity<?> response = userSerVice.editProfile(editUser, id_infomation_user);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.EXITS_INFO, ((ResponseSucces) response.getBody()).getMessage());
    }

    @Test
    void editUser_success(){
        Long id_infomation_user = 1L;
        UpdateUser editUser = new UpdateUser();
        editUser.setCountry("Viet Nam");
        // Thiết lập behavior cho mock objects
        Mockito.when(infomationUserRepository.existsById(id_infomation_user)).thenReturn(true);
        Mockito.when(infomationUserRepository.findById(id_infomation_user)).thenReturn(Optional.of(new InformationUser()));
        ResponseEntity<?> response = userSerVice.editProfile(editUser, id_infomation_user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
         assertEquals(Constants.SUCCCES_CODE, ((ResponseSucces) response.getBody()).getCode());
         assertEquals(Constants.MESSAGE_SUCCES_UPDATE_EDITINFO, ((ResponseSucces) response.getBody()).getMessage());
        Mockito.verify(infomationUserRepository, Mockito.times(1)).save(any(InformationUser.class));
    }

    @Test
    void getUserById() {
        User user = new User();
        user.setIdUser(1L);
        user.setUsername("abc");
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        Mockito.when(infomationUserRepository.findByUserIdUser(any(Long.class))).thenReturn(Mockito.mock(InformationUser.class));
        InformationReponse response = userSerVice.getUserById();
        assertEquals(1L, response.getId_user());
         assertEquals("abc", response.getUsername());
        Mockito.verify(infomationUserRepository, Mockito.times(1)).findByUserIdUser(any(Long.class));
    }

    @Test
    void deleteInfo_fail() {
        Authentication authentication = Mockito.mock(Authentication.class);
        AuthUserDetails userDetails = Mockito.mock(AuthUserDetails.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(userDetails.getUsername()).thenReturn("testuser");
        Mockito.when(userRepository.findByUsername(any(String.class))).thenReturn(Mockito.mock(User.class));
        Mockito.when(infomationUserRepository.existsById(any(Long.class))).thenReturn(true);
        ResponseEntity<?> response = userSerVice.deleteInfo();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
         assertTrue(response.getBody() instanceof ResponseSucces);
         assertEquals(Constants.USER_ERR_CODE, ((ResponseSucces) response.getBody()).getCode());
         assertEquals(Constants.EXITS_INFO, ((ResponseSucces) response.getBody()).getMessage());
        Mockito.verify(securityContext, Mockito.times(1)).getAuthentication();
        Mockito.verify(authentication, Mockito.times(1)).getPrincipal();
        Mockito.verify(userDetails, Mockito.times(1)).getUsername();
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(any(String.class));
        Mockito.verify(infomationUserRepository, Mockito.times(1)).existsById(any(Long.class));
        Mockito.verify(infomationUserRepository, Mockito.times(0)).deleteByUserIdUser(any(Long.class));
    }

    @Test
    void deleteInfo_success(){
        User user = new User();
        user.setIdUser(1L);
        user.setUsername("abc");
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        Mockito.when(infomationUserRepository.existsById(any(Long.class))).thenReturn(false);
        ResponseEntity<?> response = userSerVice.deleteInfo();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.SUCCCES_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.MESSAGE_DELETE_SUCCES, ((ResponseSucces) response.getBody()).getMessage());
        Mockito.verify(securityContext, Mockito.times(1)).getAuthentication();
        Mockito.verify(authentication, Mockito.times(1)).getPrincipal();
        Mockito.verify(infomationUserRepository, Mockito.times(1)).existsById(Mockito.anyLong());
        Mockito.verify(infomationUserRepository, Mockito.times(1)).deleteByUserIdUser(Mockito.anyLong());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void load_viewImage() throws IOException {
        Long idInfomationUser = 1L;
        String nameFile = "74de0498-76f1-456c-83fe-59d5114da2c3";
        Path root = Paths.get("uploads");
        Path file = root.resolve(nameFile);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setNameFile("74de0498-76f1-456c-83fe-59d5114da2c3");
        Resource resource = Mockito.mock(Resource.class);
        InformationUser informationUser = new InformationUser();
        informationUser.setName_file(fileInfo);
        Optional<InformationUser> optional = Optional.of(informationUser);
        when(infomationUserRepository.findById(idInfomationUser)).thenReturn(optional);
        when(urlResource.exists()).thenReturn(true);
        when(urlResource.isReadable()).thenReturn(true);
        Resource result = userSerVice.load(idInfomationUser);
        assertNotNull(result);
        assertEquals("file:///C:/Users/phithiquynh/Downloads/baitap/uploads/" + nameFile, result.getURI().toString());
        Mockito.verify(infomationUserRepository, Mockito.times(1)).findById(idInfomationUser);
    }

    @Test
    public void testLoad_NullNameFile() {
        Long idInfomationUser = 1L;
        InformationUser informationUser = new InformationUser();
        informationUser.setName_file(null);
        Optional<InformationUser> optional = Optional.of(informationUser);
        when(infomationUserRepository.findById(idInfomationUser)).thenReturn(optional);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userSerVice.load(idInfomationUser));
        assertEquals("Bạn chưa thêm ảnh vào thông tin cá nhân", exception.getMessage());
        Mockito.verify(infomationUserRepository, Mockito.times(1)).findById(idInfomationUser);
        Mockito.verify(urlResource, Mockito.never()).exists();
        Mockito.verify(urlResource, Mockito.never()).isReadable();
    }

    @Test
    public void testLoad_MalformedURLException() throws Exception {
        // Arrange
        Long idInfomationUser = 123L;
        String nameFile = "abc.png";
        Path root = Paths.get("uploads");
        Path file = root.resolve(nameFile);
        Resource resource = Mockito.mock(Resource.class);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setNameFile("abc.png");
        InformationUser informationUser = new InformationUser();
        informationUser.setName_file(fileInfo);
        Optional<InformationUser> optional = Optional.of(informationUser);
        when(infomationUserRepository.findById(idInfomationUser)).thenReturn(optional);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userSerVice.load(idInfomationUser));
        assertEquals("Could not read the file!", exception.getMessage());
        Mockito.verify(infomationUserRepository, Mockito.times(1)).findById(idInfomationUser);
        Mockito.verify(urlResource, Mockito.never()).exists();
        Mockito.verify(urlResource, Mockito.never()).isReadable();
    }

}