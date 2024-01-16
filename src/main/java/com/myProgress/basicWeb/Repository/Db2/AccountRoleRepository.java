package com.myProgress.basicWeb.Repository.Db2;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.myProgress.basicWeb.Entities.Db2.AccountRoleEntity;

public interface AccountRoleRepository extends JpaRepository<AccountRoleEntity, Long>{
    

    @Query("""
        SELECT u.roleName FROM AccountRoleEntity u WHERE u.accountRefId =:accountId
    """)
    Optional<String> getRoleNameByRefId(long accountId);

    @Query("""
        SELECT u FROM AccountRoleEntity u WHERE u.accountRefId =:accountId
    """)
    Optional<AccountRoleEntity> getRoleByRefId(long accountId);

    @Query("""
        SELECT u FROM AccountRoleEntity u WHERE u.roleName =:role
    """)
    ArrayList<AccountRoleEntity> getUserRoleNames(String role);

    @Query("""
        SELECT u FROM AccountRoleEntity u WHERE u.roleName =:role
    """)
    ArrayList<AccountRoleEntity> getWorkRoleNames(String role);

}
