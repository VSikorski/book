package nl.rug.API.reviewmanagement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application, providing centralized exception handling across all methods.
 * Annotations:
 * - {@code @ControllerAdvice} enables global advice that can be applied to a wide range of controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions of type {@link MethodArgumentNotValidException} that occur during request processing
     * due to validation failures, typically @Valid annotation throwing errors in request body.
     *
     * @param e the {@link MethodArgumentNotValidException} exception that was thrown during method argument validation
     * @return a {@link ResponseEntity} containing the details of the validation errors and the HTTP status code
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        // Collect all field errors and construct a map of field names to error messages
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        // Return a ResponseEntity with the errors mapped and a BAD_REQUEST status
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
