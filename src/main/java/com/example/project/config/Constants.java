package com.example.project.config;

public class Constants {
    public static final long JWT_EXPIRATION = 160 * 60 * 60; // 7 day
    public static final String[] ATTRIBUTIES_TO_TOKEN = new String[] {
            "userId",
            "username",
            "role"
    };
    public static final String[] ENDPOINTS_PUBLIC = new String[] {
            "/",
            "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
            "/api/v1/file/**",
            "/abc",
            "/api/v1/user/login/**",
            "/api/v1/user/register/**",
            "/api/v1/user/forgotpassword/**",
            "/api/v1/user/resetpassword/**",
            "/api/v1/user/delete/**",
            "/api/v1/user/otp/**",
            "/error/**"
    };
    public static final String[] ENDPOINTS_WITH_ROLE = new String[] {
            "/user/**"
    };
    public static final boolean IS_CROSS_ALLOW = true;
    public static final String JWT_SECRET = "socialmedia";
    public static final Integer SUCCCES_CODE = 200;
    public  static final String MESSAGE_SUCCES_ADD = "Đăng kí thành công";
    public  static final String MESSAGE_ERR_ADD = "Đăng kí thất bại";
    public static final Integer USER_ERR_CODE = 400;
    public  static final  Integer ERR_CODE = 500;
    public  static  final String MESSAGE_SUCCES_UPDATE_ADDINFO = "Thêm mới thông tin thành công";
    public  static final String MESSAGE_ERR_UPDATE_ADDINFO = "Thêm mới thông tin thất bại";
    public  static  final String MESSAGE_SUCCES_UPDATE_EDITINFO = "Cập nhâp thông tin thành công";
    public  static final String MESSAGE_ERR_UPDATE_EDITINFO = "Cập nhập thông tin thất bại";
    public static final String MESSAGE_SUCCES_ADDPOST = "Thêm mới bài viết thành công";
    public static final String MESSAGE_ERR_ADDPOST = " Thêm mới bài viết thât bại";
    public static final String MESSAGE_ERR_COMMENT = " Thêm mới comment thất bại";
    public static final String MESSAGE_SUCCES_COMMENT = " Thêm mới commnet thành công";
    public static final String MESSAGE_ERR_UPDATE_COMMENT = " Cập nhập comment thất bại";
    public static final String MESSAGE_SUCCES_UPDATE_COMMENT = " Cập nhập commnet thành công";
    public static final String MESSAGE_SUCCES_LIKE = "Bạn đã thích commnet này";
    public static final String MESSAGE_SUCCES_LOVE = "Bạn đã thả tim commnet này";
    public static final String MASSAGE_SUCCES_UNLIKE = "Bạn không thích comment này";
    public static final String MASSAGE_SUCCESS_LIKE_POST = "Bạn đã thích bài viết này";
    public static final String MASSAGE_SUCCESS_LOVE_POST = "Bạn đã thả tim bài viết này";
    public static final String MASSAGE_SUCCESS_UNLIKE_POST = "Bạn không thích bài viết này";
    public static final String ERR_MESSAGE = "Có lỗi rồi kìa! Sửa đi!";
    public static final String MESSAGE_UPDATE_POST_ERR = "Sửa bài viết thất bại";
    public static final String MESSAGE_UPDATE_POST_SUCCES = "Sửa bài viết thành công";
    public static final String MESSAGE_DELETE_SUCCES = "Xóa thành công";
    public static final String MESSAGE_DELETE_ERR= "Xóa thất bại";
    public static final String SEND_REQUEST = "Chờ xác nhận";
    public static final String ACCEPT_FRIEND = "Xác nhận kết bạn thành công";
    public static final String NOT_ACCEPT_FRIEND = "Không chấp nhận lời mời kết bạn";
    public static final String EXITS_POST = "Không tìm thấy id_post";
    public static final String EXITS_USER = "Không tìm thấy id_user";
    public static final String EXITS_INFO = "Không tìm thấy thông tin người dùng này";
    public static final String EXITS_COMMEMT = "Không tìm thấy id_comment";
    public static final String NULL_USERNAME = "Không được để trống username";
    public static final String NULL_PASSWORD = "Không được để trống password";
    public static final String MESS_LENGTH = "Nhập sai số kí tự cho phép";
    public static final String ERR_USERNAME = "Nhập sai định dạng email";
    public static final String USERNAME_EXIST_MESSAGE = "Username này đã tồn tại. Hãy nhập username khác";
    public static final String EXIST_MESSAGE = "Lỗi không thể gửi lời mời kết bạn";
    public static final String ERR_USERNAMES = "Nhập sai username";
    public static final String ERR_FILE = " Không tìm thấy ảnh";
    public static final String NOT_FOUND_POST = " Không tìm thấy post";
    public static final String INFORMATION_USER_NOT_FOUND = "Không tìm thấy thông tin người dùng";
    public static final String USER_NOT_FOUND = "Không tìm thấy người dùng này";
}
