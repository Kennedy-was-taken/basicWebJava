package com.myProgress.basicWeb.Entities.Db2;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "refresh_token")
public class RefreshTokenEntity {
    
    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String Token;

    @Column(name = "account_ref_id")
    private long accountRefId;
}
