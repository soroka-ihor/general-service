package es.localchat.utils;

import es.local.chat.sharedentities.exception.BaseException;
import es.local.chat.sharedentities.exception.handling.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiError> handleUsernameAlreadyExists(BaseException ex) {
        return new ResponseEntity<>(
                new ApiError(
                        ex.getStatus(),
                        ex
                ), ex.getStatus()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessages = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessages.append(error.getField()).append(": ").append(error.getDefaultMessage());
        });
        return new ResponseEntity<>(
                new ApiError(
                        HttpStatus.BAD_REQUEST,
                        errorMessages.toString(),
                        ex
                ), ex.getStatusCode()
        );
    }
}
