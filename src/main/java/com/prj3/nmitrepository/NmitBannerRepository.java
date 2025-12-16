package com.prj3.nmitrepository;

import com.prj3.nmitentity.NmitBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NmitBannerRepository extends JpaRepository<NmitBanner, Long> {
    // Lấy các banner đang hoạt động theo vị trí
    List<NmitBanner> findByNmitPositionAndNmitActiveTrue(String position);
}