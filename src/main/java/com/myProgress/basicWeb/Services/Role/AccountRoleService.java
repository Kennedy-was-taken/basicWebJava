package com.myProgress.basicWeb.Services.Role;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myProgress.basicWeb.DTO.Role.RoleDTO;
import com.myProgress.basicWeb.Entities.Db2.AccountRoleEntity;
import com.myProgress.basicWeb.Model.Roles;
import com.myProgress.basicWeb.Model.ServiceResponseModel;
import com.myProgress.basicWeb.Repository.Db2.AccountRoleRepository;
import com.myProgress.basicWeb.Services.Account.IAccountService;
import com.myProgress.basicWeb.Services.User.IUserService;
import com.myProgress.basicWeb.Services.UserProfile.IUserProfileService;

@Service
public class AccountRoleService implements IAccountRoleService{

    @Autowired
    private AccountRoleRepository _accountRoleRepository;

    @Autowired
    private IAccountService _accountService;

    @Autowired
    private IUserService _userService;

    @Autowired
    private IUserProfileService _userProfileService;

    @Override
    public void saveNewRole(AccountRoleEntity newRole) {
        
        _accountRoleRepository.save(newRole);

    }

    @Override
    public String getRoleNameByRefId(long id) {

        String roleName = _accountRoleRepository.getRoleNameByRefId(id).get();
        return roleName;
    }

    @Override
    public ServiceResponseModel<RoleDTO> updateRole(RoleDTO updatedRole) {
        
        ServiceResponseModel<RoleDTO> serviceResponse = new ServiceResponseModel<RoleDTO>();

        if(updatedRole.getUsername().isEmpty() || updatedRole.getRoleName().isEmpty()){
            serviceResponse.setSuccessful(false);
            serviceResponse.setMessage("Cannot leave field empty");
            return serviceResponse;
        }

        if(!_userService.doesExist(updatedRole.getUsername())){
            serviceResponse.setSuccessful(false);
            serviceResponse.setMessage("Username doesn't exist");
            return serviceResponse;
        }

        long userId = _userService.getUserId(updatedRole.getUsername());

        Optional<AccountRoleEntity> role = _accountRoleRepository.getRoleByRefId(_accountService.getAccountId(userId));

        if(role.get().getRoleName().equals(updatedRole.getRoleName())){
            serviceResponse.setSuccessful(false);
            serviceResponse.setMessage("User already has the " + updatedRole.getRoleName() + " role");
            return serviceResponse;
        }
        //Updates the old role to the new role
        role.get().setRoleName(updatedRole.getRoleName());

        saveNewRole(role.get());

        _userProfileService.updateRole(role.get().getAccountRefId(), role.get().getRoleName());

        //sets the reponse data
        serviceResponse.setData(mapToRoleDTO(role.get().getRoleName(), updatedRole.getUsername()));
        serviceResponse.setSuccessful(true);
        serviceResponse.setMessage("Role Updated");

        return serviceResponse;
    }

    @Override 
    public String CreateUserRole(long accountId) {
        
        //Create AccountRoleEntity object and save new role
        var accountRole = new AccountRoleEntity();
        accountRole.setRoleName(Roles.USER.toString()); //takes one of the roles from the Roles enum and store it in the AccountRole object
        accountRole.setAccountRefId(accountId);

        saveNewRole(accountRole);

        return accountRole.getRoleName();
    }

    @Override
    public String CreateWorkRole(long accountId, String role) {
        
        //Create AccountRoleEntity object and save new role
        var accountRole = new AccountRoleEntity();
        accountRole.setRoleName(role);
        accountRole.setAccountRefId(accountId);

        saveNewRole(accountRole);

        return accountRole.getRoleName();
    }

    @Override //retrieves all user roles
    public ArrayList<AccountRoleEntity> getAllUserRoles() {

        String role = Roles.USER.toString();
       
        ArrayList<AccountRoleEntity> roles = _accountRoleRepository.getUserRoleNames(role);

        return roles;
    }

    @Override //retrieves all work roles
    public ArrayList<AccountRoleEntity> getAllWorkRoles() {

        String role = Roles.EMPLOYEE.toString();
        
        ArrayList<AccountRoleEntity> roles = _accountRoleRepository.getWorkRoleNames(role);

        return roles;
    }

    private RoleDTO mapToRoleDTO(String role, String username) {

        var newRole = new RoleDTO();
        newRole.setRoleName("ROLE_" + role);
        newRole.setUsername(username);

        return newRole;
    }
    
}
