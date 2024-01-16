package com.myProgress.basicWeb.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myProgress.basicWeb.DTO.UserProfile.UserProfileGetDTO;
import com.myProgress.basicWeb.DTO.UserProfile.UserProfilePutDTO;
import com.myProgress.basicWeb.Model.ServiceResponseModel;
import com.myProgress.basicWeb.Services.UserProfile.IUserProfileService;

@RestController
@RequestMapping("api/account")
public class UserProfileController {
    
    @Autowired
    private IUserProfileService _userProfileService;

    @GetMapping("/profile")
    public ResponseEntity<ServiceResponseModel<UserProfileGetDTO>> Profile(){

        ServiceResponseModel<UserProfileGetDTO> serviceResponse = new ServiceResponseModel<UserProfileGetDTO>();

        serviceResponse.setData(_userProfileService.getProfile());
        serviceResponse.setSuccessful(true);
        serviceResponse.setMessage("Successfully retrieved profile");

        return new ResponseEntity<ServiceResponseModel<UserProfileGetDTO>>(serviceResponse, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ServiceResponseModel<UserProfileGetDTO>> UpdateProfile(@RequestBody UserProfilePutDTO updatedProfile){

        ServiceResponseModel<UserProfileGetDTO> serviceResponse = _userProfileService.updateProfile(updatedProfile);

        return new ResponseEntity<ServiceResponseModel<UserProfileGetDTO>>(serviceResponse, HttpStatus.OK);
    }
}
