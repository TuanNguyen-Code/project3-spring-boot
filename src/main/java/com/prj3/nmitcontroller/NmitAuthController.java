package com.prj3.nmitcontroller;

import com.prj3.nmitentity.NmitUser;
import com.prj3.nmitrepository.NmitUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/nmit/auth")
@CrossOrigin(origins = "*")
public class NmitAuthController {

    @Autowired
    private NmitUserRepository userRepo;

    @PostMapping("/nmitlogin")
    public ResponseEntity<?> nmitLogin(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        // 1. Tìm user trong DB
        NmitUser user = userRepo.findByNmitUsername(username);

        // 2. Kiểm tra mật khẩu (So sánh chuỗi đơn giản)
        if (user != null && user.getNmitPassword().equals(password)) {
            // Đăng nhập thành công -> Trả về thông tin user (trừ pass)
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("role", user.getNmitRole());
            response.put("fullName", user.getNmitFullName());
            response.put("userId", user.getNmitId());
            return ResponseEntity.ok(response);
        }

        // Đăng nhập thất bại
        return ResponseEntity.badRequest().body("Sai tài khoản hoặc mật khẩu!");
    }

    // API Đăng ký tài khoản mới
    @PostMapping("/nmitregister")
    public ResponseEntity<?> nmitRegister(@RequestBody Map<String, String> regData) {
        String username = regData.get("username");
        String password = regData.get("password");
        String fullName = regData.get("fullName");

        // 1. Kiểm tra dữ liệu đầu vào
        if (username == null || password == null || fullName == null) {
            return ResponseEntity.badRequest().body("Vui lòng điền đầy đủ thông tin!");
        }

        // 2. Kiểm tra xem tài khoản đã tồn tại chưa
        if (userRepo.findByNmitUsername(username) != null) {
            return ResponseEntity.badRequest().body("Tên đăng nhập đã tồn tại! Vui lòng chọn tên khác.");
        }

        // 3. Tạo user mới
        NmitUser newUser = new NmitUser();
        newUser.setNmitUsername(username);
        newUser.setNmitPassword(password);
        newUser.setNmitFullName(fullName);
        newUser.setNmitRole("USER"); // Mặc định đăng ký là USER thường

        userRepo.save(newUser);

        return ResponseEntity.ok("Đăng ký thành công!");
    }
}