package com.myProgress.basicWeb.Services.SecretKey;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

@Service
public class SecretKeyManagerService implements ISecretKeyManagerService{

    private static SecretKey secretKey;

    // Initialize the secret key during application startup
    static {
        secretKey = generateSecretKey();
    }

    @Override
    public SecretKey getSecretKey() {
        
        return secretKey;
    }

     //used to generate Hmac-Sha512 secret key
    private static SecretKey generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating HMAC-SHA512 key", e);
        }
    }
    
}
