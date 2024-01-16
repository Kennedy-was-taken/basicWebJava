package com.myProgress.basicWeb.Controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myProgress.basicWeb.DTO.Role.RoleDTO;
import com.myProgress.basicWeb.DTO.User.UserGetDTO;
import com.myProgress.basicWeb.DTO.UserInfo.UserInfoDTO;
import com.myProgress.basicWeb.Model.ServiceResponseModel;
import com.myProgress.basicWeb.Services.Account.IAccountService;
import com.myProgress.basicWeb.Services.Role.IAccountRoleService;
import com.myProgress.basicWeb.Services.User.IUserService;

@RestController
@RequestMapping("api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AccountController {
    
    @Autowired
    private IAccountService _accountService;

    @Autowired 
    private IAccountRoleService _accountRoleService;

    @Autowired
    private IUserService _userService;

    @GetMapping("/allUserAccounts")
    public ResponseEntity<ServiceResponseModel<ArrayList<UserInfoDTO>>> getAllUsersAccounts(){

        ServiceResponseModel<ArrayList<UserInfoDTO>> serviceResponse = _accountService.getUserAccounts();

        if(serviceResponse.getData() == null){

            return new ResponseEntity<ServiceResponseModel<ArrayList<UserInfoDTO>>>(serviceResponse, HttpStatus.BAD_REQUEST);
        }
        else{

            return new ResponseEntity<ServiceResponseModel<ArrayList<UserInfoDTO>>>(serviceResponse, HttpStatus.OK);
        }

    }

    @GetMapping("/allWorkAccounts")
    public ResponseEntity<ServiceResponseModel<ArrayList<UserInfoDTO>>> getAllWorkAccounts(){

        ServiceResponseModel<ArrayList<UserInfoDTO>> serviceResponse = _accountService.getWorkAccounts();

        if(serviceResponse.getData() == null){

            return new ResponseEntity<ServiceResponseModel<ArrayList<UserInfoDTO>>>(serviceResponse, HttpStatus.BAD_REQUEST);
        }
        else{

            return new ResponseEntity<ServiceResponseModel<ArrayList<UserInfoDTO>>>(serviceResponse, HttpStatus.OK);
        }
    }

    @PutMapping("/changeRole")
    public ResponseEntity<ServiceResponseModel<RoleDTO>> ChangeUserRole(@RequestBody RoleDTO newRole){

        ServiceResponseModel<RoleDTO> serviceResponse = _accountRoleService.updateRole(newRole);

        if(serviceResponse.getData() == null){

            return new ResponseEntity<ServiceResponseModel<RoleDTO>>(serviceResponse, HttpStatus.BAD_REQUEST);
        }
        else{

            return new ResponseEntity<ServiceResponseModel<RoleDTO>>(serviceResponse, HttpStatus.OK);
        }
    }

    @PostMapping("/deleteAccount")
    public ResponseEntity<ServiceResponseModel<String>> DeleteUser(@RequestBody UserGetDTO user){

        ServiceResponseModel<String> serviceResponse = _userService.deleteUser(user);

        if(serviceResponse.getData() == null){

            return new ResponseEntity<ServiceResponseModel<String>>(serviceResponse, HttpStatus.BAD_REQUEST);
        }
        else{

            return new ResponseEntity<ServiceResponseModel<String>>(serviceResponse, HttpStatus.OK);
        }
    }

}
