package com.uservault.config;

import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleException(MethodArgumentNotValidException ex) {

        ErrorDto dto = new ErrorDto(HttpStatus.BAD_REQUEST, "Validation error");

        dto.setDetailedMessages(ex.getBindingResult().getAllErrors().stream()
                .map(err -> err.unwrap(ConstraintViolation.class))
                .map(err -> String.format("'%s' %s", err.getPropertyPath(), err.getMessage()))
                .collect(Collectors.toList()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    public static class ErrorDto {

        private final int status;
        private final String error;
        private final String message;
        private List<String> detailedMessages;

        public int getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }

        public List<String> getDetailedMessages() {
            return detailedMessages;
        }

        public void setDetailedMessages(List<String> detailedMessages) {
            this.detailedMessages = detailedMessages;
        }

        @Override
        public String toString() {
            return "ErrorDto{" +
                    "status=" + status +
                    ", error='" + error + '\'' +
                    ", message='" + message + '\'' +
                    ", detailedMessages=" + detailedMessages +
                    '}';
        }

        public ErrorDto(HttpStatus httpStatus, String message) {
            status = httpStatus.value();
            error = httpStatus.getReasonPhrase();
            this.message = message;
        }
    }
}