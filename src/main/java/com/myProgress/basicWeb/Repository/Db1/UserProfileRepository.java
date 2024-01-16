package com.myProgress.basicWeb.Repository.Db1;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.myProgress.basicWeb.Entities.Db1.UserProfileEntity;

import jakarta.transaction.Transactional;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long>{
    
    @Query("""
        SELECT u FROM UserProfileEntity u WHERE u.id =:id
    """)
    Optional<UserProfileEntity> findById(long id);

    @Query("""
        SELECT u FROM UserProfileEntity u WHERE u.username =:username
    """)
    Optional<UserProfileEntity> findByUsername(String username);

    @Query("""
        SELECT u FROM UserProfileEntity u WHERE u.id =:id
    """)
    Optional<UserProfileEntity> findByRefId(long id);

    @Query("""
        SELECT u FROM UserProfileEntity u WHERE u.role =:role
    """)
    ArrayList<UserProfileEntity> getAllUsersProfiles(String role);

    @Query("""
        SELECT u FROM UserProfileEntity u WHERE u.role =:role
    """)
    ArrayList<UserProfileEntity> getAllWorkProfiles(String role);

    @Transactional
    @Modifying
    @Query("""
        DELETE FROM UserProfileEntity u WHERE u.accountId =:accountId
    """)
    void deleteByRefId(long accountId);

}
