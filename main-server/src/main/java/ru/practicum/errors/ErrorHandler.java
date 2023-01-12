package ru.practicum.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<ApiError> getErrorResponse(ResponseStatusException e) {
        String[] stackTraceMas = getStackTrace(e);
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
        log.info(e.getMessage());
        return new ResponseEntity<ApiError>(apiError, e.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> getErrorResponse(Throwable e) {
        ApiError apiError = new ApiError(getStackTrace(e),
                e.getMessage(),
                "Error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                LocalDateTime.now().format(formatter)
        );
        log.info(e.getMessage());
        return new ResponseEntity<ApiError>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> getErrorResponse(MethodArgumentNotValidException e) {
        ApiError apiError = new ApiError(getStackTrace(e),
                e.getMessage(),
                "Request parameters not valid.",
                HttpStatus.BAD_REQUEST.name(),
                LocalDateTime.now().format(formatter)
        );
        log.info(e.getMessage());
        return new ResponseEntity<ApiError>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> getErrorResponse(MissingServletRequestParameterException e) {
        ApiError apiError = new ApiError(getStackTrace(e),
                e.getMessage(),
                "Request parameters not valid.",
                HttpStatus.BAD_REQUEST.name(),
                LocalDateTime.now().format(formatter)
        );
        log.info(e.getMessage());
        return new ResponseEntity<ApiError>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ApiError> getErrorResponse(ConstraintViolationException e) {
        ApiError apiError = new ApiError(getStackTrace(e),
                e.getMessage(),
                "Request parameters not valid.",
                HttpStatus.BAD_REQUEST.name(),
                LocalDateTime.now().format(formatter)
        );
        log.info(e.getMessage());
        return new ResponseEntity<ApiError>(apiError, HttpStatus.BAD_REQUEST);
    }


    private String[] getStackTrace(Throwable e) {
        List<String> stackTrace = new ArrayList<>();
        for (StackTraceElement el : e.getStackTrace()) {
            stackTrace.add(el.toString());
        }
        String[] stackTraceMas = stackTrace.toArray(new String[stackTrace.size()]);
        return stackTraceMas;
    }
}