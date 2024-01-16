package com.myProgress.basicWeb.Services.RefreshToken;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.myProgress.basicWeb.DTO.RefreshToken.RefreshTokenDTO;
import com.myProgress.basicWeb.Entities.Db2.RefreshTokenEntity;
import com.myProgress.basicWeb.Model.ServiceResponseModel;
import com.myProgress.basicWeb.Repository.Db2.RefreshTokenRepository;
import com.myProgress.basicWeb.Services.CustomUserDetailsService;
import com.myProgress.basicWeb.Services.Account.IAccountService;
import com.myProgress.basicWeb.Services.Token.ITokenUtilService;
import com.myProgress.basicWeb.Services.User.IUserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class RefreshTokenService implements IRefreshTokenService{

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ITokenUtilService tokenUtilService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private IAccountService _accountService;

    @Autowired
    private IUserService _userService;

    @Override
    public String getFreshToken(long accountRefId) {
        
        return refreshTokenRepository.getRefreshTokenByRefId(accountRefId).get();
    }

    @Override
    public void saveToken(RefreshTokenEntity refreshToken) {
        
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public ServiceResponseModel<RefreshTokenDTO> updateRefreshToken(RefreshTokenDTO expiredToken, HttpServletRequest request) {
        
        ServiceResponseModel<RefreshTokenDTO> serviceResponse = new ServiceResponseModel<RefreshTokenDTO>();

        Object getClaimsAttribute = request.getAttribute("claims");

        //checks if the fields are empty
        if(expiredToken.getAccessToken() == null || expiredToken.getRefreshToken() == null){

            serviceResponse.setSuccessful(false);
            serviceResponse.setMessage("Invalid. fields are empty");
            return serviceResponse;
        }

        //checks if getClaimsAttribute is empty
        if(getClaimsAttribute == null){

            serviceResponse.setSuccessful(false);
            serviceResponse.setMessage("Invalid Token");
            return serviceResponse;
        }

        //convert the retrieved attributes into claims
        Claims claims = (Claims) getClaimsAttribute;

        Map<String, Object> newClaims = mapToClaims(claims); //maps the data found in claims and maps it to the HashMap object

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(newClaims.get("sub").toString());

        //retrieves the Refresh tokens information
        var refreshToken = refreshTokenRepository.findByRefId(_accountService.getAccountId(_userService.getUserId(userDetails.getUsername())));

        if(!refreshToken.get().getToken().equals(expiredToken.getRefreshToken())){

            serviceResponse.setSuccessful(false);
            serviceResponse.setMessage("Invalid Token");
            return serviceResponse;
        }

        // RefreshTokenPost object
        var newTokens = new RefreshTokenDTO();
        newTokens.setAccessToken(tokenUtilService.generateTokenWithClaims(newClaims, userDetails.getUsername()));
        newTokens.setRefreshToken(generateRefreshToken());
        
        //sets the new refresh token value
        refreshToken.get().setToken(newTokens.getRefreshToken());

        //updates the refresh token in the database
        saveToken(refreshToken.get());

        serviceResponse.setData(newTokens);
        serviceResponse.setSuccessful(true);
        serviceResponse.setMessage("Successfully regenerated new tokens");
        return serviceResponse;

    }

    @Override
    public String generateRefreshToken() {
        
        SecureRandom random = new SecureRandom();
        var randomNumbers = new byte[32];
        random.nextBytes(randomNumbers);
        String encodedString = Base64.getEncoder().encodeToString(randomNumbers);

        return encodedString;
    }

    //Loops through the claims, stores it in a new HashMap and returns it
    private Map<String, Object> mapToClaims(Claims claim){
        Map<String, Object> claims = new HashMap<>();

        for(Map.Entry<String, Object> entry : claim.entrySet()){
            claims.put(entry.getKey(), entry.getValue());
        }

        return claims;
    }

    @Override
    public Optional<RefreshTokenEntity> getTokenObject(long accountId) {
        
        return refreshTokenRepository.findByRefId(accountId);
    }

    @Override
    public Boolean doesRefreshExist(long accountId) {
        
        return refreshTokenRepository.doesRefreshTokenExist(accountId);
    }
    
}
