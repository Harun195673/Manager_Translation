package Reentry.first.Exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {



///    Timestamp -> When?
///    Status    -> What type of error?
///    Error     -> Human-readable status name
///    Message   -> What exactly went wrong?
///    Path      -> Where did it happen?
///    Details   -> Additional information




    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                ex.getMessage(),                ///  message of the thrown exception
                HttpStatus.NOT_FOUND.value(),   ///  HTTP error number
                HttpStatus.NOT_FOUND.getReasonPhrase(),   ///  HTTP error in human words
                request.getRequestURI(),               ///  endpoint path
                LocalDateTime.now()
        );


        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }







    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicateNotFound (
            DuplicateResourceException ex,
            HttpServletRequest request
            ){

        ApiError error = new ApiError(
                ex.getMessage(),                ///  message of the thrown exception
                HttpStatus.CONFLICT.value()    ,   ///  HTTP error number
                HttpStatus.CONFLICT.getReasonPhrase(),   ///  HTTP error in human words
                request.getRequestURI(),               ///  endpoint path
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }








    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<ApiError> handleForbiddenOperationNotFound (
            ForbiddenOperationException ex,
            HttpServletRequest request
    ){

        ApiError error = new ApiError(
                ex.getMessage(),                      // message of the thrown exception
                HttpStatus.FORBIDDEN.value(),         // 403
                HttpStatus.FORBIDDEN.getReasonPhrase(), // "Forbidden"
                request.getRequestURI(),              // endpoint path
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);
    }








    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ApiError> handleBusinessValidationNotFound (
            BusinessValidationException ex,
            HttpServletRequest request
    ){

        ApiError error = new ApiError(
                ex.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(error);
    }





    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ApiError> handleInvalidOperationNotFound (
            InvalidOperationException ex,
            HttpServletRequest request
    ){

        ApiError error = new ApiError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }








































}
