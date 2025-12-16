package com.prj3.nmitservice;

import com.prj3.nmitentity.NmitBanner;
import com.prj3.nmitentity.NmitBook;
import com.prj3.nmitentity.NmitBorrowRecord;
import com.prj3.nmitrepository.NmitBannerRepository;
import com.prj3.nmitrepository.NmitBorrowRecordRepository;
import com.prj3.nmitrepository.NmitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NmitService {

    @Autowired
    private NmitRepository nmitRepo;

    // 1. Tìm kiếm / Lấy tất cả
    public List<NmitBook> nmitSearchBooks(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return nmitRepo.findByNmitTitleContainingOrNmitAuthorContaining(keyword, keyword);
        }
        return nmitRepo.findAll();
    }

    // 2. Lấy 1 sách
    public NmitBook nmitGetBookById(Long id) {
        return nmitRepo.findById(id).orElse(null);
    }

    // 3. Thêm mới
    public NmitBook nmitSaveBook(NmitBook book) {
        // Mới tạo thì Available = Quantity
        book.setNmitAvailable(book.getNmitQuantity());
        return nmitRepo.save(book);
    }

    // 4. Xóa
    public void nmitDeleteBook(Long id) {
        nmitRepo.deleteById(id);
    }

    // 5. Cập nhật (Logic sửa lỗi tồn kho)
    public NmitBook nmitUpdateBook(Long id, NmitBook details) {
        NmitBook existing = nmitRepo.findById(id).orElse(null);
        if (existing != null) {
            // Tính chênh lệch số lượng
            int oldQty = existing.getNmitQuantity();
            int newQty = details.getNmitQuantity();
            int diff = newQty - oldQty;

            // Cập nhật thông tin
            existing.setNmitTitle(details.getNmitTitle());
            existing.setNmitAuthor(details.getNmitAuthor());
            existing.setNmitCategory(details.getNmitCategory());
            existing.setNmitIsbn(details.getNmitIsbn());
            existing.setNmitQuantity(newQty);

            // Cập nhật tồn kho mới
            int newAvailable = existing.getNmitAvailable() + diff;
            if (newAvailable < 0) newAvailable = 0;
            existing.setNmitAvailable(newAvailable);

            return nmitRepo.save(existing);
        }
        return null;
    }

    // 6. Mượn sách
    public boolean nmitBorrowBook(Long id) {
        NmitBook book = nmitRepo.findById(id).orElse(null);
        if (book != null && book.getNmitAvailable() > 0) {
            book.setNmitAvailable(book.getNmitAvailable() - 1);
            nmitRepo.save(book);
            return true;
        }
        return false;
    }

    @Autowired
    private NmitBannerRepository bannerRepo; // Inject thêm repo Banner

    // --- PHẦN 1: BANNER ---
    public List<NmitBanner> nmitGetBannersByPosition(String position) {
        return bannerRepo.findByNmitPositionAndNmitActiveTrue(position);
    }

    // --- PHẦN 2: TOP SÁCH ---
    public List<NmitBook> nmitGetTopBooks() {
        // Lấy top 5 cuốn
        return nmitRepo.findTopPopularBooks(org.springframework.data.domain.PageRequest.of(0, 5));
    }

    @Autowired
    private NmitBorrowRecordRepository recordRepo; // Inject thêm cái này

    // LOGIC MƯỢN SÁCH
    public String nmitBorrowBookUser(Long bookId, Long userId) {
        // 1. BẢO MẬT: Kiểm tra xem User ID này có tồn tại thật không?
        // (Tránh trường hợp hacker gửi ID bừa bãi như 99999)
        boolean userExists = userRepo.existsById(userId);
        if (!userExists) {
            return "Lỗi bảo mật: Tài khoản người dùng không tồn tại!";
        }

        // 2. Kiểm tra sách
        NmitBook book = nmitRepo.findById(bookId).orElse(null);
        if (book == null || book.getNmitAvailable() <= 0) {
            return "Hết sách hoặc sách không tồn tại!";
        }

        // 3. Trừ tồn kho
        book.setNmitAvailable(book.getNmitAvailable() - 1);
        nmitRepo.save(book);

        // 4. Tạo phiếu mượn
        NmitBorrowRecord record = new NmitBorrowRecord();
        record.setNmitUserId(userId);
        record.setNmitBook(book);
        record.setNmitBorrowDate(java.time.LocalDate.now());
        record.setNmitStatus("BORROWING");

        recordRepo.save(record);
        return "OK";
    }

    // --- LOGIC MỚI: LẤY DANH SÁCH SÁCH ĐÃ MƯỢN CỦA USER ---
    public List<NmitBorrowRecord> nmitGetMyBooks(Long userId) {
        return recordRepo.findByNmitUserIdOrderByNmitBorrowDateDesc(userId);
    }

    // --- LOGIC MỚI: TRẢ SÁCH ---
    public String nmitReturnBook(Long recordId) {
        NmitBorrowRecord record = recordRepo.findById(recordId).orElse(null);

        if (record != null && "BORROWING".equals(record.getNmitStatus())) {
            // 1. Cập nhật trạng thái phiếu
            record.setNmitStatus("RETURNED");
            record.setNmitReturnDate(java.time.LocalDate.now());
            recordRepo.save(record);

            // 2. Cộng lại tồn kho cho sách
            NmitBook book = record.getNmitBook();
            book.setNmitAvailable(book.getNmitAvailable() + 1);
            nmitRepo.save(book);

            return "OK";
        }
        return "Lỗi: Không tìm thấy phiếu mượn hoặc sách đã trả rồi!";
    }

    @Autowired
    private com.prj3.nmitrepository.NmitUserRepository userRepo; // Inject thêm Repo User

    // --- PHẦN 3: QUẢN LÝ USER (ADMIN) ---

    // 1. Lấy tất cả danh sách User
    public List<com.prj3.nmitentity.NmitUser> nmitGetAllUsers() {
        return userRepo.findAll();
    }

    // 2. Cập nhật quyền (Phân quyền)
    public boolean nmitUpdateUserRole(Long userId, String newRole) {
        com.prj3.nmitentity.NmitUser user = userRepo.findById(userId).orElse(null);
        if (user != null) {
            user.setNmitRole(newRole); // Ví dụ: chuyển từ USER -> ADMIN
            userRepo.save(user);
            return true;
        }
        return false;
    }

    // 3. Xóa User
    public boolean nmitDeleteUser(Long userId) {
        if (userRepo.existsById(userId)) {
            // Lưu ý: Nếu user đang mượn sách thì database có thể chặn xóa (do khóa ngoại).
            // Ở mức độ cơ bản, ta cứ xóa, nếu lỗi thì báo Frontend.
            try {
                userRepo.deleteById(userId);
                return true;
            } catch (Exception e) {
                return false; // Lỗi do ràng buộc dữ liệu
            }
        }
        return false;
    }
    @Autowired
    private com.prj3.nmitrepository.NmitReviewRepository reviewRepo;

    // 1. Lấy danh sách đánh giá của sách
    public List<com.prj3.nmitentity.NmitReview> nmitGetReviews(Long bookId) {
        return reviewRepo.findByNmitBookNmitIdOrderByNmitCreatedAtDesc(bookId);
    }

    // 2. Thêm đánh giá mới
    public String nmitAddReview(Long bookId, Long userId, int rating, String comment) {
        com.prj3.nmitentity.NmitBook book = nmitRepo.findById(bookId).orElse(null);
        com.prj3.nmitentity.NmitUser user = userRepo.findById(userId).orElse(null);

        if (book != null && user != null) {
            com.prj3.nmitentity.NmitReview review = new com.prj3.nmitentity.NmitReview();
            review.setNmitBook(book);
            review.setNmitUser(user);
            review.setNmitRating(rating);
            review.setNmitComment(comment);
            review.setNmitCreatedAt(java.time.LocalDateTime.now());

            reviewRepo.save(review);
            return "OK";
        }
        return "Lỗi: Không tìm thấy sách hoặc người dùng!";
    }
}