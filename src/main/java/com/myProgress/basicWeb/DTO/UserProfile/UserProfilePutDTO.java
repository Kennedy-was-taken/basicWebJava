package com.myProgress.basicWeb.DTO.UserProfile;

import lombok.Data;

@Data
public class UserProfilePutDTO {
    
    private String firstName;

    private String middleName;

    private String lastName;

    private int age;
}
