package com.example.project.service;

import com.example.project.config.Constants;
import com.example.project.config.jwt.AuthUserDetails;
import com.example.project.dto.FriendList;
import com.example.project.ennity.Friend;
import com.example.project.ennity.FriendshipStatus;
import com.example.project.ennity.User;
import com.example.project.payload.ResponseSucces;
import com.example.project.repository.FriendRepository;
import com.example.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendServicesImplTest {

    @Mock
    FriendRepository friendRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    FriendServicesImpl friendServices;

    @Test
    void testAddFriends_success() {
        Long id_friend = 1L;
        User user = new User();
        user.setUsername("abc");
        user.setIdUser(1L);
        User friend = new User();
        Friend existingFriendship = null;
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(friend));
        when(friendRepository.findByUserAndFriend(user, friend )).thenReturn(existingFriendship);
        ResponseEntity<?> response = friendServices.addFriends(id_friend);
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(userRepository, times(1)).findById(id_friend);
        verify(friendRepository).findByUserAndFriend(user, friend);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.SUCCCES_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.SEND_REQUEST, ((ResponseSucces) response.getBody()).getMessage());
    }

    @Test
    void testAddFriend_err(){
        Long id_friend = 1L;
        User user = new User();
        user.setUsername("abc");
        user.setIdUser(1L);
        User friend = new User();
        Friend existingFriendship = null;
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(friend));
        when(friendRepository.findByUserAndFriend(user, friend )).thenReturn(existingFriendship);
        ResponseEntity<?> response = friendServices.addFriends(id_friend);
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(userRepository, times(1)).findById(id_friend);
        verify(friendRepository).findByUserAndFriend(user, friend);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseSucces);
        assertEquals(Constants.SUCCCES_CODE, ((ResponseSucces) response.getBody()).getCode());
        assertEquals(Constants.SEND_REQUEST, ((ResponseSucces) response.getBody()).getMessage());
    }

    @Test
    void acceptFriend_success() {
        Long id_user = 1L;
        User user = new User();
        user.setUsername("abc");
        user.setIdUser(1L);
        User currentUser = new User();
        User friend = new User();
        Friend existingFriendship = new Friend();
        existingFriendship.setStatus(FriendshipStatus.PENDING);
        Friend friendRequest = new Friend();
        friendRequest.setStatus(FriendshipStatus.PENDING);
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(userRepository.findByUsername(anyString())).thenReturn(currentUser);
        when(userRepository.findById(id_user)).thenReturn(Optional.of(friend));
        when(friendRepository.findByUserAndFriend(currentUser, friend)).thenReturn(existingFriendship);
        ResponseEntity<?> response = friendServices.acceptFriend(id_user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void acceptFriend_fail(){
        Long id_user = 1L;
        User user = new User();
        user.setUsername("abc");
        user.setIdUser(1L);
        User currentUser = new User();
        Friend existingFriendship = new Friend();
        existingFriendship.setStatus(FriendshipStatus.PENDING);
        Friend friendRequest = new Friend();
        friendRequest.setStatus(FriendshipStatus.PENDING);
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(userRepository.findByUsername(anyString())).thenReturn(currentUser);
        when(userRepository.findByUsername(anyString())).thenReturn(new User());
        when(userRepository.findById(id_user)).thenReturn(Optional.empty());
        ResponseEntity<?> response = friendServices.acceptFriend(id_user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testNotAcceptFriend_ValidFriendId_ReturnsSuccessResponse() {
        Long userId = 1L;
//        User currentUser = new User();
        User friend = new User();
        User currentUser = new User();
        currentUser.setUsername("abc");
        currentUser.setIdUser(1L);
        Friend existingFriendship = new Friend();
        existingFriendship.setStatus(FriendshipStatus.PENDING);
        Friend friendRequest = new Friend();
        friendRequest.setStatus(FriendshipStatus.PENDING);
        AuthUserDetails userDetails = new AuthUserDetails();
        userDetails.setUser(currentUser);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(userRepository.findByUsername(anyString())).thenReturn(currentUser);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(friend));
//        when(friendRepository.findByUserAndFriend(currentUser, friend)).thenReturn(existingFriendship);
//        when(friendRepository.findByUserAndFriend(friend, currentUser)).thenReturn(friendRequest);
//        when(friendRepository.findByUserIdUser(userId)).thenReturn(new Friend());
        ResponseEntity<?> response = friendServices.notAcceptFriend(userId);
//        verify(SecurityContextHolder.getContext(), times(1)).getAuthentication();
//        verify(authentication, times(1)).getPrincipal();
//        verify(userRepository, times(1)).findByUsername("current_user");
//        verify(userRepository, times(1)).findById(userId);
//        verify(friendRepository, times(1)).findByUserAndFriend(currentUser, friend);
//        verify(friendRepository, times(1)).findByUserAndFriend(friend, currentUser);
//        verify(friendRepository, times(1)).findByUserIdUser(userId);
//        verify(friendRepository, times(1)).save(any(Friend.class));
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() instanceof ResponseSucces;
    }

    @Test
    public void testGetListFriend_ReturnsFriendList() {
//        Authentication authentication = mock(Authentication.class);
//        AuthUserDetails userDetails = mock(AuthUserDetails.class);
//        Friend friend1 = new Friend();
//        friend1.setUser(new User());
//        friend1.setFriend(new User());
//        Friend friend2 = new Friend();
//        friend2.setUser(new User());
//        friend2.setFriend(new User());
//        List<Friend> friendLists = new ArrayList<>();
//        friendLists.add(friend1);
//        friendLists.add(friend2);
//        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
//        when(authentication.getPrincipal()).thenReturn(userDetails);
//        when(userDetails.getUser()).thenReturn(new User());
//        when(friendRepository.listFriendAndInfo(anyLong(), anyLong())).thenReturn(friendLists);
//        ResponseEntity<?> response = friendServices.getListFriend();
//        verify(SecurityContextHolder.getContext(), times(1)).getAuthentication();
//        verify(authentication, times(1)).getPrincipal();
//        verify(userDetails, times(2)).getUser();
//        verify(friendRepository, times(1)).listFriendAndInfo(anyLong(), anyLong());
//        assert response.getStatusCode() == HttpStatus.OK;
//        assert response.getBody() instanceof Map;
//        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
//        assert responseBody.get("code").equals("200");
//        assert responseBody.get("count").equals(2);
//        List<FriendList> friendList = (List<FriendList>) responseBody.get("post");
//        assert friendList.size() == 2;
    }

    @Test
    public void testGetFriend_ReturnsFriendList() {

//        Authentication authentication = mock(Authentication.class);
//        AuthUserDetails userDetails = mock(AuthUserDetails.class);
//        Friend friend = new Friend();
//        friend.setUser(new User());
//        friend.setFriend(new User());
//        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
//        when(authentication.getPrincipal()).thenReturn(userDetails);
//        when(userDetails.getUser()).thenReturn(new User());
//        FriendList result = friendServices.getFriend(friend);
//        verify(SecurityContextHolder.getContext(), times(1)).getAuthentication();
//        verify(authentication, times(1)).getPrincipal();
//        verify(userDetails, times(1)).getUser();
//        assert result != null;
    }
}
