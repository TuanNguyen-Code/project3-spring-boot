package com.prj3.nmitrepository;

import com.prj3.nmitentity.NmitBorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NmitBorrowRecordRepository extends JpaRepository<NmitBorrowRecord, Long> {
    // Tìm danh sách sách đang mượn của 1 user
    List<NmitBorrowRecord> findByNmitUserIdOrderByNmitBorrowDateDesc(Long userId);
}