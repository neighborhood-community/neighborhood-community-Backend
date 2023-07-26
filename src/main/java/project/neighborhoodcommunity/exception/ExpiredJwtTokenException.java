package project.neighborhoodcommunity.exception;

import lombok.Getter;
import lombok.Setter;
import project.constant.CommonResponseStatus;

@Getter
@Setter
public class ExpiredJwtTokenException extends RuntimeException {
    private CommonResponseStatus status;

    public ExpiredJwtTokenException(CommonResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
