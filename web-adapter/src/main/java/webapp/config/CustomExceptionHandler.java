/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package webapp.config;

import exception.ConcurrentOperationException;
import exception.InsufficientFundsException;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import webapp.api.ResponseError;

/**
 * Custom exception handler.
 *
 * @since 1.0
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    /**
     * Handle IO failures.
     *
     * @param exception Exception.
     * @return Response entity.
     * @since 1.0
     * @checkstyle NonStaticMethodCheck (4 lines)
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ioFailure(final IOException exception) {
        final ResponseError error = CustomExceptionHandler.handleError(exception);
        return ResponseEntity.status(error.httpStatus()).body(error);
    }

    /**
     * Handle job updates failures.
     *
     * @param exception Exception.
     * @return Response entity.
     * @since 1.0
     * @checkstyle NonStaticMethodCheck (4 lines)
     */
    @ExceptionHandler({InsufficientFundsException.class, ConcurrentOperationException.class})
    public ResponseEntity<?> jobUpdateFailure(final Exception exception) {
        final ResponseError error = CustomExceptionHandler.handleError(exception);
        return ResponseEntity.status(error.httpStatus()).body(error);
    }

    /**
     * Handle error.
     *
     * @param exception Exception.
     * @return Response error.
     */
    private static ResponseError handleError(final Exception exception) {
        CustomExceptionHandler.LOGGER.error(exception.getMessage(), exception);
        return
            new ResponseError(
                HttpStatus.BAD_REQUEST,
                Optional.ofNullable(exception.getMessage()).orElse("")
            );
    }
}
