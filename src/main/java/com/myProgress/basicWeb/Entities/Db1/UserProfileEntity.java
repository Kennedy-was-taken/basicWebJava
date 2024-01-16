package com.myProgress.basicWeb.Entities.Db1;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_profile")
@NoArgsConstructor
public class UserProfileEntity {
    
    @Id
    @Column(name = "profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "age")
    private int age;

    @Column(name = "role")
    private String role;

    @Column(name = "account_ref_id")
    private long accountId;
}
