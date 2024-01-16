package com.myProgress.basicWeb.Services.Token;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

public interface ITokenUtilService {
    
    String generateToken(UserDetails userDetails);

    String generateTokenWithClaims(Map<String, Object> claims, String username);
    
    Boolean validateToken(String token, UserDetails userDetails);

    String getUsernameFromToken(String token);

}
