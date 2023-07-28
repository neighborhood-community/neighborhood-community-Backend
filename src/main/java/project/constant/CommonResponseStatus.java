package project.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CommonResponseStatus {
    SUCCESS("200", "요청에 성공하였습니다."),

    /* ------ JWT error ------- */
    EXPIRED_JWT("JWT100", "만료된 JWT 토큰입니다."),
    UNSUITABLE_JWT("JWT101", "잘못된 JWT 입니다."),
    UNAUTHORIZED("JWT102", "JWT 토큰이 없습니다."),
    NOT_FOUND("JWT103", "값을 찾을 수 없습니다.");

    private final String message;
    private final String code;

    CommonResponseStatus(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
