package com.myProgress.basicWeb.Services.Account;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myProgress.basicWeb.DTO.UserInfo.UserInfoDTO;
import com.myProgress.basicWeb.Entities.Db1.UserProfileEntity;
import com.myProgress.basicWeb.Entities.Db2.AccountEntity;
import com.myProgress.basicWeb.Entities.Db2.AccountRoleEntity;
import com.myProgress.basicWeb.Model.ServiceResponseModel;
import com.myProgress.basicWeb.Repository.Db2.AccountRepository;
import com.myProgress.basicWeb.Services.Role.IAccountRoleService;
import com.myProgress.basicWeb.Services.UserProfile.IUserProfileService;

@Service
public class AccountService implements IAccountService{

    @Autowired
    private AccountRepository _accountRepository;

    @Autowired
    private IAccountRoleService _accountRoleService;

    @Autowired
    private IUserProfileService _userProfileService;

    @Override
    public long getAccountId(long userId) {
        
        return _accountRepository.getIdByRefId(userId).get();
    }

    @Override
    public void saveNewAccount(AccountEntity newAccount) {
        
        _accountRepository.save(newAccount);
    }

    @Override
    public long getUserRefId(long accountId) {
        
        return _accountRepository.getRefIdById(accountId).get();
    }

    @Override 
    public long CreateAccount(long userId) {

        //Create AccountEntity object and save new account
        var account = new AccountEntity();
        account.setUserRefId(userId);

        saveNewAccount(account);

        return getAccountId(account.getUserRefId());
    }

    @Override
    public ServiceResponseModel<ArrayList<UserInfoDTO>> getUserAccounts() {

        ServiceResponseModel<ArrayList<UserInfoDTO>> serviceResponse = new ServiceResponseModel<ArrayList<UserInfoDTO>>();

        ArrayList<AccountRoleEntity> roles = _accountRoleService.getAllUserRoles();
        ArrayList<UserProfileEntity> profiles = _userProfileService.getAllUserProfiles();
        ArrayList<UserInfoDTO> listOfUsers = new ArrayList<UserInfoDTO>();

        //checks to see if there is data
        if(roles == null || profiles == null){
            serviceResponse.setSuccessful(true);
            serviceResponse.setMessage("No users");
        }

        for(int i = 0; i < profiles.size(); i++){

            //maps the data found in role and profiles and maps it to another object and stores it in the ArrayList
            listOfUsers.add(mapToUserInfo(profiles.get(i).getFirstName(), profiles.get(i).getLastName(), roles.get(i).getRoleName()));
        }

        serviceResponse.setData(listOfUsers);
        serviceResponse.setSuccessful(true);
        serviceResponse.setMessage("Retrieved all users");

        return serviceResponse;
    }

    @Override
    public ServiceResponseModel<ArrayList<UserInfoDTO>> getWorkAccounts() {
        
        ServiceResponseModel<ArrayList<UserInfoDTO>> serviceResponse = new ServiceResponseModel<ArrayList<UserInfoDTO>>();

        ArrayList<AccountRoleEntity> roles = _accountRoleService.getAllWorkRoles();
        ArrayList<UserProfileEntity> profiles = _userProfileService.getAllWorkProfiles();
        ArrayList<UserInfoDTO> listOfUsers = new ArrayList<UserInfoDTO>();

        //checks to see if there is data
        if(roles == null || profiles == null){
            serviceResponse.setSuccessful(true);
            serviceResponse.setMessage("No employees");
        }

        //populates the listof users with data from roles and profiles arraylist
        for(int i = 0; i < profiles.size(); i++){

            //maps the data found in role and profiles and maps it to another object and stores it in the ArrayList
            listOfUsers.add(mapToUserInfo(profiles.get(i).getFirstName(), profiles.get(i).getLastName(), roles.get(i).getRoleName()));
        }

        serviceResponse.setData(listOfUsers);
        serviceResponse.setSuccessful(true);
        serviceResponse.setMessage("Retrieved all employees");

        return serviceResponse;

    }

    private UserInfoDTO mapToUserInfo(String firstName, String lastName, String role) {

        var userInfo = new UserInfoDTO();

        userInfo.setFirstName(firstName);
        userInfo.setLastName(lastName);
        userInfo.setRole(role);

        return userInfo;
    }
    
}
