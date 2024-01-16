package com.myProgress.basicWeb.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myProgress.basicWeb.DTO.RefreshToken.RefreshTokenDTO;
import com.myProgress.basicWeb.Model.ServiceResponseModel;
import com.myProgress.basicWeb.Services.RefreshToken.IRefreshTokenService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api")
public class RefreshTokenController {

    @Autowired
    private IRefreshTokenService _refreshTokenService;
    
    @PostMapping("/refreshToken")
    public ResponseEntity<ServiceResponseModel<RefreshTokenDTO>> refreshToken(@RequestBody RefreshTokenDTO expiredToken, HttpServletRequest request){

        ServiceResponseModel<RefreshTokenDTO> serviceResponse = _refreshTokenService.updateRefreshToken(expiredToken, request);

        if(serviceResponse.getData() == null){

            return new ResponseEntity<ServiceResponseModel<RefreshTokenDTO>>(serviceResponse, HttpStatus.BAD_REQUEST);
        }
        else{
            
            return new ResponseEntity<ServiceResponseModel<RefreshTokenDTO>>(serviceResponse, HttpStatus.OK);
        }
    }
}
