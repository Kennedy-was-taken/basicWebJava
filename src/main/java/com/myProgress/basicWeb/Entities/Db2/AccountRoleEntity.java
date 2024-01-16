package com.myProgress.basicWeb.Entities.Db2;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "account_role")
@NoArgsConstructor
@AllArgsConstructor
public class AccountRoleEntity {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "role_name")
    private String roleName;

    @Column(name = "account_ref_id")
    private long accountRefId;

    
}
