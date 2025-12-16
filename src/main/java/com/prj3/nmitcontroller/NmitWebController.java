package com.prj3.nmitcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Lưu ý: Dùng @Controller, KHÔNG dùng @RestController
public class NmitWebController  {

    // Vào trang chủ (http://localhost:8080/) thì hiện file index.html
    @GetMapping("/nmitlib")
    public String homePage() {
        return "index";
    }

    // Vào trang quản trị (http://localhost:8080/admin) thì hiện file admin.html
    @GetMapping("/nmitadmin")
    public String adminPage() {
        return "admin";
    }

    // Trang đăng nhập user
    @GetMapping("/nmitlogin")
    public String loginPage() {
        return "login"; // Trả về file login.html
    }

    // Trang quản lí sách của người dùng
    @GetMapping("/nmitprofile")
    public String profilePage() {
        return "profile"; // Trả về file profile.html
    }

    // Trang đăng kí
    @GetMapping("/nmitregister")
    public String registerPage() {
        return "register"; // Trả về file register.html
    }

    // Trang chi tiết về sách
    @GetMapping("/product-detail")
    public String productDetail() {
        return "product-detail"; // Trả về file product-detail.html
    }
}
