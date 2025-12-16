package com.prj3.nmitrepository;

import com.prj3.nmitentity.NmitReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NmitReviewRepository extends JpaRepository<NmitReview, Long> {
    // Lấy danh sách review của 1 cuốn sách, sắp xếp mới nhất lên đầu
    List<NmitReview> findByNmitBookNmitIdOrderByNmitCreatedAtDesc(Long bookId);
}