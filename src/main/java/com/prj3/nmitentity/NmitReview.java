package com.prj3.nmitentity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "nmit_reviews")
@Data
public class NmitReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nmitReviewId;

    @ManyToOne
    @JoinColumn(name = "nmit_user_id")
    private NmitUser nmitUser; // Để lấy tên người bình luận

    @ManyToOne
    @JoinColumn(name = "nmit_book_id")
    private NmitBook nmitBook;

    @Column(name = "nmit_rating")
    private int nmitRating;

    @Column(name = "nmit_comment")
    private String nmitComment;

    @Column(name = "nmit_created_at")
    private LocalDateTime nmitCreatedAt;
}