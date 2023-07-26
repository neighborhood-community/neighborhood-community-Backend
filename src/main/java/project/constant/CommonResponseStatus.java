package project.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CommonResponseStatus {
    SUCCESS(HttpStatus.OK.value(), "요청에 성공하였습니다."),

    /* ------ 400번대 error ------- */
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED.value(), "만료된 JWT 토큰입니다."),
    UNSUITABLE_JWT(HttpStatus.UNAUTHORIZED.value(), "잘못된 JWT 입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "JWT 토큰이 없습니다.");

    private final String message;
    private final int code;

    CommonResponseStatus(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
