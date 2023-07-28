package project.constant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {

    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public CommonResponse() {
        this.code = "200";
        this.message = "요청에 성공하였습니다.";
    }

    public CommonResponse(CommonResponseStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public CommonResponse(T result, CommonResponseStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = result;
    }
}
