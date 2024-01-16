package com.myProgress.basicWeb.Services.Role;

import java.util.ArrayList;

import com.myProgress.basicWeb.DTO.Role.RoleDTO;
import com.myProgress.basicWeb.Entities.Db2.AccountRoleEntity;
import com.myProgress.basicWeb.Model.ServiceResponseModel;

public interface IAccountRoleService {
    
    void saveNewRole(AccountRoleEntity newRole);

    String getRoleNameByRefId(long id);

    ServiceResponseModel<RoleDTO> updateRole(RoleDTO role);

    String CreateUserRole(long accountId);

    String CreateWorkRole(long accountId, String role);

    ArrayList<AccountRoleEntity> getAllUserRoles();

    ArrayList<AccountRoleEntity> getAllWorkRoles();

}
