package project.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {

    private final boolean success;
    private final int code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;
}
