package project.neighborhoodcommunity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.constant.CommonResponse;
import project.constant.CommonResponseStatus;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ExpiredJwtTokenException.class)
    public CommonResponse<CommonResponseStatus> ExpiredJwtTokenExceptionHandle(ExpiredJwtTokenException exception) {
        return new CommonResponse<>(CommonResponseStatus.EXPIRED_JWT);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidJwtTokenException.class)
    public CommonResponse<CommonResponseStatus> InvalidJwtTokenExceptionHandle(InvalidJwtTokenException exception) {
        return new CommonResponse<>(exception.getStatus());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public CommonResponse<CommonResponseStatus> NotFoundExceptionHandle(NotFoundException exception) {
        return new CommonResponse<>(exception.getStatus());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public CommonResponse<CommonResponseStatus> AccessDeniedExceptionHandle(AccessDeniedException exception) {
        return new CommonResponse<>(exception.getStatus());
    }
}
