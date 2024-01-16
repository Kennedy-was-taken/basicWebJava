package com.myProgress.basicWeb.Repository.Db2;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.myProgress.basicWeb.Entities.Db2.RefreshTokenEntity;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long>{

    @Query("""
        SELECT u.Token FROM RefreshTokenEntity u WHERE u.accountRefId =:accountId
    """)
    Optional<String> getRefreshTokenByRefId(long accountId);

    @Query("""
        SELECT u FROM RefreshTokenEntity u WHERE u.accountRefId =:accountId
    """)
    Optional<RefreshTokenEntity> findByRefId(long accountId);

    @Query("""
        SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END 
        FROM RefreshTokenEntity u WHERE u.accountRefId = :accountId
    """)
    Boolean doesRefreshTokenExist(long accountId);
    
}
