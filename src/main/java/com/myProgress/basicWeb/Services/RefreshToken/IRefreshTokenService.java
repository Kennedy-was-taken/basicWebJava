package com.myProgress.basicWeb.Services.RefreshToken;

import java.util.Optional;

import com.myProgress.basicWeb.DTO.RefreshToken.RefreshTokenDTO;
import com.myProgress.basicWeb.Entities.Db2.RefreshTokenEntity;
import com.myProgress.basicWeb.Model.ServiceResponseModel;

import jakarta.servlet.http.HttpServletRequest;

public interface IRefreshTokenService {
    
    String getFreshToken(long accountRefId);

    void saveToken(RefreshTokenEntity refreshToken);

    ServiceResponseModel<RefreshTokenDTO> updateRefreshToken(RefreshTokenDTO refreshToken, HttpServletRequest request);

    String generateRefreshToken();

    Optional<RefreshTokenEntity> getTokenObject(long accountId);
    
    Boolean doesRefreshExist(long accountId);

}
