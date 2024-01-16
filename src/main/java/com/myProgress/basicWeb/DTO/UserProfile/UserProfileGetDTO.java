package com.myProgress.basicWeb.DTO.UserProfile;

import lombok.Data;

@Data
public class UserProfileGetDTO {
    
    private String firstName;

    private String middleName;

    private String lastName;

    private String username;

    private int age;

    private String role;

}
