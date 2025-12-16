package com.prj3.nmitrepository;

import com.prj3.nmitentity.NmitBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NmitRepository extends JpaRepository<NmitBook, Long> {
    // Hàm tìm kiếm theo kiểu Nmit
    List<NmitBook> findByNmitTitleContainingOrNmitAuthorContaining(String title, String author);
    // Query tìm Top 5 sách được mượn nhiều nhất (Số lượng tổng - Số lượng còn lại)
    @org.springframework.data.jpa.repository.Query(
            "SELECT b FROM NmitBook b ORDER BY (b.nmitQuantity - b.nmitAvailable) DESC"
    )
    List<NmitBook> findTopPopularBooks(org.springframework.data.domain.Pageable pageable);
}