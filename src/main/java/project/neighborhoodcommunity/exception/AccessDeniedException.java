package project.neighborhoodcommunity.exception;

import lombok.Getter;
import lombok.Setter;
import project.constant.CommonResponseStatus;

@Getter
@Setter
public class AccessDeniedException extends RuntimeException {
    private CommonResponseStatus status;

    public AccessDeniedException(CommonResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
