package project.neighborhoodcommunity.exception;

import lombok.Getter;
import lombok.Setter;
import project.constant.CommonResponseStatus;

@Getter
@Setter
public class NotFoundException extends RuntimeException {
    private CommonResponseStatus status;

    public NotFoundException(CommonResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
