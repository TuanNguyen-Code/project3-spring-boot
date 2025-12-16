package com.prj3.nmitentity;

import jakarta.persistence.*;
import lombok.Data; // Dùng Lombok cho gọn code

@Entity
@Table(name = "nmit_books")
@Data
public class NmitBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nmit_id")
    private Long nmitId;

    @Column(name = "nmit_title")
    private String nmitTitle;

    @Column(name = "nmit_author")
    private String nmitAuthor;

    @Column(name = "nmit_category")
    private String nmitCategory;

    @Column(name = "nmit_isbn")
    private String nmitIsbn;

    @Column(name = "nmit_quantity")
    private int nmitQuantity;

    @Column(name = "nmit_available")
    private int nmitAvailable;
}
