package com.prj3.nmitentity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "nmit_borrow_records")
@Data
public class NmitBorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nmit_record_id")
    private Long nmitRecordId;

    @Column(name = "nmit_user_id")
    private Long nmitUserId;

    // Link tới bảng sách để lấy thông tin sách
    @ManyToOne
    @JoinColumn(name = "nmit_book_id")
    private NmitBook nmitBook;

    @Column(name = "nmit_borrow_date")
    private LocalDate nmitBorrowDate;

    @Column(name = "nmit_return_date")
    private LocalDate nmitReturnDate;

    @Column(name = "nmit_status")
    private String nmitStatus; // "BORROWING" hoặc "RETURNED"
}