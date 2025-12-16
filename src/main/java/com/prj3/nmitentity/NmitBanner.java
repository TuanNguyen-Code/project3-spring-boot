package com.prj3.nmitentity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "nmit_banners")
@Data
public class NmitBanner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nmitId;

    @Column(name = "nmit_title")
    private String nmitTitle;

    @Column(name = "nmit_image_url")
    private String nmitImageUrl;

    @Column(name = "nmit_position")
    private String nmitPosition; // 'MAIN', 'SIDE_TOP', 'SIDE_BOTTOM'

    @Column(name = "nmit_active")
    private boolean nmitActive;
}