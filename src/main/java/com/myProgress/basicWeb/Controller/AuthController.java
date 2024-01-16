package com.myProgress.basicWeb.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myProgress.basicWeb.DTO.Employee.EmployeeDTO;
import com.myProgress.basicWeb.DTO.RefreshToken.RefreshTokenDTO;
import com.myProgress.basicWeb.DTO.User.UserPostDTO;
import com.myProgress.basicWeb.DTO.User.UserGetDTO;
import com.myProgress.basicWeb.Model.ServiceResponseModel;
import com.myProgress.basicWeb.Services.User.IUserService;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    
    @Autowired
    private IUserService _userService;

    @PostMapping("/login")
    public ResponseEntity<ServiceResponseModel<RefreshTokenDTO>> Login(@RequestBody UserPostDTO returnedUser) throws Exception{

        ServiceResponseModel<RefreshTokenDTO> serviceResponse = _userService.Login(returnedUser);

        if(serviceResponse.getData() == null){
            return new ResponseEntity<ServiceResponseModel<RefreshTokenDTO>>(serviceResponse, HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<ServiceResponseModel<RefreshTokenDTO>>(serviceResponse, HttpStatus.OK);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ServiceResponseModel<UserGetDTO>> Register(@RequestBody UserPostDTO newUser){

        ServiceResponseModel<UserGetDTO> serviceResponse = _userService.createUser(newUser);

        if(serviceResponse.getData() == null){
            return new ResponseEntity<ServiceResponseModel<UserGetDTO>>(serviceResponse, HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<ServiceResponseModel<UserGetDTO>>(serviceResponse, HttpStatus.OK);
        }
    }

    @PostMapping("/createEmployee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceResponseModel<UserGetDTO>> RegisterEmployee(@RequestBody EmployeeDTO newUser){

        ServiceResponseModel<UserGetDTO> serviceResponse = _userService.createEmployee(newUser);

        if(serviceResponse.getData() == null){
            return new ResponseEntity<ServiceResponseModel<UserGetDTO>>(serviceResponse, HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<ServiceResponseModel<UserGetDTO>>(serviceResponse, HttpStatus.OK);
        }
    }
}
