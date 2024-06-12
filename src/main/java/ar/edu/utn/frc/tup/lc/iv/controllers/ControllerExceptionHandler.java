package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.Generated;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.ErrorApi;
import jakarta.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@Generated
public class ControllerExceptionHandler {
    /**
     * handleError.
     * @param exception
     * @return ResponseEntity<ErrorApi>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApi> handleError(
            final Exception exception) {
        ErrorApi error = buildError(exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * handleError.
     * @param exception
     * @return ResponseEntity<ErrorApi>
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorApi> handleError(
            final IllegalArgumentException exception) {
        ErrorApi error = buildError(exception.getMessage(),
                HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * handleError.
     * @param exception
     * @return ResponseEntity<ErrorApi>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApi> handleError(
            final MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        ErrorApi error = buildError(fieldErrors.get(0).getDefaultMessage(),
                HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * handleError.
     * @param exception
     * @return ResponseEntity<ErrorApi>
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorApi> handleError(
            final ResponseStatusException exception) {
        ErrorApi error = buildError(exception.getReason(),
                HttpStatus.valueOf(exception.getStatusCode().value()));
        return ResponseEntity.status(exception.getStatusCode()).body(error);
    }

    /**
     * handleError.
     * @param exception
     * @return ResponseEntity<ErrorApi>
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorApi> handleError(
            final EntityNotFoundException exception) {
        ErrorApi error = buildError(
                exception.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * handleError.
     *
     * @param message mensaje a mostrar
     * @param status estado del error
     * @return ErrorApi
     */
    private ErrorApi buildError(final String message,
                                final HttpStatus status) {
        return ErrorApi.builder()
                .timestamp(String.valueOf(
                        Timestamp.from(ZonedDateTime.now().toInstant())))
                .error(status.getReasonPhrase())
                .status(status.value())
                .message(message)
                .build();
    }
}
