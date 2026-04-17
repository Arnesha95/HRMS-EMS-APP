package com.vvdn.ems_backend.exception;

import com.vvdn.ems_backend.dtos.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;




@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            CredentialsExpiredException.class,
            DisabledException.class,
            ResponseStatusException.class
    })
    public ResponseEntity<ApiError> handleAuthException(Exception e, HttpServletRequest request){
        logger.info("Exception : {}", e.getMessage());

        var apiError = ApiError.of(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiError> handleNullPointer(
            NullPointerException ex,
            HttpServletRequest request
    ) {
        var apiError = ApiError.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Null Pointer",
                "Something went wrong internally",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDatabaseException(
            DataAccessException ex,
            HttpServletRequest request
    ) {
        var apiError = ApiError.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Database Error",
                "Database operation failed",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        var apiError = ApiError.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }



    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidJson(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        var apiError = ApiError.of(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON",
                "Invalid JSON format",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpServletRequest request
    ) {
        var apiError = ApiError.of(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                "API endpoint not found",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }



    @ExceptionHandler(LeaveAlreadyAllocatedException.class)
    public ResponseEntity<ApiError> handleLeaveAlreadyAllocated(
            LeaveAlreadyAllocatedException ex,
            HttpServletRequest request
    ) {
        var apiError = ApiError.of(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(
            BadRequestException ex,
            HttpServletRequest request
    ) {
        logger.warn("Bad Request: {}", ex.getMessage());

        var apiError = ApiError.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(AttendanceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            AttendanceNotFoundException ex,
            HttpServletRequest request
    ) {

        ApiError error = ApiError.of(
                404,
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }



//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handleGenericException(
//            Exception ex,
//            HttpServletRequest request
//    ) {
//        logger.error("Unhandled Exception: ", ex);
//
//        var apiError = ApiError.of(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                "Internal Server Error",
//                ex.getMessage(),
//                request.getRequestURI()
//        );
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
//    }


}
