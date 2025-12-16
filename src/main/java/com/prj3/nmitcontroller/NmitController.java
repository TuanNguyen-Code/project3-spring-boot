package com.prj3.nmitcontroller;

import com.prj3.nmitentity.NmitBanner;
import com.prj3.nmitentity.NmitBook;
import com.prj3.nmitentity.NmitBorrowRecord;
import com.prj3.nmitservice.NmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/nmit") // Đường dẫn API: http://localhost:8080/api/nmit
@CrossOrigin(origins = "*")
public class NmitController {

    @Autowired
    private NmitService nmitService;

    @GetMapping
    public List<NmitBook> nmitGetAll(@RequestParam(required = false) String keyword) {
        return nmitService.nmitSearchBooks(keyword);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NmitBook> nmitGetOne(@PathVariable Long id) {
        NmitBook book = nmitService.nmitGetBookById(id);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<NmitBook> nmitCreate(@RequestBody NmitBook book) {
        return ResponseEntity.ok(nmitService.nmitSaveBook(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NmitBook> nmitUpdate(@PathVariable Long id, @RequestBody NmitBook book) {
        NmitBook updated = nmitService.nmitUpdateBook(id, book);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> nmitDelete(@PathVariable Long id) {
        nmitService.nmitDeleteBook(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/borrow")
    public ResponseEntity<String> nmitBorrow(@PathVariable Long id) {
        boolean success = nmitService.nmitBorrowBook(id);
        return success ? ResponseEntity.ok("Mượn thành công")
                : ResponseEntity.badRequest().body("Hết sách rồi! :)");
    }
    // 1. API lấy danh sách Banner (theo vị trí)
    @GetMapping("/banners")
    public List<NmitBanner> nmitGetBanners(@RequestParam String position) {
        // position ví dụ: MAIN, SIDE_TOP, SIDE_BOTTOM
        return nmitService.nmitGetBannersByPosition(position);
    }

    // 2. API lấy Top sách đọc nhiều
    @GetMapping("/top-read")
    public List<NmitBook> nmitGetTopRead() {
        return nmitService.nmitGetTopBooks();
    }

    // 1. API Mượn sách (Có User ID)
    @PostMapping("/{bookId}/borrow-user")
    public ResponseEntity<String> nmitBorrowWithUser(@PathVariable Long bookId, @RequestParam Long userId) {
        String result = nmitService.nmitBorrowBookUser(bookId, userId);
        if ("OK".equals(result)) {
            return ResponseEntity.ok("Mượn thành công!");
        }
        return ResponseEntity.badRequest().body(result);
    }

    // 2. API Lấy danh sách sách của tôi
    @GetMapping("/my-books")
    public List<NmitBorrowRecord> nmitGetMyHistory(@RequestParam Long userId) {
        return nmitService.nmitGetMyBooks(userId);
    }

    // 3. API Trả sách
    @PostMapping("/return/{recordId}")
    public ResponseEntity<String> nmitReturnBook(@PathVariable Long recordId) {
        String result = nmitService.nmitReturnBook(recordId);
        if ("OK".equals(result)) {
            return ResponseEntity.ok("Trả sách thành công!");
        }
        return ResponseEntity.badRequest().body(result);
    }

    // API Lấy danh sách User
    @GetMapping("/users")
    public List<com.prj3.nmitentity.NmitUser> nmitGetAllUsers() {
        return nmitService.nmitGetAllUsers();
    }

    // API Cập nhật quyền (Gửi role qua tham số query: ?role=ADMIN)
    @PutMapping("/users/{id}/role")
    public ResponseEntity<String> nmitUpdateRole(@PathVariable Long id, @RequestParam String role) {
        boolean success = nmitService.nmitUpdateUserRole(id, role);
        if (success) return ResponseEntity.ok("Cập nhật quyền thành công!");
        return ResponseEntity.badRequest().body("Không tìm thấy User!");
    }

    // API Xóa User
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> nmitDeleteUser(@PathVariable Long id) {
        boolean success = nmitService.nmitDeleteUser(id);
        if (success) return ResponseEntity.ok("Xóa thành công!");
        return ResponseEntity.badRequest().body("Không thể xóa (User đang mượn sách hoặc lỗi hệ thống)!");
    }
    // API Lấy bình luận
    @GetMapping("/{bookId}/reviews")
    public List<com.prj3.nmitentity.NmitReview> nmitGetReviews(@PathVariable Long bookId) {
        return nmitService.nmitGetReviews(bookId);
    }

    // API Đăng bình luận
    @PostMapping("/{bookId}/reviews")
    public ResponseEntity<String> nmitPostReview(@PathVariable Long bookId,
                                                 @RequestParam Long userId,
                                                 @RequestParam int rating,
                                                 @RequestBody String comment) {
        String res = nmitService.nmitAddReview(bookId, userId, rating, comment);
        return "OK".equals(res) ? ResponseEntity.ok("Đánh giá thành công!") : ResponseEntity.badRequest().body(res);
    }
}