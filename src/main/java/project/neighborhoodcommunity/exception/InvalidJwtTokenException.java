package project.neighborhoodcommunity.exception;

import lombok.Getter;
import lombok.Setter;
import project.constant.CommonResponseStatus;

@Getter
@Setter
public class InvalidJwtTokenException extends RuntimeException {
    private CommonResponseStatus status;

    public InvalidJwtTokenException(CommonResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
