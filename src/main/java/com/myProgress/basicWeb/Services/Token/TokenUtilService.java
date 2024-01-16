package com.myProgress.basicWeb.Services.Token;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.myProgress.basicWeb.Services.SecretKey.ISecretKeyManagerService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class TokenUtilService implements ITokenUtilService{

    @Autowired
    private ISecretKeyManagerService _secretKeyManagerService;

    private final long TOKEN_EXPIRATION_DATE = 5 * 60;

    @Override //generate token
    public String generateToken(UserDetails userDetails) {
        
        Map<String, Object> claims = new HashMap<String, Object>();
        return doGenerate(claims, userDetails.getUsername());
    }

    @Override //generate token
    public String generateTokenWithClaims(Map<String, Object> claims, String username) {

        return doGenerate(claims, username);    
    }

    @Override //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    @Override //extract username
    public String getUsernameFromToken(String token) {
        
        return getClaimFromToken(token, Claims::getSubject);
    }

    //while creating the token -
	//1. Defining claims of the token, like IssuerAt, Expiration and Subject
	//2. Sign the JWT using the HS512 algorithm and secret key.
    private String doGenerate(Map<String, Object> claims, String username) {

        SecretKey secretKey = _secretKeyManagerService.getSecretKey();
        
        String token = Jwts.builder()
                        .claims().add(claims).and()
                        .claims().subject(username).and()
                        .claims().issuedAt(new Date(System.currentTimeMillis())).and()
                        .claims().expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_DATE * 1000)).and()
                        .signWith(secretKey)
                        .compact();
        
        return token;

    }

    //check the expiration date of the token
    private boolean isTokenExpired(String token) {
        final Date isExpired = getExpirationDateFromToken(token);
        return isExpired.before(new Date());
    }

    //gets the expiration date
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    //claim resolver
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver){
        final Claims claim = getAllClaimsFromToken(token);
        return claimResolver.apply(claim);
    }

    //retrieve all claims
    private Claims getAllClaimsFromToken(String token) {
        
        SecretKey secretKey = _secretKeyManagerService.getSecretKey();

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
