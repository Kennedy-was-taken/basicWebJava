package com.myProgress.basicWeb.DTO.RefreshToken;

import lombok.Data;

@Data
public class RefreshTokenDTO {
    
    private String accessToken;
    private String refreshToken;
}
