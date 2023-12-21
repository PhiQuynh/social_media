package com.example.project.repository;

import com.example.project.ennity.Friend;
import com.example.project.ennity.FriendshipStatus;
import com.example.project.ennity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Friend findByUserAndFriend(User user, User friend);

    Friend findByUserIdUser(Long id_user );

//    @Query(value = "SELECT  f.id,i.name, i.name_file, i.country, f.id_user, f.id_friend, i.birthday, i.profession, i.id_infomation_user, f.status\n" +
//            "FROM friend as f left join information_user as i on (f.id_friend = i.id_user OR f.id_user = i.id_user) \n" +
//            "WHERE (f.id_user = ? AND f.status = 'ACCEPTED') OR (f.id_friend = ?  AND f.status = 'ACCEPTED')", nativeQuery = true)
//        List<Friend> listFriendAndInfo(Long id_user, Long id_friend);

    @Query(value = "select friend.id, friend.id_user, friend.id_friend, friend.status\n" +
            "from friend\n" +
            "where  (friend.id_user = ? AND friend.status = 'ACCEPTED') OR (friend.id_friend = ?  AND friend.status = 'ACCEPTED')", nativeQuery = true)
    List<Friend> listFriendAndInfo(Long id_user, Long id_friend);

}
