package com.prj3.nmitentity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "nmit_users")
@Data
public class NmitUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nmitId;

    @Column(name = "nmit_username")
    private String nmitUsername;

    @Column(name = "nmit_password")
    private String nmitPassword;

    @Column(name = "nmit_full_name")
    private String nmitFullName;

    @Column(name = "nmit_role")
    private String nmitRole;
}