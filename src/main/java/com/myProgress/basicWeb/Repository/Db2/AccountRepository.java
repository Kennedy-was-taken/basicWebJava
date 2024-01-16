package com.myProgress.basicWeb.Repository.Db2;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.myProgress.basicWeb.Entities.Db2.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, Long>{
    
    @Query("""
        SELECT u.id FROM AccountEntity u WHERE u.userRefId = :userId
    """)
    Optional<Long> getIdByRefId(long userId);

     @Query("""
        SELECT u.userRefId FROM AccountEntity u WHERE u.id = :accountId
    """)
    Optional<Long> getRefIdById(long accountId);

}
