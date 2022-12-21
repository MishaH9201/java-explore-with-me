package ru.practicum.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    public ResponseEntity<ApiError> getErrorResponse(ResponseStatusException e) {
        List<String> stackTrace = new ArrayList<>();
        for (StackTraceElement el : e.getStackTrace()) {
            stackTrace.add(el.toString());
        }
        String[] stackTraceMas = stackTrace.toArray(new String[stackTrace.size()]);
        ApiError apiError = null;
        switch (e.getStatus()) {
            case NOT_FOUND:
                apiError = new ApiError(stackTraceMas,
                        e.getMessage(),
                        "The required object was not found.",
                        HttpStatus.NOT_FOUND.name(),
                        LocalDateTime.now().format(formatter)
                );
                break;
            case FORBIDDEN:
                apiError = new ApiError(stackTraceMas,
                        e.getMessage(),
                        "For the requested operation the conditions are not met.",
                        HttpStatus.FORBIDDEN.name(),
                        LocalDateTime.now().format(formatter)
                );
                break;
            case CONFLICT:
                apiError = new ApiError(stackTraceMas,
                        e.getMessage(),
                        "Integrity constraint has been violated",
                        HttpStatus.CONFLICT.name(),
                        LocalDateTime.now().format(formatter)
                );
                break;
            case BAD_REQUEST:
                apiError = new ApiError(stackTraceMas,
                        e.getMessage(),
                        "Request parameters not valid.",
                        HttpStatus.BAD_REQUEST.name(),
                        LocalDateTime.now().format(formatter)
                );
                break;
            case INTERNAL_SERVER_ERROR:
                apiError = new ApiError(stackTraceMas,
                        e.getMessage(),
                        "Error occurred",
                        HttpStatus.INTERNAL_SERVER_ERROR.name(),
                        LocalDateTime.now().format(formatter)
                );
                break;
        }
        return new ResponseEntity<ApiError>(apiError, e.getStatus());
    }
}
