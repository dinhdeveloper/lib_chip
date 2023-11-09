package unit.chip.lib_unit_chip.public_release


/**
 * Created by DinhTC on 11/7/2023.
 * Skype: 0975469232
 */


enum class NfcError {
    NOT_SUPPORT, //Thiết bị không hỗ trợ NFC
    DISABLE,      //Thiết bị có hỗ trợ NFC nhưng đang tắt
    TAG_INVALID,     // Thẻ không hợp lệ, không đúng định dạng
    DOCUMENT_NUMBER_INVALID,     // Số căn cước phải bao gồm 12 chữ số
    DATE_OF_BIRTH_INVALID,     // Ngày sinh phải gồm 6 chữ số
    DATE_OF_EXPIRY_INVALID,     // Ngày hết hạn phải gồm 6 chữ số
    OPEN_FAILURE,     // Không truy cập được vào thẻ chip NFC
    AUTHENTICATE_FAILURE,     // Không xác thực được thẻ chip (thường do số giấy tờ không đúng với thẻ)
    READ_DATA_FAILURE,     // Có lỗi trong quá trình đọc dữ liệu trên thẻ
    NFC_OPTION_NULL     // Lỗi chung
}