package com.prj3.nmitrepository;

import com.prj3.nmitentity.NmitUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NmitUserRepository extends JpaRepository<NmitUser, Long> {
    // Tìm user theo tên đăng nhập
    NmitUser findByNmitUsername(String username);
}