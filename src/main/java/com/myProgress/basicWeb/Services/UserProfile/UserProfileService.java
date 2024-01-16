package com.myProgress.basicWeb.Services.UserProfile;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myProgress.basicWeb.DTO.UserProfile.UserProfileGetDTO;
import com.myProgress.basicWeb.DTO.UserProfile.UserProfilePutDTO;
import com.myProgress.basicWeb.Entities.Db1.UserProfileEntity;
import com.myProgress.basicWeb.Model.Roles;
import com.myProgress.basicWeb.Model.ServiceResponseModel;
import com.myProgress.basicWeb.Repository.Db1.UserProfileRepository;
import com.myProgress.basicWeb.Services.Account.IAccountService;
import com.myProgress.basicWeb.Services.User.IUserService;

@Service
public class UserProfileService implements IUserProfileService{

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private IUserService _userService;

    @Autowired
    private IAccountService _accountService;

    @Override
    public ServiceResponseModel<UserProfileGetDTO> updateProfile(UserProfilePutDTO newProfile) {
        
        ServiceResponseModel<UserProfileGetDTO> serviceResponse = new ServiceResponseModel<UserProfileGetDTO>();

        //updates profile and saves it
        var profile = userProfileRepository.findByUsername(_userService.getUsername()).get();
        profile.setFirstName(newProfile.getFirstName());
        profile.setMiddleName(newProfile.getMiddleName()); 
        profile.setLastName(newProfile.getLastName());
        profile.setAge(newProfile.getAge());

        saveProfile(profile);
        _userService.updateUsername(profile.getUsername(), profile.getAccountId());

        //sets the reponse data
        serviceResponse.setData(mapToUserProfile(profile)); //maps the data found in profile and to another object
        serviceResponse.setSuccessful(true);
        serviceResponse.setMessage("Profile Updated");

        return serviceResponse;
    }

  
    @Override
    public void saveProfile(UserProfileEntity newProfile) {
        
        userProfileRepository.save(newProfile);
    }

    @Override //retrieves the users profile
    public UserProfileGetDTO getProfile() {

        String username = _userService.getUsername();
        long accountId = _accountService.getAccountId(_userService.getUserId(username));
        UserProfileEntity user = userProfileRepository.findByRefId(accountId).get();
        return mapToUserProfileWithRole(user);
    }

    @Override //Create default profile
    public void createDefaultProfile(long accountId, String username, String role){

        var profile = new UserProfileEntity();

        profile.setFirstName("user");
        profile.setMiddleName("");
        profile.setLastName("user");
        profile.setUsername(username);
        profile.setAge(0);
        profile.setRole(role);
        profile.setAccountId(accountId);

        saveProfile(profile);
    }

    @Override //retrieves all users profile
    public ArrayList<UserProfileEntity> getAllUserProfiles() {
        
        String role = Roles.USER.toString();
        ArrayList<UserProfileEntity> profiles = userProfileRepository.getAllUsersProfiles(role);

        return profiles;
    }

    @Override //retrieves all workers profile
    public ArrayList<UserProfileEntity> getAllWorkProfiles() {
        
        String role = Roles.EMPLOYEE.toString();
        ArrayList<UserProfileEntity> profiles = userProfileRepository.getAllWorkProfiles(role);

        return profiles;
    }

    @Override
    public void deleteProfile(long accoundId) {
        
        userProfileRepository.deleteByRefId(accoundId);
    }

    @Override
    public void updateRole(long accoundId, String role){
        UserProfileEntity user = userProfileRepository.findByRefId(accoundId).get();
        user.setRole(role);

        saveProfile(user);
    }

    private UserProfileGetDTO mapToUserProfile(UserProfileEntity newProfile) {
        
        var userPost = new UserProfileGetDTO();
        userPost.setFirstName(newProfile.getFirstName());
        userPost.setMiddleName(newProfile.getMiddleName());
        userPost.setLastName(newProfile.getLastName());
        userPost.setUsername(newProfile.getUsername());
        userPost.setRole(newProfile.getRole());
        userPost.setAge(newProfile.getAge());

        return userPost;
    }

    private UserProfileGetDTO mapToUserProfileWithRole(UserProfileEntity newProfile) {
        
        var userPost = new UserProfileGetDTO();
        userPost.setFirstName(newProfile.getFirstName());
        userPost.setMiddleName(newProfile.getMiddleName());
        userPost.setLastName(newProfile.getLastName());
        userPost.setUsername(newProfile.getUsername());
        userPost.setRole(newProfile.getRole());
        userPost.setAge(newProfile.getAge());

        return userPost;
    }

}
