package com.myProgress.basicWeb.Services.UserProfile;

import java.util.ArrayList;

import com.myProgress.basicWeb.DTO.UserProfile.UserProfileGetDTO;
import com.myProgress.basicWeb.DTO.UserProfile.UserProfilePutDTO;
import com.myProgress.basicWeb.Entities.Db1.UserProfileEntity;
import com.myProgress.basicWeb.Model.ServiceResponseModel;

public interface IUserProfileService {
    
    ServiceResponseModel<UserProfileGetDTO> updateProfile(UserProfilePutDTO newProfile);

    void saveProfile(UserProfileEntity newProfile);

    UserProfileGetDTO getProfile();

    void createDefaultProfile(long accountId, String username, String role);

    ArrayList<UserProfileEntity> getAllUserProfiles();

    ArrayList<UserProfileEntity> getAllWorkProfiles();

    void deleteProfile(long accoundId);

    void updateRole(long accoundId, String role);
    
}
