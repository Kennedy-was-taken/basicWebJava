package com.myProgress.basicWeb.Repository.Db2;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.myProgress.basicWeb.Entities.Db2.UserEntity;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
    
    @Query("""
        SELECT u FROM UserEntity u WHERE u.id =:id
    """)
    Optional<UserEntity> findById(int id);

    @Query("""
        SELECT u FROM UserEntity u WHERE u.username =:username
    """)
    Optional<UserEntity> findByUsername(String username);

    @Query("""
        SELECT u.id FROM UserEntity u WHERE u.username =:username
    """)
    Optional<Long> getIdByUsername(String username);

    @Transactional
    @Modifying
    @Query("""
        DELETE FROM UserEntity u WHERE u.username =:username
    """)
    void deleteByUsername(String username);

    @Query("""
        SELECT u.username FROM UserEntity u WHERE u.id =:id
    """)
    Optional<String> getUsernameById(long id);

    @Query("""
        SELECT CASE WHEN COUNT(u.id) > 0 THEN true ELSE false END 
        FROM UserEntity u WHERE u.username = :username
    """)
    Boolean doesExist(String username);
}
