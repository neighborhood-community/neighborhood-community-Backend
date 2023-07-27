package project.neighborhoodcommunity.exception;

import lombok.Getter;
import lombok.Setter;
import project.constant.CommonResponseStatus;

@Getter
@Setter
public class NotFoundJwtToken extends RuntimeException {
    private CommonResponseStatus status;

    public NotFoundJwtToken(CommonResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
