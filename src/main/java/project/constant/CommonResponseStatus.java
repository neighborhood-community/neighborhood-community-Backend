package project.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CommonResponseStatus {
    SUCCESS(HttpStatus.OK.value(), "요청에 성공하였습니다."),

    LOGINFAILE(HttpStatus.UNAUTHORIZED.value(), "회원이 없습니다."),
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
